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

  // do i need to input an id for this or will that come in the table class?
  Record(String... field_entries) {
    for (String field_entry: field_entries) {
      the_record.add(field_entry);
      size++;
    }
  }

  String getItem(int field_num) {
    return the_record.get(field_num);
  }

  // Should these be booleans and return true or false such that better for testing?
  void ammendItem(int field_num, String value) {
    the_record.set(field_num, value);// is it best to use the set error or create your own?
  }

  void printRecord(ArrayList<String> record) {
    System.out.println(record);
  }

  ArrayList<String> theRecord() {
    return the_record;
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
    Record a_record = new Record("One","Two","Three");
    Record b_record = new Record("a","b");
    Record c_record = new Record("five","m","i","l","l","i","o","n");

    claim(a_record.getItem(1).equals("Two"));
    claim(c_record.getItem(7).equals("n"));

    c_record.ammendItem(0,"six");
    claim(c_record.getItem(0).equals("six"));
    b_record.ammendItem(1,"c");
    claim(b_record.getItem(1).equals("c"));

    printRecord(a_record.the_record);
    printRecord(b_record.the_record);
  }
}
