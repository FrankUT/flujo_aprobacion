const API_BASE_URL = 'http://localhost:8080/api/solicitudes';
const SOLICITANTE_ID = document.getElementById('solicitanteId').value; // Obtenemos el ID del input oculto
const solicitudesContainer = document.getElementById('solicitudes-container');
const filtroEstado = document.getElementById('filtroEstado');

let todasLasSolicitudes = [];

function formatearFecha(fechaStr) {
    if (!fechaStr) {
        return 'N/A';
    }
    try {
        const fecha = new Date(fechaStr);
        return fecha.toLocaleDateString() + ' ' + fecha.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    } catch (e) {
        return fechaStr.substring(0, 10);
    }
}

// ----------------------------------------------------------------------

/**
 * Llamar a la API para obtener TODAS las solicitudes creadas por el solicitante.
 */
async function obtenerTodasLasSolicitudes() {
    try {
        const url = `${API_BASE_URL}/usuario/${SOLICITANTE_ID}`;
        
        const response = await fetch(url);

        if (!response.ok) {
            throw new Error(`Error en la petición: ${response.statusText}`);
        }
        
        // Lista completa de solicitudes
        todasLasSolicitudes = await response.json(); 
        
        cargarSolicitudesConFiltro();

    } catch (error) {
        console.error('Error al cargar solicitudes:', error);
        solicitudesContainer.innerHTML = `<p style="color: red;">Error al conectar con el servidor: ${error.message}</p>`;
    }
}

function obtenerSolicitudesPorUsuario() {
    const userId = getActiveUserId(); 

    fetch(`/api/solicitudes/usuario/${userId}`) 
}

/**
 * Filtrar la lista global de solicitudes y renderizar
 */
function cargarSolicitudesConFiltro() {
    const estadoSeleccionado = filtroEstado.value;
    let solicitudesFiltradas = todasLasSolicitudes;

    if (estadoSeleccionado !== "") {
        solicitudesFiltradas = todasLasSolicitudes.filter(sol => sol.estado === estadoSeleccionado);
    }
    
    renderizarSolicitudes(solicitudesFiltradas);
}

/**
 * Renderizar la lista de solicitudes en el DOM
 * @param {Array} solicitudes
 */
function renderizarSolicitudes(solicitudes) {
    solicitudesContainer.innerHTML = ''; 

    if (solicitudes.length === 0) {
        const estado = filtroEstado.value === "" ? "en cualquier estado" : filtroEstado.value;
        solicitudesContainer.innerHTML = `<p>No has creado solicitudes ${estado}.</p>`;
        return;
    }

    solicitudes.forEach(sol => {
        const solicitudDiv = document.createElement('div');
        solicitudDiv.classList.add('solicitud-item', `estado-${sol.estado}`);
        
        let colorEstado = '';
        if (sol.estado === 'aprobado') colorEstado = 'green';
        else if (sol.estado === 'rechazado') colorEstado = 'red';
        else colorEstado = 'orange';

        // Determina quién tomó la acción
        const aprobadorAccion = sol.ultimoAprobadorAccion 
            ? sol.ultimoAprobadorAccion.fullName || sol.ultimoAprobadorAccion.userId
            : sol.aprobador.fullName || sol.aprobador.userId; // Si no hay acción, se muestra el probador original

        solicitudDiv.innerHTML = `
            <h3>${sol.titulo} (${sol.tipo})</h3>
            <p><strong>ID Solicitud:</strong> ${sol.id}</p>
            <p><strong>Aprobador Asignado:</strong> ${sol.aprobador ? sol.aprobador.fullName || sol.aprobador.userId : 'N/A'}</p>
            <p><strong>Estado:</strong> <strong style="color: ${colorEstado};">${sol.estado.toUpperCase()}</strong></p>
            <p><strong>Enviada el:</strong> ${formatearFecha(sol.fechaCreacion)}</p>
            
            ${sol.updatedAt ? `
                <p><strong>Última Acción el:</strong> ${formatearFecha(sol.updatedAt)}</p>
                <p><strong>Acción tomada por:</strong> ${aprobadorAccion}</p>
            ` : `<p><strong>Estado:</strong> Pendiente de acción</p>`}

            ${sol.comentarioAprobador ? `<p class="comentario"><strong>Comentario del Aprobador:</strong> ${sol.comentarioAprobador}</p>` : ''}
            
            <hr>
        `;
        solicitudesContainer.appendChild(solicitudDiv);
    });
}

obtenerTodasLasSolicitudes();