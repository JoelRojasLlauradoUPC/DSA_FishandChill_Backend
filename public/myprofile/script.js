document.getElementById('backToShopBtn').addEventListener('click', () => {
    window.location.href = '../shop';
});

document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.removeItem('authToken');
    window.location.href = '../index.html';
});

async function loadMe() {
    const token = localStorage.getItem('authToken');

    if (!token) {
        window.location.href = '../index.html';
        return;
    }

    const res = await fetch('/api/me', {
        method: 'GET',
        headers: { 'Authorization': token }
    });

    if (!res.ok) {
        localStorage.removeItem('authToken');
        window.location.href = '../index.html';
        return;
    }

    const user = await res.json();

    document.getElementById('profile-username').innerHTML = `Username: <span class="muted">${user.username}</span>`;
    document.getElementById('profile-email').innerHTML = `Email: <span class="muted">${user.email}</span>`;
    document.getElementById('profile-coins').innerHTML = `Coins: <span class="muted">${user.coins}</span>`;
}

const deleteBackdrop = document.getElementById('deleteModalBackdrop');
const openDeleteModalBtn = document.getElementById('openDeleteModalBtn');
const cancelDeleteBtn = document.getElementById('cancelDeleteBtn');
const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');
const deleteConfirmInput = document.getElementById('deleteConfirmInput');
const deleteError = document.getElementById('deleteError');

const goodbyeBackdrop = document.getElementById('goodbyeBackdrop');
const byeBtn = document.getElementById('byeBtn');

function openDeleteModal() {
    deleteError.style.display = 'none';
    deleteError.textContent = '';
    deleteConfirmInput.value = '';
    confirmDeleteBtn.disabled = true;
    deleteBackdrop.style.display = 'flex';
    deleteBackdrop.setAttribute('aria-hidden', 'false');
    setTimeout(() => deleteConfirmInput.focus(), 0);
}

function closeDeleteModal() {
    deleteBackdrop.style.display = 'none';
    deleteBackdrop.setAttribute('aria-hidden', 'true');
}

function openGoodbyeModal() {
    goodbyeBackdrop.style.display = 'flex';
    goodbyeBackdrop.setAttribute('aria-hidden', 'false');
}

openDeleteModalBtn.addEventListener('click', openDeleteModal);
cancelDeleteBtn.addEventListener('click', closeDeleteModal);

deleteBackdrop.addEventListener('click', (e) => {
    if (e.target === deleteBackdrop) closeDeleteModal();
});

deleteConfirmInput.addEventListener('input', () => {
    confirmDeleteBtn.disabled = (deleteConfirmInput.value !== 'DELETE MY ACCOUNT');
    deleteError.style.display = 'none';
    deleteError.textContent = '';
});

async function deleteAccount() {
    const token = localStorage.getItem('authToken');
    if (!token) {
        window.location.href = '../index.html';
        return;
    }

    confirmDeleteBtn.disabled = true;
    deleteError.style.display = 'none';
    deleteError.textContent = '';

    const res = await fetch('/api/me', {
        method: 'DELETE',
        headers: { 'Authorization': token }
    });

    if (!res.ok) {
        const text = await res.text().catch(() => 'Delete failed');
        deleteError.textContent = "❌ " + text;
        deleteError.style.display = 'block';
        confirmDeleteBtn.disabled = false;
        return;
    }

    closeDeleteModal();
    localStorage.removeItem('authToken');
    openGoodbyeModal();
}

confirmDeleteBtn.addEventListener('click', deleteAccount);

byeBtn.addEventListener('click', () => {
    window.location.href = '../register';
});

document.addEventListener('DOMContentLoaded', () => {
    loadMe();
});
