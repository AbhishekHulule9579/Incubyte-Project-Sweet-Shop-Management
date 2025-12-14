import React from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const Cart = () => {
    const { isCustomer } = useAuth();
    const navigate = useNavigate();

    // Protect route (optional, since handled by conditional main link, but good for direct access)
    React.useEffect(() => {
        if (!isCustomer()) {
            navigate('/');
        }
    }, [isCustomer, navigate]);

    return (
        <div className="container" style={{ padding: '2rem 1rem' }}>
            <h2 style={{ fontFamily: 'var(--font-heading)', color: 'var(--primary-color)', marginBottom: '1rem' }}>
                Your Cart
            </h2>
            <div style={{ textAlign: 'center', padding: '4rem 0', color: 'var(--text-light)' }}>
                <span style={{ fontSize: '4rem', display: 'block', marginBottom: '1rem' }}>🛒</span>
                <p>Your cart is empty.</p>
                <button
                    onClick={() => navigate('/')}
                    style={{
                        marginTop: '1rem',
                        padding: '0.5rem 1.5rem',
                        backgroundColor: 'var(--accent-color)',
                        color: 'white',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer'
                    }}
                >
                    Start Shopping
                </button>
            </div>
        </div>
    );
};

export default Cart;
