/*
 Copyright (c) 2026, Alberth Matos
 */

/*
 Author: Alberth Matos
 CMSC 315, Project 1
 Date: 20 January 2026
 Description: SourceFileReader loads a source file into memory, and provides
 methods to read characters one at a time, skipping single-line and
 multi-line comments, as well as string and character literals.  The class
 also provides methods to get the next character, the character's position
 (line number and position within the line), as well as a method to close
 the file when done.  The class also contains private methods to get the
 individual line number and character index, used by the public
 getCharacterPosition() method.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SourceFileReader {

  // Initialize private member variables
  private Integer lineNumber; // Current line number in the source file.
  private Integer charIndex; // Current character index in the current line.
  private final Scanner fileReader; // Scanner to read from the source file.
  private String currentLine; // Content of the current line being processed.

  public SourceFileReader(String filename) throws FileNotFoundException {
    /*
     Class constructor that initializes the file reader with the given
     filename, sets the line number and character indexes to 0, and initiates
     the current line to an empty string.  If a file with the given name does
     not exist, a FileNotFoundException is thrown.
     */
    fileReader = new Scanner(new File(filename));
    lineNumber = 0;
    charIndex = 0;
    currentLine = "";
  }

  public void close() {
    // Close the file reader when done.
    fileReader.close();
  }

  public char getNextCharacter() {
    /*
    Return the next character from the source file, provided that the
    character is not:
     - Inside a comment
     - Inside a character literal
     - Inside a string literal
     */

    // Initialize character variable to hold the next character to be
    // returned.
    char character;

    while (true) {
      /*
       If we have reached the end of the current line, that is, charIndex
       is equal to or greater than the length of the line, we read the
       next line, increment the line number, and reset charIndex to 0.
      */
      if (charIndex >= currentLine.length()) {
        if (fileReader.hasNextLine()) {
          currentLine = fileReader.nextLine();
          lineNumber++;
          charIndex = 0;
        } else {
          // If there are no more lines to read, return the null character.
          return '\0';
        }
      }

      /*
       Read the next character from the current line. If the line is empty,
       catch the StringIndexOutOfBoundsException, increment charIndex by 1,
       and continue to the next iteration of the loop. This is so that we
       can safely handle empty lines without throwing an error.
       */
      try {
        character = currentLine.charAt(charIndex);
      } catch (StringIndexOutOfBoundsException e) {
        charIndex++;
        continue;
      }

      // increment charIndex by 1.
      charIndex++;

      // Skip comments and string/character literals
      // -------------------------------------------

      // Single-line comments, i.e.:
      // comment

      // The logic here is if the current character is '/', and the next
      // character is also '/', we ignore the test of the line and move
      // to the end of the line.  In order for this to work, note that we
      // incremented charIndex above, so currentLine.charAt(charIndex) is
      // actually the next character.
      if ((character == '/') &&
          (charIndex < currentLine.length()) &&
          (currentLine.charAt(charIndex) == '/')
      ) {
        charIndex = currentLine.length(); // Set charIndex to the end of line.
        continue;
      }

      // Multi-line comments, i.e.:
      /*
       comment
       */

      // As with the single-line comments, we check if the current character
      // is '/', and the next character is '*', to identify the start of
      // a multi-line comment.  We then continue reading characters until
      // we find the closing '*/' sequence, via getNextCharAfterSkip().
      if ((character == '/') &&
          (charIndex < currentLine.length()) &&
          (currentLine.charAt(charIndex) == '*')
      ) {
        charIndex++; // Increment to move past the current character.
        while (true) {
          character = getNextCharAfterSkip();
          if ((character == '*') &&
              (charIndex < currentLine.length()) &&
              (currentLine.charAt(charIndex) == '/')
          ) {
            charIndex++; // Increment to move past the current character.
            break; // Exit multi-line comment
          }
        }
        continue;
      }

      // Skip string literals (i.e., "string")
      // Here, if the current character is a double quote ("), we continue
      // reading characters until we find the closing double quote, again
      // via getNextCharAfterSkip().
      if (character == '"') {
        do {
          character = getNextCharAfterSkip();
        } while (character != '"');
        continue;
      }

      // Skip character literals (i.e., 'c')
      // Here, if the current character is a single quote ('), we continue
      // reading characters until we find the closing double quote, again
      // via getNextCharAfterSkip().
      if (character == '\'') {
        do {
          character = getNextCharAfterSkip();
        } while (character != '\'');
        continue;
      }

      // If we reach here, the character is valid and not part of a comment
      // or string/character literal, so we return it.
      return character;
    }
  }

  public String getCharacterPosition() {
    // Return the current character position as a string in the format:
    // "line X, character (index) Y"
    return "line " + lineNumber.toString() + ", character (index) " +
      charIndex.toString();
  }

  private char getNextCharAfterSkip() {
    /*
     The logic in this method is that we sometimes need to read the next
     character without applying the comment and string/character literal
     skipping logic.  This is used when we are inside a comment or string/
     character literal, and we need to read until we find the closing
     sequence.

     If we have reached the end of line, that is, charIndex is greater than
     or equal to the length of the currentLine, we check if the file has
     another line.  If it does, we read the next line, increment the line
     number counter, and reset charIndex to 0.

     If there are no more lines to read, we return the null character.
    */
    if (charIndex >= currentLine.length()) {
      if (fileReader.hasNextLine()) {
        currentLine = fileReader.nextLine();
        lineNumber++;
        charIndex = 0;
      } else {
        return '\0'; // End of file reached, return a null character.
      }
    }
    // Get the current character and increment charIndex by 1.
    char character = currentLine.charAt(charIndex);
    charIndex++;

    // Return the character.
    return character;
  }
}
