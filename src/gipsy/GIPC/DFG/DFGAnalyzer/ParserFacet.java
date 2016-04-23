package gipsy.GIPC.DFG.DFGAnalyzer;

/**
 * This class is facet class to deal with the tree
 *
 * @version 1.0, by GIPSY project team.
 * @author  <a href="mailto:dingyimin@zworg.com">Yimin Ding</a>
 */
public class ParserFacet {

  SimpleNode simpleNode; // all kind of trees are referred by this node only
  DFGParser parser;
  String filename;
  String message;

  void parsing(String filename) {

    try {
      parser = new DFGParser(new java.io.FileInputStream(filename));
    }
    catch (java.io.FileNotFoundException e) {
      display("Parser :  File " + filename + " not found.");
      return;
    }

    parser.ParserStart(parser, this);

  }

  void display(String message) {

    System.out.println(message);

  }

  void treepass(SimpleNode simpleNode) {

    this.simpleNode = simpleNode;

  }

  void showtree() {

    simpleNode.dump(" ");

  }

  SimpleNode getNode() {
    return simpleNode;
  }

}
