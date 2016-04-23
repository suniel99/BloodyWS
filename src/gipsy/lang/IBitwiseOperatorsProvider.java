package gipsy.lang;


/**
 * All delegates and providers that wish to provide implementation
 * of logic operators must implement this interface.
 * 
 * @author Serguei Mokhov
 * @version $Id: IBitwiseOperatorsProvider.java,v 1.1 2007/11/22 15:11:14 mokhov Exp $
 * @since 1.0.0, November 2007
 */
public interface IBitwiseOperatorsProvider
{
	// Binary operators
	
	GIPSYType bitwiseAnd(GIPSYType poLHS, GIPSYType poRHS);
	GIPSYType bitwiseOr(GIPSYType poLHS, GIPSYType poRHS);

	/**
	 * NOT AND.
	 * @param poLHS
	 * @param poRHS
	 * @return
	 */
	GIPSYType bitwiseNand(GIPSYType poLHS, GIPSYType poRHS);

	/**
	 * NOT OR.
	 * @param poLHS
	 * @param poRHS
	 * @return
	 */
	GIPSYType bitwiseNor(GIPSYType poLHS, GIPSYType poRHS);

	/**
	 * Logical XOR, unlike bitwise XOR, works on truth
	 * values similarly to logical OR or AND.
	 *
	 * @param poLHS
	 * @param poRHS
	 * @return
	 */
	GIPSYType bitwiseXor(GIPSYType poLHS, GIPSYType poRHS);

	// Unary operators

	GIPSYType bitwiseNot(GIPSYType poRHS);
}

// EOF
