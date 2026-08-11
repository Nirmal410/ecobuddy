import { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';
import './Navbar.css';

export default function Navbar() {
  const [scrolled, setScrolled] = useState(false);
  const [menuOpen, setMenuOpen] = useState(false);
  const [searchOpen, setSearchOpen] = useState(false);
  const [searchVal, setSearchVal] = useState('');
  const { user, logout } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 20);
    window.addEventListener('scroll', onScroll);
    return () => window.removeEventListener('scroll', onScroll);
  }, []);

  useEffect(() => { setMenuOpen(false); }, [location]);

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchVal.trim()) {
      navigate(`/search?q=${encodeURIComponent(searchVal.trim())}`);
      setSearchOpen(false);
      setSearchVal('');
    }
  };

  const handleLogout = async () => {
    await logout();
    navigate('/login?logout=true');
  };

  const isActive = (path) => location.pathname === path;

  return (
    <header className={`navbar ${scrolled ? 'scrolled' : ''}`}>
      <div className="container navbar__inner">
        {/* Logo */}
        <Link to="/" className="navbar__logo">
          <span className="navbar__logo-icon">🌿</span>
          <span className="navbar__logo-text">EcoBuddy</span>
        </Link>

        {/* Nav Links */}
        <nav className={`navbar__links ${menuOpen ? 'open' : ''}`}>
          <Link to="/"         className={isActive('/')         ? 'active' : ''}>Home</Link>
          <Link to="/search"   className={isActive('/search')   ? 'active' : ''}>Shop</Link>
          <Link to="/dashboard" className={isActive('/dashboard') ? 'active' : ''}>Dashboard</Link>
        </nav>

        {/* Right Controls */}
        <div className="navbar__actions">
          {/* Search Toggle */}
          <button
            className="navbar__icon-btn"
            onClick={() => setSearchOpen((s) => !s)}
            aria-label="Search"
          >
            🔍
          </button>

          {user?.loggedIn ? (
            <div className="navbar__profile-group">
              <Link to="/dashboard" className="navbar__profile-badge" title="Go to Dashboard">
                <span className="navbar__profile-avatar">👤</span>
                <span className="navbar__profile-name">{user.firstName}</span>
              </Link>
              <button
                onClick={handleLogout}
                className="btn btn-outline navbar__btn-sm navbar__logout-btn"
              >
                Logout
              </button>
            </div>
          ) : (
            <>
              <Link to="/login"  className="btn btn-outline navbar__btn-sm">Login</Link>
              <Link to="/signup" className="btn btn-primary navbar__btn-sm">Sign Up</Link>
            </>
          )}

          {/* Hamburger */}
          <button
            className={`navbar__hamburger ${menuOpen ? 'open' : ''}`}
            onClick={() => setMenuOpen((m) => !m)}
            aria-label="Menu"
          >
            <span /><span /><span />
          </button>
        </div>
      </div>

      {/* Search Bar Dropdown */}
      <div className={`navbar__search-bar ${searchOpen ? 'open' : ''}`}>
        <div className="container">
          <form onSubmit={handleSearch} className="navbar__search-form">
            <input
              type="text"
              value={searchVal}
              onChange={(e) => setSearchVal(e.target.value)}
              placeholder="Search eco-friendly alternatives..."
              autoFocus={searchOpen}
            />
            <button type="submit" className="btn btn-primary">Search</button>
          </form>
        </div>
      </div>
    </header>
  );
}
