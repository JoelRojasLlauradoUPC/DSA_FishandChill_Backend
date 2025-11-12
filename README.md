# Fishing Game Backend API

This backend exposes a simple game API with authentication, catalog, in-game actions, and a shop.

Key endpoints:
- POST /auth/register, POST /auth/login
- GET /catalog/species, GET /catalog/rods
- GET /me, GET /me/inventory
- POST /game/capture
- POST /shop/rods/{rodId}/buy

Implementation notes:
- No DTOs are used. Domain models are returned. Sensitive fields are hidden (User.getPassword annotated with @XmlTransient).
- GameManager is a wrapper and keeps in-memory state. It can be migrated to repositories later.
- Inventory returns unmodifiable maps; it holds equippedRodId and helpers for common game logic.

Tests:
- Existing tests are deprecated and should be updated to match the new structure if needed.

