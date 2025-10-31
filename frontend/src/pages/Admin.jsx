import React from 'react';
import axios from 'axios';
import useSWR from 'swr';
import './Admin.css';

const fetcher = (url) => axios.get(url, { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } }).then(r => r.data);

const Admin = () => {
  const { data: stats } = useSWR('/api/admin/stats', fetcher);

  return (
    <div>
      <h2>Panel de Administración</h2>
      <div className="stats">
        <div className="stat"><span>Usuarios</span><strong>{stats?.users ?? '-'}</strong></div>
        <div className="stat"><span>Canciones</span><strong>{stats?.songs ?? '-'}</strong></div>
        <div className="stat"><span>Playlists</span><strong>{stats?.playlists ?? '-'}</strong></div>
      </div>
    </div>
  );
};

export default Admin;
