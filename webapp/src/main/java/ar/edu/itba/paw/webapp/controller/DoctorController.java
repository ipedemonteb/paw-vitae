package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.AppointmentNotFoundException;
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
    private final AvailabilitySlotsService ass;

    @Autowired
    public DoctorController(DoctorService ds, AppointmentService as, CoverageService cs, SpecialtyService ss, AppointmentFileService afs, RatingService rs, AvailabilitySlotsService ass) {
        this.ds = ds;
        this.as = as;
        this.cs = cs;
        this.ss = ss;
        this.afs = afs;
        this.rs = rs;
        this.ass = ass;

    }

    @RequestMapping(value = "/doctor/dashboard")
    public ModelAndView getDoctorDashboard() {
        return new ModelAndView("redirect:/doctor/dashboard/upcoming");
    }

    @RequestMapping(value = "/doctor/dashboard/upcoming", method = RequestMethod.GET)
    public ModelAndView getUpcomingAppointments(@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "all", required = false) String dateRange,
                                                @ModelAttribute("loggedUser") final Doctor doctor
    ) {
        final ModelAndView mav = new ModelAndView("doctor/dashboard-upcoming");
        mav.addObject("doctor", doctor);
        Page<Appointment> appointmentsPage = as.getAppointments(doctor.getId(), true, page, 10, dateRange);
        mav.addObject("upcomingAppointments", appointmentsPage.getContent());
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", appointmentsPage.getTotalPages());
        mav.addObject("hasMore", page < appointmentsPage.getTotalPages());
        LOGGER.debug("Loading dashboard and upcoming appointments for doctor ID: {}", doctor.getId());
        return mav;
    }


    @RequestMapping(value = "/doctor/dashboard/history")
    public ModelAndView getAppointmentHistory(@RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "all", required = false) String status,
                                              @ModelAttribute("loggedUser") final Doctor doctor
    ) {
        final ModelAndView mav = new ModelAndView("doctor/dashboard-history");
        Page<Appointment> appointmentsPage = as.getAppointments(doctor.getId(), false, page, 10, status);
        mav.addObject("pastAppointments", appointmentsPage.getContent());
        mav.addObject("currentPage", page);
        mav.addObject("doctor", doctor);
        mav.addObject("totalPages", appointmentsPage.getTotalPages());
        mav.addObject("hasMore", page < appointmentsPage.getTotalPages());
        return mav;
    }


    @RequestMapping(value = "/doctor/dashboard/profile")
    public ModelAndView getProfile(@ModelAttribute("updateDoctorForm") final UpdateDoctorForm updateDoctorForm,
                                   @ModelAttribute("loggedUser") final Doctor doctor
    ) {
        final ModelAndView mav = new ModelAndView("doctor/dashboard-profile");
        mav.addObject("doctor", doctor);
        mav.addObject("coverageList", cs.getAll());
        mav.addObject("specialtyList", ss.getAll());
        mav.addObject("display", "none");
        return mav;
    }

    @RequestMapping(value = "/doctor/dashboard/availability")
    public ModelAndView getAvailability(@ModelAttribute("updateAvailabilityForm") UpdateAvailabilityForm updateAvailabilityForm,
                                        @ModelAttribute("loggedUser") final Doctor doctor
    ) {
        final ModelAndView mav = new ModelAndView("doctor/dashboard-availability");
        updateAvailabilityForm.setForm(doctor.getAvailabilitySlots());
        mav.addObject("doctor", doctor);
        return mav;
    }

    @RequestMapping(value = "/doctor/dashboard/update", method = RequestMethod.POST)
    public ModelAndView updateDoctor(@Valid @ModelAttribute("updateDoctorForm") final UpdateDoctorForm updateDoctorForm,
                                     final BindingResult errors,
                                     @ModelAttribute("loggedUser") final Doctor doctor
    ) {
        if (errors.hasErrors()) {
            ModelAndView mav = getProfile(updateDoctorForm, doctor);
            mav.addObject("display", "block");
            return mav;
        }
        ds.updateDoctor(doctor,
                updateDoctorForm.getName(),
                updateDoctorForm.getLastName(),
                updateDoctorForm.getPhone(),
                updateDoctorForm.getSpecialties(),
                updateDoctorForm.getCoverages(),
                updateDoctorForm.getImage());
        return new ModelAndView("redirect:/doctor/dashboard/profile?updated=true");
    }



    @PostMapping(value = "/doctor/dashboard/appointment/cancel", produces = "application/json")
    @ResponseBody
    public ModelAndView cancelAppointment(@RequestParam("appointmentId") Long appointmentId,
                                          @ModelAttribute("loggedUser") final User user
    ) {
        boolean result = as.cancelAppointment(appointmentId, user.getId());
        String value = String.valueOf(result);
        return new ModelAndView("redirect:/doctor/dashboard/upcoming?cancelled=" + value);
    }

    @RequestMapping(value = "/doctor/dashboard/availability/update", method = RequestMethod.POST)
    public ModelAndView updateAvailability(
            @Valid @ModelAttribute("updateAvailabilityForm") UpdateAvailabilityForm form,
            BindingResult errors,
            @ModelAttribute("loggedUser") final Doctor doctor
    ) {

        if (errors.hasErrors()) {
            return getAvailability(form, doctor);
        }
        ass.updateDoctorAvailability(doctor.getId(), form.getAvailabilitySlots());
        return new ModelAndView("redirect:/doctor/dashboard/availability?updated=true");
    }

    @RequestMapping(value = "doctor/dashboard/appointment-details/{id}", method = RequestMethod.GET)
    public ModelAndView doctorAppointmentDetails(
            @ModelAttribute("doctorFileForm") final DoctorFileForm doctorFileForm,
            @ModelAttribute("loggedUser") final Doctor doctor,
            @PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("/doctor/appointment-details");
        Appointment appointment = as.getById(id).orElseThrow(AppointmentNotFoundException::new);
        mav.addObject("appointment", appointment);
        mav.addObject("doctor", doctor);
        mav.addObject("patientFiles", afs.getByAppointmentId(appointment.getId()));
        mav.addObject("doctorFiles", afs.getByAppointmentId(appointment.getId()));
        mav.addObject("existingRating", rs.getRatingByAppointmentId(appointment.getId()).orElse(null));
        doctorFileForm.setAppointmentId(id);
        return mav;
    }

    @RequestMapping(value = "doctor/dashboard/appointment-details/{id}", method = RequestMethod.POST)
    public ModelAndView doctorAppointmentDetails(
            @Valid @ModelAttribute("doctorFileForm") final DoctorFileForm doctorFileForm,
            BindingResult errors,
            @ModelAttribute("loggedUser") final Doctor doctor,
            @PathVariable("id") Long id
    ) {
        if (errors.hasErrors()) {
            return doctorAppointmentDetails(doctorFileForm, doctor, id);
        }
        Appointment appointment = as.getById(id).orElseThrow(AppointmentNotFoundException::new);
        afs.create(doctorFileForm.getFiles(), "doctor", appointment.getId());
        as.updateAppointmentReport(appointment.getId(), doctorFileForm.getReport());
        return new ModelAndView("redirect:/doctor/dashboard/appointment-details/" + id);
    }
}
