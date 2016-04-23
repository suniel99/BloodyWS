/**
 * Introduce this visitor interface as in the Visitor design pattern, to visit the SimpleNode objects.
 */
package gipsy.GIPC.intensional.SIPL.Lucx;
import gipsy.GIPC.intensional.SimpleNode; //using this SimpleNode rather than the generated one

public interface LucxNodeVisitor {
	public void visit(SimpleNode pNode);

}
