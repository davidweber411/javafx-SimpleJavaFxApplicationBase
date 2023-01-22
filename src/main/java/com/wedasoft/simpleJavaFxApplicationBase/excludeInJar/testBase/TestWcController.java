package com.wedasoft.simpleJavaFxApplicationBase.excludeInJar.testBase;

import com.wedasoft.simpleJavaFxApplicationBase.fxmlDialog.FxmlDialogControllerBase;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

@SuppressWarnings("unused")
public class TestWcController extends FxmlDialogControllerBase {

    @FXML
    private TextField tfFirstname;
    @FXML
    private TextField tfLastname;
    @FXML
    private TextField tfAge;
    @FXML
    private TextField tfControlField;
    @FXML
    private RadioButton rbMale;
    @FXML
    private RadioButton rbFemale;
    @FXML
    private RadioButton rbOther;
    @FXML
    private ChoiceBox<String> cbNationality;
    @FXML
    private CheckBox cbProgramming;
    @FXML
    private CheckBox cbGaming;
    @FXML
    private CheckBox cbSports;
    @FXML
    private CheckBox cbMusic;
    @FXML
    private Button btnOk;
    @FXML
    private Button btnClear;
    @FXML
    private Label lControlLabel;

    @Override
    public void onFxmlDialogReady() {
        ToggleGroup rbToggleGroup = new ToggleGroup();
        rbMale.setToggleGroup(rbToggleGroup);
        rbFemale.setToggleGroup(rbToggleGroup);
        rbOther.setToggleGroup(rbToggleGroup);

        cbNationality.getItems().addAll("Other", "Austrian", "English", "German", "Italian", "Spanish", "Swedish");
        cbNationality.getSelectionModel().selectFirst();

        btnOk.setOnAction(event -> tfControlField.setText("btnOkClicked"));

        btnClear.setOnAction(event -> {
            tfFirstname.setText("");
            tfLastname.setText("");
            tfAge.setText("");
            tfControlField.setText("btnClearClicked");
            rbMale.setSelected(false);
            rbFemale.setSelected(false);
            rbOther.setSelected(false);
            cbNationality.getSelectionModel().selectFirst();
            cbProgramming.setSelected(false);
            cbGaming.setSelected(false);
            cbSports.setSelected(false);
            cbMusic.setSelected(false);
            lControlLabel.setText("");
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public TextField getTfFirstname() {
        return tfFirstname;
    }

    public void setTfFirstname(TextField tfFirstname) {
        this.tfFirstname = tfFirstname;
    }

    public TextField getTfLastname() {
        return tfLastname;
    }

    public void setTfLastname(TextField tfLastname) {
        this.tfLastname = tfLastname;
    }

    public TextField getTfAge() {
        return tfAge;
    }

    public void setTfAge(TextField tfAge) {
        this.tfAge = tfAge;
    }

    public TextField getTfControlField() {
        return tfControlField;
    }

    public void setTfControlField(TextField tfControlField) {
        this.tfControlField = tfControlField;
    }

    public RadioButton getRbMale() {
        return rbMale;
    }

    public void setRbMale(RadioButton rbMale) {
        this.rbMale = rbMale;
    }

    public RadioButton getRbFemale() {
        return rbFemale;
    }

    public void setRbFemale(RadioButton rbFemale) {
        this.rbFemale = rbFemale;
    }

    public RadioButton getRbOther() {
        return rbOther;
    }

    public void setRbOther(RadioButton rbOther) {
        this.rbOther = rbOther;
    }

    public ChoiceBox<String> getCbNationality() {
        return cbNationality;
    }

    public void setCbNationality(ChoiceBox<String> cbNationality) {
        this.cbNationality = cbNationality;
    }

    public CheckBox getCbProgramming() {
        return cbProgramming;
    }

    public void setCbProgramming(CheckBox cbProgramming) {
        this.cbProgramming = cbProgramming;
    }

    public CheckBox getCbGaming() {
        return cbGaming;
    }

    public void setCbGaming(CheckBox cbGaming) {
        this.cbGaming = cbGaming;
    }

    public CheckBox getCbSports() {
        return cbSports;
    }

    public void setCbSports(CheckBox cbSports) {
        this.cbSports = cbSports;
    }

    public CheckBox getCbMusic() {
        return cbMusic;
    }

    public void setCbMusic(CheckBox cbMusic) {
        this.cbMusic = cbMusic;
    }

    public Button getBtnOk() {
        return btnOk;
    }

    public void setBtnOk(Button btnOk) {
        this.btnOk = btnOk;
    }

    public Button getBtnClear() {
        return btnClear;
    }

    public void setBtnClear(Button btnClear) {
        this.btnClear = btnClear;
    }

    public Label getLControlLabel() {
        return lControlLabel;
    }

    public void setLControlLabel(Label lControlLabel) {
        this.lControlLabel = lControlLabel;
    }
}
