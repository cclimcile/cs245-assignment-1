/**
 *
 * This program creates a hybrid sort using randomized quickSort and
 * insertionSort to sort an unsorted array
 *
 * @author Chiara Lim
 *
 * Sources: https://www.geeksforgeeks.org/quicksort-using-random-pivoting/
 *
 **/

import java.io.*;
import java.util.*;

public class ExternalSort {

    public static void main(String[] args) throws IOException{

        File input = new File("input.txt");
        File output = new File("output.txt");

        externalSort(input, output, 10, 3);
    }

    /**
     * Sorts data in input file to temp files of k size and then performs k-mergeSort
     * to sort all temp files
     * @param inputFile
     * @param outputFile
     * @param n
     * @param k
     * @throws IOException
     */
    public static void externalSort(File inputFile, File outputFile, int n, int k) throws IOException{

        Scanner scanner = new Scanner(inputFile);

        int num_chunk = (int)Math.ceil(n/k);
        int file_num = 0;

        /* Read in a chunk at a time */
        for(int i = 0; i < num_chunk; i ++){
            /* Populate one chunk file */
            double [] temp = new double[k];
            for(int j = 0; j < k; j ++){
                String number = scanner.nextLine();
                double result = Double.parseDouble(number);
                System.out.println(number);
                temp[j] = result;
            }
            insertionSort(temp);
            createFile(file_num, temp, k);
            file_num += 1;
        }

        /* Special case: last chunk */
        double [] last_chunk = new double[k];
        int last = 0;
        int check = 0;
        while(scanner.hasNextLine()) {
            check += 1;
            /* Populate one chunk file */
            String number = scanner.nextLine();
            double result = Double.parseDouble(number);
            System.out.println(number);
            last_chunk[last++] = result;
        }
        for(int i = check; i < k; i++){
            last_chunk[i] = -1;
        }
        insertionSort(last_chunk);
        createFile(file_num, last_chunk, k);

        /* Time to sort the files */
        file_num = 0;
        String filename = "";
        int indices [] = new int[num_chunk];
        double temp [] = new double[num_chunk];
        int temp_i = 0;

        /* Initialize temp */
        for(int i = 0; i < num_chunk; i++){
            filename = "temp" + file_num + ".txt";
            File temp_file = new File(filename);
            Scanner temp_s = new Scanner(temp_file);
            file_num += 1;

            String num = temp_s.nextLine();
            double value = Double.parseDouble(num);
            temp[temp_i++] = value;
        }

        int num_sorted = 0;
        PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
        while(num_sorted < indices.length){
            /* Find the smallest value in temp */
            int index = findMin(temp);
            System.out.println("Min in temp is: " + temp[index]);
            /* Append it to output file */
            writer.println(temp[index]);

            /* Increment the index for that file and replace the temp*/
            indices[index]  += 1;
            /* If everything in file_index has been outputted, we don't want to access it */
            if(indices[index] >= k){
                num_sorted += 1;
                temp[index] = -1;
            } else {
                filename = "temp" + index + ".txt";
                File temp_file = new File(filename);
                Scanner temp_s = new Scanner(temp_file);
                String num = "";
                /* Keeps track of which line to read in the file */
                for(int i = 0; i <= indices[index]; i++){
                    num = temp_s.nextLine();
                }
                double value = Double.parseDouble(num);
                /* Update the value of temp */
                temp[index] = value;
            }

        }
        writer.close();

    }

    /**
     * Returns index of min value in the array
     * @param a
     * @return
     */
    public static int findMin(double [] a){
        int index = 0;
        for(int i = 1; i < a.length; i++){
            if(a[i] == -1){
                /* We want to skip it */
            }else if(a[i] < a[index]){
                index = i;
            }
        }
        return index;
    }

    /**
     * InsertionSort to sort temp files
     * @param arr
     */
    public static void insertionSort(double [] arr){

        for(int i = 1; i < arr.length; i++){
            double next = arr[i];
            int j = i  - 1;
            /* Only is the temp is smaller than value at index,
             * it pushes the numbers back to insert temp at that index
             */
            while(j >= 0 && next < arr[j]){
                arr[j + 1] = arr[j];
                j -= 1;
            }
            arr[j + 1] = next;
        }

    }

    /**
     * Create and writes to temp files
     * @param file_num
     * @param arr
     * @param k
     * @throws IOException
     */
    public static void createFile(int file_num, double [] arr, int k) throws IOException{

        /* Create the file */
        String filename = "temp" + file_num + ".txt";
        File new_file = new File(filename);
        new_file.createNewFile();
        System.out.println("Successfully created " + filename);

        /* Write into the file */
        FileWriter file_writer = new FileWriter(filename);
        for(int i = 0; i < k; i++){
            file_writer.write(arr[i] + "\n");
        }
        file_writer.close();
        System.out.println("Successfully wrote to " + filename + "\n");

    }


}
