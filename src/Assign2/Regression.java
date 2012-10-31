package Assign2;

import eveGP.internal.Tree;
import static java.lang.Math.*;
import static eveGP.internal.Parameter.*;

/**
 *
 * @author ev09qz
 */
public class Regression extends eveGP.GPproblem {

    private float myFunction (int X) {
	return (float) (pow(X,3) + pow(X,2) + X);
    }
    
    @Override
    public float evaluate(Tree tree) {
	float sum = 0;
	for (int x = 0; x < 10; x++) {
	    set("X", (float) x);
	    sum += abs(tree.evaluate() - myFunction(x));
	}
	return sum;
    }
}
