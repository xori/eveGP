package Assign2;

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
    
}
