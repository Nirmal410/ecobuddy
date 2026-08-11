import { useState, useEffect } from 'react';
import { Link, useSearchParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';
import './LoginPage.css';

const ECO_FACTS = [
  { icon: '🌿', stat: '12,400+', label: 'kg CO₂ saved by our community' },
  { icon: '♻️', stat: '8,200+',  label: 'kg plastic avoided together' },
  { icon: '🛒', stat: '34,000+', label: 'eco purchases tracked' },
  { icon: '👥', stat: '5,000+',  label: 'eco warriors joined' },
];

const TESTIMONIALS = [
  { name: 'Priya S.', text: 'Switched 8 products. Saved 3.2kg CO₂ this month!', avatar: '🌱' },
  { name: 'Arjun M.', text: 'Love how easy it is to find eco alternatives.', avatar: '🍃' },
  { name: 'Neha K.', text: 'My dashboard motivates me every single day.', avatar: '🌎' },
];

export default function LoginPage() {
  const [searchParams] = useSearchParams();
  const [showPass, setShowPass]     = useState(false);
  const [loading, setLoading]       = useState(false);
  const [factIndex, setFactIndex]   = useState(0);
  const [testimonialIdx, setTestimonialIdx] = useState(0);
  const { checkAuthStatus }         = useAuth();

  const [loginError, setLoginError] = useState(searchParams.get('error') === 'true');
  const hasLogout  = searchParams.get('logout') !== null;
  const navigate   = useNavigate();

  // Cycle eco facts
  useEffect(() => {
    const t = setInterval(() => setFactIndex(i => (i + 1) % ECO_FACTS.length), 3000);
    return () => clearInterval(t);
  }, []);

  // Cycle testimonials
  useEffect(() => {
    const t = setInterval(() => setTestimonialIdx(i => (i + 1) % TESTIMONIALS.length), 4000);
    return () => clearInterval(t);
  }, []);

  return (
    <div className="login-page">
      {/* ── LEFT VISUAL PANEL ── */}
      <div className="login-visual">
        <div className="login-visual__orbs">
          <div className="lv-orb lv-orb--1" />
          <div className="lv-orb lv-orb--2" />
          <div className="lv-orb lv-orb--3" />
        </div>

        {/* Logo */}
        <div className="login-visual__top">
          <Link to="/" className="login-logo">🌿 EcoBuddy</Link>
        </div>

        {/* Hero text */}
        <div className="login-visual__hero">
          <span className="login-visual__tag">🌎 Join the Movement</span>
          <h1 className="login-visual__headline">
            Every eco-swap<br />
            <span className="login-visual__accent">makes a difference.</span>
          </h1>
          <p className="login-visual__sub">
            Log in to track your impact, discover new swaps,<br />
            and join a community choosing the planet.
          </p>
        </div>

        {/* Animated stat badge */}
        <div className="login-visual__stat-wrap">
          {ECO_FACTS.map((f, i) => (
            <div
              key={i}
              className={`login-stat-badge ${i === factIndex ? 'active' : ''}`}
            >
              <span className="login-stat-badge__icon">{f.icon}</span>
              <div>
                <div className="login-stat-badge__val">{f.stat}</div>
                <div className="login-stat-badge__label">{f.label}</div>
              </div>
            </div>
          ))}
        </div>

        {/* Testimonial carousel */}
        <div className="login-testimonial">
          {TESTIMONIALS.map((t, i) => (
            <div
              key={i}
              className={`login-testimonial__item ${i === testimonialIdx ? 'active' : ''}`}
            >
              <span className="login-testimonial__avatar">{t.avatar}</span>
              <div>
                <p className="login-testimonial__text">"{t.text}"</p>
                <p className="login-testimonial__name">— {t.name}</p>
              </div>
            </div>
          ))}
          <div className="login-testimonial__dots">
            {TESTIMONIALS.map((_, i) => (
              <button
                key={i}
                className={`login-testimonial__dot ${i === testimonialIdx ? 'active' : ''}`}
                onClick={() => setTestimonialIdx(i)}
              />
            ))}
          </div>
        </div>

        {/* Nature image strip */}
        <div className="login-visual__imgs">
          {[
            'https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=200&q=80',
            'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=200&q=80',
            'https://images.unsplash.com/photo-1542838132-92c53300491e?w=200&q=80',
          ].map((src, i) => (
            <div key={i} className="login-visual__img-chip">
              <img src={src} alt="eco" />
            </div>
          ))}
        </div>
      </div>

      {/* ── RIGHT FORM PANEL ── */}
      <div className="login-form-panel">
        <div className="login-form-wrap">
          <Link to="/" className="login-back-link">← Back to Home</Link>

          {/* Header */}
          <div className="login-form__header">
            <h2 className="login-form__title">Welcome back 👋</h2>
            <p className="login-form__sub">Sign in to your EcoBuddy account</p>
          </div>

          {/* Alerts */}
          {loginError && (
            <div className="login-alert login-alert--error">
              <span>⚠️</span>
              <span>Invalid email or password. Please try again.</span>
            </div>
          )}
          {hasLogout && (
            <div className="login-alert login-alert--success">
              <span>✅</span>
              <span>You've been logged out successfully.</span>
            </div>
          )}

          {/* Google sign-in (UI only) */}
          <button type="button" className="login-google-btn">
            <svg width="18" height="18" viewBox="0 0 18 18" fill="none">
              <path d="M17.64 9.2c0-.637-.057-1.251-.164-1.84H9v3.481h4.844a4.14 4.14 0 01-1.796 2.716v2.259h2.908c1.702-1.567 2.684-3.875 2.684-6.615z" fill="#4285F4"/>
              <path d="M9 18c2.43 0 4.467-.806 5.956-2.184l-2.908-2.259c-.806.54-1.837.859-3.048.859-2.344 0-4.328-1.584-5.036-3.711H.957v2.332A8.997 8.997 0 009 18z" fill="#34A853"/>
              <path d="M3.964 10.705A5.41 5.41 0 013.682 9c0-.593.102-1.17.282-1.705V4.963H.957A8.996 8.996 0 000 9c0 1.452.348 2.827.957 4.037l3.007-2.332z" fill="#FBBC05"/>
              <path d="M9 3.58c1.321 0 2.508.454 3.44 1.345l2.582-2.58C13.463.891 11.426 0 9 0A8.997 8.997 0 00.957 4.963L3.964 7.295C4.672 5.163 6.656 3.58 9 3.58z" fill="#EA4335"/>
            </svg>
            Continue with Google
          </button>

          <div className="login-divider">
            <span>or continue with email</span>
          </div>

          {/* THE FORM — JSON POST to /api/auth/login */}
          <form
            className="login-form"
            onSubmit={async (e) => {
              e.preventDefault();
              setLoading(true);
              setLoginError(false);
              try {
                const res = await fetch('/api/auth/login', {
                  method: 'POST',
                  headers: { 'Content-Type': 'application/json' },
                  body: JSON.stringify({
                    username: e.target.username.value,
                    password: e.target.password.value,
                  }),
                  credentials: 'include',
                });
                const data = await res.json();
                if (data.success) {
                  await checkAuthStatus();
                  navigate('/dashboard');
                } else {
                  setLoginError(true);
                  setLoading(false);
                }
              } catch {
                setLoginError(true);
                setLoading(false);
              }
            }}
          >
            {/* Email */}
            <div className="login-field">
              <label htmlFor="username">Email Address</label>
              <div className="login-input-wrap">
                <span className="login-input-icon">✉️</span>
                <input
                  type="text"
                  id="username"
                  name="username"
                  placeholder="you@example.com"
                  required
                  autoComplete="email"
                />
              </div>
            </div>

            {/* Password */}
            <div className="login-field">
              <div className="login-field__row">
                <label htmlFor="password">Password</label>
                <Link to="#" className="login-forgot">Forgot password?</Link>
              </div>
              <div className="login-input-wrap">
                <span className="login-input-icon">🔒</span>
                <input
                  type={showPass ? 'text' : 'password'}
                  id="password"
                  name="password"
                  placeholder="••••••••"
                  required
                  autoComplete="current-password"
                />
                <button
                  type="button"
                  className="login-input-toggle"
                  onClick={() => setShowPass(s => !s)}
                  tabIndex={-1}
                >
                  {showPass ? '🙈' : '👁️'}
                </button>
              </div>
            </div>

            {/* Remember me */}
            <label className="login-remember">
              <input type="checkbox" name="remember" />
              <span>Keep me signed in for 30 days</span>
            </label>

            {/* Submit */}
            <button
              type="submit"
              className="login-submit-btn"
              disabled={loading}
            >
              {loading ? (
                <span className="login-submit-btn__loading">
                  <span className="login-spinner" /> Signing in...
                </span>
              ) : (
                '🌿 Sign In to EcoBuddy'
              )}
            </button>
          </form>

          {/* Impact preview */}
          <div className="login-impact-preview">
            <div className="login-impact-item">
              <span>🌿</span>
              <span>Track CO₂ savings</span>
            </div>
            <div className="login-impact-item">
              <span>♻️</span>
              <span>Discover eco swaps</span>
            </div>
            <div className="login-impact-item">
              <span>📊</span>
              <span>View your impact</span>
            </div>
          </div>

          {/* Switch to signup */}
          <p className="login-switch">
            New to EcoBuddy?{' '}
            <Link to="/signup">Create a free account →</Link>
          </p>
        </div>
      </div>
    </div>
  );
}
