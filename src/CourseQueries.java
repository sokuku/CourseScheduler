import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CourseQueries {
    private static Connection connection;
    private static PreparedStatement addCourse;
    private static PreparedStatement getAllCourses;
    private static PreparedStatement getAllCourseCodes;
    private static PreparedStatement getCourseSeats;
    private static PreparedStatement dropCourse;
    private static ResultSet resultSet;
    
    public static ArrayList<CourseEntry> getAllCourses(String semester){
        connection = DBConnection.getConnection();
        ArrayList<CourseEntry> courseList = new ArrayList<>();
        
        try{
            String query = String.format("SELECT * FROM APP.COURSE WHERE SEMESTER='%s'", semester);
            getAllCourses = connection.prepareStatement(query);
            resultSet = getAllCourses.executeQuery();
            
            while(resultSet.next()) {
                String courseCode = resultSet.getString(2);
                String courseDesc =resultSet.getString(3);
                int numSeats = Integer.parseInt(resultSet.getString(4));
                
                CourseEntry course = new CourseEntry(semester, courseCode, courseDesc, numSeats);
                courseList.add(course);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        
        return courseList;
    }
    
    public static void addCourse(CourseEntry course){
        connection = DBConnection.getConnection();
        
        try{
            String semester = course.getSemester();
            String courseCode = course.getCourseCode();
            String courseDesc = course.getCourseDescription();
            int numSeats = course.getSeats();
            
            String query = String.format("INSERT INTO APP.COURSE VALUES ('%s','%s','%s',%d)", semester, courseCode, courseDesc, numSeats);
            addCourse = connection.prepareStatement(query);
            addCourse.executeUpdate();
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<String> getAllCourseCodes(String semester){
        connection = DBConnection.getConnection();
        ArrayList<String> courseCodes = new ArrayList<String>();
        
        try{
            String query = String.format("SELECT * FROM APP.COURSE WHERE SEMESTER='%s'", semester);
            getAllCourseCodes = connection.prepareStatement(query);
            resultSet = getAllCourseCodes.executeQuery();
            
            while(resultSet.next()) {
                String courseCode = resultSet.getString(2);
                courseCodes.add(courseCode);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        
        return courseCodes;
    }
    
    public static int getCourseSeats(String semester, String courseCode){
        connection = DBConnection.getConnection();
        int courseSeats = -1;
        
        try{
            String query = String.format("SELECT * FROM APP.COURSE WHERE SEMESTER='%s' AND COURSECODE='%s'", semester, courseCode);
            getCourseSeats = connection.prepareStatement(query);
            resultSet = getCourseSeats.executeQuery();
            
            while (resultSet.next()) {
                courseSeats = resultSet.getInt(4);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        
        return courseSeats;
    }
    
    public static void dropCourse(String semester, String courseCode){
        connection = DBConnection.getConnection();
        
        try{
            String update = String.format("DELETE FROM APP.COURSE WHERE SEMESTER='%s' AND COURSECODE='%s'", semester, courseCode);
            dropCourse = connection.prepareStatement(update);
            dropCourse.executeUpdate();
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    
}
