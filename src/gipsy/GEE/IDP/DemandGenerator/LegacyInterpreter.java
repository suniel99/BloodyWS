package gipsy.GEE.IDP.DemandGenerator;

import gipsy.GEE.CONFIG;
import gipsy.GEE.GEEException;
import gipsy.GEE.IVW.Warehouse.IVWControl;
import gipsy.GEE.IVW.Warehouse.IVWInterface;
import gipsy.GIPC.intensional.SimpleNode;
import gipsy.GIPC.intensional.GIPL.GIPLParserTreeConstants;
import gipsy.GIPC.intensional.SIPL.IndexicalLucid.IndexicalLucidParserTreeConstants;
import gipsy.lang.GIPSYContext;
import gipsy.lang.GIPSYInteger;
import gipsy.lang.GIPSYType;
import gipsy.lang.GenericArithmeticOperatorsDelegate;
import gipsy.lang.IArithmeticOperatorsProvider;
import gipsy.storage.Dictionary;
import gipsy.storage.DictionaryItem;
import gipsy.util.GIPSYRuntimeException;
import marf.util.Arrays;
import marf.util.Debug;


/**
 * <p>Legacy eager GEE Executor/Interpreter.</p>
 * 
 * <p>Input: the dictionary from a GIPSYProgram.</p>
 * 
 * TODO: Problems: dimension is numbered as the identifier, space for dimensions is wasted.
 * 
 * @author Peter Grogono (C++)
 * @author Bo Lu (Java)
 * @author Lei Tao (Warehouse)
 * @author Serguei Mokhov (integration, threading, CPs and STs, and refactoring)
 * 
 * @version $Id: LegacyInterpreter.java,v 1.6 2010/12/23 19:39:46 mokhov Exp $
 * @since November 18, 2002
 */
public class LegacyInterpreter
extends DemandGenerator
implements CONFIG, GIPLParserTreeConstants
{
	/**
	 * Main dictionary.
	 */
	protected Dictionary oDictionary = new Dictionary();

	/**
	 * Dictionary with only idName and its number.
	 */
	protected Dictionary oMiniDictionary = new Dictionary(); 

	/**
	 * Warehouse reference.
	 * Default is IVWControl.
	 * @see IVWControl
	 */
	protected IVWInterface oValueHouse = new IVWControl();


	/**
	 * Assumes all the values at their defaults.
	 */
	public LegacyInterpreter()
	{
		// Intentionally empty.
	}
	
    /**
     * Allows setting a different than the default data warehouse.
     * @param poValueHouse the new data warehouse to set.
     * @see #oValueHouse
     */
    public LegacyInterpreter(IVWInterface poValueHouse)
    {
        this.oValueHouse = poValueHouse;
    }  

	/**
	 * Original context is necessary to be got back for situations like binary operations
	 * clone all values of the original context.
	 * @deprecated
	 */
	public int[] getBack(int[] paiOriginalContext)
	{
		int[] aiContextTmp = new int[DIMENSION_MAX];
		Arrays.copy(aiContextTmp, paiOriginalContext, DIMENSION_MAX);
		return aiContextTmp;
	}

	/**
	 * Original context is necessary to be got back for situations like binary operations
	 * clone all values of the original context.
	 */
	public GIPSYContext[] getBack(GIPSYContext[] paoOriginalContext)
	{
		GIPSYContext[] aoContextTmp = new GIPSYContext[DIMENSION_MAX];
		Arrays.copy(aoContextTmp, paoOriginalContext, DIMENSION_MAX);
		return aoContextTmp;
	}

	/**
	 * Older implementation of the main evaluation routine
	 * based on the local interpreter, integer dimensions,
	 * and integer types.
	 * 
	 * @param poExpressionRoot Lucid expression's root node in the AST
	 * @param paiContext current integer context of evaluation
	 * @param piIndent indentation for the printing purposes as we descend into the tree
	 * @return the value the expression evaluates to, an integer
	 * @throws GEEException
	 */
	@Deprecated
	public int eval(SimpleNode poExpressionRoot, int[] paiContext, int piIndent) 
	throws GEEException
	{
		// This line is added to ensure we find the identifier in case the id is not initialized.
		//expr.ID =  miniDict.indexOf( expr.image );
		//System.out.println( "Evaluating: " +" __ " + expr.id + " __ " + expr.image + " __ "+ expr.ID + " at: " + cont[1] );

//		if(poExpressionRoot == null)
//		{
//			return 123;
//		}

		// result 1 is usually the LHS of the expression, and result 2 us RHS
		int iResult1, iResult2;
		
		// Current context index.
		int iDimension;

		// just to keep the original context
		int[] aiOriginalContext = new int[DIMENSION_MAX];
		Arrays.copy(aiOriginalContext, 0, paiContext);

		// The order of children is fixed in the syntax checking
		switch(poExpressionRoot.id)
		{
			// Core @ & # algebra

			case JJTAT:
			{
				iDimension = ((SimpleNode)poExpressionRoot.children[1]).ID;
				paiContext[iDimension] = eval((SimpleNode)poExpressionRoot.children[2], paiContext, piIndent + 1);
				return eval((SimpleNode)poExpressionRoot.children[0], paiContext, piIndent + 1);
			}

			case JJTHASH:
			{
				iDimension = ((SimpleNode)poExpressionRoot.children[0]).ID;
				return paiContext[iDimension];
			}

			// arith_op

			case JJTADD:
			{
				iResult1 = eval((SimpleNode)poExpressionRoot.children[0], paiContext, piIndent + 1);
				paiContext = getBack(aiOriginalContext);
				iResult2 = eval((SimpleNode)poExpressionRoot.children[1], paiContext, piIndent + 1);
				return iResult1 + iResult2;
			}
        
			case JJTMIN:
			{
				iResult1 = eval((SimpleNode)poExpressionRoot.children[0], paiContext, piIndent + 1);
				paiContext = getBack(aiOriginalContext);
				iResult2 = eval((SimpleNode)poExpressionRoot.children[1], paiContext, piIndent + 1);
				return iResult1 - iResult2;
			}

			case JJTTIMES:
			{
				iResult1 = eval((SimpleNode)poExpressionRoot.children[0], paiContext, piIndent + 1);
				paiContext = getBack(aiOriginalContext);
				iResult2 = eval((SimpleNode)poExpressionRoot.children[1], paiContext, piIndent + 1);
				return iResult1 * iResult2;
			}

			case JJTDIV:
			{
				iResult1 = eval((SimpleNode)poExpressionRoot.children[0], paiContext, piIndent + 1);
				paiContext = getBack(aiOriginalContext);
				iResult2 = eval((SimpleNode)poExpressionRoot.children[1], paiContext, piIndent + 1);
				return iResult1 / iResult2;
			}

			case JJTMOD:
			{
				iResult1 = eval((SimpleNode)poExpressionRoot.children[0], paiContext, piIndent + 1);
				paiContext = getBack(aiOriginalContext);
				iResult2 = eval((SimpleNode)poExpressionRoot.children[1], paiContext, piIndent + 1);
				return iResult1 % iResult2;
			}

			// rel_op
			
			case JJTLT:
			{
				iResult1 = eval((SimpleNode)poExpressionRoot.children[0], paiContext, piIndent + 1);
				paiContext = getBack(aiOriginalContext);
				iResult2 = eval((SimpleNode)poExpressionRoot.children[1], paiContext, piIndent + 1);
					
				if(iResult1 < iResult2)
				{
					return 1;
				}
				else
				{
					return 0;
				}
			}
    
			case JJTGT:
			{
				iResult1 = eval((SimpleNode)poExpressionRoot.children[0], paiContext, piIndent + 1);
				paiContext = getBack(aiOriginalContext);
				iResult2 = eval((SimpleNode)poExpressionRoot.children[1], paiContext, piIndent + 1);
				
				if(iResult1 > iResult2)
				{
					return 1;
				}
				else
				{
					return 0;
				}
			}

			case JJTLE:
			{
				iResult1 = eval((SimpleNode)poExpressionRoot.children[0], paiContext, piIndent + 1);
				paiContext = getBack(aiOriginalContext);
				iResult2 = eval((SimpleNode)poExpressionRoot.children[1], paiContext, piIndent + 1);

				if(iResult1 <= iResult2)
				{
					return 1;
				}
				else
				{
					return 0;
				}
			}
      
			case JJTGE:
			{
				iResult1 = eval((SimpleNode)poExpressionRoot.children[0], paiContext, piIndent + 1);
				paiContext = getBack( aiOriginalContext );
				iResult2 = eval((SimpleNode)poExpressionRoot.children[1], paiContext, piIndent + 1);
				
				if(iResult1 >= iResult2)
				{
					return 1;
				}
				else
				{
					return 0;
				}
			}
      
			case JJTEQ:
			{
				iResult1 = eval((SimpleNode) poExpressionRoot.children[0], paiContext, piIndent + 1);
				paiContext = getBack(aiOriginalContext);
				iResult2 = eval((SimpleNode) poExpressionRoot.children[1], paiContext, piIndent + 1);
				
				if(iResult1 == iResult2)
				{
					return 1;
				}
				else
				{
					return 0;
				}
			}
      
			case JJTNE:
			{
				iResult1 = eval((SimpleNode)poExpressionRoot.children[0], paiContext, piIndent + 1);
				paiContext = getBack(aiOriginalContext);
				iResult2 = eval((SimpleNode)poExpressionRoot.children[1], paiContext, piIndent + 1);

				if(iResult1 != iResult2)
				{
					return 1;
				}
				else
				{
					return 0;
				}
			}
          
			// log_op

			case JJTAND:
			{
				iResult1 = eval((SimpleNode)poExpressionRoot.children[0], paiContext, piIndent + 1);
				paiContext = getBack(aiOriginalContext);
				iResult2 = eval((SimpleNode)poExpressionRoot.children[1], paiContext, piIndent + 1);

				if(iResult1 == 1 && iResult2 == 1)
				{
					return 1;
				}
				else
				{
					return 0;
				}
			}

			case JJTOR:
			{
				iResult1 = eval((SimpleNode) poExpressionRoot.children[0], paiContext, piIndent + 1);
				paiContext = getBack(aiOriginalContext);
				iResult2 = eval((SimpleNode) poExpressionRoot.children[1], paiContext, piIndent + 1);

				if(iResult1 == 1 || iResult2 == 1 )
				{
					return 1; // true
				}
				else
				{
					return 0; // false
				}
			}
          
			case JJTIF:
			{
				// if true
				if(eval((SimpleNode)poExpressionRoot.children[0], paiContext, piIndent + 1) == 1)  
				{
					paiContext = getBack(aiOriginalContext);
					return eval((SimpleNode) poExpressionRoot.children[1], paiContext, piIndent + 1);
				}
				else
				{
					paiContext = getBack(aiOriginalContext);
					return eval((SimpleNode) poExpressionRoot.children[2], paiContext, piIndent + 1);
				}
			}

			/* 
			 * The key of the vhouse is a string combined with the id and its context.
			 * Enquire the id+context in the value house
			 *	if it exists, return the value
			 *	if it doesn't, evaluate the id+context
			 */
			case JJTID:
			{
				String strID = (new Integer(poExpressionRoot.ID)).toString();

				try
				{
					DictionaryItem oIDEntry = (DictionaryItem)this.oDictionary.get(poExpressionRoot.ID);
					StringBuffer oBuffer = new StringBuffer(strID);

					for(int i = 0; i < DIMENSION_MAX; i++)
					{
						oBuffer = oBuffer.append(",");
						Integer iTmpContextItem = new Integer(paiContext[i]);
						oBuffer = oBuffer.append(iTmpContextItem.toString());
					}

					//Integer result = (Integer)vhouse.get(sbuff.toString());
					//XXX: null is a test hardcoding
					//Integer iIDResult = (Integer)oValueHouse.getValue(oBuffer.toString());
					Integer iIDResult = null;

					// Not in the DS
					if(iIDResult == null)
					{
						int iResultEval = eval((SimpleNode)(oIDEntry.getEntry()), paiContext, piIndent + 1);
						//vhouse.put(sbuff.toString(),new Integer(result_eval));
						//XXX: uncomment: oValueHouse.setValue(oBuffer.toString(), new Integer(iResultEval));

						return iResultEval;
					}
					else
					{
						return iIDResult.intValue();
					}
				}
				catch(ArrayIndexOutOfBoundsException e)  // in case the id is null
				{
					//System.err.println("LegacyInterpreter couldn't resolve the identifier symbol: " + s);
					throw new GEEException("LegacyInterpreter couldn't resolve the identifier symbol: " + strID);
				}
			}

			// the value of a constant is its image
			case JJTCONST:
			case IndexicalLucidParserTreeConstants.JJTCONST:
			{
				return (new Integer(poExpressionRoot.getImage())).intValue();
				//return (new Number(expr.getImage())).intValue();
			}

			default:
			{
				throw new GEEException
				(
					"LegacyInterpreter bad node type: " + poExpressionRoot.id
					+ " of node " + poExpressionRoot
				);
			}
		}
	}

	/**
	 * Main execution method.
	 * @param poDictionary
	 * @param pstrDimensionValues
	 * @deprecated in favor of GIPSYContext version
	 */
	@Deprecated
	public int execute(Dictionary poDictionary, String pstrDimensionValues)
	{
		DictionaryItem oDictionaryElement;

		boolean bFini = false;

		for(int k = 0; k < poDictionary.size(); k++)
		{
			oDictionaryElement = (DictionaryItem)poDictionary.elementAt(k);
			this.oDictionary.add(oDictionaryElement.getID(), oDictionaryElement);
			this.oMiniDictionary.add(oDictionaryElement.getID(), oDictionaryElement.getName());
		}

		Debug.debug
		(
			"pram dic: " + poDictionary
			+ ", this dic: " + this.oDictionary
			+ ", mini dic: " + this.oMiniDictionary
		);
		
		int[] aiContexts = new int[DIMENSION_MAX];

		for(int iContextualIndex = 0; iContextualIndex < DIMENSION_MAX; iContextualIndex++)
		{
			aiContexts[iContextualIndex] = 0;
		}

		// buildDict();
		
		// parse dimensions values, which are in a form: d=2,m=3
//		pstrDimensionValues = "d=2";

		StringBuffer oDimensionValue = new StringBuffer(pstrDimensionValues);
		oDimensionValue.append(",");
		
		String strSubS, strSubS1, strSubS2;
		
		int iIndex1, iIndex2;
		int iDimId;

		// "> 1" is because the "," we add above automatically
		// makes the length != 0 and == 1, so weed need more than
		// just a trailing comma.
		while(oDimensionValue.length() > 1)
		{
			// TODO: this parsing business does not belong
			// to the interpreter. Ought to move it to GIPC.
			iIndex1 = oDimensionValue.indexOf(",");

			strSubS = oDimensionValue.substring(0, iIndex1);

			strSubS1 = new String(strSubS);
			strSubS2 = new String(strSubS);

			iIndex2 = strSubS.indexOf("=");
			strSubS2 = strSubS1.substring(iIndex2 + 1);
			strSubS1 = strSubS.substring(0, iIndex2);

			iDimId = this.oMiniDictionary.indexOf(strSubS1);
			
			System.out.println
			(
				"idx=" + iIndex1
				+ ", subS=" + strSubS
				+ ", subS1=" + strSubS1
				+ ", subS2=" + strSubS2
				+ ",idx2=" + iIndex2
				+ ",dimId=" + iDimId
			);

			System.out.println("cont.length=" + aiContexts.length);

			// TODO: need to guard dimId against DIMENSION_MAX (eg. DIMENSION_MAX = 1 causes this to fail)
			if(iDimId != -1)
			{
				aiContexts[iDimId] = (new Integer(strSubS2)).intValue();
			}
			else
			{
				System.err.println("LegacyInterpreter cannot resolve dimension symbol: " + strSubS1);
				bFini = true; // if the demanded thing can't be resolved, no further processing
			}

			oDimensionValue = new StringBuffer(oDimensionValue.substring(iIndex1 + 1));
		}

	    if(bFini == false)
	    {
			try
			{
				//oDictionaryElement = (DictionaryItem)this.oDictionary.get(2);
				oDictionaryElement = (DictionaryItem)this.oDictionary.get(1);
				//oDictionaryElement = (DictionaryItem)oDictionary.get(0);

				Debug.debug("LegacyInterpreter: dictionary: " + this.oDictionary);
				Debug.debug("LegacyInterpreter: item: " + oDictionaryElement);

				int iResult = eval((SimpleNode)(oDictionaryElement.getEntry()), aiContexts, 0);
				System.out.println("The result of calculation is: " + iResult);

				return iResult;
			}
			catch(Exception e)
			{
				System.err.println("LegacyInterpreter: error calculating the result.");
				System.err.println(e.getMessage());
				e.printStackTrace(System.err);
				throw new GIPSYRuntimeException(e);
			}
		}

	    throw new GIPSYRuntimeException();
	}

	/**
	 * New version of <code>eval()</code> that corresponds to the new definitions
	 * of GIPSYContext.
	 * 
	 * @param poRoot root of the AST
	 * @param paoContext
	 * @param piIndent
	 * @return
	 * @throws GEEException
	 * 
	 * @since Serguei Mokhov
	 */
	public GIPSYType eval(SimpleNode poRoot, GIPSYContext[] paoContext, int piIndent) 
	throws GEEException
	{
		//int iResult1, iResult2;
		int iDimension;

		//IArithmeticOperatorsProvider oResult1;
		// XXX: pull up to a instance or class instance
		IArithmeticOperatorsProvider oArithmeticAlgebra = new GenericArithmeticOperatorsDelegate();

		GIPSYType oResult1;
		GIPSYType oResult2;
		
		// just to keep the original context
		GIPSYContext[] aoOriginalContext = new GIPSYContext[DIMENSION_MAX];
		Arrays.copy(aoOriginalContext, 0, paoContext);
		
		
		// The order of children is fixed in the syntax checking
		switch(poRoot.id)
		{
			// @ d
			case JJTAT:
			{
				iDimension = ( (SimpleNode) poRoot.children[1] ).ID ;
				paoContext[iDimension] = (GIPSYContext)eval( (SimpleNode) poRoot.children[2], paoContext, piIndent + 1);
				return eval( (SimpleNode) poRoot.children[0], paoContext, piIndent + 1);
			}

			// # d
			case JJTHASH:
			{
				iDimension = ( (SimpleNode) poRoot.children[0] ).ID ;
				return paoContext[iDimension];
			}

			// arith_op
			
			// x + y
			case JJTADD:
			{
				oResult1 = (GIPSYType)eval((SimpleNode)poRoot.children[0], paoContext, piIndent + 1);
				paoContext = getBack(aoOriginalContext);
				oResult2 = eval((SimpleNode) poRoot.children[1], paoContext, piIndent + 1);
				return oArithmeticAlgebra.add(oResult1, oResult2);
			}

			// x - y
			case JJTMIN:
			{
				oResult1 = (GIPSYInteger)eval((SimpleNode)poRoot.children[0], paoContext, piIndent + 1);
				paoContext = getBack(aoOriginalContext);
				oResult2 = eval((SimpleNode)poRoot.children[1], paoContext, piIndent + 1);
				//return ((GIPSYInteger)oResult1).subtract(oResult2);
				return oArithmeticAlgebra.subtract(oResult1, oResult2);
			}
/*
        case JJTTIMES:
          iResult1 = eval((SimpleNode) oRoot.children[0], paoContext, piIndent + 1) ;
          paoContext = getBack( aoOriginalContext );
          iResult2 = eval((SimpleNode) oRoot.children[1], paoContext, piIndent + 1) ;
          return iResult1 * iResult2;
        
        case JJTDIV:
          iResult1 = eval((SimpleNode) oRoot.children[0], paoContext, piIndent + 1) ;
          paoContext = getBack( aoOriginalContext );
          iResult2 = eval((SimpleNode) oRoot.children[1], paoContext, piIndent + 1) ;
          return iResult1 / iResult2;
        
        case JJTMOD:
          iResult1 = eval((SimpleNode) oRoot.children[0], paoContext, piIndent + 1) ;
          paoContext = getBack( aoOriginalContext );
          iResult2 = eval((SimpleNode) oRoot.children[1], paoContext, piIndent + 1) ;
          return iResult1 % iResult2;

          // rel_op
        case JJTLT:
          iResult1 = eval((SimpleNode) oRoot.children[0], paoContext, piIndent + 1) ;
          paoContext = getBack( aoOriginalContext );
          iResult2 = eval((SimpleNode) oRoot.children[1], paoContext, piIndent + 1) ;
			
          	if(iResult1 < iResult2)
				return 1;
			else
				return 0;
        
        case JJTGT:
          iResult1 = eval((SimpleNode) oRoot.children[0], paoContext, piIndent + 1) ;
          paoContext = getBack( aoOriginalContext );
          iResult2 = eval((SimpleNode) oRoot.children[1], paoContext, piIndent + 1) ;
          if (iResult1 > iResult2) return 1;
          else return 0;
          
        case JJTLE:
          iResult1 = eval((SimpleNode) oRoot.children[0], paoContext, piIndent + 1) ;
          paoContext = getBack( aoOriginalContext );
          iResult2 = eval((SimpleNode) oRoot.children[1], paoContext, piIndent + 1) ;
          if (iResult1 <= iResult2) return 1;
          else return 0;
          
      case JJTGE:
          iResult1 = eval((SimpleNode) oRoot.children[0], paoContext, piIndent + 1) ;
          paoContext = getBack( aoOriginalContext );
          iResult2 = eval((SimpleNode) oRoot.children[1], paoContext, piIndent + 1) ;
          if (iResult1 >= iResult2) return 1;
          else return 0;
          
        case JJTEQ:
          iResult1 = eval((SimpleNode) oRoot.children[0], paoContext, piIndent + 1) ;
          paoContext = getBack( aoOriginalContext );
          iResult2 = eval((SimpleNode) oRoot.children[1], paoContext, piIndent + 1) ;
          if (iResult1 == iResult2) return 1;
          else return 0;
          
        case JJTNE:
          iResult1 = eval((SimpleNode) oRoot.children[0], paoContext, piIndent + 1) ;
          paoContext = getBack( aoOriginalContext );
          iResult2 = eval((SimpleNode) oRoot.children[1], paoContext, piIndent + 1) ;
          if (iResult1 != iResult2) return 1;
          else return 0;
          
// log_op
        case JJTAND:
          iResult1 = eval((SimpleNode) oRoot.children[0], paoContext, piIndent + 1) ;
          paoContext = getBack( aoOriginalContext );
          iResult2 = eval((SimpleNode) oRoot.children[1], paoContext, piIndent + 1) ;
          if ( iResult1 == 1 && iResult2 == 1 ) return 1;
          else return 0;
          
        case JJTOR:
          iResult1 = eval((SimpleNode) oRoot.children[0], paoContext, piIndent + 1) ;
          paoContext = getBack( aoOriginalContext );
          iResult2 = eval((SimpleNode) oRoot.children[1], paoContext, piIndent + 1) ;
          if ( iResult1 == 1 || iResult2 == 1 ) return 1;  // true
          else return 0;    // false
          
        case JJTIF:
          if (eval((SimpleNode) oRoot.children[0], paoContext, piIndent + 1) == 1 )   //if true
          {
             paoContext = getBack( aoOriginalContext );
             return eval((SimpleNode) oRoot.children[1], paoContext, piIndent + 1);
          }
          else
          {
             paoContext = getBack( aoOriginalContext );
             return eval((SimpleNode) oRoot.children[2], paoContext, piIndent + 1);
          }

// The key of the vhouse is a string combined by the id and its context
// Enquire the id+context in the value house
//	if it exists, return the value
//	if it doesn't, evaluate the id+context

        case JJTID:
          String s = ( new Integer (oRoot.ID) ).toString();
          
          try
          {
            DictionaryItem identry = (DictionaryItem) oDictionary.get( oRoot.ID );
            StringBuffer sbuff = new StringBuffer(s);

            for ( int i = 0 ; i < DIMENSION_MAX; i++)
            {
              sbuff = sbuff.append(",");
              Integer tmp = new Integer (paoContext[i]);
              sbuff = sbuff.append (tmp.toString());
            }
            
            //Integer result = (Integer)vhouse.get(sbuff.toString());
            Integer result = (Integer)oValueHouse.getValue(sbuff.toString());

            if(result == null)
            {
              int result_eval = eval((SimpleNode)(identry.getEntry()), paoContext, piIndent + 1);
              //vhouse.put(sbuff.toString(),new Integer(result_eval));
              oValueHouse.setValue(sbuff.toString(),new Integer(result_eval));
              return result_eval;
            }
            else
            {
              return result.intValue();
            }

          }
          catch(ArrayIndexOutOfBoundsException e)  // in case the id is null
          {
            //System.err.println("LegacyInterpreter couldn't resolve the identifier symbol: " + s);
            throw new GEEException("LegacyInterpreter couldn't resolve the identifier symbol: " + s);
          }
          
          // the value of a constant is its image
        case JJTCONST:
        case IndexicalLucidParserTreeConstants.JJTCONST:
        {
			return ( new Integer ( oRoot.getImage() ) ).intValue();
          //return ( new Number ( expr.getImage() ) ).intValue();
        }
          
          */
        default:
         throw new GEEException("LegacyInterpreter bad node type: " + poRoot.id);
    }
  }

	/**
	 * Main execution method.
	 * @param poDictionary
	 * @param poDimensionTags
	 */
	public GIPSYType execute(Dictionary poDictionary, GIPSYContext poDimensionTags)
	{
		DictionaryItem oItem;
		
		boolean bFini = false;

		for(int k = 0; k < poDictionary.size(); k++)
		{
			oItem = (DictionaryItem)poDictionary.elementAt(k);
			this.oDictionary.add(oItem.getID(), oItem);
			this.oMiniDictionary.add(oItem.getID(), oItem.getName());
		}

		Debug.debug("pram dic: " + poDictionary + ", this dic: " + this.oDictionary + ", mini dic: " + this.oMiniDictionary);
	
		int[] aiContexts = new int[DIMENSION_MAX];
		
		for(int iContextualIndex = 0 ; iContextualIndex < DIMENSION_MAX; iContextualIndex++)
		{
			aiContexts[iContextualIndex] = 0;
		}
	
		// buildDict();
		
		// parse dimensions values, which are in a form: d=2,m=3
	
		//            pstrDimensionValues = "d=2";
			
		//            StringBuffer oDimensionValue = new StringBuffer(poDimensionTags);
		StringBuffer oDimensionValue = new StringBuffer(poDimensionTags.toString());
		oDimensionValue.append(",");
	
		String subS, subS1, subS2;
	
		int idx, idx2;
		int dimId;

		while(oDimensionValue.length() != 0)
		{
			// TODO: this parsing business does not belong
			// to the interpreter. Ought to move it to GIPC.
			idx = oDimensionValue.indexOf(",");
			subS = oDimensionValue.substring(0, idx);

			subS1 = new String(subS);
			subS2 = new String(subS);
			idx2 = subS.indexOf("=");
			subS2 = subS1.substring(idx2 + 1);
			subS1 = subS.substring(0, idx2);
			dimId = oMiniDictionary.indexOf(subS1);

			System.out.println("idx="+idx+", subS="+subS+", subS1="+subS1+", subS2="+subS2+",idx2="+idx2+",dimId="+dimId);
			System.out.println("cont.length="+aiContexts.length);

			//TODO: need to guard dimId against DIMENSION_MAX (eg. DIMENSION_MAX = 1 causes this to fail
			if(dimId != -1)
			{
				aiContexts[dimId] = (new Integer(subS2)).intValue();
			}
			else
			{
				System.err.println("LegacyInterpreter cannot resolve dimension symbol: " + subS1);
				bFini = true;    // if the demanded thing can't be resolved, no further process
			}

			oDimensionValue = new StringBuffer(oDimensionValue.substring(idx + 1));
		}
	
		if(!bFini)
		{
			try
			{
				oItem = (DictionaryItem)oDictionary.get(2);
				Debug.debug("LegacyInterpreter item: " + oItem);
				//int result = eval((SimpleNode)(oItem.getEntry()), aiContexts, 0);
				GIPSYType oReuslt = eval((SimpleNode)(oItem.getEntry()), new GIPSYContext[] {poDimensionTags}, 0);
				System.out.println("The result of calculation is: " + oReuslt);
				return oReuslt;
			}
			catch(Exception e)
			{
				System.err.println("LegacyInterpreter error calculating the result.");
				System.err.println(e.getMessage());
				e.printStackTrace(System.err);
				throw new GIPSYRuntimeException(e);
			}
		}
		
		throw new GIPSYRuntimeException();
	}
}

// EOF
