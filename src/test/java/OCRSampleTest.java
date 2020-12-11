import com.chhabras.entities.Item;
import com.chhabras.handler.RestAssuredHandler;
import com.chhabras.request.GoogleVisionRequest;
import com.chhabras.request.OCRRequest;
import com.chhabras.response.RestResponse;
import com.chhabras.utilities.Parser;
import com.google.gson.Gson;
import org.testng.annotations.Test;

import java.net.URISyntaxException;
import java.util.List;

public class OCRSampleTest {
    private Gson g = new Gson();
    private RestAssuredHandler handler = new RestAssuredHandler();
    @Test
    public void test() throws URISyntaxException {
        String file = "edeka01.png";
        OCRRequest request = new OCRRequest(file);
        RestResponse rsp = handler.sendRequest(request);
        System.out.println(rsp);
    }

    @Test
    public void test2() throws Exception {
        //String file = "edeka01.png";
        String file = "lidl03.jpg";
        GoogleVisionRequest request = new GoogleVisionRequest(file);
        String rawvalue = request.detectText();
        List<Item> items = Parser.parse(rawvalue);
        for(Item i : items) {
            System.out.println(i.getName()+" : "+i.getPrice());
        }
        //validateLidl(items)
    }

}