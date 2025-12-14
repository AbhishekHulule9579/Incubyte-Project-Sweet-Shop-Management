import React, { useState, useEffect } from 'react';
import ProductCard from '../components/ProductCard';
import '../styles/ShopAll.css'; // Reusing the same grid styles

const CategoryPage = ({ category }) => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Filter States
    const [searchQuery, setSearchQuery] = useState('');
    const [maxQuantity, setMaxQuantity] = useState(200);

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

    // Filter Logic
    const filteredProducts = products.filter(product => {
        const matchesSearch = product.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
            product.description?.toLowerCase().includes(searchQuery.toLowerCase());
        const matchesQuantity = product.quantity <= maxQuantity;

        return matchesSearch && matchesQuantity;
    });

    if (loading) return <div className="shop-loading">Loading {category}...</div>;
    if (error) return <div className="shop-error">Error: {error}</div>;

    return (
        <div className="shop-page">
            <div className="shop-header">
                <h1>{category}</h1>
                <p>Explore our premium collection of {category}</p>
            </div>

            {/* Filter Bar */}
            <div className="filter-bar" style={{ display: 'flex', gap: '20px', alignItems: 'center', marginBottom: '20px', padding: '15px', backgroundColor: '#f8f9fa', borderRadius: '8px', flexWrap: 'wrap' }}>
                <div className="filter-group">
                    <input
                        type="text"
                        placeholder="Search sweets..."
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        style={{ padding: '8px 12px', borderRadius: '4px', border: '1px solid #ddd', minWidth: '200px' }}
                    />
                </div>
                <div className="filter-group" style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                    <label style={{ whiteSpace: 'nowrap' }}>Max Qty: {maxQuantity}</label>
                    <input
                        type="range"
                        min="0"
                        max="200"
                        value={maxQuantity}
                        onChange={(e) => setMaxQuantity(Number(e.target.value))}
                        style={{ minWidth: '150px' }}
                    />
                </div>
            </div>

            <div className="products-grid container">
                {filteredProducts.length > 0 ? (
                    filteredProducts.map(product => (
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
