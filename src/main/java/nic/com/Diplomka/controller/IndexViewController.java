package nic.com.Diplomka.controller;

import nic.com.Diplomka.service.GeneratorService;
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
            Path favDir = Paths.get("favorite_images");
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




}
