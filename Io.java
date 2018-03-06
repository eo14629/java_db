import java.util.*;
import java.io.*;
import java.util.stream.*;

class Io {
  private final int null_str_len = 4;

  public static void main(String args[]) {
    Io program = new Io();
    program.testToFile();
  }

  void writeCsv(Table table, String file_name) {
    File f = new File(file_name);
    FileWriter out = null;

    try {
      out = new FileWriter(f);

      writeFields(table.selectFieldNames(), out);
      for (ArrayList<String> key : table.getKeys()) {
        writeFields(table.selectRecord(table.listToString(key)), out);
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

  /////////////////////////////////////////////
  /// Still need to do multiple line prints ///
  /////////////////////////////////////////////
  void printTable(Table table) {
    int col_width[] = new int[table.selectFieldNames().size()];

    // getting max width of the columns and inserting these values into
    // the col width array for use later
    findColWidths(table, col_width);

    // print the heading and rows with the corerect spacing and decoration
    print(table, col_width);
  }

  void print(Table table, int[] col_width) {
    int table_width = IntStream.of(col_width).sum() + (2*table.selectFieldNames().size());

    printTableLine(table_width);
    theHeadings(table, col_width);
    printTableLine(table_width);
    theRecords(table, col_width);
    printTableLine(table_width);
  }

  void theHeadings(Table table, int[] col_width) {
    System.out.print('|');
    for (int i=0; i<table.selectFieldNames().size(); i++) {
      StringBuilder s_prnt = new StringBuilder(table.selectFieldNames().getItem(i));
      s_prnt = align(s_prnt, col_width, i);
      System.out.print(s_prnt.toString() + '|');
    }
    System.out.println();
  }

  void theRecords(Table table, int[] col_width) {
    for (ArrayList<String> hkey : table.getKeys()) {
      String s_hkey = table.listToString(hkey);
      System.out.print('|');
        for (int i=0; i<table.selectRecord(s_hkey).size(); i++) {
          StringBuilder s_prnt = new StringBuilder();
          if (table.selectRecord(s_hkey).getItem(i)==null) {
            s_prnt.append("NULL");
          } else {
            s_prnt.append(table.selectRecord(s_hkey).getItem(i));
          }
          s_prnt = align(s_prnt, col_width, i);
          System.out.print(s_prnt.toString() + '|');
        }
      System.out.println();
    }
  }

  void findColWidths(Table table, int[] col_width) {
    ofHeadings(table, col_width);
    ofRecords(table, col_width);
  }

  void ofHeadings(Table table, int[] col_width) {
    for (int i=0; i<table.selectFieldNames().size(); i++) {
      String s = table.selectFieldNames().getItem(i);
      col_width[i] = s.length();
    }
  }

  void ofRecords(Table table, int[] col_width) {
    for (ArrayList<String> hkey : table.getKeys()) {
      String s_hkey = table.listToString(hkey);
      for (int i=0; i<table.selectRecord(s_hkey).size(); i++) {
        if (table.selectRecord(s_hkey).getItem(i)==null) {
          if (null_str_len > col_width[i]) {
            col_width[i] = null_str_len;
          }
        } else {
          String s = table.selectRecord(s_hkey).getItem(i);
          if (s.length() > col_width[i]) {
            col_width[i] = s.length();
          }
        }
      }
    }
  }

  StringBuilder align(StringBuilder sb, int[] col_width, int index) {
    int space = col_width[index] - sb.length() + 1;
    for (int n=0; n<space; n++) {
      sb.append(' ');
    }
    return sb;
  }

  void printTableLine(int table_width) {
    for (int i=0; i<table_width; i++) {
      System.out.print('-');
    }
    System.out.println();
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

    // instantiation in a loop
    for (int i=0; i<5; i++) {
      Record r6 = new Record(Integer.toString(i), "904", "17");
      football_out.insertRecord(r6);
    }
    claim(football_out.size()==10);

    // write the table to a file, then output the file into a table
    // and ensure the table is correctly layed out.
    writeCsv(football_out, "test.csv");
    football_in = inputCsv("test.csv");

    claim(football_in.size()==10);
    claim(football_in.selectFieldNames().getItem(0).equals("Team*"));
    claim(football_in.selectFieldNames().getItem(1).equals("Goals"));
    claim(football_in.selectFieldNames().getItem(2).equals("Points"));
    claim(football_in.selectRecord("4").getItem(0).equals("4"));
    claim(football_in.selectRecord("hoop's").getItem(1).equals("17"));
    claim(football_in.selectRecord("Bristol Rovers").getItem(0).equals("Bristol Rovers"));

    printTable(football_in);

    System.out.println("Testing finished");
  }

}
