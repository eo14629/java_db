// make a Hashtable which has an integer key and a variable length array of strings for input.
// This way any number of fields can be inputted but this could be set to a specific value.
// Also, each record has its unique key so it is easy to retrieve the record.

import java.util.*;

class Record {
  private Hashtable<Integer,ArrayList<String>> recordTable = new Hashtable<Integer,ArrayList<String>>();
  private int key = 0;

  public static void main(String args[]) {
    Record program = new Record();
    program.testRecord();
  }

  void addRecord(String... field_entries) {
    ArrayList<String> new_record = new ArrayList<String>();
    for (String field_entry: field_entries) {
      new_record.add(field_entry);
    }
    recordTable.put(key, new_record);
    key++;
  }

  void printHashtable() {
    System.out.println(recordTable);
  }

  void testRecord() {
    addRecord("One","Two","Three");
    addRecord("a","b");
    addRecord("five","m","i","l","l","i","o","n");
    printHashtable();
  }
}
