package gipsy.GEE.IDP.DemandGenerator;

import gipsy.GEE.GEEException;
import gipsy.GEE.IDP.demands.DemandSignature;
import gipsy.GEE.IDP.demands.IDemand;
import gipsy.GEE.IDP.demands.IntensionalDemand;
import gipsy.GEE.IVW.Warehouse.IVWInterface;
import gipsy.GIPC.intensional.SimpleNode;
import gipsy.GIPC.intensional.SIPL.IndexicalLucid.IndexicalLucidParserTreeConstants;
import gipsy.lang.GIPSYInteger;
import gipsy.lang.GIPSYType;
import gipsy.lang.context.Dimension;
import gipsy.storage.DictionaryItem;

import java.io.Serializable;
import java.util.Vector;

import marf.util.Arrays;


/**
 * <p>Legacy eductive interpreter.</p>
 * 
 * @author Serguei Mokhov
 * @version $Id: LegacyEductiveInterpreter.java,v 1.10 2013/08/25 03:07:06 mokhov Exp $
 * @since December 6, 2010
 */
public class LegacyEductiveInterpreter
extends LegacyInterpreter
{
	/**
	 * Assumes all the values at their defaults.
	 */
	public LegacyEductiveInterpreter()
	{
		super();
	}
	
    /**
     * Allows setting a different than the default data warehouse.
     * @param poValueHouse the new data warehouse to set.
     * @see #oValueHouse
     */
    public LegacyEductiveInterpreter(IVWInterface poValueHouse)
    {
        super(poValueHouse);
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
		/*
		 * normal processing
		 */
		System.out.println
		(
			"LegacyEductiveInterpreter: eval("
			+ poExpressionRoot + ","
			+ ")"
		);
		
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
			// XXX: WHERE kludge
		/*
			case JJTSTART:
			case JJTWHERE:
			{
				System.out.println
				(
					"KLUDGE Note: ignoring a node: " + poExpressionRoot.id
					+ " of " + poExpressionRoot
				);
				
				return eval((SimpleNode)poExpressionRoot.children[0], paiContext, piIndent + 1);
			}
		*/
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
			 * The signature of the vhouse is a string combined with the id and its context.
			 * Enquire the id+context in the value house
			 *	if it exists, return the value
			 *	if it doesn't, evaluate the id+context
			 */
			case JJTID:
			{
				String strID = (new Integer(poExpressionRoot.ID)).toString();
				System.out.println("strID: " + strID + ", .ID: " + poExpressionRoot.ID);

				try
				{
					System.out.println("oDictionary: " + oDictionary);
					DictionaryItem oIDEntry = (DictionaryItem)this.oDictionary.get(poExpressionRoot.ID);
					System.out.println("oIDEntry: " + oIDEntry);
					
					StringBuffer oIDSignature = new StringBuffer(strID);

					for(int i = 0; i < DIMENSION_MAX; i++)
					{
						oIDSignature = oIDSignature.append(",");
						Integer iTmpContextItem = new Integer(paiContext[i]);
						oIDSignature = oIDSignature.append(iTmpContextItem.toString());
					}

					System.out.println("oIDSignature: " + oIDSignature);
					
					// At first we always dispatch to see if the store has it cached
					IntensionalDemand oDemand = new IntensionalDemand();
					DemandSignature oSignature = new DemandSignature(oIDSignature.toString());

					System.out.println("oDemand: " + oDemand);
					System.out.println("oSignature: " + oSignature);
					
					// Keep a local pending copy
					this.oPendingDemands.put(oSignature, oDemand);
					System.out.println("oPendingDemands: " + oPendingDemands);

					// Demand
					//IDemand oResult = this.oDispatcher.readDemandIfExists();
					IDemand oResult = this.oDispatcher.readResultIfExists(oSignature);
					System.out.println("oResult: " + oResult);

					// Not in the DS
					if(oResult == null)
					{
						//int iResultEval = eval((SimpleNode)(oIDEntry.getEntry()), paiContext, piIndent + 1);
						int iResultEval = 12345;
						System.out.println("iResultEval: " + iResultEval);

						DemandSignature oStoredSignature = oDemand.storeResult(iResultEval);
						System.out.println("oStoredSignature: " + oStoredSignature);

						// write() is non-blocking
						oSignature = this.oDispatcher.writeDemand(oDemand);
						System.out.println("oSignature: " + oSignature);
						
						return iResultEval;
					}
					else
					{
						Serializable oSerializableResult = oResult.getResult();
						System.out.println("oSerializableResult: " + oSerializableResult);
						
						if(oSerializableResult instanceof GIPSYType)
						{
							if(oSerializableResult instanceof GIPSYInteger)
							{
								return ((GIPSYInteger)oSerializableResult).getValue().intValue(); 
							}
							else
							{
								throw new GEEException("Can't handle presently non-Integer result: " + oResult);
							}
						}
						else
						{
							throw new GEEException("Non GIPSY-typed result: " + oResult);
						}
					}
				}
				catch(ArrayIndexOutOfBoundsException e)  // in case the id is null
				{
					//System.err.println("Executor: couldn't resolve the identifier symbol: " + s);
					throw new GEEException("LegacyEductiveInterpreter: couldn't resolve the identifier symbol: " + strID);
				}
			}

			// the value of a constant is its image
			case JJTCONST:
			case IndexicalLucidParserTreeConstants.JJTCONST:
			{
				System.out.println("CONST: " + poExpressionRoot.getImage());
				return (new Integer(poExpressionRoot.getImage())).intValue();
				//return (new Number(expr.getImage())).intValue();
			}

			default:
			{
				throw new GEEException
				(
					"LegacyEductiveInterpreter: bad node type: " + poExpressionRoot.id
					+ " of node " + poExpressionRoot
				);
			}
		}
	}

	@Override
	public GIPSYType eval()
	throws GEEException
	{
		Vector<GIPSYType> oDimensions = this.oGEER.getContextValue().getDemensions();
		Vector<GIPSYType> oTags = this.oGEER.getContextValue().getTags();
		
		int iTagIndex = 0;
		
		String strContext = "";
		
		for(GIPSYType oDimension: oDimensions)
		{
			if(iTagIndex > 0)
			{
				strContext += ",";
			}
			
			strContext += ((Dimension)oDimension).getDimensionName().getValue() + "=" + oTags.get(iTagIndex).getEnclosedTypeOject();
			iTagIndex++;
		}
		
		System.out.println("Older style context: [" + strContext + "]");
		this.oGEER.setContext(strContext);
		//this.oGEER.setContext(poContext);
		
		return new GIPSYInteger(eval((SimpleNode)this.oGEER.getAbstractSyntaxTrees()[0].getRoot(), new int[] {1}, 0));
	}

	@Override
	public GIPSYType execute()
	{
		Vector<GIPSYType> oDimensions = this.oGEER.getContextValue().getDemensions();
		Vector<GIPSYType> oTags = this.oGEER.getContextValue().getTags();
		
		int iTagIndex = 0;
		
		String strContext = "";
		
		for(GIPSYType oDimension: oDimensions)
		{
			if(iTagIndex > 0)
			{
				strContext += ",";
			}
			
			strContext += ((Dimension)oDimension).getDimensionName().getValue() + "=" + oTags.get(iTagIndex).getEnclosedTypeOject();
			iTagIndex++;
		}
		
		System.out.println("LegacyEductiveInterpreter:execute(): Older style context: [" + strContext + "]");
		this.oGEER.setContext(strContext);
		
		return new GIPSYInteger(execute(this.oGEER.getDictionary(), strContext));
	}
}

// EOF
