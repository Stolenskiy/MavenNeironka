import nic.com.Diplomka.controller.IndexViewController;
import nic.com.Diplomka.service.GeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class IndexViewControllerTest {
    @Test
    void testImageListSize() {
        IndexViewController controller = new IndexViewController();
        Model model = new ExtendedModelMap();
        controller.startPage(model);
        List<String> imageList = (List<String>) model.getAttribute("imageList");
        assertNotNull(imageList);
        assertEquals(GeneratorService.builderCount * 2, imageList.size());
    }
}
