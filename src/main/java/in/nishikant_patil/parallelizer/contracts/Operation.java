package in.nishikant_patil.parallelizer.contracts;

import java.util.List;

/**
 * This interface must be implemented by all the operations that are passed in to the Parallelizer.
 */
public interface Operation<T, U> {
    List<U> process(List<T> dataSet);
}
