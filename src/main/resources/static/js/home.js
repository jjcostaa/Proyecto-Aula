/**
 * FitZone - Efectos y Animaciones para Home
 * Desarrollado para mejorar la experiencia de usuario en el sitio web de FitZone
 */

document.addEventListener('DOMContentLoaded', function() {
    // Inicializar efectos
    initScrollAnimations();
    initParallaxEffects();
    initCounters();
    initImageHoverEffects();
    initSmoothScroll();
    initTypingEffect();
    initFeatureBoxes();
    initTestimonialEnhancement();
    initTeamHoverEffects();
    initBmiCalculator();
});

/**
 * Animaciones al hacer scroll
 */
function initScrollAnimations() {
    const elementsToAnimate = document.querySelectorAll('.gym-class-box, .container.feature .row > div, .team .card, .blog-item');
    
    // Función para verificar si un elemento está en el viewport
    function isInViewport(element) {
        const rect = element.getBoundingClientRect();
        return (
            rect.top <= (window.innerHeight || document.documentElement.clientHeight) * 0.85 &&
            rect.bottom >= 0
        );
    }
    
    // Función para animar elementos visibles
    function animateVisibleElements() {
        elementsToAnimate.forEach(element => {
            if (isInViewport(element) && !element.classList.contains('animated')) {
                element.classList.add('animated', 'fadeInUp');
                
                // Añadimos un pequeño retraso escalonado para los elementos en filas
                if (element.parentElement.classList.contains('row')) {
                    const index = Array.from(element.parentElement.children).indexOf(element);
                    element.style.animationDelay = `${index * 0.1}s`;
                }
            }
        });
    }
    
    // Aplicar animación inicial
    setTimeout(animateVisibleElements, 300);
    
    // Escuchar el evento scroll
    window.addEventListener('scroll', animateVisibleElements);
    
    // Añadir estilos CSS para las animaciones
    const style = document.createElement('style');
    style.textContent = `
        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translate3d(0, 40px, 0);
            }
            to {
                opacity: 1;
                transform: translate3d(0, 0, 0);
            }
        }
        
        .animated {
            animation-duration: 0.8s;
            animation-fill-mode: both;
        }
        
        .fadeInUp {
            animation-name: fadeInUp;
        }
    `;
    document.head.appendChild(style);
}

/**
 * Efectos Parallax
 */
function initParallaxEffects() {
    // Efecto parallax para carousel y secciones con imágenes de fondo
    const parallaxElements = document.querySelectorAll('.carousel-item, .bmi, .testimonial');
    
    window.addEventListener('scroll', function() {
        const scrollTop = window.pageYOffset;
        
        parallaxElements.forEach(element => {
            // Verificar si el elemento tiene una imagen de fondo
            const elementTop = element.offsetTop;
            const elementHeight = element.offsetHeight;
            
            // Si el elemento está en el viewport
            if (scrollTop < elementTop + elementHeight && scrollTop + window.innerHeight > elementTop) {
                const speed = 0.3;
                const yPos = -(scrollTop - elementTop) * speed;
                
                // Aplicar efecto solo a elementos que tienen una imagen de fondo
                if (window.getComputedStyle(element).backgroundImage !== 'none') {
                    element.style.backgroundPosition = `center ${yPos}px`;
                } else if (element.querySelector('img')) {
                    // Para elementos con imágenes (como el carousel)
                    const img = element.querySelector('img');
                    img.style.transform = `translateY(${yPos * 0.5}px)`;
                }
            }
        });
    });
}

/**
 * Contadores animados
 */
function initCounters() {
    // Añadir contadores a la sección "10 años de experiencia"
    const aboutSection = document.querySelector('.container.py-5');
    if (aboutSection) {
        // Crear contenedor para los contadores
        const countersContainer = document.createElement('div');
        countersContainer.className = 'row counter-row text-center py-4';
        
        // Definir datos para los contadores
        const counterData = [
            { icon: 'fas fa-users', count: 1250, title: 'Miembros Satisfechos' },
            { icon: 'fas fa-trophy', count: 15, title: 'Premios Ganados' },
            { icon: 'fas fa-dumbbell', count: 30, title: 'Tipos de Clases' },
            { icon: 'fas fa-user-tie', count: 12, title: 'Entrenadores Expertos' }
        ];
        
        // Crear elementos HTML para cada contador
        counterData.forEach(data => {
            const counterDiv = document.createElement('div');
            counterDiv.className = 'col-lg-3 col-md-6 counter-item';
            counterDiv.innerHTML = `
                <div class="py-3">
                    <i class="${data.icon} text-primary mb-2" style="font-size: 2.5rem;"></i>
                    <h2 class="counter-number" data-target="${data.count}">0</h2>
                    <h6 class="text-uppercase">${data.title}</h6>
                </div>
            `;
            countersContainer.appendChild(counterDiv);
        });
        
        // Insertar después del primer div hijo de aboutSection
        const firstDiv = aboutSection.querySelector('.row');
        if (firstDiv) {
            firstDiv.parentNode.insertBefore(countersContainer, firstDiv.nextSibling);
        } else {
            aboutSection.appendChild(countersContainer);
        }
        
        // Función para animar contadores
        function animateCounters() {
            const counterElements = document.querySelectorAll('.counter-number');
            
            counterElements.forEach(counter => {
                if (!counter.classList.contains('counted')) {
                    const target = +counter.getAttribute('data-target');
                    const count = +counter.innerText;
                    const increment = target / 100;
                    
                    if (count < target) {
                        counter.innerText = Math.ceil(count + increment);
                        setTimeout(() => animateCounters(), 20);
                    } else {
                        counter.innerText = target;
                        counter.classList.add('counted');
                    }
                }
            });
        }
        
        // Iniciar animación cuando estén en viewport
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    animateCounters();
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.5 });
        
        if (countersContainer) {
            observer.observe(countersContainer);
        }
    }
}

/**
 * Efectos hover para imágenes
 */
function initImageHoverEffects() {
    // Añadir efectos a las imágenes del gimnasio
    const imageElements = document.querySelectorAll('.blog-item img, .team .card-img-top, .feature img');
    
    imageElements.forEach(img => {
        // Añadir clase para efecto
        img.classList.add('zoom-effect');
        
        // Crear contenedor para la imagen si no está en uno
        if (!img.parentElement.classList.contains('img-container')) {
            const parent = img.parentElement;
            const wrapper = document.createElement('div');
            wrapper.className = 'img-container overflow-hidden';
            parent.insertBefore(wrapper, img);
            wrapper.appendChild(img);
        }
    });
    
    // Añadir estilos CSS
    const style = document.createElement('style');
    style.textContent = `
        .img-container {
            overflow: hidden;
            border-radius: 5px;
        }
        
        .zoom-effect {
            transition: transform 0.7s ease;
        }
        
        .zoom-effect:hover {
            transform: scale(1.1);
        }
    `;
    document.head.appendChild(style);
}

/**
 * Scroll suave para anclajes
 */
function initSmoothScroll() {
    // Obtener todos los enlaces dentro de la navegación
    const navLinks = document.querySelectorAll('.navbar-nav a, .btn-outline-light, .btn-lg');
    
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            // Solo si el enlace apunta a un ancla en la misma página
            const href = this.getAttribute('href');
            if (href && href.startsWith('#') && href.length > 1) {
                e.preventDefault();
                
                const targetId = href.substring(1);
                const targetElement = document.getElementById(targetId);
                
                if (targetElement) {
                    // Scroll suave al elemento
                    window.scrollTo({
                        top: targetElement.offsetTop - 70,
                        behavior: 'smooth'
                    });
                    
                    // Si estamos en móvil, cerrar el menú de navegación
                    const navbarCollapse = document.querySelector('.navbar-collapse');
                    if (navbarCollapse && navbarCollapse.classList.contains('show')) {
                        navbarCollapse.classList.remove('show');
                    }
                }
            }
        });
    });
}

/**
 * Efecto de escritura para títulos
 */
function initTypingEffect() {
    // Aplicar efecto de escritura al título principal del carousel
    const mainTitle = document.querySelector('.carousel-item.active h2');
    if (mainTitle) {
        const originalText = mainTitle.textContent;
        mainTitle.textContent = '';
        
        // Esconder texto original
        mainTitle.style.visibility = 'hidden';
        
        // Crear elemento para texto con efecto
        const typedText = document.createElement('div');
        typedText.className = 'typed-text';
        typedText.style.whiteSpace = 'nowrap';
        mainTitle.parentNode.insertBefore(typedText, mainTitle.nextSibling);
        
        // Función para animar la escritura
        let charIndex = 0;
        
        function typeText() {
            if (charIndex < originalText.length) {
                typedText.textContent += originalText.charAt(charIndex);
                charIndex++;
                setTimeout(typeText, 70); // Velocidad de escritura
            } else {
                // Añadir cursor parpadeante al final
                typedText.innerHTML = typedText.textContent + '<span class="cursor">|</span>';
                
                // Parpadeo del cursor
                setInterval(() => {
                    const cursor = document.querySelector('.cursor');
                    if (cursor) {
                        cursor.style.opacity = cursor.style.opacity === '0' ? '1' : '0';
                    }
                }, 500);
            }
        }
        
        // Iniciar la animación después de un retraso
        setTimeout(typeText, 500);
        
        // Estilos para el cursor
        const style = document.createElement('style');
        style.textContent = `
            .typed-text {
                font-size: inherit;
                font-weight: inherit;
                color: inherit;
                text-transform: inherit;
                line-height: inherit;
                display: inline-block;
            }
            
            .cursor {
                display: inline-block;
                width: 3px;
                background-color: #fff;
                animation: cursor-blink 1s infinite;
                transition: opacity 0.3s;
            }
            
            @keyframes cursor-blink {
                0%, 100% { opacity: 1; }
                50% { opacity: 0; }
            }
        `;
        document.head.appendChild(style);
    }
}

/**
 * Mejoras para las cajas de características
 */
function initFeatureBoxes() {
    // Añadir efecto hover a las cajas de características
    const featureBoxes = document.querySelectorAll('.gym-class-box, .d-flex.align-items-center.bg-secondary, .d-flex.align-items-center.bg-primary');
    
    featureBoxes.forEach(box => {
        box.classList.add('feature-box-enhanced');
        
        // Añadir efecto de ondas al hacer click
        box.addEventListener('click', function(e) {
            // Crear elemento de onda
            const ripple = document.createElement('span');
            ripple.classList.add('ripple-effect');
            
            // Posicionar la onda en el punto del click
            const rect = this.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);
            
            ripple.style.width = ripple.style.height = `${size}px`;
            ripple.style.left = `${e.clientX - rect.left - size/2}px`;
            ripple.style.top = `${e.clientY - rect.top - size/2}px`;
            
            this.appendChild(ripple);
            
            // Eliminar la onda después de la animación
            setTimeout(() => ripple.remove(), 700);
        });
    });
    
    // Estilos para los efectos
    const style = document.createElement('style');
    style.textContent = `
        .feature-box-enhanced {
            transition: transform 0.3s, box-shadow 0.3s;
            cursor: pointer;
            position: relative;
            overflow: hidden;
        }
        
        .feature-box-enhanced:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
        }
        
        .ripple-effect {
            position: absolute;
            border-radius: 50%;
            background-color: rgba(255, 255, 255, 0.3);
            transform: scale(0);
            animation: ripple 0.6s linear;
            pointer-events: none;
        }
        
        @keyframes ripple {
            to {
                transform: scale(4);
                opacity: 0;
            }
        }
    `;
    document.head.appendChild(style);
}

/**
 * Mejoras para los testimonios
 */
function initTestimonialEnhancement() {
    // Mejorar sección de testimonios
    const testimonials = document.querySelectorAll('.testimonial-text');
    
    testimonials.forEach(testimonial => {
        // Añadir comillas grandes al inicio
        const quoteIcon = document.createElement('div');
        quoteIcon.className = 'quote-icon';
        quoteIcon.innerHTML = '❝';
        testimonial.prepend(quoteIcon);
        
        // Añadir efecto de resaltado a palabras clave
        const text = testimonial.innerText;
        const highlightWords = ['excelente', 'increíble', 'fantástico', 'fitness', 'gimnasio', 'entrenamiento'];
        
        let highlightedText = text;
        highlightWords.forEach(word => {
            const regex = new RegExp(`\\b${word}\\b`, 'gi');
            highlightedText = highlightedText.replace(regex, match => `<span class="highlight">${match}</span>`);
        });
        
        // Reemplazar el texto con la versión resaltada
        const contentElement = document.createElement('div');
        contentElement.className = 'testimonial-content';
        contentElement.innerHTML = highlightedText;
        
        // Conservar solo el texto dentro del testimonial
        testimonial.innerHTML = '';
        testimonial.appendChild(quoteIcon);
        testimonial.appendChild(contentElement);
    });
    
    // Estilos para las mejoras
    const style = document.createElement('style');
    style.textContent = `
        .testimonial-text {
            position: relative;
            padding: 2rem !important;
        }
        
        .quote-icon {
            position: absolute;
            top: -10px;
            left: 20px;
            font-size: 4rem;
            color: #dc3545;
            opacity: 0.2;
            z-index: 0;
            font-family: serif;
        }
        
        .testimonial-content {
            position: relative;
            z-index: 1;
        }
        
        .highlight {
            color: #dc3545;
            font-weight: bold;
        }
    `;
    document.head.appendChild(style);
}

/**
 * Efectos hover para el equipo
 */
function initTeamHoverEffects() {
    // Mejorar tarjetas del equipo
    const teamCards = document.querySelectorAll('.team .card');
    
    teamCards.forEach(card => {
        // Crear capa de información
        const infoLayer = document.createElement('div');
        infoLayer.className = 'team-info-layer';
        
        // Obtener información del entrenador
        const name = card.querySelector('.card-title').textContent;
        const role = card.querySelector('.card-text').textContent;
        
        // Añadir información adicional
        infoLayer.innerHTML = `
            <div class="team-hover-content">
                <h4>${name}</h4>
                <p>${role}</p>
                <p class="specialties">Especialidades: Fitness, Nutrición, Cardio</p>
                <p class="experience">5+ años de experiencia</p>
            </div>
        `;
        
        // Insertar después de la imagen
        const cardImg = card.querySelector('.card-img-top');
        if (cardImg) {
            cardImg.parentNode.insertBefore(infoLayer, cardImg.nextSibling);
        }
        
        // Añadir clase para efecto
        card.classList.add('team-card-enhanced');
    });
    
    // Estilos para los efectos
    const style = document.createElement('style');
    style.textContent = `
        .team-card-enhanced {
            transition: transform 0.3s ease;
            transform-style: preserve-3d;
            perspective: 1000px;
        }
        
        .team-card-enhanced:hover {
            transform: rotateY(10deg) translateY(-10px);
            box-shadow: -10px 10px 20px rgba(0, 0, 0, 0.2);
        }
        
        .team-card-enhanced .card-img-top {
            transition: transform 0.5s ease;
        }
        
        .team-card-enhanced:hover .card-img-top {
            transform: scale(1.05);
        }
        
        .team-info-layer {
            position: absolute;
            top: 0;
            right: -80px;
            width: 80px;
            height: 100%;
            background: rgba(0, 0, 0, 0.8);
            color: white;
            display: flex;
            flex-direction: column;
            justify-content: center;
            padding: 10px;
            transform: translateX(0);
            transition: transform 0.3s ease;
            overflow: hidden;
            opacity: 0;
        }
        
        .team-card-enhanced:hover .team-info-layer {
            transform: translateX(-80px);
            opacity: 1;
        }
        
        .team-hover-content {
            transform: rotate(-90deg);
            white-space: nowrap;
            font-size: 0.8rem;
            text-align: left;
        }
        
        .team-hover-content h4 {
            margin-bottom: 5px;
            font-size: 1rem;
        }
        
        .specialties, .experience {
            margin: 5px 0;
            font-size: 0.7rem;
        }
    `;
    document.head.appendChild(style);
}

/**
 * Calculadora de IMC interactiva
 */
function initBmiCalculator() {
    // Obtener elementos de la calculadora
    const bmiForm = document.querySelector('.bmi form');
    const weightInput = bmiForm ? bmiForm.querySelector('input[placeholder="Weight (KG)"]') : null;
    const heightInput = bmiForm ? bmiForm.querySelector('input[placeholder="Height (CM)"]') : null;
    const calculateButton = bmiForm ? bmiForm.querySelector('input[type="button"]') : null;
    
    if (bmiForm && weightInput && heightInput && calculateButton) {
        // Crear div para mostrar resultado
        const resultDiv = document.createElement('div');
        resultDiv.className = 'bmi-result';
        resultDiv.style.display = 'none';
        
        // Añadir resultado después del formulario
        bmiForm.appendChild(resultDiv);
        
        // Configurar función de cálculo
        calculateButton.addEventListener('click', function() {
            const weight = parseFloat(weightInput.value);
            const height = parseFloat(heightInput.value) / 100; // convertir cm a m
            
            if (isNaN(weight) || isNaN(height) || weight <= 0 || height <= 0) {
                resultDiv.innerHTML = '<p class="text-danger">Por favor, ingresa valores válidos.</p>';
                resultDiv.style.display = 'block';
                return;
            }
            
            // Calcular IMC
            const bmi = weight / (height * height);
            let category, color;
            
            // Determinar categoría
            if (bmi < 18.5) {
                category = 'Bajo peso';
                color = '#3498db';
            } else if (bmi < 25) {
                category = 'Peso normal';
                color = '#2ecc71';
            } else if (bmi < 30) {
                category = 'Sobrepeso';
                color = '#f1c40f';
            } else {
                category = 'Obesidad';
                color = '#e74c3c';
            }
            
            // Crear gráfico visual
            resultDiv.innerHTML = `
                <h4 class="text-white mt-4">Tu IMC: ${bmi.toFixed(1)}</h4>
                <p class="text-white">Categoría: ${category}</p>
                <div class="bmi-chart">
                    <div class="bmi-indicator" style="left: ${Math.min(Math.max(bmi * 3, 10), 90)}%;"></div>
                    <div class="bmi-range" style="left: 0%; width: 18.5%;">Bajo</div>
                    <div class="bmi-range" style="left: 18.5%; width: 25%;">Normal</div>
                    <div class="bmi-range" style="left: 43.5%; width: 30%;">Sobrepeso</div>
                    <div class="bmi-range" style="left: 73.5%; width: 26.5%;">Obesidad</div>
                </div>
                <p class="text-white mt-3">¿Quieres mejorar tu condición física? <a href="#" class="text-primary">Consulta con nuestros entrenadores</a></p>
            `;
            
            // Mostrar resultado con animación
            resultDiv.style.display = 'block';
            resultDiv.classList.add('fadeIn');
            
            // Añadir animación a la indicación del IMC
            setTimeout(() => {
                const indicator = resultDiv.querySelector('.bmi-indicator');
                if (indicator) {
                    indicator.style.transform = 'scale(1.5)';
                    setTimeout(() => {
                        indicator.style.transform = 'scale(1)';
                    }, 300);
                }
            }, 500);
        });
        
        // Estilos para la calculadora de IMC
        const style = document.createElement('style');
        style.textContent = `
            .bmi-result {
                padding: 15px;
                margin-top: 20px;
                animation: fadeIn 0.5s;
            }
            
            .bmi-chart {
                position: relative;
                height: 30px;
                background: linear-gradient(90deg, 
                    #3498db 0%, #3498db 18.5%, 
                    #2ecc71 18.5%, #2ecc71 43.5%, 
                    #f1c40f 43.5%, #f1c40f 73.5%, 
                    #e74c3c 73.5%, #e74c3c 100%);
                border-radius: 15px;
                margin: 15px 0;
            }
            
            .bmi-indicator {
                position: absolute;
                top: -10px;
                width: 20px;
                height: 50px;
                background-color: white;
                border-radius: 10px;
                transform: translateX(-50%);
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
                transition: transform 0.3s, left 1s;
            }
            
            .bmi-range {
                position: absolute;
                top: 35px;
                color: white;
                font-size: 0.7em;
                text-align: center;
            }
            
            @keyframes fadeIn {
                from { opacity: 0; transform: translateY(20px); }
                to { opacity: 1; transform: translateY(0); }
            }
        `;
        document.head.appendChild(style);
    }
}