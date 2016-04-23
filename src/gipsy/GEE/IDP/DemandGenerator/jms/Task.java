package gipsy.GEE.IDP.DemandGenerator.jms;
//**** Calculation of Pi by Machin method
//**** Originated by M. Gallant 10/16/97
//**** Modified by Emil Vassev

import java.math.BigDecimal;
import java.io.Serializable;

public class Task implements Serializable
{
	static final long serialVersionUID =10;
	public BigDecimal execute(int iNumOfDigits)
	{
		BigDecimal one, sixteen, four, oneby5, oneby239, atn15, atn239, xsq, powercum, bigpi;
		int tscale = iNumOfDigits;

		one = new BigDecimal("1") ;
		four = new BigDecimal("4") ;
		sixteen = new BigDecimal("16") ;
		oneby5 = new BigDecimal("0.2") ;
		oneby239 = one.divide(new BigDecimal("239"), tscale, BigDecimal.ROUND_DOWN) ;

//**** Calculate arctan(1/5) to tscale digits of precision:
//**** based on log10(1/5) need about 1.5*tscale maximum exponent.
		atn15 = oneby5 ;        // initialize to first term of arctan series.
		powercum = oneby5;      // start with first power.
		xsq = oneby5.multiply(oneby5) ;

//**** Starting Calculation with tscale terms

		for (int i=3; i<=(3*tscale/2); i+=4) 
		{ 
			powercum = powercum.multiply(xsq) ;
			atn15 = atn15.subtract(powercum.divide(new BigDecimal(String.valueOf(i)), tscale, BigDecimal.ROUND_DOWN)); 
			powercum = powercum.multiply(xsq) ;
			atn15 = atn15.add(powercum.divide(new BigDecimal(String.valueOf(i+2)), tscale, BigDecimal.ROUND_DOWN)); 
			atn15 = atn15.setScale(tscale, BigDecimal.ROUND_DOWN) ;
		} 
 
//**** Calculate arctan(1/239) to tscale digits of precision:
//**** based on log10(1/239) need about 0.5*tscale maximum exponent.
		atn239 = oneby239 ;      // initialize to first term of arctan series.
		powercum = oneby239;     // start with first power.
		xsq = oneby239.multiply(oneby239) ;

		for (int i=3; i<=tscale/2; i+=4) 
		{
			powercum = (powercum.multiply(xsq)).setScale(tscale, BigDecimal.ROUND_DOWN) ;
			atn239 = atn239.subtract(powercum.divide(new BigDecimal(String.valueOf(i)), BigDecimal.ROUND_DOWN)); 
			powercum = (powercum.multiply(xsq)).setScale(tscale, BigDecimal.ROUND_DOWN) ;
			atn239 = atn239.add(powercum.divide(new BigDecimal(String.valueOf(i+2)),  BigDecimal.ROUND_DOWN)); 
			atn239 = atn239.setScale(tscale, BigDecimal.ROUND_DOWN) ;
		} 

		bigpi = sixteen.multiply(atn15) ;
		bigpi = bigpi.subtract(four.multiply(atn239)) ;

		return bigpi;
	}
}