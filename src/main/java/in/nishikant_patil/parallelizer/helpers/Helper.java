package in.nishikant_patil.parallelizer.helpers;

import in.nishikant_patil.parallelizer.contracts.Operation;

import java.util.List;
import java.util.concurrent.*;

/**
 * Abstract helper class to hold the generic functionality of performing a given operation on a data set over multiple
 * threads.
 */
public abstract class Helper {

    protected static final int DEGREE_OF_PARALLELISM=4;
    protected ExecutorService executorService = Executors.newFixedThreadPool(DEGREE_OF_PARALLELISM);

    public <T, U> List<U> process(List<T> dataSet, Operation<T, U> operationToPerform) throws ExecutionException, InterruptedException {
        List<Callable<List<U>>> callables = getCallables(dataSet, operationToPerform);
        List<Future<List<U>>> futures = executorService.invokeAll(callables);
        List<U> results = combineResults(futures);
        shutdown();
        return results;
    }

    protected abstract <U, T> List<Callable<List<U>>> getCallables(List<T> dataSet, Operation<T, U> operationToPerform);


    protected <U> List<U> combineResults(List<Future<List<U>>> futures) throws InterruptedException, ExecutionException {
        List<U> ret = null;
        for(Future<List<U>> future : futures){
            if(null==ret){
                ret = future.get();
            } else {
                ret.addAll(future.get());
            }
        }
        return ret;
    }

    protected void shutdown(){
        executorService.shutdown();
    }
}
