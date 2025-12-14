import React, { useState, useEffect } from 'react';
import ProductCard from '../components/ProductCard';
import '../styles/ShopAll.css';

const ShopAll = () => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        // Fetch all sweets from the backend
        fetch('http://localhost:8080/api/sweets')
            .then(res => {
                if (!res.ok) {
                    throw new Error('Failed to fetch products');
                }
                return res.json();
            })
            .then(data => {
                setProducts(data);
                setLoading(false);
            })
            .catch(err => {
                console.error("Error fetching products:", err);
                setError(err.message);
                setLoading(false);
            });
    }, []);

    // Scroll to top on mount
    useEffect(() => {
        window.scrollTo(0, 0);
    }, []);

    if (loading) return <div className="shop-loading">Loading products...</div>;
    if (error) return <div className="shop-error">Error: {error}</div>;

    return (
        <div className="shop-page">
            <div className="shop-header">
                <h1>Shop All</h1>
                <p>Discover our complete collection of traditional delights</p>

                {/* Optional: Filter controls could go here as shown in screenshot "Filter v Date, new to old v" */}
                {/* For now we just keep the header simple as per immediate request */}
            </div>

            <div className="products-grid container">
                {products.length > 0 ? (
                    products.map(product => (
                        <ProductCard key={product.id} product={product} />
                    ))
                ) : (
                    <div className="no-products">No products found.</div>
                )}
            </div>
        </div>
    );
};

export default ShopAll;
