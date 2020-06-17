package nic.com.Diplomka.enums;

import java.util.Random;

public enum ActivationFunction implements IActivationFunction {
	SIGMOID {
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
			return (Math.sin(x) - Math.exp(Math.cos(-x / 2)) / 2) + Math.sin(x / 5);
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
		private static final double handred = 0.01785;

		@Override
		public double function(double x) {
			double my = 100;
			double sigma = Math.sqrt(500);
			double a = 1 / (sigma * Math.sqrt(2 * Math.PI));
			return a * Math.exp(-Math.pow(x - my, 2) / (2 * Math.pow(sigma, 2)));
		}

		@Override
		public float functionInProcent(double x) {
			return procentOfRange(function(x));
		}

		@Override
		public float procentOfRange(double number) {
			return procentOfRange(zero, handred, number);
		}
	},
	SINNAX {
		private static final double zero = -0.2;
		private static final double handred = 1;

		@Override
		public double function(double x) {
			return Math.sin(x)/x;
		}

		@Override
		public float functionInProcent(double x) {
			return procentOfRange(function(x));
		}

		@Override
		public float procentOfRange(double number) {
			return procentOfRange(zero, handred, number);
		}
	},
	SIGN {
		private static final double zero = -5;
		private static final double handred = 5;

		@Override
		public double function(double x) {
			return 5 * Math.signum(Math.cos(x));
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
