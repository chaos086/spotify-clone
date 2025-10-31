import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FiHome, FiMusic, FiLayers, FiSettings, FiLogOut, FiUser } from 'react-icons/fi';
import './Sidebar.css';

const Sidebar = () => {
  const navigate = useNavigate();
  const isAuthenticated = !!localStorage.getItem('token');
  const role = localStorage.getItem('role');

  const logout = () => {
    localStorage.clear();
    navigate('/');
  };

  return (
    <aside className="sidebar">
      <div className="logo">Spotify Clone</div>
      <nav>
        <Link to="/"><FiHome /> Inicio</Link>
        <Link to="/library"><FiLayers /> Biblioteca</Link>
        {role === 'admin' && <Link to="/admin"><FiSettings /> Admin</Link>}
      </nav>
      <div className="auth">
        {isAuthenticated ? (
          <button onClick={logout}><FiLogOut /> Salir</button>
        ) : (
          <Link to="/login"><FiUser /> Ingresar</Link>
        )}
      </div>
    </aside>
  );
};

export default Sidebar;
