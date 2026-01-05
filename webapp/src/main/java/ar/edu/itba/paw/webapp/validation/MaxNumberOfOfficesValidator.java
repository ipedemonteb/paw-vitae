//package ar.edu.itba.paw.webapp.validation;
//
//import ar.edu.itba.paw.interfaceServices.DoctorOfficeService;
//import ar.edu.itba.paw.models.DoctorOffice;
//import ar.edu.itba.paw.models.DoctorOfficeForm;
//import ar.edu.itba.paw.webapp.form.OfficeForm;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.validation.ConstraintValidator;
//import javax.validation.ConstraintValidatorContext;
//import java.util.List;
//
//public class MaxNumberOfOfficesValidator implements ConstraintValidator<MaxNumberOfOffices, OfficeForm> {
//    private static final int MAX_OFFICES = 7;
//    private final DoctorOfficeService doctorOfficeService;
//
//    @Autowired
//    public MaxNumberOfOfficesValidator(DoctorOfficeService doctorOfficeService) {
//        this.doctorOfficeService = doctorOfficeService;
//    }
//
//    @Override
//    public boolean isValid(OfficeForm officeForm, ConstraintValidatorContext constraintValidatorContext) {
//        List<DoctorOffice> existingOffices = doctorOfficeService.getByDoctorId(officeForm.getDoctorId());
//        List<DoctorOfficeForm> newOffices = officeForm.getDoctorOfficeForm();
//
//        boolean flag = newOffices.stream()
//                .filter(office -> !office.getRemoved())
//                .count() <= MAX_OFFICES;
//        if (!flag) {
//            constraintValidatorContext.disableDefaultConstraintViolation();
//            constraintValidatorContext.buildConstraintViolationWithTemplate(
//                    "{offices.max}")
//                    .addPropertyNode("doctorOfficeForm")
//                    .addConstraintViolation();
//        }
//
//        return flag;
//    }
//}
