import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentQueries {
    private static Connection connection;
    private static PreparedStatement addStudent;
    private static PreparedStatement getAllStudents;
    private static PreparedStatement getStudent;
    private static PreparedStatement dropStudent;
    private static ResultSet resultSet;
    
    public static void addStudent(StudentEntry student){
        connection = DBConnection.getConnection();
        
        try{
            String studentID = student.getStudentID();
            String firstName = student.getFirstName();
            String lastName = student.getLastName();
            String query = String.format("INSERT INTO APP.STUDENT VALUES ('%s','%s','%s')", studentID, firstName, lastName);
            addStudent = connection.prepareStatement(query);
            addStudent.executeUpdate();
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<StudentEntry> getAllStudents(){
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> studentList = new ArrayList<>();
        
        try{
            getAllStudents = connection.prepareStatement("SELECT * FROM APP.STUDENT");
            resultSet = getAllStudents.executeQuery();
            
            while(resultSet.next()) {
                String studentID = resultSet.getString(1);
                String firstName = resultSet.getString(2);
                String lastName =resultSet.getString(3);
                
                StudentEntry student = new StudentEntry(studentID, firstName, lastName);
                studentList.add(student);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        
        return studentList;
    }
    
    public static StudentEntry getStudent(String studentID){
        connection = DBConnection.getConnection();
        StudentEntry student = null;
        
        try{
            String query = String.format("SELECT * FROM APP.STUDENT WHERE STUDENTID='%s'", studentID);
            getStudent = connection.prepareStatement(query);
            resultSet = getStudent.executeQuery();
            while(resultSet.next()) {
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
            
                student = new StudentEntry(studentID, firstName, lastName);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        
        return student;
    }
    
    public static void dropStudent(String studentID){
        connection = DBConnection.getConnection();
        
        try{
            String update = String.format("DELETE FROM APP.STUDENT WHERE STUDENTID='%s'", studentID);
            dropStudent = connection.prepareStatement(update);
            dropStudent.executeUpdate();
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    
}
