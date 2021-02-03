import com.chhabras.parser.impl.EdekaParser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Test
    public void test_1() {
        String input1 = "ZONIN CHIANTI0,75L 8,76 B";
        String input2 = "SUMME EUR 8.76";
        String input3 = "SOMAT TABS 7,69 B : ";

        assertEquals("ZONIN CHIANTI", parser.segregate(input1).get("description").trim());
        assertEquals("8,76", parser.segregate(input1).get("price").trim());
        assertEquals("SUMME EUR", parser.segregate(input2).get("description").trim());
        assertEquals("8.76", parser.segregate(input2).get("price").trim());
        assertEquals("SOMAT TABS", parser.segregate(input3).get("description").trim());
        assertEquals("7,69", parser.segregate(input3).get("price").trim());
    }

}
