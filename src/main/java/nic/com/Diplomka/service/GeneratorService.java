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
        public static int builderCount = 16;
	public static NeuralNetwork neuralNetwork = new NeuralNetwork(builderCount, 3, 3);

	public static double calculateDistanceBetweenPoints(double x1, double y1, double x2, double y2) {
		double ac = Math.abs(y2 - y1);
		double cb = Math.abs(x2 - x1);
		return Math.hypot(ac, cb);
	}

        public static void generateImages() {
                BufferedImage[] bufferedImagesHSB = new BufferedImage[builderCount];
                BufferedImage[] bufferedImagesRGB = new BufferedImage[builderCount];

                for (int b = 0; b < builderCount; b++) {
                        bufferedImagesHSB[b] = new BufferedImage(weightImage, heightImage, BufferedImage.TYPE_INT_RGB);
                        bufferedImagesRGB[b] = new BufferedImage(weightImage, heightImage, BufferedImage.TYPE_INT_RGB);
                }

                File dir = new File("src/main/resources/image_db");
                if (!dir.exists()) {
                        dir.mkdirs();
                }

                double[] inputs = new double[3];
                for (int i = 0; i < heightImage; i++) {
                        for (int j = 0; j < weightImage; j++) {
                                double d = calculateDistanceBetweenPoints(i, j, heightImage / 2.0, weightImage / 2.0);
                                inputs[0] = i;
                                inputs[1] = j;
                                inputs[2] = d;
                                neuralNetwork.feedForward(inputs);
                                for (int b = 0; b < builderCount; b++) {
                                        Color colorHSB = neuralNetwork.getHsbColor(b);
                                        Color colorRGB = neuralNetwork.getRgbColor(b);
                                        bufferedImagesHSB[b].setRGB(j, i, colorHSB.getRGB());
                                        bufferedImagesRGB[b].setRGB(j, i, colorRGB.getRGB());
                                }
                        }
                }

                for (int b = 0; b < builderCount; b++) {
                        saveImage("src/main/resources/image_db/" + b + "_hsb.png", bufferedImagesHSB[b], "png");
                        saveImage("src/main/resources/image_db/" + b + "_rgb.png", bufferedImagesRGB[b], "png");
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
