package in.nishikant_patil.parallelizer.helpers;

import in.nishikant_patil.parallelizer.contracts.Mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Helper to processes the data set as a stream and perform the operation over multiple threads.
 */
public final class StreamHelper extends Helper {
    @Override
    protected <U, T> List<Callable<List<U>>> getCallables(final List<T> dataSet, final Mapper<T, U> mapper) {
        List<Callable<List<U>>> callables = new ArrayList<>();
        final AtomicInteger index = new AtomicInteger(0);
        for (int i = 0; i != DEGREE_OF_PARALLELISM; ++i) {
            callables.add(new Callable<List<U>>() {
                @Override
                public List<U> call() throws Exception {
                    List<U> ret = new ArrayList<>();
                    while (index.get() < dataSet.size()) {
                        int localIndex = index.getAndIncrement();
                        if (localIndex < dataSet.size()) {
                            ret.addAll(mapper.map(Arrays.asList(dataSet.get(localIndex))));
                        }
                    }
                    return ret;
                }
            });
        }
        return callables;
    }
}
