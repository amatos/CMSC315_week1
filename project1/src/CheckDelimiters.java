/*
 Copyright (c) 2026, Alberth Matos
 */

/*
 Author: Alberth Matos
 CMSC 315, Project 1
 Date: 20 January 2026
 Description: CheckDelimiters prompts the user for a source file name, and,
 using SourceFileReader, reads through the file to identify unmatched
 delimiters: (), {}, and [].  If an unmatched right-delimiter is found, an
 error message is displayed indicating the type of delimiter and its position
 in the source file.  If the end of file is reached without finding any
 unmatched right-delimiters, a message indicating such is displayed.

 Please note that unmatched left-delimiters are not reported, per the
 project instructions.  This can potentially lead to user confusion when
 unmatched left-delimiters exist in the source file, as, per the instructions
 given, any time a right delimiter is encountered, it is matched ONLY against
 the most recent left-delimiter, and the left delimiter is discarded from the
 stack.  This can lead to a situation where an unmatched left-delimiter can
 cause a subsequent right-delimiter to be reported as unmatched, even though
 it actually has a matching left-delimiter earlier in the file.

 e.g., in the following code snippet, the program reports an unmatched
 right-delimiter '}' at line 3, character (index) 1, even though there is a
 matching left-delimiter '{' at line 2, character (index) 24.

 Sample Input File (testdata/badfile2):
 line1: public class badExample {
 line2:   [ // Bad delimiter
 line3: } // Unmatched good delimiter

 Sample Output:
 Enter the name of the source file: testdata/badfile2
 Unmatched right delimiter '}' found at line 3, character (index) 1.
 End of file reached.
 */

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class CheckDelimiters {

  public static void main(String[] args) {
    // Initialize variables
    // --------------------

    // Initialize scanner to read from standard input.
    Scanner scanner = new Scanner(System.in);
    // Initialize source file reader
    SourceFileReader reader = null;
    // Initialize character stack for delimiters
    Stack<Character> delimiterStack = new Stack<>();
    // Initialize variables to track the current character and left-delimiter.
    char character;
    char leftDelimiter;

    /*
     Prompt user for filename until a valid file is provided.
     Per the project instructions, we loop until the user provides a valid
     filename.
    */
    while (reader == null) {
      System.out.print("Enter the name of the source file: ");
      String filename = scanner.nextLine();

      /*
       Try to create a SourceFileReader object with the provided filename.
       If the file is not found, catch the exception and prompt the user again.
      */
      try {
        reader = new SourceFileReader(filename);
      } catch (FileNotFoundException e) {
        System.out.println("File not found. Please enter a valid file name.");
      }
    }

    // Close the Scanner object.
    scanner.close();

    // Loop through the source file, until we reach a null character.
    while ((character = reader.getNextCharacter()) != '\0') {
      // Process each character
      if (isLeftDelimiter(character)) {
        // If the character is a left-delimiter, push onto the delimiter stack.
        delimiterStack.push(character);
      } else if (isRightDelimiter(character)) {
        /*
         If the character is a right-delimiter, pop from the delimiter stack
         and check for a matching type.  If the stack is empty, there is no
         matching left-delimiter.  Note, delimiterStack.pop() will throw an
         EmptyStackException if the stack is empty, so we catch that exception
         and display a mismatched delimiter message.
        */
        try {
          leftDelimiter = delimiterStack.pop();
        } catch (java.util.EmptyStackException e) {
          mismatchedDelimiter(character, reader.getCharacterPosition());
          continue;
        }

        if (!isComplementaryDelimiter(leftDelimiter, character)) {
          // If the delimiters do not match, call mismatchedDelimiter method
          // to display an error message.
          mismatchedDelimiter(character, reader.getCharacterPosition());
        }
      }
    }

    // Close the source file reader.
    reader.close();

    /*
     If we have reached the end of file, print end of file message so that
     the user is aware that processing is complete.
    */
    System.out.println("End of file reached.");
  }

  private static void mismatchedDelimiter(char character,
                                          String characterPosition) {
    // Print an error message indicating the unmatched right-delimiter and
    // its position.
    System.out.println("Unmatched right delimiter '" + character +
        "' found at " + characterPosition + ".");
  }

  private static boolean isComplementaryDelimiter(char leftDelimiter,
                                                  char rightDelimiter) {
    /*
     Check if the left and right delimiters are of matching types.
     This could get consolidated into a single return statement, but is left
     expanded for clarity.
     We run leftDelimiter through a switch statement, and for each case,
     return true if the rightDelimiter is the corresponding delimiter,
     otherwise, return false.
    */
    switch (leftDelimiter) {
      case '(':
        return (rightDelimiter == ')');
      case '{':
        return (rightDelimiter == '}');
      case '[':
        return (rightDelimiter == ']');
      default:
        return false;
    }
  }

  private static boolean isRightDelimiter(char delimiter) {
    /*
     If character is (, {, or [, return true, otherwise return false.
     As with isMatchingType, this could get consolidated into a single return
     statement, but is expanded into a switch for clarity.
    */
    switch (delimiter) {
      case ')':
      case '}':
      case ']':
        return true;
      default:
        return false;
    }
  }

  private static boolean isLeftDelimiter(char delimiter) {
    /*
     If character is (, {, or [, return true, otherwise return false.
     As with isMatchingType, this could get consolidated into a single return
     statement, but is expanded into a switch for clarity.
    */
    switch (delimiter) {
      case '(':
      case '{':
      case '[':
        return true;
      default:
        return false;
    }
  }
}
