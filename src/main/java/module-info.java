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
    exports com.example.meetingplanner_demo.Model;
    opens com.example.meetingplanner_demo.Model to javafx.fxml;
    exports com.example.meetingplanner_demo.DataAccessLayer;
    opens com.example.meetingplanner_demo.DataAccessLayer to javafx.fxml;
    exports com.example.meetingplanner_demo.BusinessLayer;
    opens com.example.meetingplanner_demo.BusinessLayer to javafx.fxml;
    exports com.example.meetingplanner_demo.ViewModel;
    opens com.example.meetingplanner_demo.ViewModel to javafx.fxml;
}