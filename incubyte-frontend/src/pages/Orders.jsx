import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { Link } from 'react-router-dom';
import '../styles/Orders.css';

const Orders = () => {
    const { token, isAuthenticated } = useAuth();
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        if (isAuthenticated() && token) {
            fetchOrders();
        }
    }, [isAuthenticated, token]);

    const fetchOrders = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/sweets/orders', {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            if (!response.ok) throw new Error('Failed to fetch orders');
            const data = await response.json();
            setOrders(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <div className="loading-container">Loading your orders...</div>;

    return (
        <div className="orders-page container">
            <h1 className="page-title">Your Orders</h1>

            {orders.length === 0 ? (
                <div className="no-orders">
                    <p>You haven't placed any orders yet.</p>
                    <Link to="/shop" className="btn-primary">Start Shopping</Link>
                </div>
            ) : (
                <div className="orders-list">
                    {orders.map(order => (
                        <div key={order.id} className="order-card">
                            <div className="order-header">
                                <div className="order-info">
                                    <span className="order-date">
                                        Ordered on {new Date(order.orderDate).toLocaleDateString()}
                                    </span>
                                    <span className="order-id">Order #{order.id}</span>
                                </div>
                                <div className="order-total">
                                    Total: ₹ {order.totalAmount}
                                </div>
                            </div>

                            <div className="order-items">
                                {order.orderDetails.map(item => (
                                    <div key={item.id} className="order-item">
                                        <div className="item-image">
                                            {item.imageUrl ? <img src={item.imageUrl} alt={item.sweetName} /> : '🍬'}
                                        </div>
                                        <div className="item-details">
                                            <h4>{item.sweetName}</h4>
                                            <p>Qty: {item.quantity}</p>
                                            <p className="item-price">₹ {item.price}</p>
                                        </div>
                                    </div>
                                ))}
                            </div>

                            <div className="order-status">
                                <span className="status-badge success">Delivered by {new Date(new Date(order.orderDate).getTime() + 3 * 24 * 60 * 60 * 1000).toDateString()}</span>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default Orders;
