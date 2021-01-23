import com.chhabras.parser.impl.LidlParser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
    public void test_segregater() {
        //String input = "Bio Zwiebel rot 500g 1,25 x 2 2,50 A";
        String input = "Bio Zwiebel rot 500g 1,25 x 2 2,50 A";
        String[] arr = parser.segregate(parser.removeWeightandQuantity(input)); // segregate should be called always after removing weight and quantity
        assertEquals("Bio Zwiebel rot", arr[0]);
        assertEquals("2,50", arr[1]);

        String input2 = "1,15 x 2 2,30 A";
        String[] arr2 = parser.segregate(input2);
        assertEquals("2,30", arr2[1]);
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
        //String input5 = "Bio Zwiebel rot 1 KG 1334,50 A";
        String input6 = "0,97 "; // it could be because of rabatt that it is not a price to be considered. refer lidl03.jpg
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
        assertFalse(parser.excludeBasedOnRegex(input1));
        assertFalse(parser.excludeBasedOnRegex(input2));
        assertFalse(parser.excludeBasedOnRegex(input3));
    }

    @Test
    public void test_lidl_refinePrice() {
        String input1 = "1,15 x 2 2,30 A"; // this should be handle by segregater() 1,15 x 2 2,30 A
        String input2 = "2,30 A";
        //String input3 = ".6,21 A"; // Negative case, removing now as most likely issue with picture taken.
        String input4 = "-3,11";
        String input5 = "0,55 A";
        String input6 = "1,80 B";
        assertEquals("1,15 x 2 2,30 A", parser.refinePrice(input1));
        assertEquals("2,30", parser.refinePrice(input2));
        //assertEquals("6,21", parser.refinePrice(input3));
        assertEquals("-3,11", parser.refinePrice(input4));
        assertEquals("0,55", parser.refinePrice(input5));
        assertEquals("1,80", parser.refinePrice(input6));
    }

    @Test
    public void test_lidl_removeWeightadQuantity() {
        String input1 = "1,15 x 2 2,30 A";
        assertEquals("2,30 A", parser.removeWeightandQuantity(input1));
    }

}
