import java.util.*;

class Table {
  private Hashtable<Integer,Record> table = new Hashtable<Integer,Record>();
  private Record headings = new Record();
  private Set<Integer> keys = table.keySet();
  private ArrayList<Integer> pk_cols = new ArrayList<Integer>();
  private Set<ArrayList<String>> primary_keys = new HashSet<ArrayList<String>>();
  private int id = 0;

  public static void main(String args[]) {
    Table program = new Table();// the * is needed for testing purposes only
    program.testTable();
  }

  Table(String... headings) {
    int col_count = 0;
    Record field_name_record = new Record(headings);
    for (int i=0; i<field_name_record.size(); i++) {
      StringBuilder sb = new StringBuilder(field_name_record.getItem(i));
      while(sb.toString().charAt(sb.length()-1) == ' ') {
        sb.deleteCharAt(sb.length()-1);
      }
      if (isPk(sb.toString())) {
        pk_cols.add(col_count);
      }
      field_name_record.ammendItem(i,sb.toString());
      col_count++;
    }
    if (pk_cols.size()==0 && field_name_record.size() > 0) {
      throw new Error("No primary keys. Put a '*' at the end of a column name when instantiating a table to represent a pk");
    }
    this.headings = field_name_record;
  }

  // probs need errors here.
  boolean insertRecord(Record r) {
    if (r.size() == headings.size() && checkPk(r)) {
      table.put(id,r);
      id++;
      return true;
    }
    return false;
  }

  boolean checkPk(Record r) {
    ArrayList<String> key = new ArrayList<String>();
    for (int col : pk_cols) {
      key.add(r.getItem(col));
    }
    return primary_keys.add(key);
  }

  int numPks() {
    return pk_cols.size();
  }

  boolean isPk(String s) {
    if (s.charAt(s.length()-1) == '*') {
      return true;
    }
    return false;
  }

  boolean deleteRecord(int id_key) {
    if (table.remove(id_key) == null) { return false; }
    return true;
  }

  ////////////////////////
  /// use primary keys ///
  ////////////////////////
  Record selectRecord(int id_key) {
    return table.get(id_key);
  }

  boolean updateRecord(Record r, String field_name, String value) {
    if (headings.contains(field_name)) {
      int i = headings.indexOf(field_name);
      r.ammendItem(i,value);
      return true;
    } else {
      return false;
    }
  }

  Set<Integer> getKeys() {
    return keys;
  }

  Record selectFieldNames() {
    return headings;
  }

  int size() {
    return table.size();
  }

  void addColumn(String heading) {
    headings.addItem(heading);
    if (isPk(heading)) {
      pk_cols.add(headings.size()-1);
    }
    for (Integer key: keys) {// this is why hash tables are still a good idea
      selectRecord(key).addItem(null);
    }
  }

  boolean removeColoumn(String heading) {
    if (headings.contains(heading)) {
      int i = headings.indexOf(heading);
      headings.removeItem(i);
      for (Integer key : keys) {
        selectRecord(key).removeItem(i);
      }
      return true;
    } else {
      return false;
    }
  }

  /******************************************/
  /***************** TESTING ****************/
  /******************************************/

  void printFieldNames() {
    headings.printRecord();
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
    System.out.println("Testing start");
    Table a_table = new Table("Name*", "League");

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
    Record r6 = new Record("WBA", "Prem");
    claim(! a_table.insertRecord(r6));

    // deletions
    claim(a_table.deleteRecord(3));
    claim(! a_table.deleteRecord(15));

    // selecting a record
    claim(a_table.selectRecord(3)==null);
    claim(a_table.selectRecord(0) != null);

    // adding and removing columns and updating the values
    a_table.printTable();
    a_table.addColumn("Points");
    claim(a_table.headings.size() == 3);
    claim(a_table.updateRecord(r3, "Points", "31"));
    claim(! a_table.updateRecord(r4, "Poits", "31"));
    a_table.printTable();
    claim(a_table.removeColoumn("League"));
    claim(! a_table.removeColoumn("Legue"));

    // check that the primary key column array list stores the correct columns
    // respresenting the primary keys.
    // Also make sure that the constructor gets rid of extra spaces
    Table table_pk = new Table("Name*      ","League* ","Points   ");
    claim(table_pk.pk_cols.size()==2);
    claim(table_pk.pk_cols.get(0)==0);
    claim(table_pk.pk_cols.get(1)==1);
    claim(table_pk.headings.size()==3);
    claim(! table_pk.headings.getItem(0).equals("Name*  "));
    claim(table_pk.headings.getItem(0).equals("Name*"));
    claim(table_pk.headings.getItem(1).equals("League*"));
    claim(table_pk.headings.getItem(2).equals("Points"));

    // check the error is thrown for no primary keys
    // Table table_pk2 = new Table("Name","League","Points");

    // ensure the primary keys work as they should and not allow a record
    // to be inserted if it disobeys the pk rules
    Record r01 = new Record("Bristol FC", "Championship", "14");
    Record r02 = new Record("Bristol FC", "Premier League", "14");
    Record r03 = new Record("QPR", "Championship", "1");
    Record r04 = new Record("Bristol FC", "Championship", "12");
    claim(table_pk.insertRecord(r01));
    claim(table_pk.insertRecord(r02));
    claim(table_pk.insertRecord(r03));
    claim(! table_pk.insertRecord(r04));
    claim(table_pk.primary_keys.size()==3);
    claim(table_pk.size()==3);

    a_table.printTable();

    System.out.println("Testing finished");
  }
}
