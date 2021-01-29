import com.chhabras.parser.impl.LidlParser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

public class TestLidl {
    LidlParser parser;

    @BeforeMethod
    public void setUp() {
        LidlParser parser = new LidlParser();
        this.parser = parser;
    }

    @Test
    public void test_isPrice() {
        String input1 = "2,50 A";
        String input2 = "2,50";
        String input3 = "-2,50";
        String input4 = ".2,50 A";

        assertFalse(parser.isPrice(input1));
        assertTrue(parser.isPrice(input2));
        assertTrue(parser.isPrice(input3));
        assertFalse(parser.isPrice(input4));
    }

    @Test
    public void test_lidl_excludeBasedOnRegex() { // if matches should return false
        String input1 = "0,67 x 2";
        String input2 = "0,142 kg x 6,80 EUR/kg";
        String input3 = "0,67 x";
        String input4 = "0,106 kg x 15,42 EUR / kg";
     //   assertFalse(parser.excludeBasedOnRegex(input1));
        assertFalse(parser.excludeBasedOnRegex(input2));
      //  assertFalse(parser.excludeBasedOnRegex(input3));
        assertFalse(parser.excludeBasedOnRegex(input4));
    }

    @Test
    public void test_lidl_refinePrice() {
        String input1 = "1,15 x 2 2,30 A";
        String input2 = "2,30 A";
        String input4 = "-3,11";
        String input5 = "0,55 A";
        String input6 = "1,80 B";
        assertEquals("1,15 x 2 2,30", parser.refinePrice(input1));
        assertEquals("2,30", parser.refinePrice(input2));
        assertEquals("-3,11", parser.refinePrice(input4));
        assertEquals("0,55", parser.refinePrice(input5));
        assertEquals("1,80", parser.refinePrice(input6));
    }




   //^(([a-zA-Z]*(\.|\s)*)+)((\d{1,2}\s*(,|\.)\s*\d{2}\s*x\s*\d{1,2})|(\d{1,3}\s*(,|\.)?\s*(\d{2,3})?\s*(kg|KG|Kg|g)))?(-?\s?\d{1,2}\s*(,|\.)\s*\d{2}\s*(B|BW|A|AW|A))  --from code regex 1
   //^(([a-zA-Z]*(\.|\s)*)+)((\d{1,2}\s*(,|\.)\s*\d{2}\s*x\s*\d{1,2})|(\d{1,3}\s*(,|\.)?\s*(\d{2,3})?\s*(kg|KG|Kg|g)))?(-?\s?\d{1,2}\s*(,|\.)\s*\d{2}) --from code regex 2

    @Test
    public void test_1() {
        Pattern pattern = Pattern.compile(parser.LIDL_regex1);
        System.out.println(pattern);
        String input1 = "Bio Ingwer 0,97 A";
        String input2 = "Romatomaten 1,15 x 2 2,30 A ";
        String input3 = "Bio Knoblauch 100g 0,96 A";
        String input4 = "Weizenmehl 1kg 0,37 A";
        String input5 = "Bio Zwiebel rot 500g 1,25 x 2 2,50 A";

        Matcher matcher1 = pattern.matcher(input1);
        matcher1.find();
        assertEquals("Bio Ingwer", matcher1.group(1).trim());
        assertEquals("0,97 A", matcher1.group(6).trim());

        Matcher matcher2 = pattern.matcher(input2);
        matcher2.find();
        assertEquals("Romatomaten", matcher2.group(1).trim());
        assertEquals("2,30 A", matcher2.group(6).trim());

        Matcher matcher3 = pattern.matcher(input3);
        matcher3.find();
        assertEquals("Bio Knoblauch 100g", matcher3.group(1).trim());
        assertEquals("0,96 A", matcher3.group(6).trim());

        Matcher matcher4 = pattern.matcher(input4);
        matcher4.find();
        assertEquals("Weizenmehl 1kg", matcher4.group(1).trim());
        assertEquals("0,37 A", matcher4.group(6).trim());

        Matcher matcher5 = pattern.matcher(input5);
        matcher5.find();
        assertEquals("Bio Zwiebel rot 500g", matcher5.group(1).trim());
        assertEquals("2,50 A", matcher5.group(6).trim());
    }


    @Test
    public void test_2() {
        Pattern pattern = Pattern.compile(parser.LIDL_regex2);
        System.out.println(pattern);
        String input1 = "zu zahlen 13,96";

        Matcher matcher1 = pattern.matcher(input1);
        matcher1.find();
        assertEquals("zu zahlen", matcher1.group(1).trim());
        assertEquals("13,96", matcher1.group(6).trim());
    }
}
