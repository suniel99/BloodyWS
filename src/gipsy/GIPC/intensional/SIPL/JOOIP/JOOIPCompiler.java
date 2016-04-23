package gipsy.GIPC.intensional.SIPL.JOOIP;

import gipsy.GIPC.GIPC;
import gipsy.GIPC.GIPCException;
import gipsy.GIPC.SemanticAnalyzer;
import gipsy.GIPC.DFG.DFGException;
import gipsy.GIPC.intensional.IntensionalCompiler;
import gipsy.GIPC.intensional.SimpleNode;
import gipsy.GIPC.intensional.GIPL.GIPLCompiler;
import gipsy.interfaces.AbstractSyntaxTree;
import gipsy.storage.Dictionary;
import gipsy.storage.DictionaryItem;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;


/**
 * Main() program for hybrid OOIP program translation.
 * First step is pattern match and to extract the text between /@...@/. 
 *
 * @author Aihua Wu 
 *
 * @version $Id: JOOIPCompiler.java,v 1.18 2013/01/09 14:53:49 mokhov Exp $
 * @since October 2007
 * XXX: JOOIPCompiler does not conform to IntensionalCompiler; a must fix
 */
public class JOOIPCompiler
//extends IntensionalCompiler
{	
	public static Vector oIntermidateFileParseOutput = new Vector();
	public static Hashtable<String, JOOIPToJavaTranslationItem> oIntensionalIdentifierTable = new Hashtable<String, JOOIPToJavaTranslationItem>();
	public boolean bIsJooip = true;		
	
	
	public static JOOIPToJavaTranslationItem oItem;
	public static Dictionary oIPLDictionary = new Dictionary();
	public static String strTempDimen = "";
	
	public void JOOIPCompiler()
	{
	}	
	
	public static void linkIPL(SimpleNode opNode)
	{
		boolean bIsChild = false;
		boolean bIsFound = false;
		String strTempImage = "";
		String strTempDimGen = "";
		SimpleNode oTempNode;
		DictionaryItem oDicTemp;
		JavaClassSymbolTable oJavaTempM;
		JavaIdentifierSymbolTable oJavaI;
		
		for(int iCount = 0; iCount < opNode.children.length; iCount++)
		{
			oTempNode = (SimpleNode)opNode.children[iCount];			
			if (oTempNode.children == null)
			{
				if (oTempNode.toString().substring(0,oTempNode.toString().indexOf(":")).trim().equals("ID"));
				strTempImage = oTempNode.getImage();
				for (Integer iDic=0;iDic<oIPLDictionary.size();iDic++)
				{
					oDicTemp = (DictionaryItem)oIPLDictionary.elementAt(iDic);					
					if (oDicTemp.getName().equals(strTempImage)) 
					{
						bIsFound = true;
						iDic = oIPLDictionary.size();
					}					
				}
				if (!bIsFound) {
					for (Integer iJavaMC=0;iJavaMC<oIntermidateFileParseOutput.size();iJavaMC++)
					{
						oJavaTempM = (JavaClassSymbolTable)oIntermidateFileParseOutput.elementAt(iJavaMC);
						if (oItem.strJavaClassName.equals(oJavaTempM.strClassName)){
							for(Integer iJavaIdentifierNo = 0; iJavaIdentifierNo<oJavaTempM.oMemberTable.size(); iJavaIdentifierNo++ )
							{	
								oJavaI = (JavaIdentifierSymbolTable)oJavaTempM.oMemberTable.get(oJavaTempM.oMemberTable.keySet().toArray()[iJavaIdentifierNo]);						
								/*if (strTempImage.equals(oJavaI.strID)){									
									strTempDimen = strTempDimen.concat("\t\t"+oItem.strIntensionalID.replace("IPL_CODE_","soGEER")+
											".getDictionary().addAll(" + oJavaI.strClassInit.replace("IPL_CODE_","soGEER")+
											".getDictionary());\n");	
									strTempDimen = strTempDimen.concat("\t\tSystem.out.println(\"-----------------------\");\n");
									strTempDimGen = "\t\t"+ oJavaI.strClassInit.replace("IPL_CODE_","soGEER")+ ".getAbstractSyntaxTrees()[0].showTree();\n" ;
									if (!strTempDimen.contains(strTempDimGen)) 
										{
											strTempDimen = strTempDimen.concat(strTempDimGen);
											strTempDimen = strTempDimen.concat("\t\tSystem.out.println(\"-----------------------\");\n");
										}
									strTempDimGen = "\t\tSystem.out.println("+ oJavaI.strClassInit.replace("IPL_CODE_","soGEER")+ ".getAbstractSyntaxTrees()[0].getRoot().jjtGetChild(0));" ;
									if (!strTempDimen.contains(strTempDimGen)) 
									{
										strTempDimen = strTempDimen.concat(strTempDimGen);
										strTempDimen = strTempDimen.concat("\t\tSystem.out.println(\"-----------------------\");\n");
									}
									strTempDimGen = "\t\t" + oItem.strIntensionalID.replace("IPL_CODE_","soGEER") + ".getAbstractSyntaxTrees()[0].showTree();\n";
									if (!strTempDimen.contains(strTempDimGen)) 
									{
										strTempDimen = strTempDimen.concat(strTempDimGen);
										strTempDimen = strTempDimen.concat("\t\tSystem.out.println(\"-----------------------\");\n");
									}
									strTempDimGen = "\t\tSystem.out.println(" + oItem.strIntensionalID.replace("IPL_CODE_","soGEER") + ".getAbstractSyntaxTrees()[0].getRoot().jjtGetChild(0).jjtGetChild(2));";
									if (!strTempDimen.contains(strTempDimGen)) 
									{
										strTempDimen = strTempDimen.concat(strTempDimGen);
										strTempDimen = strTempDimen.concat("\t\tSystem.out.println(\"-----------------------\");\n");
									}
									strTempDimGen = "\t\t" + oItem.strIntensionalID.replace("IPL_CODE_","soGEER") + 
									".getAbstractSyntaxTrees()[0].getRoot().jjtGetChild(0).jjtGetChild(2).jjtAddChild(" +
									oJavaI.strClassInit.replace("IPL_CODE_","soGEER") + ".getAbstractSyntaxTrees()[0].getRoot().jjtGetChild(0), 3);\n";
									strTempDimen = strTempDimen.concat(strTempDimGen);
									strTempDimen = strTempDimen.concat("\t\tSystem.out.println(\"-----------------------\");\n");
									strTempDimGen = "\t\t" + oItem.strIntensionalID.replace("IPL_CODE_","soGEER") + ".getAbstractSyntaxTrees()[0].showTree();\n";
									strTempDimen = strTempDimen.concat(strTempDimGen);
									strTempDimen = strTempDimen.concat("\t\tSystem.out.println(\"-----------------------\");\n\n");
									iJavaMC = oIntermidateFileParseOutput.size();											
								}*/
							}								
						}
					}
				}
			}
			else linkIPL(oTempNode);
		}
	}
		
	public static void main(String[] args)
	throws IOException, DFGException, GIPCException, ParseException
	{
		PrintStream oIntermediateFile;
		PrintStream oFinalJavaFile;
		PrintStream oIPLFile;
				
		InputStream oIPLCode;
		//GIPLCompiler oGIPL;		
		IntensionalCompiler oGIPL;		
		AbstractSyntaxTree oAstTree;
		SemanticAnalyzer oSemantic;
		
		
		boolean bIsIntensional = false;
		
		String strRecordClassName="";
		String strTempIplID = "";
		Integer iIntensionalCount = 0;		
		String strTempICompiler = "";
		StringBuffer oTempIntensionalCode = new StringBuffer();
		JOOIPToJavaTranslationItem oSymbol;	
		
		String strFileExtension = ".jooip";
		String strIntermediateFileName = (args[0].substring(0, args[0].indexOf('.'))).concat(".jop");
		//String strIntermediateFileName = (args[0].substring(0, args[0].indexOf('.'))).concat(strFileExtension);
		String strFinalFileName = (args[0].substring(0, args[0].indexOf('.'))).concat(".java");
		String strIPLFileName = "IPL_CODE_";
		
		File oSourceFile = new File(args[0]);
		oIntermediateFile = new PrintStream(new FileOutputStream(strIntermediateFileName));
		oFinalJavaFile = new PrintStream(new FileOutputStream(strFinalFileName));
		
		if(args[0].indexOf('.') == -1)
		{
			System.err.println("Invalid input.");
			System.exit(1);
		}

		String strSuffix = args[0].substring(args[0].lastIndexOf('.') + 1);

		if(strSuffix.equals(strFileExtension))
		{
			System.out.println
			(
				"The Source File must have the "
				+ strFileExtension + " extension."
			);

			System.exit(1);
		}
		
		if(!oSourceFile.exists())
		{
			System.out.println("File not found.");
			System.exit(1);
		}
		
		BufferedReader oSourceLine = new BufferedReader(new FileReader(oSourceFile));
		
						
		//this step is to extract ipl code part in the .jooip file.				
		String strTempLine = oSourceLine.readLine();
		while(!(strTempLine == null))
		{						
			if(strTempLine.indexOf("/@")==-1)
			{
				if (!bIsIntensional)	
				{
					oIntermediateFile.println(strTempLine);										
					strTempLine = oSourceLine.readLine();					
				}
				else if (strTempLine.indexOf("@/")==-1)
				{
					oTempIntensionalCode.append(strTempLine);
					strTempLine = oSourceLine.readLine();					
				}
				else
				{
					oTempIntensionalCode.append(strTempLine.substring(0, strTempLine.indexOf("@/")));
					bIsIntensional = false;
					strTempIplID = strIPLFileName.concat(iIntensionalCount.toString());					
					strIPLFileName = strIPLFileName.concat(iIntensionalCount.toString()).concat(".ipl");
					oIPLFile = new PrintStream(new FileOutputStream(strIPLFileName));
					oIPLFile.print(oTempIntensionalCode);
					oIPLCode = new BufferedInputStream(new FileInputStream(strIPLFileName));
					oGIPL = new GIPLCompiler(oIPLCode);
					//oGIPL = new IndexicalLucidCompiler(oIPLCode);
			        oGIPL.init();        
			        GIPC.siPrimaryParserType=GIPC.GIPL_PARSER;
			        //GIPC.siPrimaryParserType=GIPC.INDEXICAL_LUCID_PARSER;
			        oAstTree = oGIPL.parse();			        
					oSymbol = new JOOIPToJavaTranslationItem(strTempIplID,"",false,strTempICompiler,oTempIntensionalCode,"",(SimpleNode)oAstTree.getRoot(),null);
					oIntensionalIdentifierTable.put(oSymbol.strIntensionalID, oSymbol);
					strIPLFileName = "IPL_CODE_";
					oTempIntensionalCode = new StringBuffer();
					strTempLine = strTempLine.substring(strTempLine.indexOf("@/")+2);
				}				
			}
			else
			{
				if (!bIsIntensional)
				{
					iIntensionalCount = iIntensionalCount + 1;
					bIsIntensional = true;
					String strTempReplace = strTempLine.substring(0, strTempLine.indexOf("/@"))+"IPL_CODE_"+iIntensionalCount.toString();					
					oIntermediateFile.print(strTempReplace);
					
					if (strTempLine.charAt(strTempLine.indexOf("/@")+2)=='#')
					{
						strTempLine = strTempLine.substring(strTempLine.indexOf("/@")+3);
						strTempICompiler = (strTempLine.substring(0, strTempLine.indexOf(' '))).trim().toUpperCase();
						strTempLine = strTempLine.substring(strTempLine.indexOf(' ')+1);
					}
					else
					{
						strTempICompiler = "GIPL";
						strTempLine = strTempLine.substring(strTempLine.indexOf("/@")+2);
					}					
				}
				else
				{
					if ((strTempLine.indexOf("@/")==-1)||(strTempLine.indexOf("@/") > strTempLine.indexOf("/@")))						
					{
						System.out.print("Error: The Intensional Part does not finished correctly!");
						System.exit(1);
					}
					else if (strTempLine.indexOf("@/") < strTempLine.indexOf("/@")) 
					{
						oTempIntensionalCode.append(strTempLine.substring(0, strTempLine.indexOf("@/")));
						bIsIntensional = false;
						strTempIplID = strIPLFileName.concat(iIntensionalCount.toString());						
						strIPLFileName = strIPLFileName.concat(iIntensionalCount.toString()).concat(".ipl");
						oIPLFile = new PrintStream(new FileOutputStream(strIPLFileName));
						oIPLFile.print(oTempIntensionalCode);
						oIPLCode = new BufferedInputStream(new FileInputStream(strIPLFileName));
						oGIPL = new GIPLCompiler(oIPLCode);		
						//oGIPL = new IndexicalLucidCompiler(oIPLCode);
				        oGIPL.init();        
				        GIPC.siPrimaryParserType=GIPC.GIPL_PARSER;
				        //GIPC.siPrimaryParserType=GIPC.INDEXICAL_LUCID_PARSER;
				        oAstTree = oGIPL.parse();				        
						oSymbol = new JOOIPToJavaTranslationItem(strTempIplID,"",false,strTempICompiler,oTempIntensionalCode,"",(SimpleNode)oAstTree.getRoot(),null);						
						oIntensionalIdentifierTable.put(oSymbol.strIntensionalID, oSymbol);
						strIPLFileName = "IPL_CODE_";
						oTempIntensionalCode = new StringBuffer();
						strTempLine = strTempLine.substring(strTempLine.indexOf("@/")+2);
					}
				}
			}
		}
		
		System.out.println
		(
			"There are " + oIntensionalIdentifierTable.size()
			+ " places of intensional part in the source file."
		);
		
		//use Java parser parse the intermediate .jop file, output is a SimpleNode		
		oIntermidateFileParseOutput = JavaParser.parse(new FileInputStream(strIntermediateFileName));
				
		JavaIdentifierSymbolTable oJavaIdentifier;
		JavaClassSymbolTable oJavaMember;
		DictionaryItem oDicItem;		
		
		Integer iJavaIdentifierNo;
		String strTempDeclare ="";
		String strTempIplDeclare = "";
		String strTempIPLDecl ="";
		String strTempWorkClass ="";
		String strTempClassInfo = "";
		String strTempConstructor = "";
		
		String strTempD = "";
		strIPLFileName = "IPL_CODE_";
		
		
		// XXX: fix constant hardcoding
		SimpleNode oWhere=new SimpleNode(16);
		SimpleNode oAssign=new SimpleNode(23);
		SimpleNode oTemp1=new SimpleNode(24);
		SimpleNode oTemp2=new SimpleNode(24);
		SimpleNode oStart = new SimpleNode(1);
		SimpleNode oTemp3;
		
		for (Integer iJavaMemberCount=0;iJavaMemberCount<oIntermidateFileParseOutput.size();iJavaMemberCount++)
		{
			oJavaMember = (JavaClassSymbolTable)oIntermidateFileParseOutput.elementAt(iJavaMemberCount);
			
			strRecordClassName = strRecordClassName.concat(oJavaMember.strClassName+";");
			
			//generate pure Java program head
			strTempClassInfo = "public class "+ oJavaMember.strClassName+" ";
			strTempConstructor = "public "+ oJavaMember.strClassName + "(GIPSYContext poContext)\n"+"\t{\n"
								+"\t\t"+"this.oContext = poContext;\n"+"\t}\n\n\t"+
								"public "+ oJavaMember.strClassName + "()\n" + "\t{\n" + "\t}\n";
			strTempWorkClass = "\t"+"public WorkResult work()\n" + "\t"+"{\n" +"\t\t"+ 
				oJavaMember.strClassName.concat(".main(null);\n")+ "\t\t"+"return null;\n"+"\t"+"}";
						
			if (!(oJavaMember.strExtendName.equals(""))) 
				strTempClassInfo = strTempClassInfo.concat("extends " + oJavaMember.strExtendName.substring(1, oJavaMember.strExtendName.length()-1));
			if (oJavaMember.strInterfaceName.equals("")) 
				strTempClassInfo = strTempClassInfo.concat(" implements ISequentialThread");
			else strTempClassInfo = strTempClassInfo.concat(" implements ISequentialThread,"+ oJavaMember.strInterfaceName.substring(1, oJavaMember.strInterfaceName.length()-1));
			
			
			//write head for final java program
			//oFinalJavaFile.println("// package gipsy.run.JOOIP."+oJavaMember.strClassName+";");
			
			oFinalJavaFile.println("package gipsy.tests.jooip;");
			oFinalJavaFile.println("import gipsy.GIPC.GIPC;");
			oFinalJavaFile.println("import gipsy.lang.*;");
			oFinalJavaFile.println("import gipsy.lang.converters.type.*;");
			oFinalJavaFile.println("import gipsy.interfaces.*;");
			oFinalJavaFile.println("import gipsy.lang.context.Dimension;");
			oFinalJavaFile.println("import gipsy.GEE.GEE;");
			oFinalJavaFile.println("import gipsy.util.*;");
			oFinalJavaFile.println("import java.lang.reflect.Method;\n");
			
			oFinalJavaFile.println(strTempClassInfo);
			oFinalJavaFile.println("{");
			
						
			//System.out.println("test class name :"+oJavaMember.strClassName);
			//System.out.println("test class name :"+oJavaMember.strExtendName);
			//System.out.println("test class name :"+oJavaMember.strInterfaceName);
			//System.out.println("test class name :"+oJavaMember.oMemberTable.size());
			
			for(iJavaIdentifierNo = 0; iJavaIdentifierNo<oJavaMember.oMemberTable.size(); iJavaIdentifierNo++ )
			{	
				oJavaIdentifier = (JavaIdentifierSymbolTable)oJavaMember.oMemberTable.get(oJavaMember.oMemberTable.keySet().toArray()[iJavaIdentifierNo]);
				//should add the code to judge if the class type is already defined in the cJava vector. even with its 
				//extends and its implements.				
				
				if ((!(oJavaIdentifier.strClassInit == null)) && (oJavaIdentifier.strClassInit.toString().contains("IPL_CODE")))
				{
					strTempDeclare = strTempDeclare.concat("\t"+"private static GIPSYProgram "+ oJavaIdentifier.strClassInit.replaceFirst("IPL_CODE_", "soGEER")+";\n");
					
					oItem = oIntensionalIdentifierTable.get(oJavaIdentifier.strClassInit.toString());				
					
					strTempIplDeclare = strTempIplDeclare.concat("GIPC " + oJavaIdentifier.strClassInit.replaceFirst("IPL_CODE_", "oGIPC")+
									    " = new GIPC(new StringInputStream(\"" + oJavaIdentifier.strID + " where " + oJavaIdentifier.strID + " = " 
									    + oItem.oIntensionalCode +"; end" + "\"), new String[] {\"--gipl\", \"--debug\"});\n\t\t\t"+ 
									    oJavaIdentifier.strClassInit.replaceFirst("IPL_CODE_", "oGIPC") + ".compile();\n\t\t\t"+ 
										oJavaIdentifier.strClassInit.replaceFirst("IPL_CODE_", "soGEER")+ " = " +
										oJavaIdentifier.strClassInit.replaceFirst("IPL_CODE_", "oGIPC")+".getGEER();\n\t");
					
						
					//the following code is to fix the identifier declaration to regular "where" node start tree;					
					oTemp1.setImage(oJavaIdentifier.strID);
					oTemp2.setImage(oJavaIdentifier.strID);
					oTemp3=(SimpleNode)oItem.oEntry.children[0];
					oAssign.jjtAddChild(oTemp1, 0);
					oAssign.jjtAddChild(oTemp3, 1);
					oTemp1.jjtSetParent(oAssign);
					oTemp3.jjtSetParent(oAssign);
					oWhere.jjtAddChild(oTemp2, 0);
					oWhere.jjtAddChild(oAssign, 1);
					oTemp2.jjtSetParent(oWhere);
					oAssign.jjtSetParent(oWhere);
					oStart.jjtAddChild(oWhere,0);
					oWhere.jjtSetParent(oStart);
					oItem.oEntry=oStart;
					oJavaIdentifier.oEntry = oStart;
					//semantics check	
					oSemantic = new SemanticAnalyzer();
					oSemantic.setupDictionary(oStart);					
					oIPLDictionary=oSemantic.getDictionary();
					
					oItem.bIsJavaMember = true;						
					oItem.strReplacementCode = "0;"+"\n"+"\t"+"private boolean b"+oJavaIdentifier.strID+"IsWritten = false";
					oItem.oSemanticDictionary =oIPLDictionary;
					oJavaIdentifier.oLucidIdentifierDictionary = oIPLDictionary;
					
					oIntensionalIdentifierTable.put(oJavaIdentifier.strClassInit.toString(), oItem);
					oJavaMember.oMemberTable.put(oJavaIdentifier.strID, oJavaIdentifier);
					oJavaMember.oMemberTable.remove(oJavaIdentifier.strClassInit);					
					
					//System.out.println("test IPL "+ IPLDictionary.size());
					//JavaItem = (JavaIdentifierSymbolTable)JavaMember.MemberTable.get(JavaMember.MemberTable.keySet().toArray()[JavaE]);
					//JavaItem.Entry.dump(" ");
				}else if (oJavaIdentifier.strID.contains("IPL_CODE"))
				{		
					oItem = oIntensionalIdentifierTable.get(oJavaIdentifier.strID);
					oItem.strJavaClassName = oJavaMember.strClassName;
					oItem.bIsJavaMember = false;
					strTempIPLDecl = strTempIPLDecl.concat("\tpublic GEE "+oJavaIdentifier.strID.replaceFirst("IPL_CODE_", "oGEE") 
							       + " = new GEE("+ oJavaIdentifier.strID.replaceFirst("IPL_CODE_", "soGEER")+");\n");
					oItem.strReplacementCode = oJavaIdentifier.strID.replaceFirst("IPL_CODE_", "this.oGEE")+
					".eval("+oJavaIdentifier.strID.replaceFirst("IPL_CODE_", "oContext")+")";
					
					oIntensionalIdentifierTable.put(oJavaIdentifier.strID, oItem);					
					//oJavaMember.oMemberTable.remove(oJavaIdentifier.strID);					
				}
			}
		}
		
		boolean bIsDefinedInJava = false;
		String sTempName = "";
		SimpleNode oTempNode;
		DictionaryItem oDicTemp;
		JavaClassSymbolTable oJavaTempM;		
		
		for(Integer iIntensionalIdentifierCount = 1; iIntensionalIdentifierCount<=oIntensionalIdentifierTable.size(); iIntensionalIdentifierCount++ )
		{
			strTempIplID = strIPLFileName.concat(iIntensionalIdentifierCount.toString());
			oItem = oIntensionalIdentifierTable.get(strTempIplID);
						
			if (oItem.oSemanticDictionary == null)
			{
				strTempDeclare = strTempDeclare.concat("\t"+"private static GIPSYProgram "+ oItem.strIntensionalID.replaceFirst("IPL_CODE_", "soGEER")+";\n");
				strTempIplDeclare = strTempIplDeclare.concat("\t\tGIPC "+oItem.strIntensionalID.replaceFirst("IPL_CODE_", "oGIPC")+
					    " = new GIPC(new StringInputStream(\"" + oItem.oIntensionalCode+"\"), new String[] {\"--gipl\", \"--debug\"});\n\t\t\t"+ 
					    oItem.strIntensionalID.replaceFirst("IPL_CODE_", "oGIPC") + ".compile();\n\t\t\t"+ 
					    oItem.strIntensionalID.replaceFirst("IPL_CODE_", "soGEER")+ " = " +
					    oItem.strIntensionalID.replaceFirst("IPL_CODE_", "oGIPC")+".getGEER();\n\t");
				oSemantic = new SemanticAnalyzer();
				oSemantic.setupDictionary(oItem.oEntry);					
				oIPLDictionary=oSemantic.getDictionary();
			
				oItem.oSemanticDictionary =oIPLDictionary;
						
				oIntensionalIdentifierTable.put(oItem.strIntensionalID, oItem);
				
				for (Integer iDic=0;iDic<oIPLDictionary.size();iDic++)
				{
					oDicTemp = (DictionaryItem)oIPLDictionary.elementAt(iDic);
					if (oDicTemp.getKind().equals("dimension"))
					{
						strTempD = "\t\tGIPSYContext "+oItem.strIntensionalID.replaceFirst("IPL_CODE_", "oContext")+" = new GIPSYContext();\n" +
						"\t\tDimension "+oItem.strIntensionalID.replaceFirst("IPL_CODE_", "oDimension")+" = new Dimension();\n"+
						"\t\t"+oItem.strIntensionalID.replaceFirst("IPL_CODE_", "oDimension")+".setDimensionName(new GIPSYIdentifier(\"" + 
						oDicTemp.getName()+"\"));\n";
						if (!(strTempDimen.contains(strTempD)))
							strTempDimen = strTempDimen.concat(strTempD);						
					}					
					
					if (oDicTemp.getName().contains("_JAVA_Defined"))
					{
						for (Integer iJavaMC=0;iJavaMC<oIntermidateFileParseOutput.size();iJavaMC++)
						{
							oJavaTempM = (JavaClassSymbolTable)oIntermidateFileParseOutput.elementAt(iJavaMC);
							if (oItem.strJavaClassName.equals(oJavaTempM.strClassName)){
								for(iJavaIdentifierNo = 0; iJavaIdentifierNo<oJavaTempM.oMemberTable.size(); iJavaIdentifierNo++ )
								{	
									oJavaIdentifier = (JavaIdentifierSymbolTable)oJavaTempM.oMemberTable.get(oJavaTempM.oMemberTable.keySet().toArray()[iJavaIdentifierNo]);						
									if ((oDicTemp.getName().replaceAll("_JAVA_Defined", "")).equals(oJavaIdentifier.strID)){
										bIsDefinedInJava = true;
										iJavaMC = oIntermidateFileParseOutput.size();
										sTempName = (oDicTemp.getName().replaceAll("_JAVA_Defined", ""));
										oDicTemp.setName(sTempName);
										oIPLDictionary.set(oDicTemp.getID(), oDicTemp);
									}
								}								
							}
						}	
						if (!(bIsDefinedInJava)) System.out.println("Semantic Error: " + oDicTemp.getName().replaceAll("_JAVA_Defined", "")+" is not defined!");
					}					
					if ((oDicTemp.getKind().contains("_FLAG"))&&(bIsDefinedInJava)){
						
						strTempD = "\t\t"+oItem.strIntensionalID.replaceFirst("IPL_CODE_", "oDimension")+".setCurrentTag(new GIPSYInteger(" + 
						oDicTemp.getKind().replaceAll("identifier_FLAG", "")+"));\n" +
						//oDicTemp.getName().substring(0, oDicTemp.getName().indexOf("_JAVA_Defined"))+"));\n" +
						"\t\t"+oItem.strIntensionalID.replaceFirst("IPL_CODE_", "oContext")+
						".addDimension("+oItem.strIntensionalID.replaceFirst("IPL_CODE_", "oDimension")+");\n\n";						
					
						if (!(strTempDimen.contains(strTempD)))
							strTempDimen = strTempDimen.concat(strTempD);
					}
				}
				
				linkIPL(oItem.oEntry);
				
			}	
			
			//System.out.println("----------- translation tree -----------");
			//eItem.TranEntry.dump(" ");
		}
		
		String strTempBody = "";
		
		File oIntermidateFile = new File(strIntermediateFileName);
		BufferedReader oIntermidtaeFileLine = new BufferedReader(new FileReader(oIntermidateFile));
		String strTempIntermidateLine = oIntermidtaeFileLine.readLine();
		String strBackupReplacement="";		
		
		Integer iIPLCount = 1;
		String strTempIntensionalID="";
		//boolean bIsInMain = false;
				
		while (!(strTempIntermidateLine == null))
		{						
			if (strTempIntermidateLine.indexOf("class ")==-1)
			{
			//	if (strTempIntermidateLine.contains("main")||strTempIntermidateLine.contains("argv")) bIsInMain = true;
					
				if (strTempIntermidateLine.indexOf("IPL_CODE")==-1)
				{
					//oFinalJavaFile.println(strTempIntermidateLine);
					strTempBody = strTempBody.concat(strTempIntermidateLine+"\n");
					strTempIntermidateLine = oIntermidtaeFileLine.readLine();
				}
				else 
				//if (!bIsInMain)
				{
					strTempIntensionalID = "IPL_CODE_" + iIPLCount.toString();					
					oItem = oIntensionalIdentifierTable.get(strTempIntensionalID);
					if (oItem.bIsJavaMember)
					{
						strTempIntermidateLine = strTempIntermidateLine.replace(strTempIntensionalID, oItem.strReplacementCode);
						strTempDeclare = strTempDeclare.concat(strTempIntermidateLine);
						strTempIntermidateLine = oIntermidtaeFileLine.readLine();
						
					}else
					{		
						if (!strTempBody.contains(strTempDimen)) strTempBody = strTempBody.concat(strTempDimen);
						strBackupReplacement = "IPLToJava.convertToInteger(" + oItem.strReplacementCode + ")";					
						strTempIntermidateLine = strTempIntermidateLine.replace(strTempIntensionalID, strBackupReplacement);
					}
					iIPLCount=iIPLCount+1;
				}
				/*else if (bIsInMain)
				{
					strTempIntensionalID = "IPL_CODE_" + iIPLCount.toString();					
					oItem = (JOOIPJOOIPToJavaTranslationItem)oIntensionalIdentifierTable.get(strTempIntensionalID);	
					strBackupReplacement = "GEE oGEE = new GEE(soGEER" + iIPLCount.toString()+");\n"
											+ "\t\t"+"GIPSYType oValue = oGEE.eval(null);\n"
											+ "\t\t"+"if (oValue instanceof GIPSYObject)\n"
											+ "\t\t"+"{\n"
											+ "\t\t\t"+"((GIPLtest)((GIPSYObject)oValue).getValue())";
					strTempIntermidateLine = strTempIntermidateLine.replace(strTempIntensionalID, strBackupReplacement);
					strTempIntermidateLine = strTempIntermidateLine.concat("\n\t\t}\n" + "\t\t"+"else{\n"
											+"\t\t\t"+"System.out.println(\"Expression resulted in a wrong value type:\"+oValue);"+"\n\t\t}");
					iIPLCount=iIPLCount+1;
				}*/
			}else
			{	
				while (strTempIntermidateLine.indexOf("{")==-1) strTempIntermidateLine = oIntermidtaeFileLine.readLine();
				strTempIntermidateLine = strTempIntermidateLine.substring(strTempIntermidateLine.indexOf("{")+1);		
			}		
			
		}
		
		oFinalJavaFile.println(strTempDeclare);		
		oFinalJavaFile.println("\t"+"private GIPSYContext oContext;\n");
		oFinalJavaFile.println(strTempIPLDecl);
		oFinalJavaFile.println("\tstatic{\n\t\ttry\n\t\t{\n\t\t\t"+strTempIplDeclare+
				"\t}\n\t\tcatch(Exception e)\n\t\t{\n\t\t\tSystem.err.println(e);\n\t\t\te.printStackTrace(System.err);\n\t\t}\n\t}\n");
		oFinalJavaFile.println("\t"+strTempConstructor);
		oFinalJavaFile.println(strTempWorkClass);	
		oFinalJavaFile.println("\t@Override\n"+
							   "\tpublic WorkResult getWorkResult()\n"+
							   "\t{\n"+
							   "\t\treturn null;\n"+
							   "\t}\n\n"+

							   "\t@Override\n"+
							   "\tpublic void setMethod(Method poSTMethod)\n"+
							   "\t{\n"+
								"\t}\n\n"+

							   "\t@Override\n"+
							   "\tpublic void run()\n"+
							   "\t{\n"+
							   "\t\twork();\n"+
								"\t}");
		oFinalJavaFile.println(strTempBody);
		
		/*for(Integer e = 1; e<=IntensionalTable.size(); e++ )
		{
			tempIPLid = IPLfilename.concat(e.toString());
			eItem = (JOOIPJOOIPToJavaTranslationItem)IntensionalTable.get(tempIPLid);		
			
			System.out.println(eItem.IntensionalID+"has "+eItem.SDictionary.size()+"IPL variable declaration");			
		}*/
     }
	
	
	
	/*public boolean IsClassName(String CName)throws ParseException{
		  int t4 = 0;
		  boolean isclass = false;
		  
		  JavaClassSymbolTable temp4;

			for(t4 = 0; t4 < cJava.size(); t4++)
			{
				temp4 = (JavaClassSymbolTable)cJava.elementAt(t4);

				if(temp4.ClassName.equals(CName))
				{
					isclass=true;
				}
			}
			return isclass;	  
	  }*/
}
