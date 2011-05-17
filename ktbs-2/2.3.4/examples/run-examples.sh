#!/bin/sh

CLASSPATH=.:../*:../lib/*

echo "Classpath: $CLASSPATH"

java -cp $CLASSPATH org.liris.ktbs.examples.$1 $2


