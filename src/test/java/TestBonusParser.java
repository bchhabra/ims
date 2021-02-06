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
    public void test_1() {
        String input1 = "Bio Ingwer 0,97 B";
        String input2 = "0, 112 kg x     7,80 EUR/kg      0,97 B";
        String input3 = "Ingwer ";

        assertEquals("Bio Ingwer", parser.segregate(input1).get("description").trim());
        assertEquals("0,97", parser.segregate(input1).get("price").trim());

        assertEquals("", parser.segregate(input2).get("description").trim());
        assertEquals("0,97", parser.segregate(input2).get("price").trim());

        // TODO To be Fixed
        // assertEquals("Ingwer", parser.segregate(input3).get("description").trim());
        // assertEquals("", parser.segregate(input3).get("price").trim());
    }

}
