package eveGP;

import eveGP.internal.Breeder;
import eveGP.internal.Parameter;
import static eveGP.internal.Parameter.*;
import eveGP.internal.Tree;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class Evolve {
    
    public static ArrayList<Tree> generations;
    public static Breeder breeder;
    
    public Evolve () {
        loadDefaults();
	loadFile(getS("param.file"));
	initalize();
	System.out.println(Parameter.dumpStats());
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Evolve.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        generations = new ArrayList<Tree>();
        breeder = new Breeder();
        
        int popsize = getI("population");
        int gensize = getI("generations");
        Tree current;
	GPproblem problem = null;
	try {
	    problem = (GPproblem) ((Class) get("problem.class")).newInstance();
	} catch (Exception ex) {
	    System.exit(2);
	}
	
        // Create initial generation
        for (int i = 0; i < popsize; i++) {
            generations.add(breeder.createTree());
        }
        
        //spit(generations);
        
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
            problem.stats(i, generations.toArray(new Tree[0]));
            
            if (generations.get(0).score == 0) {
                System.out.println("Optimal found");
                break;
            }

            if (i != gensize-1) // Don't breed on last run.
                // Will look to 'selector.tourney' to decide who to breed.
                breeder.breeeed(generations);
        }
        System.out.println("Finished");	
    }
    
    private void spit (Collection<Tree> l) {
        for (Tree i : l) {
            System.out.println(i.toString());
        }
    }
    
    public static void loadDefaults () {
	set("generations", 50);
	set("population" , 100);
	set("functions"	 , 0);
	set("mutation"	 , 0.1f);
	set("crossover"  , 0.9f);
	set("tourney"	 , 7);
	Random generator = new Random();
	int seed = generator.nextInt(1000000);
	set("seed"	 , seed);
	set("rGenerator" , generator);
	set("threads"	 , 1);
	set("problem"	 , "");
        set("root.result", "*");
    }   
    public static void initalize () {
	Random g = (Random) get("rGenerator");
	g.setSeed(getI("seed"));
	if ("".equals(getS("problem"))) {
	    System.err.println("Problem not defined.");
	    System.exit(1);
	}
	String c = getS("problem");
	loadClass(c,eveGP.GPproblem.class, "problem.class", false);
    }
    
    public static void main (String args[]) {
	if (args.length > 0)
	    set("param.file", args[0]);
        set("param.file", "src/Assign2/test.params");        
	new Evolve();
    }
}
