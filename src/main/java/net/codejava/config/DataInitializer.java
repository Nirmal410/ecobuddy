package net.codejava.config;

import net.codejava.entity.*;
import net.codejava.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private ProductMappingRepository productRepository;
    
    @Autowired
    private PurchaseHistoryRepository purchaseHistoryRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Delete any unwanted products (Safety Razors, Eco friendly bulbs) and their purchase history references
        List<ProductMapping> unwanted = productRepository.findAll().stream()
            .filter(p -> p.getEcoAlternative() != null && (
                p.getEcoAlternative().toLowerCase().contains("razor") || 
                p.getEcoAlternative().toLowerCase().contains("bulb")
            ))
            .toList();
        if (!unwanted.isEmpty()) {
            for (ProductMapping p : unwanted) {
                List<PurchaseHistory> histories = purchaseHistoryRepository.findAll().stream()
                    .filter(ph -> ph.getProduct() != null && ph.getProduct().getId().equals(p.getId()))
                    .toList();
                if (!histories.isEmpty()) {
                    purchaseHistoryRepository.deleteAll(histories);
                }
                try {
                    productRepository.delete(p);
                } catch (Exception e) {
                    System.err.println("Could not delete product " + p.getEcoAlternative() + ": " + e.getMessage());
                }
            }
            System.out.println("🗑️ Cleaned up unwanted product(s) from database!");
        }

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
                    "Kitchen",
                    "<ul>" +
                    "<li><strong>🌿 Environmental Damage Saved:</strong> Prevents 2.5 kg CO₂ emissions & 0.5 kg plastic waste per year (over 150+ disposable bottles kept out of oceans & landfills).</li>" +
                    "<li><strong>💧 1,200L Water Preserved:</strong> Reusable steel saves 1,200 liters of water required to produce single-use plastic bottles repeatedly.</li>" +
                    "<li><strong>💰 Save $300+/Year:</strong> Stop buying bottled water daily — pays for itself in less than 2 weeks.</li>" +
                    "<li><strong>🛡️ 100% Non-Toxic & Safe:</strong> Food-grade 18/8 stainless steel, completely free of BPA, phthalates, and microplastics.</li>" +
                    "</ul>",
                    "<ul>" +
                    "<li><strong>🚨 450+ Years in Landfills:</strong> Single-use plastic bottles take up to 450 years to break down, contaminating soil and groundwater.</li>" +
                    "<li><strong>🌊 Ocean & Marine Destruction:</strong> Millions of plastic bottles enter ocean gyres annually, choking sea turtles and fish.</li>" +
                    "<li><strong>🏭 Toxic Manufacturing:</strong> Producing plastic bottles consumes 3x more water than they contain and releases heavy greenhouse gases.</li>" +
                    "</ul>",
                    "Switching to this reusable stainless steel bottle is the fastest single step to eliminate daily plastic waste while keeping your water ice-cold for 24 hours!"
                ),
                createProduct(
                    "Disposable Plastic Bags",
                    "Reusable Cotton Shopping Bags",
                    "Set of 5 organic cotton bags perfect for groceries. Machine washable and extremely durable.",
                    0.10,
                    19.99,
                    "https://www.amazon.com/s?k=reusable+cotton+bags",
                    "https://images.unsplash.com/photo-1544816155-12df9643f363?w=400",
                    1.2,
                    0.8,
                    "Kitchen",
                    "<ul>" +
                    "<li><strong>🌿 Environmental Damage Saved:</strong> Prevents 1.2 kg CO₂ & 0.8 kg plastic waste per year (eliminates 500+ disposable bags per bag set).</li>" +
                    "<li><strong>🐢 Wildlife Preservation:</strong> Stops thin plastic films from entering rivers and choking marine life like sea turtles.</li>" +
                    "<li><strong>🛍️ Unmatched Durability:</strong> Holds up to 15kg without tearing or handle failures; machine-washable for years of use.</li>" +
                    "<li><strong>🌱 100% Organic & Biodegradable:</strong> Made from pure organic cotton that naturally degrades without leaving microplastics.</li>" +
                    "</ul>",
                    "<ul>" +
                    "<li><strong>🚨 500+ Years to Degrade:</strong> Single-use plastic bags choke storm drains, rivers, and soil, releasing hazardous toxins.</li>" +
                    "<li><strong>🌬️ Carcinogenic Incineration:</strong> Burning discarded plastic bags releases toxic dioxins and heavy greenhouse gases into the air.</li>" +
                    "<li><strong>🧪 Microplastic Pollution:</strong> Plastic bags shred into invisible microplastic fragments ingested by wildlife and humans.</li>" +
                    "</ul>",
                    "Store 2-3 of these organic cotton bags in your car or backpack — you'll never buy a harmful plastic bag at checkout again!"
                ),
                createProduct(
                    "Plastic Straws",
                    "Bamboo Reusable Straws",
                    "Pack of 12 eco-friendly bamboo straws with cleaning brush. Biodegradable and sustainable.",
                    0.01,
                    12.99,
                    "https://www.amazon.com/s?k=bamboo+straws",
                    "https://images.unsplash.com/photo-1563171522-757f89d8cbe7?w=400",
                    0.5,
                    0.3,
                    "Kitchen",
                    "<ul>" +
                    "<li><strong>🌿 Environmental Damage Saved:</strong> Prevents 0.5 kg CO₂ & 0.3 kg plastic waste per year (replaces 500+ single-use plastic straws).</li>" +
                    "<li><strong>🎋 100% Organic & Soil-Safe:</strong> Naturally composts back into nutrient-rich soil in 90 days with zero toxic residue.</li>" +
                    "<li><strong>🍹 Elevates Every Beverage:</strong> Smooth natural bamboo aesthetic perfect for smoothies, iced coffee, and cocktails.</li>" +
                    "<li><strong>🧽 Includes Pipe Cleaner:</strong> Comes with a dedicated natural-bristle cleaning brush for easy maintenance.</li>" +
                    "</ul>",
                    "<ul>" +
                    "<li><strong>🚨 200 Years in Oceans:</strong> Plastic straws never decompose; over 500 million are discarded daily in landfills and waters.</li>" +
                    "<li><strong>🐢 Top 5 Ocean Debris Killer:</strong> Plastic straws directly injure sea turtles and marine life when ingested.</li>" +
                    "<li><strong>🧪 Chemical Leaching:</strong> Cheap plastic straws release BPA and chemical softeners into hot or acidic beverages.</li>" +
                    "</ul>",
                    "A stylish, eco-conscious swap that protects marine animals while making your everyday drinks look and feel gourmet!"
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
                    "Bathroom",
                    "<ul>" +
                    "<li><strong>🌿 Environmental Damage Saved:</strong> Saves 0.8 kg CO₂ & 0.4 kg plastic per box (eliminates plastic brushes that last centuries).</li>" +
                    "<li><strong>🎋 100% Compostable Moso Bamboo:</strong> Rapidly renewable bamboo handle degrades in home compost within 6 months.</li>" +
                    "<li><strong>🦷 Naturally Antibacterial:</strong> Organic bamboo possesses natural antimicrobial bio-agents resisting mold and bacteria.</li>" +
                    "<li><strong>✨ Premium Dental Care:</strong> Soft charcoal-infused bristles effectively absorb plaque and gently whiten teeth.</li>" +
                    "</ul>",
                    "<ul>" +
                    "<li><strong>🚨 1 Billion Dumped Annually:</strong> Over 1 billion plastic toothbrushes are thrown away each year in North America alone.</li>" +
                    "<li><strong>🧪 500-Year Plastic Lifespan:</strong> Non-recyclable plastic handles sit in landfills for 5 centuries without decomposing.</li>" +
                    "<li><strong>🌊 Leaches Bisphenols:</strong> Weathering plastic toothbrushes release microplastics into coastal ecosystems.</li>" +
                    "</ul>",
                    "Upgrade your morning routine with a sleek, antibacterial bamboo toothbrush that leaves your mouth fresh and the planet clean!"
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
                    "Kitchen",
                    "<ul>" +
                    "<li><strong>🌿 Environmental Damage Saved:</strong> Saves 3.0 kg CO₂ & 0.6 kg plastic waste (replaces 500+ single-use coffee cups per year).</li>" +
                    "<li><strong>🌲 Forest Preservation:</strong> Prevents trees from being chopped down for single-use paper cup manufacturing.</li>" +
                    "<li><strong>☕ Pure Unaltered Taste:</strong> Ceramic interior guarantees no metallic or plastic taste in your artisan coffee or tea.</li>" +
                    "<li><strong>💵 Earn Café Discounts:</strong> Most coffee shops offer 50¢ discounts every time you bring your own travel mug.</li>" +
                    "</ul>",
                    "<ul>" +
                    "<li><strong>🚨 99.75% Cannot Be Recycled:</strong> Paper coffee cups have a polyethylene plastic lining preventing standard recycling.</li>" +
                    "<li><strong>🌲 Deforestation Impact:</strong> Over 20 million trees are cut down every year to produce disposable paper cups.</li>" +
                    "<li><strong>🔥 Chemical Migration:</strong> Hot liquids in paper cups melt microplastic coatings directly into your morning brew.</li>" +
                    "</ul>",
                    "Enjoy hot, fresh coffee without chemical taste or plastic guilt. Pays for itself in café discounts while saving trees!"
                ),
                createProduct(
                    "Plastic Wrap",
                    "Beeswax Food Wraps",
                    "Set of reusable beeswax wraps for food storage. Natural, washable, and biodegradable.",
                    4.99,
                    18.99,
                    "https://www.amazon.com/s?k=beeswax+food+wraps",
                    "https://images.unsplash.com/photo-1605371924599-2d0365da1ae0?w=400",
                    1.5,
                    0.7,
                    "Kitchen",
                    "<ul>" +
                    "<li><strong>🌿 Environmental Damage Saved:</strong> Prevents 1.5 kg CO₂ & 0.7 kg plastic wrap waste per year (replaces 100+ meters of cling film).</li>" +
                    "<li><strong>🥗 Keeps Food Fresh 2x Longer:</strong> Natural breathable antibacterial beeswax & jojoba oil seal food freshness naturally.</li>" +
                    "<li><strong>♻️ Reusable 150+ Times:</strong> Simply wash with cold water and mild soap — molds effortlessly with the heat of your hands.</li>" +
                    "<li><strong>🐝 100% Natural & Compostable:</strong> Made from organic cotton, beeswax, and tree resin that returns safely to earth.</li>" +
                    "</ul>",
                    "<ul>" +
                    "<li><strong>🚨 Non-Recyclable PVC Toxin:</strong> Cling wrap is made of thin PVC/PVDC that clogs recycling equipment and cannot be processed.</li>" +
                    "<li><strong>🧪 Leaches Phthalates:</strong> Plastic wrap transfers endocrine-disrupting chemicals into high-fat foods like cheese and butter.</li>" +
                    "<li><strong> Suffocates Wildlife:</strong> Discarded plastic wrap entangles ocean creatures and strangles land animals.</li>" +
                    "</ul>",
                    "Ditch toxic plastic cling wrap forever! Beeswax wraps keep avocados, cheese, and leftovers fresh while smelling naturally sweet."
                )
            );
            
            productRepository.saveAll(sampleProducts);
            System.out.println("✅ Sample eco-friendly products initialized with damage metrics!");
        } else {
            // Force update all existing products with the rich damage metrics
            List<ProductMapping> existing = productRepository.findAll();
            for (ProductMapping p : existing) {
                if (p.getEcoAlternative().toLowerCase().contains("steel") || p.getEcoAlternative().toLowerCase().contains("water bottle")) {
                    p.setAiGeneratedPros("<ul>" +
                    "<li><strong>🌿 Environmental Damage Saved:</strong> Prevents 2.5 kg CO₂ emissions & 0.5 kg plastic waste per year (over 150+ disposable bottles kept out of oceans & landfills).</li>" +
                    "<li><strong>💧 1,200L Water Preserved:</strong> Reusable steel saves 1,200 liters of water required to produce single-use plastic bottles repeatedly.</li>" +
                    "<li><strong>💰 Save $300+/Year:</strong> Stop buying bottled water daily — pays for itself in less than 2 weeks.</li>" +
                    "<li><strong>🛡️ 100% Non-Toxic & Safe:</strong> Food-grade 18/8 stainless steel, completely free of BPA, phthalates, and microplastics.</li>" +
                    "</ul>");
                    p.setAiGeneratedCons("<ul>" +
                    "<li><strong>🚨 450+ Years in Landfills:</strong> Single-use plastic bottles take up to 450 years to break down, contaminating soil and groundwater.</li>" +
                    "<li><strong>🌊 Ocean & Marine Destruction:</strong> Millions of plastic bottles enter ocean gyres annually, choking sea turtles and fish.</li>" +
                    "<li><strong>🏭 Toxic Manufacturing:</strong> Producing plastic bottles consumes 3x more water than they contain and releases heavy greenhouse gases.</li>" +
                    "</ul>");
                    p.setAiGeneratedSuggestion("Switching to this reusable stainless steel bottle is the fastest single step to eliminate daily plastic waste while keeping your water ice-cold for 24 hours!");
                } else if (p.getEcoAlternative().toLowerCase().contains("cotton") || p.getEcoAlternative().toLowerCase().contains("bag")) {
                    p.setImageUrl("https://images.unsplash.com/photo-1544816155-12df9643f363?w=400");
                    if ("Other".equalsIgnoreCase(p.getCategory()) || p.getCategory() == null) {
                        p.setCategory("Kitchen");
                    }
                    p.setAiGeneratedPros("<ul>" +
                    "<li><strong>🌿 Environmental Damage Saved:</strong> Prevents 1.2 kg CO₂ & 0.8 kg plastic waste per year (eliminates 500+ disposable bags per bag set).</li>" +
                    "<li><strong>🐢 Wildlife Preservation:</strong> Stops thin plastic films from entering rivers and choking marine life like sea turtles.</li>" +
                    "<li><strong>🛍️ Unmatched Durability:</strong> Holds up to 15kg without tearing or handle failures; machine-washable for years of use.</li>" +
                    "<li><strong>🌱 100% Organic & Biodegradable:</strong> Made from pure organic cotton that naturally degrades without leaving microplastics.</li>" +
                    "</ul>");
                    p.setAiGeneratedCons("<ul>" +
                    "<li><strong>🚨 500+ Years to Degrade:</strong> Single-use plastic bags choke storm drains, rivers, and soil, releasing hazardous toxins.</li>" +
                    "<li><strong>🌬️ Carcinogenic Incineration:</strong> Burning discarded plastic bags releases toxic dioxins and heavy greenhouse gases into the air.</li>" +
                    "<li><strong>🧪 Microplastic Pollution:</strong> Plastic bags shred into invisible microplastic fragments ingested by wildlife and humans.</li>" +
                    "</ul>");
                    p.setAiGeneratedSuggestion("Store 2-3 of these organic cotton bags in your car or backpack — you'll never buy a harmful plastic bag at checkout again!");
                } else if (p.getEcoAlternative().toLowerCase().contains("straw")) {
                    p.setImageUrl("https://images.unsplash.com/photo-1563171522-757f89d8cbe7?w=400");
                    p.setAiGeneratedPros("<ul>" +
                    "<li><strong>🌿 Environmental Damage Saved:</strong> Prevents 0.5 kg CO₂ & 0.3 kg plastic waste per year (replaces 500+ single-use plastic straws).</li>" +
                    "<li><strong>🎋 100% Organic & Soil-Safe:</strong> Naturally composts back into nutrient-rich soil in 90 days with zero toxic residue.</li>" +
                    "<li><strong>🍹 Elevates Every Beverage:</strong> Smooth natural bamboo aesthetic perfect for smoothies, iced coffee, and cocktails.</li>" +
                    "<li><strong>🧽 Includes Pipe Cleaner:</strong> Comes with a dedicated natural-bristle cleaning brush for easy maintenance.</li>" +
                    "</ul>");
                    p.setAiGeneratedCons("<ul>" +
                    "<li><strong>🚨 200 Years in Oceans:</strong> Plastic straws never decompose; over 500 million are discarded daily in landfills and waters.</li>" +
                    "<li><strong>🐢 Top 5 Ocean Debris Killer:</strong> Plastic straws directly injure sea turtles and marine life when ingested.</li>" +
                    "<li><strong>🧪 Chemical Leaching:</strong> Cheap plastic straws release BPA and chemical softeners into hot or acidic beverages.</li>" +
                    "</ul>");
                    p.setAiGeneratedSuggestion("A stylish, eco-conscious swap that protects marine animals while making your everyday drinks look and feel gourmet!");
                } else if (p.getEcoAlternative().toLowerCase().contains("cloth") || p.getEcoAlternative().toLowerCase().contains("towel")) {
                    p.setImageUrl("https://images.unsplash.com/photo-1705248382836-3618e25706d0?w=400");
                    p.setAiGeneratedPros("<ul>" +
                    "<li><strong>🌿 Environmental Damage Saved:</strong> Prevents 2.0 kg CO₂ & 1.5 kg paper waste per year.</li>" +
                    "<li><strong>🌱 100% Washable & Reusable:</strong> Soft organic bamboo cloths replace hundreds of single-use paper towel rolls.</li>" +
                    "</ul>");
                } else if (p.getEcoAlternative().toLowerCase().contains("wrap") || p.getEcoAlternative().toLowerCase().contains("beeswax")) {
                    p.setImageUrl("https://images.unsplash.com/photo-1605371924599-2d0365da1ae0?w=400");
                    p.setAiGeneratedPros("<ul>" +
                    "<li><strong>🌿 Environmental Damage Saved:</strong> Prevents 1.5 kg CO₂ & 0.7 kg plastic wrap waste per year.</li>" +
                    "<li><strong>🥗 Natural Food Preservation:</strong> Antibacterial beeswax seals freshness safely without plastic cling film.</li>" +
                    "</ul>");
                } else if (p.getEcoAlternative().toLowerCase().contains("toothbrush")) {
                    p.setAiGeneratedPros("<ul>" +
                    "<li><strong>🌿 Environmental Damage Saved:</strong> Saves 0.8 kg CO₂ & 0.4 kg plastic per box (eliminates plastic brushes that last centuries).</li>" +
                    "<li><strong>🎋 100% Compostable Moso Bamboo:</strong> Rapidly renewable bamboo handle degrades in home compost within 6 months.</li>" +
                    "<li><strong>🦷 Naturally Antibacterial:</strong> Organic bamboo possesses natural antimicrobial bio-agents resisting mold and bacteria.</li>" +
                    "<li><strong>✨ Premium Dental Care:</strong> Soft charcoal-infused bristles effectively absorb plaque and gently whiten teeth.</li>" +
                    "</ul>");
                    p.setAiGeneratedCons("<ul>" +
                    "<li><strong>🚨 1 Billion Dumped Annually:</strong> Over 1 billion plastic toothbrushes are thrown away each year in North America alone.</li>" +
                    "<li><strong>🧪 500-Year Plastic Lifespan:</strong> Non-recyclable plastic handles sit in landfills for 5 centuries without decomposing.</li>" +
                    "<li><strong>🌊 Leaches Bisphenols:</strong> Weathering plastic toothbrushes release microplastics into coastal ecosystems.</li>" +
                    "</ul>");
                    p.setAiGeneratedSuggestion("Upgrade your morning routine with a sleek, antibacterial bamboo toothbrush that leaves your mouth fresh and the planet clean!");
                } else if (p.getEcoAlternative().toLowerCase().contains("mug") || p.getEcoAlternative().toLowerCase().contains("cup")) {
                    p.setAiGeneratedPros("<ul>" +
                    "<li><strong>🌿 Environmental Damage Saved:</strong> Saves 3.0 kg CO₂ & 0.6 kg plastic waste (replaces 500+ single-use coffee cups per year).</li>" +
                    "<li><strong>🌲 Forest Preservation:</strong> Prevents trees from being chopped down for single-use paper cup manufacturing.</li>" +
                    "<li><strong>☕ Pure Unaltered Taste:</strong> Ceramic interior guarantees no metallic or plastic taste in your artisan coffee or tea.</li>" +
                    "<li><strong>💵 Earn Café Discounts:</strong> Most coffee shops offer 50¢ discounts every time you bring your own travel mug.</li>" +
                    "</ul>");
                    p.setAiGeneratedCons("<ul>" +
                    "<li><strong>🚨 99.75% Cannot Be Recycled:</strong> Paper coffee cups have a polyethylene plastic lining preventing standard recycling.</li>" +
                    "<li><strong>🌲 Deforestation Impact:</strong> Over 20 million trees are cut down every year to produce disposable paper cups.</li>" +
                    "<li><strong>🔥 Chemical Migration:</strong> Hot liquids in paper cups melt microplastic coatings directly into your morning brew.</li>" +
                    "</ul>");
                    p.setAiGeneratedSuggestion("Enjoy hot, fresh coffee without chemical taste or plastic guilt. Pays for itself in café discounts while saving trees!");
                } else {
                    p.setAiGeneratedPros("<ul>" +
                    "<li><strong>🌿 Environmental Damage Saved:</strong> Significantly reduces carbon footprint and eliminates plastic waste.</li>" +
                    "<li><strong>♻️ Reusable & Durable:</strong> Built from premium sustainable materials designed to last years.</li>" +
                    "<li><strong>💰 Money Saving Investment:</strong> Replaces endless repurchasing of disposable items.</li>" +
                    "</ul>");
                    p.setAiGeneratedCons("<ul>" +
                    "<li><strong>🚨 Severe Pollution:</strong> Conventional disposable items take centuries to decompose in landfills.</li>" +
                    "<li><strong>🧪 Microplastic & Chemical Risks:</strong> Single-use plastics leach hazardous compounds into nature.</li>" +
                    "</ul>");
                    p.setAiGeneratedSuggestion("A high-impact sustainable choice that delivers immediate environmental savings!");
                }
            }
            productRepository.saveAll(existing);
            System.out.println("✅ Updated existing products with environmental damage metrics!");
        }
    }
    
    private ProductMapping createProduct(String normalProduct, String ecoAlternative, 
                                        String description, Double normalPrice, Double ecoPrice,
                                        String purchaseLink, String imageUrl,
                                        Double co2Saved, Double plasticSaved, String category,
                                        String pros, String cons, String suggestion) {
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
        product.setAiGeneratedPros(pros);
        product.setAiGeneratedCons(cons);
        product.setAiGeneratedSuggestion(suggestion);
        return product;
    }
}
