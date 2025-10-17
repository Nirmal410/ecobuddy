package net.codejava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private ProductMappingRepository productRepository;
    
    @GetMapping("")
    public String showAdminPanel(Model model) {
        List<ProductMapping> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "admin";
    }
    
    @GetMapping("/product/new")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new ProductMapping());
        return "product-form";
    }
    
    @GetMapping("/product/edit/{id}")
    public String showEditProductForm(@PathVariable("id") Long id, Model model) {
        ProductMapping product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/admin";
        }
        model.addAttribute("product", product);
        return "product-form";
    }
    
    @PostMapping("/product/save")
    public String saveProduct(@ModelAttribute ProductMapping product) {
        productRepository.save(product);
        return "redirect:/admin";
    }
    
    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productRepository.deleteById(id);
        return "redirect:/admin";
    }
}
