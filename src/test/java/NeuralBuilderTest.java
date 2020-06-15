class NeuralBuilderTest {

	public static void main(String[] args) {
		NeuralBuilder neuralBuilder = new NeuralBuilder(20, 4);
		neuralBuilder.feedForward(new double[]{1, 2, 3, 4});

		NeuralNetwork.serializebleObject(neuralBuilder, "neuralBilder.obj");
		NeuralBuilder clone = (NeuralBuilder) NeuralNetwork.deserializebleObject("neuralBilder.obj");

		clone.removeRandomNeuron();
		System.out.println(clone);
	}
}