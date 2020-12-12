import com.chhabras.entities.Item;
import com.chhabras.parser.impl.LidlParser;
import com.chhabras.request.GoogleVisionRequest;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

public class OCRSampleTest {

    @Test
    public void test() throws Exception {
        String file1 = "edeka05.jpg";
        String file2 = "edeka06.jpg";
        String file3 = "lidl03.jpg";
        String file4 = "lidl02.jpg";
       // all(file1, 3);
      //  all(file2, 8);
        all(file3, 12);
      //  all(file4, 4);
    }

    private void all(String file, int count) throws Exception {
        GoogleVisionRequest request = new GoogleVisionRequest(file);
        String rawvalue = request.detectText();
        LidlParser parser = new LidlParser();
        List<Item> items = parser.parse(rawvalue);

        for(Item i : items) {
            System.out.println(i.getName()+" : "+i.getPrice());
        }
        assertEquals(count,items.size());
    }
}