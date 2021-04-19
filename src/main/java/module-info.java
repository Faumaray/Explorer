module com {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.apache.commons.io;
    requires javafx.swing;

    opens com to javafx.fxml;
    exports com;
}