package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.DoctorOfficeService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.exception.AppointmentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@Component("accessHandler")
public class AccessHandler {

    private final AppointmentService appointmentService;
    private final DoctorOfficeService doctorOfficeService;

    @Autowired
    public AccessHandler(AppointmentService appointmentService, DoctorOfficeService doctorOfficeService) {
        this.appointmentService = appointmentService;
        this.doctorOfficeService = doctorOfficeService;
    }

    /**
     * Helper privado para obtener el usuario casteado de forma segura.
     * Evita repetir el instanceof y el cast en cada método.
     */
    private AuthUserDetails getPrincipal(Authentication auth) {
        if (auth != null && auth.getPrincipal() instanceof AuthUserDetails) {
            return (AuthUserDetails) auth.getPrincipal();
        }
        return null;
    }

    public boolean canAccessDoctorProfile(Authentication auth, Long doctorId) {
        AuthUserDetails user = getPrincipal(auth);
        if (user == null) {
            return false;
        }
        return user.getUserId() == doctorId;
    }

    public boolean canHandleAppointment(Authentication auth, String appointmentIdStr) {
        AuthUserDetails user = getPrincipal(auth);
        if (user == null) {
            return false;
        }

        long appointmentId;
        try {
            appointmentId = Long.parseLong(appointmentIdStr);
        } catch (NumberFormatException e) {
            return true;
        }

        return appointmentService.getById(appointmentId)
                .map(a -> a.getPatient().getId() == user.getUserId()
                        || a.getDoctor().getId() == user.getUserId())
                .orElse(true);
    }

    public boolean isUserQuery(Authentication auth, HttpServletRequest request) {
        String userIdParam = request.getParameter("userId");
        return isUser(auth, userIdParam);
    }

    public boolean isUserDoctorQuery(Authentication auth, HttpServletRequest request) {
        String doctorIdParam = request.getParameter("doctorId");
        return isUser(auth, doctorIdParam);
    }

    public boolean isUser(Authentication auth, String id) {
        AuthUserDetails user = getPrincipal(auth);
        if (user == null) {
            return false;
        }
        if (id == null || id.isBlank()) {
            return true;
        }
        try {
            long userId = Long.parseLong(id);
            return userId == user.getUserId();
        } catch (NumberFormatException e) {
            return true;
        }
    }

    public boolean hasOfficeOwnership(Authentication auth, long officeId) {
        AuthUserDetails user = getPrincipal(auth);
        if (user == null) {
            return false;
        }
        DoctorOffice office = doctorOfficeService.getById(officeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return office.getDoctor().getId() == user.getUserId();
    }

    public boolean canHandleUnavailability(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equalsIgnoreCase(requestedWith);
    }

    public boolean hasEnabledFullMedicalHistory(Authentication auth, long appointmentId) {
        AuthUserDetails user = getPrincipal(auth);
        if (user == null) {
            return false;
        }

        Appointment appointment = appointmentService.getById(appointmentId)
                .orElseThrow(AppointmentNotFoundException::new);

        boolean isDoctor = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DOCTOR"));

        if (isDoctor) {
            long patientId = appointment.getPatient().getId();
            return appointmentService.hasFullMedicalHistoryEnabled(patientId, user.getUserId())
                    || appointment.getDoctor().getId() == user.getUserId();
        } else {
            return appointment.getPatient().getId() == user.getUserId();
        }
    }

    public boolean canSeeHistory(Authentication auth, HttpServletRequest request) {

        isUserDoctorQuery(auth, request);

        String patient = request.getParameter("userId");
        String doctor = request.getParameter("doctorId");

        if (patient == null) {
            return false;
        }

        return appointmentService.hasFullMedicalHistoryEnabled(Long.parseLong(patient), Long.parseLong(doctor));

    }

    public boolean canSeeMedicalHistoryApp(Authentication auth, String appointmentIdStr) {
        AuthUserDetails user = getPrincipal(auth);
        if (user == null) {
            return false;
        }
        long appointmentId = Long.parseLong(appointmentIdStr);
        Appointment appointment = appointmentService.getById(appointmentId)
                .orElseThrow(AppointmentNotFoundException::new);
        return appointmentService.hasHistoryAllowedByAppointmentId(appointmentId, user.getUserId()) || appointmentService.hasFullMedicalHistoryEnabled(appointment.getPatient().getId(), user.getUserId());
    }

    public boolean hasAppointmentWithPatient(Authentication auth, String patientId) {
        AuthUserDetails user = getPrincipal(auth);
        if (user == null) {
            return false;
        }
        long doctorId = user.getUserId();
        return appointmentService.hasAppointmentWithPatient(doctorId, Long.parseLong(patientId));
    }

    public boolean canRateAppointment(Authentication auth, HttpServletRequest request) {
        AuthUserDetails user = getPrincipal(auth);
        if (user == null) {
            return false;
        }
        String appointmentId = request.getParameter("appointmentId");
        if (appointmentId == null) {
            return false;
        }

        return canHandleAppointment(auth, appointmentId);
    }
}
