package dao_pattern.dao;

import dao_pattern.connection.DatabaseConnection;
import dao_pattern.contracts.StudentDao;
import dao_pattern.models.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {
    public static final String SQL_SELECT = "SELECT * FROM students";
    public static final String SQL_INSERT = "INSERT INTO students(id, name) VALUES(?, ?)";
    public static final String SQL_UPDATE = "UPDATE students SET name = ? WHERE id = ?";
    public static final String SQL_DELETE = "DELETE FROM students WHERE id = ?";

    private List<Student> students;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public StudentDaoImpl() {
        this.students = new ArrayList<>();
    }

    @Override
    public List<Student> getAllStudents() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
        this.preparedStatement = this.connection.prepareStatement(SQL_SELECT);

        this.resultSet = this.preparedStatement.executeQuery();

        while (this.resultSet.next()){
            int id = this.resultSet.getInt("id");
            String name = this.resultSet.getString("name");

            Student student = new Student();
            student.setId(id);
            student.setName(name);


            this.students.add(student);

        }

        return this.students;
    }

    @Override
    public void insertStudent(Student student) throws SQLException {
        this.connection = DatabaseConnection.getConnection();
        this.preparedStatement = this.connection.prepareStatement(SQL_INSERT);

        int id = student.getId();
        String name = student.getName();

        this.preparedStatement.setInt(1, id);
        this.preparedStatement.setString(2, name);

        this.preparedStatement.execute();
    }

    @Override
    public void updateStudent(Student student) throws SQLException {
        this.connection = DatabaseConnection.getConnection();
        this.preparedStatement = this.connection.prepareStatement(SQL_UPDATE);

        int id = student.getId();
        String name = student.getName();

        this.preparedStatement.setString(1, name);
        this.preparedStatement.setInt(2,id);

        this.preparedStatement.execute();
    }

    @Override
    public void deleteStudent(Student student) throws SQLException {
        this.connection = DatabaseConnection.getConnection();
        this.preparedStatement = this.connection.prepareStatement(SQL_DELETE);

        int id = student.getId();

        this.preparedStatement.setInt(1,id);

        this.preparedStatement.execute();
    }

    @Override
    public void close() throws Exception {
        if(this.resultSet != null){
            this.resultSet.close();
        }

        if(this.preparedStatement != null){
            this.preparedStatement.close();
        }

        if(this.connection != null){
            this.connection.close();
        }

    }
}
