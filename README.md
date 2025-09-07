# John's Super Market - Inventory Management System

This project is a JavaFX-based desktop application for managing the inventory and suppliers of a supermarket. It serves as a graphical user interface (GUI) for the underlying inventory system, providing a more user-friendly experience than a command-line interface.

This application is built using Java and Maven, with a focus on Object-Oriented Programming (OOP) principles.

## Features

*   **Dynamic Dashboard**: On startup, the application immediately identifies and displays all inventory items that are below their configured low-stock threshold, allowing for quick assessment of what needs to be reordered.
*   **Comprehensive Item Management**:
    *   **Add**: Add new items to the inventory with details such as item code, name, brand, price, quantity, category, and a product image.
    *   **Update**: Modify the details of any existing item.
    *   **Delete**: Remove items from the inventory using a user-friendly selection mechanism.
    *   **View**: Display all inventory items in a neatly formatted table, sorted by category. The view also shows the total monetary value of the inventory.
*   **Supplier Management**:
    *   Simulates a supplier selection process by randomly choosing four dealers from a pre-populated text file.
    *   Displays the details of the selected suppliers, sorted by location, to help with procurement decisions.
*   **Data Persistence**:
    *   Inventory data is loaded from a text file (`items.txt`) on startup, enabling resume capabilities.
    *   Any changes (items added, updated, or deleted) can be saved back to the text file at any point.

## How to Build and Run

This project uses Apache Maven to manage dependencies and build the application.

### Prerequisites

*   Java Development Kit (JDK) 11 or higher.
*   Apache Maven.

### Running the Application

1.  Open a terminal in the root directory of the project (`john_super_market`).
2.  Compile the project and run the application using the Maven JavaFX plugin:
    ```sh
    mvn clean javafx:run
    ```

## Testing

The project includes a suite of JUnit tests to ensure functionality and robustness.

You can run the tests using the standard Maven command:
```sh
mvn test
```

**Warning**: The test suite is designed to run in a clean environment. Running the tests may clear or overwrite the `items.txt` and `dealers.txt` files, as it needs to create specific scenarios to validate the application's behavior. It is recommended to back up your data files before running the tests.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Author

*   GitHub: [p-sahas](https://github.com/p-sahas)