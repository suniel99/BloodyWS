package gipsy.lang;


/**
 * All delegates and providers that wish to provide implementation
 * of logic operators must implement this interface.
 * 
 * @author Serguei Mokhov
 * @version $Id: ILogicOperatorsProvider.java,v 1.1 2007/11/22 15:11:14 mokhov Exp $
 * @since 1.0.0, November 2007
 */
public interface ILogicOperatorsProvider
{
	// Binary operators
	
	GIPSYType and(GIPSYType poLHS, GIPSYType poRHS);
	GIPSYType or(GIPSYType poLHS, GIPSYType poRHS);

	/**
	 * NOT AND.
	 * @param poLHS
	 * @param poRHS
	 * @return
	 */
	GIPSYType nand(GIPSYType poLHS, GIPSYType poRHS);

	/**
	 * NOT OR.
	 * @param poLHS
	 * @param poRHS
	 * @return
	 */
	GIPSYType nor(GIPSYType poLHS, GIPSYType poRHS);

	/**
	 * Logical XOR, unlike bitwise XOR, works on truth
	 * values similarly to logical OR or AND.
	 *
	 * @param poLHS
	 * @param poRHS
	 * @return
	 */
	GIPSYType xor(GIPSYType poLHS, GIPSYType poRHS);

	// Unary operators

	GIPSYType not(GIPSYType poRHS);
}

// EOF
