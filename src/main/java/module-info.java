module bst.marketsimulator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens application.driver to javafx.fxml;
    exports application.driver;
    opens application.panels to javafx.fxml;
    exports application.panels;
    opens application.panels.creative to javafx.fxml;
    exports application.panels.creative;
    opens application.panels.informative to javafx.fxml;
    exports application.panels.informative;
    opens application.panels.plot to javafx.fxml;
    exports  application.panels.plot;
}