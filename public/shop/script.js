function randomColor() {
    const colors = [
        ["#ff6b6b", "#ffb347"],
        ["#6b8cff", "#4661ff"],
        ["#6bff95", "#20c659"],
        ["#ff6bf3", "#d446ff"],
        ["#ffd36b", "#ffb200"],
        ["#6bfff1", "#2ed8cc"],
    ];
    return colors[Math.floor(Math.random() * colors.length)];
}

function randomFishBehavior(fish, index) {
    const top = 10 + index * 13 + Math.random() * 10;
    fish.style.top = top + "%";

    const duration = 18 + Math.random() * 10;
    fish.style.animationDuration = duration + "s";

    const goLeft = Math.random() < 0.5;

    if (goLeft) {
        fish.style.animationName = "swim-left";
        fish.style.setProperty("--tail-x", "calc(100% - 4px)");
        fish.style.setProperty("--eye-x", "8px");
    } else {
        fish.style.animationName = "swim-right";
        fish.style.setProperty("--tail-x", "-18px");
        fish.style.setProperty("--eye-x", "calc(100% - 16px)");
    }

    const [c1, c2] = randomColor();
    fish.style.background = `linear-gradient(90deg, ${c1}, ${c2})`;
    fish.style.setProperty("--tail-color", c1);
}

document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".fish").forEach((fish, i) => {
        randomFishBehavior(fish, i);
    });
});

document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.removeItem('authToken');
    window.location.href = '../index.html';
});

(function () {
    const info = document.getElementById('user-info');
    const rodsContainer = document.getElementById('rods');
    const token = localStorage.getItem('authToken');

    // Cache of state used to render the shop
    let catalogRods = [];
    let ownedRodNames = new Set();
    let equippedRodName = null;

    async function init() {
        await refreshUserState();
        await refreshRodsState();
        renderRods();
    }

    if (!token) {
        info.textContent = 'No session — Redirecting...';
        setTimeout(() => window.location.href = '../index.html', 1200);
    } else {
        init();
    }

    async function refreshUserState() {
        const res = await fetch('/api/me', {
            headers: {'Authorization': token}
        });

        if (!res.ok) return;

        const user = await res.json();
        const name = user.username;
        const coins = user.coins;

        // MeService dto has `equippedFishingRod` (string rod name)
        equippedRodName = user.equippedFishingRod || null;

        info.textContent = `${name} | 💰 ${coins} coins`;
    }

    async function refreshRodsState() {
        const [catalogRes, ownedRes] = await Promise.all([
            fetch('/api/catalog/fishing_rods', { headers: {'Authorization': token} }),
            fetch('/api/me/owned_fishing_rods', { headers: {'Authorization': token} })
        ]);

        if (catalogRes.ok) {
            catalogRods = await catalogRes.json();
        } else {
            catalogRods = [];
        }

        if (ownedRes.ok) {
            const owned = await ownedRes.json();
            ownedRodNames = new Set((owned || []).map(r => r.name));
        } else {
            ownedRodNames = new Set();
        }
    }

    function showFloatMsg(parentEl, text, ok = true) {
        parentEl.style.position = "relative";

        const msg = document.createElement("div");
        msg.className = "float-msg " + (ok ? "float-success" : "float-error");
        msg.textContent = text;

        parentEl.appendChild(msg);
        setTimeout(() => msg.remove(), 1300);
    }

    function renderRods() {
        rodsContainer.innerHTML = '';

        catalogRods.forEach(rod => {
            const el = document.createElement('div');
            const rarity = Number(rod.rarity);
            const rarityClass = Number.isFinite(rarity) ? `rarity-${rarity}` : '';

            el.className = `rod ${rarityClass}`.trim();

            const imgSrc = rod.url;
            const isOwned = ownedRodNames.has(rod.name);
            const isEquipped = !!equippedRodName && rod.name === equippedRodName;

            let buttonLabel = 'Buy';
            let buttonDisabled = false;
            let buttonClass = 'buy';

            if (isOwned && isEquipped) {
                buttonLabel = 'Equipped';
                buttonDisabled = true;
                buttonClass = 'buy equipped';
            } else if (isOwned) {
                buttonLabel = 'Equip';
                buttonClass = 'buy equip';
            }

            const speed = Number.isFinite(Number(rod.speed)) ? Number(rod.speed).toFixed(1) : rod.speed;
            const power = Number.isFinite(Number(rod.power)) ? Number(rod.power).toFixed(1) : rod.power;

            // Hide coins if already owned
            const priceHtml = isOwned ? '' : `<div class="price">💰 ${rod.price} coins</div>`;

            el.innerHTML = `
              <div class="rod-left">
                <img class="rod-image" src="${imgSrc}" alt="${rod.name}">
                <div class="rod-footer">
                  <button class="${buttonClass}" ${buttonDisabled ? 'disabled' : ''}>${buttonLabel}</button>
                </div>
              </div>

              <div class="rod-right">
                <div class="rod-info">
                  <header>${rod.name}</header>
                  <div class="stats">
                    <div>Power: ${power}</div>
                    <div>Speed: ${speed}</div>
                    <div>Durability: ${rod.durability}</div>
                  </div>
                  ${priceHtml}
                </div>
              </div>
            `;

            const btn = el.querySelector('button');
            btn.addEventListener('click', async () => {
                if (btn.disabled) return;

                // Don’t allow buying already-owned rods. Owned => Equip.
                if (ownedRodNames.has(rod.name)) {
                    await equipFishingRod(rod.name, el);
                    return;
                }

                await buyFishingRod(rod.name, rod.price, el);
            });

            rodsContainer.appendChild(el);
        });
    }

    async function buyFishingRod(name, price, parentEl) {
        const res = await fetch(`/api/shop/fishing_rods/${encodeURIComponent(name)}/buy`, {
            method: 'POST',
            headers: {'Authorization': token}
        });

        if (!res.ok) {
            const text = await res.text();
            showFloatMsg(parentEl, "❌ " + text, false);

            // If backend says already owned, we can refresh state so UI flips to Equip.
            await refreshUserState();
            await refreshRodsState();
            renderRods();
            return;
        }

        showFloatMsg(parentEl, `✅ You bought ${name} for ${price} coins!`, true);

        await refreshUserState();
        await refreshRodsState();
        renderRods();
    }

    async function equipFishingRod(name, parentEl) {
        const res = await fetch(`/api/shop/fishing_rods/${encodeURIComponent(name)}/equip`, {
            method: 'POST',
            headers: {'Authorization': token}
        });

        if (!res.ok) {
            const text = await res.text();
            showFloatMsg(parentEl, "❌ " + (text || 'Unable to equip'), false);
            await refreshUserState();
            await refreshRodsState();
            renderRods();
            return;
        }

        showFloatMsg(parentEl, `✅ Equipped ${name}!`, true);
        await refreshUserState();
        await refreshRodsState();
        renderRods();
    }

})();