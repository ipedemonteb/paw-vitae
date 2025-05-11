document.addEventListener("DOMContentLoaded", () => {
    const fileInput = document.getElementById("files")
    const filePreview = document.getElementById("filePreview")
    const dropZone = document.getElementById("dropZone")
    let appointmentForm = document.getElementById("appointmentForm")
    if (!appointmentForm){
        appointmentForm = document.getElementById("doctorFileForm");
    }
    const MAX_FILES = 5
    const ALLOWED_TYPES = ["application/pdf"]
    const MAX_FILE_SIZE = 3 * 1024 * 1024 // 3MB en bytes

    let selectedFiles = [];


    window.addEventListener("pageshow", () => {
        const fileInput = document.getElementById("files")
        const filePreview = document.getElementById("filePreview")

        if (fileInput) fileInput.value = ""
        if (filePreview) filePreview.innerHTML = ""
        selectedFiles = [];
        if(!dropZone){
            return
        }
        dropZone.classList.remove("disabled")
    })

    const showNotification = (message, type = "info") => {
        const notification = document.createElement("div")
        notification.className = `file-notification ${type}`
        notification.textContent = message

        document.body.appendChild(notification)

        setTimeout(() => {
            notification.classList.add("show")
        }, 10)

        setTimeout(() => {
            notification.classList.remove("show")
            setTimeout(() => {
                notification.remove()
            }, 300)
        }, 3000)
    }


    const validateFiles = (files) => {

        if (selectedFiles.length + files.length > MAX_FILES) {
            showNotification(window.appointmentMessages?.fileUpload?.tooManyFiles, "error")
            return false
        }

        for (let i = 0; i < files.length; i++) {
            if (!ALLOWED_TYPES.includes(files[i].type)) {
                showNotification(
                    window.appointmentMessages?.fileUpload?.invalidType,
                    "error",
                )
                return false
            }


            if (files[i].size > MAX_FILE_SIZE) {
                showNotification(
                    window.appointmentMessages?.fileUpload?.fileTooLarge ,
                    "error",
                )

                return false
            }
        }
        showSuccessToast()
        return true
    }

    const updateFileInput = () => {
        const formData = new FormData();

        selectedFiles.forEach((file, index) => {
            formData.append("files", file);
        });

        return formData;
    };

    const updatePreview = (files) => {
        if (!validateFiles(files)){
            fileInput.value = ""
            return
        }

        Array.from(files).forEach((file) => {
            selectedFiles.push(file);

            const fileItem = document.createElement("div");
            fileItem.className = "file-item";
            fileItem.dataset.fileName = file.name;

            const fileIcon = document.createElement("div");
            fileIcon.className = "file-icon";
            fileIcon.innerHTML = '<i class="fas fa-file-pdf"></i>';

            const fileInfo = document.createElement("div");
            fileInfo.className = "file-info";

            const fileName = document.createElement("div");
            fileName.className = "file-name";
            fileName.textContent = file.name;

            const fileSize = document.createElement("div");
            fileSize.className = "file-size";
            fileSize.textContent = formatFileSize(file.size);

            fileInfo.appendChild(fileName);
            fileInfo.appendChild(fileSize);

            const fileRemove = document.createElement("button");
            fileRemove.className = "file-remove";
            fileRemove.type = "button";
            fileRemove.innerHTML = '<i class="fas fa-times"></i>';
            fileRemove.addEventListener("click", (e) => {
                e.preventDefault();
                fileItem.classList.add("removing");

                const fileIndex = selectedFiles.findIndex(f => f.name === file.name);
                if (fileIndex !== -1) {
                    selectedFiles.splice(fileIndex, 1);
                }

                setTimeout(() => {
                    fileItem.remove();
                    // showNotification(window.appointmentMessages?.fileUpload?.fileRemoved || "Archivo eliminado");

                    updateDropzoneState();
                }, 300);
            });
            fileItem.appendChild(fileIcon);
            fileItem.appendChild(fileInfo);
            fileItem.appendChild(fileRemove);

            // Agregar a la vista previa con animación
            fileItem.style.opacity = "0";
            fileItem.style.transform = "translateY(10px)";
            filePreview.appendChild(fileItem);

            // Forzar un reflow para que la animación funcione
            fileItem.offsetHeight;

            // Aplicar animación
            fileItem.style.opacity = "1";
            fileItem.style.transform = "translateY(0)";

            // showNotification(window.appointmentMessages?.fileUpload?.fileAdded || "Archivo añadido");
        })

        fileInput.value = "";
        updateDropzoneState();
    }

    const formatFileSize = (bytes) => {
        if (bytes < 1024) return bytes + " B"
        else if (bytes < 1048576) return (bytes / 1024).toFixed(1) + " KB"
        else return (bytes / 1048576).toFixed(1) + " MB"
    }

    const updateDropzoneState = () => {
        if(!dropZone || !filePreview) {
            return
        }
        if (selectedFiles.length >= MAX_FILES) {
            dropZone.classList.add("disabled")
        } else {
            dropZone.classList.remove("disabled")
        }
    }

    if (fileInput && filePreview) {
        fileInput.addEventListener("change", function () {
            updatePreview(this.files)
        })
    }

    if (dropZone) {
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


        ;["dragleave", "drop"].forEach((eventName) => {
            dropZone.addEventListener(
                eventName,
                () => {
                    dropZone.classList.remove("active")
                },
                false,
            )
        })


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


        dropZone.addEventListener("click", () => {
            if (!dropZone.classList.contains("disabled")) {
                fileInput.click()
            }
        })
    }


    if (appointmentForm) {
        appointmentForm.addEventListener("submit", function(e) {

            if (selectedFiles.length > 0) {
                const dataTransfer = new DataTransfer();
                selectedFiles.forEach(file => {
                    dataTransfer.items.add(file);
                });

                fileInput.files = dataTransfer.files;
            }
        });
    }

    updateDropzoneState()
})