module com.example.meetingplanner_demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires io;
    requires kernel;
    requires layout;

    opens com.example.meetingplanner_demo to javafx.fxml;
    exports com.example.meetingplanner_demo;
}