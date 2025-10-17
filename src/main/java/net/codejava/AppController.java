package net.codejava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class AppController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductMappingRepository productRepository;
    
    @Autowired
    private OpenAIService openAIService;
    
    @Autowired
    private TrackingService trackingService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    // Homepage
    @GetMapping({"/", "/index"})
    public String showHome(Model model) {
        List<ProductMapping> featuredProducts = productRepository.findAll();
        if (featuredProducts.size() > 6) {
            featuredProducts = featuredProducts.subList(0, 6);
        }
        model.addAttribute("products", featuredProducts);
        return "index";
    }
    
    // Registration
    @GetMapping("/register")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new User());
        return "signup_form";
    }
    
    @PostMapping("/process_register")
    public String processRegistration(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setTotalCo2Saved(0.0);
        user.setTotalPlasticSaved(0.0);
        user.setTotalPurchases(0);
        userRepository.save(user);
        return "redirect:/login";
    }
    
    // Login
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
    
    // Dashboard (after login)
    @GetMapping("/dashboard")
    public String showDashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userDetails.getUser();
        Map<String, Object> stats = trackingService.getUserImpactStats(user);
        
        model.addAttribute("user", user);
        model.addAttribute("stats", stats);
        
        return "dashboard";
    }
    
    // Search Page
    @GetMapping("/search")
    public String showSearchPage(Model model) {
        model.addAttribute("categories", List.of("All", "Kitchen", "Bathroom", "Food", "Clothing", "Electronics"));
        return "search";
    }
    
    // Search Processing
    @PostMapping("/search")
    public String searchResult(@RequestParam("keyword") String keyword, Model model) {
        List<ProductMapping> results = productRepository.findByNormalProductContainingIgnoreCase(keyword);
        
        if (results.isEmpty()) {
            results = productRepository.findByEcoAlternativeContainingIgnoreCase(keyword);
        }
        
        model.addAttribute("alternatives", results);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categories", List.of("All", "Kitchen", "Bathroom", "Food", "Clothing", "Electronics"));
        
        return "search";
    }
    
    // View Product Details with AI Analysis
    @GetMapping("/product/{id}")
    public String viewProductDetails(@PathVariable("id") Long id, Model model) {
        ProductMapping product = productRepository.findById(id).orElse(null);
        
        if (product == null) {
            return "redirect:/search";
        }
        
        // Get AI-generated analysis
        Map<String, String> analysis = openAIService.generateProductAnalysis(
            product.getNormalProduct(), 
            product.getEcoAlternative()
        );
        
        product.setAiGeneratedPros(analysis.get("pros"));
        product.setAiGeneratedCons(analysis.get("cons"));
        product.setAiGeneratedSuggestion(analysis.get("suggestion"));
        
        model.addAttribute("product", product);
        
        return "product-detail";
    }
    
    // Record Purchase
    @PostMapping("/purchase/{id}")
    public String recordPurchase(
            @PathVariable("id") Long id,
            @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        
        User user = userDetails.getUser();
        ProductMapping product = productRepository.findById(id).orElse(null);
        
        if (product != null) {
            trackingService.recordPurchase(user, product, quantity);
            model.addAttribute("message", "Purchase tracked! You've saved the environment!");
        }
        
        return "redirect:/dashboard";
    }
}
