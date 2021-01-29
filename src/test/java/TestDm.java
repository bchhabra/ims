import com.chhabras.parser.impl.DmParser;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class TestDm {
    DmParser parser;

    @BeforeMethod
    public void setUp() {
        DmParser parser = new DmParser();
        this.parser = parser;
    }

    @Test
    public void test_dm_refinePrice() {
        String input1 = "5,95 1";
        String input2 = "5,95 2";
        String input3 = "2,85";
        String input4 = "-0,85";
        assertEquals("5,95", parser.refinePrice(input1));
        assertEquals("5,95", parser.refinePrice(input2));
        assertEquals("2,85", parser.refinePrice(input3));
        assertEquals("-0,85", parser.refinePrice(input4));

    }


}
