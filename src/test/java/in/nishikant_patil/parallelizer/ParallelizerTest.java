package in.nishikant_patil.parallelizer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Nishikant on 11/14/2015.
 */
public class ParallelizerTest {
    Parallelizer<Integer, Integer> parallelizer = new Parallelizer<>();

    @Test
    public void testChunkMode() throws ExecutionException, InterruptedException {
        List<Integer> ret = parallelizer.parallelize(Arrays.asList(1, 2, 3), new Operation<Integer, Integer>() {
            @Override
            public List<Integer> process(List<Integer> dataSet) {
                List<Integer> ret = new ArrayList<>();
                for(Integer item : dataSet){
                    ret.add(item * 10);
                }
                return ret;
            }
        }, Mode.CHUNK);
        System.out.println(ret);
    }

    @Test
    public void testStreamMode() throws ExecutionException, InterruptedException {
        List<Integer> ret = parallelizer.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), new Operation<Integer, Integer>() {
            @Override
            public List<Integer> process(List<Integer> dataSet) {
                List<Integer> ret = new ArrayList<>();
                for(Integer item : dataSet){
                    ret.add(item * 10);
                }
                return ret;
            }
        }, Mode.STREAM);
        System.out.println(ret);
    }

    @Test
    public void testDefaultMode() throws ExecutionException, InterruptedException {
        List<Integer> ret = parallelizer.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), new Operation<Integer, Integer>() {
            @Override
            public List<Integer> process(List<Integer> dataSet) {
                List<Integer> ret = new ArrayList<>();
                for(Integer item : dataSet){
                    ret.add(item * 10);
                }
                return ret;
            }
        });
        System.out.println(ret);
    }


}
