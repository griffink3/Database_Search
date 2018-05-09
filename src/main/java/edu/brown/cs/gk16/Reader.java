package edu.brown.cs.gk16;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The class represents a file reader that can take in a file and then return
 * the words in the file (split on spaces) as a list of strings.
 */
public class Reader {

  private List<String> input;

  /**
   * No paramaters.
   */
  public Reader() {
    input = new ArrayList<String>();
  }

  /**
   * @param file
   *          string describing file name
   * @return a list of strings representing all the words in the file (split on
   *         spaces) - if the list is empty, an error occurred or no words were
   *         in the file
   */
  public List<String> readFile(String file) {
    BufferedReader br = null;
    String line = "";
    try {
      br = new BufferedReader(new FileReader(file));
      // Skip over header line
      line = br.readLine();
      while (line != null) {
        String[] words = line.split(" ");
        for (String word : words) {
          input.add(word);
        }
        line = br.readLine();
      }
    } catch (FileNotFoundException e) {
      System.out.println("ERROR: File not found");
      return input;
    } catch (IOException e) {
      System.out
          .println("ERROR: IO Exception occurred upon opening - check file");
      return input;
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          System.out.println(
              "ERROR: IO Exception occurred upon closing - check file");
          return input;
        }
      }
    }
    return input;
  }

}
