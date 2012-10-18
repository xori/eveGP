package eveGP;

import eveGP.internal.tree.GPtree;

/**
 *
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class GPstat {
    
    /**
     * This is the function responsible for outputting statistics about the best
     * of generations to a file. You can override it to have your own stats sent
     * to the `stats.file`. May I also recommend running the `super.statistics()`
     * function when you do override.
     * @param generation number of generation currently being evaluated.
     * @param trees an array of all trees in this generation. Their score is
     *	    accessed via `trees[i].score`.
     */
    public void statistics (int generation, GPtree ... trees) {
	// Outputs to file.
    }
}
