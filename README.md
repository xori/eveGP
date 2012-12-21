# eveGP
The evolutionary library _with grammar_ created for 4V82 and now used for everything. It has been created by Evan Verworn (4582938) `<ev09qz@brocku.ca>` for the purpose of creating experiments quickly.

## How to install
Located in the `dist` folder will be a file called `eveGP.jar`. This is the entire library that must be included in your java CLASSPATH for your import statements to work.

That's it.

## How to use
This is more of a longer topic but can be broken into smaller ones.

* [Structure](#structure)
* [Parameters](#parameters)
* [Problem/Function Creation](#problem)
* [Executing](#execution)

Explicit [examples](#examples) will be given at the end of this document. You can totally just learn from those.

<a id="structure"></a>
### Structure 
The structure of a genetic programming problem is pretty simple, there are 3 primary things that will need to be in every project.

<a id="problem"></a>
#### your **Problem**

Your definition of your fitness function and possibily your testing functions if you have any. _(For testing data not trained on)_ This is a java class extending the `eveGP.GPproblem`, to learn more about what this entails you can scroll down to where we cover the [expected overriden functions](#GPproblem). Note that this will have to be in a package that you define and that this package must also be in your java CLASSPATH as eveGP will try to dynamically load it.

#### your **Functions**

Very minimal java classes extending the [GPfunction](#GPfunction) class with most likely a single function in them. These can range from as simple as addition to as complex as forier transforms. _(There are also built in functions for the simple cases. Add, Subtract, Multiply, etc. These are in the package `eveGP.func.*`)_ Your GP functions must also be in a package referenced in your java CLASSPATH.

<a id="parameters"></a>
#### your **Parameters**

This is a text file named whatever you want, the standard to is end it with a '`.param`'. These parameters are where you can set global parameters, some of them will be required. You can also make up your custom parameters here then get them in your defined problems/functions with the `Parameter.get("<name>")` function.

The parameters follow a syntax style of,

	NAME = VALUE
where the `'='` is optional, and can also be replaced with either whitespace or the `':'` symbol. Or both `':='`

You must also declare your functions and how the GP can perform crossover/mutation with them. Your functions can be declared in your parameter file with the following syntax.

	function MY.PACKAGE.CLASS ({TYPE}) : TYPE
where that is the manditory string 'function' followed by the java class URI of the defined function (this being in your java CLASSPATH). Followed by a list of virtual types that your function recieves, followed lastly by a return virtual type. For example.

	function net.verworn.GreaterThan (INT,INT) : BOOL
It should be noted that `'INT'` and `'BOOL'` do not map to actual java classes but are instead more a way of differentiating generic types. So when the crossover function is being applied it **does not** do this,

	GreaterThan(GreaterThan(5,2),3)
Which is of course incorrect because how can you compare a boolean to an integer in terms of which is greater.

If however you **do** want your function to _also_ accept `BOOL`s and `INT`s you can declare it twice,

	function net.verworn.GreaterThan (INT,INT) : BOOL
	function net.verworn.GreaterThan (BOOL,BOOL) : BOOL
then decide how it should handle these cases within the class `net.verworn.GreaterThan`. This declaration would accept **either** `BOOL`s OR `INT`s but not a mix of either. To make a function accept all virtual types you can denote it with a `'*'`.

### System Parameters
The following parameters are optional (with the exception of "`problem`") their default values will be listed below.

	generations = 50
	population  = 100
	seed		= <random generated integer number at startup>
	mutation	= 0.1
	crossover	= 0.9
	elitism		= 0
These are fairly standard parameters, just know that `mutation` and `crossover` should equal 1.0, `elitism` takes either a `0` or `1` and that there exists a global parameter called `generation` which defines the **current** generation when evaluating.

	tourney		= 3
Represents how big the tournement size will be when selecting the top individuals.

	threads		= 1
This is a multi-threaded enabled library so you can have as many threads as you wish.

	problem		= my.package.Class
This is the only **manditory** parameter **not set** for you. This must be the java URI for your defined [problem](#problem) that is in your java CLASSPATH

	root.result = *
Because this library supports grammars you can define what you want your final virtual type to be. In the default case it is set to the wildcard, but you can make it whatever you wish.

	stats		= "stats"
This is filename of the default stats file relative to the start location of the experiment. When you override the default `stat()` function in [GPproblem](#GPproblem) this is no longer used (unless you call `super.stat()`). 

That's it. Remember you can define your own custom global variables by using the [previously defined syntax](#parameters).

### Accessing Parameters in Java

The above was more of the syntax of **defining** global parameters, this section will be how to **use** them, and it all starts with

	import eveGP.internal.Parameter;
This class then has a few static functions which we will go into detail. Just know that these are for GLOBAL parameters and not [local (input) parameters](#localParameters).

<a id="Parameter.set"></a>
##### `void Parameter.set(String key, Object value)`
This sets a global parameter with the index of `key` to the value of `value`. Note that it doesn't care about the type of the value and that this operation **is** multi-thread safe.

<a id="Parameter.get"></a>
##### `Object Parameter.get(String key)`
This returns an uncasted object reference. This object is dependant on the `key` you send in, that is set with the [`set`](#Parameter.set) function.

##### `float Parameter.getF(String key)`
This returns an unboxed Float primitive. This works in the same way as the [`get`](#Parameter.get) function but will attempt to cast it to a _'float'_. If it fails, it will print to the `stderr` and return a `null` object.

##### `int Parameter.getI(String key)`
This returns an unboxed Integer primitive. This works in the same way as the [`get`](#Parameter.get) function but will attempt to cast it to a _'int'_. If it fails, it will print to the `stderr` and return a `null` object.

##### `String Parameter.getS(String key)`
This returns a String object. This works in the same way as the [`get`](#Parameter.get) function but will attempt to cast it to a _'String'_. If it fails, it will print to the `stderr` and return a `null` object.

##### `boolean Parameter.exists(String key)`
Returns true only if there exists a link for an index named `key`.

### Local Variables
When this GP library creates your parse trees you will need to set variables relivent for a particular individual. For example, in the case of

	+ ( X, Y)
What is `X` and `Y`? You will most likely be testing this data on many rows of data and `X` and `Y` will be changing with each row. You can't hardcode it. In these cases you can set these variables within the current tree by the following statement.

	setVariable("X", i);
	setVariable("Y", j);
Where `i` and `j` are some object (possibly 5 or 10 or "cookie"). These variables can then be retrieved by your later defined terminal functions with the statement.

	int x = (int) getVariable("X");
Casting these variables can get very annoying fast so a few helper functions were created to do just that.

	getIntVariable("X");
	getStringVariable("X");
	getFloatVariable("X");
All of these will attempt to cast the stored variable to the desired type, if it fails it will yell an error message and return null.

<a id="problem"></a><a id="GPproblem"></a>
### Creating Problems (GPproblem)
A problem in eveGP is a java class in a package defined in your java CLASSPATH that extends the `eveGP.GPproblem` class. This class has a few functions that can be overriden.

##### `abstract float evaluate ( Tree tree )`
This function is the only **required** function to be overriden. The purpose of this function is to return the evaluated fitness result, or the 'score' for the individual (_`tree`_). The individuals result can be retreived by running `tree.evaluate()`. The standard is to return a zero if that individual is a optimal solution.

##### `void stats (int generation, Tree ... trees)`
This function is an optional one that is run at the end of each generation. This is where you would process the generations results and maybe run your testing data if you had any. The array `trees` have been scored and their scores can be accessed via the field `trees[i].score`.

It should also be noted that if you want the default stats creation you should run `super.stats(generation, trees)` to output to the `stats.txt` file.

##### `void best (Tree t)`
There is also one more function, this function is run _once_ at the end of the experiment. It is passed the individual with the best score for you to run the individual on maybe more data, data that you wouldn't want to run on all of the individuals. This can sometimes give you a better representation of _how good_ your generated solution is.

<a id="GPfunction"></a>
### Creating Functions (GPfunction)
Functions can be like 'add' or 'subtract' or 'sine'. They can also be like 'move left', 'turn right' or 'attack'. It's really up to your creativity but the basis is that it must take **in** parameters and **return** a result.

The process of making a function starts by extending the `eveGP.GPfunction` class. This class has really only 2 functions that must be overriden.

##### `abstract float result (Tree ... children)`
This function takes in an array of other functions. The result of one of these trees can be retrieved using `children[i].evaluate()`. This function can be very simple, below is an example of a multiply function.

	@Override
	public float result(Tree ... children) {
		return children[0].evaluate() * children[1].evaluate();
	}
Crazy I know. Here is another example, of a `X` terminal function getting the local variable.

	@Override
	public float result(Tree ... children) {
		return getFloatVariable("X");
	}
They can of course be more complicated but there isn't a rule saying that they have to be.

##### `String toString(Tree ... children)`
When eveGP tries to output this tree it needs to know how to serialize this current fuction. Best just to see what I mean by example.

    public String toString (Tree ... children){
        return "(PLUS "+children[0].toString()+" "+children[1].toString()+")";
    }
This is an example of a `toString` method for an addition `GPfunction`. Calling `toString()` on a tree will trigger it to recurse to serialize its' children.

While this function is not *manditory* it is obviously highly recommended. After all if you don't put this in, you won't be able to do analysis on your generated function because you won't know what it is.

	(NOT DEFINED (NOT DEFINED (NOT DEFINED NOT DEFINED NOT DEFINED)))
What an amazing function that it.

<a id="execution"></a>
### Running Your Experiment
So now you have,

1. A [Problem](#GPproblem)
2. A list of [functions](#GPfunction)
3. A [Parameter](#parameters) file (with your functions declared in them)

You can then run your experiment with,

	java -jar eveGP.jar /some/dir/yourParameters.params
		or
	java eveGP.Evolve /some/dir/yourParameters.params

You can also set parameters on the command line. So for example if you were running your experiement 50 times you could (in a linux environment)

	#!/bin/bash
	for i in {0..50}
	do
		java -jar eveGP.jar myParams.params seed=$i
	done

You can append as many parameters as you want onto the end of the line, but make sure that there are **NO** whitespace between the name and the value.

	seed = 5 // INCORRECT
	seed=5   // CORRECT!

<a id="examples"></a>
## Learn by Example

#### An example parameter file.
	generations = 10
	population  = 50
	
	mutation    = 0.1
	crossover   = 0.9
	
	seed	    = 2
	tourney     = 5
	threads     = 6
	
	problem	    = example.Regression
	
	function eveGP.func.Add      (Float, Float) : Float
	function eveGP.func.Multiply (Float, Float) : Float
	function eveGP.func.X        ()             : Float
	function eveGP.func.ERC      ()             : Float

_Note the function `eveGP.func.ERC` this is a ephemeral random constant function that has been made for you. I would suggest tring to modify this to your needs rather than creating something yourself._

#### An example simple regression GPproblem
	package example;
	
	import eveGP.internal.Tree;
	import static java.lang.Math.*;
	
	public class Regression extends eveGP.GPproblem {
	
	    private float myFunction (int X) {
			return (float) (pow(X,3) + pow(X,2) + X);
	    }
	    
	    @Override
	    public float evaluate(Tree tree) {
			float sum = 0;
			for (int x = 0; x < 100; x++) {
			    setVariable("X", x);
			    sum += abs(tree.evaluate() - myFunction(x));
			}
			return sum;
	    }
	}

#### An example of an ephemeral random constant GPfunction
This is one of the more complicated GPfunctions that you could implement. I would just recommend coping this if you want to implement a ERC.

	package eveGP.func;
	
	import eveGP.internal.Tree;
	import static eveGP.internal.Parameter.get;
	import java.util.Random;

	public class ERC extends eveGP.GPfunction {
	
	    private float variable = Float.NaN;
	    
	    public void init () {
	        Random g = (Random) get("rGenerator");
	        // rGenerator is the system random generator.
	        this.variable = (g.nextInt(2) - 1) + g.nextFloat();
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
	        ERC node = null;
	        try {
	            node = (ERC) super.clone();
	            if (Float.isNaN(variable));
	                // Don't init on ourselves because there is one object at the
	                // begining that we clone into every other ERC. 
	                node.init();
	        } catch (Exception e) { 
	            System.err.println("Failure.");
	        }
	        return node;
	    }
	    
	    @Override
	    public String toString (Tree ... t) {
	        return String.valueOf(variable);
	    }
	    
	}

#### A move conventional example of a GPfunction
This example is a GreaterThan function.

	package eveGP.func;
	
	import eveGP.internal.Tree;
	
	public class GreaterThan extends eveGP.GPfunction {
	
	    @Override
	    public float result(Tree... children) {
	        float d = children[0].evaluate() - children[1].evaluate();
	        return (d > 0) ? 1 : 0;
	    }
	    
	    public String toString (Tree ... children) {
	        return "( > "+ children[0].toString() +
	                " " + children[1].toString() + " )";
	    }
	}