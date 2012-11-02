package Assign2;

import eveGP.internal.Tree;
import static eveGP.internal.Parameter.get;
import java.util.Random;


/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class ERC extends eveGP.GPfunction {

    private float variable = Float.NaN;
    
    private void init () {
        Random g = (Random) get("rGenerator");
        // rGenerator is the system random generator.
        variable = (g.nextInt(10) - 5) + g.nextFloat();
        // sets this node to a floating value between -5.0 and 5.0
    }
    
    @Override
    public float result(Tree ... children) {
        if (variable == Float.NaN) {
            init();
        }
        return variable;
    }
    
    public Object clone() {
        ERC node = null;
        try {
            node = (ERC) super.clone();
            if (this.variable == Float.NaN)
                // Don't init on ourselves because there is one object at the
                // begining that we clone into every other ERC. 
                node.init();
        } catch (Exception e) { 
            System.err.println("Failure.");
        }
        return node;
    }
    
    public String toString (Tree ... t) {
        return String.valueOf(variable);
    }
    
}
