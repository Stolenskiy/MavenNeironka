package nic.com.Diplomka;

import nic.com.Diplomka.neuralNetwork.NeuralNetwork;
import nic.com.Diplomka.service.GeneratorService;

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
                int builderCount = 16;
		Scanner scanner = new Scanner(System.in);

        NeuralNetwork neuralNetwork = new NeuralNetwork(builderCount, 3, 3);

        BufferedImage bufferedImageHSB = new BufferedImage(GeneratorService.weightImage, GeneratorService.heightImage, BufferedImage.TYPE_INT_RGB);
        BufferedImage bufferedImageRGB = new BufferedImage(GeneratorService.weightImage, GeneratorService.heightImage, BufferedImage.TYPE_INT_RGB);
		while (true) {
			for (int b = 0; b < builderCount; b++) {

                                for (int i = 0; i < GeneratorService.heightImage; i++) {
                                        for (int j = 0; j < GeneratorService.weightImage; j++) {

                                                double d = calculateDistanceBetweenPoints(i, j, GeneratorService.heightImage / 2, GeneratorService.weightImage / 2);

						neuralNetwork.feedForward(new double[]{i, j, d});

						Color colorHSB = neuralNetwork.getHsbColor(b);
						Color colorRGB = neuralNetwork.getRgbColor(b);

						bufferedImageHSB.setRGB(j, i, colorHSB.getRGB());
						bufferedImageRGB.setRGB(j, i, colorRGB.getRGB());
					}
				}
				saveImage("image\\\\" + b + "_hsb.png", bufferedImageHSB, "png");
				saveImage("image\\\\" + b + "_rgb.png", bufferedImageRGB, "png");
			}


			int index = 5;
			for (; ; ) {
				try {
					System.out.print("Ввід: ");

					String line = scanner.nextLine();
					index = Integer.valueOf(line);
					break;
				} catch (Exception ex) {

				}
			}
			neuralNetwork.evolute(index);
		}
	}
	private static void saveImage(String imgDir, BufferedImage image, String format){
		File output = new File(imgDir);
		try {
			ImageIO.write(image, format, output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
