package parallelizer.helpers;

import parallelizer.contracts.Mapper;
import parallelizer.contracts.Reducer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Abstract processor class to hold the generic functionality of performing a given operation on a data set over multiple
 * threads.
 */
public abstract class Processor {

    protected static final int DEGREE_OF_PARALLELISM = 4;
    protected ExecutorService executorService = Executors.newFixedThreadPool(DEGREE_OF_PARALLELISM);

    public <T, U, V> V process(List<T> dataSet, Mapper<T, U> mapper, Reducer<U, V> reducer) throws ExecutionException, InterruptedException {
        try {
            return reducer.reduce(collectProcessedData(executorService.invokeAll(getCallables(dataSet, mapper))));
        } finally {
            shutdown();
        }
    }

    private <U> List<List<U>> collectProcessedData(List<Future<List<U>>> futures) throws ExecutionException, InterruptedException {
        List<List<U>> data = new ArrayList<>();
        for (Future<List<U>> future : futures) {
            data.add(future.get());
        }
        return data;
    }

    protected abstract <U, T> List<Callable<List<U>>> getCallables(List<T> dataSet, Mapper<T, U> mapper);

    protected void shutdown() {
        executorService.shutdown();
    }
}
