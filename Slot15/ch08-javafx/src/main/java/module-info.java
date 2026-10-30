module fu.de200635.ch08javafx {
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens fu.de200635.ch08javafx to javafx.fxml;
    exports fu.de200635.ch08javafx;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;      // T5
    requires javafx.media;
    requires java.desktop;    // T5

    // FXMLLoader cần reflection vào controller (@FXML private field/method)
    opens fu.de200635.ch08javafx.b4 to javafx.fxml;
    opens fu.de200635.ch08javafx.b5 to javafx.fxml;
    opens fu.de200635.ch08javafx.b6 to javafx.fxml;
    opens fu.de200635.ch08javafx.t1 to javafx.fxml, javafx.base;   // javafx.base: PropertyValueFactory (TableView)
    opens fu.de200635.ch08javafx.t4 to javafx.fxml, javafx.base;

    // Application class phải được export để javafx.graphics khởi tạo được
    // b1–b3 viết UI bằng code (không FXML) → chỉ cần exports, không cần opens
    exports fu.de200635.ch08javafx.b1;
    exports fu.de200635.ch08javafx.b2;
    exports fu.de200635.ch08javafx.b3;
    exports fu.de200635.ch08javafx.b4;
    exports fu.de200635.ch08javafx.b5;
    exports fu.de200635.ch08javafx.b6;
    exports fu.de200635.ch08javafx.t1;
    exports fu.de200635.ch08javafx.t2;
    exports fu.de200635.ch08javafx.t3;
    exports fu.de200635.ch08javafx.t4;
    exports fu.de200635.ch08javafx.t5;
    exports fu.de200635.ch08javafx.t6;
}
