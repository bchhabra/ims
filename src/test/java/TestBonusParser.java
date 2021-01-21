import com.chhabras.parser.impl.BonusParser;
import com.chhabras.utilities.Regex;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class TestBonusParser {
    BonusParser parser;

    @BeforeMethod
    public void setUp(){
        BonusParser parser = new BonusParser();
        this.parser = parser;
    }

    @Test
    public void test() {
    String one = "1, 95 B";
    String two = "0,76 B";
        System.out.println();
        System.out.println(two.substring(0,two.lastIndexOf(" ")));
    }

    @Test
    public void test1() {
        assertTrue("2,69".matches(Regex.price));
        assertTrue("2,69 EUR/kg".matches(Regex.Bonus_regex3));
    }


}
