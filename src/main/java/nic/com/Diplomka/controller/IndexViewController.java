package nic.com.Diplomka.controller;

import nic.com.Diplomka.service.GeneratorService;
import nic.com.Diplomka.neuralNetwork.NeuralBuilder;
import nic.com.Diplomka.neuralNetwork.NeuralNetwork;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class IndexViewController {
    @GetMapping("/")
    public String startPage (Model model) {
        model.addAttribute("imageList", getImageList());
        return "index.html";
    }
    @GetMapping("/selectedImage")
    public ModelAndView startPage(
            @RequestParam() String imageDir,
            ModelAndView modelAndView
    ) {
        int index = Integer.parseInt(imageDir.replaceAll("\\D", ""));
        GeneratorService.neuralNetwork.evolute(index);
        GeneratorService.generateImages();
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @PostMapping("/saveFavorites")
    public String saveFavorites(
            @RequestParam(value = "selectedImages", required = false) List<String> selectedImages
    ) {
        if (selectedImages != null && !selectedImages.isEmpty()) {
            Path favDir = Paths.get("src/main/resources/favorite_images");
            if (!Files.exists(favDir)) {
                try {
                    Files.createDirectories(favDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (String img : selectedImages) {
                Path srcPath = Paths.get(img);
                if (!Files.exists(srcPath)) {
                    srcPath = Paths.get("src/main/resources").resolve(img);
                }
                if (!Files.exists(srcPath)) {
                    srcPath = Paths.get("image").resolve(Paths.get(img).getFileName());
                }
                if (Files.exists(srcPath)) {
                    String timestamp = LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                    String fileName = srcPath.getFileName().toString();
                    int dotIndex = fileName.lastIndexOf('.');
                    String newName;
                    if (dotIndex != -1) {
                        newName = fileName.substring(0, dotIndex) + "_" + timestamp + fileName.substring(dotIndex);
                    } else {
                        newName = fileName + "_" + timestamp;
                    }
                    Path dest = favDir.resolve(newName);
                    try {
                        Files.copy(srcPath, dest);
                        String builderName = newName.replaceAll("\\.png$", ".ser");
                        int idx = fileName.replaceAll("\\D", "").isEmpty() ? 0 : Integer.parseInt(fileName.replaceAll("\\D", ""));
                        NeuralBuilder builder = GeneratorService.neuralNetwork.getNeuralBuilder(idx);
                        NeuralNetwork.serializebleObject(builder, favDir.resolve(builderName).toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "redirect:/";
    }


    private List<String> getImageList() {
        List<String> imageList = new ArrayList<>();
        for (int i = 0; i < GeneratorService.builderCount; i++) {
            imageList.add("image_db/" + i + "_hsb.png");
            imageList.add("image_db/" + i + "_rgb.png");
        }

        return imageList;
    }

    @GetMapping("/favorites")
    public String favoritesPage(Model model) {
        model.addAttribute("favoriteImages", getFavoriteImageList());
        return "favorites.html";
    }

    @GetMapping("/favoriteSelectedImage")
    public ModelAndView favoriteSelectedImage(
            @RequestParam() String imageDir,
            ModelAndView modelAndView
    ) {
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
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    private List<String> getFavoriteImageList() {
        Path dir = Paths.get("src/main/resources/favorite_images");
        if (!Files.exists(dir)) {
            return new ArrayList<>();
        }
        try (Stream<Path> stream = Files.list(dir)) {
            return stream
                    .filter(p -> Files.isRegularFile(p) && p.getFileName().toString().endsWith(".png"))
                    .map(p -> "favorite_images/" + p.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }




}
