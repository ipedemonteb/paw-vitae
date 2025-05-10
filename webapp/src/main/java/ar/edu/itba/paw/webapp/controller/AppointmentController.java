package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.AppointmentNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.AppointmentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class AppointmentController {

    private AppointmentService as;
    private DoctorService ds;
    private AppointmentFileService afs;

    @Autowired
    public AppointmentController(AppointmentService as, DoctorService ds, AppointmentFileService afs) {
        this.as = as;
        this.ds = ds;
        this.afs = afs;
    }


    @RequestMapping(value = "/appointment", method = RequestMethod.GET)
    public ModelAndView appointment(
            @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm,
            @RequestParam(required = true) Long doctorId,
            @ModelAttribute("loggedUser") final Patient patient
    ) {
        appointmentForm.setPatientId(patient.getId());
        appointmentForm.setDoctorId(doctorId);
        ModelAndView mav = new ModelAndView("appointment/appointment");
        Doctor doctor = ds.getById(doctorId).orElseThrow(IllegalArgumentException::new);
        Map<LocalDate, List<Integer>> appointmentsByDate = as.getFutureAppointmentsByUserAndDate(doctorId);
        mav.addObject("doctor", doctor);
        mav.addObject("appointmentsByDate", appointmentsByDate);
        return mav;
    }

    @RequestMapping(value = "/appointment", method = RequestMethod.POST)
    public ModelAndView appointment(
            @Valid @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm,
            final BindingResult errors,
            @RequestParam() Long doctorId,
            @ModelAttribute("loggedUser") final Patient patient
    ) {

        if (errors.hasErrors()) {
            return appointment(appointmentForm, doctorId, patient);
        }

        Appointment appointment = as.create(patient.getId(), doctorId, appointmentForm.getAppointmentDate(), appointmentForm.getAppointmentHour(), appointmentForm.getReason(), appointmentForm.getSpecialtyId());
        afs.create(appointmentForm.getFiles(), "patient", appointment.getId());

        return new ModelAndView("redirect:/appointment/confirmation/" + appointment.getId());
    }

    @RequestMapping(value = "/appointment/confirmation/{id}", method = RequestMethod.GET)
    public ModelAndView appointmentConfirmation(
            @PathVariable("id") final long id
    ) {
        Appointment appointment = as.getById(id).orElseThrow(AppointmentNotFoundException::new);
        ModelAndView mav = new ModelAndView("appointment/confirmation");
        mav.addObject("patientFiles", afs.getByAppointmentId(appointment.getId()));
        mav.addObject("appointment", appointment);
        mav.addObject("specialty", appointment.getSpecialty());
        return mav;
    }
}