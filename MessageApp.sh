#!/bin/bash

javac -d bin src/*.java
java -cp "bin:img:lib/*" Main
