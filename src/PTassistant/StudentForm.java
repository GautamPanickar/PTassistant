/*
 * The form for the Head of the department
 */

package PTassistant;

import Database.DatabaseManager;
import PTassistant.LoginForm;
import java.awt.Component;
import java.awt.Window;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import net.proteanit.sql.DbUtils;
import org.javatuples.Triplet;

/**
 *
 * @author Panickar
 */
public class StudentForm extends javax.swing.JFrame {

    /**
     * Creates new form HODForm
     */
    
    private int studentId  = 0;
    private int studentDeptId  = 0;
    private boolean isView = false;
    
    public StudentForm(){
        
        initComponents();
        setLocationRelativeTo(null);        
        
    }
    
    public StudentForm(int studentId){
        initComponents();
        setLocationRelativeTo(null); 
        this.studentId = studentId;       
        this.SetStudentDepartmentID();
        this.isView = false;
        if(isView == false)
        {
           closeButton.setVisible(false);
           LogoutButton.setVisible(true);
           StudentChangePasswordButton.setVisible(true);
        }
    }
    
    public StudentForm(int studentId,boolean isView){
        initComponents();
        setLocationRelativeTo(null); 
        this.studentId = studentId;       
        this.SetStudentDepartmentID();
        this.isView = isView;
        if(isView == true)
        {
           closeButton.setVisible(true);
           LogoutButton.setVisible(false);
           StudentChangePasswordButton.setVisible(false);
        }
        
    }
     
    /// <summary>
    /// Setting the department id of the HOD
    /// </summary>    
    private void SetStudentDepartmentID()
    {
        try
        {
            this.studentDeptId = new DatabaseManager().getStudentDepartmentIdFromStudentId(studentId);
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
    
    /// <summary>
    /// Populates the subject attendacne display dialog
    /// </summary>
    private void PopulateSubjectAttendance(JTable table, JTextField subjectName, JTextField attendance, int subjectID)
    {
        DatabaseManager manager = null; 
        ResultSet rs = null; 
        try 
        {
            manager = new DatabaseManager();
            // Setting the subject name on the text field
            subjectName.setText(new DatabaseManager().GetSubjectName(String.valueOf(subjectID)));
            rs = manager.RetrieveStudentAttendanceData(this.studentId, subjectID);
            if ( null == rs )
            {
                    
            }
            else
            {
                table.setModel( DbUtils.resultSetToTableModel(rs)); 
            }
            rs = null;
            // Getting the percentage
            String percentage = new DatabaseManager().GetAttendancePercentageForSubject(this.studentId, subjectID);
            // Setting the corresponding textfield
            attendance.setText(percentage);
        }
        catch (ClassNotFoundException | SQLException ex) 
        {
            Logger.getLogger(StudentForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /// <summary>
    /// Populates the subject assignment display dialog
    /// </summary>
    private void PopulateSubjectAssignment(JTable table, JTextField subjectName, JTextField attendance, int subjectID)
    {
        DatabaseManager manager = null; 
        ResultSet rs = null; 
        try 
        {
            manager = new DatabaseManager();
            // Setting the subject name on the text field
            subjectName.setText(new DatabaseManager().GetSubjectName(String.valueOf(subjectID)));
            rs = manager.RetrieveStudentAssignmentData(this.studentId, subjectID);
            if ( null == rs )
            {
                    
            }
            else
            {
                table.setModel( DbUtils.resultSetToTableModel(rs)); 
            }
            rs = null;
            // Getting the percentage
            String totalMark = new DatabaseManager().GetAggregateAssignmentMarkForSubject(this.studentId, subjectID);
            // Setting the corresponding textfield
            attendance.setText(totalMark);
        }
        catch (ClassNotFoundException | SQLException ex) 
        {
            Logger.getLogger(StudentForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /// <summary>
    /// Populates the subject testdetails display dialog
    /// </summary>
    private void PopulateSubjectTest(JTable table, JTextField subjectName, JTextField attendance, int subjectID)
    {
        DatabaseManager manager = null; 
        ResultSet rs = null; 
        try 
        {
            manager = new DatabaseManager();
            // Setting the subject name on the text field
            subjectName.setText(new DatabaseManager().GetSubjectName(String.valueOf(subjectID)));
            rs = manager.RetrieveStudentTestData(this.studentId, subjectID);
            if ( null == rs )
            {
                    
            }
            else
            {
                table.setModel( DbUtils.resultSetToTableModel(rs)); 
            }
            rs = null;
            // Getting the percentage
            String totalMark = new DatabaseManager().GetAggregateTestMarkForSubject(this.studentId, subjectID);
            // Setting the corresponding textfield
            attendance.setText(totalMark);
        }
        catch (ClassNotFoundException | SQLException ex) 
        {
            Logger.getLogger(StudentForm.class.getName()).log(Level.SEVERE, null, ex);
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
                rs = manager.RetrieveSubjectDetails(this.studentDeptId);
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
                                                    batchColumn,
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
                Logger.getLogger(StudentForm.class.getName()).log(Level.SEVERE, null, ex);
            }        
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
        SubjectViceAttendanceDisplayDialog = new javax.swing.JDialog();
        SubjectChooserBasePanel = new javax.swing.JPanel();
        SubjectNAmeDisplayBox = new javax.swing.JTextField();
        SubjectChooserScrollPane = new javax.swing.JScrollPane();
        SubjectAttendanceDisplayTable = new javax.swing.JTable();
        SubjectChooserLabel = new javax.swing.JLabel();
        SubjectChooserLabel1 = new javax.swing.JLabel();
        SubjectAttendanceDisplaybox = new javax.swing.JTextField();
        SubjectViceAssignmentDisplayDialog = new javax.swing.JDialog();
        SubjectChooserBasePanel1 = new javax.swing.JPanel();
        AssignmentSubjectNAmeDisplayBox = new javax.swing.JTextField();
        SubjectChooserScrollPane1 = new javax.swing.JScrollPane();
        SubjectAssignmentDisplayTable = new javax.swing.JTable();
        SubjectChooserLabel2 = new javax.swing.JLabel();
        SubjectChooserLabel3 = new javax.swing.JLabel();
        SubjectAssignmentMarkDisplaybox = new javax.swing.JTextField();
        SubjectViceTestDisplayDialog = new javax.swing.JDialog();
        SubjectChooserBasePanel2 = new javax.swing.JPanel();
        TestSubjectNameDisplayBox = new javax.swing.JTextField();
        SubjectChooserScrollPane2 = new javax.swing.JScrollPane();
        SubjectTestDisplayTable = new javax.swing.JTable();
        SubjectChooserLabel4 = new javax.swing.JLabel();
        SubjectChooserLabel5 = new javax.swing.JLabel();
        SubjectTestMarkDisplaybox = new javax.swing.JTextField();
        HeaderPanel = new javax.swing.JPanel();
        HeaderLogoLabel = new javax.swing.JLabel();
        InfoLabelHeader2 = new javax.swing.JLabel();
        SideOptionsPanel = new javax.swing.JPanel();
        StudentRegularOptionsPanel = new javax.swing.JPanel();
        HomeOptionButton = new javax.swing.JButton();
        ViewTestResultsOptionButton1 = new javax.swing.JButton();
        ViewAttendanceOptionButton = new javax.swing.JButton();
        ViewAssignmentOptionButton = new javax.swing.JButton();
        ViewTestResultsOptionButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        StudentOtherOptionsPanel = new javax.swing.JPanel();
        LogoutButton = new javax.swing.JButton();
        StudentChangePasswordButton = new javax.swing.JButton();
        MiddlePanel = new javax.swing.JPanel();
        HomePanel = new javax.swing.JPanel();
        HODHomeLabel = new javax.swing.JLabel();
        ViewAttendancePanel = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ViewAttendanceTable = new javax.swing.JTable();
        jLabel29 = new javax.swing.JLabel();
        ViewAttendanceAtendancePercentageTextbox = new javax.swing.JTextField();
        ViewAssignmentPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ViewAssignmentTable = new javax.swing.JTable();
        jLabel30 = new javax.swing.JLabel();
        ViewAssignmentMarkTextbox = new javax.swing.JTextField();
        ViewTestResultsPanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        ViewTestDetailsTable = new javax.swing.JTable();
        jLabel31 = new javax.swing.JLabel();
        ViewTestMarkTextbox = new javax.swing.JTextField();
        OverallPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        overallTable = new javax.swing.JTable();

        jLabel18.setText("jLabel18");

        SubjectViceAttendanceDisplayDialog.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SubjectChooserBasePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Attendance for the subject...", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12))); // NOI18N

        SubjectNAmeDisplayBox.setEditable(false);
        SubjectNAmeDisplayBox.setBackground(new java.awt.Color(153, 204, 255));
        SubjectNAmeDisplayBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        SubjectNAmeDisplayBox.setForeground(new java.awt.Color(0, 51, 51));
        SubjectNAmeDisplayBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SubjectNAmeDisplayBoxKeyReleased(evt);
            }
        });

        SubjectAttendanceDisplayTable.setFont(new java.awt.Font("Miriam Fixed", 0, 12)); // NOI18N
        SubjectAttendanceDisplayTable.setForeground(new java.awt.Color(102, 0, 0));
        SubjectAttendanceDisplayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Attendance_Date", "Attendance_Percentage"
            }
        ));
        SubjectChooserScrollPane.setViewportView(SubjectAttendanceDisplayTable);

        SubjectChooserLabel.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        SubjectChooserLabel.setForeground(new java.awt.Color(102, 0, 0));
        SubjectChooserLabel.setText("Subject Name");

        SubjectChooserLabel1.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        SubjectChooserLabel1.setForeground(new java.awt.Color(102, 0, 0));
        SubjectChooserLabel1.setText("Percentage %");

        SubjectAttendanceDisplaybox.setEditable(false);
        SubjectAttendanceDisplaybox.setBackground(new java.awt.Color(153, 204, 255));
        SubjectAttendanceDisplaybox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        SubjectAttendanceDisplaybox.setForeground(new java.awt.Color(0, 51, 51));

        javax.swing.GroupLayout SubjectChooserBasePanelLayout = new javax.swing.GroupLayout(SubjectChooserBasePanel);
        SubjectChooserBasePanel.setLayout(SubjectChooserBasePanelLayout);
        SubjectChooserBasePanelLayout.setHorizontalGroup(
            SubjectChooserBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SubjectChooserBasePanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(SubjectChooserLabel)
                .addGap(35, 35, 35)
                .addComponent(SubjectNAmeDisplayBox, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(SubjectChooserBasePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SubjectChooserBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SubjectChooserScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
                    .addGroup(SubjectChooserBasePanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(SubjectChooserLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(SubjectAttendanceDisplaybox, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        SubjectChooserBasePanelLayout.setVerticalGroup(
            SubjectChooserBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SubjectChooserBasePanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(SubjectChooserBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SubjectChooserLabel)
                    .addComponent(SubjectNAmeDisplayBox, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SubjectChooserScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addGap(19, 19, 19)
                .addGroup(SubjectChooserBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SubjectAttendanceDisplaybox, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SubjectChooserLabel1))
                .addContainerGap())
        );

        SubjectViceAttendanceDisplayDialog.getContentPane().add(SubjectChooserBasePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        SubjectViceAssignmentDisplayDialog.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SubjectChooserBasePanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Assignment for the subject...", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12))); // NOI18N

        AssignmentSubjectNAmeDisplayBox.setEditable(false);
        AssignmentSubjectNAmeDisplayBox.setBackground(new java.awt.Color(153, 204, 255));
        AssignmentSubjectNAmeDisplayBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        AssignmentSubjectNAmeDisplayBox.setForeground(new java.awt.Color(0, 51, 51));
        AssignmentSubjectNAmeDisplayBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                AssignmentSubjectNAmeDisplayBoxKeyReleased(evt);
            }
        });

        SubjectAssignmentDisplayTable.setFont(new java.awt.Font("Miriam Fixed", 0, 12)); // NOI18N
        SubjectAssignmentDisplayTable.setForeground(new java.awt.Color(102, 0, 0));
        SubjectAssignmentDisplayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Assignment_Date", "Assignment_Name", "Assignment_Mark"
            }
        ));
        SubjectChooserScrollPane1.setViewportView(SubjectAssignmentDisplayTable);

        SubjectChooserLabel2.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        SubjectChooserLabel2.setForeground(new java.awt.Color(102, 0, 0));
        SubjectChooserLabel2.setText("Subject Name");

        SubjectChooserLabel3.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        SubjectChooserLabel3.setForeground(new java.awt.Color(102, 0, 0));
        SubjectChooserLabel3.setText("Mark aggregate");

        SubjectAssignmentMarkDisplaybox.setEditable(false);
        SubjectAssignmentMarkDisplaybox.setBackground(new java.awt.Color(153, 204, 255));
        SubjectAssignmentMarkDisplaybox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        SubjectAssignmentMarkDisplaybox.setForeground(new java.awt.Color(0, 51, 51));

        javax.swing.GroupLayout SubjectChooserBasePanel1Layout = new javax.swing.GroupLayout(SubjectChooserBasePanel1);
        SubjectChooserBasePanel1.setLayout(SubjectChooserBasePanel1Layout);
        SubjectChooserBasePanel1Layout.setHorizontalGroup(
            SubjectChooserBasePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SubjectChooserBasePanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(SubjectChooserLabel2)
                .addGap(35, 35, 35)
                .addComponent(AssignmentSubjectNAmeDisplayBox, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(SubjectChooserBasePanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SubjectChooserBasePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SubjectChooserScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
                    .addGroup(SubjectChooserBasePanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(SubjectChooserLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(SubjectAssignmentMarkDisplaybox, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        SubjectChooserBasePanel1Layout.setVerticalGroup(
            SubjectChooserBasePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SubjectChooserBasePanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(SubjectChooserBasePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SubjectChooserLabel2)
                    .addComponent(AssignmentSubjectNAmeDisplayBox, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SubjectChooserScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addGap(19, 19, 19)
                .addGroup(SubjectChooserBasePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SubjectAssignmentMarkDisplaybox, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SubjectChooserLabel3))
                .addContainerGap())
        );

        SubjectViceAssignmentDisplayDialog.getContentPane().add(SubjectChooserBasePanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        SubjectViceTestDisplayDialog.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SubjectChooserBasePanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Test details for the subject...", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12))); // NOI18N

        TestSubjectNameDisplayBox.setEditable(false);
        TestSubjectNameDisplayBox.setBackground(new java.awt.Color(153, 204, 255));
        TestSubjectNameDisplayBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        TestSubjectNameDisplayBox.setForeground(new java.awt.Color(0, 51, 51));
        TestSubjectNameDisplayBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TestSubjectNameDisplayBoxKeyReleased(evt);
            }
        });

        SubjectTestDisplayTable.setFont(new java.awt.Font("Miriam Fixed", 0, 12)); // NOI18N
        SubjectTestDisplayTable.setForeground(new java.awt.Color(102, 0, 0));
        SubjectTestDisplayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Test_Date", "Test_Mark"
            }
        ));
        SubjectChooserScrollPane2.setViewportView(SubjectTestDisplayTable);

        SubjectChooserLabel4.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        SubjectChooserLabel4.setForeground(new java.awt.Color(102, 0, 0));
        SubjectChooserLabel4.setText("Subject Name");

        SubjectChooserLabel5.setFont(new java.awt.Font("Miriam Fixed", 1, 14)); // NOI18N
        SubjectChooserLabel5.setForeground(new java.awt.Color(102, 0, 0));
        SubjectChooserLabel5.setText("Mark aggregate");

        SubjectTestMarkDisplaybox.setEditable(false);
        SubjectTestMarkDisplaybox.setBackground(new java.awt.Color(153, 204, 255));
        SubjectTestMarkDisplaybox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        SubjectTestMarkDisplaybox.setForeground(new java.awt.Color(0, 51, 51));

        javax.swing.GroupLayout SubjectChooserBasePanel2Layout = new javax.swing.GroupLayout(SubjectChooserBasePanel2);
        SubjectChooserBasePanel2.setLayout(SubjectChooserBasePanel2Layout);
        SubjectChooserBasePanel2Layout.setHorizontalGroup(
            SubjectChooserBasePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SubjectChooserBasePanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(SubjectChooserLabel4)
                .addGap(35, 35, 35)
                .addComponent(TestSubjectNameDisplayBox, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(SubjectChooserBasePanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SubjectChooserBasePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SubjectChooserScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
                    .addGroup(SubjectChooserBasePanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(SubjectChooserLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(SubjectTestMarkDisplaybox, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        SubjectChooserBasePanel2Layout.setVerticalGroup(
            SubjectChooserBasePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SubjectChooserBasePanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(SubjectChooserBasePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SubjectChooserLabel4)
                    .addComponent(TestSubjectNameDisplayBox, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SubjectChooserScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addGap(19, 19, 19)
                .addGroup(SubjectChooserBasePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SubjectTestMarkDisplaybox, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SubjectChooserLabel5))
                .addContainerGap())
        );

        SubjectViceTestDisplayDialog.getContentPane().add(SubjectChooserBasePanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
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

        StudentRegularOptionsPanel.setBackground(new java.awt.Color(22, 37, 47));
        StudentRegularOptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Regular options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        StudentRegularOptionsPanel.setLayout(new java.awt.GridLayout(7, 1));

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
        StudentRegularOptionsPanel.add(HomeOptionButton);

        ViewTestResultsOptionButton1.setBackground(new java.awt.Color(255, 255, 255));
        ViewTestResultsOptionButton1.setFont(new java.awt.Font("Miriam Fixed", 1, 15)); // NOI18N
        ViewTestResultsOptionButton1.setForeground(new java.awt.Color(0, 53, 91));
        ViewTestResultsOptionButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images32x32/1489695329_Artboard_1.png"))); // NOI18N
        ViewTestResultsOptionButton1.setText("Over all view");
        ViewTestResultsOptionButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ViewTestResultsOptionButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ViewTestResultsOptionButton1MouseClicked(evt);
            }
        });
        ViewTestResultsOptionButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewTestResultsOptionButton1ActionPerformed(evt);
            }
        });
        StudentRegularOptionsPanel.add(ViewTestResultsOptionButton1);

        ViewAttendanceOptionButton.setBackground(new java.awt.Color(255, 255, 255));
        ViewAttendanceOptionButton.setFont(new java.awt.Font("Miriam Fixed", 1, 15)); // NOI18N
        ViewAttendanceOptionButton.setForeground(new java.awt.Color(0, 53, 91));
        ViewAttendanceOptionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images32x32/1489695263_List.png"))); // NOI18N
        ViewAttendanceOptionButton.setText("View attendance");
        ViewAttendanceOptionButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ViewAttendanceOptionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ViewAttendanceOptionButtonMouseClicked(evt);
            }
        });
        ViewAttendanceOptionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewAttendanceOptionButtonActionPerformed(evt);
            }
        });
        StudentRegularOptionsPanel.add(ViewAttendanceOptionButton);

        ViewAssignmentOptionButton.setBackground(new java.awt.Color(255, 255, 255));
        ViewAssignmentOptionButton.setFont(new java.awt.Font("Miriam Fixed", 1, 15)); // NOI18N
        ViewAssignmentOptionButton.setForeground(new java.awt.Color(0, 53, 91));
        ViewAssignmentOptionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images32x32/1489695293_ic_assignment_48px.png"))); // NOI18N
        ViewAssignmentOptionButton.setText("View assignment");
        ViewAssignmentOptionButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ViewAssignmentOptionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ViewAssignmentOptionButtonMouseClicked(evt);
            }
        });
        ViewAssignmentOptionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewAssignmentOptionButtonActionPerformed(evt);
            }
        });
        StudentRegularOptionsPanel.add(ViewAssignmentOptionButton);

        ViewTestResultsOptionButton.setBackground(new java.awt.Color(255, 255, 255));
        ViewTestResultsOptionButton.setFont(new java.awt.Font("Miriam Fixed", 1, 15)); // NOI18N
        ViewTestResultsOptionButton.setForeground(new java.awt.Color(0, 53, 91));
        ViewTestResultsOptionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images32x32/1489695329_Artboard_1.png"))); // NOI18N
        ViewTestResultsOptionButton.setText("View test results");
        ViewTestResultsOptionButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ViewTestResultsOptionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ViewTestResultsOptionButtonMouseClicked(evt);
            }
        });
        ViewTestResultsOptionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewTestResultsOptionButtonActionPerformed(evt);
            }
        });
        StudentRegularOptionsPanel.add(ViewTestResultsOptionButton);

        closeButton.setBackground(new java.awt.Color(255, 255, 255));
        closeButton.setFont(new java.awt.Font("Miriam Fixed", 1, 15)); // NOI18N
        closeButton.setForeground(new java.awt.Color(0, 53, 91));
        closeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images32x32/1488760932_ic_delete_48px.png"))); // NOI18N
        closeButton.setText("Close");
        closeButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeButtonMouseClicked(evt);
            }
        });
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        StudentRegularOptionsPanel.add(closeButton);

        StudentOtherOptionsPanel.setBackground(new java.awt.Color(22, 37, 47));
        StudentOtherOptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Other options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        StudentOtherOptionsPanel.setLayout(new java.awt.GridLayout(2, 0));

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
        StudentOtherOptionsPanel.add(LogoutButton);

        StudentChangePasswordButton.setBackground(new java.awt.Color(255, 255, 255));
        StudentChangePasswordButton.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        StudentChangePasswordButton.setForeground(new java.awt.Color(0, 53, 91));
        StudentChangePasswordButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/POTAM_Images/1485992392_free-38.png"))); // NOI18N
        StudentChangePasswordButton.setText("Change password");
        StudentChangePasswordButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StudentChangePasswordButtonMouseClicked(evt);
            }
        });
        StudentChangePasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StudentChangePasswordButtonActionPerformed(evt);
            }
        });
        StudentOtherOptionsPanel.add(StudentChangePasswordButton);

        javax.swing.GroupLayout SideOptionsPanelLayout = new javax.swing.GroupLayout(SideOptionsPanel);
        SideOptionsPanel.setLayout(SideOptionsPanelLayout);
        SideOptionsPanelLayout.setHorizontalGroup(
            SideOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SideOptionsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(SideOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(StudentRegularOptionsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(StudentOtherOptionsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        SideOptionsPanelLayout.setVerticalGroup(
            SideOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SideOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(StudentRegularOptionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StudentOtherOptionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        MiddlePanel.setBackground(new java.awt.Color(255, 255, 255));
        MiddlePanel.setLayout(new java.awt.CardLayout());

        HomePanel.setBackground(new java.awt.Color(255, 255, 255));

        HODHomeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HODHomeLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/POTAM_Images/1488324714_Student-3.png"))); // NOI18N

        javax.swing.GroupLayout HomePanelLayout = new javax.swing.GroupLayout(HomePanel);
        HomePanel.setLayout(HomePanelLayout);
        HomePanelLayout.setHorizontalGroup(
            HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 602, Short.MAX_VALUE)
            .addGroup(HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(HODHomeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE))
        );
        HomePanelLayout.setVerticalGroup(
            HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 512, Short.MAX_VALUE)
            .addGroup(HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(HODHomeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        MiddlePanel.add(HomePanel, "card7");

        ViewAttendancePanel.setBackground(new java.awt.Color(255, 255, 255));
        ViewAttendancePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "View overall attendance", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Miriam Fixed", 0, 12))); // NOI18N

        jPanel9.setBackground(new java.awt.Color(204, 219, 222));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        ViewAttendanceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Subject_ID", "Subject_Name", "Attendance_Percentage"
            }
        ));
        ViewAttendanceTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ViewAttendanceTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(ViewAttendanceTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
        );

        jLabel29.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jLabel29.setText("Attendance %");

        ViewAttendanceAtendancePercentageTextbox.setEditable(false);
        ViewAttendanceAtendancePercentageTextbox.setBackground(new java.awt.Color(153, 204, 255));
        ViewAttendanceAtendancePercentageTextbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        ViewAttendanceAtendancePercentageTextbox.setForeground(new java.awt.Color(0, 51, 51));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ViewAttendanceAtendancePercentageTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ViewAttendanceAtendancePercentageTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addContainerGap())
        );

        javax.swing.GroupLayout ViewAttendancePanelLayout = new javax.swing.GroupLayout(ViewAttendancePanel);
        ViewAttendancePanel.setLayout(ViewAttendancePanelLayout);
        ViewAttendancePanelLayout.setHorizontalGroup(
            ViewAttendancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewAttendancePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        ViewAttendancePanelLayout.setVerticalGroup(
            ViewAttendancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewAttendancePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        MiddlePanel.add(ViewAttendancePanel, "card2");

        ViewAssignmentPanel.setBackground(new java.awt.Color(255, 255, 255));
        ViewAssignmentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "View assigment details", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Miriam Fixed", 0, 12))); // NOI18N

        jPanel4.setBackground(new java.awt.Color(204, 219, 222));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        ViewAssignmentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Subject_ID", "Subject_Name", "Assignment_Mark"
            }
        ));
        ViewAssignmentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ViewAssignmentTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(ViewAssignmentTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
        );

        jLabel30.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jLabel30.setText("Assignment mark ");

        ViewAssignmentMarkTextbox.setEditable(false);
        ViewAssignmentMarkTextbox.setBackground(new java.awt.Color(153, 204, 255));
        ViewAssignmentMarkTextbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        ViewAssignmentMarkTextbox.setForeground(new java.awt.Color(0, 51, 51));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ViewAssignmentMarkTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ViewAssignmentMarkTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout ViewAssignmentPanelLayout = new javax.swing.GroupLayout(ViewAssignmentPanel);
        ViewAssignmentPanel.setLayout(ViewAssignmentPanelLayout);
        ViewAssignmentPanelLayout.setHorizontalGroup(
            ViewAssignmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ViewAssignmentPanelLayout.setVerticalGroup(
            ViewAssignmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        MiddlePanel.add(ViewAssignmentPanel, "card2");

        ViewTestResultsPanel.setBackground(new java.awt.Color(255, 255, 255));
        ViewTestResultsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "View test details", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Miriam Fixed", 0, 12))); // NOI18N

        jPanel7.setBackground(new java.awt.Color(204, 219, 222));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        ViewTestDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Subject_ID", "Subject_Name", "Test_Mark"
            }
        ));
        ViewTestDetailsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ViewTestDetailsTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(ViewTestDetailsTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
        );

        jLabel31.setFont(new java.awt.Font("Miriam Fixed", 0, 14)); // NOI18N
        jLabel31.setText("Test mark");

        ViewTestMarkTextbox.setEditable(false);
        ViewTestMarkTextbox.setBackground(new java.awt.Color(153, 204, 255));
        ViewTestMarkTextbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        ViewTestMarkTextbox.setForeground(new java.awt.Color(0, 51, 51));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ViewTestMarkTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ViewTestMarkTextbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout ViewTestResultsPanelLayout = new javax.swing.GroupLayout(ViewTestResultsPanel);
        ViewTestResultsPanel.setLayout(ViewTestResultsPanelLayout);
        ViewTestResultsPanelLayout.setHorizontalGroup(
            ViewTestResultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewTestResultsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        ViewTestResultsPanelLayout.setVerticalGroup(
            ViewTestResultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewTestResultsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        MiddlePanel.add(ViewTestResultsPanel, "card2");

        OverallPanel.setBackground(new java.awt.Color(255, 255, 255));
        OverallPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Over all details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        overallTable.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        overallTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Subject id", "subjectName", "Attendence %", "Assignment mark", "Test mark", "Internal mark"
            }
        ));
        overallTable.setRowHeight(30);
        jScrollPane4.setViewportView(overallTable);

        javax.swing.GroupLayout OverallPanelLayout = new javax.swing.GroupLayout(OverallPanel);
        OverallPanel.setLayout(OverallPanelLayout);
        OverallPanelLayout.setHorizontalGroup(
            OverallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
        );
        OverallPanelLayout.setVerticalGroup(
            OverallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
        );

        MiddlePanel.add(OverallPanel, "card6");

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
                    .addComponent(MiddlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)))
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

    private void ViewAssignmentOptionButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewAssignmentOptionButtonMouseClicked
        // TODO add your handling code here:
        //this.ButtonSelection(MonthlyFeeOptionButton, null);
    }//GEN-LAST:event_ViewAssignmentOptionButtonMouseClicked

    private void ViewAttendanceOptionButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewAttendanceOptionButtonMouseClicked
        // TODO add your handling code here:
        //this.ButtonSelection(CreditTransactionOptionButton, null);
    }//GEN-LAST:event_ViewAttendanceOptionButtonMouseClicked

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

    private void ViewAssignmentOptionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewAssignmentOptionButtonActionPerformed
       this.SetPanelVisibility(ViewAssignmentPanel);
       
       // By default sets the assignment table with the details of the student
        DatabaseManager manager = null; 
        int i= 0, j=0, numberOfRows = 0;
        float markTotal = 0;
        Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>> assignmentTriplet = null;
        try 
            {
                manager = new DatabaseManager();
                assignmentTriplet = manager.RetrieveStudentAssignmentData(this.studentId);
                // After receiving the triplet convert the data from it to the table
                if(assignmentTriplet != null)
                {
                    DefaultTableModel dm = (DefaultTableModel)ViewAssignmentTable.getModel();
                    dm.setRowCount(0);
                    numberOfRows = assignmentTriplet.getValue0().size();
                    
                    // Calculates the total assignment mark
                    String temp = null;
                    while(j < assignmentTriplet.getValue2().size())
                    {
                        temp = assignmentTriplet.getValue2().get(j);
                        markTotal = markTotal + Float.parseFloat(temp);
                        j++;
                    }
                    
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
                
                // Setting the overal assignment mark
                float aggregateAssignmentMark = markTotal/numberOfRows;
                ViewAssignmentMarkTextbox.setText(String.valueOf(aggregateAssignmentMark));
                
            } 
            catch (ClassNotFoundException | SQLException ex) 
            {
                Logger.getLogger(StudentForm.class.getName()).log(Level.SEVERE, null, ex);
            }      
    }//GEN-LAST:event_ViewAssignmentOptionButtonActionPerformed

    private void ViewAttendanceOptionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewAttendanceOptionButtonActionPerformed
       this.SetPanelVisibility(ViewAttendancePanel);
       
       // By default sets the attendance table with the atendace details of the student
        DatabaseManager manager = null; 
        int i= 0, j=0, numberOfRows = 0;
        float percentageTotal = 0;
        Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>> attendaceTriplet = null;
        try 
            {
                manager = new DatabaseManager();
                attendaceTriplet = manager.RetrieveStudentAttendanceData(this.studentId);
                // After receiving the triplet convert the data from it to the table
                if(attendaceTriplet != null)
                {
                    DefaultTableModel dm = (DefaultTableModel)ViewAttendanceTable.getModel();
                    dm.setRowCount(0);
                    numberOfRows = attendaceTriplet.getValue0().size();
                    
                    // Calculates the total attendance percentage
                    String temp = null;
                    while(j < attendaceTriplet.getValue2().size())
                    {
                        temp = attendaceTriplet.getValue2().get(j);
                        percentageTotal = percentageTotal + Float.parseFloat(temp);
                        j++;
                    }
                    
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
                
                // Setting the overal attendance percentage
                float aggregatePercentage = percentageTotal/numberOfRows;
                ViewAttendanceAtendancePercentageTextbox.setText(String.valueOf(aggregatePercentage));
                
            } 
            catch (ClassNotFoundException | SQLException ex) 
            {
                Logger.getLogger(StudentForm.class.getName()).log(Level.SEVERE, null, ex);
            }       
    }//GEN-LAST:event_ViewAttendanceOptionButtonActionPerformed

    private void ViewTestResultsOptionButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewTestResultsOptionButtonMouseClicked
        
    }//GEN-LAST:event_ViewTestResultsOptionButtonMouseClicked

    private void ViewTestResultsOptionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewTestResultsOptionButtonActionPerformed
        this.SetPanelVisibility(ViewTestResultsPanel);     
        
        // By default sets the assignment table with the details of the student
        DatabaseManager manager = null; 
        int i= 0, j=0, numberOfRows = 0;
        float markTotal = 0;
        Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>> testTriplet = null;
        try 
            {
                manager = new DatabaseManager();
                testTriplet = manager.RetrieveStudentTestData(this.studentId);
                // After receiving the triplet convert the data from it to the table
                if(testTriplet != null)
                {
                    DefaultTableModel dm = (DefaultTableModel)ViewTestDetailsTable.getModel();
                    dm.setRowCount(0);
                    numberOfRows = testTriplet.getValue0().size();
                    
                    // Calculates the total assignment mark
                    String temp = null;
                    while(j < testTriplet.getValue2().size())
                    {
                        temp = testTriplet.getValue2().get(j);
                        markTotal = markTotal + Float.parseFloat(temp);
                        j++;
                    }
                    
                    // Populating the table by extratcing values from each rows
                    while(i < numberOfRows)
                    {    
                        String subjectID = testTriplet.getValue0().get(i);
                        String subjectName = testTriplet.getValue1().get(i);
                        String testMark = testTriplet.getValue2().get(i);
                            dm.addRow
                                    (
                                        new Object[]
                                                {
                                                    subjectID,
                                                    subjectName,
                                                    testMark
                                                }
                                    );
                        // For iteration
                        i++;
                    }
                }                
                // deallocation
                manager.closeConection();
                manager    = null;
                
                // Setting the overal assignment mark
                float aggregateMark = markTotal/numberOfRows;
                ViewTestMarkTextbox.setText(String.valueOf(aggregateMark));
                
            } 
            catch (ClassNotFoundException | SQLException ex) 
            {
                Logger.getLogger(StudentForm.class.getName()).log(Level.SEVERE, null, ex);
            }       
    }//GEN-LAST:event_ViewTestResultsOptionButtonActionPerformed

    private void SubjectNAmeDisplayBoxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SubjectNAmeDisplayBoxKeyReleased
        // When searched for the subject
        DatabaseManager manager = null;
        ResultSet rs = null;

        if( SubjectNAmeDisplayBox.getText().length() > 0 )
        {
            try {
                manager = new DatabaseManager();
                rs = manager.RetrieveSubjectDetails(SubjectNAmeDisplayBox.getText());
                if ( null == rs )
                {
                  
                }
                else
                {
                    SubjectAttendanceDisplayTable.setModel( DbUtils.resultSetToTableModel(rs));
                }

                // deallocation
                manager.closeConection();
                manager    = null;
                rs         = null;

            }
            catch (ClassNotFoundException | SQLException ex)
            {
                Logger.getLogger(StudentForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_SubjectNAmeDisplayBoxKeyReleased

    // On clicking the view attendace table
    private void ViewAttendanceTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewAttendanceTableMouseClicked
        if(ViewAttendanceTable.getSelectedRows().length > 1)
        {
            JOptionPane.showMessageDialog(null, "Only one row can be selected at a time...");
        }
        else if( ViewAttendanceTable.getSelectedRow() >= 0 && ViewAttendanceTable.getSelectedRows().length == 1)
        {
            // Getting the value of the assignid
            int subjectID = Integer.parseInt(String.valueOf(ViewAttendanceTable.getValueAt(ViewAttendanceTable.getSelectedRow() ,0)));
            SubjectViceAttendanceDisplayDialog.setSize(800, 500);            
            SubjectViceAttendanceDisplayDialog.setLocationRelativeTo(this);
            SubjectViceAttendanceDisplayDialog.setVisible(true);
            
            // populates the subject atendace display dialog box with necessarry fields
            this.PopulateSubjectAttendance(SubjectAttendanceDisplayTable, SubjectNAmeDisplayBox, SubjectAttendanceDisplaybox, subjectID);           
        }
    }//GEN-LAST:event_ViewAttendanceTableMouseClicked

    private void ViewAssignmentTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewAssignmentTableMouseClicked
        if(ViewAssignmentTable.getSelectedRows().length > 1)
        {
            JOptionPane.showMessageDialog(null, "Only one row can be selected at a time...");
        }
        else if( ViewAssignmentTable.getSelectedRow() >= 0 && ViewAssignmentTable.getSelectedRows().length == 1)
        {
            // Getting the value of the assignid
            int subjectID = Integer.parseInt(String.valueOf(ViewAssignmentTable.getValueAt(ViewAssignmentTable.getSelectedRow() ,0)));
            SubjectViceAssignmentDisplayDialog.setSize(800, 500);            
            SubjectViceAssignmentDisplayDialog.setLocationRelativeTo(this);
            SubjectViceAssignmentDisplayDialog.setVisible(true);
            
            // populates the subject atendace display dialog box with necessarry fields
            this.PopulateSubjectAssignment(SubjectAssignmentDisplayTable, AssignmentSubjectNAmeDisplayBox, SubjectAssignmentMarkDisplaybox, subjectID);           
        }
    }//GEN-LAST:event_ViewAssignmentTableMouseClicked

    private void AssignmentSubjectNAmeDisplayBoxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AssignmentSubjectNAmeDisplayBoxKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_AssignmentSubjectNAmeDisplayBoxKeyReleased

    private void TestSubjectNameDisplayBoxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TestSubjectNameDisplayBoxKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_TestSubjectNameDisplayBoxKeyReleased

    private void ViewTestDetailsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewTestDetailsTableMouseClicked
        if(ViewTestDetailsTable.getSelectedRows().length > 1)
        {
            JOptionPane.showMessageDialog(null, "Only one row can be selected at a time...");
        }
        else if( ViewTestDetailsTable.getSelectedRow() >= 0 && ViewTestDetailsTable.getSelectedRows().length == 1)
        {
            // Getting the value of the assignid
            int subjectID = Integer.parseInt(String.valueOf(ViewTestDetailsTable.getValueAt(ViewTestDetailsTable.getSelectedRow() ,0)));
            SubjectViceTestDisplayDialog.setSize(800, 500);            
            SubjectViceTestDisplayDialog.setLocationRelativeTo(this);
            SubjectViceTestDisplayDialog.setVisible(true);
            
            // populates the subject test details display dialog box with necessarry fields
            this.PopulateSubjectTest(SubjectTestDisplayTable, TestSubjectNameDisplayBox, SubjectTestMarkDisplaybox, subjectID);           
        }
    }//GEN-LAST:event_ViewTestDetailsTableMouseClicked

    private void ViewTestResultsOptionButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ViewTestResultsOptionButton1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_ViewTestResultsOptionButton1MouseClicked

    private void ViewTestResultsOptionButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewTestResultsOptionButton1ActionPerformed
       // By default sets the attendance table with the atendace details of the student
        DatabaseManager manager = null; 
        int i= 0, j=0, numberOfRows = 0;
        float percentageTotal = 0;
        Triplet<ArrayList<String>, ArrayList<String>, ArrayList<String>> attendaceTriplet = null;
        try 
            {
                manager = new DatabaseManager();
                attendaceTriplet = manager.RetrieveStudentAttendanceData(this.studentId);
                // After receiving the triplet convert the data from it to the table
                if(attendaceTriplet != null)
                {
                    DefaultTableModel dm = (DefaultTableModel)overallTable.getModel();
                    dm.setRowCount(0);
                    numberOfRows = attendaceTriplet.getValue0().size();
                    
                    // Calculates the total attendance percentage
                    String temp = null;
                    while(j < attendaceTriplet.getValue2().size())
                    {
                        temp = attendaceTriplet.getValue2().get(j);
                        percentageTotal = percentageTotal + Float.parseFloat(temp);
                        j++;
                    }
                    
                    // Populating the table by extratcing values from each rows
                    while(i < numberOfRows)
                    {    
                        String subjectID = attendaceTriplet.getValue0().get(i);
                        String subjectName = attendaceTriplet.getValue1().get(i);
                        String attendancePercentage = attendaceTriplet.getValue2().get(i);
                        String assignmentAvg  = manager.GetAggregateAssignmentMarkForSubject(studentId, Integer.valueOf(subjectID));
                        String testAvg   = manager.GetAggregateTestMarkForSubject(studentId, Integer.valueOf(subjectID));
                        float totalMark = 0;
                        float attendenceMark = 0;
                        if(!assignmentAvg.equals("") && !testAvg.equals("") && !attendancePercentage.equals(""))
                        {
                            attendenceMark = getMarkFromPersentage(Float.valueOf(attendancePercentage));
                            totalMark = Float.valueOf(assignmentAvg)+Float.valueOf(testAvg)+attendenceMark;
                        }
                        dm.addRow
                                    (
                                        new Object[]
                                                {
                                                    subjectID,
                                                    subjectName,
                                                    attendancePercentage,
                                                    assignmentAvg,
                                                    testAvg,
                                                    totalMark
                                                }
                                    );
                        // For iteration
                        i++;
                    }
                }                
                // deallocation
                manager.closeConection();
                manager    = null;
                
                // Setting the overal attendance percentage
                float aggregatePercentage = percentageTotal/numberOfRows;
                //ViewAttendanceAtendancePercentageTextbox.setText(String.valueOf(aggregatePercentage));
                
            } 
            catch (ClassNotFoundException | SQLException ex) 
            {
                Logger.getLogger(StudentForm.class.getName()).log(Level.SEVERE, null, ex);
            }       
            SetPanelVisibility(OverallPanel);
    }//GEN-LAST:event_ViewTestResultsOptionButton1ActionPerformed

    private void closeButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_closeButtonMouseClicked

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void StudentChangePasswordButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StudentChangePasswordButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_StudentChangePasswordButtonMouseClicked

    private void StudentChangePasswordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StudentChangePasswordButtonActionPerformed
        // TODO add your handling code here:
        try
        {
            DatabaseManager manager = new DatabaseManager();
            int regID = manager. getStudentRegIdFromID(this.studentId);
            if( regID != 0)
            {
                String username = "s"+regID;
                ChangePasswordForm cpwd = new ChangePasswordForm(username);
                cpwd.setVisible(true);
            }
        }
        catch(ClassNotFoundException | SQLException e)
        {

            Logger.getLogger(TeacherForm.class.getName()).log(Level.SEVERE, null, e);
        }
    }//GEN-LAST:event_StudentChangePasswordButtonActionPerformed

    
    private float getMarkFromPersentage(float attendancePercentage){
       float mark = 0;
       if(attendancePercentage > 90.0)
       {
          mark = 10; 
       }
       else if(attendancePercentage > 80.0)
       {
           mark = 9;
       }
       else if(attendancePercentage > 70.0)
       {
           mark = 8;
       }
       else if(attendancePercentage > 60.0)
       {
           mark = 7;
       }
       else if(attendancePercentage > 50.0)
       {
           mark = 6;
       }
       else if(attendancePercentage > 40.0)
       {
           mark = 5;
       }
       else if(attendancePercentage > 30.0)
       {
           mark = 4;
       }
       else if(attendancePercentage > 20.0)
       {
           mark = 3;
       }
       else if(attendancePercentage > 10.0)
       {
           mark = 2;
       }
       return mark;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       new StudentForm().setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AssignmentSubjectNAmeDisplayBox;
    private javax.swing.JLabel HODHomeLabel;
    private javax.swing.JLabel HeaderLogoLabel;
    private javax.swing.JPanel HeaderPanel;
    private javax.swing.JButton HomeOptionButton;
    private javax.swing.JPanel HomePanel;
    private javax.swing.JLabel InfoLabelHeader2;
    private javax.swing.JButton LogoutButton;
    private javax.swing.JPanel MiddlePanel;
    private javax.swing.JPanel OverallPanel;
    private javax.swing.JPanel SideOptionsPanel;
    private javax.swing.JButton StudentChangePasswordButton;
    private javax.swing.JPanel StudentOtherOptionsPanel;
    private javax.swing.JPanel StudentRegularOptionsPanel;
    private javax.swing.JTable SubjectAssignmentDisplayTable;
    private javax.swing.JTextField SubjectAssignmentMarkDisplaybox;
    private javax.swing.JTable SubjectAttendanceDisplayTable;
    private javax.swing.JTextField SubjectAttendanceDisplaybox;
    private javax.swing.JPanel SubjectChooserBasePanel;
    private javax.swing.JPanel SubjectChooserBasePanel1;
    private javax.swing.JPanel SubjectChooserBasePanel2;
    private javax.swing.JLabel SubjectChooserLabel;
    private javax.swing.JLabel SubjectChooserLabel1;
    private javax.swing.JLabel SubjectChooserLabel2;
    private javax.swing.JLabel SubjectChooserLabel3;
    private javax.swing.JLabel SubjectChooserLabel4;
    private javax.swing.JLabel SubjectChooserLabel5;
    private javax.swing.JScrollPane SubjectChooserScrollPane;
    private javax.swing.JScrollPane SubjectChooserScrollPane1;
    private javax.swing.JScrollPane SubjectChooserScrollPane2;
    private javax.swing.JTextField SubjectNAmeDisplayBox;
    private javax.swing.JTable SubjectTestDisplayTable;
    private javax.swing.JTextField SubjectTestMarkDisplaybox;
    private javax.swing.JDialog SubjectViceAssignmentDisplayDialog;
    private javax.swing.JDialog SubjectViceAttendanceDisplayDialog;
    private javax.swing.JDialog SubjectViceTestDisplayDialog;
    private javax.swing.JTextField TestSubjectNameDisplayBox;
    private javax.swing.JTextField ViewAssignmentMarkTextbox;
    private javax.swing.JButton ViewAssignmentOptionButton;
    private javax.swing.JPanel ViewAssignmentPanel;
    private javax.swing.JTable ViewAssignmentTable;
    private javax.swing.JTextField ViewAttendanceAtendancePercentageTextbox;
    private javax.swing.JButton ViewAttendanceOptionButton;
    private javax.swing.JPanel ViewAttendancePanel;
    private javax.swing.JTable ViewAttendanceTable;
    private javax.swing.JTable ViewTestDetailsTable;
    private javax.swing.JTextField ViewTestMarkTextbox;
    private javax.swing.JButton ViewTestResultsOptionButton;
    private javax.swing.JButton ViewTestResultsOptionButton1;
    private javax.swing.JPanel ViewTestResultsPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable overallTable;
    // End of variables declaration//GEN-END:variables
}
