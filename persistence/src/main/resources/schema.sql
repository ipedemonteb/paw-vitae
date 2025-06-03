CREATE TABLE IF NOT EXISTS Users (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    language VARCHAR(10),
    verification_token VARCHAR(255),
    reset_token VARCHAR(255),
    is_verified BOOLEAN DEFAULT FALSE,
    token_expiration_date TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS Coverages (
                                         id SERIAL PRIMARY KEY,
                                         coverage_name VARCHAR(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS Patients (
                                       patient_id INT PRIMARY KEY,
                                       coverage_id INT NOT NULL,
                                        neighborhood_id INT NOT NULL,
                                        FOREIGN KEY (neighborhood_id) REFERENCES Neighborhoods(id) ON DELETE RESTRICT,
                                       FOREIGN KEY (patient_id) REFERENCES Users(id) ON DELETE CASCADE,
                                        FOREIGN KEY (coverage_id) REFERENCES Coverages(id) ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS Images (
                                      id SERIAL PRIMARY KEY,
                                      image BYTEA NOT NULL
);

CREATE TABLE IF NOT EXISTS Doctors (
                                       doctor_id INT PRIMARY KEY,
                                       rating FLOAT CHECK (rating >= 1 AND rating <= 5),
    rating_count INT DEFAULT 0,
    image_id INT,
    FOREIGN KEY (doctor_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES Images(id) ON DELETE SET NULL
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
                                                   day_of_week INT NOT NULL,
                                                   start_time TIME NOT NULL,
                                                   end_time TIME NOT NULL,
                                                   PRIMARY KEY (doctor_id, day_of_week, start_time),
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS Appointments (
                                            id SERIAL PRIMARY KEY,
                                            doctor_id INT NOT NULL,
                                            patient_id INT NOT NULL,
                                            specialty_id INT NOT NULL,
                                            date TIMESTAMP NOT NULL,
                                            status VARCHAR(20) NOT NULL DEFAULT 'confirmado' CHECK (status IN ('confirmado','cancelado','completo')),
    reason TEXT,
    report TEXT,
    office_id INT NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (specialty_id) REFERENCES Specialties(id) ON DELETE RESTRICT,
    FOREIGN KEY (office_id) REFERENCES Doctor_Offices(id) ON DELETE RESTRICT
    );

CREATE TABLE IF NOT EXISTS Ratings (
                                       id SERIAL PRIMARY KEY,
                                       doctor_id INT NOT NULL,
                                       patient_id INT NOT NULL,
                                       appointment_id INT NOT NULL UNIQUE,
                                       rating INT CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES Appointments(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS appointment_files (
                                                 id SERIAL PRIMARY KEY,
                                                 appointment_id INTEGER NOT NULL,
                                                 uploader_role VARCHAR(20) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_data BYTEA NOT NULL,
    FOREIGN KEY (appointment_id) REFERENCES Appointments(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS Neighborhoods
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS Doctor_Offices (
                                              id SERIAL PRIMARY KEY,
                                              doctor_id INT NOT NULL,
                                              neighborhood_id INT NOT NULL,
                                              office_name VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    removed TIMESTAMP,
    UNIQUE (doctor_id, neighborhood_id, office_name, removed),
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE,
    FOREIGN KEY (neighborhood_id) REFERENCES Neighborhoods(id) ON DELETE RESTRICT
    );

CREATE TABLE IF NOT EXISTS Doctor_Unavailability (
                                                     doctor_id INT NOT NULL,
                                                     start_date DATE NOT NULL,
                                                     end_date DATE NOT NULL,
                                                     PRIMARY KEY (doctor_id, start_date, end_date),
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS Doctor_Office_Specialties (
                                                            office_id INT NOT NULL,
                                                            specialty_id INT NOT NULL,
                                                            PRIMARY KEY (office_id, specialty_id),
    FOREIGN KEY (office_id) REFERENCES Doctor_Offices(id) ON DELETE CASCADE,
    FOREIGN KEY (specialty_id) REFERENCES Specialties(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Doctor_Profile (
                                              doctor_id INT PRIMARY KEY,
                                              bio TEXT,
                                              description TEXT,
                                              FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Doctor_Experience (
                                                 id SERIAL PRIMARY KEY,
                                                 doctor_id INT NOT NULL,
                                                 position_title VARCHAR(100) NOT NULL,
    organization_name VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    description TEXT,
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS Doctor_Certifications (
                                                     id SERIAL PRIMARY KEY,
                                                     doctor_id INT NOT NULL,
                                                     certificate_name VARCHAR(100) NOT NULL,
    issuing_entity VARCHAR(100) NOT NULL,
    issue_date DATE NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE
    );
