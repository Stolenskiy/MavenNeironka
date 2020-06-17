package nic.com.Diplomka;

import nic.com.Diplomka.neuralNetwork.NeuralNetwork;
import nic.com.Diplomka.service.GeneratorService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.image.BufferedImage;

@SpringBootApplication
public class DiplomkaApplication {
    public static void main(String[] args) {
        GeneratorService.generateImages();
        SpringApplication.run(DiplomkaApplication.class);
    }

}
