/*
 * The form for the Head of the department
 */

package PTassistant;

import Database.DatabaseManager;
import PTassistant.LoginForm;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import org.javatuples.Triplet;

/**
 *
 * @author Panickar
 */
public class HODForm extends javax.swing.JFrame {

    /**
     * Creates new form HODForm
     */
    
    private int hodId      = 0;
    private int hodDeptId  = 0;
    private String oldSubjectID = null;
    
    public HODForm(){
        
        initComponents();
        setLocationRelativeTo(null);        
        
    }
    
    public HODForm(int hodId){
        initComponents();
        setLocationRelativeTo(null); 
        this.hodId = hodId;       
        this.SetHODDepartmentID();
    }
     
    /// <summary>
    /// Setting the department id of the HOD
    /// </summary>    
    private void SetHODDepartmentID()
    {
        try
        {
            this.hodDeptId = new DatabaseManager().getHodDepartmentIdFromHODId(hodId);
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(rootPane,ex.getMessage());
        }
    }
    /// Set the visibility of the panel in the card layout.
    /// Logic:
    ///   First set visibility of all panel to false
    ///   Then make the visibility of selected panel to true
    private void SetPanelVisibility( JPanel panel ){
        
        MiddlePanel.removeAll();
        MiddlePanel.repaint();
        MiddlePanel.revalidate();
        
        MiddlePanel.add(panel);
        MiddlePanel.repaint();
        MiddlePanel.revalidate();
    }
    
    private void SetTableVisibility( JPanel panel )
    {
        
        TableHolderPanel.removeAll();
        TableHolderPanel.repaint();
        TableHolderPanel.revalidate();
        
        TableHolderPanel.add(panel);
        TableHolderPanel.repaint();
        TableHolderPanel.revalidate();
    } 
    
    /// <summary>
    /// Populates the department combobox
    /// </summary>    
    private void PopulateDepartmentCombobox(JComboBox box)
    {
        String deptName = null;
        box.removeAllItems();
        try 
        {
            DatabaseManager dbManager = new DatabaseManager();
            deptName = dbManager.SelectDepartmentName(hodDeptId);
            box.addItem(deptName);
        }
        catch(ClassNotFoundException | SQLException e)
        {
            JOptionPane.showMessageDialog(rootPane,e.getMessage());
        }
    }
    private void  showAllTeacherTable(){
        try {
            
            // select all batches from db
            DatabaseManager dbManager   = new DatabaseManager();
            ResultSet allTeachers       = dbManager.selectAllTeachers();
            DefaultTableModel  dm       = (DefaultTableModel)showAllTeacherTable.getModel();
            // check selection is sucessfull or not
            dm.setRowCount(0);
            if(allTeachers.isBeforeFirst()){                
                
                while(allTeachers.next()){
                    
                   dm.addRow(new Object[]{
                   
                       allTeachers.getString("Teacher_RegID"),
                       allTeachers.getString("Teacher_Name"),
                       allTeachers.getString("Teacher_Details"),
                       allTeachers.getString("Department_Name")
                   
                   
                   });
                }
                SetPanelVisibility(ShowAllTeacher);
            }
            
        
        }catch(ClassNotFoundException | SQLException e){
            
           Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, e);  
        } 
        
    }
    
    /// <summary>
    /// Populates the batch combobox
    /// </summary>    
    private void PopulateBatchCombobox(JComboBox box)
    {
        ResultSet result;
        box.removeAllItems();
        try 
        {
            DatabaseManager dbManager = new DatabaseManager();
            result = dbManager.SelectBatchName(hodDeptId);
            if ( result.isBeforeFirst() ) 
            {   
                while(result.next())
                {
                    box.addItem(result.getString("Batch_Name"));
                }
            } 
        }
        catch(ClassNotFoundException | SQLException e)
        {
            JOptionPane.showMessageDialog(rootPane,e.getMessage());
        }
    }
    
    /// <summary>
    /// Populates the subject chooser table
    /// </summary>  
    private void PopulateSubjectDetails(JTable table)
    {
        // By default setst the table with all subjects in DB
        DatabaseManager manager = null; 
        ResultSet rs = null; 
        try 
            {
                manager = new DatabaseManager();
                rs = manager.RetrieveSubjectDetails(hodDeptId);
                if ( null == rs )
                {
                    
                }
                else
                {
                    DefaultTableModel dm = (DefaultTableModel)table.getModel();
                    if(rs.isBeforeFirst())
                    {
                        dm.setRowCount(0);
                        while(rs.next())
                        {
                            String batchColumn = new DatabaseManager().GetBatchName(rs.getString("Subject_Batch_ID"));
                            String deartmentColumn = new DatabaseManager().GetDepartmentName(rs.getString("Subject_Department_ID"));
                            dm.addRow
                                    (
                                        new Object[]
                                                {
                                                    rs.getString("Subject_ID"),
                                                    rs.getString("Subject_Name"),
                                                    rs.getString("Subject_Semester"),        
                                                    deartmentColumn
                                                }
                                    );
                        }
                    }
                }
                
                // deallocation
                manager.closeConection();
                manager    = null;
                rs         = null;
                
            } 
            catch (ClassNotFoundException | SQLException ex) 
            {
                Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, ex);
            }        
    }
    
    private void setActiveSemesterTable()
    {
        try {
            // TODO add your handling code here:
            DefaultTableModel dm = (DefaultTableModel)activeSemesterTable.getModel();                
            ResultSet batchDetails = new DatabaseManager().getBatchDetails(hodDeptId);
            if(batchDetails.isBeforeFirst())
            { 
               dm.setRowCount(0);
               while(batchDetails.next())
               {
                  dm.addRow(new Object[]{
                      batchDetails.getString("Batch_ID"),
                      batchDetails.getString("Batch_Name"),
                      batchDetails.getString("Batch_Starts_On")+" - "+batchDetails.getString("Batch_Ends_On"),
                      batchDetails.getString("Batch_current_Semester")
                  });
               }
            }
            
        }
        catch(ClassNotFoundException | SQLException e)
        {
           JOptionPane.showMessageDialog(null,e.getMessage());         
        }       
    }
    
    /// <summary>
    /// Populates the teacher chooser table
    /// </summary>  
    private void PopulateTeacherDetails(JTable table, int departmentID)
    {
        // By default sets the table with all teachers in DB
        DatabaseManager manager = null; 
        ResultSet rs = null; 
        try 
            {
                manager = new DatabaseManager();
                if(departmentID == 0)
                {
                    rs = manager.RetrieveTeacherDetails(hodDeptId);
                }
                else
                {
                    rs = manager.RetrieveTeacherDetails(departmentID);
                }
                if ( null == rs )
                {
                    
                }
                else
                {
                   table.setModel(DbUtils.resultSetToTableModel(rs));
                }
                
                // deallocation
                manager.closeConection();
                manager    = null;
                rs         = null;
                
            } 
            catch (ClassNotFoundException | SQLException ex) 
            {
                Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, ex);
            }        
    }
    
    /// <summary>
    /// Populates the subject to teacher allotment table
    /// </summary>  
    private void PopulateSubjectAllotmentTable(JTable table)
    {
        // By default sets the table 
        DatabaseManager manager = null; 
        ResultSet rs = null; 
        try 
            {
                manager = new DatabaseManager();
                rs = manager.RetrieveSubjectToTeacherAllotmentDetails();
                
                if ( null == rs )
                {
                    
                }
                else
                {
                    DefaultTableModel dm = (DefaultTableModel)table.getModel();
                    if(rs.isBeforeFirst())
                    {
                        dm.setRowCount(0);
                        while(rs.next())
                        {
                            String subjectColumn = new DatabaseManager().GetSubjectName(rs.getString("Assign_Subject_ID"));
                            String teacherColumn = new DatabaseManager().GetTeacherName(rs.getString("Assign_Teacher_ID"));
                            dm.addRow
                                    (
                                        new Object[]
                                                {
                                                    rs.getString("Assign_ID"),
                                                    subjectColumn,
                                                    teacherColumn
                                                }
                                    );
                        }
                    }
                }
                
                // deallocation
                manager.closeConection();
                manager    = null;
                rs         = null;
                
            } 
            catch (ClassNotFoundException | SQLException ex) 
            {
                Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, ex);
            }        
    }       
  
    /// <summary>
    /// Populates the subject to teacher allotment table
    /// </summary>  
    private void PopulateViewAllotmentTable(JTable table, String basisOf, int id)
    {
        // By default sets the table 
        DatabaseManager manager = null; 
        ResultSet rs = null; 
        
        
        try 
            {
                manager = new DatabaseManager();                
                rs = manager.RetrieveSubjectToTeacherAllotmentDetails(id, basisOf);                
                if ( null == rs )
                {
                    
                }
                else
                {
                    DefaultTableModel dm = (DefaultTableModel)table.getModel();
                    if(rs.isBeforeFirst())
                    {
                        dm.setRowCount(0);
                        while(rs.next())
                        {
                            String subjectColumn = new DatabaseManager().GetSubjectName(rs.getString("Assign_Subject_ID"));
                            String teacherColumn = new DatabaseManager().GetTeacherName(rs.getString("Assign_Teacher_ID"));
                            dm.addRow
                                    (
                                        new Object[]
                                                {
                                                    rs.getString("Assign_ID"),
                                                    subjectColumn,
                                                    teacherColumn
                                                }
                                    );
                        }
                    }
                }
                
                // deallocation
                manager.closeConection();
                manager    = null;
                rs         = null;
                
            } 
            catch (ClassNotFoundException | SQLException ex) 
            {
                Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, ex);
            }        
    }       
    
    // Populates the attendance for the student subject vice
    public void PopulateStudentAttendanceSubjectVice(int studentID)
    {
        DatabaseManager manager = null; 
        int i= 0, j=0, numberOfRows = 0;
        Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>> attendaceTriplet = null;
        try 
            {
                manager = new DatabaseManager();
                attendaceTriplet = manager.RetrieveStudentAttendanceData(studentID);
                // After receiving the triplet convert the data from it to the table
                if(attendaceTriplet != null)
                {
                    DefaultTableModel dm = (DefaultTableModel)SubjectAttendanceDisplayTable.getModel();
                    dm.setColumnCount(0);
                    dm.setRowCount(0);
                    dm.addColumn("SubjectID");dm.addColumn("Subject_Name");dm.addColumn("Aggregate_Attendance");
                    numberOfRows = attendaceTriplet.getValue0().size();
                    
                    // Populating the table by extratcing values from each rows
                    while(i < numberOfRows)
                    {    
                        String subjectID = attendaceTriplet.getValue0().get(i);
                        String subjectName = attendaceTriplet.getValue1().get(i);
                        String attendancePercentage = attendaceTriplet.getValue2().get(i);
                            dm.addRow
                                    (
                                        new Object[]
                                                {
                                                    subjectID,
                                                    subjectName,
                                                    attendancePercentage
                                                }
                                    );
                        // For iteration
                        i++;
                    }
                }                
                // deallocation
                manager.closeConection();
                manager    = null;
                
            } 
            catch (ClassNotFoundException | SQLException ex) 
            {
                Logger.getLogger(StudentForm.class.getName()).log(Level.SEVERE, null, ex);
            }       
    }
    
     // Populates the assignment for the student subject vice
    public void PopulateStudentAssignmentSubjectVice(int studentID)
    {
        DatabaseManager manager = null; 
        int i= 0, j=0, numberOfRows = 0;
        Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>> assignmentTriplet = null;
        try 
            {
                manager = new DatabaseManager();
                assignmentTriplet = manager.RetrieveStudentAssignmentData(studentID);
                // After receiving the triplet convert the data from it to the table
                if(assignmentTriplet != null)
                {
                    DefaultTableModel dm = (DefaultTableModel)SubjectAssignmentDisplayTable.getModel();
                    dm.setColumnCount(0);
                    dm.setRowCount(0);
                    dm.addColumn("SubjectID");dm.addColumn("Subject_Name");dm.addColumn("Aggregate_Assignement_Mark");
                    numberOfRows = assignmentTriplet.getValue0().size();

                    // Populating the table by extratcing values from each rows
                    while(i < numberOfRows)
                    {    
                        String subjectID = assignmentTriplet.getValue0().get(i);
                        String subjectName = assignmentTriplet.getValue1().get(i);
                        String assignmentMark = assignmentTriplet.getValue2().get(i);
                            dm.addRow
                                    (
                                        new Object[]
                                                {
                                                    subjectID,
                                                    subjectName,
                                                    assignmentMark
                                                }
                                    );
                        // For iteration
                        i++;
                    }
                }                
                // deallocation
                manager.closeConection();
                manager    = null;
                
            } 
            catch (ClassNotFoundException | SQLException ex) 
            {
                Logger.getLogger(StudentForm.class.getName()).log(Level.SEVERE, null, ex);
            }       
    }
    
    public void PopulateStudentTestSubjectVice(int studentID)
    {
        DatabaseManager manager = null; 
        int i= 0, j=0, numberOfRows = 0;
        Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>> testTriplet = null;
        try 
            {
                manager = new DatabaseManager();
                testTriplet = manager.RetrieveStudentTestData(studentID);
                // After receiving the triplet convert the data from it to the table
                if(testTriplet != null)
                {
                    DefaultTableModel dm = (DefaultTableModel)SubjectTestDisplayTable.getModel();
                    dm.setRowCount(0);
                    numberOfRows = testTriplet.getValue0().size();

                    // Populating the table by extratcing values from each rows
                    while(i < numberOfRows)
                    {    
                        String subjectID = testTriplet.getValue0().get(i);
                        String subjectName = testTriplet.getValue1().get(i);
                        String mark = testTriplet.getValue2().get(i);
                            dm.addRow
                                    (
                                        new Object[]
                                                {
                                                    subjectID,
                                                    subjectName,
                                                    mark
                                                }
                                    );
                        // For iteration
                        i++;
                    }
                }                
                // deallocation
                manager.closeConection();
                manager    = null;
                
            } 
            catch (ClassNotFoundException | SQLException ex) 
            {
                Logger.getLogger(StudentForm.class.getName()).log(Level.SEVERE, null, ex);
            }       
    }
    
    public void setShowAllStudentsTable() {
        
         // select all batches from db
         try{
             
            DatabaseManager dbManager   = new DatabaseManager();
            ResultSet allStudents       = dbManager.selectAllStudentsWithDept(hodDeptId);
            DefaultTableModel  dm       = (DefaultTableModel)showAllStudentsTable.getModel();
            // check selection is sucessfull or not
            dm.setRowCount(0);
            if(allStudents.isBeforeFirst()){                
                
                while(allStudents.next()){
                    
                   dm.addRow(new Object[]{
                   
                       allStudents.getString("Student_ID"),
                       allStudents.getString("Student_RegID"),
                       allStudents.getString("Student_Name"),
                       allStudents.getString("Student_Details"),
                       allStudents.getString("Department_Name"),
                       allStudents.getString("Student_Semester"),
                       allStudents.getString("Batch_Name")    
                      
                   
                   });
                }
                
            }
            
        
        }catch(ClassNotFoundException | SQLException e){
            
           Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, e);  
        } 
        SetPanelVisibility(studentDetailsPanel);
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel18 = new javax.swing.JLabel();
        SubjectChooserDialog = new javax.swing.JDialog();
        SubjectChooserBasePanel = new javax.swing.JPanel();
        SubjectNameSearchTextbox = new javax.swing.JTextField();
        SubjectChooserScrollPane = new javax.swing.JScrollPane();
        SubjectChooserTable = new javax.swing.JTable();
        SubjectChooserOKButton = new javax.swing.JButton();
        SubjectChooserCloseButton = new javax.swing.JButton();
        SubjectChooserLabel = new javax.swing.JLabel();
        TeacherChooserDialog = new javax.swing.JDialog();
        TeacherChooserBasePanel = new javax.swing.JPanel();
        TeacherNameSearchTextbox = new javax.swing.JTextField();
        TeacherChooserScrollPane = new javax.swing.JScrollPane();
        TeacherChooserTable = new javax.swing.JTable();
        TeacherChooserOKButton = new javax.swing.JButton();
        TeacherChooserCloseButton = new javax.swing.JButton();
        TeacherChooserLabel = new javax.swing.JLabel();
        SubjectAttendanceDisplayDialog = new javax.swing.JDialog();
        SubjectChooserBasePanel1 = new javax.swing.JPanel();
        SubjectChooserScrollPane1 = new javax.swing.JScrollPane();
        SubjectAttendanceDisplayTable = new javax.swing.JTable();
        SubjectAssignmentDisplayDialog = new javax.swing.JDialog();
        SubjectChooserBasePanel2 = new javax.swing.JPanel();
        SubjectChooserScrollPane2 = new javax.swing.JScrollPane();
        SubjectAssignmentDisplayTable = new javax.swing.JTable();
        SubjectTestDisplayDialog = new javax.swing.JDialog();
        SubjectChooserBasePanel3 = new javax.swing.JPanel();
        SubjectChooserScrollPane3 = new javax.swing.JScrollPane();
        SubjectTestDisplayTable = new javax.swing.JTable();
        HeaderPanel = new javax.swing.JPanel();
        HeaderLogoLabel = new javax.swing.JLabel();
        InfoLabelHeader2 = new javax.swing.JLabel();
        SideOptionsPanel = new javax.swing.JPanel();
        RegularOptionsPanel1 = new javax.swing.JPanel();
        HomeOptionButton = new javax.swing.JButton();
        AddSubjectOptionButton = new javax.swing.JButton();
        AllotSubjectOptionButton1 = new javax.swing.JButton();
        AllotSubjectOptionButton = new javax.swing.JButton();
        ViewSubjectOptionButton = new javax.swing.JButton();
        ViewAllotmentOptionButton = new javax.swing.JButton();
        ViewStudentDetailsOptionButton = new javax.swing.JButton();
        ViewStudentDetailsOptionButton1 = new javax.swing.JButton();
        ViewStudentDetailsOptionButton2 = new javax.swing.JButton();
        ViewStudentDetailsOptionButton3 = new javax.swing.JButton();
        OtherOptionsPanel1 = new javax.swing.JPanel();
        LogoutButton = new javax.swing.JButton();
        HODChangePasswordButton = new javax.swing.JButton();
        MiddlePanel = new javax.swing.JPanel();
        HomePanel = new javax.swing.JPanel();
        HODHomeLabel = new javax.swing.JLabel();
        AddSubjectPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        AddSubjectSubjectIDTextbox = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        AddSubjectSubjectNameTextbox = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        AddSubjectDepartmentCombobox = new javax.swing.JComboBox<String>();
        jLabel19 = new javax.swing.JLabel();
        AddSubjectSemesterCombobox = new javax.swing.JComboBox<String>();
        AddSubjectButton = new javax.swing.JButton();
        UpdateSubjectButton = new javax.swing.JButton();
        AllotSubjectPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        AllotSubjectSubjectIDTextbox = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        AllotSubjectTeacherIDTextbox = new javax.swing.JTextField();
        AllotSubjectButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        AllotSubjectTable = new javax.swing.JTable();
        ViewSubjectPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        ViewSubjectSubjectNameTextbox = new javax.swing.JTextField();
        DeleteSubjectButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        ViewSubjectTable = new javax.swing.JTable();
        EditSubjectButton = new javax.swing.JButton();
        ViewAllotmentPanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        ViewAllotmentSubjectIDTextbox = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        ViewAllotmentTeacherIDTextbox = new javax.swing.JTextField();
        ViewAllotmentSearchButton = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        ViewAllotmentTable = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        ViewAllotmentSearchByCombobox = new javax.swing.JComboBox<String>();
        ViewAllotmentDeleteButton = new javax.swing.JButton();
        ViewStudentDetailsPanel = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        TableHolderPanel = new javax.swing.JPanel();
        StudentDetailsPanel = new javax.swing.JPanel();
        DetailsScrol = new javax.swing.JScrollPane();
        DetailsTable = new javax.swing.JTable();
        AttendencePanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        AssignmentPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        testPanel = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();
        ViewStudentDetailsBatchCombobox = new javax.swing.JComboBox<String>();
        jPanel3 = new javax.swing.JPanel();
        ViewStudentDetailsButton = new javax.swing.JButton();
        ViewStudentAttendanceButton = new javax.swing.JButton();
        ViewStudentAssignmentButton = new javax.swing.JButton();
        ViewStudentTestResultsButton = new javax.swing.JButton();
        studentDetailsPanel = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        showAllStudentsTable = new javax.swing.JTable();
        jButton12 = new javax.swing.JButton();
        SemesterShiftPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        activeSemesterTable = new javax.swing.JTable();
        DateSettingsPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        startDate = new org.jdesktop.swingx.JXDatePicker();
        jLabel7 = new javax.swing.JLabel();
        endDate = new org.jdesktop.swingx.JXDatePicker();
        jButton2 = new javax.swing.JButton();
        ShowAllTeacher = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        showAllTeacherTable = new javax.swing.JTable();
        removeHodButton2 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        AddTeacher = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        teacherRegIdTF = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        teacherNameTF = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        teacherInfoTA = new javax.swing.JTextArea();
        jLabel12 = new javax.swing.JLabel();
        teacherDeptCombo = new javax.swing.JComboBox();
        addTeacherButton = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        teacherDob = new org.jdesktop.swingx.JXDatePicker();
        jLabel26 = new javax.swing.JLabel();
        teacher_joinDate = new org.jdesktop.swingx.JXDatePicker();
        jLabel27 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        teacherQualification = new javax.swing.JComboBox();

        jLabel18.setText("jLabel18");

        SubjectChooserDialog.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SubjectChooserBasePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Choose the subject...", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12))); // NOI18N

        SubjectNameSearchTextbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SubjectNameSearchTextboxKeyReleased(evt);
            }
        });

        SubjectChooserTable.setFont(new java.awt.Font("Miriam Fixed", 0, 12)); // NOI18N
        SubjectChooserTable.setForeground(new java.awt.Color(102, 0, 0));
        SubjectChooserTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Subject_ID", "Subject_Name", "Semester", "Department"
            }
        ));
        SubjectChooserScrollPane.setViewportView(SubjectChooserTable);

        SubjectChooserOKButton.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        SubjectChooserOKButton.setText("OK");
        SubjectChooserOKButton.setBorder(null);
        SubjectChooserOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubjectChooserOKButtonActionPerformed(evt);
            }
        });

        SubjectChooserCloseButton.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        SubjectChooserCloseButton.setText("CLOSE");
        SubjectChooserCloseButton.setBorder(null);
        SubjectChooserCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubjectChooserCloseButtonActionPerformed(evt);
            }
        });

        SubjectChooserLabel.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        SubjectChooserLabel.setForeground(new java.awt.Color(102, 0, 0));
        SubjectChooserLabel.setText("Search subject");

        javax.swing.GroupLayout SubjectChooserBasePanelLayout = new javax.swing.GroupLayout(SubjectChooserBasePanel);
        SubjectChooserBasePanel.setLayout(SubjectChooserBasePanelLayout);
        SubjectChooserBasePanelLayout.setHorizontalGroup(
            SubjectChooserBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SubjectChooserBasePanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(SubjectChooserLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(SubjectChooserBasePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SubjectChooserBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SubjectNameSearchTextbox)
                    .addComponent(SubjectChooserScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SubjectChooserBasePanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(SubjectChooserOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(SubjectChooserCloseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        SubjectChooserBasePanelLayout.setVerticalGroup(
            SubjectChooserBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SubjectChooserBasePanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(SubjectChooserLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SubjectNameSearchTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SubjectChooserScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(SubjectChooserBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(SubjectChooserOKButton, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(SubjectChooserCloseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        SubjectChooserDialog.getContentPane().add(SubjectChooserBasePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        TeacherChooserDialog.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TeacherChooserBasePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Choose the teacher...", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12))); // NOI18N

        TeacherNameSearchTextbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TeacherNameSearchTextboxKeyReleased(evt);
            }
        });

        TeacherChooserTable.setFont(new java.awt.Font("Miriam Fixed", 0, 12)); // NOI18N
        TeacherChooserTable.setForeground(new java.awt.Color(102, 0, 0));
        TeacherChooserTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        TeacherChooserScrollPane.setViewportView(TeacherChooserTable);

        TeacherChooserOKButton.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        TeacherChooserOKButton.setText("OK");
        TeacherChooserOKButton.setBorder(null);
        TeacherChooserOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeacherChooserOKButtonActionPerformed(evt);
            }
        });

        TeacherChooserCloseButton.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        TeacherChooserCloseButton.setText("CLOSE");
        TeacherChooserCloseButton.setBorder(null);
        TeacherChooserCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeacherChooserCloseButtonActionPerformed(evt);
            }
        });

        TeacherChooserLabel.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        TeacherChooserLabel.setForeground(new java.awt.Color(102, 0, 0));
        TeacherChooserLabel.setText("Search teacher");

        javax.swing.GroupLayout TeacherChooserBasePanelLayout = new javax.swing.GroupLayout(TeacherChooserBasePanel);
        TeacherChooserBasePanel.setLayout(TeacherChooserBasePanelLayout);
        TeacherChooserBasePanelLayout.setHorizontalGroup(
            TeacherChooserBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TeacherChooserBasePanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(TeacherChooserLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(TeacherChooserBasePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TeacherChooserBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TeacherNameSearchTextbox)
                    .addComponent(TeacherChooserScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TeacherChooserBasePanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(TeacherChooserOKButton, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(TeacherChooserCloseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        TeacherChooserBasePanelLayout.setVerticalGroup(
            TeacherChooserBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TeacherChooserBasePanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(TeacherChooserLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TeacherNameSearchTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TeacherChooserScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(TeacherChooserBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TeacherChooserOKButton, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(TeacherChooserCloseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        TeacherChooserDialog.getContentPane().add(TeacherChooserBasePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        SubjectAttendanceDisplayDialog.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SubjectChooserBasePanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Attendance for the subject...", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12))); // NOI18N

        SubjectAttendanceDisplayTable.setFont(new java.awt.Font("Miriam Fixed", 0, 12)); // NOI18N
        SubjectAttendanceDisplayTable.setForeground(new java.awt.Color(102, 0, 0));
        SubjectAttendanceDisplayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Subject_ID", "Subject_Name", "Attendance_Percentage"
            }
        ));
        SubjectChooserScrollPane1.setViewportView(SubjectAttendanceDisplayTable);

        javax.swing.GroupLayout SubjectChooserBasePanel1Layout = new javax.swing.GroupLayout(SubjectChooserBasePanel1);
        SubjectChooserBasePanel1.setLayout(SubjectChooserBasePanel1Layout);
        SubjectChooserBasePanel1Layout.setHorizontalGroup(
            SubjectChooserBasePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SubjectChooserBasePanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SubjectChooserScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
                .addContainerGap())
        );
        SubjectChooserBasePanel1Layout.setVerticalGroup(
            SubjectChooserBasePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SubjectChooserBasePanel1Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(SubjectChooserScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        SubjectAttendanceDisplayDialog.getContentPane().add(SubjectChooserBasePanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        SubjectAssignmentDisplayDialog.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SubjectChooserBasePanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Attendance for the subject...", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12))); // NOI18N

        SubjectAssignmentDisplayTable.setFont(new java.awt.Font("Miriam Fixed", 0, 12)); // NOI18N
        SubjectAssignmentDisplayTable.setForeground(new java.awt.Color(102, 0, 0));
        SubjectAssignmentDisplayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Subject_ID", "Subject_Name", "Assignment_Marks"
            }
        ));
        SubjectChooserScrollPane2.setViewportView(SubjectAssignmentDisplayTable);

        javax.swing.GroupLayout SubjectChooserBasePanel2Layout = new javax.swing.GroupLayout(SubjectChooserBasePanel2);
        SubjectChooserBasePanel2.setLayout(SubjectChooserBasePanel2Layout);
        SubjectChooserBasePanel2Layout.setHorizontalGroup(
            SubjectChooserBasePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SubjectChooserBasePanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SubjectChooserScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
                .addContainerGap())
        );
        SubjectChooserBasePanel2Layout.setVerticalGroup(
            SubjectChooserBasePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SubjectChooserBasePanel2Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(SubjectChooserScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        SubjectAssignmentDisplayDialog.getContentPane().add(SubjectChooserBasePanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        SubjectTestDisplayDialog.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SubjectChooserBasePanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Attendance for the subject...", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12))); // NOI18N

        SubjectTestDisplayTable.setFont(new java.awt.Font("Miriam Fixed", 0, 12)); // NOI18N
        SubjectTestDisplayTable.setForeground(new java.awt.Color(102, 0, 0));
        SubjectTestDisplayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Subject_ID", "Subject_Name", "Attendance_Percentage"
            }
        ));
        SubjectChooserScrollPane3.setViewportView(SubjectTestDisplayTable);

        javax.swing.GroupLayout SubjectChooserBasePanel3Layout = new javax.swing.GroupLayout(SubjectChooserBasePanel3);
        SubjectChooserBasePanel3.setLayout(SubjectChooserBasePanel3Layout);
        SubjectChooserBasePanel3Layout.setHorizontalGroup(
            SubjectChooserBasePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SubjectChooserBasePanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SubjectChooserScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
                .addContainerGap())
        );
        SubjectChooserBasePanel3Layout.setVerticalGroup(
            SubjectChooserBasePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SubjectChooserBasePanel3Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(SubjectChooserScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        SubjectTestDisplayDialog.getContentPane().add(SubjectChooserBasePanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(6);

        HeaderPanel.setBackground(new java.awt.Color(22, 37, 47));
        HeaderPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        HeaderLogoLabel.setFont(new java.awt.Font("Tekton Pro", 1, 36)); // NOI18N
        HeaderLogoLabel.setForeground(new java.awt.Color(255, 255, 255));
        HeaderLogoLabel.setText("PTassistant");

        InfoLabelHeader2.setFont(new java.awt.Font("Miriam Fixed", 0, 10)); // NOI18N
        InfoLabelHeader2.setForeground(new java.awt.Color(204, 219, 222));
        InfoLabelHeader2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        InfoLabelHeader2.setText("Poly-Technic Attendance Management");

        javax.swing.GroupLayout HeaderPanelLayout = new javax.swing.GroupLayout(HeaderPanel);
        HeaderPanel.setLayout(HeaderPanelLayout);
        HeaderPanelLayout.setHorizontalGroup(
            HeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeaderPanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(HeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(HeaderLogoLabel)
                    .addComponent(InfoLabelHeader2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        HeaderPanelLayout.setVerticalGroup(
            HeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HeaderPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(HeaderLogoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(InfoLabelHeader2)
                .addGap(51, 51, 51))
        );

        SideOptionsPanel.setBackground(new java.awt.Color(235, 156, 39));
        SideOptionsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        RegularOptionsPanel1.setBackground(new java.awt.Color(22, 37, 47));
        RegularOptionsPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Regular options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        RegularOptionsPanel1.setLayout(new java.awt.GridLayout(10, 1));

        HomeOptionButton.setBackground(new java.awt.Color(255, 255, 255));
        HomeOptionButton.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        HomeOptionButton.setForeground(new java.awt.Color(0, 53, 91));
        HomeOptionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images24x24/1486299512_Streamline-18.png"))); // NOI18N
        HomeOptionButton.setText("Home");
        HomeOptionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HomeOptionButtonMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                HomeOptionButtonMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                HomeOptionButtonMouseReleased(evt);
            }
        });
        HomeOptionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HomeOptionButtonActionPerformed(evt);
            }
        });
        RegularOptionsPanel1.add(HomeOptionButton);

        AddSubjectOptionButton.setBackground(new java.awt.Color(255, 255, 255));
        AddSubjectOptionButton.setFont(new java.awt.Font("Miriam Fixed", 1, 15)); // NOI18N
        AddSubjectOptionButton.setForeground(new java.awt.Color(0, 53, 91));
        AddSubjectOptionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images32x32/1488760786_flat-style-circle-add.png"))); // NOI18N
        AddSubjectOptionButton.setText("Add subject");
        AddSubjectOptionButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        AddSubjectOptionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AddSubjectOptionButtonMouseClicked(evt);
            }
        });
        AddSubjectOptionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddSubjectOptionButtonActionPerformed(evt);
            }
        });
        RegularOptionsPanel1.add(AddSubjectOptionButton);

        AllotSubjectOptionButton1.setBackground(new java.awt.Color(255, 255, 255));
        AllotSubjectOptionButton1.setFont(new java.awt.Font("Miriam Fixed", 1, 15)); // NOI18N
        AllotSubjectOptionButton1.setForeground(new java.awt.Color(0, 53, 91));
        AllotSubjectOptionButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images32x32/1488760730_More_2.png"))); // NOI18N
        AllotSubjectOptionButton1.setText("Add teacher");
        AllotSubjectOptionButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        AllotSubjectOptionButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AllotSubjectOptionButton1MouseClicked(evt);
            }
        });
        AllotSubjectOptionButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AllotSubjectOptionButton1ActionPerformed(evt);
            }
        });
        RegularOptionsPanel1.add(AllotSubjectOptionButton1);

        AllotSubjectOptionButton.setBackground(new java.awt.Color(255, 255, 255));
        AllotSubjectOptionButton.setFont(new java.awt.Font("Miriam Fixed", 1, 15)); // NOI18N
        AllotSubjectOptionButton.setForeground(new java.awt.Color(0, 53, 91));
        AllotSubjectOptionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images32x32/1488760730_More_2.png"))); // NOI18N
        AllotSubjectOptionButton.setText("Subject allocation");
        AllotSubjectOptionButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        AllotSubjectOptionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AllotSubjectOptionButtonMouseClicked(evt);
            }
        });
        AllotSubjectOptionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AllotSubjectOptionButtonActionPerformed(evt);
            }
        });
        RegularOptionsPanel1.add(AllotSubjectOptionButton);

        ViewSubjectOptionButton.setBackground(new java.awt.Color(255, 255, 255));
        ViewSubjectOptionButton.setFont(new java.awt.Font("Miriam Fixed", 1, 15)); // NOI18N
        ViewSubjectOptionButton.setForeground(new java.awt.Color(0, 53, 91));
        ViewSubjectOptionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images32x32/1488760886_eye.png"))); // NOI18N
        ViewSubjectOptionButton.setText("View subject");
        ViewSubjectOptionButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ViewSubjectOptionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ViewSubjectOptionButtonMouseClicked(evt);
            }
        });
        ViewSubjectOptionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewSubjectOptionButtonActionPerformed(evt);
            }
        });
        RegularOptionsPanel1.add(ViewSubjectOptionButton);

        ViewAllotmentOptionButton.setBackground(new java.awt.Color(255, 255, 255));
        ViewAllotmentOptionButton.setFont(new java.awt.Font("Miriam Fixed", 1, 15)); // NOI18N
        ViewAllotmentOptionButton.setForeground(new java.awt.Color(0, 53, 91));
        ViewAllotmentOptionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images32x32/1488760898_eye_preview_see_seen_view.png"))); // NOI18N
        ViewAllotmentOptionButton.setText("View  allocation");
        ViewAllotmentOptionButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ViewAllotmentOptionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ViewAllotmentOptionButtonMouseClicked(evt);
            }
        });
        ViewAllotmentOptionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewAllotmentOptionButtonActionPerformed(evt);
            }
        });
        RegularOptionsPanel1.add(ViewAllotmentOptionButton);

        ViewStudentDetailsOptionButton.setBackground(new java.awt.Color(255, 255, 255));
        ViewStudentDetailsOptionButton.setFont(new java.awt.Font("Miriam Fixed", 1, 15)); // NOI18N
        ViewStudentDetailsOptionButton.setForeground(new java.awt.Color(0, 53, 91));
        ViewStudentDetailsOptionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images32x32/1489695340_office-15.png"))); // NOI18N
        ViewStudentDetailsOptionButton.setText("Student Details");
        ViewStudentDetailsOptionButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ViewStudentDetailsOptionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ViewStudentDetailsOptionButtonMouseClicked(evt);
            }
        });
        ViewStudentDetailsOptionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewStudentDetailsOptionButtonActionPerformed(evt);
            }
        });
        RegularOptionsPanel1.add(ViewStudentDetailsOptionButton);

        ViewStudentDetailsOptionButton1.setBackground(new java.awt.Color(255, 255, 255));
        ViewStudentDetailsOptionButton1.setFont(new java.awt.Font("Miriam Fixed", 1, 15)); // NOI18N
        ViewStudentDetailsOptionButton1.setForeground(new java.awt.Color(0, 53, 91));
        ViewStudentDetailsOptionButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images32x32/1489695340_office-15.png"))); // NOI18N
        ViewStudentDetailsOptionButton1.setText("Students List");
        ViewStudentDetailsOptionButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ViewStudentDetailsOptionButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ViewStudentDetailsOptionButton1MouseClicked(evt);
            }
        });
        ViewStudentDetailsOptionButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewStudentDetailsOptionButton1ActionPerformed(evt);
            }
        });
        RegularOptionsPanel1.add(ViewStudentDetailsOptionButton1);

        ViewStudentDetailsOptionButton2.setBackground(new java.awt.Color(255, 255, 255));
        ViewStudentDetailsOptionButton2.setFont(new java.awt.Font("Miriam Fixed", 1, 15)); // NOI18N
        ViewStudentDetailsOptionButton2.setForeground(new java.awt.Color(0, 53, 91));
        ViewStudentDetailsOptionButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images32x32/1489695272_More.png"))); // NOI18N
        ViewStudentDetailsOptionButton2.setText("Semester shift");
        ViewStudentDetailsOptionButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ViewStudentDetailsOptionButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ViewStudentDetailsOptionButton2MouseClicked(evt);
            }
        });
        ViewStudentDetailsOptionButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewStudentDetailsOptionButton2ActionPerformed(evt);
            }
        });
        RegularOptionsPanel1.add(ViewStudentDetailsOptionButton2);

        ViewStudentDetailsOptionButton3.setBackground(new java.awt.Color(255, 255, 255));
        ViewStudentDetailsOptionButton3.setFont(new java.awt.Font("Miriam Fixed", 1, 15)); // NOI18N
        ViewStudentDetailsOptionButton3.setForeground(new java.awt.Color(0, 53, 91));
        ViewStudentDetailsOptionButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images32x32/1488760886_eye.png"))); // NOI18N
        ViewStudentDetailsOptionButton3.setText("Date settings");
        ViewStudentDetailsOptionButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ViewStudentDetailsOptionButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ViewStudentDetailsOptionButton3MouseClicked(evt);
            }
        });
        ViewStudentDetailsOptionButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewStudentDetailsOptionButton3ActionPerformed(evt);
            }
        });
        RegularOptionsPanel1.add(ViewStudentDetailsOptionButton3);

        OtherOptionsPanel1.setBackground(new java.awt.Color(22, 37, 47));
        OtherOptionsPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Other options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        OtherOptionsPanel1.setLayout(new java.awt.GridLayout(2, 0));

        LogoutButton.setBackground(new java.awt.Color(255, 255, 255));
        LogoutButton.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        LogoutButton.setForeground(new java.awt.Color(0, 53, 91));
        LogoutButton.setText("Logout");
        LogoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LogoutButtonMouseClicked(evt);
            }
        });
        LogoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutButtonActionPerformed(evt);
            }
        });
        OtherOptionsPanel1.add(LogoutButton);

        HODChangePasswordButton.setBackground(new java.awt.Color(255, 255, 255));
        HODChangePasswordButton.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        HODChangePasswordButton.setForeground(new java.awt.Color(0, 53, 91));
        HODChangePasswordButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/POTAM_Images/1485992392_free-38.png"))); // NOI18N
        HODChangePasswordButton.setText("Change password");
        HODChangePasswordButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HODChangePasswordButtonMouseClicked(evt);
            }
        });
        HODChangePasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HODChangePasswordButtonActionPerformed(evt);
            }
        });
        OtherOptionsPanel1.add(HODChangePasswordButton);

        javax.swing.GroupLayout SideOptionsPanelLayout = new javax.swing.GroupLayout(SideOptionsPanel);
        SideOptionsPanel.setLayout(SideOptionsPanelLayout);
        SideOptionsPanelLayout.setHorizontalGroup(
            SideOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SideOptionsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(SideOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RegularOptionsPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(OtherOptionsPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        SideOptionsPanelLayout.setVerticalGroup(
            SideOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SideOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RegularOptionsPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(OtherOptionsPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        MiddlePanel.setBackground(new java.awt.Color(255, 255, 255));
        MiddlePanel.setLayout(new java.awt.CardLayout());

        HomePanel.setBackground(new java.awt.Color(255, 255, 255));

        HODHomeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HODHomeLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/POTAM_Images/1488324366_computer.png"))); // NOI18N

        javax.swing.GroupLayout HomePanelLayout = new javax.swing.GroupLayout(HomePanel);
        HomePanel.setLayout(HomePanelLayout);
        HomePanelLayout.setHorizontalGroup(
            HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 703, Short.MAX_VALUE)
            .addGroup(HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(HODHomeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE))
        );
        HomePanelLayout.setVerticalGroup(
            HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 564, Short.MAX_VALUE)
            .addGroup(HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(HODHomeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        MiddlePanel.add(HomePanel, "card7");

        AddSubjectPanel.setBackground(new java.awt.Color(255, 255, 255));
        AddSubjectPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Add new subject", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Miriam Fixed", 0, 12))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(204, 219, 222));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jLabel1.setText("Subject Code");

        AddSubjectSubjectIDTextbox.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jLabel2.setText("Subject Name");

        AddSubjectSubjectNameTextbox.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jLabel3.setText("Department");

        jLabel19.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jLabel19.setText("Semester");

        AddSubjectSemesterCombobox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6" }));
        AddSubjectSemesterCombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddSubjectSemesterComboboxActionPerformed(evt);
            }
        });

        AddSubjectButton.setBackground(new java.awt.Color(202, 74, 9));
        AddSubjectButton.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        AddSubjectButton.setForeground(new java.awt.Color(255, 255, 255));
        AddSubjectButton.setText("Add");
        AddSubjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddSubjectButtonActionPerformed(evt);
            }
        });

        UpdateSubjectButton.setBackground(new java.awt.Color(0, 102, 102));
        UpdateSubjectButton.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        UpdateSubjectButton.setForeground(new java.awt.Color(255, 255, 255));
        UpdateSubjectButton.setText("Update");
        UpdateSubjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateSubjectButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(AddSubjectSubjectIDTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(AddSubjectDepartmentCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(AddSubjectSubjectNameTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(AddSubjectSemesterCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(AddSubjectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(UpdateSubjectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(183, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AddSubjectSubjectIDTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AddSubjectSubjectNameTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AddSubjectDepartmentCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AddSubjectSemesterCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddSubjectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UpdateSubjectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(218, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AddSubjectPanelLayout = new javax.swing.GroupLayout(AddSubjectPanel);
        AddSubjectPanel.setLayout(AddSubjectPanelLayout);
        AddSubjectPanelLayout.setHorizontalGroup(
            AddSubjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddSubjectPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        AddSubjectPanelLayout.setVerticalGroup(
            AddSubjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddSubjectPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        MiddlePanel.add(AddSubjectPanel, "card2");

        AllotSubjectPanel.setBackground(new java.awt.Color(255, 255, 255));
        AllotSubjectPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Allot subject to teacher", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Miriam Fixed", 0, 12))); // NOI18N

        jPanel4.setBackground(new java.awt.Color(204, 219, 222));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel20.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jLabel20.setText("Subject ID");

        AllotSubjectSubjectIDTextbox.setEditable(false);
        AllotSubjectSubjectIDTextbox.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        AllotSubjectSubjectIDTextbox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AllotSubjectSubjectIDTextboxMouseClicked(evt);
            }
        });
        AllotSubjectSubjectIDTextbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AllotSubjectSubjectIDTextboxActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jLabel21.setText("Teacher ID");

        AllotSubjectTeacherIDTextbox.setEditable(false);
        AllotSubjectTeacherIDTextbox.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        AllotSubjectTeacherIDTextbox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AllotSubjectTeacherIDTextboxMouseClicked(evt);
            }
        });

        AllotSubjectButton.setBackground(new java.awt.Color(202, 74, 9));
        AllotSubjectButton.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        AllotSubjectButton.setForeground(new java.awt.Color(255, 255, 255));
        AllotSubjectButton.setText("Allot");
        AllotSubjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AllotSubjectButtonActionPerformed(evt);
            }
        });

        AllotSubjectTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Assign_ID", "Subject_Name", "Teacher_Name"
            }
        ));
        AllotSubjectTable.setRowHeight(30);
        jScrollPane1.setViewportView(AllotSubjectTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(AllotSubjectTeacherIDTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(AllotSubjectSubjectIDTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)))
                        .addComponent(AllotSubjectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(AllotSubjectSubjectIDTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(AllotSubjectTeacherIDTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(AllotSubjectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout AllotSubjectPanelLayout = new javax.swing.GroupLayout(AllotSubjectPanel);
        AllotSubjectPanel.setLayout(AllotSubjectPanelLayout);
        AllotSubjectPanelLayout.setHorizontalGroup(
            AllotSubjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AllotSubjectPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        AllotSubjectPanelLayout.setVerticalGroup(
            AllotSubjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AllotSubjectPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        MiddlePanel.add(AllotSubjectPanel, "card2");

        ViewSubjectPanel.setBackground(new java.awt.Color(255, 255, 255));
        ViewSubjectPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "View subject details", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Miriam Fixed", 0, 12))); // NOI18N

        jPanel5.setBackground(new java.awt.Color(204, 219, 222));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel22.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jLabel22.setText("Subject Name");

        ViewSubjectSubjectNameTextbox.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        ViewSubjectSubjectNameTextbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ViewSubjectSubjectNameTextboxKeyReleased(evt);
            }
        });

        DeleteSubjectButton.setBackground(new java.awt.Color(202, 74, 9));
        DeleteSubjectButton.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        DeleteSubjectButton.setForeground(new java.awt.Color(255, 255, 255));
        DeleteSubjectButton.setText("Delete");
        DeleteSubjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteSubjectButtonActionPerformed(evt);
            }
        });

        ViewSubjectTable.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ViewSubjectTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Subject_ID", "Subject_Name", "Semester", "Department"
            }
        ));
        ViewSubjectTable.setRowHeight(30);
        jScrollPane6.setViewportView(ViewSubjectTable);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
        );

        EditSubjectButton.setBackground(new java.awt.Color(0, 102, 102));
        EditSubjectButton.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        EditSubjectButton.setForeground(new java.awt.Color(255, 255, 255));
        EditSubjectButton.setText("Edit");
        EditSubjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditSubjectButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ViewSubjectSubjectNameTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(DeleteSubjectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(EditSubjectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ViewSubjectSubjectNameTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel22))
                    .addComponent(DeleteSubjectButton, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(EditSubjectButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout ViewSubjectPanelLayout = new javax.swing.GroupLayout(ViewSubjectPanel);
        ViewSubjectPanel.setLayout(ViewSubjectPanelLayout);
        ViewSubjectPanelLayout.setHorizontalGroup(
            ViewSubjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewSubjectPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        ViewSubjectPanelLayout.setVerticalGroup(
            ViewSubjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewSubjectPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        MiddlePanel.add(ViewSubjectPanel, "card2");

        ViewAllotmentPanel.setBackground(new java.awt.Color(255, 255, 255));
        ViewAllotmentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "View subject allotment details", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Miriam Fixed", 0, 12))); // NOI18N

        jPanel7.setBackground(new java.awt.Color(204, 219, 222));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel23.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jLabel23.setText("Subject ID");

        ViewAllotmentSubjectIDTextbox.setEditable(false);
        ViewAllotmentSubjectIDTextbox.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        ViewAllotmentSubjectIDTextbox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ViewAllotmentSubjectIDTextboxMouseClicked(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jLabel24.setText("Teacher ID");

        ViewAllotmentTeacherIDTextbox.setEditable(false);
        ViewAllotmentTeacherIDTextbox.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        ViewAllotmentTeacherIDTextbox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ViewAllotmentTeacherIDTextboxMouseClicked(evt);
            }
        });

        ViewAllotmentSearchButton.setBackground(new java.awt.Color(202, 74, 9));
        ViewAllotmentSearchButton.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        ViewAllotmentSearchButton.setForeground(new java.awt.Color(255, 255, 255));
        ViewAllotmentSearchButton.setText("Search");
        ViewAllotmentSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewAllotmentSearchButtonActionPerformed(evt);
            }
        });

        ViewAllotmentTable.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ViewAllotmentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Assign_ID", "Subject", "Teacher"
            }
        ));
        ViewAllotmentTable.setRowHeight(30);
        jScrollPane4.setViewportView(ViewAllotmentTable);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
        );

        jLabel25.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jLabel25.setText("Search by");

        ViewAllotmentSearchByCombobox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Subject", "Teacher" }));

        ViewAllotmentDeleteButton.setBackground(new java.awt.Color(204, 204, 204));
        ViewAllotmentDeleteButton.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        ViewAllotmentDeleteButton.setText("Delete");
        ViewAllotmentDeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewAllotmentDeleteButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(ViewAllotmentSubjectIDTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(ViewAllotmentTeacherIDTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ViewAllotmentSearchByCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(ViewAllotmentSearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ViewAllotmentDeleteButton)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addComponent(ViewAllotmentSearchByCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ViewAllotmentSubjectIDTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel23)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ViewAllotmentTeacherIDTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel24))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ViewAllotmentSearchButton)
                        .addComponent(ViewAllotmentDeleteButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout ViewAllotmentPanelLayout = new javax.swing.GroupLayout(ViewAllotmentPanel);
        ViewAllotmentPanel.setLayout(ViewAllotmentPanelLayout);
        ViewAllotmentPanelLayout.setHorizontalGroup(
            ViewAllotmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewAllotmentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        ViewAllotmentPanelLayout.setVerticalGroup(
            ViewAllotmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewAllotmentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        MiddlePanel.add(ViewAllotmentPanel, "card2");

        ViewStudentDetailsPanel.setBackground(new java.awt.Color(255, 255, 255));
        ViewStudentDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "View students details", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Miriam Fixed", 0, 12))); // NOI18N

        jPanel9.setBackground(new java.awt.Color(204, 219, 222));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        TableHolderPanel.setLayout(new java.awt.CardLayout());

        DetailsTable.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        DetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student id", "Student name", "Student info", "Semester"
            }
        ));
        DetailsTable.setRowHeight(30);
        DetailsScrol.setViewportView(DetailsTable);

        javax.swing.GroupLayout StudentDetailsPanelLayout = new javax.swing.GroupLayout(StudentDetailsPanel);
        StudentDetailsPanel.setLayout(StudentDetailsPanelLayout);
        StudentDetailsPanelLayout.setHorizontalGroup(
            StudentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 647, Short.MAX_VALUE)
            .addGroup(StudentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(StudentDetailsPanelLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(DetailsScrol, javax.swing.GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
        StudentDetailsPanelLayout.setVerticalGroup(
            StudentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 428, Short.MAX_VALUE)
            .addGroup(StudentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(StudentDetailsPanelLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(DetailsScrol, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );

        TableHolderPanel.add(StudentDetailsPanel, "card3");

        jTable1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Name", "Persentage"
            }
        ));
        jTable1.setRowHeight(30);
        jScrollPane3.setViewportView(jTable1);

        javax.swing.GroupLayout AttendencePanelLayout = new javax.swing.GroupLayout(AttendencePanel);
        AttendencePanel.setLayout(AttendencePanelLayout);
        AttendencePanelLayout.setHorizontalGroup(
            AttendencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE)
        );
        AttendencePanelLayout.setVerticalGroup(
            AttendencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
        );

        TableHolderPanel.add(AttendencePanel, "card4");

        jTable2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "ID", "Name", "Mark ( out of 15 )"
            }
        ));
        jTable2.setRowHeight(30);
        jScrollPane5.setViewportView(jTable2);

        javax.swing.GroupLayout AssignmentPanelLayout = new javax.swing.GroupLayout(AssignmentPanel);
        AssignmentPanel.setLayout(AssignmentPanelLayout);
        AssignmentPanelLayout.setHorizontalGroup(
            AssignmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE)
        );
        AssignmentPanelLayout.setVerticalGroup(
            AssignmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
        );

        TableHolderPanel.add(AssignmentPanel, "card5");

        jTable3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "NAME", "MARK ( out of 25 )"
            }
        ));
        jTable3.setRowHeight(30);
        jScrollPane7.setViewportView(jTable3);

        javax.swing.GroupLayout testPanelLayout = new javax.swing.GroupLayout(testPanel);
        testPanel.setLayout(testPanelLayout);
        testPanelLayout.setHorizontalGroup(
            testPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE)
        );
        testPanelLayout.setVerticalGroup(
            testPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
        );

        TableHolderPanel.add(testPanel, "card6");

        jLabel28.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jLabel28.setText("Batch");

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        ViewStudentDetailsButton.setBackground(new java.awt.Color(202, 74, 9));
        ViewStudentDetailsButton.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        ViewStudentDetailsButton.setForeground(new java.awt.Color(255, 255, 255));
        ViewStudentDetailsButton.setText("Student info");
        ViewStudentDetailsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewStudentDetailsButtonActionPerformed(evt);
            }
        });
        jPanel3.add(ViewStudentDetailsButton);

        ViewStudentAttendanceButton.setBackground(new java.awt.Color(0, 51, 51));
        ViewStudentAttendanceButton.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        ViewStudentAttendanceButton.setForeground(new java.awt.Color(255, 255, 255));
        ViewStudentAttendanceButton.setText("Attendance");
        ViewStudentAttendanceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewStudentAttendanceButtonActionPerformed(evt);
            }
        });
        jPanel3.add(ViewStudentAttendanceButton);

        ViewStudentAssignmentButton.setBackground(new java.awt.Color(51, 51, 255));
        ViewStudentAssignmentButton.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        ViewStudentAssignmentButton.setForeground(new java.awt.Color(255, 255, 255));
        ViewStudentAssignmentButton.setText("Assignment");
        ViewStudentAssignmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewStudentAssignmentButtonActionPerformed(evt);
            }
        });
        jPanel3.add(ViewStudentAssignmentButton);

        ViewStudentTestResultsButton.setBackground(new java.awt.Color(51, 102, 0));
        ViewStudentTestResultsButton.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        ViewStudentTestResultsButton.setForeground(new java.awt.Color(255, 255, 255));
        ViewStudentTestResultsButton.setText("Test results");
        ViewStudentTestResultsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewStudentTestResultsButtonActionPerformed(evt);
            }
        });
        jPanel3.add(ViewStudentTestResultsButton);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TableHolderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ViewStudentDetailsBatchCombobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 532, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ViewStudentDetailsBatchCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(14, 14, 14)
                .addComponent(TableHolderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout ViewStudentDetailsPanelLayout = new javax.swing.GroupLayout(ViewStudentDetailsPanel);
        ViewStudentDetailsPanel.setLayout(ViewStudentDetailsPanelLayout);
        ViewStudentDetailsPanelLayout.setHorizontalGroup(
            ViewStudentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewStudentDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        ViewStudentDetailsPanelLayout.setVerticalGroup(
            ViewStudentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewStudentDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        MiddlePanel.add(ViewStudentDetailsPanel, "card2");

        showAllStudentsTable.setAutoCreateRowSorter(true);
        showAllStudentsTable.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        showAllStudentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "auto id", "Registar number", "Name", "Personal info", "Department", "semester", "Batch name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        showAllStudentsTable.setGridColor(new java.awt.Color(204, 204, 204));
        showAllStudentsTable.setRowHeight(30);
        jScrollPane8.setViewportView(showAllStudentsTable);

        jButton12.setText("Student details");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout studentDetailsPanelLayout = new javax.swing.GroupLayout(studentDetailsPanel);
        studentDetailsPanel.setLayout(studentDetailsPanelLayout);
        studentDetailsPanelLayout.setHorizontalGroup(
            studentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, studentDetailsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton12)
                .addContainerGap())
        );
        studentDetailsPanelLayout.setVerticalGroup(
            studentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, studentDetailsPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton12)
                .addGap(12, 12, 12))
        );

        MiddlePanel.add(studentDetailsPanel, "card8");

        SemesterShiftPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Semester shift", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jButton1.setBackground(new java.awt.Color(204, 255, 204));
        jButton1.setText("Shift to next semester");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Warning : All data that related to current semester will cleared premenatntly");

        activeSemesterTable.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        activeSemesterTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Batch id", "Batch name", "Batch year", "current semester"
            }
        ));
        activeSemesterTable.setRowHeight(30);
        jScrollPane2.setViewportView(activeSemesterTable);

        javax.swing.GroupLayout SemesterShiftPanelLayout = new javax.swing.GroupLayout(SemesterShiftPanel);
        SemesterShiftPanel.setLayout(SemesterShiftPanelLayout);
        SemesterShiftPanelLayout.setHorizontalGroup(
            SemesterShiftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SemesterShiftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPane2)
        );
        SemesterShiftPanelLayout.setVerticalGroup(
            SemesterShiftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SemesterShiftPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SemesterShiftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(SemesterShiftPanelLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        MiddlePanel.add(SemesterShiftPanel, "card9");

        DateSettingsPanel.setBackground(new java.awt.Color(255, 255, 255));
        DateSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Date settings", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        DateSettingsPanel.setToolTipText("");

        jLabel5.setText("specify the starting and ending date to add/modify attendence,assignment,test.");

        jLabel6.setText("Entry starting date");

        jLabel7.setText("Entry ending date");

        jButton2.setBackground(new java.awt.Color(204, 255, 204));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton2.setText("Save changes");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout DateSettingsPanelLayout = new javax.swing.GroupLayout(DateSettingsPanel);
        DateSettingsPanel.setLayout(DateSettingsPanelLayout);
        DateSettingsPanelLayout.setHorizontalGroup(
            DateSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DateSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DateSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
                    .addGroup(DateSettingsPanelLayout.createSequentialGroup()
                        .addGroup(DateSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(DateSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(endDate, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                            .addComponent(startDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(DateSettingsPanelLayout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DateSettingsPanelLayout.setVerticalGroup(
            DateSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DateSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(22, 22, 22)
                .addGroup(DateSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(startDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(DateSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(endDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(373, Short.MAX_VALUE))
        );

        MiddlePanel.add(DateSettingsPanel, "card10");

        showAllTeacherTable.setAutoCreateRowSorter(true);
        showAllTeacherTable.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        showAllTeacherTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Teacher id", "Teacher name", "Personal info", "Department"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        showAllTeacherTable.setGridColor(new java.awt.Color(204, 204, 204));
        showAllTeacherTable.setRowHeight(30);
        jScrollPane9.setViewportView(showAllTeacherTable);

        removeHodButton2.setText("Remove teacher");
        removeHodButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeHodButton2ActionPerformed(evt);
            }
        });

        jButton9.setText("Print");

        javax.swing.GroupLayout ShowAllTeacherLayout = new javax.swing.GroupLayout(ShowAllTeacher);
        ShowAllTeacher.setLayout(ShowAllTeacherLayout);
        ShowAllTeacherLayout.setHorizontalGroup(
            ShowAllTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE)
            .addGroup(ShowAllTeacherLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(removeHodButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        ShowAllTeacherLayout.setVerticalGroup(
            ShowAllTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ShowAllTeacherLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ShowAllTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9)
                    .addComponent(removeHodButton2))
                .addGap(12, 12, 12))
        );

        MiddlePanel.add(ShowAllTeacher, "card11");

        AddTeacher.setBackground(new java.awt.Color(255, 255, 255));
        AddTeacher.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ADD TEACHER", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        jLabel9.setText("Registered Id");

        jLabel10.setText("Name");

        jLabel11.setText("Personal info");

        teacherInfoTA.setColumns(20);
        teacherInfoTA.setRows(5);
        jScrollPane10.setViewportView(teacherInfoTA);

        jLabel12.setText("Department");

        teacherDeptCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        addTeacherButton.setBackground(new java.awt.Color(204, 255, 204));
        addTeacherButton.setText("Add Teacher");
        addTeacherButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTeacherButtonActionPerformed(evt);
            }
        });

        jButton8.setText("Cancel");

        jLabel26.setText("DOB");

        jLabel27.setText("Joining date");

        jLabel29.setText("Qualification");

        teacherQualification.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mtech", "Btech", "dipoama", "other degree", " " }));

        javax.swing.GroupLayout AddTeacherLayout = new javax.swing.GroupLayout(AddTeacher);
        AddTeacher.setLayout(AddTeacherLayout);
        AddTeacherLayout.setHorizontalGroup(
            AddTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddTeacherLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AddTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AddTeacherLayout.createSequentialGroup()
                        .addGroup(AddTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(teacher_joinDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel27))
                        .addGap(50, 50, 50)
                        .addGroup(AddTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(AddTeacherLayout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(teacherQualification, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(AddTeacherLayout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addGap(368, 368, 368))
                    .addComponent(teacherDob, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(teacherRegIdTF)
                    .addComponent(teacherNameTF)
                    .addComponent(teacherDeptCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane10)
                    .addGroup(AddTeacherLayout.createSequentialGroup()
                        .addGroup(AddTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AddTeacherLayout.createSequentialGroup()
                        .addComponent(addTeacherButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(292, 292, 292))
        );
        AddTeacherLayout.setVerticalGroup(
            AddTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddTeacherLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teacherRegIdTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teacherNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(teacherDob, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teacherDeptCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(AddTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jLabel29))
                .addGap(5, 5, 5)
                .addGroup(AddTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(teacher_joinDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(teacherQualification, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(AddTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addTeacherButton)
                    .addComponent(jButton8))
                .addContainerGap(98, Short.MAX_VALUE))
        );

        MiddlePanel.add(AddTeacher, "card5");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(HeaderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(SideOptionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(MiddlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(HeaderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SideOptionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(MiddlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void HomeOptionButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HomeOptionButtonMouseClicked
        // TODO add your handling code here:
        //this.ButtonSelection(HomeOptionButton, null);
    }//GEN-LAST:event_HomeOptionButtonMouseClicked

    private void HomeOptionButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HomeOptionButtonMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_HomeOptionButtonMouseReleased

    private void HomeOptionButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HomeOptionButtonMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_HomeOptionButtonMouseExited

    private void AllotSubjectOptionButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AllotSubjectOptionButtonMouseClicked
        // TODO add your handling code here:
        //this.ButtonSelection(MonthlyFeeOptionButton, null);
    }//GEN-LAST:event_AllotSubjectOptionButtonMouseClicked

    private void AddSubjectOptionButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AddSubjectOptionButtonMouseClicked
        // TODO add your handling code here:
        //this.ButtonSelection(CreditTransactionOptionButton, null);
    }//GEN-LAST:event_AddSubjectOptionButtonMouseClicked

    private void LogoutButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LogoutButtonMouseClicked
        // TODO add your handling code here:
        //this.ButtonSelection(LogoutButton, null);
    }//GEN-LAST:event_LogoutButtonMouseClicked

    private void HomeOptionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HomeOptionButtonActionPerformed
        this.SetPanelVisibility(HomePanel);
    }//GEN-LAST:event_HomeOptionButtonActionPerformed

    private void LogoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutButtonActionPerformed
        new LoginForm().setVisible(true);
        dispose();
    }//GEN-LAST:event_LogoutButtonActionPerformed

    private void AllotSubjectOptionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AllotSubjectOptionButtonActionPerformed
        this.SetPanelVisibility(AllotSubjectPanel);
        this.PopulateSubjectAllotmentTable(AllotSubjectTable);        
    }//GEN-LAST:event_AllotSubjectOptionButtonActionPerformed

    private void AddSubjectOptionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddSubjectOptionButtonActionPerformed
       this.SetPanelVisibility(AddSubjectPanel);
       AddSubjectButton.setVisible(true);
       UpdateSubjectButton.setVisible(false);
       AddSubjectSubjectIDTextbox.setEditable(true);
       // Populating the combobox
       this.PopulateDepartmentCombobox(AddSubjectDepartmentCombobox);
       //this.PopulateBatchCombobox(AddSubjectBatchCombobox);
    }//GEN-LAST:event_AddSubjectOptionButtonActionPerformed

    private void ViewSubjectOptionButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewSubjectOptionButtonMouseClicked
        
    }//GEN-LAST:event_ViewSubjectOptionButtonMouseClicked

    private void ViewSubjectOptionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewSubjectOptionButtonActionPerformed
        this.SetPanelVisibility(ViewSubjectPanel);     
        this.PopulateSubjectDetails(ViewSubjectTable);
    }//GEN-LAST:event_ViewSubjectOptionButtonActionPerformed

    private void ViewAllotmentOptionButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewAllotmentOptionButtonMouseClicked
        
    }//GEN-LAST:event_ViewAllotmentOptionButtonMouseClicked

    private void ViewAllotmentOptionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewAllotmentOptionButtonActionPerformed
        this.SetPanelVisibility(ViewAllotmentPanel);
        this.PopulateSubjectAllotmentTable(ViewAllotmentTable); 
    }//GEN-LAST:event_ViewAllotmentOptionButtonActionPerformed

    private void AddSubjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddSubjectButtonActionPerformed
        String subjectID = AddSubjectSubjectIDTextbox.getText();
        String subjectName = AddSubjectSubjectNameTextbox.getText();
        String department =(String)AddSubjectDepartmentCombobox.getSelectedItem();
        //String batch =(String)AddSubjectBatchCombobox.getSelectedItem();
        String semester = (String)AddSubjectSemesterCombobox.getSelectedItem();     
        
        // Validating the texts        
        if (subjectID.isEmpty() || subjectName.isEmpty() || department.isEmpty()  || semester.isEmpty())
        {
            JOptionPane.showMessageDialog(rootPane, "Empty fields prevent the addition of subject! Please check again.");
        }
        else
        {
            try
            {
                DatabaseManager dbManager = new DatabaseManager();
                boolean status = dbManager.AddSubject(subjectID, subjectName, department, semester);
                if (status)
                {
                    JOptionPane.showMessageDialog(rootPane, "New subject added!");
                    AddSubjectSubjectIDTextbox.setText(null);
                    AddSubjectSubjectNameTextbox.setText(null);
                }
                else
                {
                    JOptionPane.showMessageDialog(rootPane, "Subject could not be added!");
                }
            }
            catch(ClassNotFoundException | SQLException e)
            {

               Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, e);  
            }
        }        
    }//GEN-LAST:event_AddSubjectButtonActionPerformed

    // Alloting the subject to a teacher
    private void AllotSubjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AllotSubjectButtonActionPerformed
        String subjectID = AllotSubjectSubjectIDTextbox.getText();
        String teacherID = AllotSubjectTeacherIDTextbox.getText();
        if (!subjectID.isEmpty() && !teacherID.isEmpty())
        {
            try
            {
                int subID = Integer.parseInt(subjectID);
                int teachID = Integer.parseInt(teacherID);
                DatabaseManager dbManager = new DatabaseManager();
                boolean status = dbManager.AssignSubjectToTeacher(subID, teachID);                
                if (status)
                {
                    JOptionPane.showMessageDialog(rootPane, "New allotment added!");
                    ResultSet rs = new DatabaseManager().RetrieveSubjectToTeacherAllotmentDetails();
                    DefaultTableModel dm = (DefaultTableModel)AllotSubjectTable.getModel();
                    if(rs.isBeforeFirst())
                    {
                        dm.setRowCount(0);
                        while(rs.next())
                        {
                            String subjectColumn = new DatabaseManager().GetSubjectName(rs.getString("Assign_Subject_ID"));
                            String teacherColumn = new DatabaseManager().GetTeacherName(rs.getString("Assign_Teacher_ID"));
                            dm.addRow
                                    (
                                        new Object[]
                                                {
                                                    rs.getString("Assign_ID"),
                                                    subjectColumn,
                                                    teacherColumn
                                                }
                                    );
                        }
                    }
                    // Setting te textfields empty for new allotment
                    AllotSubjectSubjectIDTextbox.setText(null);
                    AllotSubjectTeacherIDTextbox.setText(null);
                }
                else
                {
                    JOptionPane.showMessageDialog(rootPane, "Allotment could not be made!");
                }
            }
            catch(ClassNotFoundException | SQLException e)
            {

               Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, e);  
            }
            
        }
        else
        {
            JOptionPane.showMessageDialog(rootPane, "Please select a Subject ID and Teacher ID to continue");
        }        
    }//GEN-LAST:event_AllotSubjectButtonActionPerformed

    // Deleting the subject
    private void DeleteSubjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteSubjectButtonActionPerformed
        // On clicking the delete button in view subject panel
        if( ViewSubjectTable.getSelectedRow() >= 0)
        {
            // Getting the value of the subjectid
            int subjectID = Integer.parseInt(String.valueOf(ViewSubjectTable.getValueAt(ViewSubjectTable.getSelectedRow() ,0)));
            try
            {
                DatabaseManager dbManager = new DatabaseManager();
                boolean status = dbManager.DeleteSubject(subjectID);
                if (status)
                {
                    JOptionPane.showMessageDialog(rootPane, "Subject deleted!");
                    this.PopulateSubjectDetails(ViewSubjectTable);
                    ViewSubjectSubjectNameTextbox.setText(null);
                }
                else
                {
                    JOptionPane.showMessageDialog(rootPane, "Subject could not be deleted!");
                }
            }
            catch(ClassNotFoundException | SQLException e)
            {

               Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, e);  
            }
            
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Please select a row to continue...");
        }
    }//GEN-LAST:event_DeleteSubjectButtonActionPerformed

    // On clicking thesearch option
    private void ViewAllotmentSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewAllotmentSearchButtonActionPerformed
        String searchBy = (String)ViewAllotmentSearchByCombobox.getSelectedItem();
        String subjectID = ViewAllotmentSubjectIDTextbox.getText();
        String teacherID = ViewAllotmentTeacherIDTextbox.getText();      
        
        if (searchBy.equals("Subject"))
        {
            // Display result on the basis of the subject id            
            this.PopulateViewAllotmentTable(ViewAllotmentTable, "Subject", Integer.parseInt(subjectID));
        }
        else if(searchBy.equals("Teacher"))
        {
            // Display result on the basis of the teacher id            
            this.PopulateViewAllotmentTable(ViewAllotmentTable, "Teacher", Integer.parseInt(teacherID));
        }
    }//GEN-LAST:event_ViewAllotmentSearchButtonActionPerformed

    private void AddSubjectSemesterComboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddSubjectSemesterComboboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddSubjectSemesterComboboxActionPerformed

    private void SubjectNameSearchTextboxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SubjectNameSearchTextboxKeyReleased
        // When searched for the subject
        DatabaseManager manager = null;
        ResultSet rs = null;

        if( SubjectNameSearchTextbox.getText().length() > 0 )
        {
            try {
                manager = new DatabaseManager();
                rs = manager.RetrieveSubjectDetails(SubjectNameSearchTextbox.getText());
                if ( null == rs )
                {
                  
                }
                else
                {
                    SubjectChooserTable.setModel( DbUtils.resultSetToTableModel(rs));
                }

                // deallocation
                manager.closeConection();
                manager    = null;
                rs         = null;

            }
            catch (ClassNotFoundException | SQLException ex)
            {
                Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_SubjectNameSearchTextboxKeyReleased

    private void SubjectChooserOKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubjectChooserOKButtonActionPerformed
        // On clicking the Ok button after searching the subject
        if( SubjectChooserTable.getSelectedRow() >= 0)
        {
            // Set text field
            // Getting the currently visible panel and passing the data tot corresponding textfields
            Component visiblePanel = MiddlePanel.getComponent(0);
            if (visiblePanel.equals(AllotSubjectPanel))
            {
                AllotSubjectSubjectIDTextbox.setText( String.valueOf(SubjectChooserTable.getValueAt( SubjectChooserTable.getSelectedRow() ,0)) );

                // dispose the search dialog
                SubjectChooserDialog.dispose();
            }
            else if(visiblePanel.equals(ViewAllotmentPanel))
            {
                ViewAllotmentSubjectIDTextbox.setText( String.valueOf(SubjectChooserTable.getValueAt( SubjectChooserTable.getSelectedRow() ,0)) );

                // dispose the search dialog
                SubjectChooserDialog.dispose();
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Please select a row to continue...");
        }
    }//GEN-LAST:event_SubjectChooserOKButtonActionPerformed

    private void SubjectChooserCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubjectChooserCloseButtonActionPerformed
        // Disposing search dialog
        SubjectChooserDialog.dispose();
    }//GEN-LAST:event_SubjectChooserCloseButtonActionPerformed

    // On clicking the subjectID textfield in allot subject panel
    private void AllotSubjectSubjectIDTextboxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AllotSubjectSubjectIDTextboxMouseClicked
        SubjectChooserDialog.setSize(800, 500);
        SubjectChooserDialog.setLocationRelativeTo(this);
        SubjectChooserDialog.setVisible(true);
        this.PopulateSubjectDetails(SubjectChooserTable);
    }//GEN-LAST:event_AllotSubjectSubjectIDTextboxMouseClicked

    private void TeacherNameSearchTextboxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TeacherNameSearchTextboxKeyReleased
        // When searched for the acceptor
        DatabaseManager manager = null;
        ResultSet rs = null;

        if( TeacherNameSearchTextbox.getText().length() > 0 )
        {
            try {
                manager = new DatabaseManager();
                rs = manager.RetrieveTeacherDetails(TeacherNameSearchTextbox.getText());
                if ( null == rs )
                {
                  
                }
                else
                {
                    TeacherChooserTable.setModel( DbUtils.resultSetToTableModel(rs));
                }

                // deallocation
                manager.closeConection();
                manager    = null;
                rs         = null;

            }
            catch (ClassNotFoundException | SQLException ex)
            {
                Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_TeacherNameSearchTextboxKeyReleased

    private void TeacherChooserOKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TeacherChooserOKButtonActionPerformed
        // On clicking the Ok button after searching the teacher
        if( TeacherChooserTable.getSelectedRow() >= 0)
        {
            // Set teacher id 
            Component visiblePanel = MiddlePanel.getComponent(0);
            if(visiblePanel.equals(AllotSubjectPanel))
            {
                AllotSubjectTeacherIDTextbox.setText( String.valueOf(TeacherChooserTable.getValueAt( TeacherChooserTable.getSelectedRow() ,0)) );

                // dispose the search dialog
                TeacherChooserDialog.dispose();
            }
            else if(visiblePanel.equals(ViewAllotmentPanel))
            {
                ViewAllotmentTeacherIDTextbox.setText( String.valueOf(TeacherChooserTable.getValueAt( TeacherChooserTable.getSelectedRow() ,0)) );

                // dispose the search dialog
                TeacherChooserDialog.dispose();
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Please select a row to continue...");
        }
    }//GEN-LAST:event_TeacherChooserOKButtonActionPerformed

    private void TeacherChooserCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TeacherChooserCloseButtonActionPerformed
       // Disposing search dialog
        TeacherChooserDialog.dispose();
    }//GEN-LAST:event_TeacherChooserCloseButtonActionPerformed

    private void ViewSubjectSubjectNameTextboxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ViewSubjectSubjectNameTextboxKeyReleased
        // When searched for the subject
        DatabaseManager manager = null;
        ResultSet rs = null;

        if( ViewSubjectSubjectNameTextbox.getText().length() > 0 )
        {
            try {
                manager = new DatabaseManager();
                rs = manager.RetrieveSubjectDetails(ViewSubjectSubjectNameTextbox.getText());
                if ( null == rs )
                {
                  
                }
                else
                {
                    ViewSubjectTable.setModel( DbUtils.resultSetToTableModel(rs));
                }

                // deallocation
                manager.closeConection();
                manager    = null;
                rs         = null;

            }
            catch (ClassNotFoundException | SQLException ex)
            {
                Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_ViewSubjectSubjectNameTextboxKeyReleased

    // on clicking the teahcer id textbox from the allot subject panel
    private void AllotSubjectTeacherIDTextboxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AllotSubjectTeacherIDTextboxMouseClicked
        String subjectID = AllotSubjectSubjectIDTextbox.getText();
        if (!subjectID.isEmpty())
        {
            int subID = Integer.parseInt(subjectID);
            int deptID = 0;
            try
            {
                DatabaseManager manager = new DatabaseManager();
                deptID = manager.GetDepartMentID(subID);                
            }
            catch (ClassNotFoundException | SQLException ex)
            {
                Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            TeacherChooserDialog.setSize(800, 500);
            TeacherChooserDialog.setLocationRelativeTo(this);
            TeacherChooserDialog.setVisible(true);
            this.PopulateTeacherDetails(TeacherChooserTable, deptID);
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Please choose a subject id to continue...");
        }
    }//GEN-LAST:event_AllotSubjectTeacherIDTextboxMouseClicked

    private void ViewAllotmentSubjectIDTextboxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewAllotmentSubjectIDTextboxMouseClicked
        SubjectChooserDialog.setSize(800, 500);
        SubjectChooserDialog.setLocationRelativeTo(this);
        SubjectChooserDialog.setVisible(true);
        this.PopulateSubjectDetails(SubjectChooserTable);
    }//GEN-LAST:event_ViewAllotmentSubjectIDTextboxMouseClicked

    private void ViewAllotmentTeacherIDTextboxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewAllotmentTeacherIDTextboxMouseClicked
       
            TeacherChooserDialog.setSize(800, 500);
            TeacherChooserDialog.setLocationRelativeTo(this);
            TeacherChooserDialog.setVisible(true);
            this.PopulateTeacherDetails(TeacherChooserTable, 0);        
    }//GEN-LAST:event_ViewAllotmentTeacherIDTextboxMouseClicked

    private void ViewAllotmentDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewAllotmentDeleteButtonActionPerformed
        // On clicking the delete button
        if( ViewAllotmentTable.getSelectedRow() >= 0)
        {
            // Getting the value of the assignid
            int assignID = Integer.parseInt(String.valueOf(ViewAllotmentTable.getValueAt(ViewAllotmentTable.getSelectedRow() ,0)));
            try
            {
                DatabaseManager dbManager = new DatabaseManager();
                boolean status = dbManager.DeleteAllotment(assignID);
                if (status)
                {
                    JOptionPane.showMessageDialog(rootPane, "Allotment deleted!");
                    this.PopulateSubjectAllotmentTable(ViewAllotmentTable); 
                    ViewAllotmentSubjectIDTextbox.setText(null);
                    ViewAllotmentTeacherIDTextbox.setText(null);
                }
                else
                {
                    JOptionPane.showMessageDialog(rootPane, "Allotment could not be deleted!");
                }
            }
            catch(ClassNotFoundException | SQLException e)
            {

               Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, e);  
            }
            
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Please select a row to continue...");
        }
    }//GEN-LAST:event_ViewAllotmentDeleteButtonActionPerformed

    private void ViewStudentDetailsOptionButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewStudentDetailsOptionButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_ViewStudentDetailsOptionButtonMouseClicked

    private void ViewStudentDetailsOptionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewStudentDetailsOptionButtonActionPerformed
        this.SetPanelVisibility(ViewStudentDetailsPanel);
        this.PopulateBatchCombobox(ViewStudentDetailsBatchCombobox);
    }//GEN-LAST:event_ViewStudentDetailsOptionButtonActionPerformed

    // On cliking the attendance button in student details panel
    private void ViewStudentAttendanceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewStudentAttendanceButtonActionPerformed
        this.SetTableVisibility(AttendencePanel);
        DatabaseManager manager = null;
        ResultSet rs = null;
        String batchName = (String)ViewStudentDetailsBatchCombobox.getSelectedItem();
        int batchID = 0, numberOfRows =0, i = 0;
        try
        {
            manager = new DatabaseManager();
            batchID = manager.getBatchIdFromBatchName(batchName);
            Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>> attendanceTriplet = manager.RetrieveStudentAttendanceDetails(this.hodDeptId, batchID);
            if(attendanceTriplet != null)
            {
                DefaultTableModel dm = (DefaultTableModel)jTable1.getModel();
                
                dm.setRowCount(0);              
                numberOfRows = attendanceTriplet.getValue0().size();
                
                // Populating the table by extratcing values from each rows
                while(i < numberOfRows)
                {    
                    String studentRegID = attendanceTriplet.getValue0().get(i);
                    String studentName = attendanceTriplet.getValue1().get(i);
                    String attendancePercentage = attendanceTriplet.getValue2().get(i);
                        dm.addRow
                            (
                                new Object[]
                                    {
                                        studentRegID,
                                        studentName,
                                        attendancePercentage
                                    }
                            );
                        // For iteration
                        i++;
                    }
                }                
        }
        catch(ClassNotFoundException | SQLException e)
        {
            Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, e);  
        }
    }//GEN-LAST:event_ViewStudentAttendanceButtonActionPerformed

    // On clicking the student details button from the student details panel
    private void ViewStudentDetailsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewStudentDetailsButtonActionPerformed
        this.SetTableVisibility(StudentDetailsPanel);
        DatabaseManager manager = null;
        ResultSet rs = null;
        String batchName = (String)ViewStudentDetailsBatchCombobox.getSelectedItem();
        int batchID = 0;
        try
        {
            manager = new DatabaseManager();
            batchID = manager.getBatchIdFromBatchName(batchName);
            rs = manager.RetrieveStudentDetails(this.hodDeptId, batchID);
            if(rs == null)
            {
                
            }
            else
            {
                DetailsTable.setModel( DbUtils.resultSetToTableModel(rs));
            }
        }
        catch(ClassNotFoundException | SQLException e)
        {
            Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, e);  
        }
    }//GEN-LAST:event_ViewStudentDetailsButtonActionPerformed

    private void ViewStudentAssignmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewStudentAssignmentButtonActionPerformed
        this.SetTableVisibility(AssignmentPanel);
        DatabaseManager manager = null;
        ResultSet rs = null;
        String batchName = (String)ViewStudentDetailsBatchCombobox.getSelectedItem();
        int batchID = 0, numberOfRows =0, i = 0;
        try
        {
            manager = new DatabaseManager();
            batchID = manager.getBatchIdFromBatchName(batchName);
            Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>> assignmentTriplet = manager.RetrieveStudentAssignmentDetails(this.hodDeptId, batchID);
            if(assignmentTriplet != null)
            {
                DefaultTableModel dm = (DefaultTableModel)jTable2.getModel();
                dm.setRowCount(0);              
                numberOfRows = assignmentTriplet.getValue0().size();
                
                // Populating the table by extratcing values from each rows
                while(i < numberOfRows)
                {    
                    String studentRegID = assignmentTriplet.getValue0().get(i);
                    String studentName = assignmentTriplet.getValue1().get(i);
                    String assignmentMarks = assignmentTriplet.getValue2().get(i);
                        dm.addRow
                            (
                                new Object[]
                                    {
                                        studentRegID,
                                        studentName,
                                        assignmentMarks
                                    }
                            );
                        // For iteration
                        i++;
                    }
                }                
        }
        catch(ClassNotFoundException | SQLException e)
        {
            Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, e);  
        }
    }//GEN-LAST:event_ViewStudentAssignmentButtonActionPerformed

    private void ViewStudentTestResultsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewStudentTestResultsButtonActionPerformed
        this.SetTableVisibility(testPanel);
        DatabaseManager manager = null;
        ResultSet rs = null;
        String batchName = (String)ViewStudentDetailsBatchCombobox.getSelectedItem();
        int batchID = 0, numberOfRows =0, i = 0;
        try
        {
            manager = new DatabaseManager();
            batchID = manager.getBatchIdFromBatchName(batchName);
            Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>> testTriplet = manager.RetrieveStudentTestDetails(this.hodDeptId, batchID);
            if(testTriplet != null)
            {
                DefaultTableModel dm = (DefaultTableModel)jTable3.getModel();
                dm.setRowCount(0);              
                numberOfRows = testTriplet.getValue0().size();
                
                // Populating the table by extratcing values from each rows
                while(i < numberOfRows)
                {    
                    String studentRegID = testTriplet.getValue0().get(i);
                    String studentName = testTriplet.getValue1().get(i);
                    String marks = testTriplet.getValue2().get(i);
                        dm.addRow
                            (
                                new Object[]
                                    {
                                        studentRegID,
                                        studentName,
                                        marks
                                    }
                            );
                        // For iteration
                        i++;
                    }
                }                
        }
        catch(ClassNotFoundException | SQLException e)
        {
            Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, e);  
        }
    }//GEN-LAST:event_ViewStudentTestResultsButtonActionPerformed

    private void ViewStudentDetailsOptionButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewStudentDetailsOptionButton1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_ViewStudentDetailsOptionButton1MouseClicked

    private void ViewStudentDetailsOptionButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewStudentDetailsOptionButton1ActionPerformed
        // TODO add your handling code here:
        setShowAllStudentsTable();
    }//GEN-LAST:event_ViewStudentDetailsOptionButton1ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        int selectedRow = showAllStudentsTable.getSelectedRow();
        if(selectedRow >= 0)
        {
            int selectedStudent = Integer.valueOf(showAllStudentsTable.getValueAt(selectedRow,0).toString());
            StudentForm form;
            form = new StudentForm(selectedStudent,true);
            form.setVisible(true);
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void HODChangePasswordButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HODChangePasswordButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_HODChangePasswordButtonMouseClicked

    private void HODChangePasswordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HODChangePasswordButtonActionPerformed
        // TODO add your handling code here:
        try
        {
            DatabaseManager manager = new DatabaseManager();
            int regID = manager. getHODRegIdFromID(this.hodId);
            if( regID != 0)
            {
                String username = "h"+regID;
                ChangePasswordForm cpwd = new ChangePasswordForm(username);
                cpwd.setVisible(true);
            }
        }
        catch(ClassNotFoundException | SQLException e)
        {

            Logger.getLogger(TeacherForm.class.getName()).log(Level.SEVERE, null, e);
        }
    }//GEN-LAST:event_HODChangePasswordButtonActionPerformed

    private void ViewStudentDetailsOptionButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewStudentDetailsOptionButton2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_ViewStudentDetailsOptionButton2MouseClicked

    private void ViewStudentDetailsOptionButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewStudentDetailsOptionButton2ActionPerformed
        // TODO add your handling code here:
        setActiveSemesterTable();
        SetPanelVisibility(SemesterShiftPanel);
    }//GEN-LAST:event_ViewStudentDetailsOptionButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int selectedRow = activeSemesterTable.getSelectedRow();
        System.out.println(selectedRow);
        if(selectedRow >= 0)
        {
            int confirmResult;
            confirmResult = JOptionPane.showConfirmDialog(rootPane,"Are you confirm to shift current semester to next semester ? .. All data of this semester will be lost","warning",JOptionPane.YES_NO_OPTION);
            if( confirmResult == JOptionPane.YES_OPTION)
            {
                String batchId = activeSemesterTable.getValueAt(selectedRow, 0).toString();
                boolean status;
                try {
                    status = new DatabaseManager().shiftSemester(batchId);
                    if(status == true)
                    {
                        JOptionPane.showMessageDialog(rootPane,"semester shifted");
                        setActiveSemesterTable();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(rootPane,"semester shift failed");
                    }

                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        else
        {
            JOptionPane.showMessageDialog(rootPane,"Please select a row to shift department");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void ViewStudentDetailsOptionButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewStudentDetailsOptionButton3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_ViewStudentDetailsOptionButton3MouseClicked

    private void ViewStudentDetailsOptionButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewStudentDetailsOptionButton3ActionPerformed
        // TODO add your handling code here:
        try
        {
            DatabaseManager dbManager = new DatabaseManager();
            ResultSet dates = dbManager.getEntryDates();
            if(dates.isBeforeFirst())
            {   dates.next();
                startDate.setDate(dates.getDate("starts_on"));
                endDate.setDate(dates.getDate("ends_on"));
                SetPanelVisibility(DateSettingsPanel);
            }
            else
            {
                System.out.println("date failed");
            }
            
            
        }
        catch(Exception e)
        {
           Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, e);  
        }
    }//GEN-LAST:event_ViewStudentDetailsOptionButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
         try
        {
            DatabaseManager dbManager = new DatabaseManager();
            if(!startDate.getDate().equals("") && !endDate.getDate().equals(""))
            {
                dbManager.changeDate(startDate.getDate(),endDate.getDate());
                JOptionPane.showMessageDialog(rootPane,"date changed");
            }
            
            
            
        }
        catch(Exception e)
        {
           Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, e);  
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void AllotSubjectOptionButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AllotSubjectOptionButton1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_AllotSubjectOptionButton1MouseClicked

    private void AllotSubjectOptionButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AllotSubjectOptionButton1ActionPerformed
        // TODO add your handling code here:
        this.PopulateDepartmentCombobox(teacherDeptCombo);
        SetPanelVisibility(AddTeacher);
        
    }//GEN-LAST:event_AllotSubjectOptionButton1ActionPerformed

    private void removeHodButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeHodButton2ActionPerformed
        // TODO add your handling code here:
        try{

            DatabaseManager dbManager = new DatabaseManager();
            int selectedRows[]        = showAllTeacherTable.getSelectedRows();
            String selectedId[]       = new String[selectedRows.length];
            int nIndex = 0;
            for( int row : selectedRows ){
                selectedId[nIndex] = showAllTeacherTable.getValueAt(row,0).toString();
                nIndex++;
            }

            if(dbManager.deleteSelectedTeachers(selectedId)){

                showAllTeacherTable();

            }else{

            }

        }catch(ClassNotFoundException | SQLException | NumberFormatException  e){

            Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, e);
        }

    }//GEN-LAST:event_removeHodButton2ActionPerformed

    private void addTeacherButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTeacherButtonActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
            DatabaseManager dbManager = new DatabaseManager();
            String teacherRegId     = teacherRegIdTF.getText();
            String teacherName      = teacherNameTF.getText();
            String teacherInfo      = teacherInfoTA.getText();
            String teacherDeptName  = teacherDeptCombo.getSelectedItem().toString();
            Date   dob       = teacherDob.getDate();
            String strDob    = dformat.format(dob);
            String joiningDate = dformat.format(teacher_joinDate.getDate());
            String qualification = teacherQualification.getSelectedItem().toString();

            if( !teacherRegId.equals("") && !teacherName.equals("") && !teacherInfo.equals("") && !teacherDeptName.equals("")){

                int teacherDeptId =  dbManager.getDepartmentIdWithName(teacherDeptName);
                if(teacherDeptId > 0){

                    if( dbManager.addTeacherWith(teacherRegId,teacherName,teacherInfo,teacherDeptId,strDob,joiningDate,qualification) ){

                        JOptionPane.showMessageDialog(rootPane, "New teacher added sucessfully");
                        JOptionPane.showMessageDialog(rootPane, "The auto generated id for the teacher is : "+new DatabaseManager().getTeacherIdFromRegId(teacherRegId) );

                    }
                    else{
                        JOptionPane.showMessageDialog(rootPane, "failed to add new teacher");
                    }

                }

            }
        }
        catch(ClassNotFoundException | SQLException e){
            Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, e);
        }
    }//GEN-LAST:event_addTeacherButtonActionPerformed

    private void AllotSubjectSubjectIDTextboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AllotSubjectSubjectIDTextboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AllotSubjectSubjectIDTextboxActionPerformed

    private void EditSubjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditSubjectButtonActionPerformed
        // TODO add your handling code here:
        if(ViewSubjectTable.getSelectedRows().length > 1)
        {
            JOptionPane.showMessageDialog(null, "Only one row can be selected at a time...");
        }
        else if( ViewSubjectTable.getSelectedRow() >= 0 && ViewSubjectTable.getSelectedRows().length == 1)
        {       
            this.SetPanelVisibility(AddSubjectPanel);
       
            // Populating the combobox
            this.PopulateDepartmentCombobox(AddSubjectDepartmentCombobox);
            //this.PopulateBatchCombobox(AddSubjectBatchCombobox);            
            AddSubjectButton.setVisible(false);
            UpdateSubjectButton.setVisible(true);
            AddSubjectSubjectIDTextbox.setEditable(false);
            AddSubjectSubjectIDTextbox.setText(ViewSubjectTable.getValueAt(ViewSubjectTable.getSelectedRow(),0).toString());
            this.oldSubjectID = ViewSubjectTable.getValueAt(ViewSubjectTable.getSelectedRow(),0).toString();
            AddSubjectSubjectNameTextbox.setText(ViewSubjectTable.getValueAt(ViewSubjectTable.getSelectedRow(),1).toString());   
            //AddSubjectBatchCombobox.addItem(ViewSubjectTable.getValueAt(ViewSubjectTable.getSelectedRow(),2).toString());
            AddSubjectDepartmentCombobox.addItem(ViewSubjectTable.getValueAt(ViewSubjectTable.getSelectedRow(),3).toString());
            AddSubjectSemesterCombobox.addItem(ViewSubjectTable.getValueAt(ViewSubjectTable.getSelectedRow(),4).toString());
        }        
    }//GEN-LAST:event_EditSubjectButtonActionPerformed

    private void UpdateSubjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateSubjectButtonActionPerformed
        String subjectID = AddSubjectSubjectIDTextbox.getText();
        String subjectName = AddSubjectSubjectNameTextbox.getText();
        String department =(String)AddSubjectDepartmentCombobox.getSelectedItem();
        //String batch =(String)AddSubjectBatchCombobox.getSelectedItem();
        String semester = (String)AddSubjectSemesterCombobox.getSelectedItem();

        // Validating the texts
        if (subjectID.isEmpty() || subjectName.isEmpty() || department.isEmpty()  || semester.isEmpty())
        {
            JOptionPane.showMessageDialog(rootPane, "Empty fields prevent the updation of subject! Please check again.");
        }
        else
        {
            try
            {
                DatabaseManager dbManager = new DatabaseManager();
                boolean status = dbManager.UpdateSubject(this.oldSubjectID, subjectID, subjectName, department,  semester);
                if (status)
                {
                    JOptionPane.showMessageDialog(rootPane, "Subject updated!");
                    AddSubjectSubjectIDTextbox.setText(null);
                    AddSubjectSubjectNameTextbox.setText(null);
                }
                else
                {
                    JOptionPane.showMessageDialog(rootPane, "Subject could not be updated!");
                }
            }
            catch(ClassNotFoundException | SQLException e)
            {

                Logger.getLogger(HODForm.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }//GEN-LAST:event_UpdateSubjectButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       new HODForm().setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddSubjectButton;
    private javax.swing.JComboBox<String> AddSubjectDepartmentCombobox;
    private javax.swing.JButton AddSubjectOptionButton;
    private javax.swing.JPanel AddSubjectPanel;
    private javax.swing.JComboBox<String> AddSubjectSemesterCombobox;
    private javax.swing.JTextField AddSubjectSubjectIDTextbox;
    private javax.swing.JTextField AddSubjectSubjectNameTextbox;
    private javax.swing.JPanel AddTeacher;
    private javax.swing.JButton AllotSubjectButton;
    private javax.swing.JButton AllotSubjectOptionButton;
    private javax.swing.JButton AllotSubjectOptionButton1;
    private javax.swing.JPanel AllotSubjectPanel;
    private javax.swing.JTextField AllotSubjectSubjectIDTextbox;
    private javax.swing.JTable AllotSubjectTable;
    private javax.swing.JTextField AllotSubjectTeacherIDTextbox;
    private javax.swing.JPanel AssignmentPanel;
    private javax.swing.JPanel AttendencePanel;
    private javax.swing.JPanel DateSettingsPanel;
    private javax.swing.JButton DeleteSubjectButton;
    private javax.swing.JScrollPane DetailsScrol;
    private javax.swing.JTable DetailsTable;
    private javax.swing.JButton EditSubjectButton;
    private javax.swing.JButton HODChangePasswordButton;
    private javax.swing.JLabel HODHomeLabel;
    private javax.swing.JLabel HeaderLogoLabel;
    private javax.swing.JPanel HeaderPanel;
    private javax.swing.JButton HomeOptionButton;
    private javax.swing.JPanel HomePanel;
    private javax.swing.JLabel InfoLabelHeader2;
    private javax.swing.JButton LogoutButton;
    private javax.swing.JPanel MiddlePanel;
    private javax.swing.JPanel OtherOptionsPanel1;
    private javax.swing.JPanel RegularOptionsPanel1;
    private javax.swing.JPanel SemesterShiftPanel;
    private javax.swing.JPanel ShowAllTeacher;
    private javax.swing.JPanel SideOptionsPanel;
    private javax.swing.JPanel StudentDetailsPanel;
    private javax.swing.JDialog SubjectAssignmentDisplayDialog;
    private javax.swing.JTable SubjectAssignmentDisplayTable;
    private javax.swing.JDialog SubjectAttendanceDisplayDialog;
    private javax.swing.JTable SubjectAttendanceDisplayTable;
    private javax.swing.JPanel SubjectChooserBasePanel;
    private javax.swing.JPanel SubjectChooserBasePanel1;
    private javax.swing.JPanel SubjectChooserBasePanel2;
    private javax.swing.JPanel SubjectChooserBasePanel3;
    private javax.swing.JButton SubjectChooserCloseButton;
    private javax.swing.JDialog SubjectChooserDialog;
    private javax.swing.JLabel SubjectChooserLabel;
    private javax.swing.JButton SubjectChooserOKButton;
    private javax.swing.JScrollPane SubjectChooserScrollPane;
    private javax.swing.JScrollPane SubjectChooserScrollPane1;
    private javax.swing.JScrollPane SubjectChooserScrollPane2;
    private javax.swing.JScrollPane SubjectChooserScrollPane3;
    private javax.swing.JTable SubjectChooserTable;
    private javax.swing.JTextField SubjectNameSearchTextbox;
    private javax.swing.JDialog SubjectTestDisplayDialog;
    private javax.swing.JTable SubjectTestDisplayTable;
    private javax.swing.JPanel TableHolderPanel;
    private javax.swing.JPanel TeacherChooserBasePanel;
    private javax.swing.JButton TeacherChooserCloseButton;
    private javax.swing.JDialog TeacherChooserDialog;
    private javax.swing.JLabel TeacherChooserLabel;
    private javax.swing.JButton TeacherChooserOKButton;
    private javax.swing.JScrollPane TeacherChooserScrollPane;
    private javax.swing.JTable TeacherChooserTable;
    private javax.swing.JTextField TeacherNameSearchTextbox;
    private javax.swing.JButton UpdateSubjectButton;
    private javax.swing.JButton ViewAllotmentDeleteButton;
    private javax.swing.JButton ViewAllotmentOptionButton;
    private javax.swing.JPanel ViewAllotmentPanel;
    private javax.swing.JButton ViewAllotmentSearchButton;
    private javax.swing.JComboBox<String> ViewAllotmentSearchByCombobox;
    private javax.swing.JTextField ViewAllotmentSubjectIDTextbox;
    private javax.swing.JTable ViewAllotmentTable;
    private javax.swing.JTextField ViewAllotmentTeacherIDTextbox;
    private javax.swing.JButton ViewStudentAssignmentButton;
    private javax.swing.JButton ViewStudentAttendanceButton;
    private javax.swing.JComboBox<String> ViewStudentDetailsBatchCombobox;
    private javax.swing.JButton ViewStudentDetailsButton;
    private javax.swing.JButton ViewStudentDetailsOptionButton;
    private javax.swing.JButton ViewStudentDetailsOptionButton1;
    private javax.swing.JButton ViewStudentDetailsOptionButton2;
    private javax.swing.JButton ViewStudentDetailsOptionButton3;
    private javax.swing.JPanel ViewStudentDetailsPanel;
    private javax.swing.JButton ViewStudentTestResultsButton;
    private javax.swing.JButton ViewSubjectOptionButton;
    private javax.swing.JPanel ViewSubjectPanel;
    private javax.swing.JTextField ViewSubjectSubjectNameTextbox;
    private javax.swing.JTable ViewSubjectTable;
    private javax.swing.JTable activeSemesterTable;
    private javax.swing.JButton addTeacherButton;
    private org.jdesktop.swingx.JXDatePicker endDate;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JButton removeHodButton2;
    private javax.swing.JTable showAllStudentsTable;
    private javax.swing.JTable showAllTeacherTable;
    private org.jdesktop.swingx.JXDatePicker startDate;
    private javax.swing.JPanel studentDetailsPanel;
    private javax.swing.JComboBox teacherDeptCombo;
    private org.jdesktop.swingx.JXDatePicker teacherDob;
    private javax.swing.JTextArea teacherInfoTA;
    private javax.swing.JTextField teacherNameTF;
    private javax.swing.JComboBox teacherQualification;
    private javax.swing.JTextField teacherRegIdTF;
    private org.jdesktop.swingx.JXDatePicker teacher_joinDate;
    private javax.swing.JPanel testPanel;
    // End of variables declaration//GEN-END:variables
}
