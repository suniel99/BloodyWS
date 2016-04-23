package gipsy.GIPC.intensional.GenericTranslator;

/**
 * Interface for defining the terminal.
 *
 * $Id: G.java,v 1.9 2005/06/15 04:28:56 mokhov Exp $
 *
 * @author Aihua Wu (initial version)
 * @author Serguei Mokhov (refactoring)
 * @version 1.0
 * @since June, 2002.
 */
interface G
{
	/**
	 * Rule grammar terminals.
	 */
	String TERMINALS[] =
	{
		"+", "-", "||", "*", "/", "%", "&&", ";", "(", ")", "#",
		"@", "==", "!=", "<", ">", "<=", ">=", "^", "if", "then",
		"else", "where", "end", "Id", "IntType", "RealType", "L",
		"R", "!", "$", "D", ".", ":", "="
	};

	/**
	 * Translation steps.
	 */
	String STEPS[] =
	{
		"<TranRule>", "<RR>", "<Rule>", "<E>", "<E1>", "<term>",
		"<term1>", "<QR>", "<Q>", "<factor>", "<sign>", "<addOp>",
		"<multOp>", "<relOp>"
	};

	/**
	 * Translation rules.
	 */
	String RULES[] =
	{
		/*line1*/
		" ",
		"<TranRule> ::= <Rule> <RR> ",
		"<RR> ::= <Rule> <RR> ",
		"<RR> ::= empty",
		"~0<Rule> ::= Id : <E> ",

		/*5*/
		"<E> ::= <term> <E1> ",
		"~A<E> ::= <sign> <term> <E1> ",
		"~1<E> ::= if <E> ~1 then <E> ~2 else <E> ~3 ; <E1> ",
		"~2<E> ::= # . D <E1> ",
		"~6<E1> ::= <addOp> <term> <E1> ",

		/*10*/
		"~7<E1> ::= <relOp> <E> ",
		"~3<E1> ::= @ . D <E> ~4 ",
		"~4<E1> ::= where <Q> <QR> end ; ",
		"<E1> ::= empty",
		"~8<term> ::= <factor> <term1> ",

		/*15*/
		"~9<term1> ::= <multOp> <term> <term1> ",
		"<term1> ::= empty", "<QR> ::= <Q> <QR> ", "<QR> ::= empty",
		"~5<Q> ::= Id = <E> ~5 ",

		/*20*/
		"<factor> ::= Id ", "<factor> ::= IntType ",
		"<factor> ::= RealType ", "<factor> ::= ( <E> ) ~6 ",
		"<factor> ::= L ",

		/*25*/
		"<factor> ::= ! ( <E> ) ~7 ", "<factor> ::= R ",
		"<sign> ::= + ", "<sign> ::= - ", "<addOp> ::= + ",

		/*30*/
		"<addOp> ::= - ", "<addOp> ::= || ", "<multOp> ::= * ",
		"<multOp> ::= / ", "<multOp> ::= % ",

		/*35*/
		"<multOp> ::= && ", "<relOp> ::= < ", "<relOp> ::= > ",
		"<relOp> ::= <= ", "<relOp> ::= >= ",

		/*40*/
		"<relOp> ::= == ", "<relOp> ::= != "
	};


	/**
	 * Symbol table for rule tokens.
	 */
	int SYMBOL_TABLE[][] =
	{
		/*line 1*/
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, -1},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 3},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, -1},
		{6, 6, -1, 0, 0, 0, 0, -1, 5, -1, 8, -1, -1, -1, -1, -1, -1, -1, -1, 7, -1, -1, -1, 0, 5, 5, 5, 5, 5, 5, 0},
		{9, 9, 9, 0, 0, 0, 0, 13, 13, 13, 0, 11, 10, 10, 10, 10, 10, 10, 13, 0, 13, 13, 12, 13, 13, 13, 13, 0, 0, 0, 0},

		/*5*/
		{-1, -1, -1, -1, -1, -1, -1, -1, 14, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, 0, 14, 14, 14, 14, 14, 14, 0},
		{16, 16, 16, 15, 15, 15, 15, 16, 16, 16, 0, 16, 16, 16, 16, 16, 16, 16, 16, 0, 16, 16, 16, 16, 16, 16, 16, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 17, 0, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 19, 0, 0, 0, 0, 0, 0},
		{-1, -1, -1, -1, -1, -1, -1, -1, 23, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, 0, 20, 21, 22, 24, 26, 25, 0},

		/*10*/
		{27, 28, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, 0},
		{29, 30, 31, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, 0},
		{0, 0, 0, 32, 33, 34, 35, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, 0},
		{-1, -1, 0, 0, 0, 0, 0, 0, -1, 0, -1, 0, 40, 41, 36, 37, 38, 39, 0, -1, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, 0}
	};

	/**
	 * Default filename extension for rules.
	 */
	String RULE_FILE_EXTENSION = ".rul";

	/**
	 * Default filename extension for errors.
	 */
	String ERROR_FILE_EXTENSION = ".err";

	/**
	 * Default filename extension for tokens.
	 */
	String TOKEN_FILE_EXTENSION = ".tkn";

	/**
	 * Default filename extension for production.
	 */
	String PRODUCTION_FILE_EXTENSION = ".pro";
}

// EOF
