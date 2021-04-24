package parallelizer;

import parallelizer.contracts.Mapper;
import parallelizer.contracts.Reducer;
import parallelizer.helpers.ChunkProcessor;
import parallelizer.helpers.StreamProcessor;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * API for performing tasks on a data set over multiple threads.
 */
public class Parallelizer {
    public <T, U, V> V parallelize(List<T> dataSet, Mapper<T, U> mapper, Reducer<U, V> reducer) throws ExecutionException, InterruptedException {
        return parallelize(dataSet, mapper, reducer, Mode.STREAM);
    }

    public <T, U, V> V parallelize(List<T> dataSet, Mapper<T, U> mapper, Reducer<U, V> reducer, Mode mode) throws ExecutionException, InterruptedException {
        return switch (mode) {
            case STREAM -> new StreamProcessor().process(dataSet, mapper, reducer);
            case CHUNK -> new ChunkProcessor().process(dataSet, mapper, reducer);
        };
    }
}
