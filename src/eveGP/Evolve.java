package eveGP;

import eveGP.internal.Breeder;
import eveGP.internal.Tree;
import java.util.ArrayList;
import static eveGP.internal.Parameter.*;
import java.util.Collections;

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
	
        // Create initial generation
        for (int i = 0; i < popsize; i++) {
            generations.add(breeder.createTree());
        }
        
        // Begin training.
        System.out.println("Begin");
        for (int i = 0; i < gensize; i++) {
            set("generation", i);
            System.out.println("Generation: " + i );
            
            for (int j = 0; j < popsize; j++) {
                current = generations.get(j);
                current.score = problem.evaluate(current);
            }
	    // Sort. 0 is best. eg) 0, 1, -2, 3, 3...
	    Collections.sort(generations);
            // Statistics.RunTesting(generations);

            if (i != gensize-1) // Don't breed on last run.
                // Will look to 'selector.tourney' to decide who to breed.
                breeder.breeeed(generations);
        }
        System.out.println("Finished");
        // Statistics.Best(generations);	
    }
    
    public static void main (String args[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
	Class<?> func = Class.forName("Assign2.Brightness");
        System.out.println(((GPfunction)func.newInstance()).toString((Tree) null));
    }
}
