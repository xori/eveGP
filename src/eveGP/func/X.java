/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eveGP.func;

import eveGP.GPfunction;
import eveGP.internal.Tree;

/**
 *
 * @author ev09qz
 */
public class X extends GPfunction{

    @Override
    public float result(Tree... children) {
	return getIntVariable("X");
    }
    
    public String toString (Tree ... children){
        return "X";
    }
}
