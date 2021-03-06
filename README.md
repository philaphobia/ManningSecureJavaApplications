# ManningSecureJavaApplications
Developing Secure Java Applications for Manning liveProject


## Java version
The program was developed on Java version 1.8, so you should download the latest JRE (and JDK if you want to build) to make sure you can work with the program in Eclipse.


## Application server
The program was developed and tested with Apache Tomcat 8.5. If you plan on deploying and testing the (not required for the project), make sure you have the latest version of Tomcat 8.5. The webapp was tested with a default Apache Tomcat and can simply be deployed by copying the target/ManningSecureCoding-0.0.1-SNAPSHOT.war file to the webapps directory under the Tomcat directory

## Folder structure is as follows:
* src/main/java - source code
* target/ManningSecureCoding-0.0.1-SNAPSHOT.war - compiled .war file to deploy to Tomcat (not required for the project)
* src/main/webapp - webapp (.war) file contents


## Integrated Development Environment (IDE)
Many Integrated Development Environment (IDE) tools are available for coding in Java, so pick the one you have used before. This project should open without major issues in IntelliJ IDEA or Eclipse EE IDE. Resource links are provided below if you are interested in learning the basic workflow of of using either IDE. 

## Help with IntelliJ
The project can be loaded directly using GIT:
1. From the GIT web page, click 'Code', then copy the HTTPS URL
2. In IntelliJ, chose 'Get from VCS'
3. Paste the GIT URL into the 'URL'

* You can also download from GIT manually, then open the project by choosing the directory downloaded from GIT.


## Help with Eclipse
You will need the Java EE version of Eclipse to open the project. On the Eclipse download page, it is called 'Eclipse IDE for Enterprise Java Developers'

The project can be loaded directly using GIT:
1. From the GIT web page, click 'Code', then copy the HTTPS URL
2. In Eclipse, chose File, then Import
3. Expand the Git option, and select 'Projects from Git'
4. Select 'Clone URI'
5. Paste the GIT URL into the 'URL'
6. Leave all the default options

* You can also download from GIT manually, then open the project by choosing the directory downloaded from GIT.
* If the project complains about the JRE library, you can following these steps to fix the issue:
1. Right-click the project and choose properties
2. Go to Java Build Path
3. Click the Libraries tab
4. Click add Library
5. Choose JRE System Library
6. Select Workspace default


## For further reading
These are resources not referenced in the liveProject but may be helpful to further your understanding of the liveProject's content.

* IntelliJ IDEA Tutorial - (https://www.tutorialspoint.com/intellij_idea/)
* Eclipse IDE Tutorial - (https://www.tutorialspoint.com/eclipse/)
* Download Eclipse IDE for Enterprise Java Developers (https://www.eclipse.org/downloads/packages/)
