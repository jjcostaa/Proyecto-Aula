/**
 * FitBot - Asistente Virtual FitZone
 * chatbot.js
 */
(function () {
    'use strict';

    /* ── Configuración ── */
    const API_ENDPOINT = '/api/chatbot/message';
    const SUGGESTIONS  = [
        '💪 ¿Qué clases tienen?',
        '⏰ ¿Cuáles son los horarios?',
        '💳 ¿Cuánto cuesta la membresía?',
        '📍 ¿Dónde están ubicados?',
    ];

    const WELCOME_MSG  =
        '¡Hola! Soy **FitBot** 🤖💪, tu asistente oficial de FitZone.\n' +
        '¿En qué puedo ayudarte hoy? Puedo informarte sobre horarios, clases, membresías y más.';

    /* ── Helpers ── */
    function now() {
        return new Date().toLocaleTimeString('es-CO', { hour: '2-digit', minute: '2-digit' });
    }

    function escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    // Soporte mínimo de markdown: **bold** y saltos de línea
    function renderMarkdown(text) {
        return escapeHtml(text)
            .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
            .replace(/\n/g, '<br>');
    }

    /* ── Estado ── */
    let isOpen     = false;
    let isLoading  = false;
    let badgeShown = false;

    /* ── DOM ── */
    const toggle      = document.getElementById('fitbot-toggle');
    const window_     = document.getElementById('fitbot-window');
    const messages    = document.getElementById('fitbot-messages');
    const input       = document.getElementById('fitbot-input');
    const sendBtn     = document.getElementById('fitbot-send');
    const badge       = document.getElementById('fitbot-badge');
    const suggestBox  = document.getElementById('fitbot-suggestions');

    if (!toggle || !window_ || !messages || !input || !sendBtn) {
        console.warn('FitBot: elementos del DOM no encontrados.');
        return;
    }

    /* ── Inicialización ── */
    function init() {
        // Sugerencias rápidas
        SUGGESTIONS.forEach(function(text) {
            const btn = document.createElement('button');
            btn.className   = 'fitbot-suggestion';
            btn.textContent = text;
            btn.addEventListener('click', function() { sendMessage(text); });
            suggestBox.appendChild(btn);
        });

        // Mensaje de bienvenida (con pequeño delay)
        setTimeout(function() {
            appendMessage('bot', WELCOME_MSG);
            if (!badgeShown) {
                badge.style.opacity = '1';
                badge.style.transform = 'scale(1)';
                badgeShown = true;
            }
        }, 800);

        // Eventos
        toggle.addEventListener('click', toggleChat);

        sendBtn.addEventListener('click', function() {
            const msg = input.value.trim();
            if (msg) sendMessage(msg);
        });

        input.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                const msg = input.value.trim();
                if (msg) sendMessage(msg);
            }
        });

        // Auto-resize del input
        input.addEventListener('input', function() {
            input.style.height = 'auto';
            input.style.height = Math.min(input.scrollHeight, 100) + 'px';
        });

        // Cerrar al hacer clic fuera
        document.addEventListener('click', function(e) {
            if (isOpen && !window_.contains(e.target) && e.target !== toggle && !toggle.contains(e.target)) {
                closeChat();
            }
        });
    }

    /* ── Abrir / Cerrar ── */
    function toggleChat() {
        isOpen ? closeChat() : openChat();
    }

    function openChat() {
        isOpen = true;
        window_.classList.add('open');
        toggle.classList.add('active');
        badge.style.display = 'none';
        setTimeout(function() { input.focus(); }, 350);
        scrollToBottom();
    }

    function closeChat() {
        isOpen = false;
        window_.classList.remove('open');
        toggle.classList.remove('active');
    }

    /* ── Mensajes ── */
    function appendMessage(role, text) {
        const wrap   = document.createElement('div');
        wrap.className = 'fitbot-msg ' + role;

        const bubble = document.createElement('div');
        bubble.className = 'fitbot-msg-bubble';
        bubble.innerHTML = renderMarkdown(text);

        const time   = document.createElement('div');
        time.className  = 'fitbot-msg-time';
        time.textContent = now();

        wrap.appendChild(bubble);
        wrap.appendChild(time);
        messages.appendChild(wrap);
        scrollToBottom();
        return wrap;
    }

    function showTyping() {
        const wrap = document.createElement('div');
        wrap.className = 'fitbot-msg bot';
        wrap.id = 'fitbot-typing-indicator';

        const typing = document.createElement('div');
        typing.className = 'fitbot-typing';
        typing.innerHTML = '<span></span><span></span><span></span>';

        wrap.appendChild(typing);
        messages.appendChild(wrap);
        scrollToBottom();
    }

    function removeTyping() {
        const el = document.getElementById('fitbot-typing-indicator');
        if (el) el.remove();
    }

    function scrollToBottom() {
        requestAnimationFrame(function() {
            messages.scrollTop = messages.scrollHeight;
        });
    }

    /* ── Envío al API ── */
    function sendMessage(text) {
        if (isLoading || !text) return;

        // Mostrar mensaje del usuario
        input.value = '';
        input.style.height = 'auto';
        appendMessage('user', text);

        // Ocultar sugerencias tras primer uso
        if (suggestBox && suggestBox.style.display !== 'none') {
            suggestBox.style.display = 'none';
        }

        // Estado de carga
        isLoading = true;
        sendBtn.disabled = true;
        showTyping();

        fetch(API_ENDPOINT, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ message: text }),
        })
        .then(function(res) {
            if (!res.ok) throw new Error('HTTP ' + res.status);
            return res.json();
        })
        .then(function(data) {
            removeTyping();
            appendMessage('bot', data.reply || 'Sin respuesta del servidor.');
        })
        .catch(function(err) {
            removeTyping();
            appendMessage('bot', '⚠️ No pude conectarme al servidor. Intenta de nuevo en un momento.');
            console.error('FitBot error:', err);
        })
        .finally(function() {
            isLoading = false;
            sendBtn.disabled = false;
            input.focus();
        });
    }

    /* ── Arrancar ── */
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

})();