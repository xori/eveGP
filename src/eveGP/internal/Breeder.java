package eveGP.internal;

import java.util.List;
import eveGP.GPfunction;
import java.util.ArrayList;
import static eveGP.internal.Parameter.*;
import static java.lang.Math.*;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class Breeder {
    
    private java.util.Random gen;
    
    
    public Breeder () {
        gen = (java.util.Random) Parameter.get("rGenerator");
    }
    
    public Tree createTree () {
        Tree output;
        String f = getS("root.result");
        output = randomFunc(f);
        return output;
    }
    // root.result = "*" OR "Flt"
    //      "*" is default
    // ThreadPoolExecutor
    public void breeeed (List<Tree> t) {
	int n2Select = Parameter.getI("tourney");
	int popsize  = Parameter.getI("population");
	float mutation = Parameter.getF("mutation");
	float crossover= Parameter.getF("crossover");
	float r;
        int r1, r2;
	ArrayList<Tree> generated = new ArrayList<Tree>(t.size());
        Tree current, crossNode;
	
	for (int i = 0; i < popsize; i++) {
	    r = gen.nextFloat();
            r1 = gen.nextInt(min(t.size(),n2Select));
            current = (Tree) t.get(r1).clone();
            
	    if (r < crossover) {
                //do {
                r2 = gen.nextInt(min(t.size(),n2Select));
                //} while( r1 == r2);
                crossNode = (Tree) t.get(r2).clone();
                // l("Chosen crossover!", current, crossNode, r1, r2);
                
                crossover(current, crossNode);
                
                generated.add(current);
                generated.add(crossNode);
                // l("\tGot this:", current, crossNode);
                
                i++; // because we made two
	    } else if (r >= 1-mutation) {
		mutate(current);
                generated.add(current);
	    } else {
		System.err.println("SELECTION ERROR:: Your mutation/crossover don't add up to 1");
	    }
	}
	t.clear();
        if (generated.size() > popsize)
            while(generated.size() > popsize)
                generated.remove(0);
        t.addAll(generated);
    }   
    
    /**
     * //TODO randomfunc() is hacky.
     * @param result
     * @return 
     */
    public Tree randomFunc (String result) {
        return randomFunc(result,0);
    }
    
    public Tree randomFunc (String result, int round) {
        Tree current = null;
        GPfunction instance = null;
        
        // Some good ol' unboxing.
        int fs = (int) (float) getI("functions");
        ArrayList<String> grabBag = new ArrayList<String>();
        ArrayList<String> terminalBag = new ArrayList<String>();
        
        // Build a grab bag of all legal functions.
        for (int i = 0; i < fs; i++) {
            if (getS("functions."+i+".result").equals(result)) {
                if (round > 5 && getI("functions."+i+".params.size")==0)
                    terminalBag.add("functions."+i);
                grabBag.add("functions."+i);
            } else if ("*".equals(result)) { // Wildcard
                if (round > 5 && getI("functions."+i+".params.size")==0)
                    terminalBag.add("functions."+i);
                grabBag.add("functions."+i);
            }
        }
        
        // Pick one function out of the grab bag and init();
        String func = grabBag.get(gen.nextInt(grabBag.size()));
       
        if (round > 5 && terminalBag.size() > 0) {
            func = terminalBag.get(gen.nextInt(terminalBag.size()));
        }
        
        instance = (GPfunction) ((GPfunction) get(func)).clone();
	current = new Tree(instance);
        
        // and recurse to take care of *its* parameters.
        for (int i = 0; i < getI(func+".params.size"); i++) {
            current.addChildren(randomFunc(current.function.parameterType.get(i), ++round));
        }
        return current;
    }
    
    /**
     * Attempts a crossover between the two trees given. Remember to clone BEFORE
     * passing it to this function. This will(!) modify the reference!!1!!!
     * @param a
     * @param b
     * @return true on success.
     */
    public boolean crossover (Tree a, Tree b) {
	Tree tA, tB;
        GPfunction tF;
        ArrayList<Tree> tC;
        
        tA = eenyMeeny(a);
        tB = eenyMeeny(b);
        if (!tA.function.result.equals(tB.function.result))
            return false;
        
        // swap functions
        tF = tA.function;
        tA.function = tB.function;
        tB.function = tF;
        
        // swap children
        tC = tA.children;
        tA.children = tB.children;
        tB.children = tC;
        
        return true;
    }
    
    /**
     * Alters the given Tree by replacing a random node. Remember to clone
     *  **BEFORE** passing it to this function.
     * @param a Tree to mutate.
     * @return true if successful.
     */
    public boolean mutate (Tree a) {
        Tree tA = eenyMeeny(a);
        Tree random = randomFunc(tA.function.result);
        tA.function = random.function;
        tA.children = random.children;
        return true;
    }
    
    /**
     * Sets the X.depth variable in every node recursivly for the given tree.
     * @param a
     * @return The number of nodes in this tree.
     */
    public int count (Tree a) {
        int c = 0;
        for(Tree p : a.children) {
            c += count(p);
        }
        a.depth = ++c;
        return c;        
    }
    
    /**
     * Return a random node in the passed in Tree.
     * @param a The tree to pick from
     * @return the node picked.
     */
    public Tree eenyMeeny (Tree a) {
        int r; count(a);
        Tree node = a;
        int sum;
        do {
            r = gen.nextInt(node.depth);
            if (r == node.depth - 1) return node;
            sum = 0;
            for(Tree f : node.children) {
                sum += f.depth;
                if (r < sum) { 
		    node = f; sum = 0; break;
		}
            }
        } while(sum == 0);
        return node;
    }
    
    
    private void l(Object ... o) {
        for (Object i : o)
            System.out.print(i.toString() + ", ");
        System.out.println();
    }
}
