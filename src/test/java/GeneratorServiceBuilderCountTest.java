import nic.com.Diplomka.service.GeneratorService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeneratorServiceBuilderCountTest {
    @Test
    void testBuilderCountIs16() {
        assertEquals(16, GeneratorService.builderCount);
    }
}
