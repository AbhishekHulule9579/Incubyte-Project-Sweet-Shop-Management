import React, { useState, useEffect } from 'react';
import ProductCard from '../components/ProductCard';
import '../styles/ShopAll.css'; // Reusing the same grid styles

const CategoryPage = ({ category }) => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        setLoading(true);
        // Fetch sweets by category
        // Ensure the category name passed here matches what the backend expects
        fetch(`http://localhost:8080/api/sweets/category/${category}`)
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
    }, [category]); // Re-run when category changes

    // Scroll to top when category changes
    useEffect(() => {
        window.scrollTo(0, 0);
    }, [category]);

    if (loading) return <div className="shop-loading">Loading {category}...</div>;
    if (error) return <div className="shop-error">Error: {error}</div>;

    return (
        <div className="shop-page">
            <div className="shop-header">
                <h1>{category}</h1>
                <p>Explore our premium collection of {category}</p>
            </div>

            <div className="products-grid container">
                {products.length > 0 ? (
                    products.map(product => (
                        <ProductCard key={product.id} product={product} />
                    ))
                ) : (
                    <div className="no-products">No {category} found.</div>
                )}
            </div>
        </div>
    );
};

export default CategoryPage;
