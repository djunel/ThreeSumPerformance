import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;

public class ThreeSum {

        static ThreadMXBean bean = ManagementFactory.getThreadMXBean( );

        /* define constants */
        static long MAXVALUE =  2000000000;
        static long MINVALUE = -2000000000;
        static int numberOfTrials = 50;
        static int MAXINPUTSIZE  = (int) Math.pow(1.5,22);
        static int MININPUTSIZE  =  1;
        // static int SIZEINCREMENT =  10000000; // not using this since we are doubling the size each time

        static String ResultsFolderPath = "/home/diana/Results/"; // pathname to results folder
        static FileWriter resultsFile;
        static PrintWriter resultsWriter;


        public static void main(String[] args) {

            verifyThreeSum();

            // run the whole experiment at least twice, and expect to throw away the data from the earlier runs, before java has fully optimized

            //System.out.println("Running first full experiment...");
            //runFullExperiment("ThreeSum-Exp1-ThrowAway.txt");
            //System.out.println("Running second full experiment...");
            //runFullExperiment("ThreeSum-Exp2.txt");
           // System.out.println("Running third full experiment...");
            //runFullExperiment("ThreeSum-Exp3.txt");
        }

        static void runFullExperiment(String resultsFileName){


            try {
                resultsFile = new FileWriter(ResultsFolderPath + resultsFileName);
                resultsWriter = new PrintWriter(resultsFile);
            } catch(Exception e) {
                System.out.println("*****!!!!!  Had a problem opening the results file "+ResultsFolderPath+resultsFileName);
                return; // not very foolproof... but we do expect to be able to create/open the file...
            }

            ThreadCpuStopWatch BatchStopwatch = new ThreadCpuStopWatch(); // for timing an entire set of trials
            ThreadCpuStopWatch TrialStopwatch = new ThreadCpuStopWatch(); // for timing an individual trial

            resultsWriter.println("#InputSize    AverageTime"); // # marks a comment in gnuplot data
            resultsWriter.flush();
            /* for each size of input we want to test: in this case starting small and doubling the size each time */
            for(int inputSize=MININPUTSIZE;inputSize<=MAXINPUTSIZE; inputSize*=2) {
                // progress message...
                System.out.println("Running test for input size "+inputSize+" ... ");

                /* repeat for desired number of trials (for a specific size of input)... */
                long batchElapsedTime = 0;
                // generate a list of randomly spaced integers in ascending sorted order to use as test input
                // In this case we're generating one list to use for the entire set of trials (of a given input size)
                // but we will randomly generate the search key for each trial
                System.out.print("    Generating test data...");
                long[] testList = createRandomIntegerList(inputSize);
                System.out.println("...done.");
                System.out.print("    Running trial batch...");

                /* force garbage collection before each batch of trials run so it is not included in the time */
                System.gc();


                // instead of timing each individual trial, we will time the entire set of trials (for a given input size)
                // and divide by the number of trials -- this reduces the impact of the amount of time it takes to call the
                // stopwatch methods themselves
                BatchStopwatch.start(); // comment this line if timing trials individually

                // run the tirals
                for (long trial = 0; trial < numberOfTrials; trial++) {
                    // generate a random key to search in the range of a the min/max numbers in the list
                    //long testSearchKey = (long) (0 + Math.random() * (testList[testList.length-1]));
                    /* force garbage collection before each trial run so it is not included in the time */
                    // System.gc();

                    //TrialStopwatch.start(); // *** uncomment this line if timing trials individually
                    /* run the function we're testing on the trial input */
                    //long foundIndex = MergeSort(testList);
                   int totalThreeSumCount = ThreeSumCount(testList);
                    // batchElapsedTime = batchElapsedTime + TrialStopwatch.elapsedTime(); // *** uncomment this line if timing trials individually
                }
                batchElapsedTime = BatchStopwatch.elapsedTime(); // *** comment this line if timing trials individually
                double averageTimePerTrialInBatch = (double) batchElapsedTime / (double)numberOfTrials; // calculate the average time per trial in this batch

                /* print data for this size of input */
                resultsWriter.printf("%12d  %15.2f \n",inputSize, averageTimePerTrialInBatch); // might as well make the columns look nice
                resultsWriter.flush();
                System.out.println(" ....done.");
            }
        }

        /*Verify merge sort is working*/
        static void verifyThreeSum(){
            long[] testList = new long[]{1,2, -3, 5,3,9,12,55,-13,-5,10,20,23,-2,4};
            System.out.println(Arrays.toString(testList));
            int result =  ThreeSumCount(testList);
           System.out.println("I found " + result + " three sums in this list.");

        }

        public static int ThreeSumCount(long[] list){

            int countThreeSums = 0;
            int N = list.length;

            for(int i = 0; i < N; i++){
                for(int j = i + 1; j < N; j++){
                    for(int k = j + 1; k < N; k++){
                        if(list[i] + list[j] + list[k] == 0){
                            countThreeSums++;
                        }
                    }
                }
            }

            return countThreeSums;
        }

        public static long[] createRandomIntegerList(int size) {

            long[] newList = new long[size];
            for (int j = 0; j < size; j++) {
                newList[j] = (long) (MINVALUE + Math.random() * (MAXVALUE - MINVALUE));
            }
            return newList;
        }


}
