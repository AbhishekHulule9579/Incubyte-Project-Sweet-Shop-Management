import React, { createContext, useState, useContext, useEffect } from 'react';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(null);
    const [role, setRole] = useState(null);
    const [loading, setLoading] = useState(true);

    // Initialize auth state from sessionStorage on mount
    useEffect(() => {
        const storedToken = sessionStorage.getItem('token');
        const storedRole = sessionStorage.getItem('role');
        const storedEmail = sessionStorage.getItem('email');

        if (storedToken && storedRole) {
            setToken(storedToken);
            setRole(storedRole);
            setUser({ email: storedEmail });
        }
        setLoading(false);
    }, []);

    const login = (authToken, userRole, userEmail) => {
        sessionStorage.setItem('token', authToken);
        sessionStorage.setItem('role', userRole);
        sessionStorage.setItem('email', userEmail);

        setToken(authToken);
        setRole(userRole);
        setUser({ email: userEmail });
    };

    const logout = () => {
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('role');
        sessionStorage.removeItem('email');

        setToken(null);
        setRole(null);
        setUser(null);
    };

    const isAuthenticated = () => {
        return !!token;
    };

    const isAdmin = () => {
        return role === 'ADMIN';
    };

    const isCustomer = () => {
        return role === 'CUSTOMER';
    };

    const value = {
        user,
        token,
        role,
        login,
        logout,
        isAuthenticated,
        isAdmin,
        isCustomer,
        loading
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};
