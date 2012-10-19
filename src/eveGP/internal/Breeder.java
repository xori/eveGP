package eveGP.internal;

import eveGP.GPfunction;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static eveGP.internal.Parameter.*;

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
    
    public void breeeed (Tree ... t) {
        
    }   
    
    /**
     * //TODO randomfunc() :: This will need to have some higher chance of rolling terminals.
     * //TODO randomfunc() is kinda hacky.
     * @param result
     * @return 
     */
    public GPfunction randomFunc (String result) {
        GPfunction parent = null;
        
        // Some good ol' unboxing.
        int fs = (int) (float) getF("functions.length");
        ArrayList<String> grabBag = new ArrayList<String>();
        
        // Build a grab bag of all legal functions.
        for (int i = 0; i < fs; i++) {
            if (getS("functions."+i+".result") == result)
                grabBag.add("functions."+i);
            else if (result == "*") // Wildcard
                grabBag.add("functions."+i);
        }
        
        // Pick one function out of the grab bag and init();
        String func = grabBag.get(gen.nextInt(grabBag.size()));
        GPfunction output = null;
        try {
            output = (GPfunction) ((Class) get(func)).newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(Breeder.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Breeder.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        output.parent = parent;
        parent = output;        
        // and recurse to take care of *its* parameters.
        for (int i = 0; i < getI(func+".params.length"); i++) {
            output.addChildren(randomFunc(output.parameterType.get(i)));
        }
        return output;
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
    
    public void crossover (Tree a, Tree b) {
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
    
    public void mutate (Tree a) {
        GPfunction t = eenyMeeny(a);
        GPfunction parent = t.parent;
        int index;
        if (parent == null) {
            a.root = randomFunc(t.result);
        } else {
            index = parent.parameters.indexOf(t);
            parent.parameters.remove(index);
            parent.parameters.add(index, randomFunc(t.result));
        }
    }
    
    public int count (GPfunction a) {
        int c = 0;
        for(GPfunction p : a.parameters) {
            c += count(p);
        }
        a.depth = ++c;
        return c;        
    }
    
    public GPfunction eenyMeeny (Tree a) {
        int r; count(a.root);
        GPfunction node = a.root;
        int sum = 0;
        do {
            r = gen.nextInt(node.depth);
            if (r == node.depth - 1) return node;
            sum = 0;
            for(GPfunction f : node.parameters) {
                sum += f.depth;
                if (r < sum) { node = f; sum = 0; break;}
            }
        } while(sum == 0);
        return node;
    }
    
    
}
