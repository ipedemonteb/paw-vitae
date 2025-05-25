package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.AppointmentNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.AppointmentForm;
import ar.edu.itba.paw.webapp.paging.ParamCustomizer;
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

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final AppointmentFileService appointmentFileService;
    private final UnavailabilitySlotsService unavailabilitySlotsService;
    private final DoctorOfficeService doctorOfficeService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, DoctorService doctorService, AppointmentFileService appointmentFileService, UnavailabilitySlotsService unavailabilitySlotsService, DoctorOfficeService doctorOfficeService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.appointmentFileService = appointmentFileService;
        this.unavailabilitySlotsService = unavailabilitySlotsService;
        this.doctorOfficeService = doctorOfficeService;
    }


    @RequestMapping(value = "/appointment", method = RequestMethod.GET)
    public ModelAndView appointment(
            @ModelAttribute(value = "appointmentForm", binding = false) final AppointmentForm appointmentForm,
            @ParamCustomizer(paramName = "doctorId") QueryParam doctorId,
            @ModelAttribute("loggedUser") final Patient patient
    ) {
        ModelAndView mav = new ModelAndView("appointment/appointment");
        Doctor doctor = doctorService.getById(doctorId.getValue()).orElseThrow(UserNotFoundException::new);
        appointmentForm.setPatientId(patient.getId());
        appointmentForm.setDoctorId(doctor.getId());
        Map<LocalDate, List<Integer>> appointmentsByDate = appointmentService.getFutureAppointmentsByUserAndDate(doctor.getId());
        mav.addObject("doctor", doctor);
        mav.addObject("appointmentsByDate", appointmentsByDate);
        mav.addObject("unavailabilitySlots", unavailabilitySlotsService.getUnavailabilityByDoctorIdCurrentAndNextMonth(doctor.getId()));
        mav.addObject("doctorOffices", doctorOfficeService.getByDoctorId(doctor.getId()));
        return mav;
    }

    @RequestMapping(value = "/appointment", method = RequestMethod.POST)
    public ModelAndView appointment(
            @Valid @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm,
            final BindingResult errors,
            @ParamCustomizer(paramName = "doctorId") QueryParam doctorId,
            @ModelAttribute("loggedUser") final Patient patient
    ) {

        if (errors.hasErrors()) {
            return appointment(appointmentForm, doctorId, patient);
        }
        Appointment appointment = appointmentService.create(patient.getId(), doctorId.getValue(), appointmentForm.getAppointmentDate(), appointmentForm.getAppointmentHour(), appointmentForm.getReason(), appointmentForm.getSpecialtyId(), appointmentForm.getOfficeId());
        appointmentFileService.create(appointmentForm.getFiles(), "patient", appointment.getId());
        return new ModelAndView("redirect:/appointment/confirmation/" + appointment.getId());
    }

    @RequestMapping(value = "/appointment/confirmation/{id}", method = RequestMethod.GET)
    public ModelAndView appointmentConfirmation(
            @PathVariable("id") final long id
    ) {
        Appointment appointment = appointmentService.getById(id).orElseThrow(AppointmentNotFoundException::new);
        ModelAndView mav = new ModelAndView("appointment/confirmation");
        mav.addObject("patientFiles", appointmentFileService.getByAppointmentId(appointment.getId()));
        mav.addObject("appointment", appointment);
        mav.addObject("specialty", appointment.getSpecialty());
        mav.addObject("office", appointment.getDoctorOffice());
        return mav;
    }
}