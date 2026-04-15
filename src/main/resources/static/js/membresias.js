// Obtener el token y datos del usuario del localStorage
const token = localStorage.getItem('jwtToken');
const nombre = localStorage.getItem('nombre');
const userId = localStorage.getItem('usuarioId');

// Variable global para saber si el usuario tiene membresía activa
let membresiaActiva = false;

// Verificar si el usuario está autenticado
if (!token) {
    window.location.href = '/login'; // Redirige si no hay token
}

let selectedPlan = "";
let selectedPrice = 0;

// Consultar el estado de membresía al cargar la página
document.addEventListener('DOMContentLoaded', function () {
    validarCampos();
    consultarEstadoMembresia();
});

// Función para consultar estado de membresía
function consultarEstadoMembresia() {
    fetch(`http://localhost:8080/api/pagos/estado/${userId}`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(res => {
        if (!res.ok) throw new Error();
        return res.json();
    })
    .then(pago => {
        membresiaActiva = true;
        // Opcional: Puedes mostrar un mensaje en la página si quieres
        // document.getElementById('mensajeMembresiaActiva').textContent = "Ya tienes una membresía activa";
        deshabilitarBotonesPago(true);
    })
    .catch(() => {
        membresiaActiva = false;
        deshabilitarBotonesPago(false);
    });
}

// Deshabilitar todos los botones de pago si ya tiene membresía
function deshabilitarBotonesPago(deshabilitar) {
    document.querySelectorAll('.buy-btn').forEach(btn => {
        btn.disabled = deshabilitar;
        btn.classList.toggle('disabled', deshabilitar);
        if (deshabilitar) {
            btn.textContent = "Ya tienes membresía activa";
        } else {
            // Restaurar el texto original según el plan
            btn.textContent = "Seleccionar Plan";
        }
    });
}

// Abrir modal cuando se selecciona un plan (solo si no tiene membresía activa)
document.querySelectorAll('.buy-btn').forEach(btn => {
    btn.addEventListener('click', function () {
        if (membresiaActiva) return; // No hacer nada si ya tiene membresía

        selectedPlan = this.getAttribute('data-plan');
        selectedPrice = parseFloat(this.getAttribute('data-price'));

        // Actualizar los textos del modal
        document.getElementById('selectedPlan').textContent = selectedPlan;
        document.getElementById('selectedPrice').textContent = selectedPrice;

        // También actualizar los precios en las opciones de frecuencia
        document.getElementById('monthlyPrice').textContent = selectedPrice;
        document.getElementById('annualPrice').textContent = selectedPrice * 10;

        document.getElementById('paymentModal').style.display = 'block';
    });
});

// Cerrar el modal
document.querySelector('.close-modal').addEventListener('click', function () {
    document.getElementById('paymentModal').style.display = 'none';
});

// Lógica de selección de método de pago
document.querySelectorAll('.payment-option').forEach(option => {
    option.addEventListener('click', function () {
        document.querySelectorAll('.payment-option').forEach(opt => opt.classList.remove('active'));
        this.classList.add('active');
    });
});

// Confirmar el pago simulado
document.getElementById('confirmPayment').addEventListener('click', function () {
    if (membresiaActiva) {
        Swal.fire({
            icon: 'warning',
            title: 'Ya tienes membresía activa',
            text: 'No puedes pagar dos veces por una membresía activa.',
            confirmButtonText: 'OK'
        });
        return;
    }

    const frecuencia = document.querySelector('input[name="paymentFrequency"]:checked').value;
    const monto = frecuencia === "annual" ? selectedPrice * 10 : selectedPrice;

    const pago = {
        usuarioId: userId,
        nombre: nombre,
        membresia: selectedPlan,
        monto: monto
    };

    console.log("🔼 Enviando pago:", JSON.stringify(pago));

    fetch('http://localhost:8080/api/pagos', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(pago)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Error al registrar el pago");
            }
            return response.json();
        })
        .then(data => {
            Swal.fire({
                icon: 'success',
                title: '¡Pago realizado!',
                text: `Se ha registrado el pago del plan ${selectedPlan} correctamente.`,
                confirmButtonText: 'OK'
            });
            document.getElementById('paymentModal').style.display = 'none';
            membresiaActiva = true;
            deshabilitarBotonesPago(true);
        })
        .catch(error => {
            console.error("❌ Error en el pago:", error);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'No se pudo completar el pago. Intenta de nuevo.'
            });
        });
});

// Validación de campos
function validarCampos() {
    const cardNumberInput = document.getElementById('cardNumber');
    const expiryDateInput = document.getElementById('expiryDate');
    const cvvInput = document.getElementById('cvv');
    const nameOnCardInput = document.getElementById('nameOnCard');

    // Validar que solo se ingresen números en los campos de números
    cardNumberInput.addEventListener('input', function () {
        this.value = this.value.replace(/[^0-9]/g, '');
    });

    expiryDateInput.addEventListener('input', function () {
        this.value = this.value.replace(/[^0-9/]/g, ''); // Permitir números y "/"
    });

    cvvInput.addEventListener('input', function () {
        this.value = this.value.replace(/[^0-9]/g, '');
    });

    // Validar que solo se ingresen letras en el campo de nombre
    nameOnCardInput.addEventListener('input', function () {
        this.value = this.value.replace(/[^a-zA-Z\s]/g, ''); // Permitir letras y espacios
    });
}