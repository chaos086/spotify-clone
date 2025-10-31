import React, { useState } from 'react';
import './PlayerBar.css';

const demoSong = {
  title: 'Canción Demo',
  artist: 'Artista Demo',
  duration: 213,
  url: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3'
};

const format = (s) => `${Math.floor(s/60)}:${String(s%60).padStart(2,'0')}`;

const PlayerBar = () => {
  const [playing, setPlaying] = useState(false);

  return (
    <div className="player-bar">
      <div className="track-info">
        <strong>{demoSong.title}</strong>
        <span>{demoSong.artist}</span>
      </div>
      <audio id="audio" src={demoSong.url} />
      <div className="controls">
        <button onClick={() => { const a = document.getElementById('audio'); a.currentTime = 0; a.play(); setPlaying(true); }}>⏮</button>
        <button onClick={() => { const a = document.getElementById('audio'); if (playing) { a.pause(); } else { a.play(); } setPlaying(!playing); }}>{playing ? '⏸' : '▶️'}</button>
        <button onClick={() => { const a = document.getElementById('audio'); a.currentTime = Math.max(0, a.currentTime - 10); }}>⏪10s</button>
        <button onClick={() => { const a = document.getElementById('audio'); a.currentTime = Math.min(a.duration, a.currentTime + 10); }}>10s⏩</button>
      </div>
      <div className="time">{format(213)}</div>
    </div>
  );
};

export default PlayerBar;
