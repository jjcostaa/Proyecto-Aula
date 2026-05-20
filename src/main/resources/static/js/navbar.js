(function () {
    'use strict';

    var navbar = document.querySelector('nav.navbar');
    if (!navbar) return;

    var lastScrollY = window.scrollY;
    var scrollThreshold = 80;   // píxeles antes de considerar "scrolled"
    var hideThreshold   = 120;  // píxeles bajados antes de ocultar

    function handleScroll() {
        var currentY = window.scrollY;
        var diff = currentY - lastScrollY;

        // ── Fondo sólido blur al tener suficiente scroll ──
        if (currentY > scrollThreshold) {
            navbar.classList.add('navbar-scrolled');
        } else {
            navbar.classList.remove('navbar-scrolled');
            navbar.classList.remove('navbar-hidden'); // en el top siempre visible
        }

        // ── Scroll hacia ABAJO → ocultar ──────────────────
        if (diff > 4 && currentY > hideThreshold) {
            navbar.classList.add('navbar-hidden');
        }

        // ── Scroll hacia ARRIBA → mostrar ─────────────────
        if (diff < -4) {
            navbar.classList.remove('navbar-hidden');
        }

        lastScrollY = currentY;
    }

    window.addEventListener('scroll', handleScroll, { passive: true });

    // Animación de entrada al cargar
    navbar.style.opacity = '0';
    navbar.style.transform = 'translateY(-14px)';
    requestAnimationFrame(function () {
        setTimeout(function () {
            navbar.style.opacity = '';
            navbar.style.transform = '';
        }, 80);
    });

})();
