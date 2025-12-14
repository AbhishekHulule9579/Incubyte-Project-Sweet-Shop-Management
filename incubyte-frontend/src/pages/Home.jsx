import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Home.css';
import heroImage from '../assets/hero.jpg';

const Home = () => {
    const navigate = useNavigate();

    const [categories, setCategories] = useState([
        { id: 'Sweet', name: 'Sweet', count: 0, image: null, bgColor: '#ffe4e1', path: '/sweet' },
        { id: 'Namkeen', name: 'Namkeen', count: 0, image: null, bgColor: '#f0f8ff', path: '/namkeen' },
        { id: 'Dry-Fruits', name: 'Dry Fruits', count: 0, image: null, bgColor: '#e6e6fa', path: '/dry-fruits' },
        { id: 'Gifting', name: 'Gifting', count: 0, image: null, bgColor: '#f5f5dc', path: '/gifting' }
    ]);

    useEffect(() => {
        fetch('http://localhost:8080/api/sweets')
            .then(res => res.json())
            .then(data => {
                const stats = {
                    'Sweet': { count: 0, maxQty: -1, image: null },
                    'Namkeen': { count: 0, maxQty: -1, image: null },
                    'Dry-Fruits': { count: 0, maxQty: -1, image: null },
                    'Gifting': { count: 0, maxQty: -1, image: null }
                };

                data.forEach(item => {
                    const catName = item.category?.name;
                    if (catName && stats[catName]) {
                        stats[catName].count++;
                        // Track image of item with highest quantity
                        if (item.quantity > stats[catName].maxQty) {
                            stats[catName].maxQty = item.quantity;
                            if (item.imageUrl) {
                                stats[catName].image = item.imageUrl;
                            }
                        }
                    }
                });

                setCategories(prev => prev.map(cat => ({
                    ...cat,
                    count: stats[cat.id]?.count || 0,
                    image: stats[cat.id]?.image || null
                })));
            })
            .catch(err => console.error("Failed to load collections data", err));
    }, []);

    const handleCategoryClick = (path) => {
        navigate(path);
    };

    return (
        <div className="home-page">
            {/* Hero Section */}
            <section className="hero-section" style={{ backgroundImage: `linear-gradient(rgba(0,0,0,0.3), rgba(0,0,0,0.3)), url(${heroImage})` }}>
                <div className="hero-content">
                    <h2>Traditional Indian Sweets</h2>
                    <p>Handcrafted with love and pure ingredients.</p>
                    <button className="cta-btn">Order Now</button>
                </div>
            </section>

            {/* Value Props */}
            <section className="value-props container">
                <div className="prop-item">
                    <span className="prop-icon">🚚</span>
                    <h3>National Shipping</h3>
                    <p>in 3-5 days</p>
                </div>
                <div className="prop-item">
                    <span className="prop-icon">🕒</span>
                    <h3>15 Days Shelf Life</h3>
                    <p>Freshly made</p>
                </div>
                <div className="prop-item">
                    <span className="prop-icon">🚫</span>
                    <h3>No Preservatives</h3>
                    <p>100% Natural</p>
                </div>
            </section>

            {/* Categories Grid */}
            <section className="categories-section container">
                <div className="section-header">
                    <h2>Our Collections</h2>
                    <p>Explore our wide range of delicacies</p>
                </div>
                <div className="categories-grid">
                    {categories.map(cat => (
                        <div
                            key={cat.id}
                            className="category-card"
                            onClick={() => handleCategoryClick(cat.path)}
                            style={{ cursor: 'pointer' }}
                        >
                            <div
                                className="cat-image"
                                style={{
                                    backgroundColor: cat.bgColor,
                                    backgroundImage: cat.image ? `url(${cat.image})` : 'none',
                                    backgroundSize: 'cover',
                                    backgroundPosition: 'center'
                                }}
                            >
                                {!cat.image && <span style={{ opacity: 0.3, fontSize: '2rem' }}>🍬</span>}
                            </div>
                            <h3>{cat.name}</h3>
                            <p>{cat.count} Products</p>
                        </div>
                    ))}
                </div>
            </section>
        </div>
    );
};

export default Home;
