package edu.brown.cs.gk16.bacon;

import org.junit.Test;

public class BaconObjectTest {

  @Test
  public void testAddDb() {
    // Creating new bacon object and loading already existing database
    BaconObject bacon = new BaconObject();
    int result = bacon.addDb("data/bacon/bacon.sqlite3");
    assert (result == 0);
  }

  @Test
  public void testAddDbNewDb() {
    // Creating new bacon object and loading database that doesn't exist
    BaconObject bacon = new BaconObject();
    int result = bacon.addDb("data/bacon/newDb.sqlite3");
    assert (result == 0);
  }

  @Test
  public void testAddDbNotValidPath() {
    // Creating new bacon object and loading database with an invalid path
    BaconObject bacon = new BaconObject();
    int result = bacon.addDb("data/notafolder/newDb.sqlite3");
    assert (result == -1);
  }

  @Test
  public void testQueryActorTableIdFromName() {
    // Creating new bacon object and loading database
    BaconObject bacon = new BaconObject();
    bacon.addDb("data/bacon/bacon.sqlite3");
    String id = bacon.queryTable("Ilona Bodnar", "id", "name", "actor");
    assert (id.equals("/m/0lt2pk5"));
    id = bacon.queryTable("Amy Grant", "id", "name", "actor");
    assert (id.equals("/m/010hn"));
  }

  @Test
  public void testQueryActorTableNameFromId() {
    // Creating new bacon object and loading database
    BaconObject bacon = new BaconObject();
    bacon.addDb("data/bacon/bacon.sqlite3");
    String id = bacon.queryTable("/m/0lt2pk5", "name", "id", "actor");
    assert (id.equals("Ilona Bodnar"));
    id = bacon.queryTable("/m/010hn", "name", "id", "actor");
    assert (id.equals("Amy Grant"));
  }

  @Test
  public void testQueryFilmTableIdFromName() {
    // Creating new bacon object and loading database
    BaconObject bacon = new BaconObject();
    bacon.addDb("data/bacon/bacon.sqlite3");
    String id = bacon.queryTable("My Left Foot", "id", "name", "film");
    assert (id.equals("/m/0yzvw"));
    id = bacon.queryTable("Awakenings", "id", "name", "film");
    assert (id.equals("/m/0y_hb"));
  }

  @Test
  public void testQueryTableReturnsEmptyStringForInvalidSearch() {
    // Creating new bacon object and loading database
    BaconObject bacon = new BaconObject();
    bacon.addDb("data/bacon/bacon.sqlite3");
    String id = bacon.queryTable("Not a name", "id", "name", "actor");
    assert (id.equals(""));
  }

  @Test
  public void testCheckFirstLetters() {
    String actor1 = "Lawrence Tibbett";
    String actor2 = "Tera Patrick";
    assert (BaconObject.checkFirstLetters(actor1, actor2));
    assert (!BaconObject.checkFirstLetters(actor2, actor1));
  }

  @Test
  public void testCheckFirstLettersMultipleNames() {
    String actor1 = "Griffin Man-Kao";
    String actor2 = "Melvin Lastname";
    assert (BaconObject.checkFirstLetters(actor1, actor2));
    assert (!BaconObject.checkFirstLetters(actor2, actor1));
    actor1 = "Mary Ellen McCartan";
    actor2 = "Michael Condron";
    assert (BaconObject.checkFirstLetters(actor1, actor2));
    assert (!BaconObject.checkFirstLetters(actor2, actor1));
  }

  @Test
  public void testCheckFirstLettersJrSrQuotes() {
    String actor1 = "Griffin Man-Kao, Jr.";
    String actor2 = "Melvin Lastname";
    assert (BaconObject.checkFirstLetters(actor1, actor2));
    assert (!BaconObject.checkFirstLetters(actor2, actor1));
    actor1 = "Griffin Man-Kao Jr.";
    assert (BaconObject.checkFirstLetters(actor1, actor2));
    assert (!BaconObject.checkFirstLetters(actor2, actor1));
    actor1 = "Griffin Man-Kao, Sr.";
    assert (BaconObject.checkFirstLetters(actor1, actor2));
    assert (!BaconObject.checkFirstLetters(actor2, actor1));
    actor1 = "Person Srinivisan";
    actor2 = "Solomon Yanuck";
    assert (BaconObject.checkFirstLetters(actor1, actor2));
    assert (!BaconObject.checkFirstLetters(actor2, actor1));
    actor1 = "José Luis Rodríguez \"El Puma\"";
    actor2 = "Roger Williams";
    assert (BaconObject.checkFirstLetters(actor1, actor2));
    assert (!BaconObject.checkFirstLetters(actor2, actor1));
  }

}
