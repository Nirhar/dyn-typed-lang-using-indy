# Implementing a dynamic typed Language for the JVM

Common Commands:
```bash
# To build the compiler
mvn package

# To run the compiler
mvn exec:java -Dexec.mainClass=mydynlang.App 2>&1

# To view the class file
javap -c -v MyLangProgram.class

# To run the MyLangProgram
java MyLangProgram
```