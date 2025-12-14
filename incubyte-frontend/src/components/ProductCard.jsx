import React, { useState } from 'react';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import '../styles/ProductCard.css';

const ProductCard = ({ product }) => {
    const { addToCart } = useCart();
    const { isAdmin } = useAuth();
    const [quantity, setQuantity] = useState(1);

    const handleQuantityChange = (type) => {
        if (type === 'increment') {
            setQuantity(prev => prev + 1);
        } else if (type === 'decrement' && quantity > 1) {
            setQuantity(prev => prev - 1);
        }
    };

    const handleAddToCart = () => {
        addToCart(product, quantity);
        alert(`Added ${quantity} ${product.name}(s) to cart!`);
    };

    // Calculate discounted price if applicable (assuming backend might provide this, 
    // or we just show a fake discount for the UI demo based on the screenshot)
    const originalPrice = product.price + 20; // Fake original price for demo as per screenshot style

    return (
        <div className="product-card">
            <div className="product-image-container">
                {product.imageUrl ? (
                    <img src={product.imageUrl} alt={product.name} className="product-image" />
                ) : (
                    <div className="placeholder-image">🍬</div>
                )}
            </div>

            <div className="product-details">
                <h3 className="product-name">{product.name}</h3>

                <div className="product-pricing">
                    <span className="current-price">₹ {product.price}</span>
                    <span className="original-price">₹ {originalPrice}</span>
                </div>

                <div className="product-actions">
                    {!isAdmin() && (
                        <>
                            <div className="quantity-selector">
                                <button
                                    onClick={() => handleQuantityChange('decrement')}
                                    className="qty-btn"
                                    disabled={product.quantity === 0}
                                >
                                    −
                                </button>
                                <span className="qty-value">{quantity}</span>
                                <button
                                    onClick={() => handleQuantityChange('increment')}
                                    className="qty-btn"
                                    disabled={product.quantity === 0}
                                >
                                    +
                                </button>
                            </div>

                            <button
                                className={`quick-add-btn ${product.quantity === 0 ? 'disabled' : ''}`}
                                onClick={handleAddToCart}
                                disabled={product.quantity === 0}
                            >
                                {product.quantity === 0 ? 'Out of Stock' : 'Purchase'}
                            </button>
                        </>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ProductCard;
