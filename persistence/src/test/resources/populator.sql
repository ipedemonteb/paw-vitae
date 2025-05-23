INSERT INTO Users (id, name, last_name, email, password, phone, language, verification_token, reset_token, is_verified, token_expiration_date)
VALUES
    (1, 'John', 'Doe', 'john@example.com', 'hashedpassword', '123456789', 'en', 'VERIFTOKEN', 'RESTOKEN', true, DATEADD('DAY', 30, CURRENT_TIMESTAMP)),
    (2, 'Jane', 'Smith', 'jane@example.com', 'hashedpassword', '987654321', 'es', 'VERIFTOKEN2', 'RESTOKEN2', true, DATEADD('DAY', 30, CURRENT_TIMESTAMP)),
    (3, 'Michael', 'Johnson', 'michael@example.com', 'hashedpassword', '111111111', 'en', 'VERIFTOKEN3', 'RESTOKEN3', true, DATEADD('DAY', 30, CURRENT_TIMESTAMP)),
    (4, 'Emily', 'Davis', 'emily@example.com', 'hashedpassword', '333333333', 'es', 'VERIFTOKEN4', 'RESTOKEN4', true, DATEADD('DAY', 30, CURRENT_TIMESTAMP)),
    (5, 'Robert', 'Brown', 'robert@example.com', 'hashedpassword', '444444444', 'en', 'VERIFTOKEN5', 'RESTOKEN5', true, DATEADD('DAY', 30, CURRENT_TIMESTAMP));

INSERT INTO Coverages (id, coverage_name)
VALUES (1, 'Coverage A'), (2, 'Coverage B');

INSERT INTO patients (patient_id, coverage_id)
VALUES (1, 1),
       (3, 2);

INSERT INTO Doctors (doctor_id, rating, rating_count, image_id)
VALUES (2, 4.5, 10, NULL),
       (4, 4.0, 5, NULL),
       (5, 3.0, 4, NULL);

INSERT INTO Doctor_Coverages (doctor_id, coverage_id)
VALUES (2, 1),
       (4, 2),
       (5, 1),
       (5, 2);

INSERT INTO Specialties (id, key)
VALUES (1, 'Cardiology'),
       (2, 'Neurology'),
       (3, 'Orthopedics'),
       (4, 'Pediatrics');

INSERT INTO Doctor_Specialties (doctor_id, specialty_id)
VALUES (2, 1),
       (4, 2),
       (4, 3),
       (5, 1);

INSERT INTO Doctor_Availability (doctor_id, day_of_week, start_time, end_time)
VALUES (2, 0, '09:00:00', '12:00:00'),
       (4, 0, '09:00:00', '12:00:00'),
       (4, 1, '09:00:00', '12:00:00'),
       (4, 2, '09:00:00', '12:00:00'),
       (4, 3, '09:00:00', '12:00:00'),
       (4, 4, '09:00:00', '12:00:00'),
       (4, 5, '09:00:00', '12:00:00'),
       (4, 6, '09:00:00', '12:00:00'),
       (5, 0, '09:00:00', '12:00:00'),
       (5, 1, '09:00:00', '12:00:00');

INSERT INTO Appointments (id, doctor_id, patient_id, specialty_id, date, status, reason)
VALUES (1, 2, 1, 1, '2025-04-29 10:00:00', 'confirmado', 'Consulta general'),
       (2, 2, 1, 1, '2025-04-30 10:00:00', 'confirmado', 'Consulta particluar'),
       (3, 4, 3, 2, '2025-04-30 10:00:00', 'confirmado', 'Consulta'),
       (4, 4, 3, 2, '2025-05-01 10:00:00', 'confirmado', 'Consulta'),
       (5, 4, 3, 2, '2025-05-02 10:00:00', 'confirmado', 'Consulta'),
       (6, 4, 3, 2, '2025-05-03 10:00:00', 'confirmado', 'Consulta'),
       (7, 4, 3, 2, '2025-05-04 10:00:00', 'confirmado', 'Consulta'),
       (8, 4, 3, 2, '2025-05-05 10:00:00', 'confirmado', 'Consulta'),
       (9, 4, 3, 2, '2025-05-06 10:00:00', 'confirmado', 'Consulta'),
       (10, 4, 3, 2, '2025-05-07 10:00:00', 'confirmado', 'Consulta');

INSERT INTO Images (id, image)
VALUES (1, X'0102030405');

INSERT INTO Ratings (id, doctor_id, patient_id, appointment_id, rating, comment)
VALUES (1, 2, 1, 1, 5, 'Excelente atención'),
       (2, 4, 3, 3, 5, 'Buena atención'),
       (3, 4, 3, 4, 4, 'Atención regular'),
       (4, 4, 3, 5, 3, 'Atención normal'),
       (5, 4, 3, 6, 2, 'Atención mala'),
       (6, 4, 3, 7, 2, 'Atención horrible'),
       (7, 4, 3, 8, 5, 'Excelente atención'),
       (8, 4, 3, 9, 4, 'Buena atención'),
       (9, 4, 3, 10, 2, 'Atención mediocre');

INSERT INTO appointment_files (id, appointment_id, uploader_role, file_name, file_data)
VALUES (1, 1, 'doctor', 'informe.pdf', X'0102030405');


ALTER SEQUENCE users_id_seq RESTART WITH 6;
ALTER SEQUENCE coverages_id_seq RESTART WITH 3;
ALTER SEQUENCE specialties_id_seq RESTART WITH 5;
ALTER SEQUENCE appointments_id_seq RESTART WITH 11;
ALTER SEQUENCE ratings_id_seq RESTART WITH 10;
ALTER SEQUENCE appointment_files_id_seq RESTART WITH 2;
ALTER SEQUENCE images_id_seq RESTART WITH 2;