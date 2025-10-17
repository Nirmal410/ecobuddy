# Eco-Friendly Shopping Assistant

## Overview
A Spring Boot web application that helps users discover eco-friendly product alternatives. The app provides AI-powered comparisons using ChatGPT, tracks environmental impact, and offers direct purchase links to sustainable products.

## Current Status
✅ **Application is running successfully on port 5000**  
✅ Database connected and initialized with sample products  
✅ Core features implemented and functional

## Features Implemented

### 1. Product Comparison System
- Compare normal products with eco-friendly alternatives
- View detailed product information (price, description, environmental impact)
- Search functionality to find products
- Category filtering (Kitchen, Bathroom, Food, Clothing, Electronics)

### 2. ChatGPT AI Integration
- AI-generated pros/cons analysis for eco-friendly vs normal products
- Personalized suggestions powered by GPT-4
- **Note**: Requires OPENAI_API_KEY environment variable to be set

### 3. Environmental Impact Tracking
- Track CO2 and plastic saved per purchase
- User dashboard showing cumulative environmental impact
- Purchase history with detailed savings metrics
- Chart.js visualizations for impact data

### 4. User Authentication
- Spring Security with BCrypt password hashing
- User registration and login
- Protected routes and session management

### 5. Admin Panel
- Add, edit, and delete product mappings
- Manage product database
- Track product inventory and links

## Technology Stack

**Backend:**
- Spring Boot 3.2.0
- Spring Data JPA with Hibernate
- Spring Security
- PostgreSQL Database
- OpenAI API Integration (GPT-4)

**Frontend:**
- Thymeleaf templates
- HTML5/CSS3/JavaScript
- Chart.js for visualizations
- Responsive design with eco-friendly green theme

## Project Structure

```
src/main/java/net/codejava/
├── CodeJavaAppApplication.java       # Main Spring Boot application
├── AppController.java                # Main web controllers
├── AdminController.java              # Admin panel controller
├── User.java                         # User entity
├── ProductMapping.java               # Product entity
├── PurchaseHistory.java              # Purchase tracking entity
├── OpenAIService.java                # ChatGPT integration service
├── TrackingService.java              # Environmental impact tracking
├── CustomUserDetails.java            # Spring Security user details
├── CustomUserDetailsService.java    # User authentication service
├── WebSecurityConfig.java            # Security configuration
├── DataInitializer.java              # Sample data loader
└── *Repository.java                  # JPA repositories

src/main/resources/
├── templates/                        # Thymeleaf HTML templates
│   ├── index.html                   # Homepage
│   ├── login.html                   # Login page
│   ├── signup_form.html             # Registration
│   ├── dashboard.html               # User dashboard
│   ├── search.html                  # Product search
│   ├── product-detail.html          # Product comparison
│   ├── admin.html                   # Admin panel
│   └── product-form.html            # Product management
├── static/
│   └── css/styles.css               # Application styles
└── application.properties            # Configuration
```

## Database Schema

**Users Table:**
- Authentication and profile information
- Environmental impact totals (CO2, plastic saved)
- Total purchases count

**Product Mapping Table:**
- Normal product and eco alternative names
- Prices, descriptions, categories
- Purchase links
- Environmental impact metrics per unit
- Product images

**Purchase History Table:**
- User purchase records
- Product references
- Environmental savings per purchase
- Purchase timestamps

## Configuration

### Environment Variables Required:
- `PGHOST` - PostgreSQL host
- `PGPORT` - PostgreSQL port  
- `PGUSER` - PostgreSQL username
- `PGPASSWORD` - PostgreSQL password
- `PGDATABASE` - PostgreSQL database name
- `OPENAI_API_KEY` (optional) - For AI features

### Application Properties:
- Server runs on port 5000
- PostgreSQL database with auto schema updates
- Thymeleaf template engine
- Spring Security enabled

## Sample Products Loaded
1. Stainless Steel Reusable Water Bottle (vs Plastic Water Bottle)
2. Reusable Cotton Shopping Bags (vs Disposable Plastic Bags)
3. Bamboo Reusable Straws (vs Plastic Straws)
4. Bamboo Toothbrush (vs Plastic Toothbrush)
5. Ceramic Travel Mug (vs Disposable Coffee Cups)
6. Beeswax Food Wraps (vs Plastic Wrap)

## How to Use

### For Users:
1. **Register/Login** - Create an account or log in
2. **Browse Products** - View featured eco-friendly alternatives on homepage
3. **Search** - Find specific products using the search feature
4. **View Details** - Click on any product to see AI-generated comparison
5. **Track Purchases** - Mark purchases to track your environmental impact
6. **Dashboard** - View your sustainability stats and purchase history

### For Admins:
1. Navigate to `/admin` (requires authentication)
2. Add new product mappings
3. Edit existing products
4. Delete outdated products
5. Set purchase links and environmental metrics

## Next Steps (Future Enhancements)

1. **AI Features** - Set OPENAI_API_KEY to enable ChatGPT analysis
2. **Product Reviews** - Add user rating and review system
3. **Social Sharing** - Share sustainability achievements
4. **Email Notifications** - Alert users about new eco-products
5. **Gamification** - Badges and rewards for environmental milestones
6. **Advanced Analytics** - Detailed impact reports and exports
7. **API Integration** - Connect with eco-product marketplaces

## Recent Changes
- **2025-10-17**: Initial project setup and deployment
  - Implemented full Spring Boot eco-shopping application
  - Integrated PostgreSQL database
  - Added ChatGPT API service (requires API key)
  - Created responsive web interface
  - Implemented environmental impact tracking
  - Added admin panel for product management

## Notes
- The application uses Lombok for reducing boilerplate code
- Spring Security handles all authentication and authorization
- Database migrations handled automatically by Hibernate
- Static resources cached for performance
- LSP diagnostics showing in IDE are false positives - application compiles and runs successfully
