package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentFileDao;
import ar.edu.itba.paw.interfaceServices.AppointmentFileService;
import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentFile;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    //@TODO: Fix
    @Transactional
    @Override
    public List<AppointmentFile> create(MultipartFile[] files, String uploader_role, long appointment_id) {
        LOGGER.debug("Creating appointment files: {} for appointment with id {}", files, appointment_id);
        if (files.length == 0) {
            return null;
        }
        List<AppointmentFile> appointmentFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }
            try {
                //appointmentFiles.add(appointmentFileDao.create(file.getOriginalFilename(), file.getBytes(), uploader_role, appointment_id));
                appointmentFiles.add(appointmentFileDao.create(file.getOriginalFilename(), file.getBytes(), uploader_role, new Appointment()));

            } catch (IOException e) {
                LOGGER.error("Error while adding files {} ", files, e);
            }
        }
        if(uploader_role.equals("doctor") && !appointmentFiles.isEmpty()) {
            Appointment appointment = appointmentService.getById(appointment_id).orElseThrow(IllegalArgumentException:: new);
            Doctor doctor = appointment.getDoctor();
            Patient patient = appointment.getPatient();
            mailService.sendFileUploadMail(doctor, patient, appointment, appointmentFiles);
        }
        LOGGER.info("Appointment files created successfully for appointment with id {}", appointment_id);
        return appointmentFiles;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<AppointmentFile> getById(long id) {
        return appointmentFileDao.getById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AppointmentFile> getByAppointmentId(long appointment_id) {
        return appointmentFileDao.getByAppointmentId(appointment_id);
    }

    //@TODO: Fix
    @Transactional(readOnly = true)
    public Optional<AppointmentFile> getAuthorizedFile(long fileId, long appointmentId, String username) {
        LOGGER.debug("Getting authorized file {} for appointment {} and user {}", fileId, appointmentId, username);
        Optional<AppointmentFile> fileOpt = appointmentFileDao.getById(fileId);
        if (fileOpt.isEmpty()) return Optional.empty();

        AppointmentFile file = fileOpt.get();
        //if (file.getAppointment_id() != appointmentId) return Optional.empty();

        Optional<Appointment> appointmentOpt = appointmentService.getById(appointmentId);
        if (appointmentOpt.isEmpty()) return Optional.empty();

        Appointment appointment = appointmentOpt.get();
        if (!appointment.getDoctor().getEmail().equals(username) &&
                !appointment.getPatient().getEmail().equals(username)) {
            return Optional.empty();
        }
        LOGGER.info("File {} authorized for appointment {} and user {}", file, appointment, username);
        return Optional.of(file);
    }

}
