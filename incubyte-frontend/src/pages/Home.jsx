import React from 'react';
import '../styles/Home.css';
import heroImage from '../assets/hero.jpg';

const Home = () => {
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
                    {/* Mock Categories - In real app, these come from API */}
                    <div className="category-card">
                        <div className="cat-image placeholder-img" style={{ backgroundColor: '#ffe4e1' }}></div>
                        <h3>Sweets</h3>
                        <p>24 Products</p>
                    </div>
                    <div className="category-card">
                        <div className="cat-image placeholder-img" style={{ backgroundColor: '#f0f8ff' }}></div>
                        <h3>Namkeen</h3>
                        <p>12 Products</p>
                    </div>
                    <div className="category-card">
                        <div className="cat-image placeholder-img" style={{ backgroundColor: '#e6e6fa' }}></div>
                        <h3>Dry Fruits</h3>
                        <p>8 Products</p>
                    </div>
                    <div className="category-card">
                        <div className="cat-image placeholder-img" style={{ backgroundColor: '#f5f5dc' }}></div>
                        <h3>Gifting</h3>
                        <p>15 Products</p>
                    </div>
                </div>
            </section>
        </div>
    );
};

export default Home;
