package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.PatientService;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.PatientRatingForm;
import ar.edu.itba.paw.webapp.form.UpdatePatientForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class PatientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientController.class);

    private final PatientService ps;
    private final AppointmentService as;
    private final CoverageService covs;
    private final AppointmentFileService afs;
    private final RatingService rs;
    @Autowired
    public PatientController(PatientService ps, AppointmentService as, CoverageService covs, AppointmentFileService afs, RatingService rs) {

        this.ps = ps;
        this.as = as;
        this.covs = covs;
        this.afs = afs;
        this.rs = rs;
    }

    @RequestMapping(value = "/patient/dashboard")
    public ModelAndView getPatientDashboard() {
        return new ModelAndView("redirect:/patient/dashboard/upcoming");
    }

    @RequestMapping(value = "/patient/dashboard/upcoming")
    public ModelAndView getUpcomingAppointments(@RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "all", required = false) String dateRange){

        final ModelAndView mav = new ModelAndView("patient/dashboard-upcoming");
        Patient patient = loggedUser();
        mav.addObject("patient", patient);
        var appointmentsPage = as.getFuturePatientAppointments(patient.getId(), page, 5,dateRange,null);
        mav.addObject("upcomingAppointments", appointmentsPage.getContent());
        mav.addObject("activeTab", "upcoming");
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", appointmentsPage.getTotalPages());
        mav.addObject("hasMore", page < appointmentsPage.getTotalPages());
        LOGGER.debug("Loading dashboard and upcoming appointments for patient ID: {}", patient.getId());
        return mav;
    }

    @RequestMapping(value = "/patient/dashboard/history")
    public ModelAndView getAppointmentHistory(@RequestParam(value = "page", defaultValue = "1") int page,
                                              @RequestParam( defaultValue = "all",required = false) String status){
        final ModelAndView mav = new ModelAndView("patient/dashboard-history");
        Patient patient = loggedUser();
        mav.addObject("patient", patient);
        var appointmentsPage = as.getPastPatientAppointments(patient.getId(), page, 5, null, status);
        mav.addObject("pastAppointments", appointmentsPage.getContent());
        System.out.println("acaaaa" + appointmentsPage.getContent());
        mav.addObject("activeTab", "history");
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", appointmentsPage.getTotalPages());
        mav.addObject("hasMore", page < appointmentsPage.getTotalPages());
        return mav;
    }

    @RequestMapping(value = "/patient/dashboard/profile")
    public ModelAndView getProfile(@ModelAttribute("updatePatientForm") final UpdatePatientForm updatePatientForm) {
        final ModelAndView mav = new ModelAndView("patient/dashboard-profile");
        Patient patient = loggedUser();
        updatePatientForm.setForm(patient);
        mav.addObject("patient", patient);
        mav.addObject("coverageList", covs.getAll());
        mav.addObject("activeTab", "profile");
        mav.addObject("display", "none");
        return mav;
    }

    @RequestMapping(value = "/patient/dashboard/update", method = RequestMethod.POST)
    public ModelAndView updatePatient(@Valid @ModelAttribute("updatePatientForm") final UpdatePatientForm updatePatientForm,
                                      final BindingResult errors) {
        if (errors.hasErrors()) {
            ModelAndView mav = getProfile(updatePatientForm);
            mav.addObject("display", "block");
            return mav;
        }
        Patient patient = loggedUser();
        ps.updatePatient(patient, updatePatientForm.getName(), updatePatientForm.getLastName(), updatePatientForm.getPhone(), covs.findById(Long.parseLong(updatePatientForm.getCoverage())).orElse(null));
        return new ModelAndView("redirect:/patient/dashboard/profile?updated=true");
    }

    @ModelAttribute
    public Patient loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ps.getByEmail((String) auth.getName()).orElseThrow(UserNotFoundException::new);
    }

    @PostMapping(value = "/patient/dashboard/appointment/cancel", produces = "application/json")
    @ResponseBody
    public String cancelAppointment(@RequestParam("appointmentId") Long appointmentId) {
        boolean result = as.cancelAppointment(appointmentId, loggedUser().getId());
        return "{\"success\": " + result + "}";
    }

    @RequestMapping(value = "patient/dashboard/appointment-details/{id}", method = RequestMethod.GET)
    public ModelAndView patientAppointmentDetails(
            @ModelAttribute("patientRatingForm") final PatientRatingForm patientRatingForm,
            @PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("patient/appointment-details");
        Appointment appointment = as.getById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid appointment Id:" + id));

        // Check if this appointment already has a rating
        Optional<Rating> existingRating = rs.getRatingByAppointmentId(appointment.getId());

        mav.addObject("appointment", appointment);
        mav.addObject("patientFiles", afs.getByAppointmentId(appointment.getId()));
        mav.addObject("existingRating", existingRating.orElse(null));

        // Only create a new form if there's no existing rating
        if (!existingRating.isPresent()) {
            patientRatingForm.setAppointmentId(id);
            mav.addObject("patientRatingForm", patientRatingForm);
        }

        return mav;
    }

    @PostMapping("/patient/dashboard/appointment/rate")
    public ModelAndView submitRating(@Valid @ModelAttribute("patientRatingForm") final PatientRatingForm form,
                                     BindingResult errors) {
        if (errors.hasErrors()) {
            return patientAppointmentDetails(form, form.getAppointmentId());
        }

        Patient patient = loggedUser();
        Appointment appointment = as.getById(form.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment Id:" + form.getAppointmentId()));

        // Check if rating already exists
        Optional<Rating> existingRating = rs.getRatingByAppointmentId(appointment.getId());
        if (existingRating.isPresent()) {
            // Rating already exists, redirect back
            return new ModelAndView("redirect:/patient/dashboard/appointment-details/" + form.getAppointmentId());
        }

        rs.create(
                form.getRating(),
                appointment.getDoctor().getId(),
                patient.getId(),
                appointment.getId(),
                form.getComment(),
                System.currentTimeMillis() // or a custom ID generator
        );

        return new ModelAndView("redirect:/patient/dashboard/appointment-details/" + form.getAppointmentId() + "?rated=true");
    }
}