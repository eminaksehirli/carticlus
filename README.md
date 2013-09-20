
CartiClus: Cartification-based Subspace Cluster Finder
======================================================

Application
-----------
CartiClus is packaged as a runnable .jar file (carticlus.jar). The .jar file
includes source code as well. You can run the application on command line with
the commands,


    java -jar carticlus.jar

or

    java -jar carticlus.jar data.properties


Input
-----
CartiClus accepts a parameter configuration file as an command line argument.
If the program is run without arguments, it uses _default.properties_ as
default. Parameter configuration file is a standard Java properties file, i.e.,
key value pairs. See the comments in _default.properties_ file for the
explanation of parameters.

Data file should be specified in the configuration file, along with the other
parameters. About data file:

- it includes space separated real values,
- each row represents an instance,
- each column represents an feature/dimension,
- does not include a(ny) header row(s),
- all instances have the same number of features,
- there are no missing values,
- real values should be in the USA locale (use . as decimal separator)


Output
------
CartiClus outputs the found clusters to the standard output. Each line of output
represents a subspace cluster. Output format:

Subspaces for cluster [Size of cluster] Objects of the cluster

For example, ```1 1 0 0 0 1 0 [10] 0 1 2 3 4 5 6 7 8 9``` means a cluster is
detected at 1st, 2nd and 6th subspaces and it has '10' objects, i.e.,
0 1 2 3 4 5 6 7 8 9


Contact
-------
For more information you can visit http://adrem.ua.ac.be/cartification or send
an email to Emin Aksehirli <emin.aksehirli@ua.ac.be>.

