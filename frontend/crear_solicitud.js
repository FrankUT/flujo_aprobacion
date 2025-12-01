// --- Configuración ---
const API_BASE_URL = 'http://localhost:8080/api/solicitudes';
const form = document.getElementById('solicitud-form');
const tipoSelect = document.getElementById('tipo');
const mensajeEstado = document.getElementById('mensaje-estado');

// ----------------------------------------------------------------------

/**
 * Cargar los tipos de solicitud disponibles
 */
async function cargarTiposSolicitud() {
    try {
        const response = await fetch(`${API_BASE_URL}/config/tipos`);

        if (!response.ok) {
            throw new Error('No se pudo cargar la lista de tipos.');
        }

        const tipos = await response.json();
        
        tipoSelect.innerHTML = '';
        
        // Agregar opción por defecto
        const defaultOption = document.createElement('option');
        defaultOption.value = "";
        defaultOption.textContent = "--- Seleccione un tipo ---";
        tipoSelect.appendChild(defaultOption);

        // Agregar tipos dinámicamente
        tipos.forEach(tipo => {
            const option = document.createElement('option');
            option.value = tipo;
            option.textContent = tipo;
            tipoSelect.appendChild(option);
        });

    } catch (error) {
        console.error('Error al cargar los tipos:', error);
        tipoSelect.innerHTML = '<option value="">Error al cargar tipos</option>';
        mensajeEstado.textContent = 'Error al conectar con la API para obtener tipos.';
        mensajeEstado.style.color = 'red';
    }
}


/**
 * Envio del formulario y realiza la petición POST a solicitudes.
 */
form.addEventListener('submit', async function (event) {
    event.preventDefault();

    mensajeEstado.textContent = 'Enviando solicitud...';
    mensajeEstado.style.color = 'orange';
    
    // Recolectar dato
    const datosSolicitud = {
        titulo: document.getElementById('titulo').value,
        descripcion: document.getElementById('descripcion').value,
        solicitanteId: document.getElementById('solicitanteId').value,
        aprobadorId: document.getElementById('aprobadorId').value,
        tipo: document.getElementById('tipo').value
    };

    try {
        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(datosSolicitud)
        });

        if (response.status === 200 || response.status === 201) {
            const nuevaSolicitud = await response.json();
            mensajeEstado.textContent = `¡Solicitud creada con éxito! ID: ${nuevaSolicitud.id}`;
            mensajeEstado.style.color = 'green';
            form.reset(); // Limpiar el formulario
        } else {
            // e.g., AprobadorId no encontrado
            const errorData = await response.json();
            throw new Error(`Error ${response.status}: ${errorData.message || response.statusText}`);
        }

    } catch (error) {
        console.error('Fallo al crear la solicitud:', error);
        mensajeEstado.textContent = `Fallo: ${error.message}`;
        mensajeEstado.style.color = 'red';
    }
});

// ----------------------------------------------------------------------
//Cargar los tipos de solicitud al cargar la página
cargarTiposSolicitud();