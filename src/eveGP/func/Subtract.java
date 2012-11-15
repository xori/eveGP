package eveGP.func;

import eveGP.internal.Tree;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class Subtract extends eveGP.GPfunction{

    /**
     * Subtracts children and returns the result.
     * @param children
     * @return 
     */
    @Override
    public float result(Tree... children) {
	return children[0].evaluate() - children[1].evaluate();
    }
    
    public String toString (Tree ... children){
        String o = "( SUB ";
        for (Tree t : children)
            o += t.toString()+ " ";
        o+= ")";
        return o;
    }
    
}
