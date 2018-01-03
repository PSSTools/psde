#!/bin/sh

# CP=./build/plugins/net.sf.psstools.lang/bin
CP="./build/pssc.jar"

for jar in ./build/pssc/libs/*.jar; do
CP="${CP};${jar}"
done

#CP="${CP};`ls ./libs/org.eclipse.equinox.common_*.jar`"
#CP="${CP};`ls ./libs/org.eclipse.emf.ecore_*.jar`"
#CP="${CP};`ls ./libs/org.eclipse.emf.common_*.jar`"
#CP="${CP};`ls ./libs/org.eclipse.emf.ecore.xmi_*.jar`"
#CP="${CP};`ls ./libs/org.eclipse.xtext_*.jar`"
#CP="${CP};`ls ./libs/org.eclipse.xtext.common.types_*.jar`"
#CP="${CP};`ls ./libs/org.eclipse.xtext.util_*.jar`"
#CP="${CP};`ls ./libs/com.google.inject_*.jar`"
#CP="${CP};`ls ./libs/com.google.guava_*.jar`"
#CP="${CP};`ls ./libs/javax.inject_*.jar`"
#CP="${CP};`ls ./libs/org.apache.log4j_*.jar`"
#CP="${CP};`ls ./libs/org.antlr.runtime_*.jar`"

# echo "CP=$CP"

java -classpath ${CP} net.sf.psstools.lang.pssc.PSSC \
  ${1+"$@"}

