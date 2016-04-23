package gipsy.GEE.IDP.DemandGenerator.jms;

import java.io.*;
public class PrintResult 
{
	protected static final char CR = (char)13;
	protected static final char LF = (char)10;
	public static final String OUT_FILE_NAME = "Last.out";
	private static FileOutputStream fileOut;
	private static Writer writerFile;
	protected static long lnTimeStart;
	protected static long lnTimeEnd;
	private static final String MSG_PREFIX = "DGPiCalculation message: ";
	private static final String ERR_PREFIX = "DGPiCalculation error: ";
	
	public PrintResult() 
	{
		super();
	}

	protected static void printOutError(String sError)
	{
		System.out.println("Printout problem in DDCommunicator.run()");
		System.err.println(ERR_PREFIX + sError);
	}

	private static void openOutFile() 
	throws IOException
	{
		fileOut = new FileOutputStream(OUT_FILE_NAME);
		writerFile = new BufferedWriter(new OutputStreamWriter(fileOut, "ASCII"));
	}

	private static void closeOutFile() 
	throws IOException
	{
		writerFile.flush();
		writerFile.close();
		fileOut.close();
	}
	/**
	 * @param sMsg
	 * @see gipsy.GEE.multitier.DGT
	 * Change protect to public, should be visible by DGTWrapper.class. -Bin
	 */
	public static void printOut(String sMsg)
	{
		System.out.println(MSG_PREFIX + sMsg);
	}
	protected static void writeOutFile(String sResults)	
	throws IOException
	{
		openOutFile();
		writerFile.write("Start time: " + lnTimeStart);
		writerFile.write(" " + CR+LF);
		writerFile.write(" " + CR+LF);
		writerFile.write(sResults);
		writerFile.write(" " + CR+LF);
		writerFile.write("End time: " + lnTimeEnd);
		writerFile.write(" " + CR+LF);
		writerFile.write(" " + CR+LF);
		writerFile.write("Duration: " + (lnTimeEnd - lnTimeStart));
		closeOutFile();
	}


}
