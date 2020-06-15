class NeuralBuilderTest {

	public static void main(String[] args) {
		NeuralNetwork neuralNetwork = new NeuralNetwork(10, 5, 3);

		neuralNetwork.feedForward(new double[]{1, 2, 3});
		neuralNetwork.getHsbColor(1);
		neuralNetwork.evolute(1);
	}
}