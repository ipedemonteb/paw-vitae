INSERT INTO Users (id, name, last_name, email, password, phone, language, is_verified)
VALUES (1, 'John', 'Doe', 'john@example.com', 'hashedpassword', '123456789', 'en', true),
       (2, 'Jane', 'Smith', 'jane@example.com', 'hashedpassword', '987654321', 'es', true);

INSERT INTO Coverages (id, coverage_name)
VALUES (1, 'Coverage A'), (2, 'Coverage B');

INSERT INTO Clients (client_id, coverage_id)
VALUES (1, 1);

INSERT INTO Doctors (doctor_id, rating, rating_count, image_id)
VALUES (2, 4.5, 10, NULL);

INSERT INTO Doctor_Coverages (doctor_id, coverage_id)
VALUES (2, 1);

INSERT INTO Specialties (id, key)
VALUES (1, 'Cardiology'),
       (2, 'Neurology'),
       (3, 'Orthopedics');

INSERT INTO Doctor_Specialties (doctor_id, specialty_id)
VALUES (2, 1);

INSERT INTO Doctor_Availability (doctor_id, day_of_week, start_time, end_time)
VALUES (2, 0, '09:00:00', '12:00:00');

INSERT INTO Appointments (id, doctor_id, client_id, specialty_id, date, status, reason)
VALUES (1, 2, 1, 1, '2025-04-29 10:00:00', 'confirmado', 'Consulta general'),
       (2, 2, 1, 1, '2025-04-30 10:00:00', 'confirmado', 'Consulta particluar');

INSERT INTO Images (id, image)
VALUES (1, X'0102030405');

INSERT INTO Ratings (id, doctor_id, client_id, appointment_id, rating, comment)
VALUES (1, 2, 1, 1, 5, 'Excelente atención');

INSERT INTO appointment_files (id, appointment_id, uploader_role, file_name, file_data)
VALUES (1, 1, 'doctor', 'informe.pdf', X'0102030405');