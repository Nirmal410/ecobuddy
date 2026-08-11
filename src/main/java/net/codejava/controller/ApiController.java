package net.codejava.controller;

import net.codejava.entity.*;
import net.codejava.repository.*;
import net.codejava.security.CustomUserDetails;
import net.codejava.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @Autowired
    private ProductMappingRepository productRepository;

    @Autowired
    private TrackingService trackingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OpenAIService openAIService;

    // ─── AUTH ───

    // POST /api/auth/login — JSON login that sets Spring Security session
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody Map<String, String> body,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(body.get("username"), body.get("password"))
            );
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, request, response);

            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "firstName", userDetails.getUser().getFirstName(),
                "email",     userDetails.getUsername()
            ));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid credentials"));
        }
    }

    // POST /api/auth/register — register new user
    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        System.out.println("REGISTER REQUEST RECEIVED FOR EMAIL: " + email);
        if (userRepository.findByEmail(email) != null) {
            System.out.println("Registration failed: Email already registered: " + email);
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Email already registered"));
        }
        User user = new User();
        user.setFirstName(body.get("firstName"));
        user.setLastName(body.get("lastName"));
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(body.get("password")));
        user.setTotalCo2Saved(0.0);
        user.setTotalPlasticSaved(0.0);
        user.setTotalPurchases(0);
        User savedUser = userRepository.saveAndFlush(user);
        System.out.println("SUCCESSFULLY SAVED USER TO DB: ID=" + savedUser.getId() + ", Email=" + savedUser.getEmail());
        return ResponseEntity.ok(Map.of("success", true, "message", "Account created successfully"));
    }

    // GET /api/auth/status — check if user is logged in
    @GetMapping("/auth/status")
    public ResponseEntity<Map<String, Object>> authStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.ok(Map.of("loggedIn", false));
        }
        return ResponseEntity.ok(Map.of(
            "loggedIn",   true,
            "firstName",  userDetails.getUser().getFirstName(),
            "email",      userDetails.getUsername()
        ));
    }

    // POST /api/auth/logout — log out current user
    @PostMapping("/auth/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("success", true, "message", "Logged out successfully"));
    }

    // GET /api/products — all products (public)
    @GetMapping("/products")
    public ResponseEntity<List<ProductMapping>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    // GET /api/products/{id} — single product with AI analysis
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductMapping> getProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/products/{id}/ai-analyze — trigger OpenAI GPT analysis
    @PostMapping("/products/{id}/ai-analyze")
    public ResponseEntity<ProductMapping> analyzeProductWithAI(@PathVariable Long id) {
        ProductMapping product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, String> aiResult = openAIService.generateProductAnalysis(
            product.getNormalProduct(),
            product.getEcoAlternative()
        );

        if (aiResult.containsKey("pros") && aiResult.get("pros") != null) {
            // Ensure formatting is html list
            String pros = aiResult.get("pros");
            if (!pros.startsWith("<ul>")) {
                pros = "<ul>" + pros.replaceAll("(?m)^[•\\-*]\\s*(.*)$", "<li>$1</li>") + "</ul>";
            }
            product.setAiGeneratedPros(pros);
        }
        if (aiResult.containsKey("cons") && aiResult.get("cons") != null) {
            String cons = aiResult.get("cons");
            if (!cons.startsWith("<ul>")) {
                cons = "<ul>" + cons.replaceAll("(?m)^[•\\-*]\\s*(.*)$", "<li>$1</li>") + "</ul>";
            }
            product.setAiGeneratedCons(cons);
        }
        if (aiResult.containsKey("suggestion") && aiResult.get("suggestion") != null) {
            product.setAiGeneratedSuggestion(aiResult.get("suggestion"));
        }

        ProductMapping updatedProduct = productRepository.save(product);
        return ResponseEntity.ok(updatedProduct);
    }

    // GET /api/search?keyword=... — search (public)
    @GetMapping("/search")
    public ResponseEntity<List<ProductMapping>> search(@RequestParam String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.ok(productRepository.findAll());
        }
        String cleanKw = keyword.trim();
        List<ProductMapping> results = productRepository.searchByKeyword(cleanKw);
        
        // Handle singular/plural mismatch before calling AI (e.g. "razor" vs "razors")
        if (results.isEmpty()) {
            if (cleanKw.endsWith("s") && cleanKw.length() > 3) {
                results = productRepository.searchByKeyword(cleanKw.substring(0, cleanKw.length() - 1));
            } else if (!cleanKw.endsWith("s")) {
                results = productRepository.searchByKeyword(cleanKw + "s");
            }
        }

        // If still empty, use OpenAI to discover product alternative, save to DB, and return
        if (results.isEmpty()) {
            try {
                System.out.println("No DB product found for '" + cleanKw + "'. Generating AI product mapping...");
                ProductMapping newProduct = openAIService.generateNewProductMapping(cleanKw);
                if (newProduct != null) {
                    ProductMapping saved = productRepository.save(newProduct);
                    System.out.println("SUCCESSFULLY SAVED AI PRODUCT TO DB: ID=" + saved.getId() + ", Eco=" + saved.getEcoAlternative());
                    results = List.of(saved);
                }
            } catch (Exception e) {
                System.err.println("Error generating/saving AI product for keyword '" + cleanKw + "': " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        return ResponseEntity.ok(results);
    }

    // GET /api/dashboard — user stats (requires auth)
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            User user = userDetails.getUser();
            User dbUser = userRepository.findById(user.getId()).orElse(user);
            Map<String, Object> stats = trackingService.getUserImpactStats(dbUser);
            stats.put("firstName", dbUser.getFirstName());
            stats.put("email", dbUser.getEmail());
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            System.err.println("Error fetching dashboard stats: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("totalCo2Saved", 0.0);
            fallback.put("totalPlasticSaved", 0.0);
            fallback.put("totalPurchases", 0);
            fallback.put("recentPurchases", List.of());
            fallback.put("firstName", userDetails.getUser() != null ? userDetails.getUser().getFirstName() : "Eco Warrior");
            fallback.put("email", userDetails.getUsername());
            return ResponseEntity.ok(fallback);
        }
    }

    // POST /api/purchase/{id} — record purchase (requires auth)
    @PostMapping("/purchase/{id}")
    public ResponseEntity<Map<String, String>> recordPurchase(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        User user = userDetails.getUser();
        ProductMapping product = productRepository.findById(id).orElse(null);
        if (product != null) {
            trackingService.recordPurchase(user, product, 1);
            return ResponseEntity.ok(Map.of("message", "Purchase tracked successfully!"));
        }
        return ResponseEntity.notFound().build();
    }
}
