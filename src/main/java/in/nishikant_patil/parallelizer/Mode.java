package in.nishikant_patil.parallelizer;

/**
 * Specifies the mode of parallel processing. CHUNK will create multiple sub lists of the data set while STREAM will
 * process the data set as a stream.
 */
public enum Mode {
    STREAM, CHUNK
}
