const express = require('express');
const router = express.Router();
const Playlist = require('../models/Playlist');
const { protect } = require('../middleware/auth');

router.use(protect);

router.get('/', async (req, res) => {
    const playlists = await Playlist.find({ owner: req.user.id }).populate('songs.song');
    res.json(playlists);
});

router.post('/', async (req, res) => {
    const playlist = await Playlist.create({ ...req.body, owner: req.user.id });
    res.status(201).json(playlist);
});

router.post('/:id/songs', async (req, res) => {
    const { songId } = req.body;
    const playlist = await Playlist.findById(req.params.id);
    await playlist.addSong(songId, req.user.id);
    res.json(playlist);
});

router.delete('/:id/songs/:songId', async (req, res) => {
    const playlist = await Playlist.findById(req.params.id);
    await playlist.removeSong(req.params.songId);
    res.json(playlist);
});

module.exports = router;
