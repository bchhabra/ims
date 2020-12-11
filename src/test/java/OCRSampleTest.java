import com.chhabras.entities.Item;
import com.chhabras.request.GoogleVisionRequest;
import com.chhabras.response.RestResponse;
import com.chhabras.utilities.Parser;
import com.google.gson.Gson;
import org.testng.annotations.Test;

import java.net.URISyntaxException;
import java.util.List;

public class OCRSampleTest {

    @Test
    public void test2() throws Exception {
        //String file = "edeka01.png";
        String file = "edeka08.jpg";
        GoogleVisionRequest request = new GoogleVisionRequest(file);
        String rawvalue = request.detectText();
        List<Item> items = Parser.parse(rawvalue);
        for(Item i : items) {
            System.out.println(i.getName()+" : "+i.getPrice());
        }
    }
}