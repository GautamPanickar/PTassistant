/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PTassistant;

import Database.DatabaseManager;
import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Panickar
 */
public class PrincipalHome extends javax.swing.JFrame {

    /**
     * Creates new form LoginForm
     */
    public PrincipalHome(){
        
        initComponents();
        setLocationRelativeTo(null);        
        
    }
    
    
    
    // Set the visibility of the panel in the card layout.
    // Logic:
    //   fist set visibility of all panel to false
    //   Then make the visibility of selected panel to true
    private void setPanelVisibility( JPanel panel ){
        
        MiddlePanel.removeAll();
        MiddlePanel.repaint();
        MiddlePanel.revalidate();
        
        MiddlePanel.add(panel);
        MiddlePanel.repaint();
        MiddlePanel.revalidate();
    }
    
    private void setShowAllBatchTable(){
         try {
            
            // select all batches from db
            DatabaseManager dbManager   = new DatabaseManager();
            ResultSet allBatches        = dbManager.selectAllBatches();
            DefaultTableModel  dm       = (DefaultTableModel)showBatchTable.getModel();
            // check selection is sucessfull or not
            if(allBatches.isBeforeFirst()){
                
                dm.setRowCount(0);
                while(allBatches.next()){
                    
                   dm.addRow(new Object[]{
                   
                       allBatches.getString("Batch_ID"),
                       allBatches.getString("Batch_Name"),
                       allBatches.getString("Batch_Starts_On"),
                       allBatches.getString("Batch_Ends_On"),
                       allBatches.getString("Max_Students")
                   
                   
                   });
                }
                setPanelVisibility(showBatches);
            }
            
        
        }catch(ClassNotFoundException | SQLException e){
            
           Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, e);  
        }
        
    }
    
    private void setHodDetailsTable(){
       try {
           
           
            
            // select all batches from db
            DatabaseManager dbManager  = new DatabaseManager();
            
            int hodId = Integer.valueOf( HodIDCombo.getSelectedItem().toString() );
           ResultSet allBatches        = dbManager.selectHodById(hodId);
            DefaultTableModel  dm       = (DefaultTableModel)showHodTable.getModel();
            // check selection is sucessfull or not
            dm.setRowCount(0);
            if(allBatches.isBeforeFirst()){                
                
                
                if(allBatches.next()){
                    
                    HodIdTF.setText(allBatches.getString("HOD_RegID"));
                    
                   dm.addRow(new Object[]{
                       " HOD NAME ",
                       allBatches.getString("HOD_Name")
                      });
                   
                   dm.addRow(new Object[]{
                       " PERSONAL DETAILS ",
                       allBatches.getString("HOD_Details")
                      });
                   
                    dm.addRow(new Object[]{
                       " DATE OF BIRTH ",
                       allBatches.getString("HOD_DOB")
                      });
                   
                   dm.addRow(new Object[]{
                       " DEPARTMENT ",
                       allBatches.getString("Department_Name")
                      });
                   
                    dm.addRow(new Object[]{
                       " QUALIFICATION ",
                       allBatches.getString("HOD_Qualification")
                      });
                    
                     dm.addRow(new Object[]{
                       " JOINED DATE ",
                       allBatches.getString("HOD_Join_Date")
                      });
                }
                
                
            }
            
        
        }catch(ClassNotFoundException | SQLException e){
            
           Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, e);  
        } 
       catch(NumberFormatException e)
       {
           JOptionPane.showMessageDialog(rootPane,"Selected hod number not valid" );
       }
        
    }
    private void setShowAllDepartmentTable(){
        
       try {
            
            // select all batches from db
            DatabaseManager dbManager   = new DatabaseManager();
            ResultSet allDepts          = dbManager.selectAllDepartments();
            DefaultTableModel  dm       = (DefaultTableModel)showAllDeptTable.getModel();
            // check selection is sucessfull or not
            dm.setRowCount(0);
            if( allDepts.isBeforeFirst()){                
                
                while( allDepts.next()){
                    
                   dm.addRow(new Object[]{
                   
                        allDepts.getString("Department_Id"),
                        allDepts.getString("Department_Name"),
                        allDepts.getString("Max_Strength")
                   
                   
                   });
                }
                setPanelVisibility(showBatches);
            }
            
        
        }catch(ClassNotFoundException | SQLException e){
            
           Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, e);  
        }  
        
    }
    
    private void  showAllTeacherTable(){
        try {
           
           
            
            // select all batches from db
            DatabaseManager dbManager  = new DatabaseManager();
            
            int Id = Integer.valueOf(teacherCombo.getSelectedItem().toString() );
           ResultSet allBatches        = dbManager.selectTeacherById(Id);
            DefaultTableModel  dm       = (DefaultTableModel)showAllTeacherTable.getModel();
            // check selection is sucessfull or not
            dm.setRowCount(0);
            if(allBatches.isBeforeFirst()){                
                
                
                if(allBatches.next()){
                    
                    HodIdTF1.setText(allBatches.getString("Teacher_RegID"));
                    
                   dm.addRow(new Object[]{
                       " TEACHER NAME ",
                       allBatches.getString("Teacher_Name")
                      });
                   
                   dm.addRow(new Object[]{
                       " PERSONAL DETAILS ",
                       allBatches.getString("Teacher_Details")
                      });
                   
                    dm.addRow(new Object[]{
                       " DATE OF BIRTH ",
                       allBatches.getString("Teacher_DOB")
                      });
                   
                   dm.addRow(new Object[]{
                       " DEPARTMENT ",
                       allBatches.getString("Department_Name")
                      });
                   
                    dm.addRow(new Object[]{
                       " QUALIFICATION ",
                       allBatches.getString("Teacher_Qualification")
                      });
                    
                     dm.addRow(new Object[]{
                       " JOINED DATE ",
                       allBatches.getString("Teacher_Join_Date")
                      });
                }
                
                
            }
            
        
        }catch(ClassNotFoundException | SQLException e){
            
           Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, e);  
        } 
       catch(NumberFormatException e)
       {
           JOptionPane.showMessageDialog(rootPane,"Selected hod number not valid" );
       }
        
        
    }
    
    public void setShowAllStudentsTable(int batchId) {
        
         // select all batches from db
         try{
             
            DatabaseManager dbManager   = new DatabaseManager();
            ResultSet allStudents       = dbManager.selectAllStudents(batchId);
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
        
    }
          
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        HeaderPanel = new javax.swing.JPanel();
        HeaderLogoLabel = new javax.swing.JLabel();
        InfoLabelHeader2 = new javax.swing.JLabel();
        SideOptionsPanel = new javax.swing.JPanel();
        RegularOptionsPanel1 = new javax.swing.JPanel();
        HomeOptionButton = new javax.swing.JButton();
        Option1OptionButton = new javax.swing.JButton();
        Option5OptionButton = new javax.swing.JButton();
        Option2OptionButton = new javax.swing.JButton();
        showBatchesButton = new javax.swing.JButton();
        Option5OptionButton4 = new javax.swing.JButton();
        Option5OptionButton3 = new javax.swing.JButton();
        Option5OptionButton5 = new javax.swing.JButton();
        Option5OptionButton6 = new javax.swing.JButton();
        OtherOptionsPanel1 = new javax.swing.JPanel();
        LogoutButton = new javax.swing.JButton();
        PrincipalChangePasswordButton = new javax.swing.JButton();
        MiddlePanel = new javax.swing.JPanel();
        HomePanel = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        AddBatchPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        batchNameTF = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        addBatchButton = new javax.swing.JButton();
        batchStartsTF = new javax.swing.JTextField();
        batchEndTF = new javax.swing.JTextField();
        batchDeptCombo = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        NumberofStudentsTF = new javax.swing.JTextField();
        AddHodPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        hodRegIdTF = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        hodNameTF = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        hodDeptCombo = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        hodInfoTA = new javax.swing.JTextArea();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        hod_joinDate = new org.jdesktop.swingx.JXDatePicker();
        jLabel24 = new javax.swing.JLabel();
        hodQualification = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        hodDob = new org.jdesktop.swingx.JXDatePicker();
        AddDepartment = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        departmentNameTF = new javax.swing.JTextField();
        addDepartmentButton = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        maxStrengthTF = new javax.swing.JTextField();
        showBatches = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        showBatchTable = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        deleteBatchesButton = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        showHodPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        showHodTable = new javax.swing.JTable();
        removeHodButton = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        HodIDCombo = new javax.swing.JComboBox();
        jLabel26 = new javax.swing.JLabel();
        jButton16 = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        HodIdTF = new javax.swing.JTextField();
        ShowAllDeptPanel = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        showAllDeptTable = new javax.swing.JTable();
        removeHodButton1 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        ShowAllTeacher = new javax.swing.JPanel();
        removeHodButton2 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        teacherCombo = new javax.swing.JComboBox();
        jButton17 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        HodIdTF1 = new javax.swing.JTextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        showAllTeacherTable = new javax.swing.JTable();
        ShowStudents = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        showAllStudentsTable = new javax.swing.JTable();
        removeHodButton3 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        StudentBatchCombo = new javax.swing.JComboBox();
        jButton13 = new javax.swing.JButton();

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
        RegularOptionsPanel1.setLayout(new java.awt.GridLayout(9, 1));

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

        Option1OptionButton.setBackground(new java.awt.Color(255, 255, 255));
        Option1OptionButton.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        Option1OptionButton.setForeground(new java.awt.Color(0, 53, 91));
        Option1OptionButton.setText("Add Department");
        Option1OptionButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Option1OptionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Option1OptionButtonMouseClicked(evt);
            }
        });
        Option1OptionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Option1OptionButtonActionPerformed(evt);
            }
        });
        RegularOptionsPanel1.add(Option1OptionButton);

        Option5OptionButton.setBackground(new java.awt.Color(255, 255, 255));
        Option5OptionButton.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        Option5OptionButton.setForeground(new java.awt.Color(0, 53, 91));
        Option5OptionButton.setText("Add Batch");
        Option5OptionButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Option5OptionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Option5OptionButtonMouseClicked(evt);
            }
        });
        Option5OptionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Option5OptionButtonActionPerformed(evt);
            }
        });
        RegularOptionsPanel1.add(Option5OptionButton);

        Option2OptionButton.setBackground(new java.awt.Color(255, 255, 255));
        Option2OptionButton.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        Option2OptionButton.setForeground(new java.awt.Color(0, 53, 91));
        Option2OptionButton.setText("Add Hod");
        Option2OptionButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Option2OptionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Option2OptionButtonMouseClicked(evt);
            }
        });
        Option2OptionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Option2OptionButtonActionPerformed(evt);
            }
        });
        RegularOptionsPanel1.add(Option2OptionButton);

        showBatchesButton.setBackground(new java.awt.Color(255, 255, 255));
        showBatchesButton.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        showBatchesButton.setForeground(new java.awt.Color(0, 53, 91));
        showBatchesButton.setText("Show batches");
        showBatchesButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        showBatchesButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showBatchesButtonMouseClicked(evt);
            }
        });
        showBatchesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showBatchesButtonActionPerformed(evt);
            }
        });
        RegularOptionsPanel1.add(showBatchesButton);

        Option5OptionButton4.setBackground(new java.awt.Color(255, 255, 255));
        Option5OptionButton4.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        Option5OptionButton4.setForeground(new java.awt.Color(0, 53, 91));
        Option5OptionButton4.setText("Show Departments");
        Option5OptionButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Option5OptionButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Option5OptionButton4MouseClicked(evt);
            }
        });
        Option5OptionButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Option5OptionButton4ActionPerformed(evt);
            }
        });
        RegularOptionsPanel1.add(Option5OptionButton4);

        Option5OptionButton3.setBackground(new java.awt.Color(255, 255, 255));
        Option5OptionButton3.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        Option5OptionButton3.setForeground(new java.awt.Color(0, 53, 91));
        Option5OptionButton3.setText("Hod Details");
        Option5OptionButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Option5OptionButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Option5OptionButton3MouseClicked(evt);
            }
        });
        Option5OptionButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Option5OptionButton3ActionPerformed(evt);
            }
        });
        RegularOptionsPanel1.add(Option5OptionButton3);

        Option5OptionButton5.setBackground(new java.awt.Color(255, 255, 255));
        Option5OptionButton5.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        Option5OptionButton5.setForeground(new java.awt.Color(0, 53, 91));
        Option5OptionButton5.setText("Teachers details");
        Option5OptionButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Option5OptionButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Option5OptionButton5MouseClicked(evt);
            }
        });
        Option5OptionButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Option5OptionButton5ActionPerformed(evt);
            }
        });
        RegularOptionsPanel1.add(Option5OptionButton5);

        Option5OptionButton6.setBackground(new java.awt.Color(255, 255, 255));
        Option5OptionButton6.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        Option5OptionButton6.setForeground(new java.awt.Color(0, 53, 91));
        Option5OptionButton6.setText("Students Details");
        Option5OptionButton6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Option5OptionButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Option5OptionButton6MouseClicked(evt);
            }
        });
        Option5OptionButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Option5OptionButton6ActionPerformed(evt);
            }
        });
        RegularOptionsPanel1.add(Option5OptionButton6);

        OtherOptionsPanel1.setBackground(new java.awt.Color(22, 37, 47));
        OtherOptionsPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Other options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Miriam Fixed", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        OtherOptionsPanel1.setLayout(new java.awt.GridLayout(2, 0));

        LogoutButton.setBackground(new java.awt.Color(255, 255, 255));
        LogoutButton.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        LogoutButton.setForeground(new java.awt.Color(0, 53, 91));
        LogoutButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/POTAM_Images/1486251154_on-off.png"))); // NOI18N
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

        PrincipalChangePasswordButton.setBackground(new java.awt.Color(255, 255, 255));
        PrincipalChangePasswordButton.setFont(new java.awt.Font("Miriam Fixed", 1, 16)); // NOI18N
        PrincipalChangePasswordButton.setForeground(new java.awt.Color(0, 53, 91));
        PrincipalChangePasswordButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/POTAM_Images/1485992392_free-38.png"))); // NOI18N
        PrincipalChangePasswordButton.setText("Change password");
        PrincipalChangePasswordButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PrincipalChangePasswordButtonMouseClicked(evt);
            }
        });
        PrincipalChangePasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrincipalChangePasswordButtonActionPerformed(evt);
            }
        });
        OtherOptionsPanel1.add(PrincipalChangePasswordButton);

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

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/POTAM_Images/1488324366_computer.png"))); // NOI18N

        javax.swing.GroupLayout HomePanelLayout = new javax.swing.GroupLayout(HomePanel);
        HomePanel.setLayout(HomePanelLayout);
        HomePanelLayout.setHorizontalGroup(
            HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)
        );
        HomePanelLayout.setVerticalGroup(
            HomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
        );

        MiddlePanel.add(HomePanel, "card7");

        AddBatchPanel.setBackground(new java.awt.Color(255, 255, 255));
        AddBatchPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153)), "ADD NEW BATCH", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Batch Name");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Starts on ( year )");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Ends on ( year )");

        jButton1.setBackground(new java.awt.Color(255, 204, 204));
        jButton1.setText("Cancel");

        addBatchButton.setBackground(new java.awt.Color(204, 255, 204));
        addBatchButton.setText("Add batch");
        addBatchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBatchButtonActionPerformed(evt);
            }
        });

        batchDeptCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel18.setText("Department");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setText("Number of students");

        javax.swing.GroupLayout AddBatchPanelLayout = new javax.swing.GroupLayout(AddBatchPanel);
        AddBatchPanel.setLayout(AddBatchPanelLayout);
        AddBatchPanelLayout.setHorizontalGroup(
            AddBatchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddBatchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AddBatchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AddBatchPanelLayout.createSequentialGroup()
                        .addGroup(AddBatchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(NumberofStudentsTF, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(batchDeptCombo, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(batchEndTF, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(batchStartsTF, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, AddBatchPanelLayout.createSequentialGroup()
                                .addComponent(addBatchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
                            .addComponent(batchNameTF, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(378, 378, 378)))
                .addContainerGap())
        );
        AddBatchPanelLayout.setVerticalGroup(
            AddBatchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddBatchPanelLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(batchNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(batchDeptCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(batchStartsTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(batchEndTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NumberofStudentsTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(AddBatchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(addBatchButton))
                .addContainerGap(170, Short.MAX_VALUE))
        );

        MiddlePanel.add(AddBatchPanel, "card2");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ADD HOD", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        jLabel4.setText("Employee id in register book");

        jLabel5.setText("Name");

        jLabel6.setText("Department");

        hodDeptCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setText("Personal info");

        hodInfoTA.setColumns(20);
        hodInfoTA.setRows(5);
        jScrollPane1.setViewportView(hodInfoTA);

        jButton3.setBackground(new java.awt.Color(204, 255, 204));
        jButton3.setText("Add Hod");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 204, 204));
        jButton4.setText("Cancel");

        jLabel23.setText("Joining date");

        jLabel24.setText("Qualification");

        hodQualification.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mtech", "Btech", "dipoama", "other degree", " " }));

        jLabel25.setText("DOB");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addComponent(hod_joinDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(71, 71, 71)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel24)
                                    .addComponent(hodQualification, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(hodDob, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(hodNameTF, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hodRegIdTF, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hodDeptCombo, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(292, 292, 292))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hodRegIdTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hodNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel25)
                .addGap(2, 2, 2)
                .addComponent(hodDob, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hodDeptCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hod_joinDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hodQualification, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap(64, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AddHodPanelLayout = new javax.swing.GroupLayout(AddHodPanel);
        AddHodPanel.setLayout(AddHodPanelLayout);
        AddHodPanelLayout.setHorizontalGroup(
            AddHodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        AddHodPanelLayout.setVerticalGroup(
            AddHodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        MiddlePanel.add(AddHodPanel, "card3");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ADD DEPARTMENT", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Department name");

        addDepartmentButton.setBackground(new java.awt.Color(204, 255, 204));
        addDepartmentButton.setText("Add Department");
        addDepartmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDepartmentButtonActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(255, 204, 204));
        jButton6.setText("Cancel");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setText("Maximum strength");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(maxStrengthTF, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(addDepartmentButton, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
                            .addComponent(departmentNameTF, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(362, 362, 362))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(departmentNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxStrengthTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton6)
                    .addComponent(addDepartmentButton))
                .addContainerGap(324, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AddDepartmentLayout = new javax.swing.GroupLayout(AddDepartment);
        AddDepartment.setLayout(AddDepartmentLayout);
        AddDepartmentLayout.setHorizontalGroup(
            AddDepartmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        AddDepartmentLayout.setVerticalGroup(
            AddDepartmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        MiddlePanel.add(AddDepartment, "card4");

        showBatches.setBackground(new java.awt.Color(255, 255, 255));
        showBatches.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Show batches", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.BELOW_TOP, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        showBatchTable.setAutoCreateRowSorter(true);
        showBatchTable.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        showBatchTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Batch id", "Batch name", "Starts on", "Ends on", "Max Students"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        showBatchTable.setGridColor(new java.awt.Color(204, 204, 204));
        showBatchTable.setRowHeight(30);
        jScrollPane4.setViewportView(showBatchTable);

        jButton2.setBackground(new java.awt.Color(204, 204, 255));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton2.setText("Print");

        deleteBatchesButton.setBackground(new java.awt.Color(255, 204, 204));
        deleteBatchesButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        deleteBatchesButton.setText("Delete batch");
        deleteBatchesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBatchesButtonActionPerformed(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(204, 255, 204));
        jButton15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton15.setText("Save changes");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout showBatchesLayout = new javax.swing.GroupLayout(showBatches);
        showBatches.setLayout(showBatchesLayout);
        showBatchesLayout.setHorizontalGroup(
            showBatchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
            .addGroup(showBatchesLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteBatchesButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        showBatchesLayout.setVerticalGroup(
            showBatchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(showBatchesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(showBatchesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(deleteBatchesButton)
                    .addComponent(jButton15))
                .addGap(12, 12, 12))
        );

        MiddlePanel.add(showBatches, "card8");

        showHodPanel.setBackground(new java.awt.Color(255, 255, 255));
        showHodPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "HOD DETAILS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        showHodTable.setAutoCreateRowSorter(true);
        showHodTable.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        showHodTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        showHodTable.setGridColor(new java.awt.Color(204, 204, 204));
        showHodTable.setRowHeight(30);
        jScrollPane5.setViewportView(showHodTable);

        removeHodButton.setText("Remove hod");
        removeHodButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeHodButtonActionPerformed(evt);
            }
        });

        jButton5.setText("Print");

        HodIDCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel26.setText("Select a hod id to view details");

        jButton16.setText("Show details");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel27.setText("Registered ID");

        HodIdTF.setEditable(false);
        HodIdTF.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        HodIdTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HodIdTFActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout showHodPanelLayout = new javax.swing.GroupLayout(showHodPanel);
        showHodPanel.setLayout(showHodPanelLayout);
        showHodPanelLayout.setHorizontalGroup(
            showHodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
            .addGroup(showHodPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(removeHodButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(showHodPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(showHodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(showHodPanelLayout.createSequentialGroup()
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(HodIDCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(showHodPanelLayout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(HodIdTF, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        showHodPanelLayout.setVerticalGroup(
            showHodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(showHodPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(showHodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(HodIDCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(jButton16))
                .addGap(21, 21, 21)
                .addGroup(showHodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(HodIdTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(showHodPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(removeHodButton))
                .addGap(12, 12, 12))
        );

        MiddlePanel.add(showHodPanel, "card9");

        ShowAllDeptPanel.setBackground(new java.awt.Color(255, 255, 255));

        showAllDeptTable.setAutoCreateRowSorter(true);
        showAllDeptTable.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        showAllDeptTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Dept Id", "Dept Name", "Maximum strength"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        showAllDeptTable.setGridColor(new java.awt.Color(204, 204, 204));
        showAllDeptTable.setRowHeight(30);
        jScrollPane6.setViewportView(showAllDeptTable);

        removeHodButton1.setBackground(new java.awt.Color(255, 204, 204));
        removeHodButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        removeHodButton1.setText("Remove department");
        removeHodButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeHodButton1ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(204, 204, 255));
        jButton7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton7.setText("Print");

        jButton14.setBackground(new java.awt.Color(204, 255, 204));
        jButton14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton14.setText("Save edited details");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ShowAllDeptPanelLayout = new javax.swing.GroupLayout(ShowAllDeptPanel);
        ShowAllDeptPanel.setLayout(ShowAllDeptPanelLayout);
        ShowAllDeptPanelLayout.setHorizontalGroup(
            ShowAllDeptPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)
            .addGroup(ShowAllDeptPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(removeHodButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        ShowAllDeptPanelLayout.setVerticalGroup(
            ShowAllDeptPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ShowAllDeptPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ShowAllDeptPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ShowAllDeptPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton7)
                        .addComponent(removeHodButton1))
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        MiddlePanel.add(ShowAllDeptPanel, "card10");

        removeHodButton2.setText("Remove teacher");
        removeHodButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeHodButton2ActionPerformed(evt);
            }
        });

        jButton9.setText("Print");

        jLabel28.setText("Select a teacher id  to view details");

        teacherCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton17.setText("Show details");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jLabel29.setText("Registered ID");

        HodIdTF1.setEditable(false);
        HodIdTF1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        HodIdTF1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HodIdTF1ActionPerformed(evt);
            }
        });

        showAllTeacherTable.setAutoCreateRowSorter(true);
        showAllTeacherTable.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        showAllTeacherTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        showAllTeacherTable.setGridColor(new java.awt.Color(204, 204, 204));
        showAllTeacherTable.setRowHeight(30);
        jScrollPane9.setViewportView(showAllTeacherTable);

        javax.swing.GroupLayout ShowAllTeacherLayout = new javax.swing.GroupLayout(ShowAllTeacher);
        ShowAllTeacher.setLayout(ShowAllTeacherLayout);
        ShowAllTeacherLayout.setHorizontalGroup(
            ShowAllTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ShowAllTeacherLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(removeHodButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(ShowAllTeacherLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ShowAllTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ShowAllTeacherLayout.createSequentialGroup()
                        .addGroup(ShowAllTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ShowAllTeacherLayout.createSequentialGroup()
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(teacherCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(ShowAllTeacherLayout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(HodIdTF1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE))
                .addContainerGap())
        );
        ShowAllTeacherLayout.setVerticalGroup(
            ShowAllTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ShowAllTeacherLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ShowAllTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(teacherCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(jButton17))
                .addGap(21, 21, 21)
                .addGroup(ShowAllTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(HodIdTF1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                .addGap(4, 4, 4)
                .addGroup(ShowAllTeacherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9)
                    .addComponent(removeHodButton2))
                .addGap(12, 12, 12))
        );

        MiddlePanel.add(ShowAllTeacher, "card11");

        ShowStudents.setBackground(new java.awt.Color(255, 255, 255));
        ShowStudents.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Student details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

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

        removeHodButton3.setText("Remove student");
        removeHodButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeHodButton3ActionPerformed(evt);
            }
        });

        jButton11.setText("Print");

        jButton12.setText("Student details");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel20.setText("Select a batch");

        StudentBatchCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton13.setText("Show students");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ShowStudentsLayout = new javax.swing.GroupLayout(ShowStudents);
        ShowStudents.setLayout(ShowStudentsLayout);
        ShowStudentsLayout.setHorizontalGroup(
            ShowStudentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
            .addGroup(ShowStudentsLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeHodButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(ShowStudentsLayout.createSequentialGroup()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(StudentBatchCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton13)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        ShowStudentsLayout.setVerticalGroup(
            ShowStudentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ShowStudentsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ShowStudentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(StudentBatchCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13))
                .addGap(9, 9, 9)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ShowStudentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton11)
                    .addComponent(removeHodButton3)
                    .addComponent(jButton12))
                .addGap(12, 12, 12))
        );

        MiddlePanel.add(ShowStudents, "card12");

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

    private void Option1OptionButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Option1OptionButtonMouseClicked
        // TODO add your handling code here:
        //this.ButtonSelection(MemberRegOptionButton, null);
    }//GEN-LAST:event_Option1OptionButtonMouseClicked

    private void Option2OptionButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Option2OptionButtonMouseClicked
        // TODO add your handling code here:
        //this.ButtonSelection(MonthlyFeeOptionButton, null);
    }//GEN-LAST:event_Option2OptionButtonMouseClicked

    private void Option5OptionButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Option5OptionButtonMouseClicked
        // TODO add your handling code here:
        //this.ButtonSelection(CreditTransactionOptionButton, null);
    }//GEN-LAST:event_Option5OptionButtonMouseClicked

    private void LogoutButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LogoutButtonMouseClicked
        // TODO add your handling code here:
        //this.ButtonSelection(LogoutButton, null);
    }//GEN-LAST:event_LogoutButtonMouseClicked

    private void HomeOptionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HomeOptionButtonActionPerformed
        this.setPanelVisibility(HomePanel);
    }//GEN-LAST:event_HomeOptionButtonActionPerformed

    private void Option1OptionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Option1OptionButtonActionPerformed
        setPanelVisibility(AddDepartment);
    }//GEN-LAST:event_Option1OptionButtonActionPerformed

    private void LogoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutButtonActionPerformed
        new LoginForm().setVisible(true);
        dispose();
    }//GEN-LAST:event_LogoutButtonActionPerformed

    private void Option2OptionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Option2OptionButtonActionPerformed
      
        ResultSet result;
        hodDeptCombo.removeAllItems();
        try {
            DatabaseManager dbManager = new DatabaseManager();
            
            result = dbManager.selectAllDepartments();
            
            if ( result.isBeforeFirst() ) {    
              
                while(result.next()){
                
                    hodDeptCombo.addItem(result.getString("Department_Name"));
                
                }
              
            } 
            
            
            setPanelVisibility(AddHodPanel); 
           
        }
        catch(ClassNotFoundException | SQLException e){
            
        }
    }//GEN-LAST:event_Option2OptionButtonActionPerformed

    private void showBatchesButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showBatchesButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_showBatchesButtonMouseClicked

    private void Option5OptionButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Option5OptionButton3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_Option5OptionButton3MouseClicked

    private void Option5OptionButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Option5OptionButton4MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_Option5OptionButton4MouseClicked

    private void Option5OptionButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Option5OptionButton5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_Option5OptionButton5MouseClicked

    private void Option5OptionButton6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Option5OptionButton6MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_Option5OptionButton6MouseClicked

    private void Option5OptionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Option5OptionButtonActionPerformed
        // TODO add your handling code here:
        ResultSet result;
        batchDeptCombo.removeAllItems();
        try {
            DatabaseManager dbManager = new DatabaseManager();
            
            result = dbManager.selectAllDepartments();
            
            if ( result.isBeforeFirst() ) {    
              
                while(result.next()){
                
                  batchDeptCombo.addItem(result.getString("Department_Name"));
                
                }
              
            } 
        }
        catch(ClassNotFoundException | SQLException e){
            
        }
        
        setPanelVisibility(AddBatchPanel);
    }//GEN-LAST:event_Option5OptionButtonActionPerformed

    private void addBatchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBatchButtonActionPerformed
        // TODO add your handling code here:
        
        // get values from text fields
        String batchName         = batchNameTF.getText();
        String batchStartOn      = batchStartsTF.getText();
        String batchEndsOn       = batchEndTF.getText();
        String batchdeptName     = batchDeptCombo.getSelectedItem().toString();
        String numberOfStudents  = NumberofStudentsTF.getText();
        // check null validation
        JOptionPane.showMessageDialog(null,batchdeptName);
        if(!batchName.equals("") && !batchStartOn.equals("") && !batchEndsOn.equals("") && !batchdeptName.equals("") && !numberOfStudents.equals("")){
            
            try {
                // Add details to batch table on database.
                Database.DatabaseManager dbManager = new DatabaseManager();
                
                int batchDeptId      =  dbManager.getDepartmentIdWithName(batchdeptName);
                int studentsNumber   =  Integer.valueOf(numberOfStudents);
                if(batchDeptId > 0){
                    if( dbManager.addNewBatch(batchName, batchStartOn, batchEndsOn,batchDeptId,studentsNumber)){

                        JOptionPane.showMessageDialog(rootPane," New batch added ");
                        clearAddBatchFields();
                    }
                    else{

                        JOptionPane.showMessageDialog(rootPane," Failed to add new batch ");
                    }
                }
                    
                
                
            } catch (ClassNotFoundException | SQLException ex) {
                
                Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, ex);
                
            }
            catch(NumberFormatException e)
            {
                JOptionPane.showMessageDialog(rootPane,"please enter valid data");
            }
            
            
        }
        // validation failed.
        else{
            
            JOptionPane.showMessageDialog(null,"Please enter all the fields");
        }
            
    }//GEN-LAST:event_addBatchButtonActionPerformed

    private void addDepartmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDepartmentButtonActionPerformed
        // TODO add your handling code here:
        String departmentName   = departmentNameTF.getText();
        String maximumStrength  = maxStrengthTF.getText();
        // department name is not empty
        if(! departmentName.equals("") && !maximumStrength.equals("")){
            
            try {
                
                // add department to database.
                int maxStrength = Integer.valueOf(maximumStrength);
                DatabaseManager dbManager = new DatabaseManager();
                if( dbManager.addDepartment(departmentName, maxStrength)){
                    
                    JOptionPane.showMessageDialog(rootPane," New department added ");
                    clearDepartmentFields();
                }
                else{
                    
                    JOptionPane.showMessageDialog(rootPane," Failed to add new department ");
                }
                
                
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, ex);
            }
            
                    
            
        }else{
            
            
        }
    }//GEN-LAST:event_addDepartmentButtonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            // TODO add your handling code here:
            SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd");
            DatabaseManager dbManager = new DatabaseManager();
            String hodRegId  = hodRegIdTF.getText();
            String hodName   = hodNameTF.getText();
            String hodInfo   = hodInfoTA.getText();
            String deptName  = hodDeptCombo.getSelectedItem().toString();
            Date   dob       = hodDob.getDate();
            String strDob    = dformat.format(dob);
            String joiningDate = dformat.format(hod_joinDate.getDate());
            String hodqualification = hodQualification.getSelectedItem().toString();
            
            if( !hodRegId.equals("") && !hodName.equals("") && !hodInfo.equals("") && 
                    !deptName.equals("") && !strDob.equals("") && !joiningDate.equals("") &&
                    !hodqualification.equals(""))
            {
                
                int hodDeptId =  dbManager.getDepartmentIdWithName(deptName);
                if(hodDeptId > 0){
                    
                    if( dbManager.addHODWith(hodRegId,hodName,hodInfo,hodDeptId,strDob,joiningDate,hodqualification) ){
                        
                        JOptionPane.showMessageDialog(hodDeptCombo, "New Hod added sucessfully");
                        JOptionPane.showMessageDialog(rootPane, "The auto generated id for the HOD is : "+new DatabaseManager().getHodIdFromRegId(hodRegId));
                        clearAddHodFields();
                        
                    }
                    else{
                        JOptionPane.showMessageDialog(hodDeptCombo, "failed to add new hod");
                    }
                        
                }
                
            }
        }
        catch(ClassNotFoundException | SQLException e){
            Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, e); 
        }
       
    }//GEN-LAST:event_jButton3ActionPerformed

    private void showBatchesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showBatchesButtonActionPerformed
       setShowAllBatchTable();
    }//GEN-LAST:event_showBatchesButtonActionPerformed

    private void deleteBatchesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBatchesButtonActionPerformed
        // TODO add your handling code here:
        try{
           
            DatabaseManager dbManager = new DatabaseManager();
            int selectedRows[]        = showBatchTable.getSelectedRows();
            int selectedBatchId[]     = new int[selectedRows.length];
            int nIndex = 0;
            for( int row : selectedRows ){
               selectedBatchId[nIndex] = Integer.parseInt( showBatchTable.getValueAt(row,0).toString() );
               nIndex++;
            }
            
            if(dbManager.deleteSelectedBatch(selectedBatchId)){
                
                setShowAllBatchTable();
                
            }else{
                
                
            }
            
        }catch(ClassNotFoundException | SQLException | NumberFormatException | HeadlessException e){
            
            Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, e); 
        }
        
    }//GEN-LAST:event_deleteBatchesButtonActionPerformed

    private void removeHodButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeHodButtonActionPerformed
        // TODO add your handling code here:
        
        try{
                String regID = HodIdTF.getText();
                if (regID != null)
                {
                    DatabaseManager dbManager = new DatabaseManager();
                    /*int selectedRows[]        = showHodTable.getSelectedRows();
                    String selectedHodId[]       = new String[selectedRows.length];
                    int nIndex = 0;
                    for( int row : selectedRows ){
                       selectedHodId[nIndex] = showHodTable.getValueAt(row,0).toString();
                       nIndex++;
                    }*/

                    if(dbManager.removeHod(regID))
                    {

                       setHodDetailsTable();

                    }
                    else
                    {
                    }
                }
            
            
        }catch(ClassNotFoundException | SQLException | NumberFormatException | HeadlessException e){
            
            Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, e); 
        }
        setPanelVisibility(showHodPanel);
    }//GEN-LAST:event_removeHodButtonActionPerformed

    private void Option5OptionButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Option5OptionButton3ActionPerformed
        // TODO add your handling code here:
        try
        {
            DatabaseManager dbManager  = new DatabaseManager();
                ResultSet allHod        = dbManager.selectAllHod();
                HodIDCombo.removeAllItems();
                if(allHod.isBeforeFirst())
                {

                    while(allHod.next())
                    {
                        HodIDCombo.addItem(allHod.getString("HOD_ID"));
                    }
                }
            setPanelVisibility(showHodPanel);
        }
        catch(Exception e)
        {
           Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, e); 
        }
    }//GEN-LAST:event_Option5OptionButton3ActionPerformed

    private void Option5OptionButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Option5OptionButton4ActionPerformed
        // TODO add your handling code here:
        setShowAllDepartmentTable();
        setPanelVisibility(ShowAllDeptPanel);
        
        
    }//GEN-LAST:event_Option5OptionButton4ActionPerformed

    private void Option5OptionButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Option5OptionButton5ActionPerformed
        // TODO add your handling code here:
         try
        {
            DatabaseManager dbManager  = new DatabaseManager();
                ResultSet allHod        = dbManager.selectAllTeachers();
                teacherCombo.removeAllItems();
                if(allHod.isBeforeFirst())
                {

                    while(allHod.next())
                    {
                        teacherCombo.addItem(allHod.getString("Teacher_ID"));
                    }
                }
            setPanelVisibility(ShowAllTeacher);
        }
        catch(Exception e)
        {
           Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, e); 
        }
        
    }//GEN-LAST:event_Option5OptionButton5ActionPerformed

    private void Option5OptionButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Option5OptionButton6ActionPerformed
        // TODO add your handling code here:
        // show all batches
        DatabaseManager dbManager;
        try {
            dbManager = new DatabaseManager();
            ResultSet result = dbManager.selectAllBatches();            
            if ( result.isBeforeFirst() ) {    
                StudentBatchCombo.removeAllItems();
                while(result.next()){
                
                    StudentBatchCombo.addItem(result.getString("Batch_Name"));                
                }              
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        setPanelVisibility(ShowStudents);
    }//GEN-LAST:event_Option5OptionButton6ActionPerformed

    private void removeHodButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeHodButton1ActionPerformed
        // TODO add your handling code here:
        try{
           
            DatabaseManager dbManager = new DatabaseManager();
            int selectedRows[]        = showAllDeptTable.getSelectedRows();
            String selectedId[]       = new String[selectedRows.length];
            int nIndex = 0;
            for( int row : selectedRows ){
               selectedId[nIndex] = showAllDeptTable.getValueAt(row,0).toString();
               nIndex++;
            }
            
            if(dbManager.deleteSelectedDept(selectedId)){
                
               setShowAllDepartmentTable();
               setPanelVisibility(ShowAllDeptPanel);
                
            }else{
                
                
            }
            
        }catch(ClassNotFoundException | SQLException | NumberFormatException | HeadlessException e){
            
            Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, e); 
        }
       
        
    }//GEN-LAST:event_removeHodButton1ActionPerformed

    private void removeHodButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeHodButton2ActionPerformed
        // TODO add your handling code here:
      try{
           String regID = HodIdTF1.getText();
           
           if(regID != null)
           {
               DatabaseManager dbManager = new DatabaseManager();
                /*int selectedRows[]        = showAllTeacherTable.getSelectedRows();
                String selectedId[]       = new String[selectedRows.length];
                int nIndex = 0;
                for( int row : selectedRows ){
                   selectedId[nIndex] = showAllTeacherTable.getValueAt(row,0).toString();
                   nIndex++;
                }*/

                if(dbManager.deleteSelectedTeachers(regID)){

                   showAllTeacherTable();

                }else{


                }
           }
            
            
        }catch(ClassNotFoundException | SQLException | NumberFormatException | HeadlessException e){
            
            Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, e); 
        }  
        
    }//GEN-LAST:event_removeHodButton2ActionPerformed

    private void removeHodButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeHodButton3ActionPerformed
        // TODO add your handling code here:
        try{
           
            DatabaseManager dbManager = new DatabaseManager();
            int selectedRows[]        = showAllStudentsTable.getSelectedRows();
            String selectedId[]       = new String[selectedRows.length];
            int nIndex = 0;
            for( int row : selectedRows ){
               selectedId[nIndex] = showAllStudentsTable.getValueAt(row,0).toString();
               nIndex++;
            }
            
            if(dbManager.deleteSelectedStudents(selectedId)){
                
               String batchName = StudentBatchCombo.getSelectedItem().toString();
               int batchId = dbManager.getBatchIdFromBatchName(batchName);
               if(batchId > 0)
               {
                    setShowAllStudentsTable(batchId);
               }
                
            }else{
                
                
            }
            
        }catch(ClassNotFoundException | SQLException | NumberFormatException | HeadlessException e){
            
            Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, e); 
        }  
        
    }//GEN-LAST:event_removeHodButton3ActionPerformed

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

    private void PrincipalChangePasswordButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PrincipalChangePasswordButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_PrincipalChangePasswordButtonMouseClicked

    private void PrincipalChangePasswordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrincipalChangePasswordButtonActionPerformed
        // TODO add your handling code here:
        ChangePasswordForm cpwd = new ChangePasswordForm("principal");
        cpwd.setVisible(true);
    }//GEN-LAST:event_PrincipalChangePasswordButtonActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        
        String batchName = StudentBatchCombo.getSelectedItem().toString();
        try {
            DatabaseManager dbmanager = new DatabaseManager();
            if(!batchName.equals(""))
            {
                int batchId = dbmanager.getBatchIdFromBatchName(batchName);
                if(batchId > 0)
                {
                    setShowAllStudentsTable(batchId);
                }
            }
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        int selectedRow = showAllDeptTable.getSelectedRow();
        if(selectedRow >= 0)
        {
            int      deptId       = Integer.valueOf(showAllDeptTable.getValueAt(selectedRow,0).toString());
            String   deptName     = showAllDeptTable.getValueAt(selectedRow, 1).toString();
            int      maxStrength  = Integer.valueOf(showAllDeptTable.getValueAt(selectedRow,2).toString());
            
            DatabaseManager dbManager;
            try {
                dbManager = new DatabaseManager();
                if( dbManager.updateDepartment(deptId,deptName,maxStrength) )
                {
                    JOptionPane.showMessageDialog(rootPane,"Details updated sucessfully");
                }
                else
                {
                   JOptionPane.showMessageDialog(rootPane,"Failed to update department details");
                }
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
        else
        {
            JOptionPane.showMessageDialog(rootPane,"Please select a row to save edited details");
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
      try{
          
        
        int selectedRow = showBatchTable.getSelectedRow();
        if(selectedRow >= 0)
        {
            int batchId        = Integer.valueOf( showBatchTable.getValueAt(selectedRow, 0).toString() );
            String batchName   = showBatchTable.getValueAt(selectedRow, 1).toString();
            String startsOn    = showBatchTable.getValueAt(selectedRow, 2).toString();
            String endsOn      = showBatchTable.getValueAt(selectedRow, 3).toString();
            int maxStrength    = Integer.valueOf( showBatchTable.getValueAt(selectedRow, 4).toString() );
            
            DatabaseManager dbManager = new DatabaseManager();
            if( dbManager.updateBatch(batchId, batchName, startsOn, endsOn, maxStrength) )
            {
                JOptionPane.showMessageDialog(rootPane, "Changes updated");
            }
            else
            {
                 JOptionPane.showMessageDialog(rootPane, "Modification update failed");
            }
            
            
        }
        else
        {
            JOptionPane.showMessageDialog(rootPane,"Please select the row that you want to save changes.");
        }
      }catch(NumberFormatException e)
      {
          JOptionPane.showMessageDialog(rootPane,"Please enter valid details.");
      }
      catch(SQLException | ClassNotFoundException ex )
      {
         Logger.getLogger(PrincipalHome.class.getName()).log(Level.SEVERE, null, ex);
          
      } 
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
        setHodDetailsTable();
        
        
    }//GEN-LAST:event_jButton16ActionPerformed

    private void HodIdTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HodIdTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_HodIdTFActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
        
        showAllTeacherTable();
    }//GEN-LAST:event_jButton17ActionPerformed

    private void HodIdTF1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HodIdTF1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_HodIdTF1ActionPerformed

    private void clearAddBatchFields(){
        batchNameTF.setText("");
        batchStartsTF.setText("");
        batchEndTF.setText("");
    }
    
    private void clearDepartmentFields(){
        
        departmentNameTF.setText("");
    }
    private void clearAddHodFields(){
        hodRegIdTF.setText("");
        hodNameTF.setText("");
        hodInfoTA.setText("");
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       new PrincipalHome().setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AddBatchPanel;
    private javax.swing.JPanel AddDepartment;
    private javax.swing.JPanel AddHodPanel;
    private javax.swing.JLabel HeaderLogoLabel;
    private javax.swing.JPanel HeaderPanel;
    private javax.swing.JComboBox HodIDCombo;
    private javax.swing.JTextField HodIdTF;
    private javax.swing.JTextField HodIdTF1;
    private javax.swing.JButton HomeOptionButton;
    private javax.swing.JPanel HomePanel;
    private javax.swing.JLabel InfoLabelHeader2;
    private javax.swing.JButton LogoutButton;
    private javax.swing.JPanel MiddlePanel;
    private javax.swing.JTextField NumberofStudentsTF;
    private javax.swing.JButton Option1OptionButton;
    private javax.swing.JButton Option2OptionButton;
    private javax.swing.JButton Option5OptionButton;
    private javax.swing.JButton Option5OptionButton3;
    private javax.swing.JButton Option5OptionButton4;
    private javax.swing.JButton Option5OptionButton5;
    private javax.swing.JButton Option5OptionButton6;
    private javax.swing.JPanel OtherOptionsPanel1;
    private javax.swing.JButton PrincipalChangePasswordButton;
    private javax.swing.JPanel RegularOptionsPanel1;
    private javax.swing.JPanel ShowAllDeptPanel;
    private javax.swing.JPanel ShowAllTeacher;
    private javax.swing.JPanel ShowStudents;
    private javax.swing.JPanel SideOptionsPanel;
    private javax.swing.JComboBox StudentBatchCombo;
    private javax.swing.JButton addBatchButton;
    private javax.swing.JButton addDepartmentButton;
    private javax.swing.JComboBox batchDeptCombo;
    private javax.swing.JTextField batchEndTF;
    private javax.swing.JTextField batchNameTF;
    private javax.swing.JTextField batchStartsTF;
    private javax.swing.JButton deleteBatchesButton;
    private javax.swing.JTextField departmentNameTF;
    private javax.swing.JComboBox hodDeptCombo;
    private org.jdesktop.swingx.JXDatePicker hodDob;
    private javax.swing.JTextArea hodInfoTA;
    private javax.swing.JTextField hodNameTF;
    private javax.swing.JComboBox hodQualification;
    private javax.swing.JTextField hodRegIdTF;
    private org.jdesktop.swingx.JXDatePicker hod_joinDate;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextField maxStrengthTF;
    private javax.swing.JButton removeHodButton;
    private javax.swing.JButton removeHodButton1;
    private javax.swing.JButton removeHodButton2;
    private javax.swing.JButton removeHodButton3;
    private javax.swing.JTable showAllDeptTable;
    private javax.swing.JTable showAllStudentsTable;
    private javax.swing.JTable showAllTeacherTable;
    private javax.swing.JTable showBatchTable;
    private javax.swing.JPanel showBatches;
    private javax.swing.JButton showBatchesButton;
    private javax.swing.JPanel showHodPanel;
    private javax.swing.JTable showHodTable;
    private javax.swing.JComboBox teacherCombo;
    // End of variables declaration//GEN-END:variables
}
