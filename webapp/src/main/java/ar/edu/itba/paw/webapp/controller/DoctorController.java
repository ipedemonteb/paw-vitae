package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.AppointmentNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.form.OfficeForm;
import ar.edu.itba.paw.webapp.paging.ParamCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
public class DoctorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorController.class);

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final CoverageService coverageService;
    private final SpecialtyService specialtyService;
    private final AppointmentFileService appointmentFileService;
    private final RatingService ratingService;
    private final AvailabilitySlotsService availabilitySlotsService;
    private final UnavailabilitySlotsService unavailabilitySlotsService;
    private final DoctorOfficeService doctorOfficeService;
    private final NeighborhoodService neighborhoodService;

    @Autowired
    public DoctorController(DoctorService doctorService, AppointmentService appointmentService, CoverageService coverageService, SpecialtyService specialtyService, AppointmentFileService appointmentFileService, RatingService ratingService, AvailabilitySlotsService availabilitySlotsService, UnavailabilitySlotsService unavailabilitySlotsService, DoctorOfficeService doctorOfficeService, NeighborhoodService neighborhoodService) {
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.coverageService = coverageService;
        this.specialtyService = specialtyService;
        this.appointmentFileService = appointmentFileService;
        this.ratingService = ratingService;
        this.availabilitySlotsService = availabilitySlotsService;
        this.unavailabilitySlotsService = unavailabilitySlotsService;
        this.doctorOfficeService = doctorOfficeService;
        this.neighborhoodService = neighborhoodService;
    }

    @RequestMapping(value = "/doctor/dashboard")
    public ModelAndView getDoctorDashboard() {
        return new ModelAndView("redirect:/doctor/dashboard/upcoming");
    }

    @RequestMapping(value = "/doctor/dashboard/upcoming", method = RequestMethod.GET)
    public ModelAndView getUpcomingAppointments(@ParamCustomizer(defaultValue = 1) QueryParam page,
                                                @RequestParam(defaultValue = "all", required = false) String dateRange,
                                                @ModelAttribute("loggedUser") final Doctor doctor
    ) {
        final ModelAndView mav = new ModelAndView("doctor/dashboard-upcoming");
        mav.addObject("doctor", doctor);
        Page<Appointment> appointmentsPage = appointmentService.getAppointments(doctor.getId(), true, (int) page.getValue(), 10, dateRange);
        mav.addObject("upcomingAppointments", appointmentsPage.getContent());
        mav.addObject("currentPage", page.getValue());
        mav.addObject("totalPages", appointmentsPage.getTotalPages());
        LOGGER.debug("Loading dashboard and upcoming appointments for doctor ID: {}", doctor.getId());
        return mav;
    }


    @RequestMapping(value = "/doctor/dashboard/history")
    public ModelAndView getAppointmentHistory(@ParamCustomizer(defaultValue = 1) QueryParam page,
                                              @RequestParam(defaultValue = "all", required = false) String status,
                                              @ModelAttribute("loggedUser") final Doctor doctor
    ) {
        final ModelAndView mav = new ModelAndView("doctor/dashboard-history");
        Page<Appointment> appointmentsPage = appointmentService.getAppointments(doctor.getId(), false, (int) page.getValue(), 10, status);
        mav.addObject("pastAppointments", appointmentsPage.getContent());
        mav.addObject("currentPage", page.getValue());
        mav.addObject("doctor", doctor);
        mav.addObject("totalPages", appointmentsPage.getTotalPages());
        return mav;
    }


    @RequestMapping(value = "/doctor/dashboard/profile")
    public ModelAndView getProfile(@ModelAttribute("updateDoctorForm") final UpdateDoctorForm updateDoctorForm,
                                   @ModelAttribute("loggedUser") final Doctor doctor
    ) {
        final ModelAndView mav = new ModelAndView("doctor/dashboard-profile");
        mav.addObject("doctor", doctor);
        mav.addObject("coverageList", coverageService.getAll());
        mav.addObject("specialtyList", specialtyService.getAll());
        mav.addObject("display", "none");
        return mav;
    }

    @RequestMapping(value = "/doctor/dashboard/availability")
    public ModelAndView getAvailability(@ModelAttribute("updateAvailabilityForm") UpdateAvailabilityForm updateAvailabilityForm,
                                        @ModelAttribute("loggedUser") final Doctor doctor
    ) {
        final ModelAndView mav = new ModelAndView("doctor/dashboard-availability");
        updateAvailabilityForm.setAvailabilitySlots(availabilitySlotsService.getDoctorAvailabilitySlots(doctor));
        updateAvailabilityForm.setUnavailabilitySlots(unavailabilitySlotsService.getDoctorUnavailabilitySlots(doctor));
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
        doctorService.updateDoctor(doctor,
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
    public ModelAndView cancelAppointment(@ParamCustomizer(paramName = "appointmentId") QueryParam appointmentId,
                                          @ModelAttribute("loggedUser") final User user
    ) {
        boolean result = appointmentService.cancelAppointment(appointmentId.getValue(), user.getId());
        String value = String.valueOf(result);
        return new ModelAndView("redirect:/doctor/dashboard/upcoming?cancelled=" + value);
    }

    @RequestMapping(value = "/doctor/dashboard/availability/update", method = RequestMethod.POST)
    public ModelAndView updateAvailability(
            @Valid @ModelAttribute("updateAvailabilityForm") UpdateAvailabilityForm form,
            BindingResult availabilityErrors,
            @ModelAttribute("loggedUser") final Doctor doctor
    ) {
        if (availabilityErrors.hasErrors()) {
            return getAvailability(form, doctor);
        }

        unavailabilitySlotsService.updateDoctorUnavailability(doctor, form.getUnavailabilitySlots());
        availabilitySlotsService.updateDoctorAvailability(doctor, form.getAvailabilitySlots());
        return new ModelAndView("redirect:/doctor/dashboard/availability?updated=true");
    }

    @RequestMapping(value = "/doctor/dashboard/unavailability/{year}/{month}", method = RequestMethod.GET)
    @ResponseBody
    public String getUnavailabilityByMonth(
            @PathVariable("year") int year,
            @PathVariable("month") int month,
            @ModelAttribute("loggedUser") final Doctor doctor
    ) {
        return unavailabilitySlotsService.getUnavailabilityByDoctorIdAndMonthAndYear(doctor.getId(), month, year);
    }

    @RequestMapping(value = "doctor/dashboard/appointment-details/{id}", method = RequestMethod.GET)
    public ModelAndView doctorAppointmentDetails(
            @ModelAttribute("doctorFileForm") final DoctorFileForm doctorFileForm,
            @ModelAttribute("loggedUser") final Doctor doctor,
            @PathVariable("id") Long id) {
        ModelAndView mav = new ModelAndView("/doctor/appointment-details");
        Appointment appointment = appointmentService.getById(id).orElseThrow(AppointmentNotFoundException::new);
        mav.addObject("appointment", appointment);
        mav.addObject("doctor", doctor);
        mav.addObject("patientFiles", appointmentFileService.getByAppointmentId(appointment.getId()));
        mav.addObject("doctorFiles", appointmentFileService.getByAppointmentId(appointment.getId()));
        mav.addObject("existingRating", ratingService.getRatingByAppointmentId(appointment.getId()).orElse(null));
        mav.addObject("isCancelled", appointment.getStatus().equals(AppointmentStatus.CANCELADO.getValue()));
        mav.addObject("office", appointment.getDoctorOffice());
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
        appointmentFileService.create(doctorFileForm.getFiles(), "doctor", id);
        appointmentService.updateAppointmentReport(id, doctorFileForm.getReport());
        return new ModelAndView("redirect:/doctor/dashboard/appointment-details/" + id);
    }

    @RequestMapping(value = "doctor/dashboard/offices", method = RequestMethod.GET)
    public ModelAndView getDoctorOffices(@ModelAttribute("loggedUser") final Doctor doctor,
                                         @ModelAttribute("doctorOfficeForm") final OfficeForm officeForm) {
        officeForm.setDoctorId(doctor.getId());
        ModelAndView mav = new ModelAndView("doctor/dashboard-offices");
        mav.addObject("doctor", doctor);
        mav.addObject("doctorOffices", doctorOfficeService.getAllByDoctorId(doctor.getId()));
        mav.addObject("neighborhoodList", neighborhoodService.getAll());
        mav.addObject("specialtyList", specialtyService.getAll());
        return mav;
    }

    @RequestMapping(value = "doctor/dashboard/offices/update", method = RequestMethod.POST)
    public ModelAndView createDoctorOffice(@Valid @ModelAttribute("doctorOfficeForm") final OfficeForm officeForm,
                                           BindingResult errors,
                                           @ModelAttribute("loggedUser") final Doctor doctor) {
        if (errors.hasErrors()) {
            return getDoctorOffices(doctor, officeForm);
        }

        doctorOfficeService.update(officeForm.getDoctorOfficeForm(),doctor);
        return new ModelAndView("redirect:/doctor/dashboard/offices?updated=true");
    }

    @RequestMapping("/doctor/appointments/{patientId}/history")
    public ModelAndView getPatientHistory(@PathVariable long patientId,
                                          @RequestParam(defaultValue = "1") int page, @ModelAttribute("loggedUser") final Doctor doctor) {
        final int pageSize = 10;

        Page<Map.Entry<Appointment, List<AppointmentFile>>> history =
                appointmentFileService.getGroupedFilesForPatient(patientId, page, pageSize);

        ModelAndView mav = new ModelAndView("doctor/patient-history");
        mav.addObject("appointmentFiles", history.getContent());
        mav.addObject("currentPage", history.getPageNumber());
        mav.addObject("totalPages", history.getTotalPages());
        mav.addObject("doctor" , doctor);
        mav.addObject("patientId", patientId);
        return mav;
    }


}
