package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("accessHandler")
public class AccessHandler {

    private final AppointmentService apptSvc;
    private final UserService userSvc;

    @Autowired
    public AccessHandler(AppointmentService apptSvc, UserService userSvc) {
        this.apptSvc = apptSvc;
        this.userSvc = userSvc;
    }

    /**
     * @return true if the currently authenticated user is
     *         either the client or the doctor on that appointment
     */
    public boolean canViewAppointment(Authentication auth, Long appointmentId) {
        Object principal = auth.getPrincipal();
        if(!(principal instanceof AuthUserDetails)) {
            return false;
        }
        long userId = userSvc.getByEmail(((AuthUserDetails)principal).getUsername()).orElseThrow(UserNotFoundException::new).getId();
        return apptSvc.getById(appointmentId)
                .map(a -> a.getPatient().getId() == userId
                        || a.getDoctor().getId() == userId)
                .orElse(false);
    }

    /**
     * @return true if the current user is a partaker of
     * the appointment, thus can cancel it
     */

    public boolean canCancelAppointment(Authentication auth, Long appointmentId) {
        Object principal = auth.getPrincipal();
        if (!(principal instanceof AuthUserDetails)) {
            return false;
        }
        long userId = userSvc.getByEmail(((AuthUserDetails) principal).getUsername()).orElseThrow(UserNotFoundException::new).getId();
        return apptSvc.getById(appointmentId)
                .map(a -> a.getPatient().getId() == userId
                        || a.getDoctor().getId() == userId)
                .orElse(false);
    }

}

