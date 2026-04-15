// Este archivo maneja la conexión con el backend y las operaciones de pago y suscripción
        const token = localStorage.getItem('jwtToken');
        const nombre = localStorage.getItem('nombre');
        const userId = localStorage.getItem('usuarioId'); // Asegúrate que esto esté bien guardado en el login

        if (!token) {
            window.location.href = '/login';
        }

        document.getElementById('bienvenida').textContent = nombre 
            ? `Bienvenido, ${nombre}` 
            : "Bienvenido, usuario no identificado";

        function realizarPago(tipoMembresia, monto) {
            const pago = {
                usuarioId: userId,
                nombre: nombre,
                membresia: tipoMembresia
            };

            fetch('http://localhost:8080/api/pagos', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(pago)
            })
            .then(response => {
                if (!response.ok) throw new Error("Error en el pago");
                return response.json();
            })
            .then(data => {
                alert("✅ Pago registrado correctamente:\n" + JSON.stringify(data, null, 2));
            })
            .catch(error => {
                console.error("❌ Error al registrar el pago:", error);
                alert("Error al registrar el pago");
            });
        }

        function cargarClasesDisponibles() {
    fetch('http://localhost:8080/api/clases/todas')
        .then(res => res.json())
        .then(clases => {
            const lista = document.getElementById("listaClasesEntrenadores");
            lista.innerHTML = "";

            if (clases.length === 0) {
                lista.innerHTML = "<li>No hay clases disponibles en este momento.</li>";
            }

            clases.forEach(clase => {
                const li = document.createElement("li");
                li.innerHTML = `
                    <strong>${clase.nombreClase}</strong> -
                    ${clase.descripcion} <br>
                    <em>Entrenador: ${clase.nombreEntrenador || "No asignado"}</em>
                `;
                lista.appendChild(li);
            });
        })
        .catch(err => {
            console.error("❌ Error al cargar clases disponibles:", err);
            document.getElementById("listaClasesEntrenadores").innerHTML = "<li>Error al cargar clases.</li>";
        });
}

// Llama a la función al cargar la página
document.addEventListener("DOMContentLoaded", cargarClasesDisponibles);

function suscribirse(claseId, nombreClase) {
    const usuarioId = localStorage.getItem("usuarioId");
    const nombre = localStorage.getItem("nombre");
    const correo = localStorage.getItem("userEmail");

    const data = {
        usuarioId: usuarioId,
        claseId: claseId,
        nombreUsuario: nombre,
        correoUsuario: correo,
        nombreClase: nombreClase
    };

    fetch("http://localhost:8080/api/suscripciones", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(data)
    })
    .then(res => {
        if (!res.ok) throw new Error("Ya estás suscrito o hubo un error.");
        return res.json();
    })
    .then(() => {
        alert("✅ Te has suscrito correctamente.");
    })
    .catch(err => alert("❌ " + err.message));
}

cargarClasesDisponibles();


    // Obtén el usuarioId correctamente desde localStorage
const usuarioId = localStorage.getItem('usuarioId');

function cargarEstadoPago() {
    // Verifica si hay usuarioId antes de hacer el fetch
    if (!usuarioId) {
        document.getElementById("estadoPagoContenido").innerHTML = `
            <i class="fas fa-times-circle text-danger" style="font-size: 3rem;"></i>
            <h5 class="mt-3">Usuario no identificado</h5>
            <p>No se pudo obtener el ID del usuario. Inicia sesión nuevamente.</p>
        `;
        return;
    }

    fetch(`http://localhost:8080/api/pagos/estado/${usuarioId}`, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
    .then(response => {
        if (!response.ok) throw new Error("No se encontró información de pago.");
        return response.json();
    })
    .then(pago => {
        // Si tu backend retorna pago.fecha, úsalo; si es pago.fechaPago, ajusta aquí
        const fechaPago = new Date(pago.fecha || pago.fechaPago);
        const proximoPago = new Date(fechaPago);
        proximoPago.setDate(proximoPago.getDate() + 30);

        const contenido = `
            <i class="fas fa-check-circle text-success" style="font-size: 3rem;"></i>
            <h5 class="mt-3">Membresía Activa</h5>
            <p>Plan: <strong>${pago.membresia}</strong></p>
            <p>Último pago: ${fechaPago.toLocaleDateString()}</p>
            <p>Próximo pago: ${proximoPago.toLocaleDateString()}</p>
            <a href="#" class="btn btn-outline-primary mt-2 hover-effect">Ver historial de pagos</a>
        `;

        document.getElementById("estadoPagoContenido").innerHTML = contenido;
    })
    .catch(err => {
        document.getElementById("estadoPagoContenido").innerHTML = `
            <i class="fas fa-times-circle text-danger" style="font-size: 3rem;"></i>
            <h5 class="mt-3">Sin membresía activa</h5>
            <p>No se encontró información de pago.</p>
        `;
    });
}

// Llama la función cuando la página esté lista
document.addEventListener('DOMContentLoaded', cargarEstadoPago);

