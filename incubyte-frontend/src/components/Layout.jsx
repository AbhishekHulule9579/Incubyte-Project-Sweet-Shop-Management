import React from 'react';
import { Outlet } from 'react-router-dom';
import Navbar from './Navbar';

const Layout = () => {
    return (
        <>
            <Navbar />
            <div style={{ paddingTop: '50px', minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
                <Outlet />
            </div>
        </>
    );
};

export default Layout;
