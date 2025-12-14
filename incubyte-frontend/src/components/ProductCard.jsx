import React, { useState } from 'react';
import '../styles/ProductCard.css';

const ProductCard = ({ product }) => {
    const [quantity, setQuantity] = useState(1);

    const handleQuantityChange = (type) => {
        if (type === 'increment') {
            setQuantity(prev => prev + 1);
        } else if (type === 'decrement' && quantity > 1) {
            setQuantity(prev => prev - 1);
        }
    };

    const handleAddToCart = () => {
        // Placeholder for future cart logic
        console.log(`Added ${quantity} of ${product.name} to cart`);
        // In a real app, we'd use a context or redux action here
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
                    <div className="quantity-selector">
                        <button
                            onClick={() => handleQuantityChange('decrement')}
                            className="qty-btn"
                        >
                            −
                        </button>
                        <span className="qty-value">{quantity}</span>
                        <button
                            onClick={() => handleQuantityChange('increment')}
                            className="qty-btn"
                        >
                            +
                        </button>
                    </div>

                    <button className="quick-add-btn" onClick={handleAddToCart}>
                        Quick Add
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ProductCard;
