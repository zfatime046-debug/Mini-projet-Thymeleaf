// ── Modals ─────────────────────────────────────────────────────
function openModal(id) {
    document.getElementById(id).classList.add('open');
}

function closeModal(id) {
    document.getElementById(id).classList.remove('open');
}

// Fermer en cliquant sur l'overlay
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.modal-overlay').forEach(overlay => {
        overlay.addEventListener('click', e => {
            if (e.target === overlay) overlay.classList.remove('open');
        });
    });

    // Fermer avec Escape
    document.addEventListener('keydown', e => {
        if (e.key === 'Escape') {
            document.querySelectorAll('.modal-overlay.open')
                .forEach(m => m.classList.remove('open'));
        }
    });

    // Initialiser la date du jour dans les inputs date vides
    document.querySelectorAll('input[type="date"]:not([value])').forEach(input => {
        if (!input.value) {
            input.value = new Date().toISOString().slice(0, 10);
        }
    });
});
