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
        tblStudents.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("student_id"));
        tblStudents.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("student_name"));
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
            System.out.println("he"+ rst);

            while (rst.next()) {
                tblStudents.getItems().add(new StudentTM(rst.getString("student_id"), rst.getString("student_name"), rst.getString("email"), rst.getString("contact"), rst.getString("address"), rst.getString("nic")));
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        tblStudents.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           // btnDelete.setDisable(newValue == null);
            btnSave.setText(newValue != null ? "Update" : "Save");
           // btnSave.setDisable(newValue == null);

            if (newValue != null) {
                txtStudentId.setText(newValue.getStudent_id());
                txtStudentName.setText(newValue.getStudent_name());
                txtEmail.setText(newValue.getEmail());
                txtContact.setText(newValue.getContact());
                txtAddress.setText(newValue.getAddress());
                txtNic.setText(newValue.getNic());

                
            }
        });

    }




    public void btnSave_OnAction(ActionEvent actionEvent) {

        String id = txtStudentId.getText();
        String name = txtStudentName.getText();
        String email = txtEmail.getText();
        String contact = txtContact.getText();
        String address = txtAddress.getText();
        String nic = txtNic.getText();

        if (!name.matches("[A-Za-z ]+")) {
            new Alert(Alert.AlertType.ERROR, "Invalid name").show();

            return;
        } else if (!address.matches(".{3,}")) {
            new Alert(Alert.AlertType.ERROR, "Address should be at least 3 characters long").show();

            return;
        }else if (!email.matches(".{3,}")) {
            new Alert(Alert.AlertType.ERROR, "Address should be at least 3 characters long").show();

            return;
        }else if (!contact.matches(".{3,}")) {
            new Alert(Alert.AlertType.ERROR, "Address should be at least 3 characters long").show();

            return;
        }else if (!nic.matches(".{3,}")) {
            new Alert(Alert.AlertType.ERROR, "Address should be at least 3 characters long").show();

            return;
        }

        if (btnSave.getText().equalsIgnoreCase("save")) {

            try {
                if (existCustomer(id)) {
                    new Alert(Alert.AlertType.ERROR, id + " already exists").show();
                }
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement pstm = connection.prepareStatement("INSERT INTO Student (student_id,student_name,email,contact, address,nic) VALUES (?,?,?,?,?,?)");
                pstm.setString(1, id);
                pstm.setString(2, name);
                pstm.setString(3, email);
                pstm.setString(4,contact );
                pstm.setString(5, address);
                pstm.setString(6, nic);
                pstm.executeUpdate();

                tblStudents.getItems().add(new StudentTM(id, name,email,contact, address,nic));
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to save the customer " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        } else {

            try {
                if (!existCustomer(id)) {
                    new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + id).show();
                }
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement pstm = connection.prepareStatement("UPDATE Student SET student_name=?, address=?,contact=?,email=?,nic=? WHERE id=?");
                pstm.setString(1, name);
                pstm.setString(2, address);
                pstm.setString(3, contact);
                pstm.setString(4, email);
                pstm.setString(5, nic);
                pstm.setString(6, id);
                pstm.executeUpdate();
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to update the customer " + id + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            StudentTM selectedCustomer = tblStudents.getSelectionModel().getSelectedItem();
            selectedCustomer.setStudent_name(name);
            selectedCustomer.setAddress(address);
            tblStudents.refresh();
        }

    }
    boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT student_id FROM Student WHERE student_id=?");
        pstm.setString(1, id);
        return pstm.executeQuery().next();
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        String id = tblStudents.getSelectionModel().getSelectedItem().getStudent_id();
        try {
            if (!existCustomer(id)) {
                new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + id).show();
            }
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM Student WHERE student_id=?");
            pstm.setString(1, id);
            pstm.executeUpdate();

            tblStudents.getItems().remove(tblStudents.getSelectionModel().getSelectedItem());
            tblStudents.getSelectionModel().clearSelection();


        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete the customer " + id).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
