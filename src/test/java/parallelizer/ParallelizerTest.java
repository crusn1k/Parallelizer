package parallelizer;

import org.junit.jupiter.api.Test;
import parallelizer.contracts.Mapper;
import parallelizer.contracts.Reducer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParallelizerTest {
    private final Parallelizer parallelizer = new Parallelizer();
    private final Mapper<Integer, Integer> mapper = dataSet -> {
        List<Integer> ret = new ArrayList<>();
        for (Integer item : dataSet) {
            ret.add(item * 10);
        }
        return ret;
    };

    private final Reducer<Integer, Long> reducer_1 = data -> {
        List<Integer> processedData = new ArrayList<>();
        for (List<Integer> integers : data) {
            processedData.addAll(integers);
        }
        long sum = 0;
        for (Integer integer : processedData) {
            sum += integer;
        }
        return sum;
    };

    private final Reducer<Integer, Double> reducer_2 = data -> {
        List<Integer> processedData = new ArrayList<>();
        for (List<Integer> integers : data) {
            processedData.addAll(integers);
        }
        double sum = 0;
        for (Integer integer : processedData) {
            sum += integer;
        }
        return sum / processedData.size();
    };

    @Test
    public void testChunkMode() throws ExecutionException, InterruptedException {
        long sum = parallelizer.parallelize(Arrays.asList(1, 2, 3, 4), mapper, reducer_1, Mode.CHUNK);
        assertEquals(100, sum);
    }

    @Test
    public void testStreamMode() throws ExecutionException, InterruptedException {
        long sum = parallelizer.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), mapper, reducer_1, Mode.STREAM);
        assertEquals(360, sum);
    }

    @Test
    public void testDefaultMode() throws ExecutionException, InterruptedException {
        Double avg = parallelizer.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6), mapper, reducer_2);
        assertEquals(new BigDecimal("35.0"), new BigDecimal(avg.toString()));
    }
}
