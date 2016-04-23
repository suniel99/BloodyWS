package gipsy.lang;


/**
 * All delegates and providers that wish to provide implementation
 * of arithmetic operators must implement this interface.
 * 
 * @author Serguei Mokhov
 * @version $Id: IArithmeticOperatorsProvider.java,v 1.4 2008/11/06 00:34:31 mokhov Exp $
 * @since 1.0.0
 */
public interface IArithmeticOperatorsProvider
{
	GIPSYType add(GIPSYType poLHS, GIPSYType poRHS);
	//GIPSYType add(GIPSYType poRHS);
	GIPSYType subtract(GIPSYType poLHS, GIPSYType poRHS);
	//GIPSYType subtract(GIPSYType poRHS);

//	GIPSYType multiply(GIPSYType poLHS, GIPSYType poRHS);
//	GIPSYType divide(GIPSYType poLHS, GIPSYType poRHS);
//	GIPSYType mod(GIPSYType poLHS, GIPSYType poRHS);
//	GIPSYType pow(GIPSYType poLHS, GIPSYType poRHS);
}

// EOF
