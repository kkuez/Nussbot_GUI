package fxml;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import org.w3c.dom.Text;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.View;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.util.Properties;

public class MainController {

    @FXML
    private TextField hostIpTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField taskNameTextField;
    @FXML
    private Button chooseCertButton;
    @FXML
    private Button sendButton;
    @FXML
    private Label certLabel;
    @FXML
    private DatePicker dateDatePicker;
    @FXML
    private ChoiceBox timeChoiceBox;
    @FXML
    private ChoiceBox timeStrategyChoiceBox;
    @FXML
    private ChoiceBox forWhomChoiceBox;
    private Properties properties;
    private static File PROPERTIES_FILE = new File("properties.properties");

    @FXML
    private void send() {

    }

    @FXML
    private void chooseCertificate() {
        FileChooser fileChooser = new FileChooser();
        final File file = fileChooser.showOpenDialog(hostIpTextField.getScene().getWindow());
        certLabel.setText(file.getAbsolutePath());
        persist();
    }

    public void postInit() {
        checkSetupPropertiesFile();

        setupListener(hostIpTextField);
        setupListener(usernameTextField);
        setupListener(passwordTextField);
        setupListener(certLabel);

        setFieldsToProperties();
    }

    private void setupListener(Control control) {
        if (control instanceof TextField) {
            final TextField textField = (TextField) control;
            textField.setOnKeyReleased(keyEvent -> {
                properties.setProperty(textField.getId(), textField.getText());
                persist();
            });
        } else if (control instanceof Label) {
            final Label label = (Label) control;
            label.setOnKeyReleased(keyEvent -> {
                properties.setProperty(label.getId(), label.getText());
                persist();
            });
        }
    }

    private void checkSetupPropertiesFile() {
        if (!PROPERTIES_FILE.exists()) {
            //TODO loggen das nicht gefunden
            try {
                PROPERTIES_FILE.createNewFile();
            } catch (IOException e) {
                //TODO
                e.printStackTrace();
            }
        } else {
            properties = new Properties();
            try {
                properties.load(new FileReader(PROPERTIES_FILE));
            } catch (IOException e) {
                //TODO
                e.printStackTrace();
            }
        }
    }

    public void setFieldsToProperties() {
        if(PROPERTIES_FILE.length() < 10) {
            return;
        }
        hostIpTextField.setText(properties.getProperty("hostIpTextField"));
        usernameTextField.setText(properties.getProperty("usernameTextField"));
        passwordTextField.setText(properties.getProperty("passwordTextField"));
        certLabel.setText(properties.getProperty("certLabel"));
    }

    private void persist() {
        try (FileWriter fileWriter = new FileWriter(PROPERTIES_FILE)) {
            properties.setProperty("hostIpTextField", getOrElseEmpty(hostIpTextField.getText()));
            properties.setProperty("usernameTextField", getOrElseEmpty(usernameTextField.getText()));
            properties.setProperty("passwordTextField", getOrElseEmpty(passwordTextField.getText()));
            properties.setProperty("certLabel", getOrElseEmpty(certLabel.getText()));
            properties.store(fileWriter, "");
        } catch (IOException e) {
            //TODO
            e.printStackTrace();
        }
    }

    private String getOrElseEmpty(Object o) {
        return o == null ? "" : o.toString();
    }
}
