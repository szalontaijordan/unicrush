UniCrush
========

## Description
This game is a JavaFX game, that has much in common with the original Candy Crush game.

## Setup
If you are not a student (or teacher) of University of Debrecen, you can' play levels from the database, you have to modify the code in the `CandyCrushGame.java` file.

1. Create account here[https://www.oracle.com/webapps/maven/register/license.html]

2. At your maven home create the proper `settings.xml` file, so you can connect to the oracle database. (the structure is the following)
```xml
<profiles>
	<profile>
	    <id>oraDB</id>
	    <activation>
		<activeByDefault>true</activeByDefault>
	    </activation>
	    <properties>
		<db.username><!-- YOUR USERNAME TO ORA DB --></db.username>
		<db.password><!-- YOUR PASSWORD TO ORA DB --></db.password>
	    </properties>
	</profile>
</profiles>
<servers>
	<server>
	    <id>maven.oracle.com</id>
	    <username><!-- YOUR EMAIL ADDRESS YOU REGISTERED WITH --></username>
	    <password><!-- YOUR PASSWORD YOU REGISTERED WITH --></password>
	    <configuration>
		<basicAuthScope>
		    <host>ANY</host>
		    <port>ANY</port>
		    <realm>OAM 11g</realm>
		</basicAuthScope>
		<httpConfiguration>
		    <all>
		        <params>
		            <property>
		                <name>http.protocol.allow-circular-redirects</name>
		                <value>%b,true</value>
		            </property>
		        </params>
		    </all>
		</httpConfiguration>
	    </configuration>
	</server>
</servers>
```

3. Clone this repository somewhere on your computer

4. Run the following command

``$ mvn package``

## Play

Use the following command to play:

``$ java -jar target/........``
