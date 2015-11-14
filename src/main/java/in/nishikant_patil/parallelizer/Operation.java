package in.nishikant_patil.parallelizer;

import java.util.List;

/**
 * Created by Nishikant on 11/14/2015.
 */
public interface Operation<T, U> {
    List<U> process(List<T> dataSet);
}
