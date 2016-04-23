package gipsy.GIPC.DFG.DFGAnalyzer;

import java.io.*;
import java.util.*;

/**
 * This class is the Lucid code generator called by DFGAnalyzer
 *
 * @version 1.0, by GIPSY project team.
 * @author  <a href="mailto:dingyimin@zworg.com">Yimin Ding</a>
 */

public class LucidCodeGenerator {

  private SimpleNode snode; // my parse tree
  private RandomAccessFile rf1; // output file handle
  private String outfilename; // output file name
  private int nchild=0; // child number --> def the num on each line
  private Hashtable NodeDict=new Hashtable();
  private LucidNodeItem current;
  private int digraphflag=0; // if DIGRAPH then 1
  private int dirflag=0; // if dir then 1
  private Stack ST_parent_type=new Stack(); // push into parent node type
  private Stack ST_parent_name=new Stack(); // push into parent node name:id->image others->"image"
  private Stack ST_lastnode=new Stack(); // push into last node name
  private String scope_name;
  private String scope_label;
  private String DIRid1;
  private String DIRid2;
  private String DIRheadlabel="1";
  private String Startnode;
  private String Startscope_name;
  private Stack ST_exp=new Stack();
  private int NodeNumCount=0;
  LucidNodeItem LCnode2=new LucidNodeItem();
  int i2=1;
  // for dim
  private Hashtable dimNode=new Hashtable();
  //for gen code
  private Stack opnode=new Stack(); // remeber operator for deal first child
  private int newline=0; // if new line begin =1
  private int oprbracket=0; // if a opr open then =1
  private int dimfirstwhere=0; // if in function, before first where then =1
  //private Stack ST_scope=new Stack(); // push when enter a scope and pop when go out
  private int scope_rank_con=0; // if enter sub cluster then +1
  private Hashtable scope_rank=new Hashtable(); // store rank --> scope:rank

  /**
   * Constructor.
   */
  public LucidCodeGenerator() {
    Hashtable NodeDict=new Hashtable();
  }

  private String itos(int num) 
  {
    String nstring = String.valueOf(num);
    return nstring;
  }


  public void linkNode(LucidNodeItem lcnode, LucidNodeItem lcparent, int nchild) {
    NodeDict.put(lcnode.ID, lcnode);
    lcnode.previous=lcparent;
    if(lcparent.ht.containsKey(itos(nchild))) {
      lcparent.ht.remove(itos(nchild));
    }
    lcparent.ht.put(itos(nchild), lcnode);
  }

  public LucidNodeItem findwhere(LucidNodeItem lcnode) {
    for(; lcnode.Label!="WHERE"&&lcnode.Label!="START"; lcnode=lcnode.previous) {
    }
    return lcnode;
  }

  public LucidNodeItem findroot(LucidNodeItem lcnode) {
    for(;
        lcnode.Label!="="&&!lcnode.Label.equals("START")&&
        !lcnode.Label.equals("WHERE"); lcnode=lcnode.previous) {

    }
    return lcnode;
  }
  /**
   * This program parse the dictionary and translate it into a AST
   */
  public void genAST() {
    LucidNodeItem LCnode=new LucidNodeItem();
    LCnode=(LucidNodeItem)NodeDict.get(Startnode);
    Startscope_name="cluster0"; //LCnode.Scope;
    LCnode.Label="START";

    for(Enumeration e=NodeDict.elements(); e.hasMoreElements(); ) {
      current=(LucidNodeItem)e.nextElement();
      current.processed=0;
    }

    NodeNumCount++;
    LucidNodeItem current2=new LucidNodeItem();
    LucidNodeItem current3=new LucidNodeItem();
    current2.ID="zz"+NodeNumCount;
    current2.Label="WHERE";
    current3=(LucidNodeItem)LCnode.ht.get("1");
    current3.previous=current2;
    current2.ht.put("1", current3);
    linkNode(current2, LCnode, 1);
    genAST(current2);

    //  genAST(LCnode);
  }

  /**
   * This program parse the dictionary and translate it into a AST
   */

  public void genAST(LucidNodeItem LCnode) { // write LC
    int skiploop=0;

    if(LCnode.processed!=0) {
      skiploop=1;
    }
    else {
      LCnode.setprocessed(1);
    }

    if(skiploop==0) {
      if(!LCnode.ht.isEmpty()) {
        for(int i=1; i<=LCnode.ht.size(); ++i) { // process all children
          LucidNodeItem n2=(LucidNodeItem)LCnode.ht.get(itos(i));
          if(n2!=null) {
// insert sth...
            ////////
            if(n2.Label=="=") { // enter a new scope from "="
              Startscope_name=n2.Scope;
            }


            int ee1=((Integer)scope_rank.get(n2.Scope)).intValue();
            int ee2=((Integer)scope_rank.get(Startscope_name)).intValue();


            if(ee1>ee2&&n2.Scope!=Startscope_name&&n2.previous.Label!="=") {

              // when entry a new scope not from "="
              // then add a node, add "where", add "=", move subtree
              ////find parent "=" or "start" or "where"
              LucidNodeItem currentps=new LucidNodeItem();
              currentps=findroot(n2);
              ////add "where"
              NodeNumCount++;
              LucidNodeItem currentw2=new LucidNodeItem();
              LucidNodeItem currentw3;
              currentw2.ID="zz"+NodeNumCount;
              currentw2.Label="WHERE";

              if(currentps.Label.equals("=")) {
                currentw3=(LucidNodeItem)currentps.ht.get("2");
                linkNode(currentw3, currentw2, 1);
                linkNode(currentw2, currentps, 2);
              }


              ////add a new node
              NodeNumCount++;
              LucidNodeItem current2=new LucidNodeItem();
              current2.ID="zz"+NodeNumCount;
              current2.Label=n2.Scope_label;
              current2.Shape="var";
              current2.Scope=Startscope_name;
              linkNode(current2, LCnode, i);

              //// add branch
              /////add "="
              NodeNumCount++;
              LucidNodeItem current3=new LucidNodeItem();
              current3.ID="zz"+NodeNumCount;
              current3.Label="=";
              current3.setScope(n2.Scope);
              current3.setScope_label(n2.Scope_label);
              LucidNodeItem wherenode=new LucidNodeItem();
              wherenode=findroot(LCnode);
              linkNode(current3, wherenode, wherenode.ht.size()+1);

              /////add label
              NodeNumCount++;
              LucidNodeItem current4=new LucidNodeItem();
              current4.ID="zz"+NodeNumCount;
              current4.Label=n2.Scope_label;
              current4.Shape="var";
              current4.setScope(Startscope_name);
              linkNode(current4, current3, 1);

              /////move subtree
              linkNode(n2, current3, 2);


            }
            else {


              ////////
              genAST(n2); // process each child
            }
          }
        }
      }
    }

/////////////
  }
  /**
   * This program parse the dictionary and eliminate point node
   */

  public void genPoint() {
    LucidNodeItem LCnode=new LucidNodeItem();
    LCnode=(LucidNodeItem)NodeDict.get(Startnode);
    Startscope_name=LCnode.Scope;

    for(Enumeration e=NodeDict.elements(); e.hasMoreElements(); ) {
      current=(LucidNodeItem)e.nextElement();
      current.processed=0;
    }

    genPoint(LCnode);
  }
  /**
   * This program parse the dictionary and eliminate point node
   */

  public void genPoint(LucidNodeItem LCnode) { // write LC
    int skiploop=0;


    if(LCnode.processed!=0) {
      skiploop=1;

      LucidNodeItem currentp; //=new LucidNodeItem();
      currentp=LCnode.previous;
      int ii;
      for(ii=1; ii<=currentp.ht.size(); ++ii) {
        LucidNodeItem n3=(LucidNodeItem)currentp.ht.get(itos(ii));
        if(n3.ID==LCnode.ID) {
          break;
        }
      }
      //     linkNode(LCnode2, currentp, ii);

      int ee1=((Integer)scope_rank.get(LCnode.Scope)).intValue();
      int ee2=((Integer)scope_rank.get(currentp.Scope)).intValue();
      if(ee1>ee2) {
        NodeNumCount++;
        LucidNodeItem currentn=new LucidNodeItem();
        currentn.ID="zzA"+NodeNumCount;
        currentn.Label=LCnode.Scope_label;
        currentn.Shape="var";
        currentn.setScope(LCnode.Scope);
        currentn.setScope_label(LCnode.Scope_label);
        linkNode(currentn, LCnode2, i2);
      }
      else {
        NodeNumCount++;
        LucidNodeItem currentn=new LucidNodeItem();
        currentn.ID="zzB"+NodeNumCount;
        currentn.Label=LCnode.Label;
        currentn.Shape="var";
        currentn.setScope(LCnode.Scope);
        currentn.setScope_label(LCnode.Scope_label);
        linkNode(currentn, LCnode2, i2);

      }

    }
    else {
      LCnode.setprocessed(1);

      if(LCnode.Shape.equals("point")&&LCnode.ht.size()==1) {
        LucidNodeItem currentp0; //=new LucidNodeItem();
        LucidNodeItem currentc0; //=new LucidNodeItem();
        currentp0=LCnode.previous;
        currentc0=(LucidNodeItem)LCnode.ht.get("1");
        int ii;
        for(ii=1; ii<=currentp0.ht.size(); ++ii) {
          LucidNodeItem n3=(LucidNodeItem)currentp0.ht.get(itos(ii));
          if(n3.ID==LCnode.ID) {
            break;
          }
        }
        linkNode(currentc0, currentp0, ii);


      }

    }

    if(skiploop==0) {
      if(!LCnode.ht.isEmpty()) {
        LCnode2=LCnode;
        for(int i=1; i<=LCnode.ht.size(); ++i) { // process all children
          i2=i;
          LucidNodeItem n2=(LucidNodeItem)LCnode.ht.get(itos(i));
          if(n2!=null) {
// insert sth...
            genPoint(n2); // process each child
          }
        }
      }
    }

/////////////
  }

  public void gendim() {
    LucidNodeItem LCnode=new LucidNodeItem();
    LCnode=(LucidNodeItem)NodeDict.get(Startnode);
    Startscope_name=LCnode.Scope;

    gendim(LCnode);
  }

  public void gendim(LucidNodeItem LCnode) { // write LC

    if(!LCnode.ht.isEmpty()) {
      for(int i=1; i<=LCnode.ht.size(); ++i) { // process all children
        LucidNodeItem n2=(LucidNodeItem)LCnode.ht.get(itos(i));
        if(n2!=null) {
// insert sth...
          gendim(n2); // process each child
        }
      }
    }
    if(LCnode.Shape!=null&&LCnode.Shape.equals("ellipse")) {
      // add it to subtree
      LucidNodeItem currentroot;
      currentroot=findwhere(LCnode);
      int ii, ii2=0;
      for(ii=1; ii<=currentroot.ht.size(); ++ii) {
        LucidNodeItem n3=(LucidNodeItem)currentroot.ht.get(itos(ii));
        if(n3.Label.equals("dimension")) {
          ii2=1;
          break;
        }
      }
      // new node
      NodeNumCount++;
      LucidNodeItem currentn=new LucidNodeItem();
      currentn.ID="zz"+NodeNumCount;
      currentn.Label=LCnode.Label;
      currentn.Shape="var";

      int n=1;

      if(ii2==1) { //find dim node
        LucidNodeItem currentdim0=(LucidNodeItem)currentroot.ht.get(itos(ii));
        ///find dim child exit?
        int iidim, ii2dim=0;
        for(iidim=1; iidim<=currentdim0.ht.size(); ++iidim) {
          LucidNodeItem n4=(LucidNodeItem)currentdim0.ht.get(itos(iidim));
          if(n4.Label.equals(currentn.Label)) {
            ii2dim=1;
            break;
          }
        }

        ///
        if(ii2dim==0) {
          linkNode(currentn, currentdim0, currentdim0.ht.size()+1);
        }
      }
      else { // no dim node now
        NodeNumCount++;
        LucidNodeItem currentdim=new LucidNodeItem();
        currentdim.ID="zz"+NodeNumCount;
        currentdim.Label="dimension";
        currentdim.Shape="box";
        linkNode(currentdim, currentroot, ii);
        linkNode(currentn, currentdim, n);

      }
      dimNode.put(currentn.Label, currentn);

    }


/////////////
  }

  public void gencode() {
    LucidNodeItem LCnode=new LucidNodeItem();
    LCnode=(LucidNodeItem)NodeDict.get(Startnode);
    Startscope_name=LCnode.Scope;
//    ST_scope.push(LCnode.Scope);
    if(!LCnode.Scope_label.equals("START")) {
      dimfirstwhere=1;
      try {
        rf1.writeBytes(LCnode.Scope_label.substring(9)+": ");
      }
      catch(Exception e) {}
    }


    gencode(LCnode, " ");
  }

  public void gencode(LucidNodeItem LCnode, String prtspace1) { // write LC
    String prtspace2=" "; // space between each word
    String prtspace=prtspace1;

    if(!LCnode.ht.isEmpty()) {
      prtspace=prtspace+" ";
      opnode.push(LCnode.Label);

      if(!LCnode.Label.equals("where")&&!LCnode.Label.equals("=")&&
         !LCnode.Label.equals("START")&&!LCnode.Label.equals("dimension")&&
         !LCnode.previous.Label.equals("where")&&
         !LCnode.previous.Label.equals("=")&&
         !LCnode.previous.Label.equals("START")&&
         !LCnode.previous.Label.equals("dimension")&&!LCnode.Label.equals("#")) {

        ////newline
        if(newline==1) {
          prtspace2=prtspace;
          newline=0;
        }

        try {
          rf1.writeBytes(prtspace2+"(");
        }
        catch(Exception e) {}

      }

      for(int i=1; i<=LCnode.ht.size(); ++i) { // process all children
        LucidNodeItem n2=(LucidNodeItem)LCnode.ht.get(itos(i));
        if(n2!=null) {
// insert sth...
          ////add if then else fi
          if(LCnode.Label.equals("if")&&i>1) {
            String ifstr="";
            if(i==2) {
              ifstr="then";
            }
            if(i==3) {
              ifstr="else";
            }

            try {
              rf1.writeBytes(prtspace2+ifstr);
            }
            catch(Exception e) {}


          }

          if(LCnode.ht.size()==1||opnode.peek().equals("dimension")||
             opnode.peek().equals("if")) { // a father oper with only one child
            if(LCnode.Label.equals("where")) {
              opnode.pop();
            }
            else {
              ////code
              if(!LCnode.Label.equals("START")) {
                ////newline
                if(newline==1) {
                  prtspace2=prtspace;
                  newline=0;
                }

                try {
                  rf1.writeBytes(prtspace2+opnode.pop());
                }
                catch(Exception e) {}
              }
            }
            gencode(n2, prtspace); // process each child
          }
          else { // a oper with more than one child
            gencode(n2, prtspace); // process each child
            ///add "fi"
            if(LCnode.Label.equals("if")&&i==LCnode.ht.size()) {
              try {
                rf1.writeBytes(prtspace2+"fi");
              }
              catch(Exception e) {}

            }
            if(i==1&&!opnode.peek().equals("START")) { //first node
              if(LCnode.Label.equals("where")) {
                ////code
                prtspace2=prtspace;
                try {
                  rf1.writeBytes("\n"+prtspace2+opnode.pop()+"\n");
                  newline=1;
                }
                catch(Exception e) {}
              }
              else {

                ////newline
                if(newline==1) {
                  prtspace2=prtspace;
                  newline=0;
                }

                ////code
                try {
                  rf1.writeBytes(prtspace2+opnode.pop());
                }
                catch(Exception e) {}

              }
            }
          }
        }
      }


//// add )
      if(!LCnode.Label.equals("where")&&!LCnode.Label.equals("=")&&
         !LCnode.Label.equals("START")&&!LCnode.Label.equals("dimension")&&
         !LCnode.previous.Label.equals("where")&&
         !LCnode.previous.Label.equals("=")&&
         !LCnode.previous.Label.equals("START")&&
         !LCnode.previous.Label.equals("dimension")&&!LCnode.Label.equals("#")) {
        ////newline
        if(newline==1) {
          prtspace2=prtspace;
          newline=0;
        }

        try {
          rf1.writeBytes(prtspace2+")");
        }
        catch(Exception e) {}

      }


      if(LCnode.previous!=null&&LCnode.previous.Label.equals("where")) {
        int ii; // which number child of "where"
        for(ii=1; ii<=LCnode.previous.ht.size(); ++ii) {
          LucidNodeItem n3=(LucidNodeItem)LCnode.previous.ht.get(itos(ii));
          if(n3.Label.equals(LCnode.Label)) {
            break;
          }
        }

        if(ii>1) {

          ////code
          try {
            rf1.writeBytes(";\n");
            newline=1;
          }
          catch(Exception e) {}

        }
      }

      if(LCnode.Label.equals("where")&&LCnode.ht.size()>1) {
        ////newline
        if(newline==1) {
          prtspace2=prtspace;
          newline=0;
        }

        ////code
        try {
          rf1.writeBytes(prtspace2+"end");
        }
        catch(Exception e) {}
      }
    }

    else {
      ////newline
      if(newline==1) {
        prtspace2=prtspace;
        newline=0;
      }

      ////code
      try {
        rf1.writeBytes(prtspace2+LCnode.Label);
        //if mult dim then add ,
        if(LCnode.previous.Label.equals("dimension")&&
           LCnode.previous.ht.size()>1) {
          int ii; // which number child of "where"
          for(ii=1; ii<=LCnode.previous.ht.size(); ++ii) {
            LucidNodeItem n3=(LucidNodeItem)LCnode.previous.ht.get(itos(ii));
            if(n3.Label.equals(LCnode.Label)) {
              break;
            }
          }
          if(ii<LCnode.previous.ht.size()) {
            rf1.writeBytes(",");
          }
        }

      }
      catch(Exception e) {}

    }


/////////////
  }

  public void genname() {
    LucidNodeItem LCnode=new LucidNodeItem();
    LCnode=(LucidNodeItem)NodeDict.get(Startnode);
    Startscope_name=LCnode.Scope;

    genname(LCnode);
  }

  public void genname(LucidNodeItem LCnode) { // write LC

    if(LCnode.Label.equals("WHERE")) {
      LCnode.Label="where";
    }
    if(LCnode.Label.equals("AT")) {
      LCnode.Label="@";
    }
    if(LCnode.Label.equals("WHEN")) {
      LCnode.Label="#";
    }
    if(LCnode.Label.equals("ADD")) {
      LCnode.Label="+";
    }
    if(LCnode.Label.equals("MINUS")) {
      LCnode.Label="-";
    }
    if(LCnode.Label.equals("STAR")) {
      LCnode.Label="*";
    }
    if(LCnode.Label.equals("SLASH")) {
      LCnode.Label="\\";
    }
    if(LCnode.Label.equals("IF")) {
      LCnode.Label="if";
    }
    if(LCnode.Label.equals("EQ")) {
      LCnode.Label="==";
    }
    if(LCnode.Label.equals("HASH")) {
      LCnode.Label="#";
    }
    if(LCnode.Label.equals("MIN")) {
      LCnode.Label="-";
    }


    if(dimNode.containsKey(LCnode.Label)&&
       !LCnode.previous.Label.equals("dimension")&&
       (LCnode.previous.Label.equals("#")||LCnode.previous.Label.equals("@"))) {
      LCnode.Label="."+LCnode.Label;
    }

    if(!LCnode.ht.isEmpty()) {
      for(int i=1; i<=LCnode.ht.size(); ++i) { // process all children
        LucidNodeItem n2=(LucidNodeItem)LCnode.ht.get(itos(i));
        if(n2!=null) {
// insert sth...
          genname(n2); // process each child
        }
      }
    }


/////////////
  }

  public void prtAST() {
    LucidNodeItem LCnode=new LucidNodeItem();
    LCnode=(LucidNodeItem)NodeDict.get(Startnode);
    Startscope_name=LCnode.Scope;

    prtAST(LCnode, " ");
  }

  public void prtAST(LucidNodeItem LCnode, String prtspace1) { // write LC

    String prtspace=prtspace1;

    if(!LCnode.ht.isEmpty()) {
      prtspace=prtspace+" ";
      for(int i=1; i<=LCnode.ht.size(); ++i) { // process all children
        LucidNodeItem n2=(LucidNodeItem)LCnode.ht.get(itos(i));
        if(n2!=null) {
// insert sth...
          prtAST(n2, prtspace); // process each child
        }
      }
    }


/////////////
  }


  public void genTable() { // generate a NodeDict table
    genTable(snode);
  }

  public void genTable(SimpleNode n) {
    //////////////////  every node
    String treecon;
    treecon=DFGParserTreeConstants.jjtNodeName[n.id];

// insert sth...
    int scopeskip=0; // if want skip scope then 1
    LucidNodeItem current=new LucidNodeItem();

    if(n.getImage()==null) {
      n.setImage(" ");
    }

    if(!ST_lastnode.isEmpty()&&!ST_parent_type.isEmpty()) {
      if(digraphflag==1) { //digraph level 1
        if(!(treecon=="ID"&&n.getImage().equals("G"))) { //filter "G"
          if(treecon=="ID"&&ST_parent_type.peek()!="ID") { //filter id->id

            if(ST_parent_type.peek()!="ASSIGN") {
              current.setID(n.getImage());
              NodeDict.put(n.getImage(), current);
            }
            else { // into NodeDict
              if(ST_lastnode.peek().equals("label")) {
                String id_name1=(String)ST_parent_name.pop();
                String id_name=(String)ST_parent_name.peek();
                ST_parent_name.push(id_name1);
                current=(LucidNodeItem)NodeDict.get(id_name);
                current.setLabel(n.getImage());
                NodeDict.put(id_name, current);
              }

              if(ST_lastnode.peek().equals("shape")) {
                String id_name1=(String)ST_parent_name.pop();
                String id_name=(String)ST_parent_name.peek();
                ST_parent_name.push(id_name1);
                current=(LucidNodeItem)NodeDict.get(id_name);
                current.setShape(n.getImage());
                NodeDict.put(id_name, current);
                if(n.getImage().equals("house")) {
                  Startnode=id_name;
                }
              }
            }
          }
        }
      }
      else { // digraphflag==0 -> sub
        if(ST_parent_type.peek()=="SUB") { //sub level 1
          if(treecon=="ID") { // sub -> id
            if(n.getImage().length()>7&&n.getImage().substring(0, 7).equals("cluster")) {
              scope_name=n.getImage().substring(0, n.getImage().length()); // get scope name
              // store clusterrank
              //    scope_rank_con++;
              Integer rankint=new Integer(scope_rank_con);
              scope_rank.put(scope_name, rankint);
              //
              //
            }
            else { // insert scope name into dict  // graph level 0
              current=(LucidNodeItem)NodeDict.get(n.getImage());
              if(current.graphlevel==0) {
                current.setScope(scope_name);
                current.setScope_label(scope_label);
                NodeDict.put(n.getImage(), current);
              }
            }
          }
        }
        else {
          if(ST_parent_type.peek()=="ASSIGN") {
            String id_type1=(String)ST_parent_type.pop();
            String id_type=(String)ST_parent_type.peek();
            ST_parent_type.push(id_type1);
            if(id_type=="GRAPH"&&ST_lastnode.peek().equals("label")) {
              this.scope_label=n.getImage();
            }
          }
          if(ST_parent_type.peek()=="ID"&&treecon=="ID") { //graph level 1
            current=(LucidNodeItem)NodeDict.get(n.getImage());
            if(current.graphlevel==0) {
              current.setScope(scope_name);
              current.setScope_label(scope_label);
              current.graphlevel=1;
              NodeDict.put(n.getImage(), current);
            }
          }
        }
      }
    }

    if(dirflag==1) {
      // dir level 1
      if(treecon=="ID") {
        if(!ST_parent_type.peek().equals("ASSIGN")) {
          if(ST_parent_type.peek().equals("DIR")) {
            if(DIRid1==null) {
              DIRid1=n.getImage();
            }
            else {
              DIRid2=n.getImage();
            }
          }
        }
        else {
          if(ST_lastnode.peek().equals("headlabel")) {
            if(n.getImage()==null||n.getImage()==""||n.getImage()==" ") {
              DIRheadlabel="1";
            }
            else {
              DIRheadlabel=n.getImage();
            }
          }
        }
      }
    }


    if(treecon=="DIGRAPH") {
      digraphflag=1;

      //   scope_rank_con=1;
      Integer rankint2=new Integer(scope_rank_con);
      scope_rank.put("cluster0", rankint2);
    }

    if(treecon=="SUB") {
      digraphflag=0;
    }

    if(digraphflag==1) {
      if(treecon=="GRAPH"||treecon=="NODE"||treecon=="EDGE") {
        scopeskip=1;
      }
    }
    if(treecon=="DIR") {
      dirflag=1;
    }

    ST_lastnode.push(n.getImage());

    if(n.children!=null&&scopeskip==0) {
      for(int i=0; i<n.children.length; ++i) { // process all children
        nchild=i+1;
        SimpleNode n2=(SimpleNode)n.children[i];
        if(n2!=null) {
// insert sth...
          ST_parent_type.push(treecon);
          ST_parent_name.push(n.getImage());
          if(ST_parent_type.peek().equals("SUB")) {
            scope_rank_con++;
          }
          genTable(n2); // process each child
          //
          if(ST_parent_type.peek().equals("SUB")) {
            scope_rank_con--;
          }
          //
          ST_parent_name.pop();
          ST_parent_type.pop();
        }
      }
    }
// insert sth...
    if(treecon=="DIR") {
      ///
      LucidNodeItem current1=new LucidNodeItem();
      LucidNodeItem current2=new LucidNodeItem();
      current1=(LucidNodeItem)NodeDict.get(DIRid1);
      current2=(LucidNodeItem)NodeDict.get(DIRid2);
      if(current1.previous==null) {
        current1.setprevious(current2);
      }
      current2.setht(DIRheadlabel, current1);
      ///
      DIRid1=null;
      DIRid2=null;
      DIRheadlabel="1";
    }

  }

  public void genTableclose() {
  }

//////////////////////////////////////////////////////////////////////
  public void genLC(SimpleNode simplenode, String filename) {
    snode=simplenode;

    if(filename==null) {
      outfilename="../../tests/lucid/LCtestout1.ipl";
    }
    else {
      outfilename=filename;
    }

    try {
      RandomAccessFile rf0=new RandomAccessFile(outfilename, "rw");
      rf0.setLength(0);
      rf0.close();
      rf1=new RandomAccessFile(outfilename, "rw");
    }
    catch(Exception e) {}

    genTable();
    genTableclose();

    if(!NodeDict.isEmpty()) {
      LucidNodeItem current=new LucidNodeItem();
      for(Enumeration e=NodeDict.elements(); e.hasMoreElements(); ) {
        current=(LucidNodeItem)e.nextElement();
      }
    }
    else {

    }

    genPoint();

    genAST();

    gendim();

    genname();

    gencode();

    prtAST(); // show dump out

  }

}
