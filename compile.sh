# Create the directories if they do not exist yet
if [ ! -d "./bin" ]; then
	echo createFolders
	mkdir bin
	if [ ! -d "./bin/nutriscore" ]; then
		mkdir bin/nutriscore
	fi
else
	echo folders already exist
fi

# Compile
javac src/nutriscore/*.java -d classes/nutriscore/

# Set the classpath - failed
# java -classpath src/nutriscore/Nutriscore

# Prepare the JAR
echo Main-Class: Nutriscore > classes/manifest.txt
jar cfm Nutriscore.jar MANIFEST.MF -C src/nutriscore/
chmod +x Nutriscore.jar

