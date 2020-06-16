import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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
			int i1 = new Random().nextInt(indexList.size());
			returnArr[i] = arr[indexList.get(i1)];
			indexList.remove(i1);
		}

		return returnArr;
	}

	public static void main(String[] args) {
		int builderCount = 15;
		Scanner scanner = new Scanner(System.in);

		NeuralNetwork neuralNetwork = new NeuralNetwork(builderCount, 3, 3);

		BufferedImage bufferedImageHSB = new BufferedImage(weightImage, heightImage, BufferedImage.TYPE_INT_RGB);
		BufferedImage bufferedImageRGB = new BufferedImage(weightImage, heightImage, BufferedImage.TYPE_INT_RGB);
		while (true) {
			for (int b = 0; b < builderCount; b++) {

				for (int i = 0; i < heightImage; i++) {
					for (int j = 0; j < weightImage; j++) {

						double d = calculateDistanceBetweenPoints(i, j, heightImage / 2, weightImage / 2);

						neuralNetwork.feedForward(new double[]{i, j, d});

						Color colorHSB = neuralNetwork.getHsbColor(b);
						Color colorRGB = neuralNetwork.getRgbColor(b);

						bufferedImageHSB.setRGB(j, i, colorHSB.getRGB());
						bufferedImageRGB.setRGB(j, i, colorRGB.getRGB());
					}
				}

				File output = new File("image\\\\" + b + "_hsb.png");
				try {
					ImageIO.write(bufferedImageHSB, "png", output);
				} catch (IOException e) {
					e.printStackTrace();
				}
				output = new File("image\\\\" + b + "_rgb.png");
				try {
					ImageIO.write(bufferedImageRGB, "png", output);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.print("Ввід: ");
			int index = scanner.nextInt();
			neuralNetwork.evolute(index);
		}
	}

}
