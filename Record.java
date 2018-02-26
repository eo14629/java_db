// make a Hashtable which has an integer key and a variable length array of strings for input.
// This way any number of fields can be inputted but this could be set to a specific value.
// Also, each record has its unique key so it is easy to retrieve the record.

import java.util.*;

class Record {
  private Hashtable<Integer,ArrayList<String>> records = new Hashtable<Integer,ArrayList<String>>();
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
    records.put(key, new_record);
    key++;
  }

  ArrayList<String> getRecord(int id_key) {
    return records.get(id_key);
  }

  String getItem(int id_key, int field_num) {
    return getRecord(id_key).get(field_num);
  }

  // Should these be booleans and return true or false such that better for testing?
  void ammendRecord(int id_key, int field_num, String value) {
    // if (field_num < getRecord(id_key).size()) {
      getRecord(id_key).set(field_num, value);// is it best to use the set error or create your own?
    // } else { throw new Error("field_num value larger than num of fields"); }
  }

  void deleteRecord(int id_key) {
    if (records.remove(id_key) == null) { throw new Error("Key does not exist"); }
  }

  void printHashtable(Hashtable<Integer,ArrayList<String>> table) {
    System.out.println(table);
  }

  /******************************************/
  /***************** TESTING ****************/
  /******************************************/

  void claim(boolean b) {
    if (!b) throw new Error("Test failed");
  }

  void testRecord() {
    addRecord("One","Two","Three");
    addRecord("a","b");
    addRecord("five","m","i","l","l","i","o","n");

    claim(getItem(1,1).equals("b"));
    claim(getItem(2,7).equals("n"));

    claim(getRecord(1).get(0).equals("a"));
    claim(getRecord(2).get(0).equals("five"));

    ammendRecord(2,0,"six");
    claim(getItem(2,0).equals("six"));
    ammendRecord(0,2,"Four");
    claim(getItem(0,2).equals("Four"));

    deleteRecord(2);
    printHashtable(records);
    deleteRecord(0);
    printHashtable(records);
  }
}
