## Property Management System

This project is a Property Management System I developed as part of a university module on Software Development Advanced Techniques built in Java. The focus was on building a well-structured Java application using good design, solid object-oriented principles and following best practices.

## What it does

The system allows for renting of properties (either Villa or Apartment). You can:

- Add new properties
- Add new tenants
- Issue rental contracts for a given amount of time
- Terminate rentals 
- Find a list of properties with rentals expiring soon
- See how many properties are currently available for rent

Both property types are handled using an interface-based hierarchy, making it easier to extend or adapt the system in the future.

## Key Features

- Clean, modular code following interface-based hierarchy design, and with classes in appropriate packages. 
- Handle different property types using polymorphism and factories to guarentee uniqueness of object instantiation
- Programming through interfaces, creating an easily extendable structure for adding future features
- Defensive programming techniques (immutability, defensive copying, null checks, error handling, parameter validation, appropriate accessors and modifiers) to minimise risks of bugs and runtime errors
- Overriding Object class where appropriate
- Utilise relevant classes from the Java Collections Framework
- Testing using the Assertions class framework


