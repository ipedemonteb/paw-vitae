INSERT INTO Users (id, name, last_name, email, password, phone, language, verification_token, reset_token, is_verified, token_expiration_date)
VALUES
    (1, 'John', 'Doe', 'john@example.com', 'hashedpassword', '123456789', 'en', 'VERIFTOKEN', 'RESTOKEN', true, DATEADD('DAY', 30, CURRENT_TIMESTAMP)),
    (2, 'Jane', 'Smith', 'jane@example.com', 'hashedpassword', '987654321', 'es', 'VERIFTOKEN2', 'RESTOKEN2', true, DATEADD('DAY', 30, CURRENT_TIMESTAMP)),
    (3, 'Michael', 'Johnson', 'michael@example.com', 'hashedpassword', '111111111', 'en', 'VERIFTOKEN3', 'RESTOKEN3', true, DATEADD('DAY', 30, CURRENT_TIMESTAMP)),
    (4, 'Emily', 'Davis', 'emily@example.com', 'hashedpassword', '333333333', 'es', 'VERIFTOKEN4', 'RESTOKEN4', true, DATEADD('DAY', 30, CURRENT_TIMESTAMP)),
    (5, 'Robert', 'Brown', 'robert@example.com', 'hashedpassword', '444444444', 'en', 'VERIFTOKEN5', 'RESTOKEN5', true, DATEADD('DAY', 30, CURRENT_TIMESTAMP));

INSERT INTO Coverages (id, coverage_name)
VALUES (1, 'Coverage A'), (2, 'Coverage B');

INSERT INTO neighborhoods (id, name)
VALUES (1, 'Recoleta'),
       (2, 'Belgrano');

INSERT INTO patients (patient_id, coverage_id, neighborhood_id)
VALUES (1, 1, 1),
       (3, 2, 2);

INSERT INTO Doctors (doctor_id, rating, rating_count, image_id)
VALUES (2, 4.5, 10, NULL),
       (4, 4.0, 5, NULL),
       (5, 3.0, 4, NULL);

INSERT INTO doctor_profile (doctor_id, bio, description)
VALUES (2, 'Cardiologo con mas de 10 años de experiencia', 'Especializado en cardiología y enfermedades del corazón'),
       (4, 'Neurólogo con enfoque en trastornos neurológicos', 'Experto en neurología y trastornos del sistema nervioso');

INSERT INTO doctor_offices (id, doctor_id, neighborhood_id, office_name, active, removed)
VALUES (1, 2, 1, 'Consultorio Recoleta', true, NULL),
       (2, 4, 2, 'Consultorio Belgrano', true, NULL),
       (3, 5, 1, 'Consultorio Palermo', true, NULL);

INSERT INTO Doctor_Coverages (doctor_id, coverage_id)
VALUES (2, 1),
       (4, 1),
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
       (4, 1),
       (4, 2),
       (4, 3),
       (5, 1);

INSERT INTO Doctor_Office_Specialties(office_id, specialty_id)
VALUES (1, 1),
       (2, 2),
       (2, 1);

INSERT INTO Doctor_Unavailability (id, doctor_id, start_date, end_date)
VALUES (1, 2, DATEADD('DAY', 60, CURRENT_TIMESTAMP), DATEADD('DAY', 65, CURRENT_TIMESTAMP)),
       (2, 4, DATEADD('DAY', 10, CURRENT_TIMESTAMP), DATEADD('DAY', 15, CURRENT_TIMESTAMP)),
       (3, 4, '2026-04-01', '2026-04-05');

INSERT INTO Appointments (id, doctor_id, patient_id, specialty_id, date, status, reason, office_id, allow_full_history, last_modified)
VALUES (1, 2, 1, 1, '2025-04-29 10:00:00', 'confirmado', 'Consulta general', 1, true, NOW()),
       (2, 2, 1, 1, '2025-04-30 10:00:00', 'confirmado', 'Consulta particluar', 1, true, NOW()),
       (3, 4, 3, 2, '2025-04-30 10:00:00', 'confirmado', 'Consulta', 2, true, NOW()),
       (4, 4, 3, 2, '2025-05-01 10:00:00', 'confirmado', 'Consulta', 2, true, NOW()),
       (5, 4, 3, 2, '2025-05-02 10:00:00', 'confirmado', 'Consulta', 2, true, NOW()),
       (6, 4, 3, 2, '2025-05-03 10:00:00', 'confirmado', 'Consulta', 2, true, NOW()),
       (7, 4, 3, 2, '2025-05-04 10:00:00', 'confirmado', 'Consulta', 2, true, NOW()),
       (8, 4, 3, 2, '2025-05-05 10:00:00', 'confirmado', 'Consulta', 2, true, NOW()),
       (9, 4, 3, 2, '2025-05-06 10:00:00', 'confirmado', 'Consulta', 2, true, NOW()),
       (10, 4, 3, 2, TIMESTAMPADD(SQL_TSI_DAY, -2, CURRENT_DATE) + INTERVAL '10' HOUR, 'confirmado', 'Consulta', 2, true, NOW());

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
VALUES (1, 1, 'doctor', 'informe.pdf', X'0102030405'),
       (2, 2, 'doctor', 'informe2.pdf', X'010203040506');

INSERT INTO doctor_experience (id, doctor_id, position_title, organization_name, start_date, end_date, description)
VALUES (1, 2, 'Residente', 'Hospital Aleman', '2010-01-01', '2016-01-01', 'Residencia en especialidad'),
       (2, 4, 'Cirujano', 'Hospital Italiano', '2015-01-01', '2020-01-01', 'Cirugía general');

INSERT INTO doctor_certifications (id, doctor_id, certificate_name, issuing_entity, issue_date)
VALUES (1, 2, 'Certificación en Cardiología', 'Sociedad Argentina de Cardiología', '2016-01-01'),
       (2, 4, 'Certificación en Neurología', 'Sociedad Argentina de Neurología', '2017-01-01');

INSERT INTO doctor_office_availability_slots (id, office_id, day_of_week, start_time, end_time)
VALUES (1, 1, 0, '09:00:00', '12:00:00'),
       (2, 1, 1, '09:00:00', '12:00:00'),
       (3, 2, 1, '09:00:00', '12:00:00');
       --(3, 2, 0, '09:00:00', '12:00:00'),
       --(4, 2, 1, '09:00:00', '12:00:00');

INSERT INTO doctor_availability_slots (id, doctor_id, slot_date, start_time, status)
VALUES (1, 2, '2026-03-02', '09:00:00', 'AVAILABLE'),
       (2, 2, '2026-03-02', '10:00:00', 'AVAILABLE'),
       (3, 2, '2026-03-02', '11:00:00', 'AVAILABLE'),
       (4, 2, '2026-03-03', '09:00:00', 'AVAILABLE'),
       (5, 2, '2026-03-03', '10:00:00', 'AVAILABLE'),
       (6, 2, '2026-03-03', '11:00:00', 'AVAILABLE'),
       (7, 4, '2026-03-03', '09:00:00', 'UNAVAILABLE'),
       (8, 4, '2026-03-03', '10:00:00', 'UNAVAILABLE'),
       (9, 4, '2026-03-03', '11:00:00', 'UNAVAILABLE');

ALTER SEQUENCE users_id_seq RESTART WITH 6;
ALTER SEQUENCE coverages_id_seq RESTART WITH 3;
ALTER SEQUENCE specialties_id_seq RESTART WITH 5;
ALTER SEQUENCE doctor_unavailability_id_seq RESTART WITH 4;
ALTER SEQUENCE appointments_id_seq RESTART WITH 11;
ALTER SEQUENCE ratings_id_seq RESTART WITH 10;
ALTER SEQUENCE appointment_files_id_seq RESTART WITH 3;
ALTER SEQUENCE images_id_seq RESTART WITH 2;
ALTER SEQUENCE neighborhoods_id_seq RESTART WITH 3;
ALTER SEQUENCE doctor_offices_id_seq RESTART WITH 4;
ALTER SEQUENCE doctor_experience_id_seq RESTART WITH 3;
ALTER SEQUENCE doctor_certification_id_seq RESTART WITH 3;
ALTER SEQUENCE doctor_office_availability_slots_id_seq RESTART WITH 4;
ALTER SEQUENCE doctor_availability_slots_id_seq RESTART WITH 10;
