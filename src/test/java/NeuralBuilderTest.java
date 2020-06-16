class NeuralBuilderTest {

	public static void main(String[] args) {

		double max = 0;
		double min = 0;
		for (int x = -3000000; x < 3000000; x++) {
			double y = 5 * Math.signum(Math.cos(x));
			System.out.println(y);
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