module com.example.issband {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.example.issband.Controller to javafx.fxml;

    exports com.example.issband;
    exports com.example.issband.Controller;

}
