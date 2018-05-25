# MIR_P02

## Before the first installation (add local LIRE jar to the Maven repository).
1. mvn install:install-file -Dfile=./lib/lire.jar -DgroupId=net.semanticmetadata.lire -DartifactId=lire -Dversion=1.0b4 -Dpackaging=jar

## Package
1. mvn clean install

## Execute
1. cd target/
2. java -jar MIR_P02.jar [image_data_base_path][index_path][query_file_path]
