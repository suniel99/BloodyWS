package gipsy.GEE;

/**
 * GEE Default Configuration Parameters.
 * [SAM]: I guess we oughtta move that to GIPSY.java or same level
 * @author Paula Bo Lu
 * @author Serguei Mokhov
 */
public interface CONFIG
{
	int DIMENSION_MAX = 10;
	int DIMENSION_LEN = 3;
	int DEMANDS_MAX   = 1000;

/*
[SAM]:
	That was in CONFIG in the threaded version.
	Just for reference for the time being...
	I updated the threaded part to use the above
	instead.

	public static final int NumDemands = 1000 ;
	public static final int NumDimensions = 1;
*/

/*
[SAM]:
	That was in GIPC.Config. Since it dupes this,
	get rid of Config.java in GIPC.

	final static int DIMENSION_MAX = 10;
	final static int DIMENSION_LEN = 3;
*/
}

// EOF
