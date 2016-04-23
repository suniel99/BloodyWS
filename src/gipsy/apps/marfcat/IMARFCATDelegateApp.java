package gipsy.apps.marfcat;

import gipsy.GEE.multitier.MultiTierException;

/**
 * Delegation API for different implementations.
 * @author Serguei Mokhov
 */
public interface IMARFCATDelegateApp
{
	void execute() throws MultiTierException;
}

// EOF
