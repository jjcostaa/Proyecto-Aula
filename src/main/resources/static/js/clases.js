document.addEventListener("DOMContentLoaded", function() {
    const listaClases = document.getElementById('lista-clases');
    const formCrear = document.getElementById('form-crear-clase');

    // Función para obtener y mostrar clases
    function cargarClases() {
        fetch('/entrenador/api/clases', { credentials: 'include' }) // Enviamos cookies
            .then(res => {
                if (!res.ok) throw new Error('Error al cargar clases');
                return res.json();
            })
            .then(data => {
                listaClases.innerHTML = '';
                if (data.length === 0) {
                    listaClases.innerHTML = '<p>No hay clases creadas.</p>';
                    return;
                }
                data.forEach(clase => {
                    const fechaFormateada = new Date(clase.fechaHora).toLocaleString();
                    const div = document.createElement('div');
                    div.innerHTML = `
                        <h3>${clase.nombre}</h3>
                        <p>${fechaFormateada}</p>
                        <button onclick="eliminarClase('${clase.id}')">Eliminar</button>
                        <button onclick="mostrarEditarClase('${clase.id}', '${clase.nombre}', '${clase.fechaHora}')">Editar</button>
                        <hr/>
                    `;
                    listaClases.appendChild(div);
                });
            })
            .catch(e => {
                listaClases.innerHTML = `<p class="text-danger">${e.message}</p>`;
            });
    }

    // Crear clase
    formCrear.addEventListener("submit", function(e) {
        e.preventDefault();
        const nombre = document.getElementById('nombre').value.trim();
        const fechaHoraRaw = document.getElementById('fechaHora').value;
        if (!nombre || !fechaHoraRaw) {
            alert('Por favor, llena todos los campos');
            return;
        }
        const fechaHora = new Date(fechaHoraRaw).toISOString();

        fetch('/entrenador/api/clases', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({ nombre: nombre, fechaHora: fechaHora })
        }).then(res => {
            if (!res.ok) throw new Error('Error al crear clase');
            return res.json();
        })
        .then(() => {
            cargarClases();
            formCrear.reset();
        })
        .catch(e => alert(e.message));
    });

    // Eliminar clase
    window.eliminarClase = function(id) {
        if (!confirm('¿Deseas eliminar esta clase?')) return;
        fetch(`/entrenador/api/clases/${id}`, { 
            method: 'DELETE',
            credentials: 'include'
        })
        .then(res => {
            if (!res.ok) throw new Error('Error al eliminar clase');
            cargarClases();
        })
        .catch(e => alert(e.message));
    }

    // Mostrar formulario editar (prompt simple)
    window.mostrarEditarClase = function(id, nombre, fechaHora) {
        const nuevoNombre = prompt("Nuevo nombre", nombre);
        if (nuevoNombre === null || nuevoNombre.trim() === "") return;

        const nuevaFecha = prompt("Nueva fecha (YYYY-MM-DDTHH:mm:ss)", fechaHora);
        if (nuevaFecha === null || nuevaFecha.trim() === "") return;

        fetch(`/entrenador/api/clases/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({ nombre: nuevoNombre.trim(), fechaHora: nuevaFecha.trim() })
        })
        .then(res => {
            if (!res.ok) throw new Error('Error al editar clase');
            cargarClases();
        })
        .catch(e => alert(e.message));
    }

    // Carga inicial
    cargarClases();
});
