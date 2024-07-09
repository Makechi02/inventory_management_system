module com.makechi.inventory_management_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.makechi.inventory_management_system to javafx.fxml;
    exports com.makechi.inventory_management_system;
    exports com.makechi.inventory_management_system.controller;
    opens com.makechi.inventory_management_system.controller to javafx.fxml;
    opens com.makechi.inventory_management_system.entity to javafx.base;
}