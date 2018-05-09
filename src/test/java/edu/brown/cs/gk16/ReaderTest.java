package edu.brown.cs.gk16;

import java.util.List;

import org.junit.Test;

public class ReaderTest {

  @Test
  public void testNewReader() {
    Reader fileReader = new Reader();
    assert (fileReader != null);
  }

  @Test
  public void testReadFileNormal() {
    Reader fileReader = new Reader();
    List<String> input = fileReader.readFile("data/autocorrect/dictionary.txt");
    assert (input.size() != 0);
  }

  @Test
  public void testReadFileInvalidFile() {
    Reader fileReader = new Reader();
    List<String> input = fileReader.readFile("invalidFile");
    assert (input.size() == 0);
  }

}
