document.addEventListener("DOMContentLoaded", () => {
    const fileInput = document.getElementById("files")
    const filePreview = document.getElementById("filePreview")
    const dropZone = document.getElementById("dropZone")
    const MAX_FILES = 5
    const ALLOWED_TYPES = ["application/pdf"]
    const MAX_FILE_SIZE = 3 * 1024 * 1024 // 3MB en bytes

    // Función para mostrar notificación
    const showNotification = (message, type = "info") => {
        const notification = document.createElement("div")
        notification.className = `file-notification ${type}`
        notification.textContent = message

        document.body.appendChild(notification)

        // Animación de entrada
        setTimeout(() => {
            notification.classList.add("show")
        }, 10)

        // Eliminar después de 3 segundos
        setTimeout(() => {
            notification.classList.remove("show")
            setTimeout(() => {
                notification.remove()
            }, 300)
        }, 3000)
    }

    // Función para validar archivos
    const validateFiles = (files) => {
        // Verificar número máximo de archivos
        if (filePreview.children.length + files.length > MAX_FILES) {
            showNotification(window.appointmentMessages?.fileUpload?.tooManyFiles || "Máximo 5 archivos permitidos", "error")
            return false
        }

        // Verificar tipos de archivo
        for (let i = 0; i < files.length; i++) {
            if (!ALLOWED_TYPES.includes(files[i].type)) {
                showNotification(
                    window.appointmentMessages?.fileUpload?.invalidType || "Solo se permiten archivos PDF",
                    "error",
                )
                return false
            }


            if (files[i].size > MAX_FILE_SIZE) {
                showNotification(
                    window.appointmentMessages?.fileUpload?.fileTooLarge || `El archivo ${files[i].name} excede el límite de 3MB`,
                    "error",
                )

                return false
            }
        }

        return true
    }

    // Función para actualizar la vista previa
    const updatePreview = (files) => {
        if (!validateFiles(files)){
            fileInput.value = ""
            return
        }

        Array.from(files).forEach((file) => {
            // Crear contenedor del archivo
            const fileItem = document.createElement("div")
            fileItem.className = "file-item"

            // Icono del archivo
            const fileIcon = document.createElement("div")
            fileIcon.className = "file-icon"
            fileIcon.innerHTML = '<i class="fas fa-file-pdf"></i>'

            // Información del archivo
            const fileInfo = document.createElement("div")
            fileInfo.className = "file-info"

            // Nombre del archivo
            const fileName = document.createElement("div")
            fileName.className = "file-name"
            fileName.textContent = file.name

            // Tamaño del archivo
            const fileSize = document.createElement("div")
            fileSize.className = "file-size"
            fileSize.textContent = formatFileSize(file.size)

            fileInfo.appendChild(fileName)
            fileInfo.appendChild(fileSize)

            // Botón para eliminar
            const fileRemove = document.createElement("button")
            fileRemove.className = "file-remove"
            fileRemove.type = "button"
            fileRemove.innerHTML = '<i class="fas fa-times"></i>'
            fileRemove.addEventListener("click", (e) => {
                e.preventDefault()
                fileItem.classList.add("removing")

                setTimeout(() => {
                    fileItem.remove()
                    showNotification(window.appointmentMessages?.fileUpload?.fileRemoved || "Archivo eliminado")

                    // Actualizar estado del dropzone
                    updateDropzoneState()
                }, 300)
            })

            // Agregar elementos al contenedor
            fileItem.appendChild(fileIcon)
            fileItem.appendChild(fileInfo)
            fileItem.appendChild(fileRemove)

            // Agregar a la vista previa con animación
            fileItem.style.opacity = "0"
            fileItem.style.transform = "translateY(10px)"
            filePreview.appendChild(fileItem)

            // Forzar un reflow para que la animación funcione
            fileItem.offsetHeight

            // Aplicar animación
            fileItem.style.opacity = "1"
            fileItem.style.transform = "translateY(0)"

            showNotification(window.appointmentMessages?.fileUpload?.fileAdded || "Archivo añadido")
        })

        // Actualizar estado del dropzone
        updateDropzoneState()
    }

    // Función para formatear el tamaño del archivo
    const formatFileSize = (bytes) => {
        if (bytes < 1024) return bytes + " B"
        else if (bytes < 1048576) return (bytes / 1024).toFixed(1) + " KB"
        else return (bytes / 1048576).toFixed(1) + " MB"
    }

    // Función para actualizar el estado del dropzone
    const updateDropzoneState = () => {
        if (filePreview.children.length >= MAX_FILES) {
            dropZone.classList.add("disabled")
        } else {
            dropZone.classList.remove("disabled")
        }
    }

    // Evento de cambio en el input de archivos
    if (fileInput && filePreview) {
        fileInput.addEventListener("change", function () {
            updatePreview(this.files)
        })
    }

    // Eventos de arrastrar y soltar
    if (dropZone) {
        // Prevenir comportamiento por defecto
        ;["dragenter", "dragover", "dragleave", "drop"].forEach((eventName) => {
            dropZone.addEventListener(
                eventName,
                (e) => {
                    e.preventDefault()
                    e.stopPropagation()
                },
                false,
            )
        })

        // Resaltar el dropzone cuando se arrastra un archivo sobre él
        ;["dragenter", "dragover"].forEach((eventName) => {
            dropZone.addEventListener(
                eventName,
                () => {
                    if (!dropZone.classList.contains("disabled")) {
                        dropZone.classList.add("active")
                    }
                },
                false,
            )
        })

        // Quitar resaltado cuando se deja de arrastrar
        ;["dragleave", "drop"].forEach((eventName) => {
            dropZone.addEventListener(
                eventName,
                () => {
                    dropZone.classList.remove("active")
                },
                false,
            )
        })

        // Manejar la caída de archivos
        dropZone.addEventListener(
            "drop",
            (e) => {
                if (!dropZone.classList.contains("disabled")) {
                    const dt = e.dataTransfer
                    const files = dt.files

                    updatePreview(files)
                }
            },
            false,
        )

        // Abrir el selector de archivos al hacer clic en el dropzone
        dropZone.addEventListener("click", () => {
            if (!dropZone.classList.contains("disabled")) {
                fileInput.click()
            }
        })
    }

    // Inicializar el estado del dropzone
    updateDropzoneState()
})
