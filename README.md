# UniCrush

This game is a JavaFX game, that has much in common with the original Candy Crush game.

## Concept

When playing this game, you can play on levels, that are loaded from the oracle database of *University of Debrecen*. On the levels you can swap candies in order to earn points. If you swapped two, and there are three or more in a row or a column, these candies will disappear, and new candies will fall from the top of the level. If you idle, a suggesting box will appear around the candies you may swap. If there are no available moves, the level will reset to its inital state in a short time.

In the database we store the user's name and we generate an ID for them, the levels, and information about players' highscore on levels.

## Setup
We assume that you have Java 1.7+ and Maven already installed on your computer. If you are not a student (or teacher) of *University of Debrecen*, you can't play levels from the database, you have to modify the code in the `CandyCrushGame.java` file.

1. Create oracle account [here](https://www.oracle.com/webapps/maven/register/license.html) for the OJDBC.

2. At your maven home create the proper `settings.xml` file, so you can connect to the oracle database. (the structure is the following)
If you'd like to use your own credentials for the database, and not the developer's, you have to create the neccesary tables with the data in it.

For example in oracle sql developer:

```sql
CREATE TABLE uc_level(
  id number primary key,
  boardSize number not null check (boardSize > 3),
  walls varchar2(50),
  score number not null,
  steps number not null
);

CREATE TABLE uc_user(
  id number primary key,
  username varchar2(20) not null
);

CREATE TABLE uc_score(
  userId number,
  levelId number,
  score number,
  constraint pk primary key(userId, levelId)
);

INSERT INTO uc_level (id, boardSize, walls, score, steps)
  SELECT id, boardSize, walls, score, steps
    FROM U_GYKK9I.UC_LEVEL
;

COMMIT;
```

Then the `settings.xml` is:


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

4. Run: `$ mvn package`

## Play

Use the following command to play:

``$ java -jar target/unicrush-1.0-jar-with-dependencies.jar``

## Advanced

Generate the site with all the API documentation and the reports

``$ mvn site``

Running the applicaton with *logback*

``$ mvn exec:java``

Running the application with *tinylog*

``$ mvn exec:java -Ptinylog``