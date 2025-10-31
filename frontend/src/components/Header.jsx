import React from 'react';
import './Header.css';

const Header = () => {
  return (
    <header className="header">
      <input placeholder="Buscar canciones, artistas, álbumes..." />
      <div className="actions">
        <button>Explorar</button>
        <button>Géneros</button>
      </div>
    </header>
  );
};

export default Header;
