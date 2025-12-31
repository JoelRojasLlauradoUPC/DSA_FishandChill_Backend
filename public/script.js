const loginForm = document.getElementById('login-form');
const loginMessage = document.getElementById('message');
const togglePassword = document.getElementById('togglePassword');
const passwordInput = document.getElementById('password');

togglePassword.addEventListener('click', () => {
    const type = passwordInput.type === 'password' ? 'text' : 'password';
    passwordInput.type = type;

    const icon = togglePassword.querySelector('i');
    icon.classList.toggle('bi-eye');
    icon.classList.toggle('bi-eye-slash');
});

loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = loginForm.username.value;
    const password = loginForm.password.value;

    loginMessage.textContent = '';
    loginMessage.className = 'fc-message mt-3 small';

    try {
        const res = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        if (!res.ok) {
            const text = await res.text();
            loginMessage.textContent = 'Login error: ' + text;
            loginMessage.classList.add('alert', 'alert-danger');
            return;
        }

        const data = await res.json();
        const token = data.token;

        if (!token) {
            loginMessage.textContent = 'Error: missing token in response';
            loginMessage.classList.add('alert', 'alert-danger');
            return;
        }

        localStorage.setItem('authToken', token);

        loginMessage.textContent = 'Login successful, redirecting...';
        loginMessage.classList.add('alert', 'alert-success');

        setTimeout(() => { window.location.href = 'shop'; }, 400);
    } catch (err) {
        console.error(err);
        loginMessage.textContent = 'Server connection error';
        loginMessage.classList.add('alert', 'alert-danger');
    }
});
