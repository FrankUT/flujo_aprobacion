const USER_MAPPING = {
    'u001': 'Administrador del sistema',
    'u002': 'María Pérez',
    'u003': 'Juan López',
    'u004': 'Carlos Sánchez',
    'u005': 'Laura Martínez',
    'u006': 'Roberto García',
    'u007': 'Diana Torres',
    'u008': 'Fernando Hernández',
    'u009': 'Ana Sánchez',
    'u010': 'José Ramírez'
};

const USER_KEY = 'activeUserId'; 
const DEFAULT_USER_ID = 'u001'; 

function getActiveUserId() {
    return localStorage.getItem(USER_KEY) || DEFAULT_USER_ID;
}

// Cambiar de usuario
function switchUser() {
    const currentId = getActiveUserId();
    const currentName = USER_MAPPING[currentId] || currentId;
    const userIdsList = Object.keys(USER_MAPPING).join(', ');

    let newId = prompt(
        `Usuario actual: ${currentName} (${currentId})\n` +
        `IDs disponibles: ${userIdsList}\n\n` +
        `Ingresa el nuevo ID de usuario:`
    );

    if (newId && USER_MAPPING[newId]) {
        localStorage.setItem(USER_KEY, newId);
        window.location.reload(); // Recargar con nuevo usuario
    } else if (newId) {
        alert("ID de usuario no válido. Intenta de nuevo.");
    }
}

// Inyectar nombre de usuario en el menú
function displayActiveUser() {
    const activeId = getActiveUserId();
    const activeName = USER_MAPPING[activeId] || activeId;
    const userDisplay = document.getElementById('user-display');

    if (userDisplay) {
        userDisplay.innerHTML = `
            Usuario Activo: <strong>${activeName} (${activeId})</strong> 
            [<a href="#" onclick="switchUser(); return false;">Cambiar</a>]
        `;
    }
}

document.addEventListener('DOMContentLoaded', displayActiveUser);