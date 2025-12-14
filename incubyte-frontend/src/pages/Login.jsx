import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import '../styles/Auth.css';

const Login = () => {
    const [role, setRole] = useState('CUSTOMER'); // 'CUSTOMER' | 'ADMIN'
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const { login } = useAuth();

    // Sliding toggle logic
    const toggleRole = () => {
        setRole(prev => prev === 'CUSTOMER' ? 'ADMIN' : 'CUSTOMER');
        setError('');
    };

    // Auto-dismiss error after 3 seconds
    useEffect(() => {
        if (error) {
            const timer = setTimeout(() => setError(''), 3000);
            return () => clearTimeout(timer);
        }
    }, [error]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        try {
            const response = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password, role })
            });

            const text = await response.text();
            let data;
            try {
                data = JSON.parse(text);
            } catch (e) {
                data = { message: text };
            }

            if (response.ok) {
                // Use AuthContext login function
                login(data.token, data.role, email);
                // Navigate to home
                navigate('/');
            } else {
                // Show red error rectangle
                setError(data.message || 'Invalid credentials');
            }
        } catch (err) {
            setError('Connection failed. Please check backend.');
        }
    };

    return (
        <div className="auth-page">
            <div className="auth-container">
                <h2 className="auth-title">Log In</h2>

                {/* Sliding Toggle */}
                <div className="toggle-container" onClick={toggleRole}>
                    <div className="toggle-switch">
                        <div
                            className="toggle-slider"
                            style={{ transform: role === 'ADMIN' ? 'translateX(100%)' : 'translateX(0)' }}
                        />
                        <span className={`toggle-option ${role === 'CUSTOMER' ? 'active' : ''}`}>Customer</span>
                        <span className={`toggle-option ${role === 'ADMIN' ? 'active' : ''}`}>Admin</span>
                    </div>
                </div>

                {/* Error Box (Red Rectangle) */}
                {error && <div className="error-rectangle">⚠️ {error}</div>}

                <form className="auth-form" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Email</label>
                        <input
                            type="email"
                            required
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                    </div>
                    <div className="form-group">
                        <label>Password</label>
                        <input
                            type="password"
                            required
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>

                    <button type="submit" className="auth-btn">
                        {role === 'ADMIN' ? 'Admin Login' : 'Sign In'}
                    </button>
                </form>

                <div className="auth-links">
                    <span className="link" onClick={() => navigate('/forgot-password')}>Forgot your password?</span>

                    {/* Only show "New Customer" if role is CUSTOMER */}
                    {role === 'CUSTOMER' && (
                        <div className="new-customer-section">
                            <h3>New Customer?</h3>
                            <p style={{ fontSize: '0.8rem', color: '#666', marginBottom: '0.5rem' }}>Sign up for early Sale access</p>
                            <button
                                className="auth-btn"
                                style={{ backgroundColor: '#d4af37', marginTop: 0 }}
                                onClick={() => navigate('/register')}
                            >
                                Register
                            </button>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Login;
