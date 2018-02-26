import java.util.*;

class Table {
  private Hashtable<Integer,ArrayList<String>> table = new Hashtable<Integer,ArrayList<String>>();
  private ArrayList<String> field_names = new ArrayList<String>();
  private int num_fields;
  private int key = 0;

  public static void main(String args[]) {
    Table program = new Table();
    program.testTable();
  }

  Table(String... field_names) {
    Record field_name_record = new Record(field_names);
    this.field_names = field_name_record.theRecord();
    num_fields = this.field_names.size();
  }

  void insertRecord(Record r) {
    if (r.size() == num_fields) {
      table.put(key,r.theRecord());
      key++;
    } else {
      throw new Error("Wrong number of fields for the table size");
    }
  }

  void deleteRecord(int id_key) {
      if (table.remove(id_key) == null) { throw new Error("Key does not exist"); }
  }

  ArrayList<String> getRecord(int id_key) {
    return table.get(id_key);
  }

  int tableSize() {
    return num_fields;
  }

  void printFieldNames() {
    System.out.println(field_names);
  }

  void printTable() {
    System.out.print("   ");
    printFieldNames();
    for (int i=0; i<key; i++) {
      if (getRecord(i)!=null) {
        System.out.println(i + ": " + getRecord(i));
      }
    }
  }

  // void makeTable(String table_name, String... field_names) {
  //   ArrayList<String> field_names = new ArrayList<String>();
  //   for (String field_name: field_names) {
  //     field_names.add(field_name);
  //   }
  //   table_field_names.put(table_name, field_names);
  // }

  /******************************************/
  /***************** TESTING ****************/
  /******************************************/

  void claim(boolean b) {
    if (!b) throw new Error("Test failed");
  }

  void testTable() {
    Table a_table = new Table("Name", "League");
    // makeTable("football_teams", "Name", "Squad Count", "League Position");
    a_table.printFieldNames();
    Record r1 = new Record("Bristol FC", "Championship");
    a_table.insertRecord(r1);
    Record r2 = new Record("Wolves", "Championship");
    a_table.insertRecord(r2);
    Record r3 = new Record("QPR", "Championship");
    a_table.insertRecord(r3);
    Record r4 = new Record("Burnley", "Championship");
    a_table.insertRecord(r4);
    Record r5 = new Record("WBA", "Championship");
    a_table.insertRecord(r5);

    claim(r5.getItem(0).equals("WBA"));
    claim(a_table.getRecord(3)!=null);

    a_table.deleteRecord(3);
    claim(a_table.getRecord(3)==null);

    ArrayList<String> record = new ArrayList<String>();
    record = a_table.getRecord(0);
    claim(record != null);

    a_table.printTable();
  }
}
