package gipsy.GEE.IDP.DemandGenerator;

import gipsy.GEE.CONFIG;
import gipsy.GEE.GEEException;
import gipsy.GEE.IVW.Warehouse.IVWControl;
import gipsy.GEE.IVW.Warehouse.IVWInterface;
import gipsy.GIPC.intensional.SimpleNode;
import gipsy.GIPC.intensional.GIPL.GIPLParserTreeConstants;
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
 * <p>GEE Interpreter-based "Demand Generator".</p>
 * 
 * <p>Input: the dictionary from a GIPSYProgram (GEER).</p>
 * 
 * @author Serguei Mokhov
 * 
 * @version $Id: Interpreter.java,v 1.3 2010/12/06 13:38:38 mokhov Exp $
 * @since
 * 
 * @see GIPSYProgram
 */
public class Interpreter
extends DemandGenerator
implements CONFIG, GIPLParserTreeConstants
{
	/**
	 * Main dictionary.
	 */
	private Dictionary oDictionary = new Dictionary();

	/**
	 * Dictionary with only idName and its number.
	 */
	private Dictionary oMiniDictionary = new Dictionary(); 

	/**
	 * Warehouse reference.
	 * Default is IVWControl.
	 * @see IVWControl
	 */
	protected IVWInterface oValueHouse = new IVWControl();


	/**
	 * Assumes all the values at their defaults.
	 */
	public Interpreter()
	{
		
	}
	
    /**
     * Allows setting a different than the default data warehouse.
     * @param poValueHouse the new data warehouse to set.
     * @see #oValueHouse
     */
    public Interpreter(IVWInterface poValueHouse)
    {
        this.oValueHouse = poValueHouse;
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
            //System.err.println("Executor: couldn't resolve the identifier symbol: " + s);
            throw new GEEException("Executor: couldn't resolve the identifier symbol: " + s);
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
         throw new GEEException("Executor: bad node type: " + poRoot.id);
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
				System.err.println("Executor: cannot resolve dimension symbol: " + subS1);
				bFini = true;    // if the demanded thing can't be resolved, no further process
			}

			oDimensionValue = new StringBuffer(oDimensionValue.substring(idx + 1));
		}
	
		if(!bFini)
		{
			try
			{
				oItem = (DictionaryItem)oDictionary.get(2);
				Debug.debug("Executor: item: " + oItem);
				//int result = eval((SimpleNode)(oItem.getEntry()), aiContexts, 0);
				GIPSYType oReuslt = eval((SimpleNode)(oItem.getEntry()), new GIPSYContext[] {poDimensionTags}, 0);
				System.out.println("The result of calculation is: " + oReuslt);
				return oReuslt;
			}
			catch(Exception e)
			{
				System.err.println("Executor: error calculating the result.");
				System.err.println(e.getMessage());
				e.printStackTrace(System.err);
				throw new GIPSYRuntimeException(e);
			}
		}
		
		throw new GIPSYRuntimeException();
	}
}

// EOF
