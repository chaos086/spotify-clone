import React from 'react';
import './Home.css';

const Home = () => {
  return (
    <div>
      <h1>Bienvenido a Spotify Clone</h1>
      <p>Explora listas destacadas, artistas populares y recomendaciones.</p>
      <div className="grid">
        {Array.from({ length: 8 }).map((_, i) => (
          <div className="card" key={i}>
            <div className="cover" />
            <div className="name">Lista #{i+1}</div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Home;
