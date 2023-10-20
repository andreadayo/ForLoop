# Valid Syntax

## 1. Empty

```java
for ( int i = 0 ; i < 10 ; i++ ) { }
```

<br>

## 2. Complete for loop

```java
for ( int i = 0 ; i < 10 ; i++ ) {
  int x , y ; // identifier list
  a=3 ; // variable declaration
  b++ ; // update
  System.out.println ( "hello" ) ; // print
  c+=3 ; // expression

  for ( int j = 0 ; j < 3 ; j++ ) { // nested for loop
    counter+=1 ;
  }
}
```

_for ( int i = 0 ; i < 10 ; i++ ) { int x , y ; a=3 ; b++ ; System.out.println ( "hello" ) ; c+=3 ; for ( int j = 0 ; j < 3 ; j++ ) { counter+=1 ; } }_

<br>

## 3. Complete nested for loop

```java
for ( int i = 0 ; i < 10 ; i++ ) {
  for ( int j = 0 ; j < 3 ; j++ ) { // nested for loop 1
    counter+=1 ;
  }
  for ( int x = 0 ; x < 3 ; x++ ) { // nested for loop 2
    a++ ;
  }
}
```

_for ( int i = 0 ; i < 10 ; i++ ) { for ( int j = 0 ; j < 3 ; j++ ) { counter+=1 ; } for ( int x = 0 ; x < 3 ; x++ ) { a++ ; } }_

<br>

## 4. Expression as update (and w/o space)

```java
for ( i=0 ; i<length ; i+=1 ) { counter++ ; }
```

<br><br><br><br>

# Invalid Syntax

## 1. For loop unequal () {}

### a. Separators are not balanced

```java
for ( ( int i = 0 ; i < 3 ; i++ ) { }
```

### b. Separators are balanced but in the wrong order

```java
for ( int i = 0 ; i < 3 ; i++ ) } {
```

### c. Sequence () {} is not followed

```java
for { int i = 0 ; i < 3 ; i++ } ( )
```

<br>

## 2. Incorrect Control Statements

### a. Empty control statement

```java
for ( ) { System.out.println ( "hello" ) ; }
```

### b. Less than 3 correct control statements

```java
for ( variable ; i < 3 ; i++ ) { counter += 3 ; }
```

### c. More than 3 correct control statements

```java
for ( int i=0 ; i < 3 ; i++ ; i++ ) { counter += 3 ; }
```

<br>

## 3. Incorrect Statements

### a. Variable Declaration

```java
for ( int i = 0 ; i < 3 ; i++ ) { int a == 10 ; }
```

### b. Print

```java
for ( int i=0 ; i < 3 ; i++ ) { System.out.println ( 1abc ) ; }
```

### c. Expression

```java
for ( int i = 0 ; i < 3 ; i++ ) { i += int ; }
```

**COMBINED:** _for ( int i = 0 ; i < 3 ; i++ ) { int a == 10 ; System.out.println ( 1abc ) ; i += int ; }_

<br>

## 4. Statement not found

### a. Statement entered is not one of the defined statements

```java
for ( int i=0 ; i < 3 ; i++ ) { abc }
```

### b. When the statement does not end with a semicolon

```java
for ( int i=0 ; i < 3 ; i++ ) { i++ }
```

<br><br><br><br>

# Errors the program cannot recognize

## 1. Same variable declaration

```java
for ( int i = 0 ; i < 5 ; i++ ) {
  a = 3 ;
  int a = 4 ; // declares an existing var
}
```

_for ( int i = 0 ; i < 5 ; i++ ) { a = 3 ; int a = 4 ; }_

<br>

## 2. Variable declared twice locally in nested for

```java
for ( int i = 0 ; i < 5 ; i++ ) {
  for ( int i = 0 ; i < var ; i++ ) { // also uses int i
    System.out.println ( “hello” ) ;
  }
  counter++ ;
}
```

_for ( int i = 0 ; i < 5 ; i++ ) { for ( int i = 0 ; i < var ; i++ ) { System.out.println ( “hello” ) ; } counter++ ; }_
