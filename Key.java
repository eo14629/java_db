import java.util.*;

// just a simple wrapper class for readablility.
// could make this an extension of Record??????????
// cannot use this class as the Set functionality does not work very smoothly with it.
// the key selection will unfortunately be verbose and ugly

class Key {
  private Record key = new Record();

  public static void main(String args[]) {
    Key program = new Key();
    program.testKey();
  }

  Key(String... keys) {
    for (String key : keys) {
      this.key.addItem(key);
    }
  }

  int size() {
    return key.size();
  }

  String get(int index) {
    return key.getItem(index);
  }

  boolean add(String value) {
    return key.addItem(value);
  }

  /******************************************/
  /***************** TESTING ****************/
  /******************************************/

  void claim(boolean b) {
    if (!b) throw new Error("Test failed");
  }

  void testKey() {
    System.out.println("Testing Started");

    Key key1 = new Key("QPR");
    claim(key1.size()==1);
    claim(key1.get(0).equals("QPR"));
    claim(key1.add("Championship"));
    claim(key1.size()==2);
    claim(key1.get(1).equals("Championship"));

    Key key2 = new Key("QPR","Premier League");
    claim(key2.size()==2);
    claim(key2.get(0).equals("QPR"));
    claim(key2.get(1).equals("Premier League"));

    System.out.println("Testing Finished");
  }
}
