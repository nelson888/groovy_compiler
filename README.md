# groovy_compiler

## Description
This a simple compiler written in Groovy

## How to compile
This project compiles with gradle
Run 
```
./gradlew uberjar
```
Note that the gradlew script was made for an Unix environment. It might not work on other OS.

## How to run
Once you've compiled the jar file, you can execute it with java such as below
```
java -jar build/libs/compiler-1.0.jar file1 file2 file3 ...
```

This programs expects a list of files as input. It will generate the compiled code in a new file
starting with the same name as the input and ending with the '.code' extension

## Execute compiled code
The generated code is for a simple processor with a few basic set of instructions.
To execute the compiled code, run
```
./msm file1.code
```

## What is supported (currently)
This compiler can compile

### Arithmetic expressions
It supports basic and boolean operators, except the power (^)
e.g:
```python
4 + 2
4 - (6 * 4 / 2)
5 % (4 - 4 * (5 - 3))
(1 >= 2) && (3 != 2)
(3 and 0) == (0 or 0)
```

### Variables
You can declare, and assign values to variables:
```javascript
var x;
x = 14;
var y = 3;
var z = x + 5* y;
```
### for and while loops
for and while loops are handled, like in the following example
```javascript
var i;
for (i = 0; i < 10; i = i + 1) {
    print i;
}
```

```javascript
var i = 0;
while (i < 10) {
    print i;
    i = i + 1;
}
```
You can print the value of variable with the `print` function

## Error handling
When there is an error while compiling, the error is displayed on the standard output.
You can see what caused the error (the line and column of the error is displayed but it is not working, sometimes there is a little offset for the column).
