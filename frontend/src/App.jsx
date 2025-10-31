import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Sidebar from './components/Sidebar';
import Header from './components/Header';
import Home from './pages/Home';
import Login from './pages/Login';
import Admin from './pages/Admin';
import Library from './pages/Library';
import PlayerBar from './components/PlayerBar';
import './styles/global.css';

const Layout = ({ children }) => (
  <div className="layout">
    <Sidebar />
    <div className="content">
      <Header />
      <div className="page">{children}</div>
    </div>
    <PlayerBar />
  </div>
);

const App = () => {
  const isAuthenticated = !!localStorage.getItem('token');
  const role = localStorage.getItem('role');

  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/" element={<Layout><Home /></Layout>} />
      <Route path="/library" element={isAuthenticated ? <Layout><Library /></Layout> : <Navigate to="/login" />} />
      <Route path="/admin" element={isAuthenticated && role === 'admin' ? <Layout><Admin /></Layout> : <Navigate to="/login" />} />
      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  );
};

export default App;
