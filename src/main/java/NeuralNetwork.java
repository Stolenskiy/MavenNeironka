import java.awt.*;
import java.io.*;

public class NeuralNetwork implements Serializable {
	private NeuralBuilder neuralBuilder;
	private Color rgbColor;
	private Color hsbColor;

	public NeuralNetwork(int neuronCount, int firstInputCount) {
		this.neuralBuilder = new NeuralBuilder(neuronCount, firstInputCount);
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


}
