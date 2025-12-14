import React from 'react';
import '../styles/AboutUs.css';
import developerPhoto from '../assets/Abhishek Hulule.jpg';

const AboutUs = () => {
    return (
        <div className="about-us-page">
            <div className="container">
                {/* Hero Section */}
                <section className="about-hero">
                    <h1>About Sweet Corner</h1>
                    <p className="subtitle">Crafting sweetness since 1990</p>
                </section>

                {/* Our Story */}
                <section className="about-section">
                    <h2>Our Story</h2>
                    <div className="content-block">
                        <p>
                            Welcome to <strong>Sweet Corner</strong>, where tradition meets taste! Established in 1990,
                            we have been serving authentic traditional Indian sweets for over three decades. Our journey
                            began with a simple mission: to bring the authentic flavors of India to every household.
                        </p>
                        <p>
                            At Sweet Corner, we believe that sweets are not just desserts – they are emotions, celebrations,
                            and memories wrapped in delicious flavors. Each sweet is handcrafted with love, using pure
                            ingredients and time-honored recipes passed down through generations.
                        </p>
                    </div>
                </section>

                {/* What We Offer */}
                <section className="about-section">
                    <h2>What We Offer</h2>
                    <div className="offerings-grid">
                        <div className="offering-card">
                            <span className="offering-icon">🍬</span>
                            <h3>Traditional Sweets</h3>
                            <p>Authentic Indian mithai made with pure ghee and premium ingredients</p>
                        </div>
                        <div className="offering-card">
                            <span className="offering-icon">🥜</span>
                            <h3>Premium Dry Fruits</h3>
                            <p>Handpicked nuts and dried fruits for health and taste</p>
                        </div>
                        <div className="offering-card">
                            <span className="offering-icon">🌶️</span>
                            <h3>Savory Namkeen</h3>
                            <p>Crispy and flavorful snacks for every occasion</p>
                        </div>
                        <div className="offering-card">
                            <span className="offering-icon">🎁</span>
                            <h3>Gift Hampers</h3>
                            <p>Beautifully curated gift boxes for your loved ones</p>
                        </div>
                    </div>
                </section>

                {/* Our Values */}
                <section className="about-section">
                    <h2>Our Values</h2>
                    <div className="values-list">
                        <div className="value-item">
                            <span className="value-icon">✨</span>
                            <div>
                                <h3>Quality First</h3>
                                <p>We use only the finest ingredients with no preservatives or artificial colors</p>
                            </div>
                        </div>
                        <div className="value-item">
                            <span className="value-icon">❤️</span>
                            <div>
                                <h3>Made with Love</h3>
                                <p>Every sweet is handcrafted with care and attention to detail</p>
                            </div>
                        </div>
                        <div className="value-item">
                            <span className="value-icon">🚚</span>
                            <div>
                                <h3>Fresh Delivery</h3>
                                <p>National shipping within 3-5 days, ensuring freshness</p>
                            </div>
                        </div>
                        <div className="value-item">
                            <span className="value-icon">🌿</span>
                            <div>
                                <h3>100% Natural</h3>
                                <p>No preservatives, just pure and natural ingredients</p>
                            </div>
                        </div>
                    </div>
                </section>

                {/* Developer Section */}
                <section className="about-section developer-section">
                    <h2>About the Developer</h2>
                    <div className="developer-card">
                        <div className="developer-avatar">
                            <img src={developerPhoto} alt="Abhishek Hulule" />
                        </div>
                        <div className="developer-info">
                            <h3>Abhishek Hulule</h3>
                            <p className="developer-title">Full Stack Developer</p>
                            <p className="developer-description">
                                This Sweet Corner e-commerce platform was developed by <strong>Abhishek Hulule</strong>,
                                a passionate full-stack developer dedicated to creating seamless digital experiences.
                                The platform combines modern web technologies with user-centric design to bring the
                                traditional sweet shopping experience online.
                            </p>
                            <p className="developer-description">
                                Built with React, Spring Boot, and MySQL, this application showcases a complete
                                e-commerce solution with features including user authentication, shopping cart,
                                order management, and an admin dashboard for inventory control.
                            </p>
                            <div className="tech-stack">
                                <span className="tech-badge">React</span>
                                <span className="tech-badge">Spring Boot</span>
                                <span className="tech-badge">MySQL</span>
                                <span className="tech-badge">JWT Auth</span>
                            </div>
                            <div className="social-links">
                                <a
                                    href="https://www.linkedin.com/in/abhishek-hulule-711566292/"
                                    target="_blank"
                                    rel="noopener noreferrer"
                                    className="social-btn linkedin"
                                >
                                    <span className="social-icon">💼</span>
                                    LinkedIn
                                </a>
                                <a
                                    href="https://github.com/AbhishekHulule9579"
                                    target="_blank"
                                    rel="noopener noreferrer"
                                    className="social-btn github"
                                >
                                    <span className="social-icon">💻</span>
                                    GitHub
                                </a>
                                <a
                                    href="https://github.com/AbhishekHulule9579/Incubyte-Project-Sweet-Shop-Management"
                                    target="_blank"
                                    rel="noopener noreferrer"
                                    className="social-btn project-repo"
                                >
                                    <span className="social-icon">📂</span>
                                    Project Repository
                                </a>
                            </div>
                        </div>
                    </div>
                </section>

                {/* Contact CTA */}
                <section className="about-section cta-section">
                    <h2>Experience the Sweetness</h2>
                    <p>Join thousands of satisfied customers who trust Sweet Corner for their sweet cravings</p>
                    <button className="cta-button" onClick={() => window.location.href = '/sweets'}>
                        Shop Now
                    </button>
                </section>
            </div>
        </div>
    );
};

export default AboutUs;
