import React from 'react';
import axios from 'axios';
import useSWR from 'swr';
import './Library.css';

const fetcher = (url) => axios.get(url, { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } }).then(r => r.data);

const Library = () => {
  const { data: playlists } = useSWR('/api/playlists', fetcher);

  return (
    <div>
      <h2>Tu Biblioteca</h2>
      <div className="grid">
        {playlists?.map(p => (
          <div className="card" key={p._id}>
            <div className="cover" />
            <div className="name">{p.name}</div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Library;
