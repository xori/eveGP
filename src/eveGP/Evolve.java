package eveGP;

import com.sun.xml.internal.ws.wsdl.writer.document.ParamType;
import eveGP.internal.Breeder;
import eveGP.internal.Parameter;
import static eveGP.internal.Parameter.*;
import eveGP.internal.Tree;
import java.util.ArrayList;
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
        generations = new ArrayList<Tree>();
        breeder = new Breeder();
        
	loadDefaults();
	loadFile(getS("param.file"));
	initalize();
	
        int popsize = getI("population");
        int gensize = getI("generations");
        Tree current;
	GPproblem problem;
	try {
	    problem = (GPproblem) ((Class) get("problem.class")).newInstance();
	} catch (Exception ex) {
	    System.exit(2);
	}
	
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
    
    public static void loadDefaults () {
	set("generations", 50);
	set("population" , 100);
	set("functions"	 , 0);
	set("mutation"	 , 0.1);
	set("crossover"  , 0.9);
	set("tourney"	 , 7);
	Random generator = new Random();
	int seed = generator.nextInt(1000000);
	set("seed"	 , seed);
	set("rGenerator" , generator);
	set("threads"	 , 1);
	set("problem"	 , "");
    }   
    public static void initalize () {
	Random g = (Random) get("rGenerator");
	g.setSeed(getI("seed"));
	if ("".equals(getS("problem"))) {
	    System.err.println("Problem not defined.");
	    System.exit(1);
	}
	String c = getS("problem");
	loadClass(c);
    }
    
    public static void main (String args[]) {
	if (args.length > 0)
	    set("param.file", args[0]);
	new Evolve();
    }
}
