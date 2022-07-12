package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.StudentDTO;
import views.tm.StudentTM;

import java.sql.*;

public class ManageStudentFormController {
    public JFXButton btnAddNewStudent;
    public JFXTextField txtStudentId;
    public JFXTextField txtStudentName;
    public JFXTextField txtEmail;
    public JFXButton btnSave;
    public JFXButton btnDelete;
    public TableView<StudentTM> tblStudents;
    public JFXTextField txtNic;
    public JFXTextField txtAddress;
    public JFXTextField txtContact;
    public JFXButton btnUpdate;

    public void initialize(){
        tblStudents.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblStudents.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblStudents.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("email"));
        tblStudents.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("contact"));
        tblStudents.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblStudents.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("nic"));

        loadAllStudents();

    }

    private void loadAllStudents() {
        tblStudents.getItems().clear();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Student");

            while (rst.next()) {
                tblStudents.getItems().add(new StudentTM(rst.getString("id"), rst.getString("name"), rst.getString("email"), rst.getString("contact"), rst.getString("address"), rst.getString("nic")));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }


    }


    public void btnAddNew_OnAction(ActionEvent actionEvent) {
    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
        StudentDTO c = new StudentDTO(
                txtStudentId.getText(),txtStudentName.getText(),txtEmail.getText(),txtContact.getText(), txtAddress.getText(),txtNic.getText()

        );

        try {
            // Class.forName("com.mysql.cj.jdbc.Driver");
            //  Connection con =DriverManager.getConnection(
            //     "jdbc:mysql://localhost:3306/ThogaKade","root","1234");
            String sql = "INSERT INTO Student VALUES (?,?,?,?,?,?)";
            PreparedStatement stm= DBConnection.getInstance().getConnection().prepareStatement(sql);
            stm.setString(1,c.getId());
            stm.setString(2,c.getName());
            stm.setString(3,c.getEmail());
            stm.setString(4,c.getContact());
            stm.setString(5, c.getAddress());
            stm.setString(6, c.getAddress());

            if (stm.executeUpdate()>0){
                new Alert(Alert.AlertType.CONFIRMATION, "Saved!..").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
    }

    public void btnUpdate_OnAction(ActionEvent actionEvent) {
    }
}
