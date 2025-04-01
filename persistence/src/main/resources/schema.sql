DROP TYPE IF EXISTS user_type CASCADE;
CREATE TYPE user_type AS ENUM ('cliente', 'doctor');

DROP TYPE IF EXISTS appointment_state CASCADE;
CREATE TYPE appointment_state AS ENUM ('pendiente', 'confirmado', 'cancelado');

CREATE TABLE IF NOT EXISTS Users (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(50) NOT NULL,
                          last_name VARCHAR(50) NOT NULL,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          phone VARCHAR(20),
                          user_type user_type NOT NULL
);

CREATE TABLE IF NOT EXISTS Coverages (
                                id SERIAL PRIMARY KEY,
                                coverage_name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS Clients (
                          client_id INT PRIMARY KEY,
                          coverage_id INT NOT NULL,
                          FOREIGN KEY (client_id) REFERENCES Users(id) ON DELETE CASCADE,
                          FOREIGN KEY (coverage_id) REFERENCES Coverages(id)
);

CREATE TABLE IF NOT EXISTS Doctors (
                          doctor_id INT PRIMARY KEY,
                          specialties TEXT NOT NULL,
                          FOREIGN KEY (doctor_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Doctor_Coverages (
                                    doctor_id INT NOT NULL,
                                    coverage_id INT NOT NULL,
                                    PRIMARY KEY (doctor_id, coverage_id),
                                    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE,
                                    FOREIGN KEY (coverage_id) REFERENCES Coverages(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Appointments (
                        id SERIAL PRIMARY KEY,
                        doctor_id INT NOT NULL,
                        client_id INT NOT NULL,
                        start_date TIMESTAMP NOT NULL,
                        status appointment_state NOT NULL DEFAULT 'pendiente',
                        reason TEXT,
                        FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE,
                        FOREIGN KEY (client_id) REFERENCES Clients(client_id) ON DELETE CASCADE
);
