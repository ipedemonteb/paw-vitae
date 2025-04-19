-- DROP TABLE IF EXISTS Appointments CASCADE;
-- DROP TABLE IF EXISTS Images CASCADE;
-- DROP TABLE IF EXISTS Doctor_Specialties CASCADE;
-- DROP TABLE IF EXISTS Doctor_Coverages CASCADE;
-- DROP TABLE IF EXISTS Doctors CASCADE;
-- DROP TABLE IF EXISTS Clients CASCADE;
-- DROP TABLE IF EXISTS Specialties CASCADE;
-- DROP TABLE IF EXISTS Coverages CASCADE;
-- DROP TABLE IF EXISTS Users CASCADE;
-- DROP TABLE IF EXISTS Doctor_Availability CASCADE;




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
--                                        specialties TEXT NOT NULL,
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
                                            status VARCHAR(20) NOT NULL DEFAULT 'pendiente' CHECK (status IN ('pendiente','confirmado','cancelado')),
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



-- -- 1. Insert Users
-- -- Insert the single client user
-- INSERT INTO Users (name, last_name, email, password, phone)
-- VALUES ('John', 'Doe', 'john.doe@example.com', 'password123', '1234567890');
-- -- (Assume this gives id = 1)
--
-- -- Insert doctor users
-- INSERT INTO Users (name, last_name, email, password, phone)
-- VALUES
--     ('Alice', 'Smith', 'alice.smith@example.com', 'securepass', '0987654321'),  -- id = 2
--     ('Bob', 'Johnson', 'jobenegaslynch@itba.edu.ar', 'strongpass', '5551234567'),    -- id = 3
--     ('Carol', 'Davis', 'carol.davis@example.com', 'passw0rd', '5559876543');      -- id = 4
--
-- -- 2. Insert Coverages
-- INSERT INTO Coverages (coverage_name) VALUES
--                                           ('OSDE'),    -- id = 1
--                                           ('Galeno'),  -- id = 2
--                                           ('Medifé');  -- id = 3
--
-- -- 3. Insert the Client record (only one client)
-- INSERT INTO Clients (client_id, coverage_id)
-- VALUES (1, 1);  -- John Doe uses OSDE (coverage id 1)
--
-- -- 4. Insert Doctors
-- INSERT INTO Doctors (doctor_id) VALUES
--                                     (2),  -- Alice Smith
--                                     (3),  -- Bob Johnson
--                                     (4);  -- Carol Davis
--
-- -- 5. Associate Doctors with Coverages (Doctor_Coverages)
-- -- Alice (id=2) accepts OSDE and Galeno
-- INSERT INTO Doctor_Coverages (doctor_id, coverage_id) VALUES (2, 1), (2, 2);
-- -- Bob (id=3) accepts Galeno and Medifé
-- INSERT INTO Doctor_Coverages (doctor_id, coverage_id) VALUES (3, 2), (3, 3);
-- -- Carol (id=4) accepts OSDE and Medifé
-- INSERT INTO Doctor_Coverages (doctor_id, coverage_id) VALUES (4, 1), (4, 3);
--
-- -- 6. Insert Specialties
-- INSERT INTO Specialties (key) VALUES
--                                             ('specialty.general'),
--                                             ('specialty.cardiologia'),
--                                             ('specialty.dermatologia'),
--                                             ('specialty.endocrinologia'),
--                                             ('specialty.gastroenterologia'),
--                                             ('specialty.hematologia'),
--                                             ('specialty.enfermedades.infecciosas'),
--                                             ('specialty.nefrologia'),
--                                             ('specialty.neurologia'),
--                                             ('specialty.oncologia'),
--                                             ('specialty.pulmonologia'),
--                                             ('specialty.reumatologia'),
--                                             ('specialty.urologia'),
--                                             ('specialty.pediatria'),
--                                             ('specialty.ginecologia'),
--                                             ('specialty.traumatologia');
--
--
-- -- 7. Associate Doctors with Specialties (Doctor_Specialties)
-- -- Alice (id=2) has Cardiology and General Medicine
-- INSERT INTO Doctor_Specialties (doctor_id, specialty_id) VALUES (2, 1), (2, 2);
-- -- Bob (id=3) has Dermatology
-- INSERT INTO Doctor_Specialties (doctor_id, specialty_id) VALUES (3, 3);
-- -- Carol (id=4) has General Medicine
-- INSERT INTO Doctor_Specialties (doctor_id, specialty_id) VALUES (4, 2);
--
-- -- 8. Insert Appointments for the one client (id=1) with different doctors/specialties
-- -- Appointment with Alice (id=2), specialty Cardiology (id=1)
-- INSERT INTO Appointments (doctor_id, client_id, specialty_id, date, reason)
-- VALUES (2, 1, 1, '2025-05-01 09:00:00', 'Routine cardiology check-up');
--
-- -- Appointment with Bob (id=3), specialty Dermatology (id=3)
-- INSERT INTO Appointments (doctor_id, client_id, specialty_id, date, reason)
-- VALUES (3, 1, 3, '2025-05-02 10:30:00', 'Skin rash evaluation');
--
-- -- Appointment with Carol (id=4), specialty General Medicine (id=2)
-- INSERT INTO Appointments (doctor_id, client_id, specialty_id, date, reason)
-- VALUES (4, 1, 2, '2025-05-03 14:00:00', 'General consultation');
--
--
