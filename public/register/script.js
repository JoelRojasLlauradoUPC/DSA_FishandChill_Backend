const registerForm = document.getElementById('register-form');
const registerMessage = document.getElementById('message');
const togglePasswordRegister = document.getElementById('togglePasswordRegister');
const passwordInputRegister = document.getElementById('password');
const confirmPasswordInput = document.getElementById('confirmPassword');
const registerBtn = document.getElementById('registerBtn');

const reqLength = document.getElementById('req-length');
const reqLetter = document.getElementById('req-letter');
const reqNumber = document.getElementById('req-number');
const reqSymbol = document.getElementById('req-symbol');
const reqMatch  = document.getElementById('req-match');

function setReq(el, ok){
    el.classList.toggle('ok', ok);
    el.classList.toggle('bad', !ok);
    const icon = el.querySelector('i');
    icon.classList.toggle('bi-check-lg', ok);
    icon.classList.toggle('bi-x-lg', !ok);
}

function validatePasswords(){
    const p1 = passwordInputRegister.value || '';
    const p2 = confirmPasswordInput.value || '';

    const okLen = p1.length >= 6;
    const okLet = /[A-Za-z]/.test(p1);
    const okNum = /[0-9]/.test(p1);
    const okSym = /[^A-Za-z0-9]/.test(p1);
    const okMat = p1.length > 0 && p1 === p2;

    setReq(reqLength, okLen);
    setReq(reqLetter, okLet);
    setReq(reqNumber, okNum);
    setReq(reqSymbol, okSym);
    setReq(reqMatch,  okMat);

    const allOk = okLen && okLet && okNum && okSym && okMat;
    registerBtn.disabled = !allOk;
    return allOk;
}

togglePasswordRegister.addEventListener('click', () => {
    const type = passwordInputRegister.type === 'password' ? 'text' : 'password';
    passwordInputRegister.type = type;

    const icon = togglePasswordRegister.querySelector('i');
    icon.classList.toggle('bi-eye');
    icon.classList.toggle('bi-eye-slash');

    confirmPasswordInput.type = type;
});

passwordInputRegister.addEventListener('input', validatePasswords);
confirmPasswordInput.addEventListener('input', validatePasswords);
window.addEventListener('load', validatePasswords);

registerForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = registerForm.username.value;
    const email = registerForm.email.value;
    const password = registerForm.password.value;
    const confirmPassword = registerForm.confirmPassword.value;

    registerMessage.textContent = '';
    registerMessage.className = 'fc-message mt-3 small';

    if (!validatePasswords()) {
        registerMessage.textContent = 'Please meet all password requirements';
        registerMessage.classList.add('alert', 'alert-danger');
        return;
    }

    if (password !== confirmPassword) {
        registerMessage.textContent = 'Passwords do not match';
        registerMessage.classList.add('alert', 'alert-danger');
        return;
    }

    try {
        const res = await fetch('/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password })
        });

        if (!res.ok) {
            const text = await res.text();
            registerMessage.textContent = 'Registration error: ' + text;
            registerMessage.classList.add('alert', 'alert-danger');
            return;
        }

        const user = await res.json();
        registerMessage.textContent = 'User created successfully with id: ' + user.id;
        registerMessage.classList.add('alert', 'alert-success');

        setTimeout(() => { window.location.href = '../index.html'; }, 700);
    } catch (err) {
        console.error(err);
        registerMessage.textContent = 'Server connection error';
        registerMessage.classList.add('alert', 'alert-danger');
    }
});
