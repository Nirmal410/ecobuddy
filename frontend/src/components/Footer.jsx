import { Link } from 'react-router-dom';
import './Footer.css';

export default function Footer() {
  return (
    <footer className="footer">
      <div className="container footer__inner">
        <div className="footer__brand">
          <div className="footer__logo">🌿 EcoBuddy</div>
          <p className="footer__tagline">
            Making sustainability simple, one eco-swap at a time.
          </p>
          <div className="footer__social">
            {['🐦','📸','💼','🌐'].map((icon, i) => (
              <button key={i} className="footer__social-btn">{icon}</button>
            ))}
          </div>
        </div>

        <div className="footer__links-group">
          <h4>Explore</h4>
          <Link to="/">Home</Link>
          <Link to="/search">Shop Eco</Link>
          <Link to="/dashboard">Dashboard</Link>
        </div>

        <div className="footer__links-group">
          <h4>Account</h4>
          <Link to="/login">Login</Link>
          <Link to="/signup">Sign Up</Link>
        </div>

        <div className="footer__newsletter">
          <h4>Join the Community</h4>
          <p>Get weekly eco tips and exclusive deals.</p>
          <form className="footer__form" onSubmit={e => e.preventDefault()}>
            <input type="email" placeholder="your@email.com" />
            <button type="submit" className="btn btn-primary">Subscribe</button>
          </form>
        </div>
      </div>

      <div className="footer__bottom container">
        <p>© 2025 EcoBuddy · Making the world greener, one step at a time 🌍</p>
        <div className="footer__bottom-links">
          <a href="#">Privacy</a>
          <a href="#">Terms</a>
          <a href="#">Contact</a>
        </div>
      </div>
    </footer>
  );
}
