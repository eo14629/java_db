import java.util.*;

class Table {
  // think there is repeted data here. because the keys are storing data
  // that is also in the hash key
  private Hashtable<ArrayList<String>,Record> table = new Hashtable<ArrayList<String>,Record>();
  private Record headings = new Record();
  private Set<ArrayList<String>> hash_keys = table.keySet();
  private ArrayList<Integer> pk_cols = new ArrayList<Integer>();
  private int id = 0;

  public static void main(String args[]) {
    Table program = new Table();
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

  int numPks() {
    return pk_cols.size();
  }

  boolean isPk(String s) {
    if (s.charAt(s.length()-1) == '*') {
      return true;
    }
    return false;
  }

  // probs need errors here.
  boolean insertRecord(Record r) {
    if (r.size() == headings.size()) {
      ArrayList<String> key = new ArrayList<String>();
      for (int col : pk_cols) {
        key.add(r.getItem(col));
      }
      if (! hash_keys.contains(key)) {
        table.put(key,r);
        return true;
      } else {
        return false;
      }
    }
    return false;
  }

  ArrayList<String> keyGen(String... keys) {
    ArrayList<String> pks = new ArrayList<String>();
    for (String key : keys) {
      pks.add(key);
    }
    return pks;
  }

  boolean deleteRecord(ArrayList<String> pk) {
    if (table.remove(pk) == null) { return false; }
    return true;
  }

  Record selectRecord(ArrayList<String> pk) {
    return table.get(pk);
  }

  boolean updateRecord(ArrayList<String> pk, String field_name, String value) {
    if (headings.contains(field_name)) {
      int i = headings.indexOf(field_name);
      selectRecord(pk).ammendItem(i,value);
      return true;
    } else {
      return false;
    }
  }

  Set<ArrayList<String>> getKeys() {
    return hash_keys;
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
    for (ArrayList<String> key: hash_keys) {
      selectRecord(key).addItem(null);
    }
  }

  boolean removeColoumn(String heading) {
    if (headings.contains(heading)) {
      int i = headings.indexOf(heading);
      headings.removeItem(i);
      for (ArrayList<String> key : hash_keys) {
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

  void claim(boolean b) {
    if (!b) throw new Error("Test failed");
  }

  void testTable() {
    System.out.println("Testing start");
    Table a_table = new Table("Name*", "League");

    testInsertions(a_table);
    testDuplicates(a_table);
    testDeletions(a_table);
    testSelections(a_table);
    testUpdates(a_table);

    Table table_pk = new Table("Name*      ","League* ","Points   ");
    testNoPks();
    testPks(table_pk);
    testMultiPk(table_pk);
    testOverride(table_pk);

    // test print from the Io class
    Io io = new Io();
    io.printTable(table_pk);

    System.out.println("Testing finished");
  }

  // Insertions
  void testInsertions(Table a_table) {
    Record r1 = new Record("Bristol FC", "Championship");
    claim(a_table.insertRecord(r1));
    Record r2 = new Record("Wolves", "Championship", "SLDKA");
    claim(! a_table.insertRecord(r2));
    Record r3 = new Record("QPR", "Championship");
    claim(a_table.insertRecord(r3));
    claim(a_table.selectRecord(a_table.keyGen("QPR"))!=null);
    Record r4 = new Record("Burnley", "Championship");
    claim(a_table.insertRecord(r4));
    Record r5 = new Record("WBA", "Championship");
    claim(a_table.insertRecord(r5));
    claim(a_table.selectRecord(a_table.keyGen("WBA")).getItem(0).equals("WBA"));
  }

  // checks that duplicate pks cannot get added
  void testDuplicates(Table a_table) {
    Record r6 = new Record("WBA", "Prem");
    claim(! a_table.insertRecord(r6));
  }

  // deletions
  void testDeletions(Table a_table) {
    claim(a_table.deleteRecord(a_table.keyGen("WBA")));
    claim(! a_table.deleteRecord(a_table.keyGen("Rovers")));
    claim(a_table.size()==3);
  }

  // selecting a record
  void testSelections(Table a_table) {
    claim(a_table.selectRecord(a_table.keyGen("WBA"))==null);
    claim(a_table.selectRecord(a_table.keyGen("Rovers"))==null);
    claim(a_table.selectRecord(a_table.keyGen("Tottenham"))==null);
    claim(a_table.selectRecord(a_table.keyGen("Bristol FC")) != null);
  }

  // adding and removing columns and updating the values
  void testUpdates(Table a_table) {
    claim(a_table.headings.size()==2);
    a_table.addColumn("Points");
    claim(a_table.headings.size()==3);
    claim(a_table.selectRecord(a_table.keyGen("Burnley")).size() == 3);
    claim(a_table.selectRecord(a_table.keyGen("QPR")).getItem(2)==null);
    claim(a_table.updateRecord(a_table.keyGen("QPR"), "Points", "31"));
    claim(a_table.selectRecord(a_table.keyGen("QPR")).getItem(2)!=null);
    claim(a_table.selectRecord(a_table.keyGen("QPR")).getItem(2).equals("31"));
    claim(! a_table.updateRecord(a_table.keyGen("Burnley"), "Poits", "31"));
    claim(a_table.removeColoumn("League"));
    claim(! a_table.removeColoumn("Legue"));
    claim(a_table.headings.size()==2);
  }

  // check that the primary key column array list stores the correct columns
  // respresenting the primary keys.
  // Also make sure that the constructor gets rid of extra spaces
  void testPks(Table table_pk) {
    claim(table_pk.pk_cols.size()==2);
    claim(table_pk.pk_cols.get(0)==0);
    claim(table_pk.pk_cols.get(1)==1);
    claim(table_pk.headings.size()==3);
    claim(! table_pk.headings.getItem(0).equals("Name*  "));
    claim(table_pk.headings.getItem(0).equals("Name*"));
    claim(table_pk.headings.getItem(1).equals("League*"));
    claim(table_pk.headings.getItem(2).equals("Points"));
  }

  // ensure the primary keys work as they should and not allow a record
  // to be inserted if it disobeys the pk rules
  void testMultiPk(Table table_pk) {
    Record r01 = new Record("Bristol FC", "Championship", "14");
    Record r02 = new Record("Bristol FC", "Premier League", "14");
    Record r03 = new Record("QPR", "Championship", "1");
    Record r04 = new Record("Bristol FC", "Premier League", "12");
    claim(table_pk.insertRecord(r01));
    claim(table_pk.insertRecord(r02));
    claim(table_pk.insertRecord(r03));
    claim(! table_pk.insertRecord(r04));
    claim(table_pk.size()==3);
  }

  // Ensuring no overriding when entering a duplicate - the record
  // should not be entered into the table
  void testOverride(Table table_pk) {
    ArrayList<String> bristol = new ArrayList<String>();
    bristol = table_pk.keyGen("Bristol FC","Premier League");
    claim(! table_pk.selectRecord(bristol).getItem(2).equals("12"));
    claim(table_pk.selectRecord(bristol).getItem(2).equals("14"));
  }

  // check the error is thrown for no primary keys (uncomment to check it works)
  void testNoPks() {
    // Table table_pk2 = new Table("Name","League","Points");
  }
}
