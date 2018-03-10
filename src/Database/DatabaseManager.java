/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.transformation.SortedList;
import javax.swing.JTable;
import org.javatuples.Triplet;
/**
 *
 * @author arun
 */
public class DatabaseManager {
    
    private String szDriver          = null;
    private String szDomainAddress   = null;    
    private String szUserName        = null;
    private String szDomainPassword  = null;    
    
    // connection and query execution variables.
    private Connection          con  = null;
    private PreparedStatement   ps   = null;
    private ResultSet           rs   = null;

    
    
    public DatabaseManager() throws ClassNotFoundException, SQLException 
    {
        szDriver          = "com.mysql.jdbc.Driver";
        szDomainAddress   = "jdbc:mysql://localhost:3306/ptassistant";
        szUserName        = "root";
        szDomainPassword  = "";
        SetConnection();        
    } 
    //********************************************************connection********************************
    // function create a my sql connection
    private void  SetConnection()
    {
        try
        {
            // creating a database connection.
            Class.forName( szDriver );
            con=DriverManager.getConnection( szDomainAddress, szUserName, szDomainPassword );
        }
        // database connection error
        catch( SQLException ex )
        {
            JOptionPane.showMessageDialog(null, " Please check the database connection ");
        }
        // driver error
        catch( ClassNotFoundException ex )
        {
            JOptionPane.showMessageDialog(null, " Database driver problem  ");
        }
        
    }    
    // function to close the connection
    public void closeConection() throws SQLException
    {
        con.close();
    }
    
    // ********************************************** Select ***************************************************************
    
     // if both matches it returns user type otherwise return null.
    public ResultSet getUserWith( String szUserName /*IN*/ , String szPassword /*IN*/ )
    {
    
        String szQuery         = null; // sql query string
        
        // validation of empty username and password
        if( null != szUserName && null != szPassword )
        {
            try {
                
                // sql query to check user name and password
                szQuery = " select  * from User_Details_Table where Username = ? and Password = ? ";
                // prepare sql query
                ps = con.prepareStatement( szQuery );
                ps.setString( 1, szUserName );
                ps.setString( 2, szPassword );
                // get result of query
                rs = ps.executeQuery();
                
                
            
            
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        return rs;
    }
    
    public ResultSet selectAllDepartments(){
        
        try {
                
            // sql query to check user name and password
            String szQuery = "select  * from Department_Table";
            ps = con.prepareStatement( szQuery );
            // get result of query
            rs = ps.executeQuery();

            
            
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return rs;
    }
    
    public boolean updateDepartment(int deptId,String deptName,int maxStrength)
    {
        boolean bStatus = false;
        String query = "update department_table set Department_Name = ?, Max_Strength = ?"
                + " where Department_ID = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, deptName);
            ps.setInt(2, maxStrength);
            ps.setInt(3, deptId);
            
            if(ps.executeUpdate() > 0)
            {
                bStatus = true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return bStatus;
    }
    
    /// <summary>
    /// seects the department name for a given department id
    /// </summary>  
    public String SelectDepartmentName(int departmentID)
    {
        String name = null;
        try 
        {
             
            String szQuery = "SELECT  Department_Name from Department_Table  WHERE Department_ID = ?";
            ps = con.prepareStatement( szQuery );
            ps.setInt(1, departmentID);
            // get result of query
            rs = ps.executeQuery();        
            if(rs.next())
            {
                name = rs.getString("Department_Name");
            }
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return name;
    }
    
     /// <summary>
    /// selects the batch name for a given department id
    /// </summary>  
    public ResultSet SelectBatchName(int departmentID)
    {
        String name = null;
        try 
        {
             
            String szQuery = "SELECT  Batch_Name from Batch_Table  WHERE Batch_Department_ID = ?";
            ps = con.prepareStatement( szQuery );
            ps.setInt(1, departmentID);
            // get result of query
            rs = ps.executeQuery();  
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rs;
    }
    
    // select all batch
    public ResultSet selectAllBatches(){
        
        try {
                
            // sql query to check user name and password
            String szQuery = "select  * from Batch_Table";
            ps = con.prepareStatement( szQuery );
            // get result of query
            rs = ps.executeQuery();

            
            
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return rs;
    }
    
    // select all hod
    public ResultSet selectAllHod(){
        try {
                
            // sql query to check user name and password
            String szQuery = "select  h.HOD_ID,h.HOD_RegID,h.HOD_Name,h.HOD_Details,d.Department_Name from hod_master_table h,department_table d where h.HOD_Department_ID = d.Department_ID";
            ps = con.prepareStatement( szQuery );
            // get result of query
            rs = ps.executeQuery();

            
            
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return rs;
    }
    
     public ResultSet selectHodById(int hodId){
        try {
                
            // sql query to check user name and password
            String szQuery = "select  h.HOD_ID,h.HOD_RegID,h.HOD_Name,h.HOD_Details,h.HOD_DOB,h.HOD_Join_Date,h.HOD_Qualification,d.Department_Name from hod_master_table h,department_table d where h.HOD_Department_ID = d.Department_ID and h.HOD_ID = ?";
            ps = con.prepareStatement( szQuery );
            ps.setInt(1, hodId);
            // get result of query
            rs = ps.executeQuery();

            
            
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return rs;
    }
    public ResultSet selectTeacherById(int Id){
        try {
                
            // sql query to check user name and password
            String szQuery = "select  h.Teacher_ID,h.Teacher_RegID,h.Teacher_Name,h.Teacher_Details,h.Teacher_DOB,h.Teacher_Join_Date,h.Teacher_Qualification,d.Department_Name from teacher_master_table h,department_table d where h.Teacher_Department_ID = d.Department_ID and h.Teacher_ID = ?";
            ps = con.prepareStatement( szQuery );
            ps.setInt(1, Id);
            // get result of query
            rs = ps.executeQuery();

            
            
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return rs;
    } 
    
    public ResultSet selectAllTeachers(){
        
       try {
                
            // sql query to check user name and password
            String szQuery = "select  t.Teacher_ID,t.Teacher_RegID,t.Teacher_Name,t.Teacher_Details,d.Department_Name from teacher_master_table t,department_table d where t.Teacher_Department_ID = d.Department_ID";
            ps = con.prepareStatement( szQuery );
            // get result of query
            rs = ps.executeQuery();

            
            
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return rs; 
    }
    
    public ResultSet selectAllStudents(){
        
        try{
           // sql query to check user name and password
            String szQuery = "select  s.Student_ID,s.Student_RegID,s.Student_Name,s.Student_Details,s.Student_Semester,d.Department_Name,b.Batch_Name from student_master_table s,department_table d,batch_table b where s.Student_Department_ID = d.Department_ID and b.Batch_Id = s.Student_Batch_ID";
            ps = con.prepareStatement( szQuery );
            // get result of query
            rs = ps.executeQuery();
 
            
        }catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    public ResultSet selectAllStudents(int batchId){
        
        try{
           // sql query to check user name and password
            String szQuery = "select  distinct s.Student_ID,s.Student_RegID,s.Student_Name,s.Student_Details,s.Student_Semester,d.Department_Name,b.Batch_Name from student_master_table s,department_table d,batch_table b where s.Student_Department_ID = d.Department_ID and s.Student_Batch_ID = b.Batch_ID and s.Student_Batch_ID = ?";
            ps = con.prepareStatement( szQuery );
            // get result of query
            ps.setInt(1, batchId);
            rs = ps.executeQuery();
 
            
        }catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    public ResultSet selectAllStudentsWithDept(int deptId){
        
        try{
           // sql query to check user name and password
            String szQuery = "select  s.Student_ID,s.Student_RegID,s.Student_Name,s.Student_Details,s.Student_Semester,d.Department_Name,b.Batch_Name from student_master_table s,department_table d,batch_table b where s.Student_Department_ID = ? and s.Student_Department_ID = d.Department_ID and b.Batch_Id = s.Student_Batch_ID";
            ps = con.prepareStatement( szQuery );
            ps.setInt(1, deptId);
            // get result of query
            rs = ps.executeQuery();
 
            
        }catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    // get dept id from name    
    public int getDepartmentIdWithName(String deptName) {
        
        
            int deptId = 0;
        try {
                
            // sql query to check user name and password
            String szQuery = "select  Department_ID from Department_Table where Department_Name = ?";
            ps = con.prepareStatement( szQuery );
             ps.setString( 1, deptName );
            // get result of query
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                
                if(rs.next()){
                    deptId = rs.getInt("Department_ID");
                }
            }

            
            
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return deptId;
       
    }
    
    public int getBatchIdFromBatchName(String batchName) {
        
        
            int batchId = 0;
        try {
                
            // sql query to check user name and password
            String szQuery = "select  Batch_ID from Batch_Table where Batch_Name = ?";
            ps = con.prepareStatement( szQuery );
             ps.setString( 1, batchName );
            // get result of query
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                
                if(rs.next()){
                    batchId = rs.getInt("Batch_ID");
                }
            }

            
            
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return batchId;
       
    }
    
    
    //************************************************ insert *****************************************
    
    // add new batch to db
    public boolean addNewBatch(String batchName, String startsOn, String endsOn, int batchDeptId,int studentsNumber ){
        
            boolean bStatus = false;
            String query = "insert into Batch_Table(Batch_Name, Batch_Starts_On, Batch_Ends_On,Batch_Department_ID,Max_Students) values(?,?,?,?,?)";
            try {
                ps = con.prepareStatement( query );
                ps.setString(1, batchName);
                ps.setString(2, startsOn);                
                ps.setString(3, endsOn);
                ps.setInt(4, batchDeptId);
                ps.setInt(5, studentsNumber);
                
                
                // check insertion sucessfull or not
                if( ps.executeUpdate() > 0){
                    bStatus = true;
                }

                closeConection();

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
       
       
       return bStatus; 
    }
    
    public boolean updateBatch(int batchId, String batchName, String startsOn, String endsOn, int studentsNumber ){
        
            boolean bStatus = false;
            String query = "update Batch_Table set Batch_Name = ?, Batch_Starts_On = ?, Batch_Ends_On = ?,Max_Students = ?"
                          + " where Batch_ID = ?";
            try {
                ps = con.prepareStatement( query );
                ps.setString(1, batchName);
                ps.setString(2, startsOn);                
                ps.setString(3, endsOn);       
                ps.setInt(4, studentsNumber);
                ps.setInt(5, batchId);
                
                
                // check insertion sucessfull or not
                if( ps.executeUpdate() > 0){
                    bStatus = true;
                }

                closeConection();

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
       
       
       return bStatus; 
    }
    
    // add new department to db
    public boolean addDepartment(String departmentName,int maxStrength){
        
        boolean bStatus = false;
            String query = "insert into Department_Table(Department_Name,Max_Strength) values(?,?)";
            try {
                ps = con.prepareStatement( query );
                ps.setString(1, departmentName);
                ps.setInt(2, maxStrength);
                // check insertion sucessfull or not
                if( ps.executeUpdate() > 0){
                    bStatus = true;
                }

                closeConection();

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage());
            } 
            
            return bStatus;
    }
    
    // add new department to db
    public boolean addHODWith(String hodRegId, String hodName, String hodInfo, int hodDeptId, 
            String strDob,String joiningDate,String hodqualification){
        
        boolean bStatus = false;
            String query = "insert into HOD_Master_Table(HOD_RegID,HOD_Name,HOD_Details,HOD_Department_ID,HOD_DOB,HOD_Join_Date,HOD_Qualification) values(?,?,?,?,?,?,?)";
            try {
                ps = con.prepareStatement( query );
                ps.setString(1, hodRegId);
                ps.setString(2, hodName);
                ps.setString(3, hodInfo);
                ps.setInt(4, hodDeptId);
                ps.setString(5, strDob);
                ps.setString(6, joiningDate);
                ps.setString(7, hodqualification);
                                
                // check insertion sucessfull or not
                if( ps.executeUpdate() > 0){
                    
                    query = "insert into User_Details_Table(Username,Password,Previlege) values(?,?,?)";
                    ps = con.prepareStatement( query );
                    ps.setString(1,"h"+hodRegId);
                    ps.setString(2,"hod");
                    ps.setString(3,"hod");
                    
                    if( ps.executeUpdate() > 0){
                      bStatus = true;  
                    }
                }

                closeConection();

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage());
            } 
            
            return bStatus;
    }
    
    // add new teacher to db
    public boolean addTeacherWith(String teacherRegId, String teacherName, String teacherInfo, int teacherDeptId,String dob,String joinDate,String qualification){
        
        boolean bStatus = false;
            String query = "insert into Teacher_Master_Table(Teacher_RegID,Teacher_Name,Teacher_Details,Teacher_Department_ID,Teacher_DOB,Teacher_Join_Date,Teacher_Qualification) values(?,?,?,?,?,?,?)";
            try {
                ps = con.prepareStatement( query );
                ps.setString(1, teacherRegId);
                ps.setString(2, teacherName);
                ps.setString(3, teacherInfo);
                ps.setInt(4, teacherDeptId);
                ps.setString(5, dob);
                ps.setString(6, joinDate);
                ps.setString(7, qualification);
                                
                // check insertion sucessfull or not
                if( ps.executeUpdate() > 0){
                    query = "insert into User_Details_Table(Username,Password,Previlege) values(?,?,?)";
                    ps = con.prepareStatement( query );
                    ps.setString(1,"t"+teacherRegId);
                    ps.setString(2,"teacher");
                    ps.setString(3,"teacher");
                    
                    if( ps.executeUpdate() > 0){
                      bStatus = true;  
                    }
                }

                closeConection();

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage());
            } 
            
            return bStatus;
    }
    
    // add new student to db
    public boolean addStudentWith(String studentRegId, String studentName, String studentInfo, int studentDeptId, int studentBatchId){
        
        boolean bStatus = false;
            String query = "insert into Student_Master_Table(Student_RegID,Student_Name,Student_Details,Student_Department_ID,Student_Batch_ID) values(?,?,?,?,?)";
            try {
                ps = con.prepareStatement( query );
                ps.setString(1, studentRegId);
                ps.setString(2, studentName);
                ps.setString(3, studentInfo);
                ps.setInt(4, studentDeptId);
                ps.setInt(5, studentBatchId);
                                
                // check insertion sucessfull or not
                if( ps.executeUpdate() > 0){
                    
                    query = "insert into User_Details_Table(Username,Password,Previlege) values(?,?,?)";
                    ps = con.prepareStatement( query );
                    ps.setString(1,"s"+studentRegId);
                    ps.setString(2,"student");
                    ps.setString(3,"student");
                    
                    if( ps.executeUpdate() > 0){
                      bStatus = true;  
                    }
                    
                }

                closeConection();

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage());
            } 
            
            return bStatus;
    }
    
    
//************************************************** delete queries ******************
    // remove selected batches.
    public boolean deleteSelectedBatch(int[] selectedDepartmentId){
        
        boolean bStatus = false;
        for(int batchId : selectedDepartmentId ){
            
           String query = "delete from Batch_Table where Batch_ID = ?";
            try {
                ps = con.prepareStatement( query );
                ps.setInt(1, batchId);
                if(ps.executeUpdate() > 0 ){
                    bStatus = true;
                }
                
            }catch (SQLException ex) {
                
                if(ex.getSQLState().startsWith("23")){
                    
                    JOptionPane.showMessageDialog(null, "Delete child data first..");
                }
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
          
        }
        return bStatus;
    }
    
    // remove selected dept
    public boolean deleteSelectedDept(String[] selectedId){
        
        boolean bStatus = false;
        for(String Id : selectedId ){
            
           String query = "delete from department_table where Department_ID = ?";
            try {
                ps = con.prepareStatement( query );
                ps.setString(1, Id);
                if(ps.executeUpdate() > 0 ){
                    bStatus = true;
                }
                
            }catch (SQLException ex) {
                
                if(ex.getSQLState().startsWith("23")){
                    
                    JOptionPane.showMessageDialog(null, "Delete child data first..");
                }
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
          
        }
        return bStatus;
    }
    
    // remove selected dept
    public boolean deleteSelectedTeachers(String[] selectedId){
        
        boolean bStatus = false;
        for(String Id : selectedId ){
            
           String query = "delete from teacher_master_table where Teacher_RegID = ?";
            try {
                ps = con.prepareStatement( query );
                ps.setString(1, Id);
                if(ps.executeUpdate() > 0 ){
                    
                  query = "delete from user_details_table where Username = ?";  
                  ps = con.prepareStatement( query );
                  ps.setString(1, ("t"+Id));
                  if(ps.executeUpdate() > 0 ){
                      
                     bStatus = true;
                      
                  }
                    
                }
                
            }catch (SQLException ex) {
                
                if(ex.getSQLState().startsWith("23")){
                    
                    JOptionPane.showMessageDialog(null, "Delete child data first..");
                }
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
          
        }
        return bStatus;
    }
    
    
    
    public boolean deleteSelectedTeachers(String selectedId){
        
        boolean bStatus = false;
            
           String query = "delete from teacher_master_table where Teacher_RegID = ?";
            try {
                ps = con.prepareStatement( query );
                ps.setString(1, selectedId);
                if(ps.executeUpdate() > 0 ){
                    
                  query = "delete from user_details_table where Username = ?";  
                  ps = con.prepareStatement( query );
                  ps.setString(1, ("t"+selectedId));
                  if(ps.executeUpdate() > 0 ){
                      
                     bStatus = true;
                      
                  }
                    
                }
                
            }catch (SQLException ex) {
                
                if(ex.getSQLState().startsWith("23")){
                    
                    JOptionPane.showMessageDialog(null, "Delete child data first..");
                }
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
          
        return bStatus;
    }
    
    
    // remove selected hod
    public boolean deleteSelectedStudents(String[] selectedId){
        
        boolean bStatus = false;
        for(String Id : selectedId ){
           System.out.println(Id); 
           String query = "delete from student_master_table where Student_ID = ?";
            try {
                ps = con.prepareStatement( query );
                ps.setString(1, Id);
                if(ps.executeUpdate() > 0 ){
                    
                  query = "delete from user_details_table where Username = ?";  
                  ps = con.prepareStatement( query );
                  ps.setString(1, ("s"+Id));
                  if(ps.executeUpdate() > 0 ){
                      
                     bStatus = true;
                      
                  }
                    
                }
                
            }catch (SQLException ex) {
                
                if(ex.getSQLState().startsWith("23")){
                    
                    JOptionPane.showMessageDialog(null, "Delete related data first..");
                }
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
          
        }
        return bStatus;
    }
    
    // remove selected hod
    public boolean removeHod(String selectedHodId){
        
        boolean bStatus = false;
            
           String query = "delete from hod_master_table where HOD_RegID = ?";
            try {
                ps = con.prepareStatement( query );
                ps.setString(1, (selectedHodId));
                if(ps.executeUpdate() > 0 ){
                    
                  query = "delete from user_details_table where Username = ?";  
                  ps = con.prepareStatement( query );
                  ps.setString(1, ("h"+selectedHodId));
                  if(ps.executeUpdate() > 0 ){
                      
                     bStatus = true;
                      
                  }
                    
                }
                
            }catch (SQLException ex) {
                
                if(ex.getSQLState().startsWith("23")){
                    
                    JOptionPane.showMessageDialog(null, "Delete related data first..");
                }
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        return bStatus;
    }
    
    
     //*****************HOD operations**********************
   // Adding subject
    public boolean AddSubject(String subjectID, String subjectName, String department, String semester)
    {
        boolean status = false;
        String query = "INSERT INTO Subject_Table(Subject_ID, Subject_Name, Subject_Semester, Subject_Department_ID)"
                      +" VALUES(?, ?, ?, ?)";
        //String batchID = String.valueOf(this.getBatchIdFromBatchName(batch));
        String departmentID = String.valueOf(this.getDepartmentIdWithName(department));
        try 
        {
            ps = con.prepareStatement(query);
            ps.setString(1, subjectID);
            ps.setString(2, subjectName);
            ps.setString(3, semester);
            //ps.setString(4, batchID);
            ps.setString(4, departmentID);
                                
            // Check insertion was successfull or not
            if( 1 == ps.executeUpdate())
            {
                status = true;
            }

            closeConection();

        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } 
            
        return status;
    }
    
    public boolean UpdateSubject(String oldID, String subjectID, String subjectName, String department, String semester)
    {
        boolean status = false;
        String query = "UPDATE Subject_Table SET Subject_ID = ?, Subject_Name = ?, Subject_Semester = ?, Subject_Department_ID = ?"
                      +" WHERE Subject_ID = ?";
        //String batchID = String.valueOf(this.getBatchIdFromBatchName(batch));
        String departmentID = String.valueOf(this.getDepartmentIdWithName(department));
        try 
        {
            ps = con.prepareStatement(query);
            ps.setString(1, subjectID);
            ps.setString(2, subjectName);
            ps.setString(3, semester);   
            ps.setString(4, departmentID);
            ps.setString(5, oldID);             
            // Check insertion was successfull or not
            if( 1 == ps.executeUpdate())
            {
                status = true;
            }

            closeConection();

        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } 
            
        return status;
    }
    
    /// <summary>
    /// Retrieving the complete subject details
    /// </summary>    
    public ResultSet RetrieveSubjectDetails(int departmentID)
    {
        try 
        {               
            String qery = "SELECT  * FROM Subject_Table where Subject_Department_ID = ?";
            ps = con.prepareStatement(qery);
            ps.setInt(1, departmentID);
            rs = ps.executeQuery(); 
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return rs;
    }
    
    /// <summary>
    /// Retrieving the complete subject details for a given subject name
    /// </summary>  
    public ResultSet RetrieveSubjectDetails(String subjectName)
    {
            
        String Query = "SELECT * FROM Subject_Table WHERE Subject_Name LIKE ?";       
        rs = null;
        try 
        {
            ps=con.prepareStatement(Query);
            ps.setString( 1, subjectName + '%' );            
            rs=ps.executeQuery();

        } catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } 
        
        return rs;
    }
    
    /// <summary>
    /// Retrieving the complete Teacher details
    /// </summary>    
    public ResultSet RetrieveTeacherDetails()
    {
        try 
        {               
            String qery = "SELECT  * FROM Teacher_Master_Table";
            ps = con.prepareStatement(qery);
            rs = ps.executeQuery(); 
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return rs;
    }
    
    /// <summary>
    /// Retrieving the complete subject to teacher allotment
    /// </summary>  
    public ResultSet RetrieveSubjectToTeacherAllotmentDetails()
    {
            
        String Query = "SELECT * FROM Assign_Subject_To_Teacher_Table";       
        rs = null;
        try 
        {
            ps=con.prepareStatement(Query);           
            rs=ps.executeQuery();

        } catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } 
        
        return rs;
    }
    
    /// <summary>
    /// Retrieving the complete subject to teacher allotment on the basis of subjectID
    /// </summary>  
    public ResultSet RetrieveSubjectToTeacherAllotmentDetails(int subjectID)
    {
            
        String Query = "SELECT * FROM Assign_Subject_To_Teacher_Table WHERE Assign_Subject_ID = ?";       
        rs = null;
        try 
        {
            ps=con.prepareStatement(Query);  
            ps.setInt(1, subjectID);
            rs=ps.executeQuery();

        } catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } 
        
        return rs;
    }
    
    /// <summary>
    /// Retrieving the complete subject to teacher allotment on the basis of subjectID or teacherID
    /// </summary>  
    public ResultSet RetrieveSubjectToTeacherAllotmentDetails(int id, String basisOf)
    {   
        String Query = null;
        if(basisOf.equals("Subject"))
        {
            Query = "SELECT * FROM Assign_Subject_To_Teacher_Table WHERE Assign_Subject_ID = ?";       
            rs = null;
                try 
                {
                    ps=con.prepareStatement(Query);  
                    ps.setInt(1, id);
                    rs=ps.executeQuery();

                } catch (SQLException ex) 
                {
                    Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                } 
        }
        else if(basisOf.equals("Teacher"))
        {
            Query = "SELECT * FROM Assign_Subject_To_Teacher_Table WHERE Assign_Teacher_ID = ?";       
            rs = null;
                try 
                {
                    ps=con.prepareStatement(Query);  
                    ps.setInt(1, id);
                    rs=ps.executeQuery();

                } catch (SQLException ex) 
                {
                    Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                } 
        }         
        return rs;
    }
    
    /// <summary>
    /// Retrieving the complete teacher details for a given departmentID 
    /// </summary>  
    public ResultSet RetrieveTeacherDetails(int departmentID)
    {
            
        String Query = "SELECT * FROM Teacher_Master_Table WHERE Teacher_Department_ID = ?";       
        rs = null;
        try 
        {
            ps=con.prepareStatement(Query);
            ps.setInt(1, departmentID);           
            rs=ps.executeQuery();

        } catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } 
        
        return rs;
    }
    
    /// <summary>
    /// Retrieving the complete subject details for a given teacher name
    /// </summary>  
    public ResultSet RetrieveTeacherDetails(String teacherName)
    {
            
        String Query = "SELECT * FROM Subject_Table WHERE Subject_Name LIKE ?";       
        rs = null;
        try 
        {
            ps=con.prepareStatement(Query);
            ps.setString( 1, teacherName + '%' );            
            rs=ps.executeQuery();

        } catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } 
        
        return rs;
    }
    
    /// <summary>
    /// Retrieving the complete teacher name for a given teacherID 
    /// </summary>  
    public String GetTeacherName(String teacherID)
    {
            
        String Query = "SELECT Teacher_Name FROM Teacher_Master_Table WHERE Teacher_ID = ?";   
        String name = null;
        rs = null;
        try 
        {
            ps=con.prepareStatement(Query);
            ps.setString(1, teacherID);           
            rs=ps.executeQuery();
            if(rs.isBeforeFirst())
            {
                if(rs.next()){
                    name = rs.getString("Teacher_Name");
                }
            }

        } catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } 
        
        return name;
    }
    
    /// <summary>
    /// Retrieving the subject name for a given subjectID 
    /// </summary>  
    public String GetSubjectName(String subjectID)
    {
            
        String Query = "SELECT Subject_Name FROM Subject_Table WHERE Subject_ID = ?";   
        String name = null;
        rs = null;
        try 
        {
            ps=con.prepareStatement(Query);
            ps.setString(1, subjectID);           
            rs=ps.executeQuery();
            if(rs.isBeforeFirst())
            {
                if(rs.next()){
                    name = rs.getString("Subject_Name");
                }
            }

        } catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } 
        
        return name;
    }
    
    /// <summary>
    /// Retrieving the batch name for a given batchID 
    /// </summary>  
    public String GetBatchName(String batchID)
    {
            
        String Query = "SELECT Batch_Name FROM Batch_Table WHERE Batch_ID = ?";   
        String name = null;
        rs = null;
        try 
        {
            ps=con.prepareStatement(Query);
            ps.setString(1, batchID);           
            rs=ps.executeQuery();
            if(rs.isBeforeFirst())
            {
                if(rs.next()){
                    name = rs.getString("Batch_Name");
                }
            }

        } catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } 
        
        return name;
    }
    
    /// <summary>
    /// Retrieving the department name for a given departmentID 
    /// </summary>  
    public String GetDepartmentName(String departmentID)
    {
            
        String Query = "SELECT Department_Name FROM Department_Table WHERE Department_ID = ?";   
        String name = null;
        rs = null;
        try 
        {
            ps=con.prepareStatement(Query);
            ps.setString(1, departmentID);           
            rs=ps.executeQuery();
            if(rs.isBeforeFirst())
            {
                if(rs.next()){
                    name = rs.getString("Department_Name");
                }
            }

        } catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } 
        
        return name;
    }    
    
    /// <summary>
    /// Gets the departmentID for a given subjectID
    /// </summary>  
    public int GetDepartMentID(int subjectID)
    {
        int id =0;   
        String Query = "SELECT Subject_Department_ID FROM Subject_Table WHERE Subject_ID = ?";       
        rs = null;
        try 
        {
            ps=con.prepareStatement(Query);
            ps.setInt(1, subjectID);           
            rs=ps.executeQuery();
            if(rs.isBeforeFirst())
            {
                if(rs.next()){
                    id = rs.getInt("Subject_Department_ID");
                }
            }

        } catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } 
        
        return id;
    }
    
    /// <summary>
    /// Gets the departmentID for a given subjectID
    /// </summary>  
    public boolean AssignSubjectToTeacher(int subjectID, int teacherID)
    {
        boolean status = false;
        String query = "INSERT INTO Assign_Subject_To_Teacher_Table(Assign_Subject_ID, Assign_Teacher_ID)"
                      +" VALUES(?, ?)";
        try 
        {
            ps = con.prepareStatement(query);
            ps.setInt(1, subjectID);
            ps.setInt(2, teacherID);
                                
            // Check insertion was successfull or not
            if( 1 == ps.executeUpdate())
            {
                status = true;
            }

            closeConection();

        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } 
            
        return status;
    }
    
    // Deleting the subject
    public boolean DeleteSubject(int subjectID)
    {
        boolean status = false;
        int count = 0;
        int subjectDeleteStatus = 0; 
        String queryAllotment = "DELETE FROM Assign_Subject_To_Teacher_Table WHERE Assign_Subject_ID = ?";
        String querySubject = "DELETE FROM Subject_Table WHERE Subject_ID = ?";
        String commonQuery = null;
        try 
        {
            commonQuery = " SELECT COUNT(*) as Count FROM Assign_Subject_To_Teacher_Table WHERE Assign_Subject_ID = ?";
            ps = con.prepareStatement(commonQuery);   
            ps.setInt(1, subjectID);
            rs = ps.executeQuery();            
            if (rs.next())
            {
                count = Integer.parseInt(rs.getString("Count"));
                if (0 == count)
                {                    
                    ps = con.prepareStatement(querySubject);   
                    ps.setInt(1, subjectID);
                    subjectDeleteStatus = ps.executeUpdate();                                       
                }
                else
                {
                    ps = con.prepareStatement(queryAllotment);   
                    ps.setInt(1, subjectID);
                    int delCount = ps.executeUpdate();
                    if (delCount == count)
                    {
                        ps = con.prepareStatement(querySubject);   
                        ps.setInt(1, subjectID);
                        subjectDeleteStatus = ps.executeUpdate();
                    }
                }
            }                  
            // Check insertion was successfull or not
            if( 1 == subjectDeleteStatus)
            {
                status = true;
            }

            closeConection();

        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } 
            
        return status;
    }
    
    /// <summary>
    /// Deletes the entry from allotment table
    /// </summary>
    public boolean DeleteAllotment(int assignID)
    {
        boolean status = false;
        String query = "DELETE FROM Assign_Subject_To_Teacher_Table where Assign_ID = ?";
        try 
        {
                ps = con.prepareStatement( query );
                ps.setInt(1, assignID);
                if(ps.executeUpdate() > 0 )
                {
                    status = true;
                }
                
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }
    
    
    public int getHodIdFromRegId(String regId){
        
        int hodId = 0;
        try {
                
            // sql query to check user name and password
            String szQuery = "select  HOD_ID from hod_master_table where HOD_RegID = ?";
            ps = con.prepareStatement( szQuery );
             ps.setString( 1, regId );
            // get result of query
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                
                if(rs.next()){
                    hodId = rs.getInt("HOD_ID");
                }
            }

            
            
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return hodId;
        
    }
    
    /// <summary>
    /// Gets the hodDepartmentID for a given hodID
    /// </summary> 
    public int getHodDepartmentIdFromHODId(int hodID){
        
        int deptId = 0;
        try {
                
            // sql query to check user name and password
            String szQuery = "select  HOD_Department_ID from hod_master_table where HOD_ID = ?";
            ps = con.prepareStatement( szQuery );
             ps.setInt( 1, hodID );
            // get result of query
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                
                if(rs.next()){
                    deptId = rs.getInt("HOD_Department_ID");
                }
            }

            
            
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return deptId;
        
    }
    
    /// <summary>
    /// Gets the studentDepartmentID for a given studentID
    /// </summary> 
    public int getStudentDepartmentIdFromStudentId(int studentID){
        
        int deptId = 0;
        try {
                
            // sql query to check user name and password
            String szQuery = "SELECT  Student_Department_ID FROM Student_Master_Table WHERE Student_ID = ?";
            ps = con.prepareStatement( szQuery );
             ps.setInt( 1, studentID );
            // get result of query
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                
                if(rs.next()){
                    deptId = rs.getInt("Student_Department_ID");
                }
            }

            
            
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return deptId;
        
    }
    
    //*********************************************Teacher details*******************************************************   
   
    public int getTeacherIdFromRegId(String regId){
        
        int teacherId = 0;
        try {
                
            // sql query to check user name and password
            String szQuery = "select  Teacher_ID from teacher_master_table where Teacher_RegID = ?";
            ps = con.prepareStatement( szQuery );
             ps.setString( 1, regId );
            // get result of query
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                
                if(rs.next()){
                    teacherId = rs.getInt("Teacher_ID");
                }
            }

            
            
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return teacherId;
        
    }
    
    public int getTeacherRegIdFromTeacherID(int id){
        
        int teacherRegId = 0;
        try {
                
            // sql query to check user name and password
            String szQuery = "select  Teacher_RegID from teacher_master_table where Teacher_ID = ?";
            ps = con.prepareStatement( szQuery );
             ps.setInt( 1, id );
            // get result of query
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                
                if(rs.next()){
                    teacherRegId = rs.getInt("Teacher_RegID");
                }
            }

            
            
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return teacherRegId;
        
    }
    
    
    /// <summary>
    /// Gets the hodDepartmentID for a given hodID
    /// </summary> 
    public int getTeacherDepartmentIdFromTeacherId(int id){
        
        int deptId = 0;
        try {
                
            // sql query to check user name and password
            String szQuery = "select  Teacher_Department_ID from Teacher_master_table where Teacher_ID = ?";
            ps = con.prepareStatement( szQuery );
             ps.setInt( 1, id );
            // get result of query
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                
                if(rs.next()){
                    deptId = rs.getInt("Teacher_Department_ID");
                }
            }

            
            
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return deptId;
        
    }
    
    public int getStudentIdFromRegId(String regId){
        
        int teacherId = 0;
        try {
                
            // sql query to check user name and password
            String szQuery = "SELECT Student_ID FROM Student_Master_Table WHERE Student_RegID = ?";
            ps = con.prepareStatement( szQuery );
             ps.setString( 1, regId );
            // get result of query
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                
                if(rs.next()){
                    teacherId = rs.getInt("Student_ID");
                }
            }

            
            
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return teacherId;
        
    }
    
    public int getStudentRegIdFromID(int id){
        
        int studentregId = 0;
        try {
                
            // sql query to check user name and password
            String szQuery = "SELECT Student_RegID FROM Student_Master_Table WHERE Student_ID = ?";
            ps = con.prepareStatement( szQuery );
             ps.setInt( 1, id );
            // get result of query
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                
                if(rs.next()){
                    studentregId = rs.getInt("Student_RegID");
                }
            }

            
            
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return studentregId;
        
    }
    
    public int getHODRegIdFromID(int id){
        
        int hodregId = 0;
        try {
                
            // sql query to check user name and password
            String szQuery = "SELECT HOD_RegID FROM HOD_Master_Table WHERE HOD_ID = ?";
            ps = con.prepareStatement( szQuery );
             ps.setInt( 1, id );
            // get result of query
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                
                if(rs.next()){
                    hodregId = rs.getInt("HOD_RegID");
                }
            }

            
            
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return hodregId;
        
    }
    
    public ResultSet getAllAttendanceData(){
        
        String query = "select am.Attendance_ID as attndId,sm.Student_ID as id,sm.Student_Name as name,sm.Student_Semester as sem,d.Department_Name as dept,b.Batch_Name as batch,am.Attendance_Percentage as attend, "
                + "am.Attendance_Date as date from teacher_master_table tmt  "
                + "inner join student_master_table sm on (sm.Student_Department_ID=tmt.Teacher_Department_ID) "
                + "inner join department_table d on (d.Department_ID=sm.Student_Department_ID) "
                + "inner join batch_table b on (b.Batch_ID=sm.Student_Batch_ID) "
                + "inner join attendance_table am on (am.Attendance_Student_ID=sm.Student_ID) "
                + "where tmt.Teacher_RegID=001";
        try {   
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            
    } catch (SQLException ex) {
            System.out.println(ex.getMessage());
           // Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    
          }
    
    
    public boolean updateAttendance(int studentId,int studentattend,String date,int subjectId){
        boolean bStatus = false;
        
        String query ="update attendance_table set Attendance_Percentage = ? "
                + " where Attendance_Student_ID = ? and Attendance_Date = ? and Subject_ID = ?";
        try{
             ps = con.prepareStatement( query );
            ps.setInt(2, studentId);
            ps.setInt(1, studentattend);
            ps.setString(3, date);            
            ps.setInt(4, subjectId);
            if( ps.executeUpdate() > 0){
                bStatus = true;
            }
            
           // closeConection();
            
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       return bStatus;
    }
    
     public ResultSet getAllAssignmentData(){
        
        String query = "select am.Assignment_ID as assignId,sm.Student_ID as id,sm.Student_Name as name,sm.Student_Semester as sem,d.Department_Name as dept,b.Batch_Name as batch,am.Assignment_Mark as assignment, "
                + "am.Assignment_Date as date from teacher_master_table tmt  "
                + "inner join student_master_table sm on (sm.Student_Department_ID=tmt.Teacher_Department_ID) "
                + "inner join department_table d on (d.Department_ID=sm.Student_Department_ID) "
                + "inner join batch_table b on (b.Batch_ID=sm.Student_Batch_ID) "
                + "inner join assignment_mark_table am on (am.Assignment_Student_ID=sm.Student_ID) "
                + "where tmt.Teacher_RegID=001";
        try {   
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            
    } catch (SQLException ex) {
            System.out.println(ex.getMessage());
           // Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    
          }
     
     public ResultSet checkDateValidation(String studentId)
     {
         String query ="select Assignment_Date from assignment_mark_table where Assignment_Student_ID=?";
         
         try{
             ps = con.prepareStatement(query);
             ps.setString(1, studentId);
             
             rs = ps.executeQuery();
         }
         catch (SQLException ex) {
            System.out.println(ex.getMessage());
           // Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
           return rs;
     }
     
       public boolean updateAssignment(String assignId,String studentAssignment,String AssignmentDate){
        boolean bStatus = false;
        
        String query ="update assignment_mark_table set Assignment_Mark=?,Assignment_Date=? where Assignment_ID=?";
        try{
             ps = con.prepareStatement( query );
            ps.setString(1, studentAssignment);
            ps.setString(2, AssignmentDate);
            ps.setString(3, assignId);
            
            if( ps.executeUpdate() > 0){
                bStatus = true;
            }
            
           // closeConection();
            
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       return bStatus;
    }
       public ResultSet getAllTestData(){
        
        String query = "select am.Test_ID as testid,sm.Student_ID as id,sm.Student_Name as name,sm.Student_Semester as sem,d.Department_Name as dept,b.Batch_Name as batch,am.Test_Mark as mark, "
                + "am.Test_Date as date from teacher_master_table tmt  "
                + "inner join student_master_table sm on (sm.Student_Department_ID=tmt.Teacher_Department_ID) "
                + "inner join department_table d on (d.Department_ID=sm.Student_Department_ID) "
                + "inner join batch_table b on (b.Batch_ID=sm.Student_Batch_ID) "
                + "inner join test_mark_table am on (am.Test_Student_ID=sm.Student_ID) "
                + "where tmt.Teacher_RegID=001";
        try {   
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            
    } catch (SQLException ex) {
            System.out.println(ex.getMessage());
           // Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    
          }
       
       public boolean updateTest(String testId,String studentTest,String TestDate){
        boolean bStatus = false;
        
        String query ="update test_mark_table set Test_Mark=?,Test_Date=? where Test_ID=?";
        try{
             ps = con.prepareStatement( query );
            ps.setString(1, studentTest);
            ps.setString(2, TestDate);
            ps.setString(3, testId);
            
            if( ps.executeUpdate() > 0){
                bStatus = true;
            }
            
           // closeConection();
            
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       return bStatus;
    }
       
       public boolean insertTest(String studentId,String studentTest,String TestDate){
        boolean bStatus = false;
        
        String query ="insert into test_mark_table(Test_Student_ID,Test_Date,Test_Mark) values(?,?,?)";
        try{
             ps = con.prepareStatement( query );
            ps.setString(1, studentId);
            ps.setString(2, TestDate);
            ps.setString(3, studentTest);
            
            if( ps.executeUpdate() > 0){
                bStatus = true;
            }
            
           // closeConection();
            
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       return bStatus;
    }
         public boolean insertAttendance(String studentId,String studentattend,String TestDate){
        boolean bStatus = false;
        
        String query ="insert into attendance_table(Attendance_Student_ID,Attendance_Date,Attendance_Percentage) values(?,?,?)";
        try{
             ps = con.prepareStatement( query );
            ps.setString(1, studentId);
            ps.setString(2, TestDate);
            ps.setString(3, studentattend);
            
            if( ps.executeUpdate() > 0){
                bStatus = true;
            }
            
           // closeConection();
            
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       return bStatus;
    }
         
    
    
           
    public ResultSet getAllocationDetailsOfTeacherWithId( int teacherId ){
        
        try{
        String query = "select a.Assign_ID,s.Subject_ID,s.Subject_Name,s.Subject_Semester,b.Batch_Name,d.Department_Name"
                + " from assign_subject_to_teacher_table a,subject_table s,batch_table b,department_table d"
                + " where a.Assign_Subject_ID = s.Subject_ID and"
                + " a.Assign_Teacher_ID = ? and"
                + " b.Batch_ID = s.Subject_Batch_ID and"
                + " d.Department_Id = s.Subject_Department_ID";
        
        ps = con.prepareStatement(query);
        ps.setInt(1, teacherId);
        rs = ps.executeQuery();
        
        }catch(SQLException e){
            
          Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, e);  
        }
        return rs;
    }
           
    public ResultSet getAllStudentsWith(int semester,int deptId){
        
        String query = "select Student_ID,Student_Name from student_master_table where "
                + " Student_Semester = ? and Student_Department_ID = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, semester);
            ps.setInt(2, deptId);
            rs = ps.executeQuery();
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return rs;
        
    }
    
    public boolean insertAttendance(int studentId,int studentattend,String date,int subjectId){
        boolean bStatus = false;
        
        String query ="insert into attendance_table(Attendance_Student_ID,Attendance_Date,Attendance_Percentage,Subject_ID) values(?,?,?,?)";
        try{
             ps = con.prepareStatement( query );
            ps.setInt(1, studentId);
            ps.setInt(3, studentattend);
            ps.setString(2, date);            
            ps.setInt(4, subjectId);
            if( ps.executeUpdate() > 0){
                bStatus = true;
            }
            
           // closeConection();
            
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       return bStatus;
    }
         
    public boolean insertAssignment(int studentId,int assignmentMark,String date,int subjectId,String assignmentName){
        boolean bStatus = false;
        
        String query ="insert into assignment_mark_table(Assignment_Student_ID,Assignment_Date,Assignment_Mark,Subject_ID,Assignment_Name) values(?,?,?,?,?)";
        try{
            ps = con.prepareStatement( query );
            ps.setInt(1, studentId);
            ps.setString(2, date);
            ps.setInt(3, assignmentMark);
            ps.setInt(4, subjectId);
            ps.setString(5, assignmentName);
            
            if( ps.executeUpdate() > 0){
                bStatus = true;
            }
            
           // closeConection();
            
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       return bStatus;
    }
    
    public boolean updateAssignment(int studentId,int assignmentMark,String date,int subjectId,String assignmentName){
        boolean bStatus = false;
        
        String query ="update assignment_mark_table set Assignment_Mark = ? "
                + " where Assignment_Name = ? and  Assignment_Student_ID = ? and Assignment_Date = ? and Subject_ID = ?";
        try{
            ps = con.prepareStatement( query );
            ps.setInt(3, studentId);
            ps.setString(4, date);
            ps.setInt(1, assignmentMark);
            ps.setInt(5, subjectId);
            ps.setString(2, assignmentName);
            
            if( ps.executeUpdate() > 0){
                bStatus = true;
            }
            
           // closeConection();
            
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       return bStatus;
    }
    
    public ResultSet getAllAttendenceDate(String subjectId){
        
        String query = "select distinct Attendance_Date from attendance_table where subject_Id = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, subjectId);
            rs = ps.executeQuery();
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    public ResultSet getAllAssignmentDate(String subjectId){
        
        String query = "select distinct Assignment_Date from assignment_mark_table where subject_Id = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, subjectId);
            rs = ps.executeQuery();
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public ResultSet getAllAttendanceData(String subjectId,String date){
        
        String query = "select s.Student_ID,s.Student_Name,att.Attendance_Percentage "
                + " from student_master_table s,attendance_table att"
                + " where s.Student_ID = att.Attendance_Student_ID and att.Subject_ID = ? and att.Attendance_Date = ?";
        try {   
            ps = con.prepareStatement(query);
            ps.setString(1, subjectId);
            ps.setString(2, date);
            rs = ps.executeQuery();
            
    } catch (SQLException ex) {
            System.out.println(ex.getMessage());
           // Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    
    }
    
    public ResultSet getAllAssignmentData(String subjectId,String date){
        
        String query = "select s.Student_ID,s.Student_Name,att.Assignment_Mark,att.Assignment_Name "
                + " from student_master_table s,assignment_mark_table att"
                + " where s.Student_ID = att.Assignment_Student_ID and att.Subject_ID = ? and att.Assignment_Date = ?";
        try {   
            ps = con.prepareStatement(query);
            ps.setString(1, subjectId);
            ps.setString(2, date);
            rs = ps.executeQuery();
            
    } catch (SQLException ex) {
            System.out.println(ex.getMessage());
           // Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    
    }
    
    // check attendence exist or not
    public boolean isDateExist(Date date,int subjectId) throws ParseException{
       boolean bStatus = false;
       
       System.out.println("no error");
       
       
       
       
       
       
        try {
            
            SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
            Date d = dformat.parse(dformat.format(date));
            int month = d.getMonth()+1;
            int year  = d.getYear()+1900;
       
            System.out.println("month = "+month);
            System.out.println("year = "+year);
            
            String query = "select * from attendance_table where MONTH(Attendance_Date) = ? and YEAR(Attendance_Date) = ?"
               + " and Subject_ID = ?";
            
            ps = con.prepareStatement(query);
            ps.setInt(1,month);
            ps.setInt(2,year);
            ps.setInt(3,subjectId);
            rs = ps.executeQuery();
            
            if(rs.isBeforeFirst()){
                if(rs.next()){
                    bStatus = true;
                    
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        return bStatus;
    }
    
    // Returns the resultset containing the attendance details of an individual student
    public Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>> RetrieveStudentAttendanceData(int studentID)
    {
        ArrayList<String> subjectIDs = new ArrayList<String>();
        ArrayList<String> subjectNames = new ArrayList<String>();
        ArrayList<String> atendancePercentage = new ArrayList<String>();
        int iterator  = 0;
        String querySubjectID = "SELECT DISTINCT(Subject_ID) FROM Attendance_Table WHERE Attendance_Student_ID = ?";
        String querySubjectTable = "SELECT Subject_Name FROM Subject_Table WHERE Subject_ID = ?";
        String queryAttendanceAvg = "SELECT AVG(Attendance_Percentage) AS Attendance FROM Attendance_Table WHERE Attendance_Student_ID = ?  AND  Subject_ID =?";
        try 
        {   
            // Populates the subjectID list
            ps = con.prepareStatement(querySubjectID);
            ps.setInt(1, studentID);            
            rs = ps.executeQuery();    
            while (rs.next())
            {
                subjectIDs.add(rs.getString("Subject_ID"));
            }
            rs = null;
            
            // Now assigning the names to the corresponding subject IDs
            while(iterator < subjectIDs.size())
            {
                ps = con.prepareStatement(querySubjectTable);
                ps.setString(1, subjectIDs.get(iterator));            
                rs = ps.executeQuery();
                if(rs.next())
                {
                    subjectNames.add(rs.getString("Subject_Name"));
                    iterator++;
                }
            }
            iterator = 0;
            rs = null;
            
            // Now populating the percentages for the corresponding subjects
            while(iterator < subjectIDs.size())
            {
                ps = con.prepareStatement(queryAttendanceAvg);
                ps.setInt(1, studentID);   
                ps.setString(2, subjectIDs.get(iterator)); 
                rs = ps.executeQuery();
                if(rs.next())
                {
                    atendancePercentage.add(rs.getString("Attendance"));
                    iterator++;
                }
            }
            iterator = 0;
            rs = null;
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
           // Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>>(subjectIDs, subjectNames, atendancePercentage);
    }
    
    // Returns the resultset containing the attendance details of an individual student
    public Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>> RetrieveStudentOverall(int studentID)
    {
        ArrayList<String> subjectIDs = new ArrayList<String>();
        ArrayList<String> subjectNames = new ArrayList<String>();
        ArrayList<String> atendancePercentage = new ArrayList<String>();
        //ArrayList<String> assignmentAvg = new ArrayList<String>();
        //ArrayList<String> markAvg = new ArrayList<String>();
        
        int iterator  = 0;
        String querySubjectID = "SELECT DISTINCT(Subject_ID) FROM Attendance_Table WHERE Attendance_Student_ID = ?";
        String querySubjectTable = "SELECT Subject_Name FROM Subject_Table WHERE Subject_ID = ?";
        String queryAttendanceAvg = "SELECT AVG(Attendance_Percentage) AS Attendance FROM Attendance_Table WHERE Attendance_Student_ID = ?  AND  Subject_ID =?";
        
        try 
        {   
            // Populates the subjectID list
            ps = con.prepareStatement(querySubjectID);
            ps.setInt(1, studentID);            
            rs = ps.executeQuery();    
            while (rs.next())
            {
                subjectIDs.add(rs.getString("Subject_ID"));
            }
            rs = null;
            
            // Now assigning the names to the corresponding subject IDs
            while(iterator < subjectIDs.size())
            {
                ps = con.prepareStatement(querySubjectTable);
                ps.setString(1, subjectIDs.get(iterator));            
                rs = ps.executeQuery();
                if(rs.next())
                {
                    subjectNames.add(rs.getString("Subject_Name"));
                    iterator++;
                }
            }
            iterator = 0;
            rs = null;
            
            // Now populating the percentages for the corresponding subjects
            while(iterator < subjectIDs.size())
            {
                ps = con.prepareStatement(queryAttendanceAvg);
                ps.setInt(1, studentID);   
                ps.setString(2, subjectIDs.get(iterator)); 
                rs = ps.executeQuery();
                if(rs.next())
                {
                    atendancePercentage.add(rs.getString("Attendance"));
                    iterator++;
                }
            }
            iterator = 0;
            rs = null;
            
            
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
           // Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>>(subjectIDs, subjectNames, atendancePercentage);
    }
    
    // Returns the resultset containing the attendance details of an individual student on the basis of subject id
    public ResultSet RetrieveStudentAttendanceData(int studentID, int subjectID)
    {
        String query = "SELECT Attendance_Date, Attendance_Percentage FROM Attendance_Table WHERE Attendance_Student_ID = ? AND Subject_ID = ?";
        try 
        {   
            ps = con.prepareStatement(query);
            ps.setInt(1, studentID);
            ps.setInt(2, subjectID);
            rs = ps.executeQuery();            
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
           // Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rs;    
    }
    
    // Returns the resultset containing the attendance details of an individual student on the basis of subject id
    public ResultSet RetrieveStudentAssignmentData(int studentID, int subjectID)
    {
        String query = "SELECT Assignment_Date, Assignment_Name, Assignment_Mark FROM Assignment_Mark_Table WHERE Assignment_Student_ID = ? AND Subject_ID = ?";
        try 
        {   
            ps = con.prepareStatement(query);
            ps.setInt(1, studentID);
            ps.setInt(2, subjectID);
            rs = ps.executeQuery();            
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
           // Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rs;    
    }
    
    // Returns the resultset containing the attendance details of an individual student on the basis of subject id
    public ResultSet RetrieveStudentTestData(int studentID, int subjectID)
    {
        String query = "SELECT Test_Date, Test_Mark FROM Test_Table WHERE Test_Student_ID = ? AND Subject_ID = ?";
        try 
        {   
            ps = con.prepareStatement(query);
            ps.setInt(1, studentID);
            ps.setInt(2, subjectID);
            rs = ps.executeQuery();            
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
           // Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rs;    
    }
    
    // Gets the attendance percentage for a given a subject ID
    public String GetAttendancePercentageForSubject(int studentID, int subjectID)
    {
        String query = "SELECT AVG(Attendance_Percentage) AS Average FROM Attendance_Table WHERE Attendance_Student_ID = ? AND Subject_ID = ?";
        String p = null;
        try 
        {   
            ps = con.prepareStatement(query);
            ps.setInt(1, studentID);
            ps.setInt(2, subjectID);
            rs = ps.executeQuery();   
            if(rs.next())
            {
                p =rs.getString("Average");
            }
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
           // Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return p;    
    }    
    
    // Gets the attendance percentage for a given a subject ID
    public String GetAggregateAssignmentMarkForSubject(int studentID, int subjectID)
    {
        String query = "SELECT Assignment_Mark FROM Assignment_Mark_Table WHERE Assignment_Student_ID = ? AND Subject_ID = ?";
        String p = null;
        float average =0;
        try 
        {   
            ps = con.prepareStatement(query);
            ps.setInt(1, studentID);
            ps.setInt(2, subjectID);
            rs = ps.executeQuery();   
            List<Float> marks =new ArrayList<Float>();
                while (rs.next())
                {
                    marks.add(Float.parseFloat(rs.getString("Assignment_Mark")));
                }
                Collections.sort(marks);
                if(marks.size() == 1)
                {
                    average = marks.get(marks.size() - 1);
                }
                else if(marks.size() == 0)
                {
                    average = 0;
                }
                else
                {                    
                    average = (marks.get(marks.size() - 1) + marks.get(marks.size() - 2))/ 2;
                }
            /*if(rs.next())
            {
                p =rs.getString("Aggregate");
            }*/
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
           // Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return String.valueOf(average);    
    }    
    
    // Returns the tuple containing the assignement details of an individual student
    public Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>> RetrieveStudentAssignmentData(int studentID)
    {
        ArrayList<String> subjectIDs = new ArrayList<String>();
        ArrayList<String> subjectNames = new ArrayList<String>();
        ArrayList<String> assignmentMark = new ArrayList<String>();
        int iterator  = 0;
        float average = 0;
        String querySubjectID = "SELECT DISTINCT(Subject_ID) FROM Assignment_Mark_Table WHERE Assignment_Student_ID = ?";
        String querySubjectTable = "SELECT Subject_Name FROM Subject_Table WHERE Subject_ID = ?";
        String queryAssignmentAvg = "SELECT Assignment_Mark FROM Assignment_Mark_Table WHERE Assignment_Student_ID = ?  AND  Subject_ID =?";
        try 
        {   
            // Populates the subjectID list
            ps = con.prepareStatement(querySubjectID);
            ps.setInt(1, studentID);            
            rs = ps.executeQuery();    
            while (rs.next())
            {
                subjectIDs.add(rs.getString("Subject_ID"));
            }
            rs = null;
            
            // Now assigning the names to the corresponding subject IDs
            while(iterator < subjectIDs.size())
            {
                ps = con.prepareStatement(querySubjectTable);
                ps.setString(1, subjectIDs.get(iterator));            
                rs = ps.executeQuery();
                if(rs.next())
                {
                    subjectNames.add(rs.getString("Subject_Name"));
                    iterator++;
                }
            }
            iterator = 0;
            rs = null;
            
            // Now populating the percentages for the corresponding subjects
            while(iterator < subjectIDs.size())
            {
                ps = con.prepareStatement(queryAssignmentAvg);
                ps.setInt(1, studentID);   
                ps.setString(2, subjectIDs.get(iterator)); 
                rs = ps.executeQuery();
                //Adding assignment marks and then sorting
                List<Float> marks =new ArrayList<Float>();
                while (rs.next())
                {
                    marks.add(Float.parseFloat(rs.getString("Assignment_Mark")));
                }
                Collections.sort(marks);
                if(marks.size() == 1)
                {
                    average = marks.get(marks.size() - 1);
                }
                else if(marks.size() == 0)
                {
                    average = 0;
                }
                else
                {                    
                    average = (marks.get(marks.size() - 1) + marks.get(marks.size() - 2))/ 2;
                }
                assignmentMark.add(String.valueOf(average));
                iterator++;
                /*if(rs.next())
                {
                    assignmentMark.add(rs.getString("Assignment_Mark"));
                    iterator++;
                }*/
            }
            iterator = 0;
            rs = null;
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
           // Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>>(subjectIDs, subjectNames, assignmentMark);
    }
    
    // Getting al the details of a tudent in a department with a corresponding batch
    public ResultSet RetrieveStudentDetails(int departmentID, int batchID)
    {
        String query = "SELECT Student_RegID AS RegID, Student_Name AS Name, Student_Details AS Details, Student_Semester AS Semester"
                     +" FROM Student_Master_Table WHERE Student_Department_ID = ? AND Student_Batch_ID = ?";
        String p = null;
        try 
        {   
            ps = con.prepareStatement(query);
            ps.setInt(1, departmentID);
            ps.setInt(2, batchID);
            rs = ps.executeQuery();
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return rs;  
    }
    
    // Returns the resultset containing the attendance details of an individual student for a given batch and department
    public Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>> RetrieveStudentAttendanceDetails(int departmentID, int batchID)
    {
        ArrayList<String> studentIDs = new ArrayList<String>();
        ArrayList<String> studentRegIDS = new ArrayList<String>();
        ArrayList<String> studentNames = new ArrayList<String>();
        ArrayList<String> percentages = new ArrayList<String>();
        int iterator  = 0;
        String queryStudentDetails = "SELECT  Student_ID, Student_RegID, Student_Name FROM Student_Master_Table"
                               +" WHERE Student_Department_ID = ? AND Student_Batch_ID = ?";
        String queryAttendanceAvg = "SELECT AVG(Attendance_Percentage) AS Attendance FROM Attendance_Table WHERE Attendance_Student_ID = ?";
        try 
        {   
            // Populates the subjectID list
            ps = con.prepareStatement(queryStudentDetails);
            ps.setInt(1, departmentID);       
            ps.setInt(2, batchID);       
            rs = ps.executeQuery();    
            while (rs.next())
            {
                studentIDs.add(rs.getString("Student_ID"));
                studentRegIDS.add(rs.getString("Student_RegID"));
                studentNames.add(rs.getString("Student_Name"));
            }
            rs = null;
            
            // Now calculating the percentages and adding too the corresponding list
            while(iterator < studentIDs.size())
            {
                ps = con.prepareStatement(queryAttendanceAvg);
                ps.setString(1, studentIDs.get(iterator));            
                rs = ps.executeQuery();
                if(rs.next())
                {
                    percentages.add(rs.getString("Attendance"));
                    iterator++;
                }
            }
            iterator = 0;
            rs = null;
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return new Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>>(studentRegIDS, studentNames, percentages);
    }
    
    // Returns the tuple containing the assignment details of an individual student for a given batch and department
    public Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>> RetrieveStudentAssignmentDetails(int departmentID, int batchID)
    {
        ArrayList<String> studentIDs = new ArrayList<String>();
        ArrayList<String> studentRegIDS = new ArrayList<String>();
        ArrayList<String> studentNames = new ArrayList<String>();
        ArrayList<String> assignment = new ArrayList<String>();
        int iterator  = 0;
        String queryStudentDetails = "SELECT  Student_ID, Student_RegID, Student_Name FROM Student_Master_Table"
                               +" WHERE Student_Department_ID = ? AND Student_Batch_ID = ?";
        String queryAttendanceAvg = "SELECT AVG(Assignment_Mark) AS Assignment FROM Assignment_Mark_Table WHERE Assignment_Student_ID = ?";
        try 
        {   
            // Populates the subjectID list
            ps = con.prepareStatement(queryStudentDetails);
            ps.setInt(1, departmentID);       
            ps.setInt(2, batchID);       
            rs = ps.executeQuery();    
            while (rs.next())
            {
                studentIDs.add(rs.getString("Student_ID"));
                studentRegIDS.add(rs.getString("Student_RegID"));
                studentNames.add(rs.getString("Student_Name"));
            }
            rs = null;
            
            // Now calculating the percentages and adding too the corresponding list
            while(iterator < studentIDs.size())
            {
                ps = con.prepareStatement(queryAttendanceAvg);
                ps.setString(1, studentIDs.get(iterator));            
                rs = ps.executeQuery();
                if(rs.next())
                {
                    assignment.add(rs.getString("Assignment"));
                    iterator++;
                }
            }
            iterator = 0;
            rs = null;
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return new Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>>(studentRegIDS, studentNames, assignment);
    }
    
    // Returns the tuple containing the assignment details of an individual student for a given batch and department
    public Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>> RetrieveStudentTestDetails(int departmentID, int batchID)
    {
        ArrayList<String> studentIDs = new ArrayList<String>();
        ArrayList<String> studentRegIDS = new ArrayList<String>();
        ArrayList<String> studentNames = new ArrayList<String>();
        ArrayList<String> test = new ArrayList<String>();
        int iterator  = 0;
        String queryStudentDetails = "SELECT  Student_ID, Student_RegID, Student_Name FROM Student_Master_Table"
                               +" WHERE Student_Department_ID = ? AND Student_Batch_ID = ?";
        String querytestAvg = "SELECT AVG(Test_Mark) AS Test FROM Test_Table WHERE Test_Student_ID = ?";
        try 
        {   
            // Populates the subjectID list
            ps = con.prepareStatement(queryStudentDetails);
            ps.setInt(1, departmentID);       
            ps.setInt(2, batchID);       
            rs = ps.executeQuery();    
            while (rs.next())
            {
                studentIDs.add(rs.getString("Student_ID"));
                studentRegIDS.add(rs.getString("Student_RegID"));
                studentNames.add(rs.getString("Student_Name"));
            }
            rs = null;
            
            // Now calculating the percentages and adding too the corresponding list
            while(iterator < studentIDs.size())
            {
                ps = con.prepareStatement(querytestAvg);
                ps.setString(1, studentIDs.get(iterator));            
                rs = ps.executeQuery();
                if(rs.next())
                {
                    test.add(rs.getString("Test"));
                    iterator++;
                }
            }
            iterator = 0;
            rs = null;
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        
        return new Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>>(studentRegIDS, studentNames, test);
    }
    
    public boolean insertTest(int studentId,int testMark,String TestDate,int subjectId){
        boolean bStatus = false;
        
        String query ="insert into test_table(Test_Student_ID,Test_Date,Test_Mark,Subject_ID) values(?,?,?,?)";
        try{
            ps = con.prepareStatement( query );
            ps.setInt(1, studentId);
            ps.setString(2, TestDate);
            ps.setInt(3, testMark);
            ps.setInt(4,subjectId);
            
            if( ps.executeUpdate() > 0){
                bStatus = true;
            }
            
           // closeConection();
            
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       return bStatus;
    }
    
    public boolean updateTest(int studentId,int testMark,String TestDate,int subjectId){
        boolean bStatus = false;
        
        String query ="update test_table set Test_Mark = ? "
                + " where  Test_Student_ID = ? and Test_Date = ? and Subject_ID = ?";
        try{
            ps = con.prepareStatement( query );
            ps.setInt(2, studentId);
            ps.setString(3, TestDate);
            ps.setInt(1, testMark);
            ps.setInt(4,subjectId);
            
            if( ps.executeUpdate() > 0){
                bStatus = true;
            }
            
           // closeConection();
            
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
       return bStatus;
    }
    
    public ResultSet getAllTestDate(String subjectId){
        
        String query = "select distinct Test_Date from test_table where subject_Id = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, subjectId);
            rs = ps.executeQuery();
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    public ResultSet getAllTestData(String subjectId,String date){
        
        String query = "select s.Student_ID,s.Student_Name,att.Test_Mark"
                + " from student_master_table s,test_table att"
                + " where s.Student_ID = att.Test_Student_ID and att.Subject_ID = ? and att.Test_Date = ?";
        try {   
            ps = con.prepareStatement(query);
            ps.setString(1, subjectId);
            ps.setString(2, date);
            rs = ps.executeQuery();
            
    } catch (SQLException ex) {
            System.out.println(ex.getMessage());
           // Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    
    }
    
    // Returns the tuple containing the test details of an individual student
    public Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>> RetrieveStudentTestData(int studentID)
    {
        ArrayList<String> subjectIDs = new ArrayList<String>();
        ArrayList<String> subjectNames = new ArrayList<String>();
        ArrayList<String> testMark = new ArrayList<String>();
        int iterator  = 0;
        float average = 0;
        String querySubjectID = "SELECT DISTINCT(Subject_ID) FROM Assignment_Mark_Table WHERE Assignment_Student_ID = ?";
        String querySubjectTable = "SELECT Subject_Name FROM Subject_Table WHERE Subject_ID = ?";
        String queryTestMarks = "SELECT Test_Mark FROM Test_Table WHERE Test_Student_ID = ?  AND  Subject_ID = ?";
        try 
        {   
            // Populates the subjectID list
            ps = con.prepareStatement(querySubjectID);
            ps.setInt(1, studentID);            
            rs = ps.executeQuery();    
            while (rs.next())
            {
                subjectIDs.add(rs.getString("Subject_ID"));
            }
            rs = null;
            
            // Now assigning the names to the corresponding subject IDs
            while(iterator < subjectIDs.size())
            {
                ps = con.prepareStatement(querySubjectTable);
                ps.setString(1, subjectIDs.get(iterator));            
                rs = ps.executeQuery();
                if(rs.next())
                {
                    subjectNames.add(rs.getString("Subject_Name"));
                    iterator++;
                }
            }
            iterator = 0;
            rs = null;
            
            // Now populating the percentages for the corresponding subjects
            while(iterator < subjectIDs.size())
            {
                ps = con.prepareStatement(queryTestMarks);
                ps.setInt(1, studentID);   
                ps.setString(2, subjectIDs.get(iterator)); 
                rs = ps.executeQuery();
                //Adding test marks and then sorting
                List<Float> marks =new ArrayList<Float>();
                while (rs.next())
                {
                    marks.add(Float.parseFloat(rs.getString("Test_Mark")));
                }
                Collections.sort(marks);
                if(marks.size() == 1)
                {
                    average = marks.get(marks.size() - 1);
                }
                else if(marks.size() == 0)
                {
                    average = 0;
                }
                else
                {                    
                    average = (marks.get(marks.size() - 1) + marks.get(marks.size() - 2))/ 2;
                }
                testMark.add(String.valueOf(average));
                iterator++;
                /*if(rs.next())
                {
                    assignmentMark.add(rs.getString("Assignment_Mark"));
                    iterator++;
                }*/
            }
            iterator = 0;
            rs = null;
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
           // Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>>(subjectIDs, subjectNames, testMark);
    }
    
    // Gets the attendance percentage for a given a subject ID
    public String GetAggregateTestMarkForSubject(int studentID, int subjectID)
    {
        String query = "SELECT Test_Mark FROM Test_Table WHERE Test_Student_ID = ? AND Subject_ID = ?";
        String p = null;
        float average = 0;
        try 
        {   
            ps = con.prepareStatement(query);
            ps.setInt(1, studentID);
            ps.setInt(2, subjectID);
            rs = ps.executeQuery(); 
            List<Float> marks =new ArrayList<Float>();
                while (rs.next())
                {
                    marks.add(Float.parseFloat(rs.getString("Test_Mark")));
                }
                Collections.sort(marks);
                if(marks.size() == 1)
                {
                    average = marks.get(marks.size() - 1);
                }
                else if(marks.size() == 0)
                {
                    average = 0;
                }
                else
                {                    
                    average = (marks.get(marks.size() - 1) + marks.get(marks.size() - 2))/ 2;
                }
            /*
            if(rs.next())
            {
                p =rs.getString("Aggregate");
            }*/
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
           // Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return String.valueOf(average);    
    }  
    
    public boolean changePassword(String userName,String oldPwd,String newPwd )
    {
         boolean status = false;
         String query = "select Password from user_details_table"
                 + " where UserName = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1,userName);
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                rs.next();
                if(rs.getString("Password").equals(oldPwd))
                {
                   query = "update user_details_table set Password = ? where Username = ?";
                   ps = con.prepareStatement(query);
                   ps.setString(1,newPwd);
                   ps.setString(2,userName);
                   if(ps.executeUpdate() > 0)
                   {
                       status = true;
                   }
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"Current password does not match.");
                }    
                    
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status; 
        
    }
    
     public boolean shiftSemester(String batchId)
    {
        boolean bStatus = true;
        String query = "update batch_table set Batch_current_semester = Batch_current_semester + 1"
                + " where Batch_ID = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, batchId);
            
            if(ps.executeUpdate() < 0)
            {
              bStatus = false;  
            }
            
            query = "update student_master_table set Student_semester = Student_semester + 1"
               + " where Student_Batch_ID = ?";
               ps = con.prepareStatement(query);
               ps.setString(1, batchId);

               if(ps.executeUpdate() < 0)
               {
                 bStatus = false;
               }
               
             query = "delete from attendance_table where Subject_ID in \n" +
              "( select Subject_ID from Subject_table where Subject_Batch_ID = ? )";
             
               ps = con.prepareStatement(query);
               ps.setString(1, batchId);

               if(ps.executeUpdate() < 0)
               {
                 bStatus = false;
               } 
               
               query = "delete from assignment_mark_table where Subject_ID in \n" +
              "( select Subject_ID from Subject_table where Subject_Batch_ID = ? )";
             
               ps = con.prepareStatement(query);
               ps.setString(1, batchId);

               if(ps.executeUpdate() < 0)
               {
                 bStatus = false;
               } 
               
               query = "delete from test_table where Subject_ID in \n" +
              "( select Subject_ID from Subject_table where Subject_Batch_ID = ? )";
             
               ps = con.prepareStatement(query);
               ps.setString(1, batchId);

               if(ps.executeUpdate() < 0)
               {
                 bStatus = false;
               } 
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bStatus;
    }
     
    public ResultSet getBatchDetails(int departmentId)
    {
        String query = "select * from batch_table where Batch_Department_Id = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1,departmentId);
            
            rs = ps.executeQuery();
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
        
    }
    
    public ResultSet getEntryDates()
    {
        String query = "select * from entry_date";
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rs;
    }
    
    public boolean changeDate(Date startDate,Date endDate)
    {
        boolean bStatus = false;
        String query = "update entry_date set starts_on = ? , ends_on = ? where date_id = 1";
        try {
            String date;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");                    
            ps = con.prepareStatement(query);
            date = format.format(startDate);
            ps.setString(1, date);
            date = format.format(endDate);
            ps.setString(2, date);
            if(ps.executeUpdate() > 0)
            {
                bStatus =true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bStatus;
    }
    
   
}
