package nic.com.Diplomka.neuralNetwork;

import nic.com.Diplomka.enums.ActivationFunction;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class NeuralBuilder implements Serializable {
	String changed;
        /**
         * This class controls which inputs connect to which neurons and can
         * modify the number of neurons and the types of connections between them.
         */
	private List<Neuron> neuronList;
	private Map<Integer, Set<Integer>> inputIndexListByNeuronId;
	private Set<Integer> allIndexSet;
	private int[] returnIndexs;


        public NeuralBuilder() {
        }

        public NeuralBuilder(NeuralBuilder other) {
                this.changed = other.changed;
                this.neuronList = new ArrayList<>();
                for (Neuron n : other.neuronList) {
                        this.neuronList.add(n.copy());
                }
                this.inputIndexListByNeuronId = new HashMap<>();
                for (Map.Entry<Integer, Set<Integer>> e : other.inputIndexListByNeuronId.entrySet()) {
                        this.inputIndexListByNeuronId.put(e.getKey(), new HashSet<>(e.getValue()));
                }
                this.allIndexSet = new HashSet<>(other.allIndexSet);
                this.returnIndexs = other.returnIndexs != null ? Arrays.copyOf(other.returnIndexs, other.returnIndexs.length) : null;
        }

        public NeuralBuilder copy() {
                return new NeuralBuilder(this);
        }

	public NeuralBuilder(int neuronCount, int firstInputCount) {
		neuronList = new ArrayList<>();

                // index set including the initial network inputs
                allIndexSet = new HashSet<>();
                inputIndexListByNeuronId = new HashMap<>();

		for (int i = 0; i < firstInputCount; i++) {
			allIndexSet.add(-(i + 1));
		}

                // Build a random neural network structure

		for (int i = 0; i < neuronCount; i++) {
                        // pick random inputs for the neuron
                        Set<Integer> inputIndexSet = getRandomInputIndexSetForNeuron(allIndexSet);


                        // create a neuron with the chosen number of inputs
                        /**
                         * inputIndexSet.size() - number of inputs
                         * neuronList.size() - neuron id
                         */
                        Neuron neuron = new Neuron(inputIndexSet.size(), neuronList.size());
                        neuronList.add(neuron);

                        // store indices by neuron id
                        inputIndexListByNeuronId.put(neuron.getId(), inputIndexSet);
			allIndexSet.add(neuron.getId());
		}
	}

        private Set<Integer> getRandomInputIndexSetForNeuron(Set<Integer> indexSet) { // choose random inputs for a neuron

                int inputCountR = new Random().nextInt(indexSet.size()) + 1; // number of inputs for the neuron

                Set<Integer> inputIndexSet = new HashSet<>(); // indices of neuron inputs

                // clone list so that already selected indices can be removed and not reused
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
                        // get input indices for the neuron
			Set<Integer> indexSet = inputIndexListByNeuronId.get(neuron.getId());

			double[] neuronInputs = new double[indexSet.size()];
			int i = 0;
			for (Integer index : indexSet) {
				if (index < 0) {
                                        // use original input when the source is not another neuron
					neuronInputs[i] = inputs[-(index + 1)];
				} else {
                                        // use the output of another neuron as input
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

                // determine where to insert the new neuron
                // indices of inputs for the new neuron
		Set<Integer> inputIndexSet = new HashSet<>();

                // move connections from neurons located before the new neuron
		for (Integer key : inputIndexListByNeuronId.keySet()) {
			if (key < neuronId) {
				newInputIndexListByNeuronId.put(Integer.valueOf(key), new HashSet<>(inputIndexListByNeuronId.get(key)));
			}
		}
                // copy indices of neurons that come before the new one
		for (Integer integer : allIndexSet) {
			if (integer < neuronId) {
				inputIndexSet.add(Integer.valueOf(integer));
			}
		}

                inputIndexSet = getRandomInputIndexSetForNeuron(inputIndexSet); // choose random inputs for the new neuron

		Neuron neuron = new Neuron(inputIndexSet.size(), neuronId);
                // insert the neuron into the list
		neuronList.add(neuronId, neuron);

                // shift indices of other neurons
		for (Integer key : inputIndexListByNeuronId.keySet()) {

			if (key >= neuronId) {
				if (key == neuronId) {
					newInputIndexListByNeuronId.put(neuron.getId(), inputIndexSet);
				}
				indexMapIncrement(newInputIndexListByNeuronId, neuronId, key);
			}

		}

                // update ids of other neurons
		for (int i = neuronId + 1; i < neuronList.size(); i++) {
			Neuron neuronI = neuronList.get(i);
			neuronI.setId(neuronI.getId() + 1);
		}
		inputIndexListByNeuronId = newInputIndexListByNeuronId;

                // create output connections for the new neuron
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
                // decrease ids of subsequent neurons
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
                        // convert set to list to access elements by index
                        List<Integer> inputs = new ArrayList<>(inputIndexSet);
                        int removeIdx = new Random().nextInt(inputs.size());
                        Integer value = inputs.get(removeIdx);
                        // remove the chosen element from the original set
                        inputIndexSet.remove(value);
                        // store updated set back to the map
                        inputIndexListByNeuronId.put(neuronId, inputIndexSet);
                }
        }

	public void changeWeightsForRandomNeuron() {
		int neuronId = new Random().nextInt(neuronList.size());
		Neuron neuron = neuronList.get(neuronId);
		double[] weights = neuron.getWeights();

		List<Integer> weightIndexList = new ArrayList<>();

                // number of weights to adjust
		int changeCount = new Random().nextInt(weights.length);

		for (int i = 0; i < weights.length; i++) {
			weightIndexList.add(i);
		}

		for (int i = 0; i < changeCount; i++) {
			int index = weightIndexList.get(new Random().nextInt(weightIndexList.size()));

                        // coefficient by which the weight will change [-0.5; 0.5]
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
                // add new outputs from one neuron to others
                if (neuronList.size() - (neuronId + 1) <= 0) {
                        // no neurons available to accept this neuron's output
                        return;
                }

		//
		int outputCount = new Random().nextInt(neuronList.size() - (neuronId + 1)) + 1;

                // list of neuron indices that can accept an output from this neuron
		List<Integer> inputNeuronIndexs = new ArrayList<>();

		for (int i = neuronId + 1; i < neuronList.size(); i++) {
			inputNeuronIndexs.add(i);
		}

		for (int i = 0; i < outputCount; i++) {
			int index = new Random().nextInt(inputNeuronIndexs.size());
			Neuron neuron = neuronList.get(inputNeuronIndexs.get(index));
                        // increase the input count of a random neuron
			List<Double> inputList = Arrays.stream(neuron.getInputs())
					.boxed()
					.collect(Collectors.toList());
			inputList.add(1d);
			double[] newInputs = new double[inputList.size()];
			for (int nI = 0; nI < newInputs.length; nI++) {
				newInputs[nI] = inputList.get(nI);
			}
			neuron.setInputs(newInputs);
                        // increase the weight count of the neuron
			List<Double> weightsList = Arrays.stream(neuron.getWeights())
					.boxed()
					.collect(Collectors.toList());
			weightsList.add(new Random().nextDouble());
			double[] newWeights = new double[weightsList.size()];
			for (int nI = 0; nI < newWeights.length; nI++) {
				newWeights[nI] = weightsList.get(nI);
			}
			neuron.setWeights(newWeights);

                        // remember the new input in the random neuron
			inputIndexListByNeuronId.get(inputNeuronIndexs.get(index)).add(neuronId);

                        // remove this neuron index from available indices to avoid duplicates
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
                        // indices may repeat!
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
