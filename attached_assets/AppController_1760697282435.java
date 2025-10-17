package net.codejava;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
public class AppController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private ProductMappingRepository productRepo;

    // Homepage
    @GetMapping("/")
    public String showHome(Model model) {
        List<ProductMapping> products = productRepo.findAll();

        // Manually map image paths for local images
        for (ProductMapping product : products) {
            if ("Product 1".equals(product.getNormalProduct())) {
                product.setImageUrl("/images/product1.jpg");
            } else if ("Product 2".equals(product.getNormalProduct())) {
                product.setImageUrl("/images/product2.jpg");
            }
            // Add more conditions as necessary for other products
        }
        model.addAttribute("products", products);
        return "index"; // Returns the homepage template
    }
    @GetMapping("/index")
    public String showIndex() {
        return "index";  // This should correspond to an index.html template in your resources
    }
    // Registration
    @GetMapping("/register")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new User());
        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegistration(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        repo.save(user);
        return "redirect:/login";
    }

    // Login
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // login.html in templates
    }

    // Search Page
    @GetMapping("/search")
    public String showSearchPage() {
        return "search"; // search.html should exist
    }

    // Search Processing
    @PostMapping("/search")
    public String searchResult(@RequestParam("keyword") String keyword, Model model) {
        List<ProductMapping> results = productRepo.findByNormalProductContainingIgnoreCase(keyword);
        model.addAttribute("alternatives", results);
        model.addAttribute("keyword", keyword);
        return "search";
    }

    // View Product Details
    @GetMapping("/product/{id}")
    public String viewProductDetails(@PathVariable("id") Long id, Model model) {
        ProductMapping product = productRepo.findById(id).orElse(null);
        if (product == null) return "redirect:/"; // Redirect to home if not found
        model.addAttribute("product", product);
        return "product-detail"; // Returns the product details template
    }
}
