package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import java.util.List;

public class ValidOfficeSpecialtiesValidator implements ConstraintValidator<ValidOfficeSpecialties, List<DoctorOfficeForm>> {
    private final SpecialtyService specialtyService;

    @Autowired
    public ValidOfficeSpecialtiesValidator(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }


    @Override
    public boolean isValid(List<DoctorOfficeForm> values, javax.validation.ConstraintValidatorContext context) {
        if (values == null || values.isEmpty()) {
            return true;
        }
        for (DoctorOfficeForm specialty : values) {
            if (specialty.getSpecialtyIds() == null || specialty.getSpecialtyIds().isEmpty()) {
                System.out.println("HOW");
                System.out.println(specialty.getSpecialtyIds());
                System.out.println("ya");
                return false;
            }
            for (Long id : specialty.getSpecialtyIds()) {
                if (id == null || id < 0) {
                    System.out.println("WHY");
                    return false;
                }
            }
            List<Specialty> specialties = specialtyService.getByIds(specialty.getSpecialtyIds());
            if (specialties.size() != specialty.getSpecialtyIds().size()) {
                System.out.println("WHAT");
                specialty.getSpecialtyIds().forEach(System.out::println);
                return false;
            }
        }
        return true;
    }
}
