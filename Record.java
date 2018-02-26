// make a Hashtable which has an integer key and a variable length array of strings for input.
// This way any number of fields can be inputted but this could be set to a specific value.
// Also, each record has its unique key so it is easy to retrieve the record.

import java.util.*;

class Record {
  private ArrayList<String> the_record = new ArrayList<String>();
  private int size;

  public static void main(String args[]) {
    Record program = new Record();
    program.testRecord();
  }

  Record(String... field_entries) {
    for (String field_entry: field_entries) {
      the_record.add(field_entry);
      size++;
    }
  }

  String getItem(int index) {
    return the_record.get(index);
  }

  // have not tested this yet
  boolean contains(String field) {
    if (the_record.contains(field)) { return true; }
    return false;
  }

  int indexOf(String field) {
    return the_record.indexOf(field);
  }

  void addItem(String value) {
    the_record.add(value);
    size++;
  }

  boolean removeItem(int index) {
    if (index < the_record.size()) {
      the_record.remove(index);
      size--;
      return true;
    }
    return false;
  }

  boolean ammendItem(int index, String value) {
    if (index < the_record.size()) {
      the_record.set(index, value);
      return true;
    }
    return false;
  }

  void printRecord() {
    System.out.println(the_record);
  }

  int size() {
    return size;
  }

  /******************************************/
  /***************** TESTING ****************/
  /******************************************/

  void claim(boolean b) {
    if (!b) throw new Error("Test failed");
  }

  void testRecord() {
    // creating records by using constructors
    Record a_record = new Record("One","Two","Three");
    Record b_record = new Record("a","b");
    Record c_record = new Record("five","m","i","l","l","i","o","n");

    // get
    claim(a_record.getItem(1).equals("Two"));
    claim(c_record.getItem(7).equals("n"));

    // ammend
    claim(c_record.ammendItem(0,"six"));
    claim(c_record.getItem(0).equals("six"));
    claim(b_record.ammendItem(1,"c"));
    claim(b_record.getItem(1).equals("c"));
    claim(! b_record.ammendItem(2,"d"));

    // add and remove
    a_record.addItem("Four");
    claim(a_record.size() == 4 && a_record.getItem(3).equals("Four"));
    claim(b_record.removeItem(0));
    claim(! b_record.removeItem(1));
    claim(b_record.size() == 1);

    // contains and indexOf
    claim(c_record.contains("six"));
    claim(c_record.indexOf("l")==3);// will find the first instance

    a_record.printRecord();
    b_record.printRecord();
    c_record.printRecord();
  }
}
