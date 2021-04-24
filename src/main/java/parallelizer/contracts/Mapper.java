package parallelizer.contracts;

import java.util.List;

/**
 * This interface specifies the operation that will be performed on the input data set by multiple threads.
 */
public interface Mapper<T, U> {
    List<U> map(List<T> dataSet);
}
