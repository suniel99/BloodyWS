package gipsy.lang.context;
import gipsy.lang.*;


public class OrderedInfiniteNonPeriodicTagSet extends TagSet implements IOrdered, IInfinite, INonPeriodic{
	
	public static final int UPPER_STEP=6;
	public static final int LOWER_STEP=8;
	public static final int INFINITY=9;

	public OrderedInfiniteNonPeriodicTagSet()
	{
		super();
	}
	
	public OrderedInfiniteNonPeriodicTagSet(int piExpressionType)
	{
		super.setExpressionType(piExpressionType);
	}
	
	
	//{...100 /2} or {2... /2} (The only argument GIPSYInteger is not sufficient to determine the expression type, additional expression type information is needed) this is not a good solution, maybe we can use the 0 argument constructor and set expression types later on.
	public OrderedInfiniteNonPeriodicTagSet(GIPSYType poUpperorLower, int piStep, int piTypeUpperorLower)
	{
		this(piTypeUpperorLower);
		switch (this.getExpressionType())
		{
		case UPPER_STEP:
		{
			this.setUpper(poUpperorLower);
			this.setStep(piStep);
			break;
		}
		case LOWER_STEP:
		{
			this.setLower(poUpperorLower);
			this.setStep(piStep);
			break;
		}
		default: //Other integers should be invalid.
		{
			System.err.println("Invalid tag set type!");
			break;
		}
		}
				
	}
	
	public boolean equals(Object poOtherObject)
	{
		boolean result=false;
		if(getClass() != poOtherObject.getClass())
		{
			return false;
		}
		else
		{
			//Types of expressions must be the same.
			if(this.getExpressionType()==((OrderedInfiniteNonPeriodicTagSet)poOtherObject).getExpressionType())
			{
				switch (this.getExpressionType())
				{
				case UPPER_STEP:
				{
					if(this.getUpper().equals(((OrderedInfiniteNonPeriodicTagSet)poOtherObject).getUpper()) && this.getStep()==((OrderedInfiniteNonPeriodicTagSet)poOtherObject).getStep())
					{
						result=true;
					}
					break;
				}
				case LOWER_STEP:
				{
					if(this.getLower().equals(((OrderedInfiniteNonPeriodicTagSet)poOtherObject).getLower()) && this.getStep()==((OrderedInfiniteNonPeriodicTagSet)poOtherObject).getStep())
					{
						result=true;
					}
					break;
				}
				case INFINITY:
				{
					result=true;
					break;
				}
				}
		     }
			
			return result;
		}
}
	public boolean isInTagSet(GIPSYType poTag)
	{
		boolean result=false;
		switch (this.getExpressionType())
		{
			
		case UPPER_STEP:
		{
			if(poTag.getClass()==this.getUpper().getClass())
			{
//				Only consider type of GIPSYInteger, since the < > operaters are not defined for other types.
				if(poTag instanceof GIPSYInteger)
				{
					//The given tag is greater than minus infinite and less than or equal to the upper boundary and can be divided by step, it is inside the tag set.
					if(((GIPSYInteger)poTag).getValue()>=Integer.MIN_VALUE && ((GIPSYInteger)poTag).getValue()<= ((GIPSYInteger)this.getUpper()).getValue() && ((GIPSYInteger)poTag).getValue()%this.getStep()==0)
					{
						result=true;
					}
					
				}
				
				/*****************************************************************************/
			    /*FUTURE POSSIBLE TYPES THAT HAS LESS THAN AND GREATER THAN FUNCTIONS DEFINED*/
				/*****************************************************************************/
			}
			break;
			
		}
			
		case LOWER_STEP:
		{
			if(poTag.getClass()==this.getLower().getClass())
			{
				//Only consider type of GIPSYInteger, since the < > operaters are not defined for other types.
				if(poTag instanceof GIPSYInteger)
				{
					//The given tag is less than plus infinite and greater than or equal to the lower boundary and can be devided by the step, it is inside the tag set.
					
					if(((GIPSYInteger)poTag).getValue()<=Integer.MAX_VALUE && ((GIPSYInteger)poTag).getValue()>= ((GIPSYInteger)this.getLower()).getValue()&& ((GIPSYInteger)poTag).getValue()%this.getStep()==0)
					{
						result=true;
					}		
					/*****************************************************************************/
				    /*FUTURE POSSIBLE TYPES THAT HAS LESS THAN AND GREATER THAN FUNCTIONS DEFINED*/
					/*****************************************************************************/
					
				}
			}
			break;
		}
		
		
		case INFINITY:
		{
			if(poTag instanceof GIPSYInteger )
			{
				//The given tag is less than plus infinite and greater than or equal to the lower boundary, it is inside the tag set.
				if(((GIPSYInteger)poTag).getValue()<=Integer.MAX_VALUE && ((GIPSYInteger)poTag).getValue()>=Integer.MIN_VALUE)
				{
					result=true;
				}
			}
			break;
		}
		}
		return result;
		
	}
	
	public GIPSYType getNext(GIPSYType poCurrentTag)
	{
		GIPSYType result=null;
		switch (this.getExpressionType())
		{
		case UPPER_STEP:
		{
			if(isInTagSet(poCurrentTag))
			{
				if(poCurrentTag instanceof GIPSYInteger)
				{	    
					if(!poCurrentTag.equals(this.getUpper()))
						result=new GIPSYInteger(((GIPSYInteger)poCurrentTag).getValue()+this.getStep());			
                            //?????????if it is the end of set, EXCEPTION?
			    					
			    }

				/*****************************************************************************/
			    /*FUTURE POSSIBLE TYPES THAT HAS PLUS FUNCTIONS DEFINED*/
				/*****************************************************************************/
			}

			break;
		}
		case LOWER_STEP:
		{
			if(isInTagSet(poCurrentTag))
			{
				if(poCurrentTag instanceof GIPSYInteger)
				{	
					if(!poCurrentTag.equals(Integer.MAX_VALUE))
					result=new GIPSYInteger(((GIPSYInteger)poCurrentTag).getValue()+this.getStep());
				}
				/*****************************************************************************/
			    /*FUTURE POSSIBLE TYPES THAT HAS PLUS FUNCTIONS DEFINED*/
				/*****************************************************************************/
			
			}
			
			break;
		}
		case INFINITY:
		{
			if(isInTagSet(poCurrentTag))
			{
				if(!poCurrentTag.equals(Integer.MAX_VALUE))         
				result=new GIPSYInteger (((GIPSYInteger)poCurrentTag).getValue()+1);
			}
			break;
		}
			
		
		}
		return result;
	}
	
}
