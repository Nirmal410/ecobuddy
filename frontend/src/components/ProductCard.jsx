import { Link } from 'react-router-dom';
import './ProductCard.css';

const ECO_IMAGES = [
  'https://images.unsplash.com/photo-1542838132-92c53300491e?w=400&q=80',
  'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=400&q=80',
  'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=400&q=80',
  'https://images.unsplash.com/photo-1484723091739-30a097e8f929?w=400&q=80',
  'https://images.unsplash.com/photo-1606787366850-de6330128bfc?w=400&q=80',
  'https://images.unsplash.com/photo-1550989460-0adf9ea622e2?w=400&q=80',
];

export default function ProductCard({ product, index = 0 }) {
  const imgSrc = product.imageUrl || ECO_IMAGES[index % ECO_IMAGES.length];

  return (
    <div className="product-card card">
      <div className="product-card__img-wrap">
        <img
          src={imgSrc}
          alt={product.ecoAlternative}
          className="product-card__img"
          onError={(e) => {
            e.target.onerror = null;
            e.target.src = 'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=400&q=80';
          }}
        />
        <span className="product-card__label section-label">🌿 Eco Swap</span>
      </div>

      <div className="product-card__body">
        <p className="product-card__instead">Instead of: {product.normalProduct}</p>
        <h3 className="product-card__title">{product.ecoAlternative}</h3>

        {product.ecoPrice && (
          <div className="product-card__prices">
            <span className="product-card__price">${product.ecoPrice}</span>
            {product.normalPrice && (
              <span className="product-card__normal-price">${product.normalPrice}</span>
            )}
          </div>
        )}

        <div className="product-card__badges">
          {product.co2SavedPerUnit && (
            <span className="badge badge-green">🌿 {product.co2SavedPerUnit}kg CO₂</span>
          )}
          {product.plasticSavedPerUnit && (
            <span className="badge badge-earth">♻️ {product.plasticSavedPerUnit}kg plastic</span>
          )}
        </div>

        <Link to={`/product/${product.id}`} className="btn btn-primary product-card__btn">
          View Details →
        </Link>
      </div>
    </div>
  );
}
