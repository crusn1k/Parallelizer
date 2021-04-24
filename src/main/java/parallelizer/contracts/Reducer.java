package parallelizer.contracts;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * This interface specifies the operation that will be performed on the output of mapper to generate the desired output for the client.
 */
public interface Reducer<T, U> {
    U reduce(List<List<T>> data) throws ExecutionException, InterruptedException;
}
