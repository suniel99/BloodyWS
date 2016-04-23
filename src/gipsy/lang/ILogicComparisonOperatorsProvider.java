package gipsy.lang;


/**
 * All delegates and providers that wish to provide implementation
 * of logic comparison operators must implement this interface.
 * 
 * @author Serguei Mokhov
 * @version $Id: ILogicComparisonOperatorsProvider.java,v 1.1 2007/12/02 05:33:55 mokhov Exp $
 * @since 1.0.0, November 2007
 */
public interface ILogicComparisonOperatorsProvider
{
	// Binary operators
	
	GIPSYBoolean greaterThan(GIPSYType poLHS, GIPSYType poRHS);
	GIPSYBoolean lessThan(GIPSYType poLHS, GIPSYType poRHS);
	GIPSYBoolean greaterThanOrEqualTo(GIPSYType poLHS, GIPSYType poRHS);
	GIPSYBoolean lessThanOrEqualTo(GIPSYType poLHS, GIPSYType poRHS);
	GIPSYBoolean equalTo(GIPSYType poLHS, GIPSYType poRHS);
	GIPSYBoolean notEqualTo(GIPSYType poLHS, GIPSYType poRHS);
}

// EOF
