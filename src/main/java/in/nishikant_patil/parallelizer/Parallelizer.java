package in.nishikant_patil.parallelizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Nishikant on 11/14/2015.
 */
public class Parallelizer<T, U> {
    private static final int DEGREE_OF_PARALLELISM=4;
    private ExecutorService executorService = Executors.newFixedThreadPool(DEGREE_OF_PARALLELISM);

    public List<U> parallelize(List<T> dataSet, Operation<T, U> operationToPerform) throws ExecutionException, InterruptedException {
        return parallelize(dataSet, operationToPerform, Mode.STREAM);
    }
    public List<U> parallelize(List<T> dataSet,  Operation<T, U> operationToPerform, Mode mode) throws ExecutionException, InterruptedException {
        switch (mode){
            case STREAM:
                return processAsStream(dataSet, operationToPerform);
            case CHUNK:
                return processAsChunks(dataSet, operationToPerform);
            default: throw new UnsupportedOperationException(mode + " is not yet supported.");
        }
    }

    private List<U> processAsChunks(final List<T> dataSet,  final Operation<T, U> operationToPerform) throws ExecutionException, InterruptedException {
        List<Callable<List<U>>> callables = getChunkedCallables(dataSet, operationToPerform);
        List<Future<List<U>>> futures = executorService.invokeAll(callables);
        return combineResults(futures);
    }

    private List<U> processAsStream(List<T> dataSet, Operation<T, U> operationToPerform) throws ExecutionException, InterruptedException {
        List<Callable<List<U>>> callables = getStreamedCallables(dataSet, operationToPerform);
        List<Future<List<U>>> futures = executorService.invokeAll(callables);
        return combineResults(futures);
    }

    private List<Callable<List<U>>> getChunkedCallables(final List<T> dataSet, final Operation<T, U> operationToPerform) {
        List<Callable<List<U>>> callables = new ArrayList<>();
        for(int i=0; i!=DEGREE_OF_PARALLELISM; ++i){
            final int chunkSize = (int) Math.ceil((dataSet.size()*1.0)/DEGREE_OF_PARALLELISM);
            final int startIndex = i*chunkSize;
            callables.add(new Callable<List<U>>() {
                @Override
                public List<U> call() throws Exception {
                    return operationToPerform.process(dataSet.subList(startIndex, Math.min(dataSet.size(), startIndex+chunkSize)));
                }
            });
        }
        return callables;
    }

    private List<Callable<List<U>>> getStreamedCallables(final List<T> dataSet, final Operation<T, U> operationToPerform) {
        List<Callable<List<U>>> callables = new ArrayList<>();
        final AtomicInteger index = new AtomicInteger(0);
        for(int i=0; i!=DEGREE_OF_PARALLELISM; ++i){
            callables.add(new Callable<List<U>>() {
                @Override
                public List<U> call() throws Exception {
                    List<U> ret = new ArrayList<>();
                    while(index.get()<dataSet.size()){
                        int localIndex = index.getAndIncrement();
                        if(localIndex<dataSet.size()){
                            ret.addAll(operationToPerform.process(Arrays.asList(dataSet.get(localIndex))));
                        }
                    }
                    return ret;
                }
            });
        }
        return callables;
    }

    private List<U> combineResults(List<Future<List<U>>> futures) throws InterruptedException, ExecutionException {
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

}
