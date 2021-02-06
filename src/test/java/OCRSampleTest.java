import com.chhabras.entities.Item;
import com.chhabras.parser.Parser;
import com.chhabras.parser.impl.LidlParser;
import com.chhabras.parser.request.GoogleVisionRequestV1;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

public class OCRSampleTest {
    @Test
    public void test() throws Exception {
        String file1 = "PXL_20210131_114845268.jpg"; //lidl
        String file2 = "PXL_20210131_114845270.jpg"; //lidl
        String file3 = "PXL_20210120_153213956.jpg"; //lidl
        String file4 = "lidl10.jpg";
        String file5 = "edeka09.jpg";
        String file6 = "dm01.jpg";
        String file7 = "PXL_20210120_153848604.jpg"; // Bonus
        String file8 = "PXL_20210120_153907375.jpg"; // Bonus
        //all(file1, 11);
        all(file2, 7);
        all(file3, 7);
        //all(file4, 4);
        //all(file5, 15);
        //all(file6, 8);
        //all(file7, 10);
        //all(file8, 3);
    }

    private void all(String file, int count) throws Exception {
        GoogleVisionRequestV1 request = new GoogleVisionRequestV1();
        List<String> lines = request.detectDocumentText(file);
        Parser parser = request.decider(lines);
        List<Item> items = parser.parse(lines);
        boolean validate = parser.validate(items);
        assertTrue(validate);
        assertEquals(count,items.size());
    }

    @Test
    public void readRaw() throws Exception {
        //String file = "PXL_20210120_153848604.jpg";  // Bonus
        String file = "PXL_20210120_153907375.jpg";  // Bonus
        //String file = "PXL_20210120_154034451.jpg";  // Edeka Tarman
        //String file = "PXL_20210120_154024658.jpg";  // Hit
        //String file = "PXL_20210131_114845268.jpg";  //Lidl issue with quantity getting mixed with price , resulting total NOK.
        //String file = "lidl06.jpg";  //Lidl issue with quantity getting mixed with price , resulting total NOK.
        // GoogleVisionRequest request = new GoogleVisionRequest(file);
        //String rawvalue = request.rawText();
        //System.out.println(rawvalue);
        //System.out.println("####################");
        GoogleVisionRequestV1 googleVisionRequestV1 = new GoogleVisionRequestV1();
        List<String> lines = googleVisionRequestV1.detectDocumentText(file);
        //LidlParser parser = new LidlParser();
        Parser parser = googleVisionRequestV1.decider(lines);
        List<Item> items = parser.parse(lines);
        assertTrue(parser.validate(items));
    }

    @Test
    public void testme() throws Exception {/*
        String file = "PXL_20210120_153848604.jpg";
        GoogleVisionRequest request = new GoogleVisionRequest(file);
        String rawvalue = request.rawText();
        Parser parser = request.decider(rawvalue);
        List<Item> items = parser.parse(rawvalue);
        boolean validate = parser.validate(items);
        System.out.println("############## items ###############");
        for(Item i : items) {
            System.out.println(i.getName()+" : "+i.getPrice());
        }
        assertTrue(validate);*/
    }
}