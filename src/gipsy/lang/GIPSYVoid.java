package gipsy.lang;

/**
 * @author Serguei Mokhov
 */
public class GIPSYVoid
extends GIPSYBoolean
{
	/**
	 * For serialization versioning.
	 */
	private static final long serialVersionUID = -6830982769657220934L;

	public GIPSYVoid()
	{
		super(true);
		this.strLexeme = "void";
		this.iType = TYPE_VOID;
	}
}

// EOF
