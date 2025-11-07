# cockpitsim
Testing:
1. Test scripts may technically be anywhere but please restrict to src/main/resources. Example script 'test_001' provided here.
2. Test scripts ran through the testing window will output to .log files in /display.
3. Both '.test' and '.log' files are both just UTF-8 so can be read/written in any text editor.

Unit Testing:
1. Unit tests can be run from "/display" using "mvn clean test"
2. Specific unit test files can be run from "/display" using "mvn clean test -Dtest={testFile}"
3. E.g. "mvn clean test -Dtest=FormatRadarTest"

Developers:
1. Install Maven
2. Run from "/display" using "mvn clean javafx:run"
3. javafx Tutorial info: https://www.tutorialspoint.com/javafx/index.htm
4. javafx.graphics javadoc: https://openjfx.io/javadoc/21/javafx.graphics/module-summary.html

Users:
1. (After project deployed) Run from "/display/src/main/java/output/display.exe"
2. An msi installer is present here if the above executable does not function
