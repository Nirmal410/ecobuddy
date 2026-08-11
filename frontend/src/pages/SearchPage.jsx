import { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { searchProducts, getProducts } from '../api';
import ProductCard from '../components/ProductCard';
import './SearchPage.css';

const CATEGORIES = ['All', 'Kitchen', 'Bathroom', 'Food', 'Clothing', 'Electronics'];

export default function SearchPage() {
  const [params] = useSearchParams();
  const [query, setQuery]       = useState(params.get('q') || '');
  const [results, setResults]   = useState([]);
  const [loading, setLoading]   = useState(false);
  const [searched, setSearched] = useState(false);
  const [category, setCategory] = useState('All');

  useEffect(() => {
    const q = params.get('q');
    if (q) { setQuery(q); doSearch(q); }
    else { loadAll(); }
  }, []);

  const loadAll = () => {
    setLoading(true);
    getProducts()
      .then(r => { setResults(r.data); setSearched(false); })
      .catch(() => setResults([]))
      .finally(() => setLoading(false));
  };

  const doSearch = (kw) => {
    if (!kw.trim()) { loadAll(); return; }
    setLoading(true);
    searchProducts(kw)
      .then(r => { setResults(r.data); setSearched(true); })
      .catch(() => setResults([]))
      .finally(() => setLoading(false));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    doSearch(query);
  };

  const filteredResults = category === 'All'
    ? results
    : results.filter(p => p.category && p.category.toLowerCase() === category.toLowerCase());

  return (
    <div className="search-page">
      <div className="search-hero">
        <div className="container">
          <span className="section-label">🔍 Explore</span>
          <h1 className="search-hero__title">Find Your Eco Swap</h1>
          <p className="search-hero__sub">
            Search for any everyday product and discover its sustainable alternative.
          </p>

          <form className="search-bar" onSubmit={handleSubmit}>
            <span className="search-bar__icon">🔍</span>
            <input
              type="text"
              value={query}
              onChange={e => setQuery(e.target.value)}
              placeholder="Try 'plastic bottle', 'straws', 'coffee cup'..."
              className="search-bar__input"
            />
            <button type="submit" className="btn btn-primary search-bar__btn">
              Search
            </button>
          </form>

          <div className="search-categories">
            {CATEGORIES.map(cat => (
              <button
                key={cat}
                className={`search-cat-btn ${category === cat ? 'active' : ''}`}
                onClick={() => setCategory(cat)}
              >
                {cat}
              </button>
            ))}
          </div>
        </div>
      </div>

      <div className="container search-results">
        {searched && query && (
          <div className="search-results__header">
            <h2>
              Results for "<span className="search-results__keyword">{query}</span>" {category !== 'All' ? `in ${category}` : ''}
            </h2>
            <span className="badge badge-green">{filteredResults.length} found</span>
          </div>
        )}

        {!searched && !loading && (
          <div className="search-results__header">
            <h2>{category === 'All' ? 'All Eco Products' : `${category} Eco Products`}</h2>
            <span className="badge badge-green">{filteredResults.length} available</span>
          </div>
        )}

        {loading ? (
          <div style={{ textAlign: 'center', margin: '60px 0' }}>
            <div className="spinner" style={{ marginBottom: '16px' }} />
            <p style={{ color: 'var(--text-muted)', fontSize: '1.05rem' }}>
              🤖 Searching database & analyzing web for eco-friendly alternatives with AI...
            </p>
          </div>
        ) : filteredResults.length > 0 ? (
          <div className="search-grid">
            {filteredResults.map((p, i) => <ProductCard key={p.id} product={p} index={i} />)}
          </div>
        ) : searched || category !== 'All' ? (
          <div className="search-empty">
            <div className="search-empty__icon">🌿</div>
            <h3>
              No results found {category !== 'All' ? `in category "${category}"` : ''} {query ? `for "${query}"` : ''}
            </h3>
            <p>Try selecting a different category or browsing all products.</p>
            <button className="btn btn-outline" onClick={() => { setCategory('All'); setQuery(''); loadAll(); }}>
              Browse All Products
            </button>
          </div>
        ) : null}
      </div>
    </div>
  );
}
