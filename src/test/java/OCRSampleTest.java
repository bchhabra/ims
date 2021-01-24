import com.chhabras.entities.Item;
import com.chhabras.parser.Parser;
import com.chhabras.request.Detect;
import com.chhabras.request.GoogleVisionRequest;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class OCRSampleTest {

    @Test
    public void test() throws Exception {
        String file1 = "edeka05.jpg";
        String file2 = "edeka06.jpg";
        //String file3 = "lidl03.jpg";
        String file4 = "lidl10.jpg";
        String file5 = "edeka09.jpg";
        String file6 = "dm01.jpg";
        String file7 = "PXL_20210120_153848604.jpg";
        String file8 = "PXL_20210120_153907375.jpg";
        all(file1, 3);
        all(file2, 8);
        //all(file3, 12);
        all(file4, 4);
        all(file5, 15);
        all(file6, 8);
        all(file7, 10);
        all(file8, 3);
    }

    private void all(String file, int count) throws Exception {
        GoogleVisionRequest request = new GoogleVisionRequest(file);
        String rawvalue = request.rawText();
        Parser parser = request.decider(rawvalue);
        List<Item> items = parser.parse(rawvalue);
        boolean validate = parser.validate(items);
        System.out.println("############## items ###############");
        for(Item i : items) {
            System.out.println(i.getName()+" : "+i.getPrice());
        }
        assertTrue(validate);
        assertEquals(count,items.size());
    }
    @Test
    public void readRaw() throws Exception {
        String file = "PXL_20210120_153213956.jpg";
        // GoogleVisionRequest request = new GoogleVisionRequest(file);
        //String rawvalue = request.rawText();
        //System.out.println(rawvalue);
        //System.out.println("####################");
        Detect detect = new Detect();
        System.out.println(detect.detectDocumentText(file));

    }

    @Test
    public void testme() throws Exception {
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
        assertTrue(validate);
    }
}