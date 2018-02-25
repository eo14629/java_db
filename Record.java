import java.util.*;

class Record {
  private ArrayList<String> recordList = new ArrayList<String>();

  public static void main(String args[]) {
    Record program = new Record();
    program.testRecord();
  }

  void addRecord(String... field_entries) {

    for (String field_entry: field_entries) {
      recordList.add(field_entry);
    }
    System.out.println(recordList + " & size = " + recordList.size());
  }

  void testRecord() {
    addRecord("One","Two","Three");
    addRecord("a","b");
    addRecord("five","m","i","l","l","i","o","n");
  }
}
