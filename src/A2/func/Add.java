package A2.func;

import eveGP.internal.Tree;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class Add extends eveGP.GPfunction{

    /**
     * Adds all children and returns the result.
     * @param children
     * @return 
     */
    @Override
    public float result(Tree... children) {
	float sum = 0;
        for (Tree c : children) {
	    sum += c.evaluate();
	}
	return sum;
    }
    
    public String toString (Tree ... children){
        String o = "( PLUS ";
        for (Tree t : children)
            o += t.toString()+ " ";
        o+= ")";
        return o;
    }
    
}
