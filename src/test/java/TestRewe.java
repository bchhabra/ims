import com.chhabras.parser.impl.ReweParser;
import com.chhabras.utilities.Regex;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class TestRewe {
    ReweParser parser;

    @BeforeMethod
    public void setUp() {
        ReweParser parser = new ReweParser();
        this.parser = parser;
    }
}
