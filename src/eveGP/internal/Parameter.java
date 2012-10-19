package eveGP.internal;

import java.util.HashMap;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class Parameter {
    private static HashMap<String, Object> Parameter = new HashMap<String, Object>();
    
    private Parameter () { }
    
    public static void set (String s, Object o) {
        Parameter.put(s, o);        
    }
    
    public static Object get (String s) {
        return Parameter.get(s);
    }
    public static float getF (String s) {
        Float f = null;
        try {
            f = (Float) Parameter.get(s);
        } catch (Exception e) {
            System.err.println("CAST ERROR:: Parameter "+s+" accessed as Float but was not a Float.");
        }
        return (float) f;
    }
    public static int getI (String s) {
        Integer f = null;
        try {
            f = (Integer) Parameter.get(s);
        } catch (Exception e) {
            System.err.println("CAST ERROR:: Parameter "+s+" accessed as Integer but was not an Integer.");
        }
        return (int) f;
    }
    public static String getS (String s) {
        String f = null;
        try {
            f = (String) Parameter.get(s);
        } catch (Exception e) {
            System.err.println("CAST ERROR:: Parameter "+s+" accessed as String but was not a String.");
        }
        return f;
    }    
}
