document.getElementById('backBtn').addEventListener('click', () => {
    window.location.href = '../shop';
});

document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.removeItem('authToken');
    window.location.href = '../index.html';
});

const coinsValue = document.getElementById('coinsValue');
const emptyMsg = document.getElementById('emptyMsg');
const grid = document.getElementById('grid');

let groups = new Map();
let indices = new Map();

function requireAuth() {
    const token = localStorage.getItem('authToken');
    if (!token) {
        window.location.href = '../index.html';
        return null;
    }
    return token;
}

function getSpeciesName(s) {
    if (typeof s === 'string') return s;
    if (s && typeof s === 'object') return s.name ?? s.speciesName ?? s.fishSpeciesName ?? s.fishSpecies ?? null;
    return null;
}

function getRarityFromFish(f) {
    const r =
        f?.rarity ??
        f?.fishRarity ??
        f?.fishSpecies?.rarity ??
        f?.fishSpecies?.fishRarity ??
        f?.fishSpecies?.rarityLevel ??
        f?.fishSpecies?.rarityId ??
        1;

    const n = Number(r);
    if (n === 2 || n === 3) return n;
    return 1;
}

function rarityLabel(r) {
    if (r === 2) return 'Rare';
    if (r === 3) return 'Legendary';
    return 'Common';
}

function formatCaptureTime(isoString) {
    if (!isoString) return '-';
    const d = new Date(isoString);

    const hh = String(d.getHours()).padStart(2, '0');
    const mm = String(d.getMinutes()).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    const MM = String(d.getMonth() + 1).padStart(2, '0');
    const yyyy = d.getFullYear();

    return `${hh}:${mm} - ${dd}/${MM}/${yyyy}`;
}

async function loadCoins() {
    const token = requireAuth();
    if (!token) return;

    const res = await fetch('/api/me', { method: 'GET', headers: { 'Authorization': token } });
    if (!res.ok) {
        localStorage.removeItem('authToken');
        window.location.href = '../index.html';
        return;
    }

    const user = await res.json();
    coinsValue.textContent = String(user.coins ?? 0);
}

async function loadCapturedFishes() {
    const token = requireAuth();
    if (!token) return;

    const res = await fetch('/api/me/captured_fishes', { method: 'GET', headers: { 'Authorization': token } });
    if (!res.ok) return;

    const list = await res.json();
    buildGroups(Array.isArray(list) ? list : []);
    renderGrid();
}

function buildGroups(list) {
    groups = new Map();
    indices = new Map();

    for (const f of list) {
        const species = getSpeciesName(f.fishSpecies) ?? getSpeciesName(f.fishSpeciesName) ?? '';
        if (!species) continue;

        if (!groups.has(species)) groups.set(species, []);
        groups.get(species).push(f);
    }

    for (const [species] of groups) {
        indices.set(species, 0);
    }
}

function setEmptyState() {
    grid.innerHTML = '';
    emptyMsg.style.display = 'block';
}

function setNonEmptyState() {
    emptyMsg.style.display = 'none';
}

function suggestedPrice(weight) {
    return Math.round(Number(weight || 0) * 20);
}

function renderGrid() {
    if (!groups || groups.size === 0) {
        setEmptyState();
        return;
    }

    setNonEmptyState();
    grid.innerHTML = '';

    const speciesNames = Array.from(groups.keys()).sort((a, b) => {
        const ra = getRarityFromFish((groups.get(a) || [])[0]);
        const rb = getRarityFromFish((groups.get(b) || [])[0]);
        if (ra !== rb) return ra - rb;
        return a.localeCompare(b);
    });

    for (const species of speciesNames) {
        const stack = groups.get(species);
        if (!stack || stack.length === 0) continue;

        const i = Math.max(0, Math.min(indices.get(species) ?? 0, stack.length - 1));
        indices.set(species, i);

        const f = stack[i];
        const rarity = getRarityFromFish(f);

        const card = document.createElement('div');
        card.className = 'card';

        const top = document.createElement('div');
        top.className = 'card-top';

        const imgBox = document.createElement('div');
        imgBox.className = 'img-box';

        const overlay = document.createElement('div');
        overlay.className = `img-overlay overlay-${rarity}`;

        const img = document.createElement('img');
        img.alt = species;
        img.src = `../img/fishes/${encodeURIComponent(species)}.png`;
        img.onerror = () => { img.removeAttribute('src'); };

        imgBox.appendChild(overlay);
        imgBox.appendChild(img);

        const data = document.createElement('div');
        data.className = 'data';

        const topRow = document.createElement('div');
        topRow.className = 'top-row';

        const fishIdxLine = document.createElement('p');
        fishIdxLine.className = 'line';
        fishIdxLine.style.margin = '0';
        fishIdxLine.innerHTML = `Fish <span class="muted">${i + 1}/${stack.length}</span>`;

        const badge = document.createElement('span');
        badge.className = 'rarity-badge';
        badge.innerHTML = `<span class="dot dot-${rarity}"></span><span class="rt-${rarity}">${rarityLabel(rarity)}</span>`;

        topRow.appendChild(fishIdxLine);
        topRow.appendChild(badge);

        const l1 = document.createElement('p');
        l1.className = 'line';
        l1.innerHTML = `Species: <span class="muted">${species}</span>`;

        const l2 = document.createElement('p');
        l2.className = 'line';
        l2.innerHTML = `Weight: <span class="muted">${(f.weight ?? '-')}</span>`;

        const l3 = document.createElement('p');
        l3.className = 'line';
        l3.innerHTML = `Capture time: <span class="muted">${formatCaptureTime(f.captureTime)}</span>`;

        data.appendChild(topRow);
        data.appendChild(l1);
        data.appendChild(l2);
        data.appendChild(l3);

        top.appendChild(imgBox);
        top.appendChild(data);

        const priceRow = document.createElement('div');
        priceRow.className = 'price-row';

        const priceLabel = document.createElement('label');
        priceLabel.innerHTML = `<span class="coin-icon">🪙</span>Sell price (coins)`;

        const priceRight = document.createElement('div');
        priceRight.className = 'price-right';

        const coin2 = document.createElement('span');
        coin2.className = 'coin-icon';
        coin2.textContent = '🪙';

        const priceInput = document.createElement('input');
        priceInput.type = 'number';
        priceInput.min = '0';
        priceInput.step = '1';
        priceInput.value = String(suggestedPrice(f.weight));

        priceRight.appendChild(coin2);
        priceRight.appendChild(priceInput);

        priceRow.appendChild(priceLabel);
        priceRow.appendChild(priceRight);

        const controls = document.createElement('div');
        controls.className = 'controls';

        const prev = document.createElement('button');
        prev.className = 'arrow-btn';
        prev.type = 'button';
        prev.textContent = '◀';
        prev.disabled = (i === 0);

        const sell = document.createElement('button');
        sell.className = 'sell-btn';
        sell.type = 'button';
        sell.textContent = 'SELL';

        const next = document.createElement('button');
        next.className = 'arrow-btn';
        next.type = 'button';
        next.textContent = '▶';
        next.disabled = (i === stack.length - 1);

        prev.addEventListener('click', () => {
            const cur = indices.get(species) ?? 0;
            if (cur > 0) indices.set(species, cur - 1);
            renderGrid();
        });

        next.addEventListener('click', () => {
            const cur = indices.get(species) ?? 0;
            if (cur < stack.length - 1) indices.set(species, cur + 1);
            renderGrid();
        });

        sell.addEventListener('click', async () => {
            const token = requireAuth();
            if (!token) return;

            const curIdx = indices.get(species) ?? 0;
            const arr = groups.get(species) || [];
            if (arr.length === 0) return;

            const fish = arr[Math.max(0, Math.min(curIdx, arr.length - 1))];
            const price = Number(priceInput.value || 0);

            sell.disabled = true;

            const res = await fetch('/api/shop/captured_fishes/sell', {
                method: 'POST',
                headers: {
                    'Authorization': token,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    fishSpeciesName: species,
                    captureTime: fish.captureTime,
                    price: price
                })
            });

            sell.disabled = false;

            if (!res.ok) return;

            arr.splice(curIdx, 1);

            if (arr.length === 0) {
                groups.delete(species);
                indices.delete(species);
            } else {
                const newIdx = Math.min(curIdx, arr.length - 1);
                indices.set(species, newIdx);
            }

            renderGrid();
            loadCoins();
        });

        controls.appendChild(prev);
        controls.appendChild(sell);
        controls.appendChild(next);

        card.appendChild(top);
        card.appendChild(priceRow);
        card.appendChild(controls);

        grid.appendChild(card);
    }

    if (grid.children.length === 0) setEmptyState();
}

document.addEventListener('DOMContentLoaded', async () => {
    await loadCoins();
    await loadCapturedFishes();
});
