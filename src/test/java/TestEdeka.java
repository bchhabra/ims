import com.chhabras.parser.impl.EdekaParser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

public class TestEdeka {
    EdekaParser parser;

    @BeforeMethod
    public void setUp() {
        EdekaParser parser = new EdekaParser();
        this.parser = parser;
    }

    @Test
    public void test_isPrice() {
        String input1 = "2,50 AW";
        String input2 = "2,50";
        String input3 = "-2,50";
        String input4 = ".2,50 A";

        assertFalse(parser.isPrice(input1));
        assertTrue(parser.isPrice(input2));
        assertTrue(parser.isPrice(input3));
        assertFalse(parser.isPrice(input4));
    }

    @Test
    public void test_refinePrice() {
        String input1 = "1,76 B";
        String input2 = "2,30 AW";
        String input3 = "6,21 A";
        String input4 = "0,15*B";
        String input5 = "1,35*A";
        assertEquals("1,76", parser.refinePrice(input1));
        assertEquals("2,30", parser.refinePrice(input2));
        assertEquals("6,21", parser.refinePrice(input3));
        assertEquals("0,15", parser.refinePrice(input4));
        assertEquals("1,35", parser.refinePrice(input5));
    }

}
