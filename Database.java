// Database class is used to group together related tables. This class is also
// able to wrap up all the related tables as csv files in a directory structure on disk.

import java.util.*;

class Database {
  // using a TreeMap to ease the process of printing in Alphabetical order
  private Map<String,Table> database = new TreeMap<String,Table>();
  private Set<String> keys = database.keySet();
  private final String db_name;

  public static void main(String args[]) {
    Database program = new Database("program");
    program.testDatabase();
  }

  // simple constructor to initialise the Database and give it a name
  Database(final String db_name) {
    this.db_name = db_name;
  }

  // encapsulating the db_name field to be read only
  String getName() {
    return db_name;
  }

  // the ability to remove a table from the database
  boolean drop(String name) {
    if (database.remove(name) == null) {
      return false;
    }
    return true;
  }

  // add a preexisting table to the database
  boolean add(String name, Table table) {
    if (! keys.contains(name)) {
      database.put(name, table);
      return true;
    }
    return false;
  }

  // get a table from the database
  Table select(String name) {
    if (keys.contains(name)) {
      return database.get(name);
    }
    return null;
  }

  // encampsulation of the keys in order to print the tables from the Io class
  Set<String> getKeys() {
    return database.keySet();
  }

  /******************************************/
  /***************** TESTING ****************/
  /******************************************/

  // size method used purely for testing
  int size() {
    return database.size();
  }

  void claim(boolean b) {
    if (!b) throw new Error("Test failed");
  }

  void testDatabase() {
    System.out.println("Testing started");

    Database d = new Database("Football");
    testAddDiffTables(d);
    testAddDuplicateTables(d);
    testSelect(d);
    testDrop(d);

    Io io = new Io();
    io.showTables(d);

    testDirectory(io,d);

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

    t1.addColumn("Name*");
    Record r = new Record("Eddie");
    claim(t1.insertRecord(r));
  }

  // names of tables must be unique
  void testAddDuplicateTables(Database d) {
    Table t3 = new Table();
    Table t4 = new Table();
    claim(! d.add("t2", t3) && d.size()==4);
    claim(! d.add("t9", t4) && d.size()==4);
    claim(d.add("t4", t4) && d.size()==5);
  }

  // test the select works as it should
  // uncomment tests to see the error throwing work correctly
  void testSelect(Database d) {
    Table t = new Table();
    claim(d.select("t1")!=null);
    t = d.select("t1");
    claim(t.selectItem(t.keyGen("Eddie"),"Name*").equals("Eddie"));
    // claim(t.selectItem(t.keyGen("Edde"),"Name*").equals("Eddie"));
    // claim(t.selectItem(t.keyGen("Eddie"),"Nae*").equals("Eddie"));
    claim(d.select("t3")==null);
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

  // making the database directory within the current directory
  void testDirectory(Io io, Database d) {
    io.mkDir(d);
  }
}
