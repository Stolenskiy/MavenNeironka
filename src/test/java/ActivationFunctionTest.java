import nic.com.Diplomka.enums.ActivationFunction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ActivationFunctionTest {
    @Test
    void testSigmoidFunction() {
        double result = ActivationFunction.SIGMOID.function(0);
        assertEquals(0.5, result, 1e-6);
    }

    @Test
    void testSinusoidFunctionInProcent() {
        float percent = ActivationFunction.SINUSOID.functionInProcent(0);
        assertEquals(0.5f, percent, 1e-6);
    }

    @Test
    void testProcentOfRange() {
        float p = ActivationFunction.procentOfRange(0, 10, 5);
        assertEquals(0.5f, p, 1e-6);
    }

    @Test
    void testGetRandomActivationFunction() {
        ActivationFunction f = ActivationFunction.getRandomActivationFunction();
        assertNotNull(f);
        assertTrue(java.util.EnumSet.allOf(ActivationFunction.class).contains(f));
    }
}
