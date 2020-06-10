import java.util.*;

public class NeuralBuilder {
	/**
	 * Клас повинен контролювати в який нейрон які входи повинні бути,
	 * а також змінювати кількість нейронів, і типи зв'язків між ними
	 */
	private List<Neuron> neuronList;
	private Map<Integer, Set<Integer>> inputIndexListByNeuronId;

	public NeuralBuilder() {
	}

	public NeuralBuilder(int neuronCount, int firstInputCount) {
		neuronList = new ArrayList<>();

		// сет індексів включно із вхідними даними в нейронну мережу
		Set<Integer> indexList = new HashSet<>();
		inputIndexListByNeuronId = new HashMap<>();

		for (int i = 0; i < firstInputCount; i++) {
			indexList.add(-(i + 1));
		}

		// Будую рандомну структуру нейронної мережі

		for (int i = 0; i < neuronCount; i++) {
			// визначаю рандомні входи в нейрони
			int inputCountR = new Random(3).nextInt(indexList.size()) + 1; // к-ть входів в нейрон

			Set<Integer> inputIdexSet = new HashSet<>(); // індекси входів в нейрон

			// Клон потрібен щоб видаляти індекси які вже були вибрані при рандомі
			// для того, щоб не вибрати їх знову
			List<Integer> indexCopyList = new ArrayList<>(indexList);

			for (int j = 0; j < inputCountR; j++) {
				int i1 = new Random(3).nextInt(indexCopyList.size());
				inputIdexSet.add(indexCopyList.get(i1));
				indexCopyList.remove(i1);
			}

			// Визначивши к-ть входів, створюю нейрон
			/**
			 * inputIdexSet.size() - к-ть входів
			 * neuronList.size() - id нейрона
			 */
			Neuron neuron = new Neuron(inputIdexSet.size(), neuronList.size());
			neuronList.add(neuron);

			// Заповнюю мапу індексів по id
			inputIndexListByNeuronId.put(neuron.getId(), inputIdexSet);
			indexList.add(neuron.getId());
		}
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

	public List<Neuron> getNeuronList() {
		return neuronList;
	}

	public void addNewRandomNeuron() {
		// визначаю в яке місце нейронної мережі запхати нейрон
		int neuronId = new Random(3).nextInt(neuronList.size());

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
