
CREATE TABLE IF NOT EXISTS Users (
                                    id SERIAL PRIMARY KEY,
                                    name VARCHAR(50) NOT NULL,
                                    last_name VARCHAR(50) NOT NULL,
                                    email VARCHAR(100) NOT NULL UNIQUE,
                                    password VARCHAR(255) NOT NULL,
                                    phone VARCHAR(20),
                                    language VARCHAR(10)
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
                                       rating FLOAT CHECK (rating >= 1 AND rating <= 5),
                                       rating_count INT DEFAULT 0,
                                       FOREIGN KEY (doctor_id) REFERENCES Users(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS Doctor_Coverages (
                                                doctor_id INT NOT NULL,
                                                coverage_id INT NOT NULL,
                                                PRIMARY KEY (doctor_id, coverage_id),
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE,
    FOREIGN KEY (coverage_id) REFERENCES Coverages(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS Specialties (
                                           id SERIAL PRIMARY KEY,
                                           key VARCHAR(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS Doctor_Specialties (
                                                  doctor_id INT NOT NULL,
                                                  specialty_id INT NOT NULL,
                                                  PRIMARY KEY (doctor_id, specialty_id),
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE,
    FOREIGN KEY (specialty_id) REFERENCES Specialties(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS Doctor_Availability (
                                                   doctor_id INT NOT NULL,
                                                   day_of_week INT NOT NULL, -- 0 = Monday, 6 = Sunday
                                                   start_time TIME NOT NULL,
                                                   end_time TIME NOT NULL,
                                                   PRIMARY KEY (doctor_id, day_of_week, start_time),
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE
    );


CREATE TABLE IF NOT EXISTS Appointments (
                                            id SERIAL PRIMARY KEY,
                                            doctor_id INT NOT NULL,
                                            client_id INT NOT NULL,
                                            specialty_id INT NOT NULL,
                                            date TIMESTAMP NOT NULL ,
                                            status VARCHAR(20) NOT NULL DEFAULT 'confirmado' CHECK (status IN ('confirmado','cancelado')),
    reason TEXT,
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE,
    FOREIGN KEY (client_id) REFERENCES Clients(client_id) ON DELETE CASCADE,
    FOREIGN KEY (specialty_id) REFERENCES Specialties(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS Images (
                        doctor_id INT PRIMARY KEY,
                        image BYTEA NOT NULL,
                        FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Reviews (
                        id SERIAL PRIMARY KEY,
                        doctor_id INT NOT NULL,
                        client_id INT NOT NULL,
                        appointment_id INT NOT NULL,
                        rating FLOAT CHECK (rating >= 1 AND rating <= 5),
                        comment TEXT,
                        FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE,
                        FOREIGN KEY (client_id) REFERENCES Clients(client_id) ON DELETE CASCADE
);

