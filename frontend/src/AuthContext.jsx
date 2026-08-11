import { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext({
  user: null,
  loading: true,
  checkAuthStatus: async () => {},
  logout: async () => {},
});

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  const checkAuthStatus = async () => {
    try {
      const res = await fetch('/api/auth/status', { credentials: 'include' });
      const data = await res.json();
      if (data.loggedIn) {
        setUser({
          loggedIn: true,
          firstName: data.firstName || 'User',
          email: data.email || '',
        });
      } else {
        setUser({ loggedIn: false });
      }
    } catch {
      setUser({ loggedIn: false });
    } finally {
      setLoading(false);
    }
  };

  const logout = async () => {
    try {
      await fetch('/api/auth/logout', { method: 'POST', credentials: 'include' });
    } catch (e) {
      console.error('Logout error:', e);
    } finally {
      setUser({ loggedIn: false });
    }
  };

  useEffect(() => {
    checkAuthStatus();
  }, []);

  return (
    <AuthContext.Provider value={{ user, loading, checkAuthStatus, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
