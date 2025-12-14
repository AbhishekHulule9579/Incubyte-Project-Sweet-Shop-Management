import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/SearchModal.css';

const SearchModal = ({ onClose }) => {
    const [query, setQuery] = useState('');
    const [sweets, setSweets] = useState([]);
    const [suggestions, setSuggestions] = useState([]);
    const [productResults, setProductResults] = useState([]);
    const modalRef = useRef(null);
    const navigate = useNavigate();

    // Close on outside click
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (modalRef.current && !modalRef.current.contains(event.target)) {
                onClose();
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, [onClose]);

    // Fetch sweets on mount
    useEffect(() => {
        const fetchSweets = async () => {
            try {
                // In a real app one might search via API, but for this size, client-side filter is fine
                // Assuming public access to get sweets list or we handle auth if needed. 
                // Using the admin path might need auth, checking if there is a public one
                // AdminDashboard uses /api/sweets, usually GET /api/sweets is public in this design from context
                const response = await fetch('http://localhost:8080/api/sweets');
                if (response.ok) {
                    const data = await response.json();
                    console.log("SearchModal: Fetched sweets:", data);
                    setSweets(data);
                } else {
                    console.error("Failed to fetch sweets for search. Status:", response.status);
                }
            } catch (error) {
                console.error("Error fetching sweets:", error);
            }
        };
        fetchSweets();
    }, []);

    // Filter logic
    useEffect(() => {
        if (!query.trim()) {
            setSuggestions([]);
            setProductResults([]);
            return;
        }

        const lowerQuery = query.toLowerCase();

        // Suggestions: Simple name matching
        const suggestedItems = sweets
            .filter(item => item.name.toLowerCase().includes(lowerQuery))
            .slice(0, 5); // Limit to 5

        setSuggestions(suggestedItems);

        // Products: More detailed match (name or description)
        const productItems = sweets
            .filter(item =>
                item.name.toLowerCase().includes(lowerQuery) ||
                (item.description && item.description.toLowerCase().includes(lowerQuery))
            )
            .slice(0, 4); // Limit to 4 for view

        setProductResults(productItems);

    }, [query, sweets]);

    const handleProductClick = (sweet) => {
        // Navigate to product detail (or cart/shop for now if detail page doesn't exist)
        // Since we don't have a specific product detail page in the file list, we might just log or go to shop
        // For now, let's close modal.
        console.log("Clicked product:", sweet.name);
        onClose();
        // navigate(`/product/${sweet.id}`); // Placeholder for future
    };

    // Highlight text helper
    const HighlightText = ({ text, highlight }) => {
        if (!highlight.trim()) return <span>{text}</span>;
        const parts = text.split(new RegExp(`(${highlight})`, 'gi'));
        return (
            <span>
                {parts.map((part, i) =>
                    part.toLowerCase() === highlight.toLowerCase() ?
                        <span key={i} className="highlight">{part}</span> : part
                )}
            </span>
        );
    };

    return (
        <div className="search-modal-overlay">
            <div className="search-modal-container" ref={modalRef}>
                <div className="search-header">
                    <input
                        type="text"
                        placeholder="Search for sweets, namkeen..."
                        autoFocus
                        value={query}
                        onChange={(e) => setQuery(e.target.value)}
                    />
                    {query && <button className="close-btn-icon" onClick={() => setQuery('')}>×</button>}
                    <button className="search-icon">🔍</button>
                    <button className="close-btn-icon" onClick={onClose} style={{ marginLeft: '10px' }}>CLOSE</button>
                </div>

                <div className="search-results-body">
                    {/* Left Column: Suggestions */}
                    <div className="results-column suggestions-col">
                        <h4 className="column-title">Suggestions</h4>
                        <ul className="suggestion-list">
                            {suggestions.length > 0 ? (
                                suggestions.map(item => (
                                    <li key={item.id} className="suggestion-item" onClick={() => setQuery(item.name)}>
                                        <HighlightText text={item.name} highlight={query} />
                                    </li>
                                ))
                            ) : (
                                query && <li style={{ color: '#999', fontStyle: 'italic' }}>No suggestions found</li>
                            )}
                        </ul>
                    </div>

                    {/* Right Column: Products */}
                    <div className="results-column products-col">
                        <h4 className="column-title">Products</h4>
                        {productResults.length > 0 ? (
                            productResults.map(product => (
                                <div key={product.id} className="product-result-item" onClick={() => handleProductClick(product)}>
                                    <img
                                        src={product.imageUrl || 'https://placehold.co/60x60?text=Sweet'}
                                        alt={product.name}
                                        className="result-img"
                                    />
                                    <div className="result-info">
                                        <h4><HighlightText text={product.name} highlight={query} /></h4>
                                        <p>₹ {product.price}</p>
                                    </div>
                                </div>
                            ))
                        ) : (
                            query && <p style={{ color: '#999', marginTop: '10px' }}>No products match your search.</p>
                        )}
                    </div>
                </div>

                <div className="search-footer">
                    {query ? `Results for "${query}"` : 'Start typing to see results...'}
                </div>
            </div>
        </div>
    );
};

export default SearchModal;
