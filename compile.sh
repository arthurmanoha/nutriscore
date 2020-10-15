javac src/nutriscore/*.java -d classes/nutriscore/
echo Main-Class: Nutriscore > classes/manifest.txt
jar cvfm Nutriscore.jar manifest.txt classes/*.class
