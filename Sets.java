import java.util.*;

class Sets {
  private Set<ArrayList<Integer>> set = new HashSet<ArrayList<Integer>>();

  public static void main(String args[]) {
    Sets program = new Sets();
    program.testSets();
  }

  void claim(boolean b) {
    if (!b) throw new Error("Test failed");
  }

  void testSets() {
    ArrayList<Integer> key1 = new ArrayList<Integer>();
    key1.add(0);
    key1.add(0);
    claim(set.add(key1));

    ArrayList<Integer> key2 = new ArrayList<Integer>();
    key2.add(0);
    key2.add(1);
    claim(set.add(key2));

    ArrayList<Integer> key3 = new ArrayList<Integer>();
    key3.add(1);
    key3.add(0);
    claim(set.add(key3));

    ArrayList<Integer> key4 = new ArrayList<Integer>();
    key4.add(1);
    key4.add(1);
    claim(set.add(key4));

    ArrayList<Integer> key5 = new ArrayList<Integer>();
    key5.add(0);
    key5.add(2);
    claim(set.add(key5));

    ArrayList<Integer> key6 = new ArrayList<Integer>();
    key6.add(2);
    key6.add(0);
    claim(set.add(key6));

    ArrayList<Integer> key7 = new ArrayList<Integer>();
    key7.add(2);
    claim(set.add(key7));

    ArrayList<Integer> key8 = new ArrayList<Integer>();
    key8.add(2);
    key8.add(0);
    claim(! set.add(key8));
  }
}
