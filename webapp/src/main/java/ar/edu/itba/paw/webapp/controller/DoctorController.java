package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class DoctorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorController.class);

    private final DoctorService ds;
    private final AppointmentService as;
    private final CoverageService cs;
    private final SpecialtyService ss;
    private final AppointmentFileService afs;
    private final RatingService rs;
    @Autowired
    public DoctorController(DoctorService ds, AppointmentService as, CoverageService cs, SpecialtyService ss, AppointmentFileService afs, RatingService rs) {
        this.ds = ds;
        this.as = as;
        this.cs = cs;
        this.ss = ss;
        this.afs = afs;
        this.rs = rs;
    }
    @RequestMapping(value = "/doctor/dashboard")
    public ModelAndView getDoctorDashboard() {
        return new ModelAndView("redirect:/doctor/dashboard/upcoming");
    }

    @RequestMapping(value = "/doctor/dashboard/upcoming",method = RequestMethod.GET)
    public ModelAndView getUpcomingAppointments(@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "all", required = false) String dateRange){
        final ModelAndView mav = new ModelAndView("doctor/dashboard-upcoming");
        Doctor doctor = loggedUser();
        mav.addObject("doctor", doctor);
        Page<Appointment> appointmentsPage = as.getFutureDoctorAppointments(doctor.getId(), page, 5,dateRange,null);
        mav.addObject("upcomingAppointments", appointmentsPage.getContent());
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", appointmentsPage.getTotalPages());
        mav.addObject("hasMore", page < appointmentsPage.getTotalPages());
        mav.addObject("activeTab", "upcoming");
        LOGGER.debug("Loading dashboard and upcoming appointments for doctor ID: {}", doctor.getId());
        return mav;
    }



    @RequestMapping(value = "/doctor/dashboard/history")
    public ModelAndView getAppointmentHistory(@RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "all", required = false) String status){
        final ModelAndView mav = new ModelAndView("doctor/dashboard-history");
        Doctor doctor = loggedUser();
        mav.addObject("doctor", doctor);
        Page<Appointment> appointmentsPage = as.getPastDoctorAppointments(doctor.getId(), page, 5,null,status);
        mav.addObject("pastAppointments", appointmentsPage.getContent());
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", appointmentsPage.getTotalPages());
        mav.addObject("hasMore", page < appointmentsPage.getTotalPages());
        mav.addObject("activeTab", "history");
        return mav;
    }


    @RequestMapping(value = "/doctor/dashboard/profile")
    public ModelAndView getProfile(@ModelAttribute("updateDoctorForm") final UpdateDoctorForm updateDoctorForm) {
        final ModelAndView mav = new ModelAndView("doctor/dashboard-profile");
        Doctor doctor = loggedUser();
        updateDoctorForm.setForm(doctor);
        mav.addObject("doctor", doctor);
        mav.addObject("coverageList", cs.getAll());
        mav.addObject("specialtyList", ss.getAll());
        mav.addObject("activeTab", "profile");
        mav.addObject("display","none");
        return mav;
    }

    @RequestMapping(value = "/doctor/dashboard/availability")
    public ModelAndView getAvailability(@ModelAttribute("updateAvailabilityForm") UpdateAvailabilityForm updateAvailabilityForm) {
        final ModelAndView mav = new ModelAndView("doctor/dashboard-availability");
        Doctor doctor = loggedUser();
        updateAvailabilityForm.setForm(doctor.getAvailabilitySlots());
        mav.addObject("doctor", doctor);
        mav.addObject("activeTab", "availability");
        return mav;
    }

    @RequestMapping(value = "/doctor/dashboard/update", method = RequestMethod.POST)
    public ModelAndView updateDoctor(@Valid @ModelAttribute("updateDoctorForm") final UpdateDoctorForm updateDoctorForm,
                                     final BindingResult errors) {
        if (errors.hasErrors()) {
            ModelAndView mav= getProfile(updateDoctorForm);
            mav.addObject("display","block");
            return mav;
        }
        Doctor doctor = loggedUser();
        ds.updateDoctor(doctor.getId(),
                updateDoctorForm.getName(),
                updateDoctorForm.getLastName(),
                updateDoctorForm.getPhone(),
                updateDoctorForm.getSpecialties(),
                updateDoctorForm.getCoverages(),
                null);
        return new ModelAndView("redirect:/doctor/dashboard/profile?updated=true");
    }

    @ModelAttribute
    public Doctor loggedUser() {
        final Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        return ds.getByEmail((String) auth.getName()).orElseThrow(UserNotFoundException::new);
    }

    @PostMapping(value = "/doctor/dashboard/appointment/cancel", produces = "application/json")
    @ResponseBody
    public ModelAndView cancelAppointment(@RequestParam("appointmentId") Long appointmentId){
        boolean result = as.cancelAppointment(appointmentId, loggedUser().getId());
        return new ModelAndView("redirect:/doctor/dashboard/upcoming?cancelled=true");
    }

    @RequestMapping(value = "/doctor/dashboard/availability/update", method = RequestMethod.POST)
    public ModelAndView updateAvailability(
            @Valid @ModelAttribute("updateAvailabilityForm") UpdateAvailabilityForm form,
            BindingResult errors,
            @RequestParam(value = "deletedSlots", required = false) String[] deletedSlots) {
        if (errors.hasErrors()) {
            return getAvailability(form);
        }
        form.setAvailabilitySlots(form.getAvailabilitySlots());
        ds.updateDoctorAvailability(loggedUser().getId(), form.getAvailabilitySlots());
        return new ModelAndView("redirect:/doctor/dashboard/availability?updated=true");
    }

    @RequestMapping(value = "doctor/dashboard/appointment-details/{id}", method = RequestMethod.GET)
    public ModelAndView doctorAppointmentDetails(
            @ModelAttribute("doctorFileForm") final DoctorFileForm doctorFileForm,
            @PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("/doctor/appointment-details");
        Appointment appointment = as.getById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid appointment Id:" + id));
        mav.addObject("appointment", appointment);
        mav.addObject("patientFiles", afs.getByAppointmentId(appointment.getId()));
        mav.addObject("doctorFiles", afs.getByAppointmentId(appointment.getId()));
        Optional<Rating> existingRating = rs.getRatingByAppointmentId(appointment.getId());
        mav.addObject("existingRating", existingRating.orElse(null));
        return mav;
    }

    @RequestMapping(value = "doctor/dashboard/appointment-details/{id}", method = RequestMethod.POST)
    public ModelAndView doctorAppointmentDetails(
            @ModelAttribute("doctorFileForm") final DoctorFileForm doctorFileForm,
            BindingResult errors,
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes
    ) {

        if (errors.hasErrors()) {
            return doctorAppointmentDetails(doctorFileForm, id);
        }

        Appointment appointment = as.getById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid appointment Id:" + id));

        afs.create(doctorFileForm.getFiles(),"doctor",appointment.getId());
        return new ModelAndView("redirect:/doctor/dashboard/appointment-details/" + id);
    }
}
