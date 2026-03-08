# Implementing a dynamic typed Language for the JVM

Common Commands:
```bash
# To build the compiler
mvn package

# To run the compiler
java -jar target/MyDynLang-1.0.jar '<Expression To Calculate>'
# For example,
java -jar target/MyDynLang-1.0.jar '"1"+"2"'

# To view the class file
javap -c -v MyLangProgram.class

# To run the MyLangProgram
java -cp ".:target/classes" MyLangProgram
```