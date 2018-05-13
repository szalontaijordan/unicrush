# UniCrush

This game is a JavaFX game, that has much in common with the original Candy Crush game.

## Concept

When playing this game, you can play on levels, that are loaded from the oracle database of *University of Debrecen*. On the levels you can swap candies in order to earn points. If you swapped two, and there are three or more in a row or a column, these candies will disappear, and new candies will fall from the top of the level. If you idle, a suggesting box will appear around the candies you may swap. If there are no available moves, the level will reset to its inital state in a short time.

In the database we store the user's name and we generate an ID for them, the levels, and information about players' highscore on levels.

## Setup
If you are not a student (or teacher) of *University of Debrecen*, you can' play levels from the database, you have to modify the code in the `CandyCrushGame.java` file.

1. Create oracle account [here](https://www.oracle.com/webapps/maven/register/license.html) for the OJDBC.

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

3. Clone this repository somewhere on your computer, and navigate there in the command line

4. Run the following command

``$ mvn package``

## Play

Use the following command to play:

``$ java -jar target/unicrush-1.0.jar``
