import Enums.ActivationFunction;

import java.util.Random;

public class Neuron {
	private double inputs[];
	private double weights[];
	private double output;
	private int id;

	private ActivationFunction activationFunction;

	public Neuron() {
	}

	public Neuron(double[] inputs, double[] weights, ActivationFunction activationFunction) {
		this.inputs = inputs;
		this.weights = weights;
		this.activationFunction = activationFunction;
	}

	public Neuron(double[] inputs) {
		this.inputs = inputs;
		this.activationFunction = ActivationFunction.getRandomActivationFunction();
		this.weights = new double[inputs.length];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = new Random().nextFloat();
		}
	}

	public Neuron(int inputCount, int id) {
		this.id = id;
		this.inputs = new double[inputCount];
		this.weights = new double[inputCount];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = new Random().nextFloat();
		}
		this.activationFunction = ActivationFunction.getRandomActivationFunction();

	}

	public double calculateOutput() {
		output = 0;
		for (int i = 0; i < inputs.length; i++) {
			output += inputs[i] * weights[i];
		}

		output = activationFunction.function(output);
		return output;
	}

	public float getOutputInProcent() {
		return activationFunction.procentOfRange(getOutput());
	}

	public double getOutput() {
		return output;
	}

	public double[] getInputs() {
		return inputs;
	}

	public void setInputs(double[] inputs) {
		this.inputs = inputs;

	}

	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	public void setOutput(double output) {
		this.output = output;
	}

	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}

	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
