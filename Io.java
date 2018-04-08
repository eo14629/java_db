// An I/O class responsible for complex printing to the screen, file writing and file reading.

import java.util.*;
import java.io.*;
import java.util.stream.*;

class Io {
  private final int null_str_len = 4;

  public static void main(String args[]) {
    Io program = new Io();
    program.testIo();
  }

  // will print in alphabetical order due to the nature of the TreeMap of the database
  void showTables(Database d) {
    System.out.println("Available Tables in the " + d.getName() + " Database:");
    for (String key : d.getKeys()) {
       System.out.println(key);
    }
  }

  // make the directory for the database and then input all the related tables.
  void mkDir(Database d) {
    File theDir = new File(d.getName());

    if (! theDir.exists()) {
      System.out.println("creating directory: " + theDir.getName());
      boolean result = false;
      try{
        theDir.mkdir();
        result = true;
      }
      catch(SecurityException se){}
      if(result) {
        System.out.println("directory created");
      } else {
        throw new Error("directory failed to create");
      }
    } else {
      throw new Error("directory already exists");
    }

    for (String key : d.getKeys()) {
      writeCsv(d.select(key), key, d);
    }
  }

  // write a table to a csv file.
  void writeCsv(Table table, String file_name, Database d) {
    File f = new File(d.getName() + "/" + file_name);
    FileWriter out = null;

    try {
      out = new FileWriter(f);

      writeFields(table.selectFieldNames(), out);
      for (ArrayList<String> key : table.getKeys()) {
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

  // assumed here that the format of the csv is double quoted fields separated by commas.
  // This is because the writeCsv method converts tables into this format
  Table readCsv(String file_name, Database d) {
    File file = new File(d.getName() + "/" + file_name);
    Scanner scanner = null;
    Table table = new Table();
    boolean first_line = true;

    try {
      scanner = new Scanner(file);

      while(scanner.hasNextLine()) {
        Record record = new Record();
        StringBuilder sb = new StringBuilder();

        sb.append(scanner.nextLine());
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length() - 1);
        for (String item : sb.toString().split("\",\"")) {
          if (first_line) {
            table.addColumn(item);
            System.out.println("col_add");
          } else {
            record.addItem(item);
          }
        }
        if (first_line && table.numPks()==0) {
          throw new Error("No primary keys. Put a '*' at the end of a column name when instantiating a table to represent a pk");
        }
        System.out.println("insert");
        if (! first_line) {
          table.insertRecord(record);
        }
        first_line = false;
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      scanner.close();
    }
    return table;
  }

  // printing the table neatly.
  void printTable(Table table) {
    int col_width[] = new int[table.selectFieldNames().size()];
    findColWidths(table, col_width);
    print(table, col_width);
  }

  // getting max width of the columns and inserting these values into
  // the col width array for use later
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
      // String s_hkey = table.listToString(hkey);
      for (int i=0; i<table.selectRecord(hkey).size(); i++) {
        if (table.selectRecord(hkey).getItem(i)==null) {
          if (null_str_len > col_width[i]) {
            col_width[i] = null_str_len;
          }
        } else {
          String s = table.selectRecord(hkey).getItem(i);
          if (s.length() > col_width[i]) {
            col_width[i] = s.length();
          }
        }
      }
    }
  }

  // print the heading and rows with the corerect spacing and decoration
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
      // String s_hkey = table.listToString(hkey);
      System.out.print('|');
        for (int i=0; i<table.selectRecord(hkey).size(); i++) {
          StringBuilder s_prnt = new StringBuilder();
          if (table.selectRecord(hkey).getItem(i)==null) {
            s_prnt.append("NULL");
          } else {
            s_prnt.append(table.selectRecord(hkey).getItem(i));
          }
          s_prnt = align(s_prnt, col_width, i);
          System.out.print(s_prnt.toString() + '|');
        }
      System.out.println();
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

  void testIo() {
    System.out.println("Testing start");

    Database d = new Database("Football_Io");

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
    mkDir(d);
    writeCsv(football_out, "test.csv", d);
    football_in = readCsv("test.csv", d);

    claim(football_in.size()==10);
    claim(football_in.selectFieldNames().getItem(0).equals("Team*"));
    claim(football_in.selectFieldNames().getItem(1).equals("Goals"));
    claim(football_in.selectFieldNames().getItem(2).equals("Points"));
    claim(football_in.selectItem(football_in.keyGen("4"),"Team*").equals("4"));
    claim(football_in.selectItem(football_in.keyGen("hoop's"),"Goals").equals("17"));
    claim(football_in.selectItem(football_in.keyGen("Bristol Rovers"),"Team*").equals("Bristol Rovers"));

    printTable(football_in);

    System.out.println("Testing finished");
  }

}
