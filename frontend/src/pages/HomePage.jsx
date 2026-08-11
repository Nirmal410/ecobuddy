import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getProducts } from '../api';
import ProductCard from '../components/ProductCard';
import './HomePage.css';

const HERO_STATS = [
  { icon: '🌿', value: '12,400+', label: 'kg CO₂ Saved' },
  { icon: '♻️', value: '8,200+',  label: 'kg Plastic Avoided' },
  { icon: '🛒', value: '34,000+', label: 'Eco Purchases Made' },
];

const FEATURES = [
  {
    icon: '🔄',
    title: 'Planet Swaps',
    desc: 'Easily swap everyday items for biodegradable, plastic-free alternatives without sacrificing quality.',
  },
  {
    icon: '🤖',
    title: 'AI Insights',
    desc: 'Get AI-powered pros & cons, so you can make informed, sustainable choices every time.',
  },
  {
    icon: '📊',
    title: 'Track Impact',
    desc: 'Watch your CO₂ and plastic savings grow with every purchase on your personal dashboard.',
  },
];

const CATEGORIES = ['All', 'Kitchen', 'Bathroom', 'Food', 'Clothing', 'Electronics'];

export default function HomePage() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeCategory, setActiveCategory] = useState('All');

  useEffect(() => {
    getProducts()
      .then(res => setProducts(res.data))
      .catch(() => setProducts([]))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="home">
      {/* ──── HERO ──── */}
      <section className="hero">
        <div className="hero__bg-orbs">
          <div className="orb orb--1" />
          <div className="orb orb--2" />
          <div className="orb orb--3" />
        </div>

        <div className="container hero__content">
          <div className="hero__text animate-fadeInUp">
            <span className="section-label">🌎 Eco-Friendly Shopping</span>
            <h1 className="hero__headline">
              Better for you,<br />
              <span className="hero__headline-accent">better for the planet.</span>
            </h1>
            <p className="hero__sub">
              Discover curated eco-friendly alternatives to everyday products.
              Simple swaps offering quality results — well-being, big impact.
            </p>
            <div className="hero__cta">
              <Link to="/search" className="btn btn-primary hero__cta-btn">
                🌿 Explore Swaps
              </Link>
              <Link to="/signup" className="btn btn-outline hero__cta-btn">
                How It Works
              </Link>
            </div>
          </div>

          <div className="hero__visual animate-fadeIn">
            <div className="hero__img-card hero__img-card--main">
              <img
                src="https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=600&q=80"
                alt="Eco products"
              />
            </div>
            <div className="hero__img-card hero__img-card--sm">
              <img
                src="https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=300&q=80"
                alt="Sustainable living"
              />
            </div>
            <div className="hero__floating-badge animate-float">
              <span className="hero__fb-icon">🌿</span>
              <div>
                <p className="hero__fb-val">2.4 kg</p>
                <p className="hero__fb-label">CO₂ Saved Today</p>
              </div>
            </div>
          </div>
        </div>

        {/* Stats bar */}
        <div className="container">
          <div className="hero__stats">
            {HERO_STATS.map((s) => (
              <div key={s.label} className="hero__stat">
                <span className="hero__stat-icon">{s.icon}</span>
                <div>
                  <div className="hero__stat-val">{s.value}</div>
                  <div className="hero__stat-label">{s.label}</div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ──── SWAP OF THE DAY ──── */}
      {products.length > 0 && (
        <section className="swap-day">
          <div className="container">
            <div className="swap-day__header">
              <div>
                <span className="section-label">✨ Featured</span>
                <h2 className="swap-day__title">Swap of the Day</h2>
                <p className="swap-day__sub">Sustainably curate to enrich your conscious lifestyle.</p>
              </div>
              <Link to="/search" className="btn btn-outline">View All →</Link>
            </div>

            <div className="swap-day__grid">
              {products.slice(0, 2).map((p, i) => (
                <div key={p.id} className={`swap-day__item ${i === 1 ? 'swap-day__item--featured' : ''}`}>
                  <img
                    src={p.imageUrl || `https://images.unsplash.com/photo-154260190${'6990'}-b4d3fb778b09?w=400&q=80`}
                    alt={p.ecoAlternative}
                    className="swap-day__img"
                    onError={e => { e.target.src = 'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=400&q=80'; }}
                  />
                  <div className="swap-day__info">
                    <span className="badge badge-green">🌿 Eco Pick</span>
                    <h3>{p.ecoAlternative}</h3>
                    <p>Switch from {p.normalProduct} and save<br />
                      <strong>{p.co2SavedPerUnit}kg CO₂</strong> per use.
                    </p>
                    {p.ecoPrice && (
                      <div className="swap-day__price">
                        <span className="swap-day__eco-price">${p.ecoPrice}</span>
                        {p.normalPrice && <span className="swap-day__old-price">${p.normalPrice}</span>}
                      </div>
                    )}
                    <Link to={`/product/${p.id}`} className="btn btn-primary">
                      Swap Now
                    </Link>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </section>
      )}

      {/* ──── FEATURES ──── */}
      <section className="features-section">
        <div className="container">
          <div className="features-section__header">
            <span className="section-label">💡 Why EcoBuddy</span>
            <h2>Making Sustainability Simple</h2>
            <p>We do the research so you don't have to. Discover easy swaps and know exactly their environmental, well-being, and financial impact.</p>
          </div>

          <div className="features-section__grid">
            {FEATURES.map((f) => (
              <div key={f.title} className="feature-card card">
                <div className="feature-card__icon">{f.icon}</div>
                <h3>{f.title}</h3>
                <p>{f.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ──── PRODUCT GRID ──── */}
      <section className="product-section">
        <div className="container">
          <div className="product-section__header">
            <span className="section-label">🛒 Top Picks</span>
            <h2>Trending Eco Products</h2>
          </div>

          {/* Category Tabs */}
          <div className="product-section__tabs">
            {CATEGORIES.map(cat => (
              <button
                key={cat}
                className={`product-section__tab ${activeCategory === cat ? 'active' : ''}`}
                onClick={() => setActiveCategory(cat)}
              >
                {cat}
              </button>
            ))}
          </div>

          {(() => {
            const filteredProducts = activeCategory === 'All'
              ? products
              : products.filter(p => p.category && p.category.toLowerCase() === activeCategory.toLowerCase());

            if (loading) return <div className="spinner" />;
            
            if (filteredProducts.length > 0) {
              return (
                <div className="product-section__grid">
                  {filteredProducts.map((p, i) => (
                    <ProductCard key={p.id} product={p} index={i} />
                  ))}
                </div>
              );
            }

            return (
              <div className="product-section__empty">
                <p>🌿 No products found in category "{activeCategory}".</p>
                <button className="btn btn-primary" onClick={() => setActiveCategory('All')}>Browse All</button>
              </div>
            );
          })()}
        </div>
      </section>

      {/* ──── NEWSLETTER ──── */}
      <section className="newsletter">
        <div className="container">
          <div className="newsletter__inner">
            <div className="newsletter__text">
              <span className="section-label">📬 Stay Updated</span>
              <h2>Join the EcoBuddy Community</h2>
              <p>Get weekly eco-tips, exclusive discounts, and impact reports delivered to your inbox.</p>
            </div>
            <form className="newsletter__form" onSubmit={e => e.preventDefault()}>
              <input type="email" placeholder="Enter your email" className="newsletter__input" />
              <button type="submit" className="btn btn-earth">Subscribe</button>
            </form>
            <p className="newsletter__fine">No spam. Unsubscribe at any time. 🌿</p>
          </div>
        </div>
      </section>
    </div>
  );
}
