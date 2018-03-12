// The Record class' main purpose is to efficiently store strings.
// However it is important that this class is responsible for adding, removing ammending
// and getting items from within the record.

import java.util.*;

class Record {
  private List<String> r = new ArrayList<String>();
  private int size;

  public static void main(String args[]) {
    Record program = new Record();
    program.testRecord();
  }

  // The constructor of this class takes in the Strings to be stored in the Record.
  Record(String... field_entries) {
    for (String field_entry: field_entries) {
      r.add(field_entry);
      size++;
    }
  }

  /* All of the methods here are essentially just wrappers for the ArrayList type. */

  void addItem(String value) {
    r.add(value);
    size++;
  }

  boolean removeItem(int index) {
    if (index < r.size()) {
      r.remove(index);
      size--;
      return true;
    }
    return false;
  }

  boolean ammendItem(int index, String value) {
    if (index < r.size()) {
      r.set(index, value);
      return true;
    }
    return false;
  }

  boolean contains(String field) {
    if (r.contains(field)) { return true; }
    return false;
  }

  String getItem(int index) {
    return r.get(index);
  }

  int indexOf(String field) {
    return r.indexOf(field);
  }

  int size() {
    return size;
  }

  /******************************************/
  /***************** TESTING ****************/
  /******************************************/

  void printRecord() {
    System.out.println(r);
  }

  void claim(boolean b) {
    if (!b) throw new Error("Test failed");
  }

  void testRecord() {
    System.out.println("Testing Started");

    Record a = new Record("One","Two","Three");
    Record b = new Record("a","b");
    Record c = new Record("five","m","i","l","l","i","o","n");

    testGet(a,b,c);
    testAmmend(a,b,c);
    testAddRemove(a,b);
    testContains(c);

    a.printRecord();
    b.printRecord();
    c.printRecord();

    System.out.println("Testing Finished");
  }

  void testGet(Record a, Record b, Record c) {
    claim(a.getItem(1).equals("Two"));
    claim(b.getItem(0).equals("a"));
    claim(c.getItem(7).equals("n"));
  }

  void testAmmend(Record a, Record b, Record c) {
    claim(a.ammendItem(0,"Two"));
    claim(a.getItem(0).equals("Two"));
    claim(a.ammendItem(0,"One"));
    claim(a.getItem(0).equals("One"));
    claim(b.ammendItem(1,"c"));
    claim(b.getItem(1).equals("c"));
    claim(! b.ammendItem(2,"d"));
    claim(c.ammendItem(0,"six"));
    claim(c.getItem(0).equals("six"));
  }

  void testAddRemove(Record a, Record b) {
    a.addItem("Four");
    claim(a.size() == 4 && a.getItem(3).equals("Four"));
    claim(b.removeItem(0));
    claim(! b.removeItem(1));
    claim(b.size() == 1);
  }

  // The indexOf will find the first instance of the string in the record.
  void testContains(Record c) {
    claim(c.contains("six"));
    claim(c.indexOf("l")==3);
  }
}
