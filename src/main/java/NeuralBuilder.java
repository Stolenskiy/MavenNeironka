import java.util.*;

public class NeuralBuilder {
	/**
	 * Клас повинен контролювати в який нейрон які входи повинні бути,
	 * а також змінювати кількість нейронів, і типи зв'язків між ними
	 */
	private List<Neuron> neuronList;
	private Map<Integer, Set<Integer>> inputIndexListByNeuronId;
	private Set<Integer> allIndexSet;

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
		// визначаю в яке місце нейронної мережі запхати нейрон
		int neuronId = 1;
//		int neuronId = new Random().nextInt(neuronList.size());
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
		/*for (int i = 0; i < neuronList.size(); i++) {
			if (neuronList.get(i).getId() == neuronId) {
				neuronList.add(i, neuron);
			}
		}
		*/

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

}
