package eveGP;

import static eveGP.internal.Parameter.*;
import eveGP.internal.Tree;
/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public abstract class GPproblem {
    
    private int thread;
    
    public GPproblem () {}
    
    public GPproblem (int thread) {
        this.thread = thread;
    }
    public void setVariable(String s, Object o) {
        //TODO This is done so we can have multi-threading.
        set("Problem."+thread+"."+s, o);
    }
    
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
    public abstract float evaluate ( Tree tree );
	/*	
	setVariable("X", 0); // Problem specific variable.
		// set("Problem."+currentThread()+".X", value);
	*/
    
    /**
     * This function is run once every generation.
     */
    public void stats ( int generation, Tree ... trees ) {
        for (int i = 0; i < 1; i++) {
            System.out.println(trees[i].score + " >> " + trees[i].toString());
        }
        
    }
}
