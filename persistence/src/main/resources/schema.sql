DROP TYPE IF EXISTS tipo_usuario CASCADE;
CREATE TYPE tipo_usuario AS ENUM ('cliente', 'doctor');

DROP TYPE IF EXISTS turno_estado CASCADE;
CREATE TYPE turno_estado AS ENUM ('pendiente', 'confirmado', 'cancelado');

-- Table: Usuarios
CREATE TABLE IF NOT EXISTS Usuarios (
                          id_usuario SERIAL PRIMARY KEY,
                          nombre VARCHAR(50) NOT NULL,
                          apellido VARCHAR(50) NOT NULL,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          telefono VARCHAR(20),
                          tipo_usuario tipo_usuario NOT NULL
);

-- Table: Obras_Sociales
CREATE TABLE IF NOT EXISTS Obras_Sociales (
                                id_obra_social SERIAL PRIMARY KEY,
                                nombre_obra_social VARCHAR(100) NOT NULL,
                                detalles TEXT
);

-- Table: Clientes
-- Each cliente is a user and has a single obra social.
CREATE TABLE IF NOT EXISTS Clientes (
                          id_cliente INT PRIMARY KEY,
                          id_obra_social INT NOT NULL,
                          FOREIGN KEY (id_cliente) REFERENCES Usuarios(id_usuario) ON DELETE CASCADE,
                          FOREIGN KEY (id_obra_social) REFERENCES Obras_Sociales(id_obra_social)
);

-- Table: Doctores
-- Each doctor is also a user; additional doctor-specific fields are included.
CREATE TABLE IF NOT EXISTS Doctores (
                          id_doctor INT PRIMARY KEY,
                          especialidad VARCHAR(100),
                          direccion_consultorio VARCHAR(200),
                          FOREIGN KEY (id_doctor) REFERENCES Usuarios(id_usuario) ON DELETE CASCADE
);

-- Table: Doctor_Obra_Social
-- Many-to-many relationship between doctors and obras sociales.
CREATE TABLE IF NOT EXISTS Doctor_Obra_Social (
                                    id_doctor INT NOT NULL,
                                    id_obra_social INT NOT NULL,
                                    PRIMARY KEY (id_doctor, id_obra_social),
                                    FOREIGN KEY (id_doctor) REFERENCES Doctores(id_doctor) ON DELETE CASCADE,
                                    FOREIGN KEY (id_obra_social) REFERENCES Obras_Sociales(id_obra_social) ON DELETE CASCADE
);

-- Table: Turnos
-- Stores appointment bookings, relating clients with doctors.
CREATE TABLE IF NOT EXISTS Turnos (
                        id_turno SERIAL PRIMARY KEY,
                        id_doctor INT NOT NULL,
                        id_cliente INT NOT NULL,
                        fecha_hora_turno TIMESTAMP NOT NULL,
                        estado turno_estado NOT NULL DEFAULT 'pendiente',
                        FOREIGN KEY (id_doctor) REFERENCES Doctores(id_doctor) ON DELETE CASCADE,
                        FOREIGN KEY (id_cliente) REFERENCES Clientes(id_cliente) ON DELETE CASCADE
);
