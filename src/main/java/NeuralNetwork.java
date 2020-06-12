import java.awt.*;
import java.util.List;

public class NeuralNetwork {
	private NeuralBuilder neuralBuilder;
	private Color rgbColor;
	private Color hsbColor;

	public NeuralNetwork(int neuronCount, int firstInputCount) {
		this.neuralBuilder = new NeuralBuilder(neuronCount, firstInputCount);
	}



}
