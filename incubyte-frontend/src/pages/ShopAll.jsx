import React, { useState, useEffect } from 'react';
import ProductCard from '../components/ProductCard';
import '../styles/ShopAll.css';

const ShopAll = () => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Filter States
    const [searchQuery, setSearchQuery] = useState('');
    const [selectedCategory, setSelectedCategory] = useState('All');
    const [maxQuantity, setMaxQuantity] = useState(200);

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

    // Filter Logic
    const filteredProducts = products.filter(product => {
        const matchesSearch = product.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
            product.description?.toLowerCase().includes(searchQuery.toLowerCase());
        const matchesCategory = selectedCategory === 'All' || product.category?.name === selectedCategory;
        const matchesQuantity = product.quantity <= maxQuantity;

        return matchesSearch && matchesCategory && matchesQuantity;
    });

    if (loading) return <div className="shop-loading">Loading products...</div>;
    if (error) return <div className="shop-error">Error: {error}</div>;

    return (
        <div className="shop-page">
            <div className="shop-header">
                <h1>Sweets</h1>
                <p>Discover our complete collection of traditional delights</p>
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
                <div className="filter-group">
                    <select
                        value={selectedCategory}
                        onChange={(e) => setSelectedCategory(e.target.value)}
                        style={{ padding: '8px 12px', borderRadius: '4px', border: '1px solid #ddd' }}
                    >
                        <option value="All">All Categories</option>
                        <option value="Sweet">Sweet</option>
                        <option value="Namkeen">Namkeen</option>
                        <option value="Gifting">Gifting</option>
                        <option value="Dry-Fruits">Dry-Fruits</option>
                    </select>
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
                    <div className="no-products">No products found.</div>
                )}
            </div>
        </div>
    );
};

export default ShopAll;
