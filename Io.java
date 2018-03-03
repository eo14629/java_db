import java.util.*;
import java.io.*;

class Io {

  public static void main(String args[]) {
    Io program = new Io();
    program.testToFile();
  }

  void print(Table table) {
    for (int item=0; item<table.selectFieldNames().size(); item++) {
      System.out.print(table.selectFieldNames().getItem(item) + "\t" + "|");
    }
    System.out.println();
    for (Integer key : table.getKeys()) {
      for (int item=0; item<table.selectRecord(key).size(); item++) {
        System.out.print(table.selectRecord(key).getItem(item) + "\t" + "|");
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
        out.write("\"" + record.getItem(item) + "\"");
        if (item != record.size() - 1) {
          out.write(",");
        }
      }
      out.write("\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // assumed here that the format of the csv is double quoted fields
  // separated by commas.
  Table inputCsv(String pathname) {
    File file = new File(pathname);
    Scanner scanner = null;
    Table table = new Table();
    boolean first_line = true;

    try {
      scanner = new Scanner(file);

      while(scanner.hasNextLine()) {
        Record record = new Record();
        StringBuilder sb = new StringBuilder();

        sb.append(scanner.nextLine());
        // if (sb.toString().charAt(0) == '\"' && sb.toString().charAt(sb.length() - 1) == '\"') {
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length() - 1);
        // }
        for (String item : sb.toString().split("\",\"")) {
          if (first_line) {
            table.addColumn(item);
          } else {
            record.addItem(item);
          }
        }
        table.insertRecord(record);
        first_line = false;
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      scanner.close();
    }
    return table;
  }

  /******************************************/
  /***************** TESTING ****************/
  /******************************************/

  void testToFile() {
    Table football = new Table("Team","Goals","Points");
    Table football2 = new Table();

    Record r1 = new Record("Bristol Rovers", "2", "3");
    Record r2 = new Record("Arsenal", "9", "42");
    Record r3 = new Record("QPR", "19", "38");
    Record r4 = new Record("hoop's", "17", "1");
    Record r5 = new Record("to be, or not to be", "904", "17");

    football.insertRecord(r1);
    football.insertRecord(r2);
    football.insertRecord(r3);
    football.insertRecord(r4);
    football.insertRecord(r5);

    // instanciate in a loop
    for (int i=0; i<5; i++) {
      Record r6 = new Record(Integer.toString(i), "904", "17");
      football.insertRecord(r6);
    }

    football2 = inputCsv("test.csv");
    print(football2);
    // writeCsv(football, "test.csv");
  }

}
