# FixME

## Running FixME:
### Please run this command to enable colour in you win10 command line:
reg add HKEY_CURRENT_USER\Console /v VirtualTerminalLevel /t REG_DWORD /d 0x00000001 /f
### If the above line gives you an error run this:
reg add HKEY_CURRENT_USER\Console /v VirtualTerminalLevel /t REG_DWORD /d 0x00000000 /f

## Now FixME
#### To Build It:
mvn clean package
#### To Run The Server (Requires you to build first):
java -jar ./router/target/fixme-router-1.0-SNAPSHOT.jar
#### To Run The Broker (Requires you to build first):
java -jar ./broker/target/fixme-broker-1.0-SNAPSHOT.jar
#### To Run The Market (Requires you to build first):
java -jar ./market/target/fixme-market-1.0-SNAPSHOT.jar

## FixME Commands
#### Broker Commands
##### markets
Provides a list of available markets
##### list 123456
Provides a list of instruments of a specified market
##### buy instrument price quantity marketID
Buys the specified instrument quantity at the specified price from the specified market
buy shoes 4.32 3 123456
##### sell instrument price quantity marketID
Sells the specified instrument quantity at the specified price to the specified market
sell shoes 1.5 3 123456