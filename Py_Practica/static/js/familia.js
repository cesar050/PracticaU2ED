document.getElementById("ordenarForm").addEventListener("submit", function (event) {
    event.preventDefault(); // Evitar recargar la página

    // Obtener valores del formulario
    const attribute = document.getElementById("attribute").value;
    const type = document.getElementById("type").value;
    const metodo = document.getElementById("metodo").value;

    // Construir la URL del endpoint
    const url1 = `http://localhost:5000/list/ordernar/${attribute}/${type}/${metodo}`;

    const url = `/list/ordernar/${attribute}/${type}/${metodo}`;

    // Hacer la llamada al backend
    fetch(url)
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Error en la respuesta: ${response.status}`);
            }
            return response.json();
        })
        .then((data) => {
            if (data.estado === "success") {
                actualizarTabla(data.data); // Actualizar tabla con los datos
            } else {
                alert(`Error: ${data.mensaje || "Error desconocido."}`);
            }
        })
        .catch((error) => {
            console.error("Error al consumir el servicio:", error);
            alert("Ocurrió un error al ordenar las familias.");
        });
});

// Función para actualizar la tabla con los datos nuevos
function actualizarTabla(familias) {
    const tbody = document.querySelector("tbody");
    tbody.innerHTML = ""; // Limpiar tabla existente

    familias.forEach((familia) => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${familia.nombre || "No especificado"}</td>
            <td>${familia.tieneGenerador ? "Sí" : "No"}</td>
            <td>${familia.presupuesto ?? "No especificado"}</td>
            <td>${familia.costoGenerador ?? "No especificado"}</td>
            <td>${familia.consumoPorHora ?? "No especificado"}</td>
            <td>${familia.energiaGenerada ?? "No especificado"}</td>
            <td>${familia.usoGenerador || "No especificado"}</td>
            <td>
                <a href="/route_familia/get_familia_by_id/${familia.id}" class="btn btn-primary btn-sm">
                    <i class="fas fa-edit"></i> Modificar
                </a>
                <form action="/route_familia/eliminar_familia/${familia.id}" method="POST" style="display:inline;">
                    <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('¿Estás seguro de eliminar esta familia?');">
                        <i class="fas fa-trash-alt"></i> Eliminar
                    </button>
                </form>
            </td>
            <td>
                <a href="/route_familia/get_familia/${familia.id}" class="btn btn-primary btn-sm">
                    <i class="fas fa-edit"></i> Ver generadores
                </a>
            </td>
        `;
        tbody.appendChild(row);
    });
}
