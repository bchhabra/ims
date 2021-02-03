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

}
