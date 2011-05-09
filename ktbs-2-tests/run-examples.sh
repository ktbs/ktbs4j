#!/bin/sh

CLASSPATH=../lib/arq-2.8.4.jar:../lib/commons-io-2.0.jar:../lib/guava-r07.jar:../lib/httpclient-4.1-beta1.jar:../lib/httpclient-cache-4.1-beta1.jar:../lib/httpcore-4.1.jar:../lib/icu4j-3.4.4.jar:../lib/iri-0.8.jar:../lib/jena-2.6.3.jar:../lib/lucene-core-2.3.1.jar:../lib/org.springframework.asm-3.0.5.RELEASE.jar:../lib/org.springframework.beans-3.0.5.RELEASE.jar:../lib/org.springframework.context-3.0.5.RELEASE.jar:../lib/org.springframework.core-3.0.5.RELEASE.jar:../lib/org.springframework.expression-3.0.5.RELEASE.jar:../lib/stax-api-1.0.1.jar:../lib/wstx-asl-3.2.9.jar:../lib/xercesImpl-2.7.1.jar:../lib/jcl-over-slf4j-1.6.1.jar:../lib/slf4j-api-1.6.1.jar:../lib/logback-classic-0.9.28.jar:../lib/logback-core-0.9.28.jar:../ktbs-client-2.1.0.jar

java -cp $CLASSPATH:. org.liris.ktbs.examples.$1 $2



