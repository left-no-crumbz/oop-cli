# 🏨 Hotel Management System

Welcome to the Hotel Management System, a command-line interface (CLI) application developed as a final project for the Object-Oriented Programming (OOP) course. This project showcases the core principles of OOP, including inheritance, polymorphism, encapsulation, and abstraction.

## 📚 Table of Contents

- [Description](#description)
- [Features](#features)
- [OOP Principles](#oop-principles)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## 📖 Description

The Hotel Management System is a Java-based CLI application that allows users to perform various tasks related to hotel management, such as making reservations, canceling reservations, and searching for available rooms. The application provides a user-friendly interface with menus and prompts for easy navigation. This project was developed as a final requirement for the Object-Oriented Programming (OOP) course and serves as a guide and reference for future students taking the course. 

## 🌟 Features

- Make a reservation by selecting the desired room tier, check-in date, and check-out date
- Cancel an existing reservation by providing the customer ID and room number
- Search and filter available rooms based on different criteria (status, tier, price)
- Display all transaction records and room information
- Write and read data to/from a CSV file for persistent storage

## 🧰 OOP Principles

The Hotel Management System project demonstrates the following OOP principles:

1. **Inheritance**: The `Room` class serves as a base class, and various room types (`DeluxeRoom`, `ExecutiveRoom`, and `PresidentialRoom`) inherit from it, inheriting common properties and methods.

2. **Polymorphism**: The `Room` class and its subclasses exhibit polymorphism through method overriding, allowing objects of different classes to be treated as instances of the base class.

3. **Encapsulation**: The project follows the principle of encapsulation by hiding implementation details within classes and providing public methods to access and modify data.

4. **Abstraction**: The `Room` class is an abstract class, representing the common characteristics of different room types, while the concrete implementations are defined in the subclasses.

## 🚀 Installation

To run the Hotel Management System locally, follow these steps:

1. Clone the repository: `git clone https://github.com/left-no-crumbz/oop-cli.git`
2. Navigate to the project directory: `cd Hotel`
3. Compile the Java files: `javac *.java`
4. Run the application: `java App`

## 🤝 Usage

Upon running the application, you will be presented with a menu that allows you to perform various actions. Follow the on-screen prompts to navigate through the application.

## 📄 License

This project is licensed under the [MIT License](LICENSE).
