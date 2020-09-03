package be.technobel.westpole_visitor_journal.validation;

import be.technobel.westpole_visitor_journal.repository.VisitorEntity;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class VisitorValidator {

    private static Validator validator;

    @BeforeClass
    public static void setup() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void fNameNotNull() {

        VisitorEntity visitor = new VisitorEntity();
        visitor.setfName("").setlName("ppppppppppp").setlPLate("").setContactName(
                "ppppppppppppppppppppppppppppppppppppppppppppppp").setCompanyName("p");

        Set<ConstraintViolation<VisitorEntity>> constraintViolations = validator.validate(visitor);

        assertEquals(1, constraintViolations.size());
        assertEquals(
                "may not be null",
                constraintViolations.iterator().next().getMessage()
                    );
    }

    //@Todo

//    @Test
//    public void fNameNotNull() {
//
//    }
//
//    @Test
//    public void fNameNotNull() {
//
//    }
//
//    @Test
//    public void fNameNotNull() {
//
//    }


}




