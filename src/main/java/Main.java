import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
	public static final int heightImage = 250;
	public static final int weightImage = 250;

	public static double calculateDistanceBetweenPoints(double x1, double y1, double x2, double y2) {
		double ac = Math.abs(y2 - y1);
		double cb = Math.abs(x2 - x1);

		return Math.hypot(ac, cb);
	}

	public static int[] randArr(int[] arr) {
		int[] returnArr = new int[arr.length];
		List<Integer> indexList = new ArrayList();
		for (int i = 0; i < arr.length; i++) {
			indexList.add(i);
		}

		for (int i = 0; i < arr.length; i++) {
			int i1 = new Random(3).nextInt(indexList.size());
			returnArr[i] = arr[indexList.get(i1)];
			indexList.remove(i1);
		}

		return returnArr;
	}

	public static void main(String[] args) {
		NeuralBuilder neuralBuilder = new NeuralBuilder(30, 3);

		BufferedImage bufferedImageHSB = new BufferedImage(weightImage, heightImage, BufferedImage.TYPE_INT_RGB);
		BufferedImage bufferedImageRGB = new BufferedImage(weightImage, heightImage, BufferedImage.TYPE_INT_RGB);
		int[] randIndexs = new int[]{-1, -1, -1};
		boolean b = true;

		for (int i = 0; i < heightImage; i++) {
			for (int j = 0; j < weightImage; j++) {
				double d = calculateDistanceBetweenPoints(i, j, heightImage / 2, weightImage / 2);


//				neuralBuilder.feedForward(new double[]{d, i, j, Math.sqrt(Math.pow(i, 2) - Math.pow(j, 2))}); // феномен трикутника
				neuralBuilder.feedForward(new double[]{i, j, d});
				float outputs[] = new float[3];
				List<Neuron> neuronList = new ArrayList<>(neuralBuilder.getNeuronList());

				if (randIndexs[0] == -1) randIndexs[0] = new Random(3).nextInt(neuronList.size() - 1);
				if (randIndexs[1] == -1) randIndexs[1] = new Random(3).nextInt(neuronList.size() - 1);
				if (randIndexs[2] == -1) randIndexs[2] = neuronList.size() - 1;

				if (b) {
					randIndexs = randArr(randIndexs);
					b = false;
				}

				outputs[0] = neuronList.get(randIndexs[0]).getOutputInProcent();
				outputs[1] = neuronList.get(randIndexs[1]).getOutputInProcent();
				outputs[2] = neuronList.get(randIndexs[2]).getOutputInProcent();

				Color colorHSB = Color.getHSBColor(outputs[0], outputs[1], outputs[2]);
				Color colorRGB = new Color(outputs[0], outputs[1], outputs[2]);
				bufferedImageHSB.setRGB(j, i, colorHSB.getRGB());
				bufferedImageRGB.setRGB(j, i, colorRGB.getRGB());
			}
		}

		File output = new File("hsb.png");
		try {
			ImageIO.write(bufferedImageHSB, "png", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		output = new File("rgb.png");
		try {
			ImageIO.write(bufferedImageRGB, "png", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/*public static void main(String[] args) {
		BufferedImage bufferedImage = new BufferedImage(weightImage, heightImage, BufferedImage.TYPE_INT_RGB);
		NeuralNetwork neuralNetwork;
		for (int i = 0; i < heightImage; i++) {
			for (int j = 0; j < weightImage; j++) {
				float d = calculateDistanceBetweenPoints(i, j, heightImage / 2, weightImage / 2);
				neuralNetwork = new NeuralNetwork(7, new double[]{i, j, d});

				float outputs[] = new float[3];
				List<Neuron> neuronList = new ArrayList<>(neuralNetwork.getNeuronList());

				outputs[0] = neuronList.get(6).getOutputInProcent();
				outputs[1] = neuronList.get(3).getOutputInProcent();
				outputs[2] = neuronList.get(4).getOutputInProcent();

				Color color = new Color(outputs[0], outputs[1], outputs[2]);
				bufferedImage.setRGB(j, i, color.getRGB());
			}
		}

		File output = new File("afterNeuralNetworkImage.png");
		try {
			ImageIO.write(bufferedImage, "png", output);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}*/
}
