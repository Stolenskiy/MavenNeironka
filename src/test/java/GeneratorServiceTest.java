import nic.com.Diplomka.service.GeneratorService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GeneratorServiceTest {
    @Test
    void testCalculateDistanceBetweenPoints() {
        double dist = GeneratorService.calculateDistanceBetweenPoints(0, 0, 3, 4);
        assertEquals(5.0, dist, 1e-6);
    }
}
