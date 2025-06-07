package nic.com.Diplomka.service;

import nic.com.Diplomka.neuralNetwork.NeuralBuilder;
import nic.com.Diplomka.neuralNetwork.NeuralNetwork;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FavoriteService {
    private static final Path FAVORITES_DIR = Paths.get("src/main/resources/favorite_images");

    public FavoriteService() {
        try {
            if (!Files.exists(FAVORITES_DIR)) {
                Files.createDirectories(FAVORITES_DIR);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveFavorites(List<String> selectedImages) {
        if (selectedImages == null || selectedImages.isEmpty()) {
            return;
        }
        for (String img : selectedImages) {
            Path srcPath = resolveSource(img);
            if (Files.exists(srcPath)) {
                String timestamp = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                String fileName = srcPath.getFileName().toString();
                int dotIndex = fileName.lastIndexOf('.');
                String newName = dotIndex != -1
                        ? fileName.substring(0, dotIndex) + "_" + timestamp + fileName.substring(dotIndex)
                        : fileName + "_" + timestamp;
                Path dest = FAVORITES_DIR.resolve(newName);
                try {
                    Files.copy(srcPath, dest);
                    String builderName = newName.replaceAll("\\.png$", ".ser");
                    int idx = fileName.replaceAll("\\D", "").isEmpty() ? 0 : Integer.parseInt(fileName.replaceAll("\\D", ""));
                    NeuralBuilder builder = GeneratorService.neuralNetwork.getNeuralBuilder(idx);
                    NeuralNetwork.serializebleObject(builder, FAVORITES_DIR.resolve(builderName).toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Path resolveSource(String img) {
        Path srcPath = Paths.get(img);
        if (!Files.exists(srcPath)) {
            srcPath = Paths.get("src/main/resources").resolve(img);
        }
        if (!Files.exists(srcPath)) {
            srcPath = Paths.get("image").resolve(Paths.get(img).getFileName());
        }
        return srcPath;
    }

    public List<String> listFavorites() {
        if (!Files.exists(FAVORITES_DIR)) {
            return new ArrayList<>();
        }
        try (Stream<Path> stream = Files.list(FAVORITES_DIR)) {
            return stream.filter(p -> Files.isRegularFile(p) && p.getFileName().toString().endsWith(".png"))
                    .map(p -> "favorite_images/" + p.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void loadFavorite(String imageDir) {
        Path neuralPath = Paths.get("src/main/resources").resolve(imageDir.replace(".png", ".ser"));
        if (!Files.exists(neuralPath)) {
            neuralPath = Paths.get(imageDir).resolveSibling(neuralPath.getFileName());
        }
        if (Files.exists(neuralPath)) {
            NeuralBuilder builder = (NeuralBuilder) NeuralNetwork.deserializebleObject(neuralPath.toString());
            if (builder != null) {
                GeneratorService.neuralNetwork.setNeuralBuilder(0, builder);
                GeneratorService.neuralNetwork.evolute(0);
                GeneratorService.generateImages();
            }
        }
    }
}
