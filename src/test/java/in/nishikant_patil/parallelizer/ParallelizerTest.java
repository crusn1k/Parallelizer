package in.nishikant_patil.parallelizer;

import in.nishikant_patil.parallelizer.contracts.Mapper;
import in.nishikant_patil.parallelizer.contracts.Reducer;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ParallelizerTest {
    private Parallelizer parallelizer = new Parallelizer();
    private Mapper<Integer, Integer> mapper = new Mapper<Integer, Integer>() {
        @Override
        public List<Integer> map(List<Integer> dataSet) {
            List<Integer> ret = new ArrayList<>();
            for(Integer item : dataSet){
                ret.add(item * 10);
            }
            return ret;
        }
    };

    private Reducer<Integer, Long> reducer_1 = new Reducer<Integer, Long>() {
        @Override
        public Long reduce(List<List<Integer>> data) throws ExecutionException, InterruptedException {
            List<Integer> processedData = new ArrayList<>();
            for(List<Integer> integers : data){
                processedData.addAll(integers);
            }
            long sum = 0;
            for(Integer integer : processedData){
                sum += integer;
            }
            return sum;
        }
    };

    private Reducer<Integer, Double> reducer_2 = new Reducer<Integer, Double>() {
        @Override
        public Double reduce(List<List<Integer>> data) throws ExecutionException, InterruptedException {
            List<Integer> processedData = new ArrayList<>();
            for(List<Integer> integers : data){
                processedData.addAll(integers);
            }
            double sum = 0;
            for(Integer integer : processedData){
                sum += integer;
            }
            return sum/processedData.size();
        }
    };

    @Test
    public void testChunkMode() throws ExecutionException, InterruptedException {
        long sum = parallelizer.parallelize(Arrays.asList(1, 2, 3, 4), mapper, reducer_1, Mode.CHUNK);
        Assert.assertEquals(100, sum);
    }

    @Test
    public void testStreamMode() throws ExecutionException, InterruptedException {
        long sum = parallelizer.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), mapper, reducer_1, Mode.STREAM);
        Assert.assertEquals(360, sum);
    }

    @Test
    public void testDefaultMode() throws ExecutionException, InterruptedException {
        Double avg = parallelizer.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6), mapper, reducer_2);
        Assert.assertEquals(new BigDecimal("35.0"), new BigDecimal(avg.toString()));
    }


}
