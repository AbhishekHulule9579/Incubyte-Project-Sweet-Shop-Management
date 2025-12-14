import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import '../styles/AdminDashboard.css';

const AdminDashboard = () => {
    const { token, isAdmin } = useAuth();
    const navigate = useNavigate();
    const [sweets, setSweets] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [loading, setLoading] = useState(true);

    // Form State
    const [newItem, setNewItem] = useState({
        name: '', description: '', price: '', quantity: '', categoryName: 'Sweets', imageUrl: ''
    });

    // Image Upload State
    const [imageMode, setImageMode] = useState('url'); // 'url' | 'file'
    const [selectedFile, setSelectedFile] = useState(null);

    useEffect(() => {
        if (!isAdmin()) {
            navigate('/');
            return;
        }
        fetchSweets();
    }, [isAdmin, navigate]);

    const fetchSweets = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/sweets', {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (response.ok) {
                const data = await response.json();
                setSweets(data);
            }
        } catch (error) {
            console.error('Failed to fetch sweets', error);
        } finally {
            setLoading(false);
        }
    };

    const handleFileUpload = async () => {
        if (!selectedFile) return null;

        const formData = new FormData();
        formData.append('file', selectedFile);

        try {
            const response = await fetch('http://localhost:8080/api/sweets/upload', {
                method: 'POST',
                headers: { 'Authorization': `Bearer ${token}` },
                body: formData
            });

            if (response.ok) {
                const data = await response.json();
                return data.url;
            } else {
                alert('Upload failed: ' + await response.text());
                return null;
            }
        } catch (error) {
            console.error('Error uploading file', error);
            return null;
        }
    };

    const handleAddItem = async (e) => {
        e.preventDefault();

        let finalImageUrl = newItem.imageUrl;

        if (imageMode === 'file' && selectedFile) {
            const uploadedUrl = await handleFileUpload();
            if (uploadedUrl) {
                finalImageUrl = uploadedUrl;
            } else {
                return; // Stop if upload failed
            }
        }

        const itemPayload = { ...newItem, imageUrl: finalImageUrl };

        try {
            const response = await fetch('http://localhost:8080/api/sweets', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(itemPayload)
            });
            if (response.ok) {
                setShowModal(false);
                setNewItem({ name: '', description: '', price: '', quantity: '', categoryName: 'Sweets', imageUrl: '' });
                setSelectedFile(null);
                fetchSweets();
            } else {
                alert('From Backend: ' + await response.text());
            }
        } catch (error) {
            console.error('Error adding sweet', error);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Are you sure you want to delete this sweet?")) return;
        try {
            const response = await fetch(`http://localhost:8080/api/sweets/${id}`, {
                method: 'DELETE',
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (response.ok) {
                fetchSweets();
            }
        } catch (error) {
            console.error('Error deleting sweet', error);
        }
    };

    const handleRestock = async (id, quantity) => {
        if (!quantity || quantity <= 0) return;
        try {
            const response = await fetch(`http://localhost:8080/api/sweets/${id}/restock`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({ quantity: parseInt(quantity) })
            });
            if (response.ok) {
                fetchSweets();
            }
        } catch (error) {
            console.error('Error restocking', error);
        }
    };

    // Helper component for Restock Row
    const RestockControl = ({ id }) => {
        const [qty, setQty] = useState('');
        return (
            <div className="restock-control">
                <input type="number" min="1" value={qty} onChange={e => setQty(e.target.value)} placeholder="Qty" className="qty-input" />
                <button onClick={() => { handleRestock(id, qty); setQty(''); }} className="action-btn restock-btn">
                    Restock
                </button>
            </div>
        );
    };

    return (
        <div className="admin-dashboard container">
            <header className="dashboard-header">
                <h2>Admin Dashboard</h2>
                <button className="add-btn" onClick={() => setShowModal(true)}>+ Add New Sweet</button>
            </header>

            {loading ? <p>Loading inventory...</p> : (
                <div className="inventory-table-wrapper">
                    <table className="inventory-table">
                        <thead>
                            <tr>
                                <th>Image</th>
                                <th>Name</th>
                                <th>Category</th>
                                <th>Price</th>
                                <th>Stock</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {sweets.map(sweet => (
                                <tr key={sweet.id}>
                                    <td>
                                        <div className="img-preview">
                                            {sweet.imageUrl ? <img src={sweet.imageUrl} alt={sweet.name} /> : '📷'}
                                        </div>
                                    </td>
                                    <td>
                                        <div className="sweet-name">{sweet.name}</div>
                                        <small className="sweet-desc">{sweet.description}</small>
                                    </td>
                                    <td><span className="badge">{sweet.category?.name}</span></td>
                                    <td>₹{sweet.price}</td>
                                    <td className={sweet.quantity < 10 ? 'low-stock' : ''}>
                                        {sweet.quantity}
                                        {sweet.quantity === 0 && <span className="error-text"> (Out of Stock)</span>}
                                    </td>
                                    <td>
                                        <div className="action-group">
                                            <RestockControl id={sweet.id} />
                                            <button onClick={() => handleDelete(sweet.id)} className="action-btn delete-btn">
                                                Remove
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}

            {showModal && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h3>Add New Sweet</h3>
                        <form onSubmit={handleAddItem}>
                            <div className="form-group">
                                <label>Category</label>
                                <select
                                    value={newItem.categoryName}
                                    onChange={e => setNewItem({ ...newItem, categoryName: e.target.value })}
                                >
                                    <option value="Sweets">Sweets</option>
                                    <option value="Namkeen">Namkeen</option>
                                    <option value="Gifting">Gifting</option>
                                </select>
                            </div>
                            <div className="form-group">
                                <label>Name</label>
                                <input type="text" required value={newItem.name} onChange={e => setNewItem({ ...newItem, name: e.target.value })} />
                            </div>
                            <div className="form-group">
                                <label>Price (₹)</label>
                                <input type="number" required value={newItem.price} onChange={e => setNewItem({ ...newItem, price: e.target.value })} />
                            </div>
                            <div className="form-group">
                                <label>Initial Quantity</label>
                                <input type="number" required value={newItem.quantity} onChange={e => setNewItem({ ...newItem, quantity: e.target.value })} />
                            </div>

                            {/* Image Input with Toggle */}
                            <div className="form-group">
                                <label>Product Image</label>
                                <div className="toggle-container" onClick={() => setImageMode(prev => prev === 'url' ? 'file' : 'url')} style={{ marginBottom: '1rem', width: '200px' }}>
                                    <div className="toggle-switch">
                                        <div
                                            className="toggle-slider"
                                            style={{ transform: imageMode === 'file' ? 'translateX(100%)' : 'translateX(0)' }}
                                        />
                                        <span className={`toggle-option ${imageMode === 'url' ? 'active' : ''}`}>URL</span>
                                        <span className={`toggle-option ${imageMode === 'file' ? 'active' : ''}`}>Upload</span>
                                    </div>
                                </div>

                                {imageMode === 'url' ? (
                                    <input
                                        type="text"
                                        value={newItem.imageUrl}
                                        onChange={e => setNewItem({ ...newItem, imageUrl: e.target.value })}
                                        placeholder="https://..."
                                    />
                                ) : (
                                    <input
                                        type="file"
                                        accept="image/*"
                                        onChange={e => setSelectedFile(e.target.files[0])}
                                    />
                                )}
                            </div>

                            <div className="form-group">
                                <label>Description</label>
                                <textarea value={newItem.description} onChange={e => setNewItem({ ...newItem, description: e.target.value })}></textarea>
                            </div>
                            <div className="modal-actions">
                                <button type="button" onClick={() => setShowModal(false)} className="cancel-btn">Cancel</button>
                                <button type="submit" className="save-btn">Add Product</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AdminDashboard;
