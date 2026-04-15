// Variables para el sintetizador de voz
let speechSynthesis = window.speechSynthesis;
let voiceActive = false;

// Elementos del DOM
const toggleButton = document.getElementById('chat-toggle');
const chatWindow = document.getElementById('chat-window');
const chatBody = document.getElementById('chat-body');
const userInput = document.getElementById('userInput');
const voiceToggle = document.getElementById('voice-toggle');
const closeButton = document.getElementById('close-chat');
// URL de tu API del chatbot
const API_URL = 'http://localhost:5000/chatbot';

// Evento para abrir/cerrar chat
toggleButton.onclick = () => {
    const visible = chatWindow.style.display === 'flex';
    chatWindow.style.display = visible ? 'none' : 'flex';
    
    if (!visible) {
        if (chatBody.innerHTML.trim() === '') {
            welcomeBot();
        }
        
    }
};
// Cerrar el chat
closeButton.onclick = () => {
    chatWindow.style.display = 'none';
};

// Activar/desactivar voz
voiceToggle.onclick = () => {
    voiceActive = !voiceActive;
    voiceToggle.querySelector('i').classList.toggle('voice-active');
    
    if (voiceActive) {
        // Mensaje para indicar que la voz está activada
        appendMessage("He activado la lectura de voz para mis respuestas.", 'bot');
        speakText("He activado la lectura de voz para mis respuestas.");
    } else {
        // Detener cualquier voz que esté reproduciéndose
        speechSynthesis.cancel();
        appendMessage("He desactivado la lectura de voz.", 'bot');
    }
};

// Mensaje de bienvenida
function welcomeBot() {
    setTimeout(() => {
        const welcomeMessage = "¡Hola! Soy tu asistente virtual de Fitzone 💪. ¿En qué puedo ayudarte hoy?";
        typeEffect(welcomeMessage);
    }, 1000);
}

// Enviar mensaje
function sendMessage() {
    const message = userInput.value.trim();
    if (!message) return;
    
    appendMessage(message, 'user');
    userInput.value = '';
    
    // Mostrar indicador de "escribiendo..."
    const typing = document.createElement('div');
    typing.className = 'message bot typing';
    typing.textContent = 'Escribiendo';
    chatBody.appendChild(typing);
    chatBody.scrollTop = chatBody.scrollHeight;
    
    // Conexión con tu API real
    fetch(API_URL, {
        method: 'POST',
        headers: { 
            'Content-Type': 'application/json',
            // Aquí puedes agregar headers adicionales si los necesitas
            // 'Authorization': 'Bearer tu-token'
        },
        body: JSON.stringify({ message })
    })
    .then(response => {
        // Verificar si la respuesta fue exitosa
        if (!response.ok) {
            throw new Error('Error en la conexión con el servidor: ' + response.status);
        }
        return response.json();
    })
    .then(data => {
        console.log('Respuesta de la API:', data);  // Ya lo tienes, bien hecho
        
        chatBody.removeChild(typing);
    
        // Verifica si `data.response` existe, si no prueba con otras
        if (data && data.response) {
            typeEffect(data.response);
        } else if (data && data.reply) {
            typeEffect(data.reply);
        } else {
            typeEffect("Lo siento, no pude procesar tu solicitud correctamente.");
        }
    })    
    .catch(error => {
        console.error('Error:', error);
        chatBody.removeChild(typing);
        typeEffect("Lo siento, hubo un problema al conectar con el servidor. Por favor, intenta de nuevo más tarde.");
    });
}

// Añadir mensaje al chat
function appendMessage(text, sender) {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${sender}`;
    messageDiv.textContent = text;
    chatBody.appendChild(messageDiv);
    chatBody.scrollTop = chatBody.scrollHeight;
}

// Efecto de escritura del bot - mejorado con velocidad variable
function typeEffect(text) {
    let i = 0;
    const messageDiv = document.createElement('div');
    messageDiv.className = 'message bot';
    chatBody.appendChild(messageDiv);
    
    // Velocidad de escritura variable basada en longitud del texto
    const baseSpeed = 30;
    const speed = text.length > 100 ? baseSpeed * 0.7 : baseSpeed;
    
    const typingInterval = setInterval(() => {
        if (i < text.length) {
            // Añadir carácter a carácter con efectos naturales
            messageDiv.textContent += text.charAt(i);
            i++;
            
            // Pausas aleatorias en puntuaciones para un efecto más natural
            const currentChar = text.charAt(i);
            if (['.', '!', '?', ',', ':'].includes(currentChar)) {
                const pauseFactor = currentChar === '.' || currentChar === '!' || currentChar === '?' ? 8 : 3;
                const pauseTime = speed * pauseFactor;
                clearInterval(typingInterval);
                setTimeout(() => {
                    const newTypingInterval = setInterval(() => {
                        if (i < text.length) {
                            messageDiv.textContent += text.charAt(i);
                            i++;
                            chatBody.scrollTop = chatBody.scrollHeight;
                        } else {
                            clearInterval(newTypingInterval);
                            if (voiceActive) {
                                speakText(text);
                            }
                        }
                    }, speed);
                }, pauseTime);
            }
            
            chatBody.scrollTop = chatBody.scrollHeight;
        } else {
            clearInterval(typingInterval);
            
            // Cuando termine de escribir, leer el mensaje si la voz está activada
            if (voiceActive) {
                speakText(text);
            }
        }
    }, speed);
}

// Función para leer texto
function speakText(text) {
    // Eliminar emojis y caracteres especiales para la voz
    const cleanText = text.replace(/[^\w\s.,;:¿?¡!]/gi, '');
    
    if (speechSynthesis) {
        speechSynthesis.cancel(); // Detener cualquier voz previa
        
        const utterance = new SpeechSynthesisUtterance(cleanText);
        utterance.lang = 'es-ES';
        utterance.rate = 1.0;
        utterance.pitch = 1.0;
        
        // Cargar voces y buscar una voz en español
        let voices = speechSynthesis.getVoices();
        
        // Si las voces no están cargadas aún, esperar y volver a intentar
        if (voices.length === 0) {
            setTimeout(() => {
                voices = speechSynthesis.getVoices();
                const spanishVoice = voices.find(voice => voice.lang.includes('es'));
                if (spanishVoice) {
                    utterance.voice = spanishVoice;
                }
                speechSynthesis.speak(utterance);
            }, 300);
        } else {
            const spanishVoice = voices.find(voice => voice.lang.includes('es'));
            if (spanishVoice) {
                utterance.voice = spanishVoice;
            }
            speechSynthesis.speak(utterance);
        }
    }
}

// Enviar mensaje con Enter
userInput.addEventListener("keypress", function(e) {
    if (e.key === "Enter") sendMessage();
});

// Manejar errores de red
window.addEventListener('online', function() {
    appendMessage("¡Conexión reestablecida! Puedes seguir consultando.", 'bot');
});

window.addEventListener('offline', function() {
    appendMessage("Parece que no hay conexión a internet. Algunas funciones pueden no estar disponibles.", 'bot');
});

// Cargar voces disponibles
window.addEventListener('load', function() {
    // Cargar las voces disponibles
    speechSynthesis.getVoices();
    
    // Algunas navegadores necesitan este evento para cargar las voces
    speechSynthesis.onvoiceschanged = function() {
        speechSynthesis.getVoices();
    };
});

// Detectar cuando el usuario hace clic fuera del chat para cerrarlo (opcional)
document.addEventListener('click', function(event) {
    const isClickInsideChat = chatWindow.contains(event.target);
    const isClickOnToggle = toggleButton.contains(event.target);
    
    if (!isClickInsideChat && !isClickOnToggle && chatWindow.style.display === 'flex') {
        // chatWindow.style.display = 'none'; // Descomenta esta línea si quieres que el chat se cierre al hacer clic fuera
    }
});