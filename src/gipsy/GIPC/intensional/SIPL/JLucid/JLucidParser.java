package gipsy.GIPC.intensional.SIPL.JLucid;

import gipsy.GIPC.intensional.SimpleNode;
import gipsy.GIPC.util.ParseException;
import gipsy.interfaces.AbstractSyntaxTree;

import java.io.InputStream;
import java.io.IOException;


/**
 * <p>Main JLucid Parser.</p>
 *
 * $Id: JLucidParser.java,v 1.10 2005/09/07 15:22:54 mokhov Exp $
 *
 * @author Serguei Mokhov, mokhov@cs.concordia.ca
 * @version $Revision: 1.10 $
 * @since 1.0.0
 */
public class JLucidParser
{
	protected JGIPLParser oGIPLParser;
	protected JIndexicalLucidParser oIndexicalParser;
	protected InputStream oSourceStream;
	
	public JLucidParser(InputStream poSourceStream)
	{
		this.oSourceStream = poSourceStream;
	}

	public AbstractSyntaxTree parse()
	throws ParseException
	{
		SimpleNode oRoot = null;

		try
		{
			this.oGIPLParser = new JGIPLParser(this.oSourceStream);
			oRoot = this.oGIPLParser.startParse();
		}
		//catch(ParseException oParseException)
		catch(Throwable e)
		{
			try
			{
				this.oSourceStream.reset();
				this.oIndexicalParser = new JIndexicalLucidParser(this.oSourceStream);
				oRoot = this.oIndexicalParser.startParse();
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
