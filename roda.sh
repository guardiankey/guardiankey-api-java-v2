#!/bin/bash

# mvn dependency:copy-dependencies -DoutputDirectory=libs/ -Dhttps.protocols=TLSv1.2

CP="src/:libs/commons-codec-1.11.jar:libs/commons-logging-1.2.jar:libs/gson-2.8.2.jar:libs/httpclient-4.5.13.jar:libs/httpcore-4.4.13.jar"

FILE=$1

/usr/lib/jvm/java-8-openjdk-amd64/bin/javac -cp $CP src/$FILE.java 
/usr/lib/jvm/java-8-openjdk-amd64/bin/java -cp $CP  $FILE

