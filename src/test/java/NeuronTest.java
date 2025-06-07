import nic.com.Diplomka.enums.ActivationFunction;
import nic.com.Diplomka.neuralNetwork.Neuron;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NeuronTest {
    @Test
    void testCalculateOutput() {
        double[] inputs = {1.0, 2.0};
        double[] weights = {0.5, 0.5};
        Neuron neuron = new Neuron(inputs, weights, ActivationFunction.SIGMOID);
        double output = neuron.calculateOutput();
        assertEquals(1 / (1 + Math.exp(-1.5)), output, 1e-6);
        assertEquals(output, neuron.getOutput(), 1e-9);
        assertEquals(output, neuron.getOutputInPercent(), 1e-6);
    }
}
