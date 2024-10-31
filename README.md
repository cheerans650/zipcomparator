# ZipComparator

`ZipComparator` is a Java Maven project for comparing the contents of two `.zip` files. This utility is useful for verifying file consistency across zip archives, especially for scenarios involving frequent file packaging and validation.

## Table of Contents

- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## Getting Started

Follow these instructions to set up and run the `ZipComparator` project on your local machine.

### Prerequisites

Make sure you have the following installed:

- **Java JDK 11** or higher
- **Apache Maven** for dependency management and project building

### Installation

1. Clone the repository:
   ```bash
   https://github.com/puthusseri/zipcomparator.git

2. Change into the project directory:
   ```bash
   cd zipcomparator
3. Build the project:
    ```bash
    mvn clean install

### Usage
To compare two sample zip files located at /path/to/zip1.zip and /path/to/zip2.zip, use:
1. Package the Project as a JAR
    ```bash
    mvn clean package
2. Run the JAR File
    ```bash
    java -jar target/ZipComparator-1.0-SNAPSHOT.jar <path/to/zip1.zip> <path/to/zip2.zip>
3. 




