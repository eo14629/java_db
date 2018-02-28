import java.util.*;
import java.io.*;

class Output {

  public static void main(String args[]) {
    Output program = new Output();
    program.testToFile();
  }

  void print(Table table) {
    for (int item=0; item<table.selectFieldNames().size(); item++) {
      System.out.print(table.selectFieldNames().getItem(item) + "\t");
    }
    System.out.println();
    for (Integer key : table.getKeys()) {
      for (int item=0; item<table.selectRecord(key).size(); item++) {
        System.out.print(table.selectRecord(key).getItem(item) + "\t");
      }
      System.out.println();
    }
  }

  void writeCsv(Table table, String file_name) {
    File f = new File(file_name);
    FileWriter out = null;
    
    try {
      out = new FileWriter(f);

      writeFields(table.selectFieldNames(), out);
      for (Integer key : table.getKeys()) {
        writeFields(table.selectRecord(key), out);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (out != null) try { out.close(); } catch (IOException ignore) {}
    }
  }

  void writeFields(Record record, FileWriter out) {
    try {
      for (int item=0; item<record.size(); item++) {
        out.write(record.getItem(item) + ",");
      }
      out.write("\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /******************************************/
  /***************** TESTING ****************/
  /******************************************/

  void testToFile() {
    Table football = new Table("Team","Goals","Points");

    Record r1 = new Record("Bristol", "2", "3");
    Record r2 = new Record("Arsenal", "9", "42");
    Record r3 = new Record("QPR", "19", "38");
    football.insertRecord(r1);
    football.insertRecord(r2);
    football.insertRecord(r3);

    print(football);
    writeCsv(football, "test.csv");
  }

}
