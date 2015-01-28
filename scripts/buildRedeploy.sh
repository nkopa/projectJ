#!/bin/sh

echo "************ UNDEPLOYING *******************"
asadmin undeploy project
echo "************ BUILDING **********************"
mvn package
echo "************ DEPLOYING *********************"
asadmin deploy target/project.war
