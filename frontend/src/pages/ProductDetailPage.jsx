import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getProduct, recordPurchase, analyzeProduct } from '../api';
import './ProductDetailPage.css';

export default function ProductDetailPage() {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [purchased, setPurchased] = useState(false);
  const [analyzing, setAnalyzing] = useState(false);

  useEffect(() => {
    getProduct(id)
      .then(r => setProduct(r.data))
      .catch(() => setProduct(null))
      .finally(() => setLoading(false));
  }, [id]);

  const handlePurchase = async () => {
    try {
      await recordPurchase(id);
      setPurchased(true);
    } catch {
      alert('Please log in to track purchases.');
    }
  };

  const handleAIAnalyze = async () => {
    setAnalyzing(true);
    try {
      const res = await analyzeProduct(id);
      setProduct(res.data);
    } catch (e) {
      console.error('AI Analysis failed:', e);
    } finally {
      setAnalyzing(false);
    }
  };

  if (loading) return <div className="spinner" style={{ marginTop: '120px' }} />;

  if (!product) {
    return (
      <div className="detail-notfound">
        <h2>Product not found 🌿</h2>
        <Link to="/search" className="btn btn-primary">Browse Products</Link>
      </div>
    );
  }

  const imgSrc = product.imageUrl || 'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=600&q=80';

  return (
    <div className="detail-page">
      <div className="container">
        {/* Breadcrumb */}
        <div className="detail-breadcrumb">
          <Link to="/">Home</Link>
          <span>›</span>
          <Link to="/search">Products</Link>
          <span>›</span>
          <span>{product.ecoAlternative}</span>
        </div>

        {/* Main Grid */}
        <div className="detail-grid">
          {/* Image */}
          <div className="detail-img-wrap">
            <img src={imgSrc} alt={product.ecoAlternative} className="detail-img" onError={e => { e.target.src = 'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=600&q=80'; }} />
            <div className="detail-img-badge">
              <span className="badge badge-green">🌿 Eco Certified</span>
            </div>
          </div>

          {/* Info */}
          <div className="detail-info animate-fadeInUp">
            <p className="detail-instead">Instead of: <strong>{product.normalProduct}</strong></p>
            <h1 className="detail-title">{product.ecoAlternative}</h1>

            {product.ecoPrice && (
              <div className="detail-prices">
                <span className="detail-eco-price">${product.ecoPrice}</span>
                {product.normalPrice && (
                  <span className="detail-old-price">${product.normalPrice} normal</span>
                )}
                {product.normalPrice && (
                  <span className="badge badge-green">
                    Save ${(product.normalPrice - product.ecoPrice).toFixed(2)}
                  </span>
                )}
              </div>
            )}

            {product.description && (
              <p className="detail-desc">{product.description}</p>
            )}

            {/* Impact box */}
            <div className="detail-impact">
              <h3 className="detail-impact__title">🌍 Environmental Damage Prevented Per Unit</h3>
              <div className="detail-impact__grid">
                <div className="detail-impact__item">
                  <span className="detail-impact__icon">🌿</span>
                  <div>
                    <div className="detail-impact__val">{product.co2SavedPerUnit} kg</div>
                    <div className="detail-impact__label">CO₂ Saved</div>
                  </div>
                </div>
                <div className="detail-impact__item">
                  <span className="detail-impact__icon">♻️</span>
                  <div>
                    <div className="detail-impact__val">{product.plasticSavedPerUnit} kg</div>
                    <div className="detail-impact__label">Plastic Avoided</div>
                  </div>
                </div>
              </div>
            </div>

            {/* Actions */}
            <div className="detail-actions">
              {product.purchaseLink && (
                <a href={product.purchaseLink} target="_blank" rel="noopener noreferrer" className="btn btn-primary">
                  🛒 Buy Now
                </a>
              )}
              {purchased ? (
                <div className="alert alert-success">✅ Purchase tracked! Impact recorded.</div>
              ) : (
                <button className="btn btn-outline" onClick={handlePurchase}>
                  📊 Track This Purchase
                </button>
              )}
            </div>
          </div>
        </div>

        {/* Pros & Cons Section (Environmental Damage Analysis) */}
        <div className="detail-ai">
          <div className="detail-ai__header">
            <div>
              <h2>🤖 OpenAI Environmental Impact & Buyer Analysis</h2>
              <p className="detail-ai__subtitle">
                Comparing <strong>{product.ecoAlternative}</strong> (Eco Choice) vs <strong>{product.normalProduct}</strong> (Conventional Choice)
              </p>
            </div>
            <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
              <button
                onClick={handleAIAnalyze}
                disabled={analyzing}
                className="btn btn-outline detail-ai__reanalyze-btn"
              >
                {analyzing ? '⏳ Asking OpenAI...' : '🤖 Ask OpenAI for Fresh Analysis'}
              </button>
              <span className="badge badge-green">Powered by GPT-4</span>
            </div>
          </div>

          <div className="detail-ai__grid">
            <div className="detail-ai__card detail-ai__card--pros">
              <div className="detail-ai__card-header">
                <h3>🌱 Why Buy Eco-Friendly (Damage Saved & Benefits)</h3>
                <span className="detail-ai__badge pros-badge">Eco Advantage</span>
              </div>
              <div
                className="detail-ai__body"
                dangerouslySetInnerHTML={{
                  __html: product.aiGeneratedPros || `<ul>
                    <li><strong>🌿 Environmental Damage Saved:</strong> Prevents ${product.co2SavedPerUnit || 2.5} kg CO₂ emissions and ${product.plasticSavedPerUnit || 0.5} kg plastic waste per unit.</li>
                    <li><strong>💧 Resource Preservation:</strong> Eliminates continuous energy, oil, and water consumption used to manufacture disposables.</li>
                    <li><strong>💰 Long-Term Money Saver:</strong> Replaces hundreds of single-use purchases over its lifetime — pays for itself quickly.</li>
                    <li><strong>🛡️ 100% Safe & Non-Toxic:</strong> BPA-free and non-toxic materials keep dangerous chemicals and microplastics away from your body.</li>
                  </ul>`
                }}
              />
            </div>

            <div className="detail-ai__card detail-ai__card--cons">
              <div className="detail-ai__card-header">
                <h3>🚨 Why Avoid Conventional Item (Environmental Damage Caused)</h3>
                <span className="detail-ai__badge cons-badge">Severe Pollution</span>
              </div>
              <div
                className="detail-ai__body"
                dangerouslySetInnerHTML={{
                  __html: product.aiGeneratedCons || `<ul>
                    <li><strong>🚨 Centuries in Landfills:</strong> Disposable ${product.normalProduct.toLowerCase()} items persist in landfills for 450+ years without degrading.</li>
                    <li><strong>🌊 Destroys Marine Wildlife:</strong> Single-use plastic waste clogs rivers and oceans, killing thousands of sea turtles and marine animals.</li>
                    <li><strong>🧪 Microplastic Contamination:</strong> Weathered plastics break down into toxic microplastic particles ingested by humans and wildlife.</li>
                  </ul>`
                }}
              />
            </div>
          </div>

          <div className="detail-ai__suggestion">
            <h3>💡 OpenAI Recommendation & Buy Motivation</h3>
            <p>
              {product.aiGeneratedSuggestion || `Making the switch to ${product.ecoAlternative} eliminates ongoing plastic pollution, protects ocean life, and saves you money. It is one of the highest-impact daily eco-swaps you can make today!`}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

