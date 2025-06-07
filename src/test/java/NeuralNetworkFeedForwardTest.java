import nic.com.Diplomka.neuralNetwork.NeuralNetwork;
import nic.com.Diplomka.neuralNetwork.NeuralBuilder;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class NeuralNetworkFeedForwardTest {
    static class CountingNeuralBuilder extends NeuralBuilder {
        static AtomicInteger count = new AtomicInteger();
        static Set<Long> threads = new ConcurrentSkipListSet<>();
        @Override
        public void feedForward(double[] inputs) {
            threads.add(Thread.currentThread().getId());
            count.incrementAndGet();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Test
    void testFeedForwardRunsParallel() throws Exception {
        int builders = 4;
        NeuralNetwork network = new NeuralNetwork(builders, 1, 1);
        CountingNeuralBuilder[] arr = new CountingNeuralBuilder[builders];
        for (int i = 0; i < builders; i++) {
            arr[i] = new CountingNeuralBuilder();
        }
        Field f = NeuralNetwork.class.getDeclaredField("neuralBuilders");
        f.setAccessible(true);
        f.set(network, arr);
        Field poolField = NeuralNetwork.class.getDeclaredField("forkJoinPool");
        poolField.setAccessible(true);
        poolField.set(network, new ForkJoinPool(builders));

        network.feedForward(new double[]{1.0});

        assertEquals(builders, CountingNeuralBuilder.count.get());
        assertTrue(CountingNeuralBuilder.threads.size() > 1);
    }
}
