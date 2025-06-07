import nic.com.Diplomka.neuralNetwork.NeuralNetwork;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

public class NeuralNetworkSerializationTest {
    @Test
    void testSerializeDeserialize() {
        String file = "test.obj";
        String data = "hello";
        NeuralNetwork.serializebleObject(data, file);
        Object result = NeuralNetwork.deserializebleObject(file);
        assertEquals(data, result);
        new File(file).delete();
    }
}
