package eveGP;

import eveGP.internal.Tree;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public abstract class GPfunction {
    private float value = Float.NaN;
    public String result;
    public ArrayList<String> parameterType;
    // that must get set whenever a Problem wants to evaluate.
    
    public GPfunction (String result, String ... paramTypes) {
        this.result = result;
        this.parameterType = new ArrayList<String>(Arrays.asList(paramTypes));
        //this.parameters = new ArrayList<GPfunction>(parameterType.size());
    }
    
    /**
     * Call this to evaluate a function. It takes care of caching!
     * @return Result of custom function.
     */
    public float value () {
	if (value != Float.NaN)
	    return value;
	value = result();
	return value;
    }
    
    /**
     * Where your magic function happens. To evaluate the parameters, call the
     *  `value()` function on `children`.
     * @param children
     * @return The result of your function
     */
    public float result (Tree ... children){return 0;};
    
    /**
     * For when outputting to a file. Something in the style of...
     *  `( operation children[0].toString children[1].toString )`
     * @param children
     * @return 
     */
    public String toString (Tree ... children){return "( NOT DEFINED )";};
}
