package Assign2;

import static eveGP.internal.Parameter.*;
import eveGP.internal.Tree;
import static java.lang.Math.*;

/**
 *
 * @author ev09qz
 */
public class Regression extends eveGP.GPproblem {

    private float myFunction (int X) {
	return (float) (pow(X,3) + pow(X,2) + X + 3);
    }
    
    @Override
    public float evaluate(Tree tree) {
	float sum = 0;
	for (int x = 0; x < 15; x++) {
	    set("X", (float) x);
	    sum += abs(tree.evaluate() - myFunction(x));
	}
	return sum;
    }
}
