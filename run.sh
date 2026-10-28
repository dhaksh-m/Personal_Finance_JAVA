#!/bin/bash
# Personal Finance Manager - Linux/Mac compile and run script
# Requires JDK 17+
set -e
mkdir -p out
echo "Compiling..."
javac -d out src/com/finance/*.java
echo "Running..."
java -cp out com.finance.Main
