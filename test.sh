#!/bin/bash

mvn package -q

JAR="target/MyDynLang-1.0.jar"

echo "=== Num + Num ==="
java -jar "$JAR" '"1"+"2"'
java -cp ".:target/classes" MyLangProgram

echo "=== Str + Str ==="
java -jar "$JAR" '"hello"+"world"'
java -cp ".:target/classes" MyLangProgram

echo "=== Str + Num ==="
java -jar "$JAR" '"hello"+"1"'
java -cp ".:target/classes" MyLangProgram

echo "=== Num + Str (expect error) ==="
java -jar "$JAR" '"1"+"hello"'
java -cp ".:target/classes" MyLangProgram
