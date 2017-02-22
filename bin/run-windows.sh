#!/bin/bash
javac -d classes -classpath classes\;. model/*.java;javac -d classes -classpath classes\;. userinterface/*.java;javac -classpath classes\;. Assign3.java;java -cp mysql-connector-java-5.1.7-bin.jar\;classes\;. Assign3
