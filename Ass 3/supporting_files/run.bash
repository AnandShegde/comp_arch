ant make-jar
java -Xmx1g -jar jars/simulator.jar supporting_files/test_cases/$1.config $2.expected supporting_files/test_cases/$3.out
