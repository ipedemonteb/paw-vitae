package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.models.Appointment;
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

    @Autowired
    public AccessHandler(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
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

    public boolean canSeeMedicalHistory(Authentication auth, long appointmentId) {
        AuthUserDetails user = getPrincipal(auth);
        if (user == null) {
            return false;
        }
        return appointmentService.hasHistoryAllowedByAppointmentId(appointmentId, user.getUserId());
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

    public boolean isUser(Authentication auth, HttpServletRequest request) {
        AuthUserDetails user = getPrincipal(auth);
        if (user == null) {
            return false;
        }
        String userIdParam = request.getParameter("userId");
        if (userIdParam == null || userIdParam.isBlank()) {
            return true;
        }
        try {
            long userId = Long.parseLong(userIdParam);
            return userId == user.getUserId();
        } catch (NumberFormatException e) {
            return true;
        }
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
}
