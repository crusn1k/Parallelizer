package in.nishikant_patil.parallelizer;

import in.nishikant_patil.parallelizer.contracts.Mapper;
import in.nishikant_patil.parallelizer.contracts.Reducer;
import in.nishikant_patil.parallelizer.helpers.ChunkHelper;
import in.nishikant_patil.parallelizer.helpers.StreamHelper;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * API for performing tasks on a data set over multiple threads.
 */
public class Parallelizer {

    public <T, U, V> V parallelize(List<T> dataSet, Mapper<T, U> mapper, Reducer<U, V> reducer) throws ExecutionException, InterruptedException {
        return parallelize(dataSet, mapper, reducer, Mode.STREAM);
    }
    public <T, U, V> V parallelize(List<T> dataSet,  Mapper<T, U> mapper, Reducer<U, V> reducer, Mode mode) throws ExecutionException, InterruptedException {
        switch (mode){
            case STREAM:
                return new StreamHelper().process(dataSet, mapper, reducer);
            case CHUNK:
                return new ChunkHelper().process(dataSet, mapper, reducer);
            default: throw new UnsupportedOperationException(mode + " is not yet supported.");
        }
    }

}
