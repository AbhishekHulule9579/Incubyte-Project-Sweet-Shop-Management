import React, { useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import { useNavigate, Link } from 'react-router-dom';
import '../styles/Cart.css';

const Cart = () => {
    const { isAuthenticated, token } = useAuth(); // Get token
    const { cartItems, updateQuantity, removeFromCart, getCartTotal, clearCart } = useCart();
    const navigate = useNavigate();

    // Protect route: If not authenticated, redirect to login
    // This is a safety check, though Navbar handles the initial redirect

    const handleCheckout = async () => {
        if(!isAuthenticated()){
            alert("Please log in to complete your purchase !");
            navigate('/login',{state:{from:'/cart'}
            });
            return;
        }
        try {
            const response = await fetch('http://localhost:8080/api/sweets/purchase', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}` // Send token
                },
                body: JSON.stringify(cartItems.map(item => ({ id: item.id, quantity: item.quantity })))
            });

            if (!response.ok) {
                const data = await response.json();
                throw new Error(data.message || 'Purchase failed');
            }

            // const deliveryDate = new Date();
            // deliveryDate.setDate(deliveryDate.getDate() + 3); // 3 days delivery
            // alert(`Congratulations! You will receive your order on ${deliveryDate.toDateString()}.`);

            clearCart();
            // Redirect to Orders page to show the new order
            navigate('/orders');
        } catch (err) {
            alert('Checkout failed: ' + err.message);
        }
    };


    return (
        <div className="cart-page container">
            <h1 className="cart-title">Shopping Cart</h1>
            <div className="cart-breadcrumb">
                <Link to="/">Home</Link> &gt; Your Shopping Cart
            </div>

            {cartItems.length === 0 ? (
                <div className="empty-cart-message">
                    <p>Your cart is empty.</p>
                    <Link to="/shop" className="continue-shopping">Continue Shopping</Link>
                </div>
            ) : (
                <div className="cart-content">
                    <table className="cart-table">
                        <thead>
                            <tr>
                                <th className="th-product">Product</th>
                                <th className="th-price">Price</th>
                                <th className="th-qty">Quantity</th>
                                <th className="th-total">Total</th>
                            </tr>
                        </thead>
                        <tbody>
                            {cartItems.map(item => (
                                <tr key={item.id}>
                                    <td className="td-product">
                                        <div className="cart-item-info">
                                            <div className="cart-item-image">
                                                {item.imageUrl ? <img src={item.imageUrl} alt={item.name} /> : '🍬'}
                                            </div>
                                            <div className="cart-item-details">
                                                <div className="cart-item-name">{item.name}</div>
                                                <div className="cart-item-weight">Weight: 200g</div> {/* Placeholder if not in data */}
                                                <button
                                                    className="remove-btn"
                                                    onClick={() => removeFromCart(item.id)}
                                                >
                                                    Remove
                                                </button>
                                            </div>
                                        </div>
                                    </td>
                                    <td className="td-price">₹ {item.price}</td>
                                    <td className="td-qty">
                                        <div className="cart-qty-selector">
                                            <button onClick={() => updateQuantity(item.id, item.quantity - 1)}>−</button>
                                            <span>{item.quantity}</span>
                                            <button onClick={() => updateQuantity(item.id, item.quantity + 1)}>+</button>
                                        </div>
                                    </td>
                                    <td className="td-total">₹ {item.price * item.quantity}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>

                    <div className="cart-footer">
                        <div className="cart-totals">
                            <div className="subtotal-row">
                                <span>Subtotal</span>
                                <span className="subtotal-amount">₹ {getCartTotal()}</span>
                            </div>
                            <p className="tax-note">Tax included. Shipping calculated at checkout.</p>
                            <button className="checkout-btn" onClick={handleCheckout}>CHECK OUT</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Cart;
