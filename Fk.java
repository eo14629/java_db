import java.util.*;

class Fk<A, B, C, D> {
  private final A fk_table;
  private final B fk_col;
  private final C ref_table;
  private final D ref_col;

  Fk(final A fk_table, final B fk_col, final C ref_table, final D ref_col) {
    this.fk_table = fk_table;
    this.fk_col = fk_col;
    this.ref_table = ref_table;
    this.ref_col = ref_col;
  }

  A getFkTable() {
    return fk_table;
  }

  B getFkCol() {
    return fk_col;
  }

  C getRefTable() {
    return ref_table;
  }

  D getRefCol() {
    return ref_col;
  }

  //
  // Override 'equals', 'hashcode' and 'toString'
  //

  public static void main(String args[]) {
    Fk<String,String,String,String> program = new Fk<String,String,String,String>("yes","no","maybe","?");
    program.testFk();
  }

  void claim(boolean b) {
    if (!b) throw new Error("Test failed");
  }

  void testFk() {
    System.out.println("Testing Started");

    List<Fk<String,String,String,String>> l1 = new ArrayList<Fk<String,String,String,String>>();
    Fk<String,String,String,String> fk1 = new Fk<String,String,String,String>("?","yes","no","maybe");
    claim(fk1.getFkTable().equals("?"));
    claim(fk1.getFkCol().equals("yes"));
    claim(fk1.getRefTable().equals("no"));
    claim(fk1.getRefCol().equals("maybe"));

    claim(l1.add(fk1));
    claim(l1.get(0).getFkCol().equals("yes"));

    System.out.println("Testing Finished");
  }
}
