package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.PatientService;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.AppointmentNotFoundException;
import ar.edu.itba.paw.webapp.form.PatientRatingForm;
import ar.edu.itba.paw.webapp.form.UpdatePatientForm;
import ar.edu.itba.paw.webapp.paging.ParamCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class PatientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientController.class);

    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final CoverageService coverageService;
    private final AppointmentFileService appointmentFileService;
    private final RatingService ratingService;

    @Autowired
    public PatientController(PatientService patientService, AppointmentService appointmentService, CoverageService coverageService, AppointmentFileService appointmentFileService, RatingService ratingService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.coverageService = coverageService;
        this.appointmentFileService = appointmentFileService;
        this.ratingService = ratingService;
    }

    @RequestMapping(value = "/patient/dashboard")
    public ModelAndView getPatientDashboard() {
        return new ModelAndView("redirect:/patient/dashboard/upcoming");
    }

    @RequestMapping(value = "/patient/dashboard/upcoming")
    public ModelAndView getUpcomingAppointments(@ParamCustomizer(defaultValue = 1) QueryParam page,
                                                @RequestParam(defaultValue = "all", required = false) String dateRange,
                                                @ModelAttribute("loggedUser") final Patient patient
    ) {
        final ModelAndView mav = new ModelAndView("patient/dashboard-upcoming");
        mav.addObject("patient", patient);
        Page<Appointment> appointmentsPage = appointmentService.getAppointments(patient.getId(), true, (int) page.getValue(), 10, dateRange);
        mav.addObject("upcomingAppointments", appointmentsPage.getContent());
        mav.addObject("currentPage", page.getValue());
        mav.addObject("totalPages", appointmentsPage.getTotalPages());
        LOGGER.debug("Loading dashboard and upcoming appointments for patient ID: {}", patient.getId());
        return mav;
    }

    @RequestMapping(value = "/patient/dashboard/history")
    public ModelAndView getAppointmentHistory(@ParamCustomizer(defaultValue = 1) QueryParam page,
                                              @RequestParam(defaultValue = "all", required = false) String status,
                                              @ModelAttribute("loggedUser") final Patient patient
    ) {
        final ModelAndView mav = new ModelAndView("patient/dashboard-history");
        mav.addObject("patient", patient);
        Page<Appointment> appointmentsPage = appointmentService.getAppointments(patient.getId(), false, (int) page.getValue(), 10, status);
        mav.addObject("pastAppointments", appointmentsPage.getContent());
        mav.addObject("currentPage", page.getValue());
        mav.addObject("totalPages", appointmentsPage.getTotalPages());
        return mav;
    }

    @RequestMapping(value = "/patient/dashboard/profile")
    public ModelAndView getProfile(@ModelAttribute("updatePatientForm") final UpdatePatientForm updatePatientForm,
                                   @ModelAttribute("loggedUser") final Patient patient
    ) {
        final ModelAndView mav = new ModelAndView("patient/dashboard-profile");
        mav.addObject("patient", patient);
        mav.addObject("coverageList", coverageService.getAll());
        mav.addObject("display", "none");
        return mav;
    }

    @RequestMapping(value = "/patient/dashboard/update", method = RequestMethod.POST)
    public ModelAndView updatePatient(@Valid @ModelAttribute("updatePatientForm") final UpdatePatientForm updatePatientForm,
                                      final BindingResult errors,
                                      @ModelAttribute("loggedUser") final Patient patient
    ) {
        if (errors.hasErrors()) {
            ModelAndView mav = getProfile(updatePatientForm, patient);
            mav.addObject("display", "block");
            return mav;
        }
        patientService.updatePatient(patient, updatePatientForm.getName(), updatePatientForm.getLastName(), updatePatientForm.getPhone(), updatePatientForm.getCoverage());
        return new ModelAndView("redirect:/patient/dashboard/profile?updated=true");
    }

    @PostMapping(value = "/patient/dashboard/appointment/cancel", produces = "application/json")
    @ResponseBody
    public ModelAndView cancelAppointment(
            @RequestParam("appointmentId") Long appointmentId,
            @ModelAttribute("loggedUser") final User user
    ) {
        boolean result = appointmentService.cancelAppointment(appointmentId, user.getId());
        String value = String.valueOf(result);
        return new ModelAndView("redirect:/patient/dashboard/upcoming?cancelled=" + value);
    }

    @RequestMapping(value = "patient/dashboard/appointment-details/{id}", method = RequestMethod.GET)
    public ModelAndView patientAppointmentDetails(
            @ModelAttribute("patientRatingForm") final PatientRatingForm patientRatingForm,
            @PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("patient/appointment-details");
        Appointment appointment = appointmentService.getById(id).orElseThrow(AppointmentNotFoundException::new);
        Optional<Rating> existingRating = ratingService.getRatingByAppointmentId(appointment.getId());
        mav.addObject("appointment", appointment);
        mav.addObject("patientFiles", appointmentFileService.getByAppointmentId(appointment.getId()));
        mav.addObject("doctorFiles", appointmentFileService.getByAppointmentId(appointment.getId()));
        mav.addObject("existingRating", existingRating.orElse(null));
        mav.addObject("isCancelled", appointment.getStatus().equals(AppointmentStatus.CANCELADO.getValue()));
        return mav;
    }

    @PostMapping("/patient/dashboard/appointment/rate")
    public ModelAndView submitRating(@Valid @ModelAttribute("patientRatingForm") final PatientRatingForm form,
                                     BindingResult errors,
                                     @ModelAttribute("loggedUser") final Patient patient
    ) {
        if (errors.hasErrors()) {
            return patientAppointmentDetails(form, form.getAppointmentId());
        }
        ratingService.create(
                form.getRating(),
                form.getDoctorId(),
                patient.getId(),
                form.getAppointmentId(),
                form.getComment()
        );
        return new ModelAndView("redirect:/patient/dashboard/appointment-details/" + form.getAppointmentId() + "?rated=true");
    }
}