package eveGP.internal;

import eveGP.GPproblem;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * major modification of this class taken from IBM:
 *      http://www.ibm.com/developerworks/library/j-jtp0730/index.html
 * @author Evan Verworn (4582938) <ev09qz@brocku.ca>
 */
public class ThreadManager {
    public ThreadPoolExecutor executor;
    private ArrayBlockingQueue<Runnable> queue;
    //private final sem flag = new sem();
    private Class problem;

    public ThreadManager(int nThreads, Class workers) {
        queue = new ArrayBlockingQueue<Runnable>(Parameter.getI("population"));
        executor = new ThreadPoolExecutor(nThreads, nThreads, 20, TimeUnit.SECONDS, queue);
        problem = workers;
    }

    public void execute(Tree ... newjobs) {
        //ProblemWorker [] jobs = new ProblemWorker[newjobs.length];
        for(int i = 0; i < newjobs.length; i++) {
            executor.execute(new ProblemWorker(newjobs[i]));
        }
    }
    
    private class ProblemWorker implements Runnable {
        Tree        theTree;
        GPproblem   worker = null;
        
        public ProblemWorker (Tree p) {
            theTree = p;
            try {
                worker = (GPproblem) problem.newInstance();
            } catch (Exception ex) { System.exit(6); }
            worker.setInfo(worker.hashCode());
        }
        
        public void run() {            
            worker.eval(theTree);
            worker.run();
//            synchronized(flag) {
//                //Evolve.done++;
//                flag.down();
//                if(flag.done()) flag.notifyAll();
//            }
        }

    }
}
