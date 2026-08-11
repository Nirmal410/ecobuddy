import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getDashboard } from '../api';
import { useAuth } from '../AuthContext';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import './DashboardPage.css';

export default function DashboardPage() {
  const [data, setData]       = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError]     = useState(false);
  const { user }              = useAuth();

  const fetchDashboard = () => {
    setLoading(true);
    setError(false);
    getDashboard()
      .then(r => setData(r.data))
      .catch(() => setError(true))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchDashboard();
  }, []);

  if (loading) return <div className="spinner" style={{ marginTop: '120px' }} />;

  if (error) {
    if (user?.loggedIn) {
      return (
        <div className="dash-notfound">
          <h2>⚠️ Unable to load dashboard data</h2>
          <p style={{ color: 'var(--text-muted)' }}>Something went wrong loading your stats. Please try again.</p>
          <div className="dash-notfound__btns">
            <button onClick={fetchDashboard} className="btn btn-primary">🔄 Retry</button>
          </div>
        </div>
      );
    }

    return (
      <div className="dash-notfound">
        <h2>🔒 Please log in to view your dashboard</h2>
        <div className="dash-notfound__btns">
          <Link to="/login"  className="btn btn-primary">Sign In</Link>
          <Link to="/signup" className="btn btn-outline">Create Account</Link>
        </div>
      </div>
    );
  }

  const chartData = [
    { name: 'CO₂ Saved (kg)',     value: data?.totalCo2Saved || 0,     fill: '#6dbf88' },
    { name: 'Plastic Saved (kg)', value: data?.totalPlasticSaved || 0, fill: '#c8a97e' },
  ];

  return (
    <div className="dashboard-page">
      <div className="container">
        {/* Header */}
        <div className="dash-header animate-fadeInUp">
          <div>
            <span className="section-label">🌿 Your Impact</span>
            <h1 className="dash-title">
              Welcome back, <span className="dash-name">{data?.firstName || 'Eco Warrior'}</span>!
            </h1>
            <p className="dash-sub">Here's your sustainability impact at a glance.</p>
          </div>
          <Link to="/search" className="btn btn-primary">🛒 Find More Swaps</Link>
        </div>

        {/* Stat Cards */}
        <div className="dash-stats">
          <div className="dash-stat-card">
            <div className="dash-stat-card__icon">🌿</div>
            <div className="dash-stat-card__body">
              <div className="dash-stat-card__val">{data?.totalCo2Saved || 0} kg</div>
              <div className="dash-stat-card__label">CO₂ Saved</div>
            </div>
            <div className="dash-stat-card__trend">+{((data?.totalCo2Saved || 0) * 0.12).toFixed(1)} this month</div>
          </div>

          <div className="dash-stat-card">
            <div className="dash-stat-card__icon">♻️</div>
            <div className="dash-stat-card__body">
              <div className="dash-stat-card__val">{data?.totalPlasticSaved || 0} kg</div>
              <div className="dash-stat-card__label">Plastic Avoided</div>
            </div>
            <div className="dash-stat-card__trend">+{((data?.totalPlasticSaved || 0) * 0.08).toFixed(1)} this month</div>
          </div>

          <div className="dash-stat-card">
            <div className="dash-stat-card__icon">🛒</div>
            <div className="dash-stat-card__body">
              <div className="dash-stat-card__val">{data?.totalPurchases || 0}</div>
              <div className="dash-stat-card__label">Eco Purchases</div>
            </div>
            <div className="dash-stat-card__trend">Total tracked swaps</div>
          </div>

          <div className="dash-stat-card dash-stat-card--highlight">
            <div className="dash-stat-card__icon">🏆</div>
            <div className="dash-stat-card__body">
              <div className="dash-stat-card__val">
                {data?.totalPurchases >= 10 ? 'Gold' : data?.totalPurchases >= 5 ? 'Silver' : 'Bronze'}
              </div>
              <div className="dash-stat-card__label">Eco Level</div>
            </div>
            <div className="dash-stat-card__trend">Keep it up! 🌱</div>
          </div>
        </div>

        {/* Chart + Purchases */}
        <div className="dash-bottom">
          {/* Chart */}
          <div className="dash-chart card">
            <h3 className="dash-section-title">📊 Impact Overview</h3>
            <ResponsiveContainer width="100%" height={280}>
              <BarChart data={chartData} margin={{ top: 10, right: 20, left: 0, bottom: 0 }}>
                <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.05)" />
                <XAxis dataKey="name" tick={{ fill: '#7a9e80', fontSize: 12 }} axisLine={false} tickLine={false} />
                <YAxis tick={{ fill: '#7a9e80', fontSize: 12 }} axisLine={false} tickLine={false} />
                <Tooltip
                  contentStyle={{ background: '#1f351f', border: '1px solid #4a7c5940', borderRadius: 12, color: '#e8f5e9' }}
                  cursor={{ fill: 'rgba(109,191,136,0.05)' }}
                />
                <Bar dataKey="value" radius={[8, 8, 0, 0]} fill="#6dbf88" />
              </BarChart>
            </ResponsiveContainer>
          </div>

          {/* Recent purchases */}
          <div className="dash-purchases card">
            <h3 className="dash-section-title">🛒 Recent Eco Purchases</h3>
            {data?.recentPurchases && data.recentPurchases.length > 0 ? (
              <div className="dash-purchase-list">
                {data.recentPurchases.map((p, i) => (
                  <div key={i} className="dash-purchase-item">
                    <div className="dash-purchase-item__info">
                      <h4>{p.product?.ecoAlternative || 'Eco Product'}</h4>
                      <p>Instead of: {p.product?.normalProduct}</p>
                      <span className="dash-purchase-item__date">
                        {p.purchaseDate ? new Date(p.purchaseDate).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }) : ''}
                      </span>
                    </div>
                    <div className="dash-purchase-item__badges">
                      <span className="badge badge-green">🌿 {p.co2Saved} kg</span>
                      <span className="badge badge-earth">♻️ {p.plasticSaved} kg</span>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="dash-purchases__empty">
                <p>🌱 No purchases yet. Start making eco swaps!</p>
                <Link to="/search" className="btn btn-primary">Browse Products</Link>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
