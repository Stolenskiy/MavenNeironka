package nic.com.Diplomka.neuralNetwork;

import nic.com.Diplomka.enums.ActivationFunction;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class NeuralBuilder implements Serializable {
	String changed;
	/**
	 * Клас повинен контролювати в який нейрон які входи повинні бути,
	 * а також змінювати кількість нейронів, і типи зв'язків між ними
	 */
	private List<Neuron> neuronList;
	private Map<Integer, Set<Integer>> inputIndexListByNeuronId;
	private Set<Integer> allIndexSet;
	private int[] returnIndexs;


	public NeuralBuilder() {
	}

	public NeuralBuilder(int neuronCount, int firstInputCount) {
		neuronList = new ArrayList<>();

		// сет індексів включно із вхідними даними в нейронну мережу
		allIndexSet = new HashSet<>();
		inputIndexListByNeuronId = new HashMap<>();

		for (int i = 0; i < firstInputCount; i++) {
			allIndexSet.add(-(i + 1));
		}

		// Будую рандомну структуру нейронної мережі

		for (int i = 0; i < neuronCount; i++) {
			// визначаю рандомні входи в нейрони
			Set<Integer> inputIndexSet = getRandomInputIndexSetForNeuron(allIndexSet);


			// Визначивши к-ть входів, створюю нейрон
			/**
			 * inputIndexSet.size() - к-ть входів
			 * neuronList.size() - id нейрона
			 */
			Neuron neuron = new Neuron(inputIndexSet.size(), neuronList.size());
			neuronList.add(neuron);

			// Заповнюю мапу індексів по id
			inputIndexListByNeuronId.put(neuron.getId(), inputIndexSet);
			allIndexSet.add(neuron.getId());
		}
	}

	private Set<Integer> getRandomInputIndexSetForNeuron(Set<Integer> indexSet) { // визначаю рандомні входи в нейрони

		int inputCountR = new Random().nextInt(indexSet.size()) + 1; // к-ть входів в нейрон

		Set<Integer> inputIndexSet = new HashSet<>(); // індекси входів в нейрон

		// Клон потрібен щоб видаляти індекси які вже були вибрані при рандомі
		// для того, щоб не вибрати їх знову
		List<Integer> indexCopyList = new ArrayList<>(indexSet);

		for (int j = 0; j < inputCountR; j++) {
			int i1 = new Random().nextInt(indexCopyList.size());
			inputIndexSet.add(indexCopyList.get(i1));
			indexCopyList.remove(i1);
		}
		return inputIndexSet;
	}

	public void feedForward(double inputs[]) {
		for (Neuron neuron : neuronList) {
			//отримую індекси входів для нейрона
			Set<Integer> indexSet = inputIndexListByNeuronId.get(neuron.getId());

			double[] neuronInputs = new double[indexSet.size()];
			int i = 0;
			for (Integer index : indexSet) {
				if (index < 0) {
					// якщо на вхід передається не вихід іншого нейрона, а початковий вхідний сигнал(x, y, d)
					neuronInputs[i] = inputs[-(index + 1)];
				} else {
					// якщо на вхід передається вихід іншого нейрона
					neuronInputs[i] = neuronList.get(index).getOutput();
				}
				i++;
			}
			neuron.setInputs(neuronInputs);
			neuron.calculateOutput();

		}

	}

	public void addNewRandomNeuron() {
		Map<Integer, Set<Integer>> newInputIndexListByNeuronId = new HashMap<>();
		int neuronId = new Random().nextInt(neuronList.size());

		// визначаю в яке місце нейронної мережі запхати нейрон
		// індекси входів в новий нейрон
		Set<Integer> inputIndexSet = new HashSet<>();

		// Переношу звязки нейронів із індаксами, котрі були до нового нейрона
		for (Integer key : inputIndexListByNeuronId.keySet()) {
			if (key < neuronId) {
				newInputIndexListByNeuronId.put(Integer.valueOf(key), new HashSet<>(inputIndexListByNeuronId.get(key)));
			}
		}
		// Копіюю індекси нейронів, котрі знаходяться перед новим
		for (Integer integer : allIndexSet) {
			if (integer < neuronId) {
				inputIndexSet.add(Integer.valueOf(integer));
			}
		}

		inputIndexSet = getRandomInputIndexSetForNeuron(inputIndexSet); // визначаю рандомні входи в новий нейрон

		Neuron neuron = new Neuron(inputIndexSet.size(), neuronId);
		// ставлю нейрон на його місце в списку нейронів
		neuronList.add(neuronId, neuron);

		// зсуваю індекси інших нейронів
		for (Integer key : inputIndexListByNeuronId.keySet()) {

			if (key >= neuronId) {
				if (key == neuronId) {
					newInputIndexListByNeuronId.put(neuron.getId(), inputIndexSet);
				}
				indexMapIncrement(newInputIndexListByNeuronId, neuronId, key);
			}

		}

		// змінюю id інших нейронів
		for (int i = neuronId + 1; i < neuronList.size(); i++) {
			Neuron neuronI = neuronList.get(i);
			neuronI.setId(neuronI.getId() + 1);
		}
		inputIndexListByNeuronId = newInputIndexListByNeuronId;

		// створюю звязки із виходами даного нейрона
		addNewOutputsForNeuron(neuronId);

		calculateAllIndexSet();
		if (returnIndexs != null) {
			for (int i = 0; i < returnIndexs.length; i++) {
				if (returnIndexs[i] >= neuronId) {
					returnIndexs[i] = returnIndexs[i] + 1;
				}
			}
		}
	}

	public void removeRandomNeuron() {
		if (neuronList.size() < 2) {
			return;
		}
		Map<Integer, Set<Integer>> newInputIndexListByNeuronId = new HashMap<>();
		int neuronId = new Random().nextInt(neuronList.size());

		for (Integer key : inputIndexListByNeuronId.keySet()) {
			if (key < neuronId) {
				newInputIndexListByNeuronId.put(Integer.valueOf(key), new HashSet<>(inputIndexListByNeuronId.get(key)));
			} else if (key > neuronId) {
				HashSet<Integer> indexSet = new HashSet<>();
				for (Integer index : inputIndexListByNeuronId.get(key)) {
					if (index > neuronId) {
						indexSet.add(index - 1);
					} else if (index < neuronId) {
						indexSet.add(index);
					}
				}
				newInputIndexListByNeuronId.put(Integer.valueOf(key - 1), new HashSet<>(indexSet));
			}
		}
		allIndexSet.remove(neuronId);
		neuronList.remove(neuronId);
		// зменшую id інших нейронів
		for (int i = neuronId; i < neuronList.size(); i++) {
			Neuron neuron = neuronList.get(i);
			neuron.setId(neuron.getId() - 1);
		}

		inputIndexListByNeuronId = newInputIndexListByNeuronId;
		calculateAllIndexSet();
		if (returnIndexs != null) {
			for (int i = 0; i < returnIndexs.length; i++) {
				if (returnIndexs[i] >= neuronId) {
					if (returnIndexs[i] > 0) {
						returnIndexs[i] = returnIndexs[i] - 1;
					}
				}
			}
		}
	}

	public void removeRandomInputForRandomNeuron() {
		int neuronId = new Random().nextInt(neuronList.size());
		Set<Integer> inputIndexSet = inputIndexListByNeuronId.get(neuronId);
		if (inputIndexSet.size() > 0) {
			inputIndexSet.remove(new Random().nextInt(inputIndexSet.size()));
			inputIndexListByNeuronId.put(neuronId, inputIndexSet);
		}
	}

	public void changeWeightsForRandomNeuron() {
		int neuronId = new Random().nextInt(neuronList.size());
		Neuron neuron = neuronList.get(neuronId);
		double[] weights = neuron.getWeights();

		List<Integer> weightIndexList = new ArrayList<>();

		//к-ть ваг, які буде відкореговано
		int changeCount = new Random().nextInt(weights.length);

		for (int i = 0; i < weights.length; i++) {
			weightIndexList.add(i);
		}

		for (int i = 0; i < changeCount; i++) {
			int index = weightIndexList.get(new Random().nextInt(weightIndexList.size()));

			// коефіцієнт на який буде змінено вагу [-0.5; 0.5]
			double delta = new Random().nextDouble() - 0.5;

			weights[index] += delta;

			weightIndexList.remove(Integer.valueOf(index));
		}

		neuron.setWeights(weights);
	}

	public void addNewOutputsForRandomNeuron() {
		if (neuronList.size() - 2 <= 0) return;
		addNewOutputsForNeuron(new Random().nextInt(neuronList.size() - 2));
	}

	public void addNewOutputsForNeuron(int neuronId) {
		// ф-ція створить нові виходи від одного нейрона до інших
		if (neuronList.size() - (neuronId + 1) <= 0) {
			// немає нейронів котрі моглиб прийняти вихід цього нейрона
			return;
		}

		//
		int outputCount = new Random().nextInt(neuronList.size() - (neuronId + 1)) + 1;

		// список можливих індексів в нейрон в котрі можна запхнути вихід даного нейрона
		List<Integer> inputNeuronIndexs = new ArrayList<>();

		for (int i = neuronId + 1; i < neuronList.size(); i++) {
			inputNeuronIndexs.add(i);
		}

		for (int i = 0; i < outputCount; i++) {
			int index = new Random().nextInt(inputNeuronIndexs.size());
			Neuron neuron = neuronList.get(inputNeuronIndexs.get(index));
			// збільшою к-ть входів в рандомний нейрон
			List<Double> inputList = Arrays.stream(neuron.getInputs())
					.boxed()
					.collect(Collectors.toList());
			inputList.add(1d);
			double[] newInputs = new double[inputList.size()];
			for (int nI = 0; nI < newInputs.length; nI++) {
				newInputs[nI] = inputList.get(nI);
			}
			neuron.setInputs(newInputs);
			// збільшую к-ть ваг нейрона
			List<Double> weightsList = Arrays.stream(neuron.getWeights())
					.boxed()
					.collect(Collectors.toList());
			weightsList.add(new Random().nextDouble());
			double[] newWeights = new double[weightsList.size()];
			for (int nI = 0; nI < newWeights.length; nI++) {
				newWeights[nI] = weightsList.get(nI);
			}
			neuron.setWeights(newWeights);

			// запам'ятовую новий вхід в рандомний нейрон
			inputIndexListByNeuronId.get(inputNeuronIndexs.get(index)).add(neuronId);

			// видаляю даний індекс нейрона із можливих індексів для призначення вихода, уникаю повторень
			inputNeuronIndexs.remove(index);
		}

	}

	public void changeReturnRandomIndexs() {
		returnIndexs = null;
		returnIndexs = getReturnIndexs();
	}

	public void changeActivationFunctinForRandomNeuron() {
		int neuronId = new Random().nextInt(neuronList.size());

		neuronList.get(neuronId).setActivationFunction(ActivationFunction.getRandomActivationFunction());
	}

	public int[] getReturnIndexs() {
		if (returnIndexs == null) {
			// індекси можуть повторюватись!
			returnIndexs = new int[3];
			returnIndexs[0] = neuronList.size() - 1;
			int index;
			if (neuronList.size() - 1 > 0) {
				index = new Random().nextInt(neuronList.size() - 1);
			} else {
				index = 0;
			}
			returnIndexs[1] = index;
			if (neuronList.size() - 1 > 0) {
				index = new Random().nextInt(neuronList.size() - 1);
			} else {
				index = 0;
			}
			returnIndexs[2] = index;
			returnIndexs = randArr(returnIndexs);
		}
		return returnIndexs;
	}

	private void indexMapIncrement(Map<Integer, Set<Integer>> newInputIndexListByNeuronId, int neuronId, int key) {
		HashSet<Integer> indexSet = new HashSet<>();
		for (Integer index : inputIndexListByNeuronId.get(key)) {
			if (index >= neuronId) {
				indexSet.add(index + 1);
			} else {
				indexSet.add(index);
			}
		}
		newInputIndexListByNeuronId.put(Integer.valueOf(key + 1), new HashSet<>(indexSet));
	}

	private void calculateAllIndexSet() {
		Set<Integer> newIndexSet = new HashSet<>();

		for (Integer integer : allIndexSet) {
			if (integer < 0) newIndexSet.add(integer);
		}

		for (Neuron neuron : neuronList) {
			newIndexSet.add(neuron.getId());
		}
		allIndexSet = newIndexSet;
	}

	private int[] randArr(int[] arr) {
		int[] returnArr = new int[arr.length];
		List<Integer> indexList = new ArrayList();
		for (int i = 0; i < arr.length; i++) {
			indexList.add(i);
		}

		for (int i = 0; i < arr.length; i++) {
			int i1 = new Random().nextInt(indexList.size());
			returnArr[i] = arr[indexList.get(i1)];
			indexList.remove(i1);
		}

		return returnArr;
	}


	public List<Neuron> getNeuronList() {
		return neuronList;
	}

	public void setNeuronList(List<Neuron> neuronList) {
		this.neuronList = neuronList;
	}

	public Map<Integer, Set<Integer>> getInputIndexListByNeuronId() {
		return inputIndexListByNeuronId;
	}

	public void setInputIndexListByNeuronId(Map<Integer, Set<Integer>> inputIndexListByNeuronId) {
		this.inputIndexListByNeuronId = inputIndexListByNeuronId;
	}

	public Set<Integer> getAllIndexSet() {
		return allIndexSet;
	}

	public void setAllIndexSet(Set<Integer> allIndexSet) {
		this.allIndexSet = allIndexSet;
	}

}
