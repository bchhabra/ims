import com.chhabras.entities.Item;
import com.chhabras.parser.Parser;
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
        String file3 = "lidl03.jpg";
        String file4 = "lidl10.jpg";
        String file5 = "edeka09.jpg";
        String file6 = "dm01.jpg";
        all(file1, 3);
        all(file2, 8);
        all(file3, 12);
        all(file4, 4);
        all(file5, 15);
        all(file6, 8);
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
    public void testme() throws Exception {
        String file = "edeka09.jpg";
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