import com.chhabras.parser.impl.LidlParser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
    public void test_lidl_excludeBasedOnRegex() { // if match, then it should return false
        String input1 = "0,142 kg x 6,80 EUR/kg";
        String input2 = "0,106 kg x 15,42 EUR / kg";
        assertFalse(parser.excludeBasedOnRegex(input1));
        assertFalse(parser.excludeBasedOnRegex(input2));
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


    @Test
    public void test_1() {
        String input1 = "Bio Ingwer 0,97 A";
        String input2 = "Romatomaten 1,15 x 2 2,30 A ";
        String input3 = "Bio Knoblauch 100g 0,96 A";
        String input4 = "Weizenmehl 1kg 0,37 A";
        String input5 = "Bio Zwiebel rot 500g 1,25 x 2 2,50 A";
        String input6 = "zu zahlen 13,96";


        assertEquals("Bio Ingwer", parser.segregate(input1).get("description").trim());
        assertEquals("0,97", parser.segregate(input1).get("price").trim());

        assertEquals("Romatomaten", parser.segregate(input2).get("description").trim());
        assertEquals("2,30", parser.segregate(input2).get("price").trim());

        assertEquals("Bio Knoblauch", parser.segregate(input3).get("description").trim());
        assertEquals("0,96", parser.segregate(input3).get("price").trim());

        assertEquals("Weizenmehl", parser.segregate(input4).get("description").trim());
        assertEquals("0,37", parser.segregate(input4).get("price").trim());

        assertEquals("Bio Zwiebel rot", parser.segregate(input5).get("description").trim());
        assertEquals("2,50", parser.segregate(input5).get("price").trim());

        assertEquals("zu zahlen", parser.segregate(input6).get("description").trim());
        assertEquals("13,96", parser.segregate(input6).get("price").trim());
    }


}
