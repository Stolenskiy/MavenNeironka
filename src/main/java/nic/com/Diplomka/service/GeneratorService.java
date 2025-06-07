package nic.com.Diplomka.service;

import nic.com.Diplomka.neuralNetwork.NeuralNetwork;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GeneratorService {
	public static final int heightImage = 250;
	public static final int weightImage = 250;
	public static int builderCount = 15;
	public static NeuralNetwork neuralNetwork = new NeuralNetwork(builderCount, 3, 3);

	public static double calculateDistanceBetweenPoints(double x1, double y1, double x2, double y2) {
		double ac = Math.abs(y2 - y1);
		double cb = Math.abs(x2 - x1);
		return Math.hypot(ac, cb);
	}

        public static void generateImages() {
                BufferedImage bufferedImageHSB = new BufferedImage(weightImage, heightImage, BufferedImage.TYPE_INT_RGB);
                BufferedImage bufferedImageRGB = new BufferedImage(weightImage, heightImage, BufferedImage.TYPE_INT_RGB);

                File dir = new File("src/main/resources/image_db");
                if (!dir.exists()) {
                        dir.mkdirs();
                }

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
                        saveImage("src/main/resources/image_db/" + b + "_hsb.png", bufferedImageHSB, "png");
                        saveImage("src/main/resources/image_db/" + b + "_rgb.png", bufferedImageRGB, "png");
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
