import com.chhabras.utilities.Parser;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

public class RegularExpressionTest {
    @Test
    public void test_segregater() {
        String input = "Bio Zwiebel rot 500g 1,25 x 2 2,50 A";
        String [] arr  =  Parser.segregate(input);
        //assertEquals("Bio Zwiebel rot 500g", arr[0]);
        assertEquals("2,50", arr[1]);
    }

    @Test
    public void test_pricesInItem() {
        String input = "Bio Zwiebel rot 500g 1,25 x 2 2,50 A";
        assertTrue(Parser.hasPrice(input));
    }

    @Test
    public void test_getWeight() {
        String input1 = "Bio Zwiebel rot 500g 1,25 x 2 2,50 A";
        String input2 = "Bio Zwiebel rot 50 g 1,25 x 2 2,50 A";
        String input3 = "Bio Zwiebel rot 1 Kg 1,25 x 2 2,50 A";
        String input4 = "Bio Zwiebel rot 1 KG 1,25 x 2 2,50 A";
        assertEquals("500g", Parser.getWeight(input1));
        assertEquals("50 g", Parser.getWeight(input2));
        assertEquals("1 Kg", Parser.getWeight(input3));
        assertEquals("1 KG", Parser.getWeight(input4));
    }

    @Test
    public void test_getQuantity() {
        String input1 = "Bio Zwiebel rot 500g 1,25 x 2 2,50 A";
        String input2 = "Bio Zwiebel rot 50 g 0,25 x 10 2,50 A";
        String input3 = "Bio Zwiebel rot 1 Kg 10,25 x 2 2,50 A";
        String input4 = "Bio Zwiebel rot 1 KG 12,25 x 22 2,50 A";
        assertEquals("1,25 x 2", Parser.getQuantity(input1));
        assertEquals("0,25 x 10", Parser.getQuantity(input2));
        assertEquals("10,25 x 2", Parser.getQuantity(input3));
        assertEquals("12,25 x 22", Parser.getQuantity(input4));
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
       assertTrue(Parser.hasPrice(input1));
       assertFalse(Parser.hasPrice(input2));
       assertFalse(Parser.hasPrice(input3));
        assertTrue(Parser.hasPrice(input4));
        //assertFalse(Parser.hasPrice(input5));
        assertFalse(Parser.hasPrice(input6));
        assertTrue(Parser.hasPrice(input7));
    }

    @Test
    public void test_isPrice() {
        String input1 = "2,50 A";
        String input2 = "2,50";
        String input3 = "-2,50";

        assertTrue(Parser.isPrice(input1));
        assertFalse(Parser.isPrice(input2));
        assertTrue(Parser.isPrice(input3));
    }
}
