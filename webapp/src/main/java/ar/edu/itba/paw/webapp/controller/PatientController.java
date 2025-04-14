package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.ClientService;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.PatientForm;
import ar.edu.itba.paw.webapp.form.UpdatePatientForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class PatientController {

    private final ClientService cs;
    private final DoctorService ds;
    private final AppointmentService as;
    private final CoverageService covs;
    @Autowired
    public PatientController(ClientService cs, DoctorService ds, AppointmentService as,CoverageService covs) {
        this.cs = cs;
        this.ds = ds;
        this.as = as;
        this.covs = covs;
    }

    @RequestMapping(value = "/patient/dashboard")
    public ModelAndView getDoctorDashboard() {
        final ModelAndView mav = new ModelAndView("patient/dashboard");
        Client patient = loggedUser();
        List<Coverage> coverageList = covs.getAll().orElse(new ArrayList<>());
        // Crear un nuevo objeto de formulario para actualización
        UpdatePatientForm updatePatientForm = new UpdatePatientForm();
        updatePatientForm.setName(patient.getName());
        updatePatientForm.setLastName(patient.getLastName());
        updatePatientForm.setPhone(patient.getPhone());
        updatePatientForm.setCoverage(patient.getCoverage().getName());

        mav.addObject("patient", patient);
        mav.addObject("updatePatientForm", updatePatientForm);
        mav.addObject("coverageList", coverageList);
        return mav;
    }

    @RequestMapping(value = "/patient/dashboard/update", method = RequestMethod.POST)
    public ModelAndView updatePatient(@Valid @ModelAttribute("updatePatientForm") final UpdatePatientForm updatePatientForm,
                                      final BindingResult errors) {
        if (errors.hasErrors()) {
            final ModelAndView mav = new ModelAndView("patient/dashboard");
            Client patient = loggedUser();
            mav.addObject("patient", patient);
            return mav;
        }
        System.out.println("ahaeljuhgaega0"+  updatePatientForm.getCoverage());
        Client client = loggedUser();
        cs.updateClient(client.getId(),
                updatePatientForm.getName(),
                updatePatientForm.getLastName(),
                updatePatientForm.getPhone(),
                updatePatientForm.getCoverage());

        return new ModelAndView("redirect:/patient/dashboard");
    }

    @ModelAttribute
    public Client loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return cs.getByEmail((String) auth.getName()).orElseThrow(UserNotFoundException::new);
    }
}
