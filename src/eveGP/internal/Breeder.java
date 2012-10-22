package eveGP.internal;

import eveGP.GPfunction;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static eveGP.internal.Parameter.*;
import java.util.Random;

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
        Tree output = new Tree();
        String f = getS("root.result");
        output.root = randomFunc(f);
        return output;
    }
    // root.result = "*" OR "Flt"
    //      "*" is default
    // ThreadPoolExecutor
    public void breeeed (ArrayList<Tree> t) {
        Random gen = (Random) Parameter.get("rGenerator");	
	int n2Select = Parameter.getI("selector.tourney");
	int popsize  = Parameter.getI("population");
	float mutation = Parameter.getF("mutation");
	float crossover= Parameter.getF("crossover");
	float r;
	ArrayList<Tree> generated = new ArrayList<Tree>(t.size());
	
	for (int i = 0; i < popsize; i++) {
	    r = gen.nextFloat();
	    if (r < crossover) {
		crossover(t.get(gen.nextInt(n2Select)),t.get())
	    } else if (r >= 1-mutation) {
		
	    } else {
		System.err.println("SELECTION ERROR:: Your mutation/crossover don't add up to 1");
	    }
	}
	gen.nextInt(n2Select);
    }   
    
    /**
     * //TODO randomfunc() :: This will need to have some higher chance of rolling terminals.
     * //TODO randomfunc() is hacky.
     * @param result
     * @return 
     */
    public Tree randomFunc (String result) {
        Tree parent = null;
        
        // Some good ol' unboxing.
        int fs = (int) (float) getF("functions.length");
        ArrayList<String> grabBag = new ArrayList<>();
        
        // Build a grab bag of all legal functions.
        for (int i = 0; i < fs; i++) {
            if (getS("functions."+i+".result").equals(result))
                grabBag.add("functions."+i);
            else if ("*".equals(result)) // Wildcard
                grabBag.add("functions."+i);
        }
        
        // Pick one function out of the grab bag and init();
        String func = grabBag.get(gen.nextInt(grabBag.size()));
        GPfunction output = null;
       
	output = (GPfunction) get(func);
        
        parent.function = output;        
        // and recurse to take care of *its* parameters.
        for (int i = 0; i < getI(func+".params.length"); i++) {
            parent.addChildren(randomFunc(output.parameterType.get(i)));
        }
        return parent;
    }
    // function evegp.add (Flt, Flt) : Flt
    // function evegp.lessThan (Flt, Flt) : Bool
    // 
    // functions.length = 2
    // functions.0 = 'evegp.add'
    // functions.1 = 'evegp.lessThan'
    // functions.0.result = 'Flt'
    // functions.0.params = ['Flt','Flt']
    // functions.0.params.size = 2
    // functions.1.result = 'Bool'
    // functions.1.params = ['Flt','Flt']
    // functions.1.params.size = 2
    
    public Tree[] crossover (Tree a, Tree b) {
	Tree A = (Tree) a.clone();
        GPfunction aParent, bParent;
        GPfunction aSwap, bSwap;
        int tries = getI("crossover.tries");
        int tempIndex;
        for (int i = 0; i < tries; i++) {
            aSwap = eenyMeeny(a);
            bSwap = eenyMeeny(b);
            if (aSwap.result == bSwap.result) {
                // Swap
                aParent = aSwap.parent;
                GPfunction temp = aSwap;
                if (aParent == null) {
                    // If root of tree;
                    a.root = bSwap;
                    a.root.parent = null;
                } else {
                    tempIndex = aParent.parameters.indexOf(aSwap);
                    aParent.parameters.remove(tempIndex);
                    aParent.parameters.add(tempIndex, bSwap);
                    bSwap.parent = aParent;
                }
                
                bParent = bSwap.parent;
                if (bParent == null) {
                    //if root of tree;
                    b.root = aSwap;
                    b.root.parent = null;
                } else {
                    tempIndex = bParent.parameters.indexOf(bSwap);
                    bParent.parameters.remove(tempIndex);
                    bParent.parameters.add(tempIndex, temp);
                    temp.parent = bParent;
                }
                return;
            }                
        }
    }
    
    public Tree mutate (Tree a) {
	Tree clone = (Tree) a.clone();
        Tree child = eenyMeeny(clone);
        int index;
	child = randomFunc(child.function.result);
	return child;
    }
    
    public int count (Tree a) {
        int c = 0;
        for(Tree p : a.children) {
            c += count(p);
        }
        a.depth = ++c;
        return c;        
    }
    
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
}
