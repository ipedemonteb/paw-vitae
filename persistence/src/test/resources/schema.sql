CREATE TABLE IF NOT EXISTS Users (
    id INTEGER IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20)
    );

CREATE TABLE IF NOT EXISTS Coverages (
                                         id INTEGER IDENTITY PRIMARY KEY,
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
                                           id INTEGER IDENTITY PRIMARY KEY,
                                           key VARCHAR(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS Doctor_Specialties (
                                                  doctor_id INT NOT NULL,
                                                  specialty_id INT NOT NULL,
                                                  PRIMARY KEY (doctor_id, specialty_id),
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE,
    FOREIGN KEY (specialty_id) REFERENCES Specialties(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS Appointments (
                                            id INTEGER IDENTITY PRIMARY KEY,
                                            doctor_id INT NOT NULL,
                                            client_id INT NOT NULL,
                                            specialty_id INT NOT NULL,
                                            date TIMESTAMP NOT NULL,
                                            status VARCHAR(20) DEFAULT 'pendiente',
    reason VARCHAR(255),
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE,
    FOREIGN KEY (client_id) REFERENCES Clients(client_id) ON DELETE CASCADE,
    FOREIGN KEY (specialty_id) REFERENCES Specialties(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS Images (
                                      doctor_id INT PRIMARY KEY,
                                      image BINARY NOT NULL,
                                      FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE
    );
