If you experience build errors saying no ojdbc7, run the following command in terminal at your project root.

``
$ mvn install:install-file 
    -Dfile="./lib/ojdbc7.jar"
    -DgroupId="com.github.noraui"
    -DartifactId="ojdbc7"
    -Dversion="12.1.0.2"
    -Dpackaging="jar"
    -DgeneratePom=true
``