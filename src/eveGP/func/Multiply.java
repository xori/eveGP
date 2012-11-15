package eveGP.func;

import eveGP.internal.Tree;

/**
 *
 * @author ev09qz
 */
public class Multiply extends eveGP.GPfunction {

    @Override
    public float result(Tree... children) {
	float sum = 1;
	for (Tree c : children) {
	    sum *= c.evaluate();
	}
	return sum;
    }
    
    public String toString (Tree ... children){
        String o = "( MULTIPLY ";
        for (Tree t : children)
            o += t.toString()+ " ";
        o+= ")";
        return o;
    }
}
