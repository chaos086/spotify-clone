const express = require('express');
const router = express.Router();
const { getUsers, getUserById, updateUser, deleteUser } = require('../controllers/userController');
const { protect, authorize } = require('../middleware/auth');

router.use(protect);
router.get('/', authorize('admin'), getUsers);
router.get('/:id', authorize('admin'), getUserById);
router.put('/:id', authorize('admin'), updateUser);
router.delete('/:id', authorize('admin'), deleteUser);

module.exports = router;
