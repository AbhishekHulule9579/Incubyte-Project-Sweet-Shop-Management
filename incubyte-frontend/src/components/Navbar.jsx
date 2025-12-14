import React, { useState, useRef, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import SearchModal from './SearchModal';
import '../styles/Navbar.css';

const Navbar = () => {
    const navigate = useNavigate();
    const { isAuthenticated, isAdmin, isCustomer, user, logout } = useAuth();
    const [showDropdown, setShowDropdown] = useState(false);
    const [showSearchModal, setShowSearchModal] = useState(false);
    const dropdownRef = useRef(null);

    // Close dropdown when clicking outside
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setShowDropdown(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    const handleLogout = () => {
        logout();
        setShowDropdown(false);
        navigate('/');
    };

    const handleProfileClick = () => {
        if (isAuthenticated()) {
            setShowDropdown(!showDropdown);
        } else {
            navigate('/login');
        }
    };

    return (
        <header className="main-header">
            <div className="container header-content">
                <Link to="/" className="logo">
                    <h1>SWEET CORNER</h1>
                    <span className="tagline">SINCE 1990</span>
                </Link>
                <nav className="main-nav">
                    <ul>
                        <li><Link to="/shop">Shop All</Link></li>
                        <li><Link to="/sweets">Sweets</Link></li>
                        <li><Link to="/dry-fruits">Dry-Fruits</Link></li>
                        <li><Link to="/namkeen">Namkeen</Link></li>
                        <li><Link to="/gifting">Gifting</Link></li>
                        <li><a href="#">About Us</a></li>
                    </ul>
                </nav>
                <div className="header-actions">
                    <button className="icon-btn" onClick={() => setShowSearchModal(true)}>🔍</button>

                    {/* Profile Button with Dropdown */}
                    <div className="profile-dropdown" ref={dropdownRef}>
                        <button className="icon-btn" onClick={handleProfileClick}>
                            👤
                        </button>

                        {/* Dropdown Menu - Only show when authenticated */}
                        {isAuthenticated() && showDropdown && (
                            <div className="dropdown-menu">
                                <div className="dropdown-header">
                                    <span className="user-email">{user?.email}</span>
                                    <span className="user-role">{isAdmin() ? 'Admin' : 'Customer'}</span>
                                </div>
                                <div className="dropdown-divider"></div>
                                <button className="dropdown-item logout-btn" onClick={handleLogout}>
                                    🚪 Logout
                                </button>
                            </div>
                        )}
                    </div>

                    {/* Admin Controls - Only visible to Admin */}
                    {isAdmin() && (
                        <button
                            className="icon-btn admin-btn"
                            title="Admin Dashboard"
                            onClick={() => navigate('/admin')}
                        >
                            ⚙️ Admin Dashboard
                        </button>
                    )}

                    {/* Cart Button - Only visible to Customers */}
                    {isCustomer() && (
                        <button className="icon-btn" onClick={() => navigate('/cart')}>🛍️</button>
                    )}
                </div>
            </div>
            {showSearchModal && <SearchModal onClose={() => setShowSearchModal(false)} />}
        </header>
    );
};

export default Navbar;
