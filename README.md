
CartiClus: Cartification-based Subspace Cluster Finder
======================================================

This project is an implementation of CartiClus algorithm which is introduced in 
the paper:

"[Cartification: A Neighborhood Preserving Transformation for Mining High 
Dimensional Data][paper-link]" _by_ Emin Aksehirli, Bart Goethals, Emmannuel 
Müller, and Jilles Vreeken in Data Mining, 2013. ICDM 2013. Thirteenth IEEE 
International Conference on, 2013 IEEE

Application
-----------
CartiClus is packaged as a runnable .jar file (carticlus.jar). The .jar file
includes source code as well. You can run the application on command line with
the commands,

```
java -jar carticlus.jar data-file k minsup numOfdimensions [cartLog] [outputfile]
```

Input
-----
CartiClus accepts parameters as command line arguments in a specified order. 
If the optional parameters are omitted, their default values will be used instead.

- `data-file`: Path to the multi dimensional datafile that will be cartified. 
  Please find the properties of the data file below.
- `k`: Parameter for the _k_ nearest neighbors. 
- `minsup`: Minimum support count for the mining. This is the actual count not a 
  percentage.
- `numOfdimensions`: The number of dimensions in the data-file.
- `cartLog`: (optional) Direct the output of the mining step instead of /dev/null
- `outputfile`: (optional) Direct output to this file instead of standart output


### About data file:

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
```
Subspaces for cluster [Size of cluster] Objects of the cluster
```
For example, ```1 1 0 0 0 1 0 [10] 0 1 2 3 4 5 6 7 8 9``` means a cluster is
detected at 1st, 2nd and 6th subspaces and it has '10' objects, i.e.,
0 1 2 3 4 5 6 7 8 9


Example
-------

```
java -jar carticlus.jar data/10c20d.mime 125 300 20
```

Datasets
--------
Link for the artificial datasets with many irrelevant dimensions are given 
[here][datasets]. 
For the datasets from Opesnsubspace please refer to 
http://dme.rwth-aachen.de/en/OpenSubspace/evaluation

The code
--------
Code repository of the project is located at https://gitlab.com/adrem/carticlus 

Contact
-------
For more information you can visit http://adrem.ua.ac.be/cartification or send
an email to Emin Aksehirli <emin.aksehirli@uantwerpen.be>.

[paper-link]:http://www.adrem.ua.ac.be/bibrem/pubs/cartification.pdf
[data-sets]:http://adrem.ua.ac.be/sites/adrem.ua.ac.be/files/noiseDimension-data.tar.bz2
