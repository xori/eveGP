package A2.func;

import eveGP.internal.Tree;
import static eveGP.internal.Parameter.get;
import java.util.Random;


/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class ERC extends eveGP.GPfunction {

    private float variable = Float.NaN;
    
    public void init () {
        Random g = (Random) get("rGenerator");
        // rGenerator is the system random generator.
        this.variable = (g.nextInt(2) - 1) + g.nextFloat();
    }
    
    @Override
    public float result(Tree ... children) {
        if (Float.isNaN(variable)) {
            init();
        }
        return this.variable;
    }
    
    @Override
    public Object clone() {
        ERC node = null;
        try {
            node = (ERC) super.clone();
            if (Float.isNaN(variable));
                // Don't init on ourselves because there is one object at the
                // begining that we clone into every other ERC. 
                node.init();
        } catch (Exception e) { 
            System.err.println("Failure.");
        }
        return node;
    }
    
    @Override
    public String toString (Tree ... t) {
        return String.valueOf(variable);
    }
    
}
