package gipsy.GIPC.intensional.GenericTranslator;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;


/**
 * A Lexical Analyzer for SIPL-GIPL Translator.
 * 
 * @author aihua_wu
 * @version 1.0
 * @since June,2002
 * @see interface T
 */
class TranslationLexer
implements T
{
	int TokenType;

	String Id;

	int IntVal;

	double RealVal;

	int Ln, Col;

	boolean Valid;

	String errMessage[] =
	{
			"Illegal number.", "String is too long."
	};

	boolean FileEnd = false, InComments = false;

	boolean MustBackUp = false, UpDateCol = false;

	int Line = 1, Column = 1, LastCol = 0;

	private InputStream source;

	private PrintStream errFile;

	char BackChar = ' ';

	/**
	 * Constructor. Initalizes the Tokens's type.
	 */
	public TranslationLexer()
	{
		TokenType = -1;
		Id = "";
		IntVal = 0;
		RealVal = 0.0;
		Ln = 1;
		Col = 1;
		Valid = true;
	}

	/**
	 * Initalizes the input and print file.
	 * 
	 * @param source Input file name.
	 * @param errFile output file for error messages.
	 */
	public TranslationLexer(InputStream source, PrintStream errFile)
	{
		this.source = source;
		this.errFile = errFile;
	}

	/**
	 * Outputs the error message to monitor and error file. Error type is about char.
	 * 
	 * @param c error char.
	 */
	public void errMsg(char c) throws IOException
	{
		int colpos;
		if (Column < 2)
		{
			colpos = 1;
		}
		else
		{
			colpos = Column - 1;
		}
		Translator.bRight = false;
		if (BackChar == '\n')
		{
			System.out.println("Illegal Char: ,,," + c + " lies in " + (Line - 1) + " Line, " + (LastCol - 1)
			+ " Column.");
			errFile.println("Illegal Char: " + c + " lies in " + (Line - 1) + " Line, " + (LastCol - 1) + " Column.");
		}
		else
		{
			System.out.println("Illegal Char: :::" + c + " lies in " + Line + " Line, " + colpos + " Column.");
			errFile.println("Illegal Char: " + c + " lies in " + Line + " Line, " + colpos + " Column.");
		}
		Translator.iErrorCount++;
	}

	/**
	 * Outputs the error message to monitor and error file. Error type is about number.
	 * 
	 * @param n error Integer value.
	 */
	public void errMsg(int n) throws IOException
	{
		int colpos;
		if (Column < 2)
		{
			colpos = 1;
		}
		else
		{
			colpos = Column;
		}
		Translator.bRight = false;
		if (BackChar == '\n')
		{
			System.out.println(errMessage[n] + " lies in " + (Line - 1) + " Line, " + Column + " Column.");
			errFile.println(errMessage[n] + " lies in " + (Line - 1) + " Line, " + (LastCol - 1) + " Column.");
		}
		else
		{
			System.out.println(errMessage[n] + " lies in " + Line + " Line, " + colpos + " Column.");
			errFile.println(errMessage[n] + " lies in " + Line + " Line, " + colpos + " Column.");
		}
		Translator.iErrorCount++;
	}

	/**
	 * Judges if a char is correct.
	 * 
	 * @param c the char.
	 * @return Integer number of the char in the correctchar_array.
	 */
	public int CorrectChar(char c)
	{
		for(int i = 0; i < CorrectCharType.length; i++)
		{
			if (c == CorrectCharType[i])
			{
				return (i);
			}
		}
		return (c);
	}

	/**
	 * Checks if a token is reserved.
	 * 
	 * @param s1 the token.
	 * @param s2 the reserve in a reserve-array.
	 */
	public static boolean isReserved(String s1, String s2)
	{
		boolean ForT = true;

		if (s1.length() != s2.length())
		{
			ForT = false;
		}
		else
		{
			for(int i = 0; i < s1.length(); i++)
			{
				if (s1.charAt(i) != s2.charAt(i))
				{
					ForT = false;
				}
			}
		}

		return ForT;
	}

	/**
	 * Deletes a char from token when there is backup.
	 * 
	 * @param PreToken a buffer of String for token.
	 */
	public void backupChar(StringBuffer PreToken)
	{
		PreToken.deleteCharAt(PreToken.length() - 1);
		Column--;
		MustBackUp = true;
	}

	/**
	 * Checks if the backup char is correct.
	 * 
	 * @return <code>true</code> the backup char is valid. <code>false</code> the backup char is invalid.
	 */
	public boolean correctBackUpChar()
	{
		boolean Valid = false;
		if (((BackChar <= '9') && (BackChar >= '0')) || ((BackChar <= 'z') && (BackChar >= 'a'))
		&& ((BackChar <= 'A') && (BackChar >= 'Z')) || (BackChar == '\r'))
		{
			Valid = true;
		}
		for(int i = 0; i < CorrectCharType.length; i++)
		{
			if (BackChar == CorrectCharType[i])
			{
				Valid = true;
			}
		}
		return (Valid);
	}

	/**
	 * Gets next char.
	 * 
	 * @param PreToken for token string.
	 * @return Integer value for Char got.
	 */
	public int NextChar(StringBuffer PreToken) throws IOException
	{
		char c;
		int CharType = OtherCharType;
		if (!MustBackUp)
		{
			c = (char)source.read();
			if (c == '\r')
			{
				c = (char)source.read();
				//MustBackUp=true;
			}
			PreToken.append(c);
			Column++;
			BackChar = c;
		}
		else
		{
			c = BackChar;
			PreToken.append(c);
			Column++;
		}
		if (c == (char)-1)
		{
			FileEnd = true;
			PreToken.deleteCharAt(PreToken.length() - 1);
			Column--;
			CharType = OtherCharType;
		}
		else
		{
			if (c == '\n')
			{
				if (!MustBackUp)
				{
					Line++;
					UpDateCol = true;
					CharType = OtherCharType;
				}
				else
				{
					PreToken.deleteCharAt(PreToken.length() - 1);
					Column--;
					CharType = OtherCharType;
				}
			}
			else if ((c >= '0') && (c <= '9'))
			{
				CharType = DigitType;
			}
			else if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')))
			{
				CharType = LetterType;
			}
			else if (c == '\t')
			{
				CharType = CorrectChar(' ');
			}
			else if ((CharType = CorrectChar(c)) < CorrectCharType.length)
			{
			}
			else
			{
				CharType = OtherCharType;
				if (!MustBackUp)
				{
					errMsg(c);
				}
			}
		}
		MustBackUp = false;
		return (CharType);
	}

	/**
	 * Generates a token after getting chars.
	 * 
	 * @param State the Integer value of state number for the driven table.
	 * @param PreToken the temporary token String.
	 */
	public TranslationLexer createToken(int State, StringBuffer PreToken) throws IOException
	{
		TranslationLexer temp = new TranslationLexer();
		int initcol;
		temp.TokenType = Table[State][TypeMsg];
		if (State == SpecicalState)
		{
			String s = new String(PreToken);
			if (BackChar == '\n')
			{
				temp.Ln = Line - 1;
			}
			else
			{
				temp.Ln = Line;
			}
			if ((temp.Col = (Column - s.length())) < 1)
			{
				temp.Col = 1;
			}
			temp.IntVal = Integer.parseInt(s.substring(0, s.length() - 1));
			initcol = Column;
			Column = temp.Col;
			errMsg(0);
			Column = initcol;
			temp.Valid = false;
			return (temp);
		}
		String s = new String(PreToken);
		temp.Id = s;
		if (BackChar == '\n')
		{
			temp.Ln = Line - 1;
		}
		else
		{
			temp.Ln = Line;
		}
		if ((temp.Col = (Column - s.length())) < 1)
		{
			temp.Col = 1;
		}
		if (UpDateCol)
		{
			LastCol = Column;
			Column = 1;
			UpDateCol = false;
		}
		if (temp.TokenType == IdType)
		{
			if (s.length() > StringLimit)
			{
				initcol = Column;
				Column = temp.Col;
				errMsg(1);
				Column = initcol;
				temp.Valid = false;
			}
			else
			{
				for(int i = ReserveBegin; i <= ReserveEnd; i++)
				{
					if (isReserved(s, Tokens[i]))
					{
						temp.TokenType = i;
					}
				}
			}
		}
		else if ((temp.TokenType == IntType) || (temp.TokenType == RealType))
		{
			if (s.length() > IntLimit)
			{
				initcol = Column;
				Column = temp.Col;
				errMsg(0);
				Column = initcol;
				temp.Valid = false;
			}
			else if (temp.TokenType == IntType)
			{
				temp.IntVal = Integer.parseInt(s);
			}
			else
			{
				temp.RealVal = Double.parseDouble(s);
			}
		}
		return (temp);
	}

	/**
	 * Gets next token.
	 */
	public TranslationLexer NextToken() throws IOException
	{
		int State = 1;
		TranslationLexer temp = new TranslationLexer();
		StringBuffer s = new StringBuffer();
		int initcol;
		while(temp.TokenType == -1)
		{
			int CharValue = NextChar(s);
			if (FileEnd)
			{
				if (finalState[Table[State][OtherCharType] - 1])
				{
					temp = createToken(Table[State][OtherCharType], s);
					Translator.bFileEnd = true;
					if (temp.TokenType == -1)
					{
						temp.TokenType = -2;
					}
				}
				if (temp.TokenType == -1)
				{
					temp.TokenType = -2;
				}
			}
			else
			{
				int OrigalState = State;
				State = Table[State][CharValue];

				if (finalState[State - 1])
				{
					if (State == SpecicalState)
					{
						backupChar(s);
						temp = createToken(State, s);
						s = new StringBuffer();
						State = 1;
						return (temp);
					}
					else
					{
						if (Table[State][BackUp] > 0)
						{
							backupChar(s);
						}
						if (State != OtherState)
						{
							temp = createToken(State, s);
							return (temp);
						}
						if ((State == OtherState) && (BackChar == '_'))
						{
							errMsg(BackChar);
							State = 1;
						}
						if ((State == OtherState) && (BackChar == '\n'))
						{
							LastCol = Column;
							Column = 1;
							UpDateCol = false;
						}
						s = new StringBuffer();
						State = 1;
					}
				}
				if ((OrigalState == BeginState) && (State == BeginState))
				{
					s = new StringBuffer();
				}
				if ((OrigalState == BeginState) && (State == OtherState))
				{
					s = new StringBuffer();
					State = 1;
				}
				if ((OrigalState == OtherState) && (State == BeginState))
				{
					if (correctBackUpChar())
					{
						s.append(BackChar);
					}
				}
				if (State == errState)
				{
					initcol = Column;
					Column = temp.Col;
					errMsg(0);
					Column = initcol;
					State = 1;
				}
			}
		}
		return (temp);
	}
}

// EOF
