package eveGP.internal;

import eveGP.GPfunction;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class Parameter {
    private static HashMap<String, Object> parameter = new HashMap<String, Object>();
    
    private Parameter () { }
    
    public static void set (String s, Object o) {
        parameter.put(s, o);        
    }
    
    public static Object get (String s) {
        return parameter.get(s);
    }
    public static float getF (String s) {
        Float f = null;
        try {
            f = (Float) parameter.get(s);
        } catch (Exception e) {
            System.err.println("CAST ERROR:: Parameter "+s+" accessed as Float but was not a Float.");
        }
        return (float) f;
    }
    public static int getI (String s) {
        Integer f = null;
        try {
            f = (Integer) parameter.get(s);
        } catch (Exception e) {
            System.err.println("CAST ERROR:: Parameter "+s+" accessed as Integer but was not an Integer.");
        }
        return (int) f;
    }
    public static String getS (String s) {
        String f = null;
        try {
            f = (String) parameter.get(s);
        } catch (Exception e) {
            System.err.println("CAST ERROR:: Parameter "+s+" accessed as String but was not a String.");
        }
        return f;
    }    
    
    public static boolean loadClass (String classToLoad, Class sup, String placeToStore, boolean init ) {
        Class<?> func;
        try {
            func = Class.forName(classToLoad).asSubclass(sup);
            if (init) {
                set(placeToStore, func.newInstance());
            } else {
                set(placeToStore, func);
            }
            return true;
        } catch (Exception e) {
            System.err.println(e.toString());
            return false;
        }
    }
    
    public static void loadFile (String f) {
        Scanner file = null;
        String temp, value;
        
        try {
            file = new Scanner(new File(f));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parameter.class.getName()).log(Level.SEVERE, null, ex);
        }
	int c = 1;
        while(file.hasNext()) {
	    System.out.println(c++);
            StringTokenizer token = new StringTokenizer(file.nextLine(),"\t :,()=");
            if (!token.hasMoreTokens()) continue; // blank line;
	    temp = token.nextToken();
	    if ("#".equals(temp)) continue;
            if ("function".equals(temp)) {
                // is function
                if (token.countTokens() != 2) { // we need at least a class name and a return value
                    int n = getI("functions");
                    String index = "functions."+n;
                    
                    if (!loadClass(token.nextToken(), eveGP.GPfunction.class, index, true)) {
                        // fail
                        continue;
                    }
                    GPfunction tempF = (GPfunction) get(index);
                    while(token.countTokens() > 1) {
			tempF.parameterType.add(token.nextToken());
		    }
                    tempF.result = token.nextToken();
                    
                    // Now put stats in parameter database.
                    set(index+".result", tempF.result);
                    set(index+".params", tempF.parameterType.toArray(new String[0]));
                    set(index+".params.size", tempF.parameterType.size());
                    set("functions", ++n);
                } else {
                    System.err.println("Needed at least 3 parameters for a function. 'function <class> () : <returnType>'");
                }
            } else {
                //is parameter
                if (token.countTokens() == 0) continue;
                if (token.countTokens()!= 1) { // should only be one more unconsumed token.
                    System.err.println("Parameter File is incorrect.");   
                    System.exit(1);
                }
                value = token.nextToken();
                
                try {
                    Integer t = Integer.parseInt(value);
                    set(temp, t);
                } catch (NumberFormatException e) {
                    try {
                        Float tf = Float.parseFloat(value);
                        set(temp, tf);
                    } catch (NumberFormatException ex) {
                        set(temp, value);
                    }
                }   
            }
            
        }
    }
    
    public static String dumpStats () {
	String output = "";
	for (String s : parameter.keySet()) {
	    output += s+": "+parameter.get(s).toString()+"\n";
	}
	return output;
    }
}
