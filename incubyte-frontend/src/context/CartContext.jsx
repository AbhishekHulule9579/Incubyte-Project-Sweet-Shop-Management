import React, { createContext, useState, useContext, useEffect } from 'react';

const CartContext = createContext();

export const CartProvider = ({ children }) => {
        const [sessionId]=useState(()=>{
            let sid=localStorage.getItem("guest_session_id");
            if(!sid){
                sid=crypto.randomUUID();
                localStorage.setItem("guest_session_id",sid);
            }
            return sid;
    });

    const[cartItems,setCartItems]=useState(()=>{
        const savedCart=localStorage.getItem('cartItems');
        return savedCart ? JSON.parse(savedCart):[];
    });

    useEffect(() => {
        localStorage.setItem('cartItems', JSON.stringify(cartItems));
    }, [cartItems]);

    const addToCart = async (product, quantity) => {
        setCartItems(prevItems => {
            const existingItem = prevItems.find(item => item.id === product.id);
            if (existingItem) {
                return prevItems.map(item =>
                    item.id === product.id
                        ? { ...item, quantity: item.quantity + quantity }
                        : item
                );
            }
            return [...prevItems, { ...product, quantity }];
        });

    try{
        const cartItemData={
            sweetName:product.name,
            price:product.price,
            quantity:quantity,
            imageUrl:product.imageUrl
        };

        await fetch(`http://localhost:8080/api/guest/cart/add?sessionId=${sessionId}`,{
            method:"POST",
            headers:{"Content-Type":"application/json"},
            body:JSON.stringify(cartItemData)
        });
        console.log("Successfully saved to the redis");
    }catch(error){
        console.log("Failed to save to Redis: ",error)
    }
};

    const removeFromCart = (productId) => {
        setCartItems(prevItems => prevItems.filter(item => item.id !== productId));
    };

    const updateQuantity = (productId, newQuantity) => {
        if (newQuantity < 1) return;
        setCartItems(prevItems =>
            prevItems.map(item =>
                item.id === productId ? { ...item, quantity: newQuantity } : item
            )
        );
    };

    const clearCart = () => {
        setCartItems([]);
        fetch(`http://localhost:8080/api/guest/cart/clear?sessionId=${sessionId}`,{
            method:"DELETE"
        }).catch(err=>console.error(err));
        
    };

    const getCartTotal = () => {
        return cartItems.reduce((total, item) => total + (item.price * item.quantity), 0);
    };

    const getCartCount = () => {
        return cartItems.reduce((count, item) => count + item.quantity, 0);
    };

    return (
        <CartContext.Provider value={{
            cartItems,
            addToCart,
            removeFromCart,
            updateQuantity,
            clearCart,
            getCartTotal,
            getCartCount
        }}>
            {children}
        </CartContext.Provider>
    );
};

export const useCart = () => {
    return useContext(CartContext);
};
