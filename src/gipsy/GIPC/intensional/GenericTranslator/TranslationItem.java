package gipsy.GIPC.intensional.GenericTranslator;

import gipsy.GIPC.intensional.SimpleNode;

/**
 * Defines items in the tran-table
 *
 * @author  aihua_wu
 * @version 1.0
 * @since June,2002
 */
class TranslationItem
{
	String TranName;
	SimpleNode TranEntry;

        /**
         * Constructor.
         */
	public TranslationItem()
	{
		this("", null);
	}

	public TranslationItem(String TranName, SimpleNode TranEntry)
	{
		this.TranName = TranName;
		this.TranEntry = TranEntry;
	}
}

// EOF
