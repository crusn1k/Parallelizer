package in.nishikant_patil.parallelizer;

import in.nishikant_patil.parallelizer.contracts.Operation;
import in.nishikant_patil.parallelizer.helpers.ChunkHelper;
import in.nishikant_patil.parallelizer.helpers.StreamHelper;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * API for performing tasks on a data set over multiple threads.
 */
public class Parallelizer<T, U> {

    public List<U> parallelize(List<T> dataSet, Operation<T, U> operationToPerform) throws ExecutionException, InterruptedException {
        return parallelize(dataSet, operationToPerform, Mode.STREAM);
    }
    public List<U> parallelize(List<T> dataSet,  Operation<T, U> operationToPerform, Mode mode) throws ExecutionException, InterruptedException {
        switch (mode){
            case STREAM:
                return new StreamHelper().process(dataSet, operationToPerform);
            case CHUNK:
                return new ChunkHelper().process(dataSet, operationToPerform);
            default: throw new UnsupportedOperationException(mode + " is not yet supported.");
        }
    }

}
