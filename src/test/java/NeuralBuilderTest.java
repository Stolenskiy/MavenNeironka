class NeuralBuilderTest {

	public static void main(String[] args) {
		double my = 0.01785;
		double sigma = Math.sqrt(500);
		double a = 1 / (sigma * Math.sqrt(2 * Math.PI));
		double max = 0;
		double min = 0;
		for (int x = -300; x < 500; x++) {
			double y = a * Math.exp(-Math.pow(x - my, 2) / (2 * Math.pow(sigma, 2)));

			if (y > max) {
				max = y;
				System.out.println("Max = " + max);
				System.out.println("Min = " + min);
			}
			if (y < min) {
				min = y;
				System.out.println("Max = " + max);
				System.out.println("Min = " + min);
			}
		}

	}
}