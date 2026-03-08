# Implementing a dynamic typed Language for the JVM

This project gives a demo of how a *(teeny-tiny)* dynamically typed language can be implemented for the JVM, using the invokedynamic bytecode. Here are the rules of the language:
1. A program only consists of one binary expression, using `+`.
2. The runtime just *evaluates* the expression and prints it to console
3. Each term in the expression is a string. However, each string-term can be interpreted as numbers for arithmetic.
4. The evaluation rules are as follows:
    - If LHS and RHS can both be interpreted as numbers, then add them arithmetically.
    - If LHS can be a Number and RHS is a String, then we throw an exception, that we cannot append strings to numbers
    - In all other cases, we convert both LHS and RHS to strings and concatenate them with a space.

The dispatch to the right method for evaluation of the expression is done by the invokedynamic bytecode at runtime.

## Common Commands:
Ensure JAVA_HOME is set and java is available in path. This has been tested on MacOS with Open JDK 21
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

# To run tests:
chmod +x test.sh
./test.sh
```