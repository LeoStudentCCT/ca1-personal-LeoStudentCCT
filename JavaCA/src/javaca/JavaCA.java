/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaca;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import static javafx.beans.binding.Bindings.length;

/**
 *
 * @author name: Leonardo Oliveira
 * Student number: 2021361
 * GitHub link: https://github.com/CCT-Dublin/ca1-LeoStudentCCT.git
 * GitHub Link personal: https://github.com/LeoStudentCCT/ca1-personal-LeoStudentCCT.git
 */
public class JavaCA {

    /**
     * @param args the command line arguments
     * 
     * 
     * cool coded
     */
    public static void main(String[] args) {
        // Create a list to store the numbers
        List<Double> numbers = new ArrayList<>();
        
        //Read the CSV file
        try {
            Scanner scanner = new Scanner(new File("C:\\Users\\leool\\OneDrive\\Documents\\NetBeansProjects\\MyProjectCA1\\ca1-personal-LeoStudentCCT\\JavaCA\\src\\javaca\\data.csv"));
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(",");
                for (String number : line) {
                    numbers.add(Double.parseDouble(number));
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Create CountDownLatch objects to synchronize the tasks. This is to control how they will be executed
        CountDownLatch latch1 = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(1);
        CountDownLatch latch3 = new CountDownLatch(1);

        // Create threads for each task
        Thread t1 = new Thread(() -> {
            calculateStandardDeviation(numbers);
            latch1.countDown(); // Signal that the first task is done
        });
        Thread t2 = new Thread(() -> {
            try {
                latch1.await(); // Wait for the first task to finish
                multiplyMatrices(numbers);
                latch2.countDown();// Signal that the second task is done
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t3 = new Thread(() -> {
            try {
                latch2.await();// Wait for the second task to finish
                mergeSort(numbers, 0, numbers.size() - 1); // Use merge sort instead of sortNumbers
                latch3.countDown();// Signal that the third task is done
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Start the threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for all threads to finish
        try {
            latch3.await();
                // Print the sorted numbers
            System.out.println("These are the numbers sorted in descending order:");
                for (double number : numbers) {
                    System.out.println(number);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
    // Method to calculate the standard deviation of a list of numbers
    public static void calculateStandardDeviation(List<Double> numbers) {
        // Calculate standard deviation
        double sum = 0.0, standardDeviation = 0.0;
        int length = numbers.size();
        
        // Calculate the mean
        for(double num : numbers) {
            sum += num;
        }

        double mean = sum/length;
        
        // Calculate the sum of squared differences from the mean
        for(double num: numbers) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        
        // Print the standard deviation
        System.out.println("The Standard Deviation is: " + Math.sqrt(standardDeviation/length));
    }
    
    // Method to multiply two matrices
    public static void multiplyMatrices(List<Double> numbers) {
        // Assume the data is in the form of two square matrices, one after another.
        int size = (int) Math.sqrt(numbers.size() / 2);//Tell the size of the matrices
        
        //Creating the matrices
        double[][] matrix1 = new double[size][size];
        double[][] matrix2 = new double[size][size];
        double[][] result = new double[size][size];

        // Fill the matrices with numbers from the list
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix1[i][j] = numbers.get(i * size + j);
                matrix2[i][j] = numbers.get(numbers.size() / 2 + i * size + j);
            }
        }

        // Multiply the matrices
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

        // Print the result of matrix multiplication
        System.out.println("The result of matrix multiplication is as followed:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(result[i][j] + "\t");
            }
            System.out.println();
        }
    }
    //Method to do the mergeSort on the list of numbers
    public static void mergeSort(List<Double> numbers, int left, int right){//Takes a list of numbers and two index, left and right, that is the range of the list that I will be sorting.
        if (left < right){//If left is not less than right, it means the list has one or zero elements and is then already sorted.
            int middle = (left + right) / 2;// Calculating the middle index of the list. It’s used to divide the current list into two bacisally equal halfs.
            
            mergeSort(numbers, left, middle);//sort the left half of the list (from left to midddle).
            mergeSort(numbers, middle +1, right);//sort the right half of the list (from midddle + 1 to right).
            
            merge(numbers, left, middle, right);//After they are sorted we merge them back together in sorted order.
        }
    }
    //Method to merge the subarrays
    public static void merge(List<Double> numbers, int left, int middle, int right){
        
        // Calculating the length of two halfs of the array to be merged
        int number1 = middle - left + 1;
        int number2 = right - middle;
        
        // Then I make the arrays to hold the two halfs
        double[] L = new double[number1];
        double[] R = new double[number2];
        
        // Pull data from numbers[] to the new arrays
        for (int i = 0; i < number1; ++i)
            L[i] = numbers.get(left + i);
        for (int j = 0; j < number2; ++j)
            R[j] = numbers.get(middle + 1 + j);
        
        // These are the start index for the two halfs and the merged array
        int i = 0, j = 0;
        int k = left;
        
        //Now merge this temporary array to numbers[] again
        while (i < number1 && j < number2) {
            if (L[i] >= R[j]) {
                numbers.set(k, L[i]);
                i++;
            } else {
                numbers.set(k, R[j]);
                j++;
            }
            k++;
        }
        
        while (i < number1) {
            numbers.set(k, L[i]);
            i++;
            k++;
        }
        
        while (j < number2) {
            numbers.set(k, R[j]);
            j++;
            k++;
        }
    }
    
}