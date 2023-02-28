# jdbc_insert
how to run:
first edit .env file with database information
then edit ConsumerMaster class, on line 32,33. Uncomment the one required and comment the one not required depending on database used.
Compile the classes: (NOTE: Run all the programs from root of project)
```
1. javac DataValidation.java -d .
2. javac CustomerMaster.java -d .
3. javac Main.java -d .
```
to run the program you need the jar file required for jdbc connection:

```java -cp <jar-file>;. org.example.Main <fileName> <fileLevel>```
This expects jar file to be at root file level.
example:

```java -cp mysql-connector-j-8.0.32.jar;. org.example.Main TestInput1.txt F```
