import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './AuthPages.css';

export default function SignupPage() {
  const [loading, setLoading] = useState(false);
  const [error, setError]     = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const res = await fetch('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          firstName: e.target.firstName.value,
          lastName:  e.target.lastName.value,
          email:     e.target.email.value,
          password:  e.target.password.value,
        }),
        credentials: 'include',
      });
      const data = await res.json();
      if (data.success) {
        navigate('/login');
      } else {
        setError(data.message || 'Registration failed. Please try again.');
        setLoading(false);
      }
    } catch {
      setError('Registration failed. Please try again.');
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      {/* Left panel – Visual */}
      <div className="auth-panel auth-panel--visual">
        <div className="auth-bg-orbs">
          <div className="auth-orb auth-orb--1" />
          <div className="auth-orb auth-orb--2" />
        </div>
        <div className="auth-visual__content">
          <div className="auth-visual__logo">🌿 EcoBuddy</div>
          <h2 className="auth-visual__headline">
            Start your eco<br />journey today.
          </h2>
          <p className="auth-visual__sub">
            Join a community committed to sustainable living and positive impact.
          </p>

          <div className="auth-visual__features">
            {[
              { icon: '🌱', text: 'Discover eco-friendly product swaps' },
              { icon: '🤖', text: 'AI-powered sustainability insights' },
              { icon: '📊', text: 'Track your CO₂ & plastic savings' },
              { icon: '🏆', text: 'Earn badges for green milestones' },
            ].map(f => (
              <div key={f.icon} className="auth-feature-item">
                <span className="auth-feature-item__icon">{f.icon}</span>
                <span>{f.text}</span>
              </div>
            ))}
          </div>

          <div className="auth-visual__img">
            <img
              src="https://images.unsplash.com/photo-1542838132-92c53300491e?w=600&q=80"
              alt="Eco community"
            />
          </div>
        </div>
      </div>

      {/* Right panel – Form */}
      <div className="auth-panel auth-panel--form">
        <div className="auth-form-wrap animate-fadeInUp">
          <Link to="/" className="auth-back">← Back to Home</Link>

          <div className="auth-form__header">
            <span className="section-label">🌿 Join the Movement</span>
            <h1 className="auth-form__title">Create your<br />free account</h1>
            <p className="auth-form__sub">Make sustainable choices, track your impact.</p>
          </div>

          {error && (
            <div className="alert alert-error" style={{marginBottom:'16px'}}>
              ⚠️ {error}
            </div>
          )}

          {/* Fetch-based registration form */}
          <form className="auth-form" onSubmit={handleSubmit}>
            <div className="auth-form__row">
              <div className="form-group">
                <label htmlFor="firstName">First Name</label>
                <input
                  type="text"
                  id="firstName"
                  name="firstName"
                  placeholder="Jane"
                  required
                  autoComplete="given-name"
                />
              </div>
              <div className="form-group">
                <label htmlFor="lastName">Last Name</label>
                <input
                  type="text"
                  id="lastName"
                  name="lastName"
                  placeholder="Doe"
                  required
                  autoComplete="family-name"
                />
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="email">Email Address</label>
              <input
                type="email"
                id="email"
                name="email"
                placeholder="you@example.com"
                required
                autoComplete="email"
              />
            </div>

            <div className="form-group">
              <label htmlFor="password">Password</label>
              <input
                type="password"
                id="password"
                name="password"
                placeholder="Create a strong password"
                required
                minLength={8}
                autoComplete="new-password"
              />
            </div>

            <div className="auth-terms">
              <label className="auth-checkbox">
                <input type="checkbox" required />
                I agree to the{' '}
                <a href="#" className="auth-link">Terms of Service</a>{' '}
                and{' '}
                <a href="#" className="auth-link">Privacy Policy</a>
              </label>
            </div>

            <button
              type="submit"
              className="btn btn-primary auth-submit"
              disabled={loading}
            >
              {loading ? '⏳ Creating account...' : '🌿 Create Account'}
            </button>
          </form>

          <p className="auth-form__switch">
            Already have an account? <Link to="/login">Sign in →</Link>
          </p>
        </div>
      </div>
    </div>
  );
}
