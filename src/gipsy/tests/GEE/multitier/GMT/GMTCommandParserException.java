package gipsy.tests.GEE.multitier.GMT;

/**
 * Exception thrown by the GMTCommandParser.
 *
 * TODO : might be useful to store the ICommand in the GMTCommandParserException Object.
 *
 * @author jos_m
 */
public class GMTCommandParserException extends Exception {

    private static final long serialVersionUID = 0L;
    
    public GMTCommandParserException(String msg) {
        super(msg);
    }

}
