DROP TYPE IF EXISTS tipo_usuario CASCADE;
CREATE TYPE tipo_usuario AS ENUM ('cliente', 'doctor');

DROP TYPE IF EXISTS turno_estado CASCADE;
CREATE TYPE turno_estado AS ENUM ('pendiente', 'confirmado', 'cancelado');


-- Table: Usuarios
CREATE TABLE IF NOT EXISTS Users (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(50) NOT NULL,
                          last_name VARCHAR(50) NOT NULL,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          phone VARCHAR(20),
                          tipo_usuario tipo_usuario NOT NULL
);

-- Table: Obras_Sociales
CREATE TABLE IF NOT EXISTS Coverages (
                                id SERIAL PRIMARY KEY,
                                coverage_name VARCHAR(100) NOT NULL
);

-- Table: Clientes
-- Each cliente is a user and has a single obra social.
CREATE TABLE IF NOT EXISTS Clients (
                          client_id INT PRIMARY KEY,
                          coverage_id INT NOT NULL,
                          FOREIGN KEY (client_id) REFERENCES Users(id) ON DELETE CASCADE,
                          FOREIGN KEY (coverage_id) REFERENCES Coverages(id)
);

-- Table: Doctores
-- Each doctor is also a user; additional doctor-specific fields are included.
CREATE TABLE IF NOT EXISTS Doctors (
                          doctor_id INT PRIMARY KEY,
                          specialties TEXT NOT NULL,
                          FOREIGN KEY (doctor_id) REFERENCES Users(id) ON DELETE CASCADE
);

-- Table: Doctor_Obra_Social
-- Many-to-many relationship between doctors and obras sociales.
CREATE TABLE IF NOT EXISTS Doctor_Coverages (
                                    doctor_id INT NOT NULL,
                                    coverage_id INT NOT NULL,
                                    PRIMARY KEY (doctor_id, coverage_id),
                                    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE,
                                    FOREIGN KEY (coverage_id) REFERENCES Coverages(id) ON DELETE CASCADE
);

-- Table: Turnos
-- Stores appointment bookings, relating clients with doctors.
CREATE TABLE IF NOT EXISTS Appointments (
                        id SERIAL PRIMARY KEY,
                        doctor_id INT NOT NULL,
                        client_id INT NOT NULL,
                        start_date TIMESTAMP NOT NULL,
                        status turno_estado NOT NULL DEFAULT 'pendiente',
                        FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE,
                        FOREIGN KEY (client_id) REFERENCES Clients(client_id) ON DELETE CASCADE
);
