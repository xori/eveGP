package eveGP.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public static void loadFile (String f) {
        Scanner file = null;
        String temp, value;
        
        try {
            file = new Scanner(new File(f));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parameter.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(file.hasNext()) {
            StringTokenizer token = new StringTokenizer(file.nextLine(),":,()=");
            temp = token.nextToken();
            if (temp.equals("function")) {
                // is function.                
            } else {
                //is parameter
                if (token.countTokens() == 0) continue;
                if (token.countTokens()!= 2) {
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
}
