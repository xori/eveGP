package example;

import eveGP.internal.Tree;
import static java.lang.Math.*;

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
	for (int x = 0; x < 100; x++) {
	    setVariable("X", x);
	    sum += abs(tree.evaluate() - myFunction(x));
	}
	return sum;
    }
}
