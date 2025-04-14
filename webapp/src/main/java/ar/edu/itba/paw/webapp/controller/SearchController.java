package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class SearchController {

    private final DoctorService doctorService;
    private final SpecialtyService specialtyService;

    @Autowired
    public SearchController(DoctorService doctorService, SpecialtyService specialtyService) {
        this.doctorService = doctorService;
        this.specialtyService = specialtyService;
    }



    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView searchBySpecialty(
            @RequestParam("specialty") long specialty,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        // Get specialty and all doctors with that specialty
        Optional<Specialty> specialtyObj = specialtyService.getById(specialty);
        List<Doctor> allDoctors = doctorService.getBySpecialty(specialtyObj.map(Specialty::getKey).orElse(null));
        List<Specialty> allSpecialties = specialtyService.getAll().orElse(new ArrayList<>());
        // Pagination logic
        int pageSize = 9;
        int totalDoctors = allDoctors.size();
        int totalPages = (int) Math.ceil((double) totalDoctors / pageSize);

        // Ensure page is within valid range
        if (page < 1) page = 1;
        if (page > totalPages && totalPages > 0) page = totalPages;

        // Get the subset of doctors for the current page
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalDoctors);

        List<Doctor> paginatedDoctors = fromIndex < totalDoctors
                ? allDoctors.subList(fromIndex, toIndex)
                : new ArrayList<>();

        ModelAndView mav = new ModelAndView("search/search");
        mav.addObject("doctors", allDoctors); // All doctors (for reference if needed)
        mav.addObject("paginatedDoctors", paginatedDoctors); // Doctors for current page
        mav.addObject("specialty", specialtyObj.orElse(null));
        mav.addObject("allSpecialties", allSpecialties);
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);

        return mav;
    }
}
