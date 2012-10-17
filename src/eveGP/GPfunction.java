package eveGP;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public abstract class GPfunction {
    private float value = Float.NaN;
    // private ArrayList<GPfunction> parameters;
    
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
    public abstract float result (GPfunction ... children);
}
