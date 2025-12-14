import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { CartProvider } from './context/CartContext';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import ForgotPassword from './pages/ForgotPassword';
import AdminDashboard from './pages/AdminDashboard';
import ShopAll from './pages/ShopAll';
import CategoryPage from './pages/CategoryPage';
import Cart from './pages/Cart';
import Orders from './pages/Orders';
import Layout from './components/Layout';
import './App.css';

function App() {
  return (
    <AuthProvider>
      <CartProvider>
        <Router>
          <Routes>
            <Route element={<Layout />}>
              <Route path="/" element={<Home />} />
              <Route path="/sweets" element={<ShopAll />} />
              <Route path="/cart" element={<Cart />} />
              <Route path="/orders" element={<Orders />} />
              <Route path="/sweet" element={<CategoryPage category="Sweet" />} />
              <Route path="/namkeen" element={<CategoryPage category="Namkeen" />} />
              <Route path="/dry-fruits" element={<CategoryPage category="Dry-Fruits" />} />
              <Route path="/gifting" element={<CategoryPage category="Gifting" />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/forgot-password" element={<ForgotPassword />} />
              <Route path="/admin" element={<AdminDashboard />} />
              <Route path="/cart" element={<Cart />} />
            </Route>
          </Routes>
        </Router>
      </CartProvider>
    </AuthProvider>
  );
}

export default App;
