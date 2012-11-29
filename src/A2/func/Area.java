package A2.func;

import eveGP.internal.Tree;
import static eveGP.internal.Parameter.get;
import java.util.Random;


/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class Area extends eveGP.GPfunction {

    private float variable = Float.NaN;
    private float [] possibleSelections = new float []
        { 3, 5, 7, 13 };
    
    public void init () {
        Random g = (Random) get("rGenerator");
        // rGenerator is the system random generator.
        this.variable = possibleSelections[g.nextInt(possibleSelections.length)];
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
        Area node = null;
        try {
            node = (Area) super.clone();
            if (Float.isNaN(variable));
                // Don't init on ourselves because there is one object at the
                // begining that we clone into every other ERC. 
                node.init();
        } catch (Exception e) { 
            System.err.println("class:Area -> Failure.");
        }
        return node;
    }
    
    @Override
    public String toString (Tree ... t) {
        return String.valueOf(variable);
    }
    
}
