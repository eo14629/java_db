import java.util.*;

class Table {
  private Hashtable<Integer,Record> table = new Hashtable<Integer,Record>();
  private Record field_names = new Record();
  private Set<Integer> keys = table.keySet();
  private int id = 0;

  public static void main(String args[]) {
    Table program = new Table();
    program.testTable();
  }

  Table(String... field_names) {
    Record field_name_record = new Record(field_names);
    this.field_names = field_name_record;
  }

  boolean insertRecord(Record r) {
    if (r.size() == field_names.size()) {
      table.put(id,r);
      id++;
      return true;
    }
    return false;
  }

  boolean deleteRecord(int id_key) {
    if (table.remove(id_key) == null) { return false; }
    return true;
  }

  Record selectRecord(int id_key) {
    return table.get(id_key);
  }

  Record selectFieldNames() {
    return field_names;
  }

  boolean updateRecord(Record r, String field_name, String value) {
    if (field_names.contains(field_name)) {
      int i = field_names.indexOf(field_name);
      r.ammendItem(i,value);
      return true;
    } else {
      return false;
    }
  }

  void addColumn(String heading) {
    field_names.addItem(heading);
    for (Integer key: keys) {
      selectRecord(key).addItem(null);
    }
  }

  boolean removeColoumn(String heading) {
    if (field_names.contains(heading)) {
      int i = field_names.indexOf(heading);
      field_names.removeItem(i);
      for (Integer key : keys) {
        selectRecord(key).removeItem(i);
      }
      return true;
    } else {
      return false;
    }
  }

  Set<Integer> getKeys() {
    return keys;
  }

  /******************************************/
  /***************** TESTING ****************/
  /******************************************/

  void printFieldNames() {
    field_names.printRecord();
  }

  void printTable() {
    System.out.print("   ");
    printFieldNames();
    for (Integer key : keys) {
      System.out.print(key + ": ");
      selectRecord(key).printRecord();
    }
  }

  void claim(boolean b) {
    if (!b) throw new Error("Test failed");
  }

  void testTable() {
    Table a_table = new Table("Name", "League");

    // Insertions
    Record r1 = new Record("Bristol FC", "Championship");
    claim(a_table.insertRecord(r1));
    Record r2 = new Record("Wolves", "Championship", "SLDKA");
    claim(! a_table.insertRecord(r2));
    Record r3 = new Record("QPR", "Championship");
    claim(a_table.insertRecord(r3));
    claim(a_table.selectRecord(1)!=null);
    Record r4 = new Record("Burnley", "Championship");
    claim(a_table.insertRecord(r4));
    Record r5 = new Record("WBA", "Championship");
    claim(a_table.insertRecord(r5));
    claim(a_table.selectRecord(3).getItem(0).equals("WBA"));

    // deletions
    claim(a_table.deleteRecord(3));
    claim(! a_table.deleteRecord(15));

    // selecting a record
    claim(a_table.selectRecord(3)==null);
    claim(a_table.selectRecord(0) != null);

    // adding and removing columns and updating the values
    a_table.printTable();
    a_table.addColumn("Points");
    claim(a_table.field_names.size() == 3);
    claim(a_table.updateRecord(r3, "Points", "31"));
    claim(! a_table.updateRecord(r4, "Poits", "31"));
    a_table.printTable();
    claim(a_table.removeColoumn("League"));
    claim(! a_table.removeColoumn("Legue"));
    a_table.printTable();
  }
}
