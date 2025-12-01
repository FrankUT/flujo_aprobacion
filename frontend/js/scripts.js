const API_BASE_URL = 'http://localhost:8080/api/solicitudes';
const solicitudesContainer = document.getElementById('solicitudes-container');
const filtroEstado = document.getElementById('filtroEstado');

let todasLasSolicitudes = [];

// ----------------------------------------------------------------------

/**
 * Llamar a la API para obtener TODAS las solicitudes del aprobador.
 */
async function obtenerTodasLasSolicitudes() {

    try {
        // Usa función global para obtener el ID activo
        const userId = getActiveUserId(); 
        console.log("Cargando solicitudes para el usuario activo:", userId);

        // Usamos el userId activo en la URL
        const url = `${API_BASE_URL}/usuario/${userId}`; 
        
        const response = await fetch(url);
        
        if (!response.ok) {
            throw new Error(`Error en la petición: ${response.statusText}`);
        }

        todasLasSolicitudes = await response.json(); 
        cargarSolicitudesConFiltro(userId);

    } catch (error) {
        console.error('Error al cargar solicitudes:', error);
        solicitudesContainer.innerHTML = `<p style="color: red;">Error al conectar con el servidor: ${error.message}</p>`;
    }
}

/**
 * Filtrar la lista global de solicitudes y las renderiza.
 * Se llama cuando la página carga y cuando el filtro cambia.
 */
function cargarSolicitudesConFiltro(userId) {
    const estadoSeleccionado = filtroEstado.value;
    let solicitudesFiltradas = todasLasSolicitudes;

    if (estadoSeleccionado !== "") {
        solicitudesFiltradas = todasLasSolicitudes.filter(sol => sol.estado === estadoSeleccionado);
    }
    
    renderizarSolicitudes(solicitudesFiltradas, userId);
}

function cargarSolicitudesPendientes() {
    const aprobadorId = getActiveUserId();
    
    fetch(`/api/solicitudes/aprobador/${aprobadorId}`) 
}

/**
 * Renderizar la lista de solicitudes en el DOM.
 * @param {Array} solicitudes
 * @param {string} userIdActivo
 */
function renderizarSolicitudes(solicitudes, userIdActivo) {
    solicitudesContainer.innerHTML = ''; 

    if (solicitudes.length === 0) {
        const estado = filtroEstado.value === "" ? "en cualquier estado" : filtroEstado.value;
        solicitudesContainer.innerHTML = `<p>No hay solicitudes ${estado} para este usuario.</p>`;
        return;
    }

    solicitudes.forEach(sol => {
        const solicitudDiv = document.createElement('div');
        solicitudDiv.classList.add('solicitud-item', `estado-${sol.estado}`);
        
        const esAprobadorActivo = sol.aprobador && sol.aprobador.userId === userIdActivo;
        const puedeAccionar = sol.estado === 'pendiente' && esAprobadorActivo;

        solicitudDiv.innerHTML = `
            <h3>${sol.titulo} (${sol.tipo})</h3>
            <p>Descripción: ${sol.descripcion}</p>
            <p>Solicitante: ${sol.solicitante ? sol.solicitante.userName || sol.solicitante.userId : 'N/A'}</p>
            <p>Estado: <strong class="estado-tag">${sol.estado.toUpperCase()}</strong></p>
            
            ${puedeAccionar ? `
                <button onclick="cambiarEstado('${sol.id}', 'aprobado')">Aprobar</button>
                <button onclick="cambiarEstado('${sol.id}', 'rechazado')">Rechazar</button>
            ` : `
                <p>Última Acción el ${formatearFecha(sol.updatedAt)}</p>
                ${sol.comentarioAprobador ? `<p><strong>Justificación:</strong> ${sol.comentarioAprobador}</p>` : ''}
                `}
            
            <hr>
        `;
        solicitudesContainer.appendChild(solicitudDiv);
    });
}

function formatearFecha(fechaStr) {
    if (!fechaStr) {
        return 'N/A';
    }
    try {
        // Crea un objeto Date o usa el constructor de new Date(string)
        const fecha = new Date(fechaStr);
        // Formatea al formato de día/mes/año hora:minuto
        return fecha.toLocaleDateString() + ' ' + fecha.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    } catch (e) {
        console.error("Error al formatear fecha:", e);
        return fechaStr.substring(0, 10); // Devuelve solo la parte YYYY-MM-DD como fallback
    }
}

/**
* @param {string} id - UUID de la solicitud.
* @param {string} nuevoEstado - 'aprobado' o 'rechazado'.
*/
async function cambiarEstado(id, nuevoEstado) {
    const accion = nuevoEstado === 'aprobado' ? 'APROBAR' : 'RECHAZAR';
    
    if (!confirm(`¿Está seguro de ${accion} la solicitud ${id}?`)) {
        return;
    }

    const userIdActivo = getActiveUserId();
    const comentario = prompt(`Por favor, ingrese un comentario para la acción de ${accion}:`);
    
    if (comentario === null) {
        // El usuario canceló el prompt
        return;
    }

    try {
        const url = `${API_BASE_URL}/${id}/estado`;
        
        const bodyData = { 
            estado: nuevoEstado, 
            comentario: comentario, // Incluir el comentario
            aprobadorId: userIdActivo
        };
        
        const response = await fetch(url, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(bodyData) 
        });

        if (!response.ok) {
            throw new Error(`Error al actualizar estado: ${response.statusText}`);
        }
        
        alert(`Solicitud ${id} actualizada a: ${nuevoEstado}`);
        await obtenerTodasLasSolicitudes(); 

    } catch (error) {
        console.error('Error al cambiar el estado:', error);
        alert(`Fallo al actualizar el estado: ${error.message}`);
    }
}

obtenerTodasLasSolicitudes();