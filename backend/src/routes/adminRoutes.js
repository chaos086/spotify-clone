const express = require('express');
const router = express.Router();
const { protect, authorize } = require('../middleware/auth');
const User = require('../models/User');
const Song = require('../models/Song');
const Playlist = require('../models/Playlist');

router.use(protect, authorize('admin'));

router.get('/stats', async (req, res) => {
    const users = await User.countDocuments();
    const songs = await Song.countDocuments();
    const playlists = await Playlist.countDocuments();
    res.json({ users, songs, playlists });
});

module.exports = router;
