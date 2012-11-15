package eveGP;

import eveGP.internal.Tree;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import static eveGP.internal.Parameter.*;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public abstract class GPfunction implements Cloneable{
    public String result;
    public ArrayList<String> parameterType;
    public int ID;
    // that must get set whenever a Problem wants to evaluate.
    
    public GPfunction () {
	result = "*";
	parameterType = new ArrayList<String>();
    }
    
    public GPfunction (String result, String ... paramTypes) {
        this.result = result;
        this.parameterType = new ArrayList<String>(Arrays.asList(paramTypes));
        //this.parameters = new ArrayList<GPfunction>(parameterType.size());
    }
    
    public final Object getVariable       (String s) { return get("Problem."+ID+"."+s);}
    public final int    getIntVariable    (String s) { return getI("Problem."+ID+"."+s);}
    public final String getStringVariable (String s) { return getS("Problem."+ID+"."+s);}
    public final float  getFloatVariable  (String s) { return getF("Problem."+ID+"."+s);}
    
    /**
     * Call this to evaluate a function. It takes care of caching!
     * //TODO put this in Tree.
     * @return Result of custom function.
     */
    public float value (int ID, Tree ... children) {
	this.ID = ID;
        return result(children);
    }
    
    /**
     * Where your magic function happens. To evaluate the parameters, call the
     *  `value()` function on `children`.
     * @param children
     * @return The result of your function
     */
    public abstract float result (Tree ... children);
    
    /**
     * For when outputting to a file. Something in the style of...
     *  `( operation children[0].toString() children[1].toString() )`
     * @param children
     * @return 
     */
    public String toString (Tree ... children){return "( NOT DEFINED )";};
  
    @Override
     public Object clone() {
	try {
	    return super.clone();
	} catch (CloneNotSupportedException ex) {
	    Logger.getLogger(GPfunction.class.getName()).log(Level.SEVERE, "GPfunction.clone", ex);
	}
	return null;
     }
}
