package nic.com.Diplomka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nic.com.Diplomka.neuralNetwork.NeuralBuilder;
import nic.com.Diplomka.service.GeneratorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Controller
public class EditNeuralController {

    @GetMapping("/editNeural")
    public String editNeural(@RequestParam("imageDir") String imageDir, Model model) throws JsonProcessingException {
        int index = Integer.parseInt(imageDir.replaceAll("\\D", ""));
        NeuralBuilder builder = GeneratorService.neuralNetwork.getNeuralBuilder(index);
        String dotGraph = builderToDot(builder);
        ObjectMapper mapper = new ObjectMapper();
        String builderJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(builder);
        model.addAttribute("imageDir", imageDir);
        model.addAttribute("builderIndex", index);
        model.addAttribute("dotGraph", dotGraph);
        model.addAttribute("builderJson", builderJson);
        return "edit-neural.html";
    }

    @PostMapping("/editNeural")
    public String updateNeural(@RequestParam("builderIndex") int index,
                               @RequestParam("builderJson") String builderJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        NeuralBuilder builder = mapper.readValue(builderJson, NeuralBuilder.class);
        GeneratorService.neuralNetwork.setNeuralBuilder(index, builder);
        GeneratorService.neuralNetwork.evolute(index);
        GeneratorService.generateImages();
        return "redirect:/editNeural?imageDir=image_db/" + index + "_hsb.png";
    }

    private String builderToDot(NeuralBuilder builder) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {");
        sb.append("rankdir=LR;");
        for (Integer idx : builder.getAllIndexSet()) {
            if (idx < 0) {
                sb.append("n").append(idx).append("[label=\"in").append(-idx).append("\"];\n");
            } else {
                sb.append("n").append(idx).append("[label=\"").append(idx).append("\"];\n");
            }
        }
        for (Map.Entry<Integer, Set<Integer>> e : builder.getInputIndexListByNeuronId().entrySet()) {
            for (Integer i : e.getValue()) {
                sb.append("n").append(i).append(" -> n").append(e.getKey()).append(";\n");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
