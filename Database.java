import java.util.*;

class Database {
  // using a TreeMap to ease the process of printing in Alphabetical order
  private TreeMap<String,Table> database = new TreeMap<String,Table>();
  private Set<String> keys = database.keySet();

  public static void main(String args[]) {
    Database program = new Database();
    program.testDatabase();
  }

  boolean add(String name, Table table) {
    if (! keys.contains(name)) {
      database.put(name, table);
      return true;
    } else {
      return false;
    }
  }

  Set<String> getKeys() {
    return database.keySet();
  }

  boolean drop(String name) {
    if (database.remove(name) == null) {
      return false;
    }
    return true;
  }

  /******************************************/
  /***************** TESTING ****************/
  /******************************************/

  int size() {
    return database.size();
  }

  void claim(boolean b) {
    if (!b) throw new Error("Test failed");
  }

  void testDatabase() {
    System.out.println("Testing started");

    Database d = new Database();
    testAddDiffTables(d);
    testAddDuplicateTables(d);
    testDrop(d);

    Io io = new Io();
    io.showTables(d);

    System.out.println("Testing finished");
  }

  // standard addition of different tables to a database
  void testAddDiffTables(Database d) {
    Table t1 = new Table();
    Table t2 = new Table();
    Table t8 = new Table();
    Table t9 = new Table();
    claim(d.add("t2", t2) && d.size()==1);
    claim(d.add("t1", t1) && d.size()==2);
    claim(d.add("t9", t9) && d.size()==3);
    claim(d.add("t8", t8) && d.size()==4);
  }

  // names of tables must be unique
  void testAddDuplicateTables(Database d) {
    Table t3 = new Table();
    Table t4 = new Table();
    claim(! d.add("t2", t3) && d.size()==4);
    claim(! d.add("t9", t4) && d.size()==4);
    claim(d.add("t4", t4) && d.size()==5);
  }

  // testing that dropping a table works well
  void testDrop(Database d) {
    claim(d.drop("t2"));
    claim(d.size()==4);
    claim(d.drop("t4"));
    claim(d.size()==3);
    claim(! d.drop("t4"));
    claim(! d.drop("t19"));
    claim(d.size()==3);
  }
}
