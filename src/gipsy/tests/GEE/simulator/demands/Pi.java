package gipsy.tests.GEE.simulator.demands;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Calculation of Pi by Machin method
 * originated by M. Gallant 10/16/97 and
 * modified by Emil Vassev.
 * 
 * @author Emil Vassev
 * @since
 * @version $Id: Pi.java,v 1.7 2009/09/29 19:10:34 mokhov Exp $
 */
public class Pi
implements Serializable
{
	/*
	 * Constants
	 */
	
	/**
	 * XXX: fix.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * XXX.
	 * @param piNumOfDigits XXX
	 * @return XXX
	 */
	public BigDecimal calculatePi(int piNumOfDigits)
	{
		BigDecimal oOne, oSixteen, oFour, oOneby5, oOneby239, oAtn15, oAtn239, oXsq, oPowerAccum, oBigPi;
		
		int iTScale = piNumOfDigits;

		oOne = new BigDecimal("1") ;
		oFour = new BigDecimal("4") ;
		oSixteen = new BigDecimal("16") ;
		oOneby5 = new BigDecimal("0.2") ;
		oOneby239 = oOne.divide(new BigDecimal("239"), iTScale, BigDecimal.ROUND_DOWN) ;

//**** Calculate arctan(1/5) to tscale digits of precision:
//**** based on log10(1/5) need about 1.5*tscale maximum exponent.
		oAtn15 = oOneby5;        // initialize to first term of arctan series.
		oPowerAccum = oOneby5;      // start with first power.
		oXsq = oOneby5.multiply(oOneby5);

//**** Starting Calculation with tscale terms

		for(int i = 3; i <= (3 * iTScale / 2); i += 4) 
		{ 
			oPowerAccum = oPowerAccum.multiply(oXsq) ;
			oAtn15 = oAtn15.subtract(oPowerAccum.divide(new BigDecimal(String.valueOf(i)), iTScale, BigDecimal.ROUND_DOWN)); 
			oPowerAccum = oPowerAccum.multiply(oXsq) ;
			oAtn15 = oAtn15.add(oPowerAccum.divide(new BigDecimal(String.valueOf(i+2)), iTScale, BigDecimal.ROUND_DOWN)); 
			oAtn15 = oAtn15.setScale(iTScale, BigDecimal.ROUND_DOWN) ;
		} 
 
//**** Calculate arctan(1/239) to tscale digits of precision:
//**** based on log10(1/239) need about 0.5*tscale maximum exponent.
		oAtn239 = oOneby239 ;      // initialize to first term of arctan series.
		oPowerAccum = oOneby239;     // start with first power.
		oXsq = oOneby239.multiply(oOneby239) ;

		for(int i = 3; i <= iTScale / 2; i += 4) 
		{
			oPowerAccum = (oPowerAccum.multiply(oXsq)).setScale(iTScale, BigDecimal.ROUND_DOWN) ;
			oAtn239 = oAtn239.subtract(oPowerAccum.divide(new BigDecimal(String.valueOf(i)), BigDecimal.ROUND_DOWN)); 
			oPowerAccum = (oPowerAccum.multiply(oXsq)).setScale(iTScale, BigDecimal.ROUND_DOWN) ;
			oAtn239 = oAtn239.add(oPowerAccum.divide(new BigDecimal(String.valueOf(i+2)),  BigDecimal.ROUND_DOWN)); 
			oAtn239 = oAtn239.setScale(iTScale, BigDecimal.ROUND_DOWN) ;
		} 

		oBigPi = oSixteen.multiply(oAtn15) ;
		oBigPi = oBigPi.subtract(oFour.multiply(oAtn239)) ;

		return oBigPi;
	}
}

// EOF
