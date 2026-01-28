/*
 Author: Alberth Matos
 CMSC 315, Chapter 22, Project 3
 Date: 20 January 2026
 Description:
 (Same-number subsequence)

 Write an O(n) time program that prompts the user to enter a sequence of
 integers ending with 0 and finds the longest subsequence with the same
 number.

 Sample Run

 Enter a series of numbers ending with 0: 2 4 4 8 8 8 8 2 4 4 0
 The longest same number sequence starts at index 3 with 4 values of 8
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class chapter22Project3 {
  // Main method
  public static void main(String[] args) {
    // Initialize variables
    Scanner scanner = new Scanner(System.in);
    String numberSequence;            // The input number sequence as a string
    int sequenceLength = 0;           // The length of the input number sequence
    int currentNumber = 0;            // The current number being processed
    int previousNumber = 0;           // The previous number processed
    int currentNumberCount = 0;       // The count of instances of the current
                                      // number.
    int previousMaxNumberCount = 0;   // The count of the previously found max
                                      // number of instances of the same number.
    int maxNumber = 0;                // The number with the max instances.
    int maxNumberStartIndex = 0;      // The starting index of the max number
                                      // subsequence.
    int currentStartIndex = 0;        // The starting index of the current
                                      // number subsequence.
    int currentIndex = 0;             // The current index in the series.
    boolean numbersNotTerminated = true; // Flag for missing 0 terminator.
    boolean nonIntegerInput = false; // Flag for non-integer input.

    // Prompt user for the series of numbers
    System.out.print("Enter a series of numbers ending with 0: ");
    numberSequence = scanner.nextLine();

    // Close scanner
    scanner.close();

    // Process input by splitting it into a list of integers
    String[] trimmedInput = numberSequence.trim().split("\\s+");
    List<Integer> numbers = new ArrayList<>();

    /*
     Loop until we reach the end of the series.
     The purpose of this section is to make certain that all input values
     are integers, so that we don't have to worry about invalid input later.
    */
    for (String integerInputAsString : trimmedInput) {
      try {
        // Add any integers to the list of numbers.
        numbers.add(Integer.parseInt(integerInputAsString));
      } catch (Exception NumberFormatException) {
        /*
         While not listed as a requirement, we handle invalid input
         gracefully, and trap the case where no integers were entered and
         break out of the loop. Later, we check if nonIntegerInput is true,
         and print an error message accordingly.
        */
        nonIntegerInput = true;
        break;
      }
    }

    /*
     Process the list of numbers to find the longest subsequence
     We do this by iterating through our list of numbers, setting
     currentNumber to each number in the list, and then tracking
     if we have a sequence of the same number, and if so, how many
     instances of that number we have seen.  If we find a longer sequence,
     we update our tracking variables accordingly.
    */

    // Get the length of the number sequence as sequenceLength.
    sequenceLength = numbers.size();

    // Loop through the list of numbers until we reach the end of the series.
    while (currentIndex < sequenceLength) {
      // Read the current number, currentNumber, from the list.
      currentNumber = numbers.get(currentIndex);

      /*
       If currentNumber, is 0, we set numbersNotTerminated to false, and
       break out of the loop. This is so that we can track if the series was
       properly terminated, and print an error message if it is not, similar
       to how we handled non-integer input earlier.  Note that
       numbersNotTerminated is initialized to true.
      */
      if (currentNumber == 0) {
        numbersNotTerminated = false;
        break;
      }

      if (currentNumber == previousNumber) {
        // If currentNumber matches previousNumber, increment the
        // current number count, currentNumberCount.
        currentNumberCount++;
      } else {
        /*
         If currentNumber does NOT match previousNumber, we set
         currentNumberCount to 1, and set previousNumber to
         currentNumber.  We also set the current starting number index,
         currentStartIndex, to the current index, currentIndex.
        */
        currentNumberCount = 1;
        previousNumber = currentNumber;
        currentStartIndex = currentIndex;
      }

      /*
       If currentNumberCount is greater than previousMaxNumberCount, we have
       found a new max sequence.  So, we set the max number, maxNumber, to
       currentNumber, the index location of maxNumber, maxNumberStartIndex,
       to currentStartIndex, and the previous max number count equal to the
       current number count.
      */
      if (currentNumberCount > previousMaxNumberCount) {
        previousMaxNumberCount = currentNumberCount;
        maxNumber = currentNumber;
        maxNumberStartIndex = currentStartIndex;
      }

      // Finally, we increment currentIndex so that we can loop and process
      // the next number in the series.
      currentIndex++;
    }

    // Print output
    if (nonIntegerInput || numbersNotTerminated) {
      // If invalid input was previously detected, print an error message.
      if (nonIntegerInput) {
        // If non-integer input was detected, print a specific error message.
        System.out.println("Invalid input. Please enter integers only," +
          " terminated with a 0");
      } else if (numbersNotTerminated) {
        // If the series was not terminated with 0, print a specific error message.
        System.out.println("Invalid input. The series must end with 0.");
      }
    } else if (previousMaxNumberCount > 0) {
      // Otherwise, print the results.
      System.out.println("The longest same number sequence starts at index "
        + maxNumberStartIndex + " with " + previousMaxNumberCount
        + " values of " + maxNumber);
    }
  }
}
