import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Random;

public class NeuralNetwork implements Serializable {
	private NeuralBuilder[] neuralBuilders;
	private Color rgbColor;
	private Color hsbColor;

	public NeuralNetwork(int builderCount, int neuronCount, int firstInputCount) {
		neuralBuilders = new NeuralBuilder[builderCount];
		neuralBuilders[0] = new NeuralBuilder(neuronCount, firstInputCount);
		evolute(0);
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
					neuronList.get(returnIndexs[0]).getOutputInProcent(),
					neuronList.get(returnIndexs[1]).getOutputInProcent(),
					neuronList.get(returnIndexs[2]).getOutputInProcent()
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
				neuronList.get(returnIndexs[0]).getOutputInProcent(),
				neuronList.get(returnIndexs[1]).getOutputInProcent(),
				neuronList.get(returnIndexs[2]).getOutputInProcent()
		);
		return hsbColor;
	}

	public void feedForward(double[] inputs) {
		for (int i = 0; i < neuralBuilders.length; i++) {
			neuralBuilders[i].feedForward(inputs);
		}
	}

	public NeuralBuilder[] evolute(int index) {
		System.out.println(neuralBuilders[index].changed);
		serializebleObject(neuralBuilders[index], "neuralBilder.obj");
		neuralBuilders[0] = neuralBuilders[index];
		for (int i = 1; i < neuralBuilders.length; i++) {
			String changed = "";
			NeuralBuilder cloneNB = (NeuralBuilder) deserializebleObject("neuralBilder.obj");
			// потрібно трохи змінити нейронну мережу
			/** Ф-ції, які рандомом змінюють нейронну мережу
			 * addNewRandomNeuron - 0;
			 * removeRandomNeuron - 1;
			 * removeRandomInputForRandomNeuron - 2;
			 * changeWeightsForRandomNeuron - 3;
			 * addNewOutputsForRandomNeuron - 4;
			 * changeReturnRandomIndexs - 5;
			 */
			int mutationCount = new Random().nextInt(7) + 1;// к-ть мутацій
			for (int mC = 0; mC < mutationCount; mC++) {
				switch (new Random().nextInt(6)) {
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
				}
			}
			changed = changed.replaceAll("0", "Додано новий рандомний нейрон");
			changed = changed.replaceAll("1", "Видалено рандомний нейрон");
			changed = changed.replaceAll("2", "Видалено рандомний вхід для рандомного нейрона");
			changed = changed.replaceAll("3", "Змінено ваги для рандомного нейрона");
			changed = changed.replaceAll("4", "Додано рандомну к-ть виходів для рандомного нейрона");
			changed = changed.replaceAll("5", "Змінено результуючі індекси");
			changed = changed.replaceAll("; ", "\n");
			cloneNB.changed = changed;

			neuralBuilders[i] = cloneNB;
		}
		return neuralBuilders;
	}

}
