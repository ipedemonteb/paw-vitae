/**
 * Inicializa la funcionalidad de carga de imagen de perfil
 */
function initImageUpload() {
    const fileInput = document.getElementById("image")
    const uploadArea = document.getElementById("image-upload-area")
    const previewContainer = document.getElementById("image-preview-container")
    const previewImage = document.getElementById("image-preview")
    const previewName = document.getElementById("image-preview-name")
    const previewDetails = document.getElementById("image-preview-details")
    const removeButton = document.getElementById("remove-image")
    const errorElement = document.getElementById("image-error")
    const doctorProfileImage = document.getElementById("doctor-profile-image")
    const currentImageUrl = doctorProfileImage ? doctorProfileImage.src : ""

    // Verificar que todos los elementos existan
    if (
        !fileInput ||
        !uploadArea ||
        !previewContainer ||
        !previewImage ||
        !previewName ||
        !previewDetails ||
        !removeButton
    ) {
        return
    }

    // Hacer clic en el área de carga para abrir el selector de archivos
    // Importante: Asegurarse de que este evento no se registre múltiples veces
    uploadArea.addEventListener(
        "click",
        (e) => {
            e.preventDefault() // Prevenir comportamiento por defecto
            e.stopPropagation() // Detener propagación del evento
            fileInput.click()
        },
        false,
    )
    // Prevenir comportamiento por defecto del drag & drop
    ;["dragenter", "dragover", "dragleave", "drop"].forEach((eventName) => {
        uploadArea.addEventListener(eventName, preventDefaults, false)
    })

    function preventDefaults(e) {
        e.preventDefault()
        e.stopPropagation()
    }
    // Resaltar área de carga durante el drag
    ;["dragenter", "dragover"].forEach((eventName) => {
        uploadArea.addEventListener(eventName, () => {
            uploadArea.classList.add("active")
        })
    })
    ;["dragleave", "drop"].forEach((eventName) => {
        uploadArea.addEventListener(eventName, () => {
            uploadArea.classList.remove("active")
        })
    })

    // Manejar el drop de archivos
    uploadArea.addEventListener("drop", (e) => {
        const dt = e.dataTransfer
        const files = dt.files

        if (files.length > 0) {
            fileInput.files = files
            handleFileSelect(files[0])
        }
    })

    // Manejar la selección de archivos
    fileInput.addEventListener("change", function () {
        if (this.files && this.files[0]) {
            handleFileSelect(this.files[0])
        }
    })

    // Función para manejar la selección de archivos
    function handleFileSelect(file) {
        // Validar tamaño del archivo (max 2MB)
        if (file.size > 2 * 1024 * 1024) {
            return
        }

        // Validar tipo de archivo
        const validTypes = ["image/jpeg", "image/png", "image/jpg"]
        if (!validTypes.includes(file.type)) {
            return
        }

        // Limpiar error si existe
        clearError()

        // Mostrar información del archivo
        previewName.textContent = file.name

        // Formatear y mostrar detalles del archivo
        const fileExtension = file.name.split(".").pop().toUpperCase()
        const fileSize = formatFileSize(file.size)
        previewDetails.textContent = `${fileExtension} • ${fileSize}`

        // Mostrar previsualización
        const reader = new FileReader()
        reader.onload = (e) => {
            previewImage.src = e.target.result
            previewContainer.style.display = "block"


        }
        reader.readAsDataURL(file)
    }

    // Función para mostrar error
    function showError(message) {
        if (errorElement) {
            errorElement.textContent = message
            errorElement.style.display = "block"
            uploadArea.classList.add("error")
        }
    }

    // Función para limpiar error
    function clearError() {
        if (errorElement) {
            errorElement.textContent = ""
            errorElement.style.display = "none"
            uploadArea.classList.remove("error")
        }
    }

    // Botón para eliminar la imagen seleccionada
    removeButton.addEventListener("click", (e) => {
        e.preventDefault() // Prevenir comportamiento por defecto
        e.stopPropagation() // Detener propagación del evento

        fileInput.value = ""
        previewContainer.style.display = "none"
        previewImage.src = ""

        // Restaurar la imagen original del perfil
        if (doctorProfileImage) {
            doctorProfileImage.src = currentImageUrl
        }
    })

    // Formatear tamaño del archivo
    function formatFileSize(bytes) {
        if (bytes < 1024) return bytes + " bytes"
        else if (bytes < 1048576) return (bytes / 1024).toFixed(1) + " KB"
        else return (bytes / 1048576).toFixed(1) + " MB"
    }
}

// Asegurarse de que la inicialización solo ocurra una vez
let imageUploadInitialized = false
document.addEventListener("DOMContentLoaded", () => {
    if (!imageUploadInitialized) {
        initImageUpload()
        imageUploadInitialized = true
    }
})
