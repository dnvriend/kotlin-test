package com.github.dnvriend

// create a class to hold data
// the data keyword creates the following:
// - equals()/hashCode() pair,
// - toString() of the form "User(name=John, age=42)",
// - componentN() functions corresponding to the properties in their order of declaration,
// - copy()
data class Person(val name: String, val age: Int)

data class Address(val street: String, val zipcode: String, val houseNumber: Int)