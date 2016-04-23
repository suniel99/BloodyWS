package gipsy.GIPC.intensional.SIPL.ObjectiveLucid;

import gipsy.GIPC.intensional.SimpleNode;
import gipsy.GIPC.util.ParseException;
import gipsy.interfaces.AbstractSyntaxTree;

import java.io.InputStream;
import java.io.IOException;


/**
 * <p>Main Objective Lucid Parser.</p>
 *
 * $Id: ObjectiveLucidParser.java,v 1.4 2005/06/25 19:34:14 mokhov Exp $
 *
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 * @version $Revision: 1.4 $
 * @since 1.0.0
 */
public class ObjectiveLucidParser
{
	/**
	 * Local objective GIPL parser.
	 */
	private ObjectiveGIPLParser oGIPLParser = null;

	/**
	 * Local objective Indexical Lucid parser.
	 */
	private ObjectiveIndexicalLucidParser oIndexicalLucidParser = null;

	protected InputStream oSourceStream;

	public ObjectiveLucidParser(InputStream poSourceStream)
	{
		this.oSourceStream = poSourceStream;
	}

	public AbstractSyntaxTree parse()
	throws ParseException
	{
		SimpleNode oRoot = null;

		try
		{
			this.oGIPLParser = new ObjectiveGIPLParser(this.oSourceStream);
			oRoot = this.oGIPLParser.startParse();
		}
		catch(ParseException oParseException)
		{
			try
			{
				this.oSourceStream.reset();
				this.oIndexicalLucidParser = new ObjectiveIndexicalLucidParser(this.oSourceStream);
				oRoot = this.oIndexicalLucidParser.startParse();
			}
			catch(IOException oIOException)
			{
				throw new ParseException(oIOException);
			}
		}

		return new AbstractSyntaxTree(oRoot);
	}
}

// EOF
