package dao_pattern;

import dao_pattern.contracts.StudentDao;
import dao_pattern.dao.StudentDaoImpl;
import dao_pattern.models.Student;

import java.sql.SQLException;


public class Demo {

    public static void main(String[] args) {

        try(StudentDao studentDao = new StudentDaoImpl()){
            studentDao.getAllStudents().forEach(f -> {
                Student student = new Student();
                student.setId(f.getId());

                if(f.getId() == 1){
                    student.setName("Kiro");
                    student.setId(f.getId());
                    try {
                        studentDao.updateStudent(student);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
