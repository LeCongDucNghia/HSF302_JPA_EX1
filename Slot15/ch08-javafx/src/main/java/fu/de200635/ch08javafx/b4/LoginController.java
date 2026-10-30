package fu.de200635.ch08javafx.b4;

import fu.de200635.ch08javafx.common.AlertUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Map;

public class LoginController {

    // TODO 3: @FXML field — tên trùng fx:id
    @FXML
    private TextField tfUsername, tfPasswordVisible;
    @FXML private PasswordField pfPassword;
    @FXML private CheckBox chkShow;
    @FXML private ComboBox<String> cbRole;
    @FXML private Label lblError;
    @FXML private Button btnLogin;

    private static final Map<String, String[]> ACCOUNTS = Map.of(
            "student", new String[]{"Student@123", "Sinh viên"},
            "teacher", new String[]{"Teacher@123", "Giảng viên"});
    private int failedCount = 0;



    @FXML
    private void handleForgot() {
        TextInputDialog d = new TextInputDialog();
        d.setHeaderText("Nhập email để lấy lại mật khẩu");
        d.showAndWait().ifPresent(email -> {
            if (email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) AlertUtil.info("Đã gửi hướng dẫn tới " + email);
            else AlertUtil.warn("Email không hợp lệ");
        });
    }
}
