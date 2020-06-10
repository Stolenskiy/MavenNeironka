package Enums;

import java.util.Map;
import java.util.Random;

public enum ActivationFunction implements IActivationFunction {
	SIGMOID{
		private static final double zero = 0;
		private static final double handred = 1;

		@Override
		public double function(double x) { // значення [0;1]
			return 1 / (1 + Math.exp(-x));
		}

		@Override
		public float functionInProcent(double x) {
			return procentOfRange(zero, handred, function(x));
		}

		@Override
		public float procentOfRange(double number) {
			return procentOfRange(zero, handred, number);
		}
	},
	SINUSOID {
		private static final double zero = -1;
		private static final double handred = 1;

		@Override
		public double function(double x) { // значення [-1;1]
			return Math.sin(x);
		}

		@Override
		public float functionInProcent(double x) {
			return procentOfRange(zero, handred, function(x));
		}

		@Override
		public float procentOfRange(double number) {
			return procentOfRange(zero, handred, number);
		}
	},
	KRIVOID {
		private static final double zero = -3.318;
		private static final double handred = 1.809;

		@Override
		public double function(double x) {
			return (Math.sin(x) - Math.exp(Math.cos(-x/2)) / 2) + Math.sin(x/5);
		}

		@Override
		public float functionInProcent(double x) {
			return procentOfRange(zero, handred, function(x));
		}

		@Override
		public float procentOfRange(double number) {
			return procentOfRange(zero, handred, number);
		}
	},
	GAUSIAN {
		private static final double zero = 0;
		private static final double handred = 0.4;

		@Override
		public double function(double x) {
			return (1 / (Math.sqrt(2 * Math.PI))) * Math.exp(-Math.pow(x, 2)/2);
		}

		@Override
		public float functionInProcent(double x) {
			return procentOfRange(function(x));
		}

		@Override
		public float procentOfRange(double number) {
			return procentOfRange(zero, handred, number);
		}
	};

	public static float procentOfRange(double zero, double handred, double number) {
		double rangeCount = handred - zero;
		double x = number - zero;
		return (float) (x / rangeCount);
	}

	public static ActivationFunction getRandomActivationFunction() {
		Random random = new Random();
		return ActivationFunction.values()[random.nextInt(ActivationFunction.values().length)];
	}

}
