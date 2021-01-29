import com.chhabras.parser.impl.BonusParser;
import com.chhabras.utilities.Regex;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class TestBonusParser {
    BonusParser parser;

    @BeforeMethod
    public void setUp() {
        BonusParser parser = new BonusParser();
        this.parser = parser;
    }

    @Test
    public void test() {
        String one = "1, 95 B";
        String two = "0,76 B";
        System.out.println();
        System.out.println(two.substring(0, two.lastIndexOf(" ")));
    }

    @Test
    public void test1() {
        assertTrue("2,69".matches(Regex.price));
        assertTrue("2,69 EUR/kg".matches(parser.Bonus_regex3));
    }


    @Test
    public void test_refinePrice() {
        String input1 = "0,95 B";
        String input2 = "1, 49 B";
        String input3 = "4, 49 A";
        String input4 = "0,48 A";
        String input5 = "0, 25 A";
        assertEquals("0,95", parser.refinePrice(input1));
        assertEquals("1,49", parser.refinePrice(input2));
        assertEquals("4,49", parser.refinePrice(input3));
        assertEquals("0,48", parser.refinePrice(input4));
        assertEquals("0,25", parser.refinePrice(input5));

    }

}
