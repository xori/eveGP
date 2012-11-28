package eveGP;

import eveGP.internal.Breeder;
import eveGP.internal.Parameter;
import eveGP.internal.ThreadManager;
import static eveGP.internal.Parameter.*;
import eveGP.internal.Tree;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class Evolve {
    
    public static ArrayList<Tree> generations;
    public static Breeder breeder;
    public static Tree best;
    
    public Evolve (String [] params) {
        loadDefaults();
	loadFile(getS("param.file"));
        loadArguments(params);
        initalize();
	System.err.println(Parameter.dumpStats());
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Evolve.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        generations = new ArrayList<Tree>();
        best = new Tree(null);
        best.score = Float.POSITIVE_INFINITY;
        breeder = new Breeder();
        
        int popsize = getI("population");
        int gensize = getI("generations");
        int threads = getI("threads");
        long current;
	GPproblem problem = null;
        ThreadManager manager;
        
	try {
            problem = (GPproblem) ((Class) get("problem.class")).newInstance();
            problem.setInfo(0);
	} catch (Exception ex) {
	    System.exit(2);
	}
        problem.setup();
	
        // Create initial generation
        for (int i = 0; i < popsize; i++) {
            generations.add(breeder.createTree());
        }
        
        //spit(generations);
                
        // Begin training.
        System.err.println("Begin");
        for (int i = 0; i < gensize; i++) {
            set("generation", i);
            System.err.println("Generation: " + i );
            
            current = System.currentTimeMillis();
            manager = new ThreadManager(threads, problem.getClass());
            manager.execute(generations.toArray(new Tree[0]));
            try {
                manager.executor.shutdown();
                manager.executor.awaitTermination(1, TimeUnit.HOURS);
            } catch (InterruptedException ex) {
                Logger.getLogger(Evolve.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Sort. 0 is best. eg) 0, 1, -2, 3, 3...
	    Collections.sort(generations);
            if (best.score > generations.get(0).score)
                best = (Tree) generations.get(0).clone();
                       
            if ( getI("elitism") == 1 )
                generations.add(best);
            problem.stats(i, generations.toArray(new Tree[0]));
            problem.cleanUpMemory();
            
            if (generations.get(0).score == 0) {
                System.err.println("Optimal found");
                break;
            }

            if (i != gensize-1) // Don't breed on last run.
                // Will look to 'selector.tourney' to decide who to breed.
                breeder.breeeed(generations);
            //System.gc();
        }
        System.err.println("Finished");	
        problem.best(best);
        System.exit(0);
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
	set("tourney"	 , 3);
	Random generator = new Random();
	int seed = generator.nextInt(1000000);
	set("seed"	 , seed);
	set("rGenerator" , generator);
	set("threads"	 , 1);
	set("problem"	 , "");
        set("root.result", "*");
        set("elitism"    , 0);
        set("stats"      , "stats");
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
        try {
            new File(getS("stats")).delete();
        } catch (Exception ignore) { }
    }
    
    public static void main (String args[]) {
        //set("param.file", "src/A2/test.params");
	if (args.length > 0)
	    set("param.file", args[0]);
	new Evolve(args);
    }
}
