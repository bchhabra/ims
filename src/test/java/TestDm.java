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

}
