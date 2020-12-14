import com.chhabras.parser.impl.DmParser;
import com.chhabras.parser.impl.LidlParser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

public class RegularExpressionTest {
    LidlParser parser;
    DmParser dmparser;

    @BeforeMethod
    public void setUp(){
        LidlParser parser = new LidlParser();
        DmParser dmparser = new DmParser();
        this.parser = parser;
        this.dmparser = dmparser;
    }
    @Test
    public void test_segregater() {
        String input = "Bio Zwiebel rot 500g 1,25 x 2 2,50 A";
        String [] arr  =  parser.segregate(input);
        //assertEquals("Bio Zwiebel rot 500g", arr[0]);
        assertEquals("2,50", arr[1]);
    }

    @Test
    public void test_pricesInItem() {
        String input = "Bio Zwiebel rot 500g 1,25 x 2 2,50 A";
        assertTrue(parser.hasPrice(input));
    }

    @Test
    public void test_getWeight() {
        String input1 = "Bio Zwiebel rot 500g 1,25 x 2 2,50 A";
        String input2 = "Bio Zwiebel rot 50 g 1,25 x 2 2,50 A";
        String input3 = "Bio Zwiebel rot 1 Kg 1,25 x 2 2,50 A";
        String input4 = "Bio Zwiebel rot 1 KG 1,25 x 2 2,50 A";
        assertEquals("500g", parser.getWeight(input1));
        assertEquals("50 g", parser.getWeight(input2));
        assertEquals("1 Kg", parser.getWeight(input3));
        assertEquals("1 KG", parser.getWeight(input4));
    }

    @Test
    public void test_getQuantity() {
        String input1 = "Bio Zwiebel rot 500g 1,25 x 2 2,50 A";
        String input2 = "Bio Zwiebel rot 50 g 0,25 x 10 2,50 A";
        String input3 = "Bio Zwiebel rot 1 Kg 10,25 x 2 2,50 A";
        String input4 = "Bio Zwiebel rot 1 KG 12,25 x 22 2,50 A";
        assertEquals("1,25 x 2", parser.getQuantity(input1));
        assertEquals("0,25 x 10", parser.getQuantity(input2));
        assertEquals("10,25 x 2", parser.getQuantity(input3));
        assertEquals("12,25 x 22", parser.getQuantity(input4));
    }

    @Test
    public void test_hasPrice() {
        String input1 = "Bio Zwiebel rot 500g 1,25 x 2 2,50 A";
        String input2 = "Bio Zwiebel rot 2,50";
        String input3 = "Bio Zwiebel rot 1 Kg 10";
        String input4 = "Bio Zwiebel rot 1 KG 134,50 A";
        String input5 = "Bio Zwiebel rot 1 KG 1334,50 A";
        String input6 = "0,97 ";
        String input7 = "0,97 BW ";
       assertTrue(parser.hasPrice(input1));
       assertFalse(parser.hasPrice(input2));
       assertFalse(parser.hasPrice(input3));
        assertTrue(parser.hasPrice(input4));
        //assertFalse(parser.hasPrice(input5));
        assertFalse(parser.hasPrice(input6));
        assertTrue(parser.hasPrice(input7));
    }

    @Test
    public void test_isPrice() {
        String input1 = "2,50 A";
        String input2 = "2,50";
        String input3 = "-2,50";

        assertTrue(parser.isPrice(input1));
        assertFalse(parser.isPrice(input2));
        assertTrue(parser.isPrice(input3));
    }

    @Test
    public void test_dm_isPrice() {
        String input1 = "2,50 1";
        String input2 = "2,50";
        String input3 = "-2,50";

        assertTrue(dmparser.isPrice(input1));
        assertTrue(dmparser.isPrice(input2));
        assertTrue(dmparser.isPrice(input3));
    }
}
