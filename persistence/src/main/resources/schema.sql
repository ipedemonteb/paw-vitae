-- DROP TABLE IF EXISTS Users CASCADE;
-- DROP TABLE IF EXISTS Coverages CASCADE;
-- DROP TABLE IF EXISTS Clients CASCADE;
-- DROP TABLE IF EXISTS Doctors CASCADE;
-- DROP TABLE IF EXISTS Doctor_Coverages CASCADE;
-- DROP TABLE IF EXISTS Appointments CASCADE;

CREATE TABLE IF NOT EXISTS Users (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20)
--                         TODO: ADD MISSING COLUMNS, E.G. DNI
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
                                            status VARCHAR(20) NOT NULL DEFAULT 'pendiente' CHECK (status IN ('pendiente','confirmado','cancelado')),
    reason TEXT,
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE,
    FOREIGN KEY (client_id) REFERENCES Clients(client_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS Images (
                        image_id SERIAL PRIMARY KEY,
                        doctor_id INT UNIQUE,
                        image BYTEA NOT NULL,
                        FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE
);