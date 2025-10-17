package net.codejava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private ProductMappingRepository productRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            List<ProductMapping> sampleProducts = List.of(
                createProduct(
                    "Plastic Water Bottle",
                    "Stainless Steel Reusable Water Bottle",
                    "Durable, BPA-free stainless steel bottle that keeps drinks cold for 24 hours. Eliminates single-use plastic waste.",
                    5.99,
                    24.99,
                    "https://www.amazon.com/s?k=stainless+steel+water+bottle",
                    "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=400",
                    2.5,
                    0.5,
                    "Kitchen"
                ),
                createProduct(
                    "Disposable Plastic Bags",
                    "Reusable Cotton Shopping Bags",
                    "Set of 5 organic cotton bags perfect for groceries. Machine washable and extremely durable.",
                    0.10,
                    19.99,
                    "https://www.amazon.com/s?k=reusable+cotton+bags",
                    "https://images.unsplash.com/photo-1590736969955-71cc94901144?w=400",
                    1.2,
                    0.8,
                    "Other"
                ),
                createProduct(
                    "Plastic Straws",
                    "Bamboo Reusable Straws",
                    "Pack of 12 eco-friendly bamboo straws with cleaning brush. Biodegradable and sustainable.",
                    0.01,
                    12.99,
                    "https://www.amazon.com/s?k=bamboo+straws",
                    "https://images.unsplash.com/photo-1595278069441-2cf29f8005a4?w=400",
                    0.5,
                    0.3,
                    "Kitchen"
                ),
                createProduct(
                    "Plastic Toothbrush",
                    "Bamboo Toothbrush",
                    "Biodegradable bamboo toothbrush with soft bristles. Eco-friendly alternative to plastic toothbrushes.",
                    2.99,
                    8.99,
                    "https://www.amazon.com/s?k=bamboo+toothbrush",
                    "https://images.unsplash.com/photo-1607613009820-a29f7bb81c04?w=400",
                    0.8,
                    0.4,
                    "Bathroom"
                ),
                createProduct(
                    "Disposable Coffee Cups",
                    "Ceramic Travel Mug",
                    "Insulated ceramic travel mug with leak-proof lid. Keeps beverages hot for hours.",
                    1.50,
                    22.99,
                    "https://www.amazon.com/s?k=ceramic+travel+mug",
                    "https://images.unsplash.com/photo-1517256064527-09c73fc73e38?w=400",
                    3.0,
                    0.6,
                    "Kitchen"
                ),
                createProduct(
                    "Plastic Wrap",
                    "Beeswax Food Wraps",
                    "Set of reusable beeswax wraps for food storage. Natural, washable, and biodegradable.",
                    4.99,
                    18.99,
                    "https://www.amazon.com/s?k=beeswax+food+wraps",
                    "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400",
                    1.5,
                    0.7,
                    "Kitchen"
                )
            );
            
            productRepository.saveAll(sampleProducts);
            System.out.println("✅ Sample eco-friendly products initialized!");
        }
    }
    
    private ProductMapping createProduct(String normalProduct, String ecoAlternative, 
                                        String description, Double normalPrice, Double ecoPrice,
                                        String purchaseLink, String imageUrl,
                                        Double co2Saved, Double plasticSaved, String category) {
        ProductMapping product = new ProductMapping();
        product.setNormalProduct(normalProduct);
        product.setEcoAlternative(ecoAlternative);
        product.setDescription(description);
        product.setNormalPrice(normalPrice);
        product.setEcoPrice(ecoPrice);
        product.setPurchaseLink(purchaseLink);
        product.setImageUrl(imageUrl);
        product.setCo2SavedPerUnit(co2Saved);
        product.setPlasticSavedPerUnit(plasticSaved);
        product.setCategory(category);
        return product;
    }
}
