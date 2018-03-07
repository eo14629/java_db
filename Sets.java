import java.util.*;

class Sets {
  private Set<ArrayList<String>> set = new HashSet<ArrayList<String>>();

  public static void main(String args[]) {
    Sets program = new Sets();
    program.testSets();
  }

  void claim(boolean b) {
    if (!b) throw new Error("Test failed");
  }

  void testSets() {
    ArrayList<String> key1 = new ArrayList<String>();
    key1.add("0");
    key1.add("0");
    claim(set.add(key1));

    ArrayList<String> key2 = new ArrayList<String>();
    key2.add("0");
    key2.add("1");
    claim(set.add(key2));

    ArrayList<String> key3 = new ArrayList<String>();
    key3.add("1");
    key3.add("0");
    claim(set.add(key3));

    ArrayList<String> key4 = new ArrayList<String>();
    key4.add("1");
    key4.add("1");
    claim(set.add(key4));

    ArrayList<String> key5 = new ArrayList<String>();
    key5.add("0");
    key5.add("2");
    claim(set.add(key5));

    ArrayList<String> key6 = new ArrayList<String>();
    key6.add("2");
    key6.add("0");
    claim(set.add(key6));

    ArrayList<String> key7 = new ArrayList<String>();
    key7.add("2");
    claim(set.add(key7));

    System.out.println(set);

    ArrayList<String> key8 = new ArrayList<String>();
    key8.add("2");
    key8.add("0");
    claim(! set.add(key8));

    ArrayList<String> key9 = new ArrayList<String>();
    key9.add("0");
    key9.add("0");
    claim(! set.add(key9));
  }
}
