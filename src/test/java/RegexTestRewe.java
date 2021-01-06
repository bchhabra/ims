import com.chhabras.parser.impl.ReweParser;
import com.chhabras.utilities.Regex;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class RegexTestRewe {
    ReweParser parser;

    @BeforeMethod
    public void setUp() {
        ReweParser parser = new ReweParser();
        this.parser = parser;
    }

    @Test
    public void test_rewe_excludeBasedOnRegex() { // if matches should return false
        String input1 = "2 Stk x 3, 99";
        String input2 = "0, 372 kg x 24, 40"; // EUR/kg
        assertTrue("0, 372 kg".matches(Regex.weight_kg));
        assertTrue(" x ".matches(Regex.x));
        assertTrue(" x".matches(Regex.x));
        assertTrue("85551 Kirchheim".matches(Regex.postalcode));
        assertFalse(parser.excludeBasedOnRegex(input1));
        assertFalse(parser.excludeBasedOnRegex(input2));
    }

    @Test
    public void test_rewe_refinePrice() {
        String input1 = "0,25A *";
        assertEquals("0,25", parser.refinePrice(input1));
    }
}
