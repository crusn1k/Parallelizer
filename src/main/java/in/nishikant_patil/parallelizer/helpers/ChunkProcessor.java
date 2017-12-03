package in.nishikant_patil.parallelizer.helpers;

import in.nishikant_patil.parallelizer.contracts.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Processor to processes the data set in chunks. The data list will be split into equal chunks and the operation will be
 * performed by multiple threads on these chunks. The operation should not have dependency between the data in different
 * chunks.
 */
public final class ChunkProcessor extends Processor {
    @Override
    protected <U, T> List<Callable<List<U>>> getCallables(final List<T> dataSet, final Mapper<T, U> mapper) {
        List<Callable<List<U>>> callables = new ArrayList<>();
        for (int i = 0; i != DEGREE_OF_PARALLELISM; ++i) {
            final int chunkSize = (int) Math.ceil((dataSet.size() * 1.0) / DEGREE_OF_PARALLELISM);
            final int startIndex = i * chunkSize;
            callables.add(new Callable<List<U>>() {
                @Override
                public List<U> call() {
                    return mapper.map(dataSet.subList(startIndex, Math.min(dataSet.size(), startIndex + chunkSize)));
                }
            });
        }
        return callables;
    }
}
