module explorer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.apache.commons.io;
    requires javafx.swing;
    requires transitive javafx.graphics;
    opens explorer to javafx.fxml;
    exports explorer;
}