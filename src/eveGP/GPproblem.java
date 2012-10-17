package eveGP;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public abstract class GPproblem {
    
    /**
     * Run once before the first generation.
     */
    public void setup () {
	
    }
    
    /**
     *	This is your fitness function. It should return a standardized fitness 
     *  score (0 is best). You can get the tree's result with tree.value();
     * @return Standardized Fitness Value (0 is best)
     */
    public abstract float evaluate (/* GPtree tree */);
    
    /**
     * This function is run once every generation.
     */
    public void test (/* GPtree tree */) { }
    
    /**
     * This function is run once per run on the best tree.
     */
    public void best (/* GPtree tree */) { }
}
