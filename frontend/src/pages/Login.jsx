import React, { useState } from 'react';
import axios from 'axios';
import './Login.css';

const Login = () => {
  const [email, setEmail] = useState('admin@spotify.com');
  const [password, setPassword] = useState('Admin123!@');
  const [error, setError] = useState('');

  const submit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const { data } = await axios.post('/api/auth/login', { email, password });
      localStorage.setItem('token', data.token);
      localStorage.setItem('role', data.user.role);
      window.location.href = data.user.role === 'admin' ? '/admin' : '/library';
    } catch (err) {
      setError(err.response?.data?.message || 'Error de autenticación');
    }
  };

  return (
    <div className="login">
      <form onSubmit={submit}>
        <h2>Ingresar</h2>
        <input value={email} onChange={(e)=>setEmail(e.target.value)} placeholder="Email" />
        <input value={password} onChange={(e)=>setPassword(e.target.value)} type="password" placeholder="Contraseña" />
        {error && <div className="error">{error}</div>}
        <button type="submit">Entrar</button>
        <p>
          Usuario: <code>user@spotify.com</code> / <code>User123!@</code>
        </p>
      </form>
    </div>
  );
};

export default Login;
