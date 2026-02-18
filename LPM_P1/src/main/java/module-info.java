module com.sau.lpm.lpm_p1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.sau.lpm.lpm_p1 to javafx.fxml;
    opens com.sau.lpm.lpm_p1.controller to javafx.fxml;
    opens com.sau.lpm.lpm_p1.dto to javafx.fxml;
    exports com.sau.lpm.lpm_p1;
    exports com.sau.lpm.lpm_p1.controller;
    exports com.sau.lpm.lpm_p1.dto;
}