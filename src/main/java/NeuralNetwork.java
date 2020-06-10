import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NeuralNetwork {
	private List<Neuron> neuronList;
	private List<Double> inputList;

	public NeuralNetwork(int neuronCount) {
		for (int i = 0; i < neuronCount; i++) {
			neuronList.add(new Neuron());
		}
	}

	public List<Neuron> getNeuronList() {
		return neuronList;
	}

	public void setNeuronList(List<Neuron> neuronList) {
		this.neuronList = neuronList;
	}

	public List<Double> getInputList() {
		return inputList;
	}

	public void setInputList(List<Double> inputList) {
		this.inputList = inputList;
	}
}
