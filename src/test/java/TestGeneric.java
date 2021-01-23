import com.chhabras.utilities.Regex;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class TestGeneric {

    @Test
    public static void test_1() {
        assertTrue("85609 Aschheim".matches(Regex.postalcode));
        assertTrue("Tel: 089/ 90019008".matches(Regex.phone1));
   //     assertTrue("23.12.2020".matches(Regex.date));

    }

}
