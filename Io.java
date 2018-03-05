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
      for (int n=0; n<record.size(); n++) {
        out.write("\"" + record.getItem(n) + "\"");
        if (n != record.size() - 1) {
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
    Record headings = new Record();
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
        if (first_line && table.numPks()==0) {
          throw new Error("No primary keys. Put a '*' at the end of a column name when instantiating a table to represent a pk");
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

  void claim(boolean b) {
    if (!b) throw new Error("Test failed");
  }

  void testToFile() {
    System.out.println("Testing start");

    Table football_out = new Table("Team*","Goals  ","Points");
    Table football_in = new Table();

    Record r1 = new Record("Bristol Rovers", "2", "3");
    Record r2 = new Record("Arsenal", "9", "42");
    Record r3 = new Record("QPR", "19", "38");
    Record r4 = new Record("hoop's", "17", "1");
    Record r5 = new Record("to be, or not to be", "904", "17");

    claim(football_out.insertRecord(r1));
    claim(football_out.insertRecord(r2));
    claim(football_out.insertRecord(r3));
    claim(football_out.insertRecord(r4));
    claim(football_out.insertRecord(r5));

    // instantiate in a loop
    for (int i=0; i<5; i++) {
      Record r6 = new Record(Integer.toString(i), "904", "17");
      football_out.insertRecord(r6);
    }
    claim(football_out.size()==10);

    // write the table to a file, then output the file into a table
    // and ensure the table is correctly layed out.
    // -- Note, the table key order is flipped because of the way records are insertRecord
    // -- and then read.
    writeCsv(football_out, "test.csv");
    football_in = inputCsv("test.csv");

    claim(football_in.size()==10);
    claim(football_in.selectFieldNames().getItem(0).equals("Team*"));
    claim(football_in.selectFieldNames().getItem(1).equals("Goals"));
    claim(football_in.selectFieldNames().getItem(2).equals("Points"));
    claim(football_in.selectRecord(0).getItem(0).equals("4"));
    claim(football_in.selectRecord(3).getItem(1).equals("904"));
    claim(football_in.selectRecord(9).getItem(0).equals("Bristol Rovers"));

    System.out.println("Testing finished");
  }

}
