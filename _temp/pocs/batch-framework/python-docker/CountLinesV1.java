import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class CountLinesV1 {

    public static long countLines(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            AtomicInteger lineCount = new AtomicInteger(0);

            reader.lines()
                  .parallel()
                  .forEach(line -> lineCount.incrementAndGet());

            return lineCount.get();
        }
    }

    public static void main(String[] args) {
        String filename = "./_data/customers-256000000.csv";
        try {
            System.out.println("Counting lines in file: " + filename);
            long startTime = System.currentTimeMillis();
            long numLines = countLines(filename);
            long endTime = System.currentTimeMillis();
            // print the execution time in hh:mm:ss format
            System.out.println("Execution time: " + String.format("%02d:%02d:%02d",
                    (endTime - startTime) / 3600000,
                    ((endTime - startTime) / 60000) % 60,
                    ((endTime - startTime) / 1000) % 60));
            System.out.println("Total number of lines: " + numLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
  # RESULTS #:
    ## File: customers-256000000.csv (44.7 GB size)
    ### CPU: 4 cores | 8 vCPU (40% usage)
    #### RAM: 2.4 GB
    #### Execution time (MM:HH:SS): 00:03:47
    #
    ### CPU: 8 cores | 16 vCPU (% usage)
    #### RAM:
    #### Execution time (MM:HH:SS):
 */