package nic.com.Diplomka.neuralNetwork;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

public class NeuralNetwork implements Serializable {
        private NeuralBuilder[] neuralBuilders;
        private Color rgbColor;
        private Color hsbColor;
        private ForkJoinPool forkJoinPool;

        public NeuralNetwork(int builderCount, int neuronCount, int firstInputCount) {
                neuralBuilders = new NeuralBuilder[builderCount];
                neuralBuilders[0] = new NeuralBuilder(neuronCount, firstInputCount);
                forkJoinPool = new ForkJoinPool(builderCount);
                evolute(0);
        }

        public NeuralBuilder getNeuralBuilder(int index) {
                return neuralBuilders[index];
        }

        public void setNeuralBuilder(int index, NeuralBuilder builder) {
                neuralBuilders[index] = builder;
        }

    public static void serializebleObject(Object obj, String fileName) {
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName));

            objectOutputStream.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (objectOutputStream != null) {
                try {
                    objectOutputStream.flush();
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Object deserializebleObject(String fileName) {
        ObjectInputStream objectInputStream = null;
        Object deserializebleObj = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(fileName));
            deserializebleObj = objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return deserializebleObj;
    }


    public Color getRgbColor(int index) {
        int[] returnIndexs = neuralBuilders[index].getReturnIndexs();
        List<Neuron> neuronList = neuralBuilders[index].getNeuronList();
        try {
            rgbColor = new Color(
                    neuronList.get(returnIndexs[0]).getOutputInPercent(),
                    neuronList.get(returnIndexs[1]).getOutputInPercent(),
                    neuronList.get(returnIndexs[2]).getOutputInPercent()
            );
        } catch (Exception ex) {
            neuronList.get(0);
        }
        return rgbColor;
    }

    public Color getHsbColor(int index) {
        int[] returnIndexs = neuralBuilders[index].getReturnIndexs();
        List<Neuron> neuronList = neuralBuilders[index].getNeuronList();
        hsbColor = Color.getHSBColor(
                neuronList.get(returnIndexs[0]).getOutputInPercent(),
                neuronList.get(returnIndexs[1]).getOutputInPercent(),
                neuronList.get(returnIndexs[2]).getOutputInPercent()
        );
        return hsbColor;
    }

        public void feedForward(double[] inputs) {
                try {
                        forkJoinPool.submit(() ->
                                IntStream.range(0, neuralBuilders.length).parallel()
                                        .forEach(i -> neuralBuilders[i].feedForward(inputs))
                        ).get();
                } catch (InterruptedException | ExecutionException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                }
        }

    public NeuralBuilder[] evolute(int index) {
        if (neuralBuilders[index].changed != null) {
            System.out.println(neuralBuilders[index].changed);
            System.out.println();
        }
        NeuralBuilder base = neuralBuilders[index];
        neuralBuilders[0] = base;
        for (int i = 1; i < neuralBuilders.length; i++) {
            String changed = "";
            NeuralBuilder cloneNB = base.copy();
            // slightly mutate the neural network
            /** Functions that randomly mutate the network
             * addNewRandomNeuron - 0;
             * removeRandomNeuron - 1;
             * removeRandomInputForRandomNeuron - 2;
             * changeWeightsForRandomNeuron - 3;
             * addNewOutputsForRandomNeuron - 4;
             * changeReturnRandomIndexs - 5;
             * changeActivationFunctinForRandomNeuron - 6
             */
            int mutationCount = new Random().nextInt(7) + 1; // number of mutations
            for (int mC = 0; mC < mutationCount; mC++) {
                switch (new Random().nextInt(7)) {
                    case 0:
                        cloneNB.addNewRandomNeuron();
                        changed += "0; ";
                        break;
                    case 1:
                        cloneNB.removeRandomNeuron();
                        changed += "1; ";
                        break;
                    case 2:
                        cloneNB.removeRandomInputForRandomNeuron();
                        changed += "2; ";
                        break;
                    case 3:
                        cloneNB.changeWeightsForRandomNeuron();
                        changed += "3; ";
                        break;
                    case 4:
                        cloneNB.addNewOutputsForRandomNeuron();
                        changed += "4; ";
                        break;
                    case 5:
                        cloneNB.changeReturnRandomIndexs();
                        changed += "5; ";
                        break;
                    case 6:
                        cloneNB.changeActivationFunctinForRandomNeuron();
                        changed += "6; ";
                        break;
                }
            }
            changed = changed.replaceAll("0", "Added a new random neuron");
            changed = changed.replaceAll("1", "Removed a random neuron");
            changed = changed.replaceAll("2", "Removed a random input for a random neuron");
            changed = changed.replaceAll("3", "Changed weights for a random neuron");
            changed = changed.replaceAll("4", "Added a random number of outputs for a random neuron");
            changed = changed.replaceAll("5", "Changed result indices");
            changed = changed.replaceAll("6", "Changed activation function for a neuron");
            changed = changed.replaceAll("; ", "\n");
            cloneNB.changed = changed;

            neuralBuilders[i] = cloneNB;
        }
        return neuralBuilders;
    }

}
