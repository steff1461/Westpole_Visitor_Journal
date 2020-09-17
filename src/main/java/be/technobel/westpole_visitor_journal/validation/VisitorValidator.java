package be.technobel.westpole_visitor_journal.validation;

import org.junit.BeforeClass;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class VisitorValidator {

    private static Validator validator;


    @BeforeClass
    public static void setup() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

//    @Test
//    public void fNameNotNull() {
//
//        VisitorEntity visitor = new VisitorEntity();
//        visitor.setfName("")
//                .setlName("Stoffels")
//                .setlPLate("")
//                .setContactName("Guy")
//                .setCompanyName("Coca-Cola")
//                .setInTime(LocalTime.now());
//
//        Set<ConstraintViolation<VisitorEntity>> constraintViolations = validator.validate(visitor);
//
//        assertEquals(1, constraintViolations.size());
//        assertEquals(
//                "may not be null",
//                constraintViolations.iterator().next().getMessage()
//                    );
//    }
//
//
//    @Test
//    public void lNameNotNull() {
//
//        VisitorEntity visitor = new VisitorEntity();
//        visitor.setfName("Steeve")
//                .setlName("")
//                .setlPLate("1-aaa-111")
//                .setContactName("Guy")
//                .setCompanyName("Coca-Cola")
//                .setInTime(LocalTime.now());
//
//        Set<ConstraintViolation<VisitorEntity>> constraintViolations = validator.validate(visitor);
//
//        assertEquals(1, constraintViolations.size());
//        assertEquals(
//                "may not be null",
//                constraintViolations.iterator().next().getMessage()
//                    );
//    }
//
//    @Test
//    public void companyNameNotNull() {
//
//        VisitorEntity visitor = new VisitorEntity();
//        visitor.setfName("Steeve")
//                .setlName("Stoffels")
//                .setlPLate("1-aaa-111")
//                .setContactName("Guy")
//                .setCompanyName("")
//                .setInTime(LocalTime.now());;
//
//        Set<ConstraintViolation<VisitorEntity>> constraintViolations = validator.validate(visitor);
//
//        assertEquals(1, constraintViolations.size());
//        assertEquals(
//                "may not be null",
//                constraintViolations.iterator().next().getMessage()
//                    );
//    }
//
//    @Test
//    public void inTimeNotNull() {
//
//        VisitorEntity visitor = new VisitorEntity();
//        visitor.setfName("")
//                .setlName("Stoffels")
//                .setlPLate("")
//                .setContactName("Guy")
//                .setInTime(LocalTime.now())
//                .setCompanyName("Coca-Cola");
//
//        Set<ConstraintViolation<VisitorEntity>> constraintViolations = validator.validate(visitor);
//
//        assertEquals(1, constraintViolations.size());
//        assertEquals(
//                "may not be null",
//                constraintViolations.iterator().next().getMessage()
//                    );
//    }


}




