module ru.swarm.dairy.yairy {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.apache.commons.io;
    requires com.google.gson;

    opens ru.swarm.dairy.yairy to javafx.fxml;
    exports ru.swarm.dairy.yairy;
    opens ru.swarm.dairy.yairy.control.controllers to javafx.fxml;
    exports ru.swarm.dairy.yairy.control.controllers;
    exports ru.swarm.dairy.yairy.model.data.book;
    exports ru.swarm.dairy.yairy.model.data.page;
}