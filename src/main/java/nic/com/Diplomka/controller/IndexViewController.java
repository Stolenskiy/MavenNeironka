package nic.com.Diplomka.controller;

import nic.com.Diplomka.service.GeneratorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


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


    private List<String> getImageList() {
        List<String> imageList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            imageList.add("image_db/" + i + "_hsb.png");
            imageList.add("image_db/" + i + "_rgb.png");
        }

        return imageList;
    }




}
