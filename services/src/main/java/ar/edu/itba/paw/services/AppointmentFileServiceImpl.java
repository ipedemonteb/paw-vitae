package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentFileDao;
import ar.edu.itba.paw.interfaceServices.AppointmentFileService;
import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.AppointmentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentFileServiceImpl implements AppointmentFileService {

    private final AppointmentFileDao appointmentFileDao;
    private final AppointmentService appointmentService;
    private final MailService mailService;
    private final static Logger LOGGER = LoggerFactory.getLogger(AppointmentFileServiceImpl.class);

    @Autowired
    public AppointmentFileServiceImpl(final AppointmentFileDao appointmentFileDao, final AppointmentService appointmentService, MailService mailService) {
        this.appointmentFileDao = appointmentFileDao;
        this.appointmentService = appointmentService;
        this.mailService = mailService;
    }


    @Transactional
    @Override
    public AppointmentFile create(MultipartFile file, String uploader_role, long appointment_id) {
        LOGGER.debug("Creating appointment file for appointment with id {}", appointment_id);
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Empty file payload");
        }

        Appointment appointment = appointmentService.getById(appointment_id).orElseThrow(AppointmentNotFoundException::new);
        try {
            AppointmentFile createdFile = appointmentFileDao.create(file.getOriginalFilename(), file.getBytes(), uploader_role, appointment);
            if ("doctor".equals(uploader_role)) {
                MailDTO dto = new MailDTO(appointment);
                mailService.sendFileUploadMail(dto, List.of(createdFile));
            }
            appointment.setLastModified(LocalDateTime.now());
            LOGGER.info("Appointment file created successfully for appointment with id {}", appointment_id);
            return createdFile;
        } catch (IOException e) {
            LOGGER.error("Error while adding file {}", file.getOriginalFilename(), e);
            throw new RuntimeException("Failed to persist uploaded file", e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<AppointmentFile> getByAppointmentId(long appointment_id) {
        return appointmentFileDao.getByAppointmentId(appointment_id);
    }

    @Transactional(readOnly = true)
    public Optional<AppointmentFile> getAuthorizedFile(long fileId, long appointmentId, String username) {
        LOGGER.debug("Getting authorized file {} for appointment {} and user {}", fileId, appointmentId, username);
        Optional<AppointmentFile> fileOpt = appointmentFileDao.getById(fileId);
        if (fileOpt.isEmpty()) return Optional.empty();

        AppointmentFile file = fileOpt.get();
        if (file.getAppointment().getId() != appointmentId) return Optional.empty();

        Optional<Appointment> appointmentOpt = appointmentService.getById(appointmentId);
        if (appointmentOpt.isEmpty()) return Optional.empty();

        Appointment appointment = appointmentOpt.get();
        LOGGER.info("File {} authorized for appointment {} and user {}", file, appointment, username);
        return Optional.of(file);
    }

    @Transactional(readOnly = true)
    public Page<Map.Entry<Appointment, List<AppointmentFile>>> getGroupedFilesForPatient(long patientId, int page, int pageSize, String direction) {
        Page<Appointment> appointmentPage = appointmentService.getAppointmentsForPatientWithFilesOrReport(patientId, page, pageSize, direction);
        List<Appointment> appointments = appointmentPage.getContent();

        if (appointments.isEmpty()) {
            return new Page<>(Collections.emptyList(), page, pageSize, 0);
        }

        List<Long> appointmentIds = appointments.stream().map(Appointment::getId).collect(Collectors.toList());
        List<AppointmentFile> files = appointmentFileDao.getFilesByAppointmentIds(appointmentIds);

        Map<Long, List<AppointmentFile>> filesByAppointmentId = files.stream()
                .collect(Collectors.groupingBy(f -> f.getAppointment().getId()));

        List<Map.Entry<Appointment, List<AppointmentFile>>> groupedList = appointments.stream()
                .map(appt -> Map.entry(appt, filesByAppointmentId.getOrDefault(appt.getId(), Collections.emptyList())))
                .collect(Collectors.toList());

        return new Page<>(groupedList, page, pageSize, appointmentPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public List<AppointmentFile> getByAppointmentIdForDoctor(long appointmentId) {
        return appointmentFileDao.getByAppointmentIdForDoctor( appointmentId);
    }
}
