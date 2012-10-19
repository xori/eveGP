package eveGP;

import eveGP.internal.Breeder;
import eveGP.internal.Tree;
import java.util.ArrayList;
import static eveGP.internal.Parameter.*;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class Evolve {
    
    public static ArrayList<Tree> generations;
    public static Breeder breeder;
    
    public Evolve () {
        generations = new ArrayList<Tree>();
        breeder = new Breeder();
        
        int popsize = getI("population");
        int gensize = getI("generations");
        Tree current;
	GPproblem problem = null;
	
        
        for (int i = 0; i < popsize; i++) {
            generations.add(breeder.createTree());
        }
        
        System.out.println("Begin");
        
        for (int i = 0; i < gensize; i++) {
            set("generation", i);
	    current = generations.get(i);
	    current.score = problem.evaluate(current);
	    // Sort. 0 is first. eg) 0, 1, -2, 3, 3...
	    Collections.sort(generations);
	    selector.
        }
	
    }
    
    public static void main (String args[]) {
	
	
	System.out.println("hello world.");
	
    }
}
