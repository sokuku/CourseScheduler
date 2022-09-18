import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.time.Instant;


public class ScheduleQueries {
    private static Connection connection;
    private static PreparedStatement addScheduleEntry;
    private static PreparedStatement getScheduleByStudent;
    private static PreparedStatement getScheduledStudentCount;
    private static PreparedStatement getScheduledStudentsByCourse;
    private static PreparedStatement getWaitlistedStudentsByCourse;
    private static PreparedStatement dropStudentScheduleByCourse;
    private static PreparedStatement dropScheduleByCourse;
    private static PreparedStatement updateScheduleEntry;
    private static ResultSet resultSet;
    
    public static void addScheduleEntry(ScheduleEntry entry){
        connection = DBConnection.getConnection();
        
        try{
            String sem = entry.getSemester();
            String courseCode = entry.getCourseCode();
            String studentID = entry.getStudentID();
            String status = entry.getStatus();
            Timestamp timestamp = entry.getTimestamp();
                    
            String query = String.format("INSERT INTO APP.SCHEDULE VALUES ('%s','%s','%s','%s','%s')", sem, courseCode, studentID, status, timestamp.toString());
            addScheduleEntry = connection.prepareStatement(query);
            addScheduleEntry.executeUpdate();
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<ScheduleEntry> getScheduleByStudent(String semester, String studentID){
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> schedule = new ArrayList<>();
        
        try{
            String query = String.format("SELECT * FROM APP.SCHEDULE WHERE SEMESTER='%s' AND STUDENTID='%s'", semester, studentID);
            getScheduleByStudent = connection.prepareStatement(query);
            resultSet = getScheduleByStudent.executeQuery();
            
            while(resultSet.next()) {
                String courseCode = resultSet.getString(2);
                String status = resultSet.getString(4);
                Timestamp timestamp = Timestamp.valueOf(resultSet.getString(5));
                
                ScheduleEntry entry = new ScheduleEntry(semester, courseCode, studentID, status, timestamp);
                schedule.add(entry);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        
        return schedule;
    }
    
    public static int getScheduledStudentCount(String semester, String courseCode){
        connection = DBConnection.getConnection();
        int numScheduled = 0;
        
        try{
            String query = String.format("SELECT COUNT(STUDENTID) FROM APP.SCHEDULE WHERE SEMESTER='%s' AND COURSECODE='%s' AND STATUS='S'", semester, courseCode);
            getScheduledStudentCount = connection.prepareStatement(query);
            resultSet = getScheduledStudentCount.executeQuery();
            
            while(resultSet.next()) {
                numScheduled = resultSet.getInt(1);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        
        return numScheduled;
    }
       
    public static ArrayList<ScheduleEntry> getScheduledStudentsByCourse(String semester, String courseCode){
        connection = DBConnection.getConnection();
        ArrayList <ScheduleEntry> schedules = new ArrayList<>();
        
        try{
            String query = String.format("SELECT * FROM APP.SCHEDULE WHERE SEMESTER='%s' AND COURSECODE='%s' AND STATUS='S' ORDER BY TIMESTAMP ASC", semester, courseCode);
            getScheduledStudentsByCourse = connection.prepareStatement(query);
            resultSet = getScheduledStudentsByCourse.executeQuery();
            
            while(resultSet.next()) {
                String studentID = resultSet.getString(3);
                Timestamp timestamp = Timestamp.from((Instant.now()));
                
                ScheduleEntry entry = new ScheduleEntry(semester, courseCode, studentID, "S", timestamp);
                schedules.add(entry);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        
        return schedules;
    }
    
    public static ArrayList<ScheduleEntry> getWaitlistedStudentsByCourse(String semester, String courseCode){
        connection = DBConnection.getConnection();
        ArrayList <ScheduleEntry> schedules = new ArrayList<>();
        
        try{
            String query = String.format("SELECT * FROM APP.SCHEDULE WHERE SEMESTER='%s' AND COURSECODE='%s' AND STATUS='W' ORDER BY TIMESTAMP ASC", semester, courseCode);
            getWaitlistedStudentsByCourse = connection.prepareStatement(query);
            resultSet = getWaitlistedStudentsByCourse.executeQuery();
            
            while(resultSet.next()) {
                String studentID = resultSet.getString(3);
                Timestamp timestamp = Timestamp.from((Instant.now()));
                
                ScheduleEntry entry = new ScheduleEntry(semester, courseCode, studentID, "W", timestamp);
                schedules.add(entry);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        
        return schedules;
    }
    
    public static void dropStudentScheduleByCourse(String semester, String studentID, String courseCode){
        connection = DBConnection.getConnection();
        
        try{
            String update = String.format("DELETE FROM APP.SCHEDULE WHERE SEMESTER='%s' AND COURSECODE='%s' AND STUDENTID='%s'", semester, courseCode, studentID);
            dropStudentScheduleByCourse = connection.prepareStatement(update);
            dropStudentScheduleByCourse.executeUpdate();
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    
    public static void dropScheduleByCourse(String semester, String courseCode){
        connection = DBConnection.getConnection();
        
        try{
            String update = String.format("DELETE FROM APP.SCHEDULE WHERE SEMESTER='%s' AND COURSECODE='%s'", semester, courseCode);
            dropScheduleByCourse = connection.prepareStatement(update);
            dropScheduleByCourse.executeUpdate();
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    
    public static void updateScheduleEntry(String semester, ScheduleEntry entry){
        connection = DBConnection.getConnection();
        
        try{
            String courseCode = entry.getCourseCode();
            String studentID = entry.getStudentID();
            Timestamp timestamp = Timestamp.from(Instant.now());
            
            dropStudentScheduleByCourse(semester, studentID, courseCode);
            String update = String.format("INSERT INTO APP.SCHEDULE VALUES ('%s','%s','%s','S','%s')", semester, courseCode, studentID, timestamp);
            updateScheduleEntry = connection.prepareStatement(update);
            updateScheduleEntry.executeUpdate();
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
}
