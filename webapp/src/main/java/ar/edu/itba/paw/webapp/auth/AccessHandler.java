package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.exception.AppointmentNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component("accessHandler")
public class AccessHandler {

    private final AppointmentService appointmentService;
    private final UserService userService;

    @Autowired
    public AccessHandler(AppointmentService appointmentService, UserService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    /**
     * @return true if the currently authenticated user is
     * either the patient or the doctor on that appointment
     */

    public boolean canAccessDoctorProfile(Authentication auth, Long doctorId) {
        Object principal = auth.getPrincipal();
        if (!(principal instanceof AuthUserDetails)) {
            return false;
        }

        long userId = userService.getByEmail(((AuthUserDetails) principal).getUsername()).orElseThrow(UserNotFoundException::new).getId();
        return userId == doctorId;
    }

    public boolean canSeeMedicalHistory(Authentication auth, long appointmentId) {
        Object principal = auth.getPrincipal();
        if (!(principal instanceof AuthUserDetails)) {
            return false;
        }

        long doctorId = userService.getByEmail(((AuthUserDetails) principal).getUsername()).orElseThrow(UserNotFoundException::new).getId();
        return appointmentService.hasHistoryAllowedByAppointmentId(appointmentId, doctorId);
    }

    public boolean canHandleAppointment(Authentication auth, String appointmentId) {
        Object principal = auth.getPrincipal();
        if (!(principal instanceof AuthUserDetails)) {
            return false;
        }
        long appointmentIdLong;
        try {
            appointmentIdLong = Long.parseLong(appointmentId);
        } catch (NumberFormatException e) {
            return true;
        }

        long userId = userService.getByEmail(((AuthUserDetails) principal).getUsername()).orElseThrow(UserNotFoundException::new).getId();
        return appointmentService.getById(appointmentIdLong)
                .map(a -> a.getPatient().getId() == userId
                        || a.getDoctor().getId() == userId)
                .orElse(true);
    }
     public boolean canHandleUnavailability( HttpServletRequest request) {
         String requestedWith = request.getHeader("X-Requested-With");
         return "XMLHttpRequest".equalsIgnoreCase(requestedWith);
    }
    public boolean hasEnabledFullMedicalHistory(Authentication auth, long appointmentId) {
        Object principal = auth.getPrincipal();
        if (!(principal instanceof AuthUserDetails)) {
            return false;
        }
        Appointment appointment = appointmentService.getById(appointmentId).orElseThrow(AppointmentNotFoundException::new);
        if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"))){
            long doctorId = userService.getByEmail(((AuthUserDetails) principal).getUsername()).orElseThrow(UserNotFoundException::new).getId();
            long patientId = appointment.getPatient().getId();
            return appointmentService.hasFullMedicalHistoryEnabled(patientId, doctorId) || appointment.getDoctor().getId() == doctorId;
        } else {
            long patientId = userService.getByEmail(((AuthUserDetails) principal).getUsername()).orElseThrow(UserNotFoundException::new).getId();
            return appointment.getPatient().getId() == patientId;
        }
    }
}

