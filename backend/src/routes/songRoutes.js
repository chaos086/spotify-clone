const express = require('express');
const router = express.Router();
const Song = require('../models/Song');
const { protect, authorize } = require('../middleware/auth');

router.get('/', async (req, res) => {
    const songs = await Song.find().limit(100).populate('artist album');
    res.json(songs);
});

router.get('/top', async (req, res) => {
    const songs = await Song.find().sort({ playCount: -1, rating: -1 }).limit(50);
    res.json(songs);
});

router.get('/:id', async (req, res) => {
    const song = await Song.findById(req.params.id).populate('artist album');
    if (!song) return res.status(404).json({ message: 'Canción no encontrada' });
    res.json(song);
});

router.post('/', protect, authorize('admin'), async (req, res) => {
    const song = await Song.create(req.body);
    res.status(201).json(song);
});

router.put('/:id', protect, authorize('admin'), async (req, res) => {
    const updated = await Song.findByIdAndUpdate(req.params.id, req.body, { new: true });
    res.json(updated);
});

router.delete('/:id', protect, authorize('admin'), async (req, res) => {
    await Song.findByIdAndDelete(req.params.id);
    res.json({ message: 'Canción eliminada' });
});

module.exports = router;
