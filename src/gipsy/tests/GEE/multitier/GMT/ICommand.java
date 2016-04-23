package gipsy.tests.GEE.multitier.GMT;

import gipsy.GEE.multitier.MultiTierException;

/**
 *
 * @author jos_m
 *
 */
interface ICommand
{
    void execute() throws MultiTierException;

    String getResultStr();

    void setResultStr(String pstrResult);
}
