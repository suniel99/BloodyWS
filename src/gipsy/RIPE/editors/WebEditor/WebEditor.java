package gipsy.RIPE.editors.WebEditor;

import gipsy.GIPC.GIPC;
import gipsy.GIPC.GIPCException;
import gipsy.GIPC.intensional.EIntensionalLanguages;
import gipsy.RIPE.RIPE;
import gipsy.tests.Regression;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import marf.util.BaseThread;
import marf.util.Debug;
import marf.util.ExpandedThreadGroup;


/**
 * <p>Servlet-based Web Editor for GIPSY.</p>
 * 
 * $Id: WebEditor.java,v 1.26 2009/08/25 18:45:31 mokhov Exp $
 * 
 * @author Serguei Mokhov
 * @version $Revision: 1.26 $
 * @since 1.0.0
 */
public class WebEditor
extends HttpServlet
{
	public static final String PARSE_LABEL = "Parse";
	public static final String RUN_LABEL = "Run";
	public static final String RUN_REGRESSION_TESTS = "Regression Tests";
	public static final String RUN_JUNIT_TESTS = "JUnit Tests";
	public static final String STOP_LABEL = "Stop";
	public static final String PRINT_LABEL = "Print";
	public static final String LOAD_LABEL = "Load";
	
	/**
	 * Local reference of RIPE to carry out
	 * web-based requests tranlated into GIPSY.
	 */
	protected transient RIPE oRIPE = null;

	protected transient HttpServletRequest oHTTPRequest;
	protected transient HttpServletResponse oHTTPResponse;
	
	protected transient GIPC oGIPC = null;
	
	protected transient Regression oRegression = null;
	
	/**
	 * 
	 */
	public WebEditor()
	{
	}

	/**
	 * WebEditor implementation of the Servlet API to process GET
	 * requests.
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest poHTTPRequest, HttpServletResponse poHTTPResponse)
	throws IOException, ServletException
	{
		this.oHTTPRequest = poHTTPRequest;
		this.oHTTPResponse = poHTTPResponse;
		
		this.oHTTPResponse.setContentType("text/html");
		ServletOutputStream oSOS = this.oHTTPResponse.getOutputStream();

		// Backup the streams
		PrintStream oStdOutBackup = System.out;
		PrintStream oStdErrBackup = System.err;
		
		// Redirect STDOUT and STDERR to the ServletOuputStream
		System.setOut(new PrintStream(oSOS));
		System.setErr(new PrintStream(oSOS));
	
		try
		{
			displayUserInterface(oSOS);
		}
		catch(Exception e)
		{
			oSOS.println("<pre>");
			oSOS.print(e.toString());
			oSOS.println("</pre>");
		}
		
		// Restore original STDOUT and STDERR
		System.setOut(oStdOutBackup);
		System.setErr(oStdErrBackup);
	}
	
	/**
	 * We are going to perform the same operations for POST requests
	 * as for GET methods, so this method just sends the request to
	 * the doGet method.
	 * 
	 * @param poHTTPRequest current HTTP request
	 * @param poHTTPResponse HTTP response to write back to
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest poHTTPRequest, HttpServletResponse poHTTPResponse)
	throws IOException, ServletException
	{
	    doGet(poHTTPRequest, poHTTPResponse);
	}
	
	protected void displayUserInterface(ServletOutputStream poSOS)
	throws IOException, GIPCException
	{
		try
		{
			poSOS.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			poSOS.println("<!DOCTYPE html");
			poSOS.println("     PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"");
			poSOS.println("     \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
			poSOS.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">");
			poSOS.println("<head>");
//			poSOS.println("<base url='http://newton.cs.concordia.ca:8080/WebEditor/' />");
			poSOS.println("<title>GIPSY WebEditor Portal $Revision: 1.26 $</title>");
			poSOS.println("<link href=\"/WebEditor/styles.css\" rel=\"stylesheet\" type=\"text/css\" />");
			poSOS.println("</head>");
			poSOS.println("<body>");

			poSOS.println("<table border='0' cellspacing='10' width='100%'><tr>");
			//poSOS.println("<td><img src='/WebEditor/images/gipsystar.png' height='150' alt='GIPSY Logo' border='0' /></td>");
			poSOS.println("<td width='50%'><h1>GIPSY WebEditor Portal</h1><i>Prototype $Revision: 1.26 $</i>");
			poSOS.println("<br /><i>$Id: WebEditor.java,v 1.26 2009/08/25 18:45:31 mokhov Exp $</i></td>");
			poSOS.println("<td><h3>Computer Science and<br />Software Engineering</h3></td>");
			poSOS.println("<td><img src='/WebEditor/images/logoconc.gif' alt='Concordia University' border='0' /></td>");
			poSOS.println("</tr></table>");
			
			poSOS.println("<hr />");
			poSOS.println("<form name='frmEditor' method='POST'>");
	
			poSOS.println("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"5\">");
			poSOS.println("<tr>");

			String strAction = this.oHTTPRequest.getParameter("action");
			String strDialect = this.oHTTPRequest.getParameter("dialect");
			String strDebug = this.oHTTPRequest.getParameter("debug");
			
			String strDebugOption = "";
			
			if(strAction == null)
			{
				strAction = "";
			}

			if(strDebug != null)
			{
				strDebugOption = "--debug";
			}
			
			if(strDialect == null)
			{
				strDialect = "GIPSY";
			}

			poSOS.println("<td valign=\"top\" width='270'>");
			poSOS.println("Intensional Dialect:<br />");
			poSOS.println("<select name='dialect' id='dialect'>");
			poSOS.println("<option " + (strDialect.equals("GIPSY") ? "selected='selected'" : "") + ">GIPSY</option>");
	
			for(int i = 0; i < EIntensionalLanguages.INTENSIONAL_LANGUAGES.length; i++)
			{
				poSOS.println
				(
					"<option value='" + EIntensionalLanguages.INTENSIONAL_LANGUAGES[i] + "' "
					+ (strDialect.equals(EIntensionalLanguages.INTENSIONAL_LANGUAGES[i]) ? "selected='selected'" : "")
					+ ">" + EIntensionalLanguages.INTENSIONAL_LANGUAGES[i]
					+ "</option>"
				);
			}
			
			poSOS.println("<option value='GLU#'>GLU#</option>");
			poSOS.println("</select>");
	
			poSOS.println("<hr />");
	
			poSOS.println("Test Programs:<br />");
			poSOS.println("<select name='sample' id='sample'>");
			
			try
			{
				poSOS.println(getFileListDropdownOptions("tests/gipsy"));
				poSOS.println(getFileListDropdownOptions("tests/lucid"));
				//poSOS.println(getFileListDropdownOptions("tests/gipl"));
				//poSOS.println(getFileListDropdownOptions("tests/indexical"));
				//poSOS.println(getFileListDropdownOptions("tests/jlucid"));
				//poSOS.println(getFileListDropdownOptions("tests/objective"));
			}
			catch(Exception e)
			{
				poSOS.println("<option>error :-(</option>");
			}

			poSOS.println("</select>");

			poSOS.println("<input type=\"submit\" name='action' value=\"" + LOAD_LABEL + "\" /><br />");
	
			poSOS.println("<hr />");
	
			poSOS.println("Controls:<br />");
			poSOS.println("<input type=\"submit\" name='action' value=\"" + PARSE_LABEL + "\" /><br />");
			poSOS.println("<input type=\"submit\" name='action' value=\"" + RUN_LABEL + "\" /><br />");
			poSOS.println("<input type=\"submit\" name='action' value=\"" + RUN_REGRESSION_TESTS + "\" /><br />");
			poSOS.println("<input type=\"submit\" name='action' value=\"" + RUN_JUNIT_TESTS + "\" /><br />");
			poSOS.println("<input type=\"submit\" name='action' value=\"" + STOP_LABEL + "\" disabled='disabled' /><br />");
			poSOS.println("<input type=\"submit\" name='action' value=\"" + PRINT_LABEL + "\" disabled='disabled' /><br />");
			poSOS.println("<input type=\"checkbox\" name='debug' id='debug' " + (strDebugOption.equals("") ? "" : "checked='checked'") + "/> debug mode<br />");
	
			poSOS.println("<hr />");
	
			poSOS.println("<input type=\"button\" value=\"Start GEE Services\" disabled='disabled' /><br />");
			poSOS.println("<input type=\"checkbox\" disabled='disabled'> All<br />");
			poSOS.println("<input type=\"checkbox\" checked=\"checked\" disabled='disabled'> Threaded (local)<br />");
			poSOS.println("<input type=\"checkbox\" disabled='disabled'> RMI<br />");
			poSOS.println("<input type=\"checkbox\" disabled='disabled'> Jini<br />");
			poSOS.println("<input type=\"checkbox\" disabled='disabled'> DCOM+<br />");
			poSOS.println("<input type=\"checkbox\" disabled='disabled'> CORBA<br />");
			poSOS.println("<input type=\"checkbox\" disabled='disabled'> GIPSY Sockets<br />");
	
			poSOS.println("<hr />");
	
			poSOS.println("<input type=\"button\" value=\"Download Worker\" disabled='disabled' /><br />");
			poSOS.println("<input type=\"button\" value=\"Register Worker\" disabled='disabled' /><br />");
	
			poSOS.println("<hr />");

			poSOS.println("Method: " + this.oHTTPRequest.getMethod() + "<br />");
			poSOS.println("Request URI: " + this.oHTTPRequest.getRequestURI() + "<br />");
			poSOS.println("Protocol: " + this.oHTTPRequest.getProtocol() + "<br />");
			poSOS.println("PathInfo: " + this.oHTTPRequest.getPathInfo() + "<br />");
			poSOS.println("Remote Address: " + this.oHTTPRequest.getRemoteAddr() + "<br />");
			poSOS.println("Remote Host: " + this.oHTTPRequest.getRemoteHost() + "<br />");

/*
			poSOS.println("RIPE: Constructing<br />");
			System.out.println("Start: " + new Date()); 
			//this.oRIPE = new RIPE(new String[] {"--debug", "--gipc=--gee"});
			this.oRIPE = new RIPE(new String[] {"--debug", "--help", "--gipc"});
			poSOS.println("RIPE: Running<br /><pre>");

			//this.oRIPE.start();
			poSOS.println("RIPE: foobarbaz " + oRIPE);
			//this.oRIPE.run();
			poSOS.println("</pre>RIPE: Waiting<br />");
			//this.oRIPE.join();
			poSOS.println("RIPE: Done<br />");
			System.out.println("End: " + new Date());
			//this.oRIPE = null;*/
			
			try
			{
				/*
				 * Source section
				 */
	
				poSOS.println("<td valign=\"top\">");
				poSOS.println("Program Text:<br />");
				poSOS.println("<textarea name=\"source\" cols=\"120\" rows=\"25\">");
				
				
				String strSourceCode = null;
				
				if(strAction.equals(LOAD_LABEL))
				{
					String strProgramSample = this.oHTTPRequest.getParameter("sample");
					FileReader oReader = new FileReader(strProgramSample);
					
					strSourceCode = "";
					
					while(oReader.ready())
					{
						strSourceCode += (char)oReader.read();
					}
				}
				else
				{
					strSourceCode = this.oHTTPRequest.getParameter("source");
				}

				if(strSourceCode == null || strSourceCode.equals(""))
				{
/*					poSOS.println("#GIPL");
					poSOS.println("A + B");
					poSOS.println("where");
					poSOS.println("    A = 1;");
					poSOS.println("    B = 2;");
					poSOS.print("end");*/

					/*
					realign.d1,d2 (x) = x @.d1 #.d2;
					realign.t,z(fib)

runningAverage.d (x) = mean
where
  mean = x fby.d (mean + diff);
  diff = (next.d x - mean) / (next.d count);
  count = 1 fby.d (count + 1);
end
															
					*/
					poSOS.println("#INDEXICALLUCID");
					poSOS.println("fib @.t 5");
					poSOS.println("where");
					poSOS.println("    dimension t;");
					poSOS.println("    fib = 0 fby.t g;");
					poSOS.println("    g = 1 fby.t (fib + 1);");
					poSOS.print("end");
					
				}
				else
				{
					poSOS.print(strSourceCode);
				}
	
				poSOS.println("</textarea>");
		
				poSOS.println("<hr />");
	
				/*
				 * Output section
				 */
				
				poSOS.println("Output:");
				poSOS.println("<hr />");
		
				poSOS.println("<pre>");
			
				Debug.enableDebug();
			

				if(strAction.equals(LOAD_LABEL) == false && strAction.equals("") == false && strSourceCode != null && strSourceCode.equals("") == false)
				{
					poSOS.println("Interpreted dialect: " + strDialect);

					poSOS.println("Source:");
					poSOS.println("--------------------------8<----------------------------");
					poSOS.println(strSourceCode);
					poSOS.println("--------------------------8<----------------------------");

					String[] astrArgv = {};
					
					String strDialectOption = "";
					
					if(strDialect.equals("GIPSY"))
					{
						strDialectOption = "";
					}
					else if(strDialect.equals("GIPL"))
					{
						strDialectOption = "--gipl";
					}
					else if(strDialect.equals("INDEXICALLUCID"))
					{
						strDialectOption = "--indexical";
					}
					else if(strDialect.equals("JLUCID"))
					{
						strDialectOption = "--jlucid";
					}
					else if(strDialect.equals("OBJECTIVELUCID"))
					{
						strDialectOption = "--objective";
					}
					else
					{
						poSOS.println("<font color='red'>Unsupported/not implemented dialect " + strDialect + ". Ignoring...</font><br />");
						strDialectOption = "";
					}
					
					if(strAction.equals(PARSE_LABEL))
					{
						astrArgv = new String[] {"--parse-only", strDialectOption, strDebugOption};
					}
					else if(strAction.equals(RUN_LABEL))
					{
						astrArgv = new String[] {"--gee", strDialectOption, strDebugOption};
					}

					/*
					 * Compiler / Regression execution
					 */
					
					poSOS.println("--------------------------8<----------------------------");

					// Used to group the parent and its all possible children in the group
					// to solicit all the output prior termination.
					ExpandedThreadGroup oGroup = new ExpandedThreadGroup("WebEditor $Revision: 1.26 $");
					
					if(strAction.equals(RUN_REGRESSION_TESTS) || strAction.equals(RUN_JUNIT_TESTS))
					{
						if(strAction.equals(RUN_REGRESSION_TESTS))
						{
							this.oRegression = new Regression(new String[] {"--regression", strDebugOption});
						}
						else if(strAction.equals(RUN_JUNIT_TESTS))
						{
							this.oRegression = new Regression(new String[] {"--junit", strDebugOption});
						}
						
						poSOS.println("Regression Testing Output:");
						this.oRegression.run();
					}
					else
					{
						this.oGIPC = new GIPC(new ByteArrayInputStream(strSourceCode.getBytes()), astrArgv);
						
						// Just enough to specify belongings; need not start,
						// thus, ideally, GEE (and its child threads), if called, will inherit
						// this group.
						BaseThread oGIPCThread = new BaseThread(oGroup, this.oGIPC);
						
						oGroup.start();
						//poSOS.println("AST:\n" + this.oGIPC.compile());
						poSOS.println("AST:\n" + this.oGIPC.getAbstractSyntaxTree());
						
						// Wait for all to terminate to properly render the output
						// in the browser.
						oGroup.join();
					}
						
					poSOS.println("--------------------------8<----------------------------");
				}
			}
			catch(Exception e)
			{
				poSOS.println("--------------------------8<----------------------------");
				poSOS.println(e.getMessage());
				e.printStackTrace(System.err);
				poSOS.println("--------------------------8<----------------------------");
			}

			poSOS.println("Done.");
			poSOS.println("</pre>");
			poSOS.println("<hr />");
			poSOS.println("</td>");
			poSOS.println("</tr>");
			poSOS.println("</table>");
			poSOS.println("</form>");
	
			poSOS.println("<div align='center' style='font: bold 11px Arial; color: teal'>");
			poSOS.println("Author: <a href=\"mailto:mokhov@cs.concordia.ca\">Serguei Mokhov</a><br />");
			poSOS.println("<a href=\"COPYRIGHT\">Copyright &copy; 2005 The GIPSY Research and Development Group</a><br />");
			poSOS.println("Computer Science and Software Engineering Department<br />");
			poSOS.println("Concordia University, Montr&eacute;al, Qu&eacute;bec, Canada<br />");
			poSOS.println("</div>");
		}
		catch(Exception e)
		{
			poSOS.println(e.toString());
		}
		
		poSOS.println("<hr />");
		
		poSOS.println("</body>");
		poSOS.println("</html>");
	}
	
	protected String getFileListDropdownOptions(String pstrDirName)
	throws Exception
	{
		String strOptionsList = "";
		File[] aoFiles = new File(pstrDirName).listFiles();

		String strFilename = "";
		
		for(int i = 0; i < aoFiles.length; i++)
		{
			strFilename = aoFiles[i].getPath();

			if(strFilename.toLowerCase().endsWith(".ipl"))
			{
				strOptionsList += "<option>" + strFilename + "</option>";
			}
		}
		
		return strOptionsList;
	}
}

// EOF
