package localizationsim.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import localizationsim.SimSystem;
import localizationsim.behavior.RobotControlGUIBehavior;
import localizationsim.robot.IRobot;

public class RobotControllerGUI extends JFrame {

  private static final String FOLDER_GUI_IMG = "gui_img";
  private static final String IMG_FOLDER_PATH = SimSystem.getProperty(FOLDER_GUI_IMG);
  private static final double inclANGULAR_VELOCITY = Math.PI /100.0D;
  private static final double inclVelocity = 0.1D;
  /**
   * 
   */
  private static final long serialVersionUID = -3902922243803264793L;
  private JTextField velocityTextField;
  private JTextField angularVelTextField;
  private RobotControlGUIBehavior behavior;

  private void updateVelocityFields(){
    IRobot robo = behavior.getTargetRobot();
    
    velocityTextField.setText(
        Double.toString(robo.getCruiseController().getVelocity()));
    angularVelTextField.setText(
        Double.toString(robo.getCruiseController().getAngularVelocity()));

  }
  
  
  public RobotControllerGUI(IRobot robot) {
    super();
    behavior = new RobotControlGUIBehavior(robot);
    setTitle("RobotController robot ID : "+robot.getRobotPosition().getID());

    JPanel panel = new JPanel();
    getContentPane().add(panel, BorderLayout.CENTER);
    GridBagLayout gbl_panel = new GridBagLayout();
    gbl_panel.columnWidths = new int[] { 0, 0, 0, 0 };
    gbl_panel.rowHeights = new int[] { 0, 0, 0, 0 };
    gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
    gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
    panel.setLayout(gbl_panel);

    JButton btnAhead = new JButton("");
    btnAhead.setIcon(new ImageIcon(RobotControllerGUI.class
        .getResource(IMG_FOLDER_PATH+"upArrow.png")));
    GridBagConstraints gbc_btnAhead = new GridBagConstraints();
    gbc_btnAhead.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnAhead.insets = new Insets(0, 0, 5, 5);
    gbc_btnAhead.gridx = 1;
    gbc_btnAhead.gridy = 0;
    panel.add(btnAhead, gbc_btnAhead);
    btnAhead.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        behavior.speedUp(inclVelocity);
        updateVelocityFields();
        
      }
    });

    JButton btnClocwise = new JButton("");
    btnClocwise.setIcon(new ImageIcon(RobotControllerGUI.class
        .getResource(IMG_FOLDER_PATH+"ccwRoll.png")));
    GridBagConstraints gbc_btnClocwise = new GridBagConstraints();
    gbc_btnClocwise.fill = GridBagConstraints.VERTICAL;
    gbc_btnClocwise.insets = new Insets(0, 0, 5, 5);
    gbc_btnClocwise.gridx = 0;
    gbc_btnClocwise.gridy = 1;
    panel.add(btnClocwise, gbc_btnClocwise);
    btnClocwise.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        behavior.rotateSpeedUp(-inclANGULAR_VELOCITY);
        updateVelocityFields();
      }
    });

    JButton btnStop = new JButton("");
    btnStop.setIcon(new ImageIcon(RobotControllerGUI.class
        .getResource(IMG_FOLDER_PATH+"stop.png")));
    GridBagConstraints gbc_btnStop = new GridBagConstraints();
    gbc_btnStop.ipady = 5;
    gbc_btnStop.ipadx = 5;
    gbc_btnStop.insets = new Insets(0, 0, 5, 5);
    gbc_btnStop.gridx = 1;
    gbc_btnStop.gridy = 1;
    panel.add(btnStop, gbc_btnStop);
    btnStop.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        behavior.stopMove();
        updateVelocityFields();
      }
    });

    JButton btnCounterClockwise = new JButton("");
    btnCounterClockwise.setIcon(new ImageIcon(RobotControllerGUI.class
        .getResource(IMG_FOLDER_PATH+"cwRoll.png")));
    GridBagConstraints gbc_btnCounterClockwise = new GridBagConstraints();
    gbc_btnCounterClockwise.fill = GridBagConstraints.VERTICAL;
    gbc_btnCounterClockwise.insets = new Insets(0, 0, 5, 0);
    gbc_btnCounterClockwise.gridx = 2;
    gbc_btnCounterClockwise.gridy = 1;
    panel.add(btnCounterClockwise, gbc_btnCounterClockwise);
    btnCounterClockwise.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        behavior.rotateSpeedUp(inclANGULAR_VELOCITY);
        updateVelocityFields();
      }
    });

    JButton btnBack = new JButton("");
    btnBack.setIcon(new ImageIcon(RobotControllerGUI.class
        .getResource(IMG_FOLDER_PATH+"backArrow.png")));
    GridBagConstraints gbc_btnBack = new GridBagConstraints();
    gbc_btnBack.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnBack.insets = new Insets(0, 0, 0, 5);
    gbc_btnBack.gridx = 1;
    gbc_btnBack.gridy = 2;
    panel.add(btnBack, gbc_btnBack);
    
    JPanel panel_1 = new JPanel();
    getContentPane().add(panel_1, BorderLayout.NORTH);
    GridBagLayout gbl_panel_1 = new GridBagLayout();
    gbl_panel_1.columnWidths = new int[]{42, 41, 51, 80, 51, 0};
    gbl_panel_1.rowHeights = new int[]{0, 19, 0};
    gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    gbl_panel_1.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
    panel_1.setLayout(gbl_panel_1);
    
    JLabel lbVel = new JLabel("Velocity");
    GridBagConstraints gbc_lbVel = new GridBagConstraints();
    gbc_lbVel.anchor = GridBagConstraints.WEST;
    gbc_lbVel.insets = new Insets(0, 0, 5, 5);
    gbc_lbVel.gridx = 1;
    gbc_lbVel.gridy = 0;
    panel_1.add(lbVel, gbc_lbVel);
    
    velocityTextField = new JTextField();
    velocityTextField.setBackground(Color.WHITE);
    velocityTextField.setEditable(false);
    GridBagConstraints gbc_velocityTextField = new GridBagConstraints();
    gbc_velocityTextField.anchor = GridBagConstraints.NORTHWEST;
    gbc_velocityTextField.insets = new Insets(0, 0, 5, 5);
    gbc_velocityTextField.gridx = 2;
    gbc_velocityTextField.gridy = 0;
    panel_1.add(velocityTextField, gbc_velocityTextField);
    velocityTextField.setColumns(12);
    
    JLabel lblms = new JLabel("(m/s)");
    lblms.setHorizontalAlignment(SwingConstants.LEFT);
    GridBagConstraints gbc_lblms = new GridBagConstraints();
    gbc_lblms.insets = new Insets(0, 0, 5, 5);
    gbc_lblms.gridx = 3;
    gbc_lblms.gridy = 0;
    panel_1.add(lblms, gbc_lblms);
    
    JLabel lbAngleVelLabel = new JLabel("AngularVelocity");
    GridBagConstraints gbc_lbAngleVelLabel = new GridBagConstraints();
    gbc_lbAngleVelLabel.insets = new Insets(0, 0, 0, 5);
    gbc_lbAngleVelLabel.gridx = 1;
    gbc_lbAngleVelLabel.gridy = 1;
    panel_1.add(lbAngleVelLabel, gbc_lbAngleVelLabel);
    
    angularVelTextField = new JTextField();
    angularVelTextField.setBackground(Color.WHITE);
    angularVelTextField.setEditable(false);
    GridBagConstraints gbc_angularVelTextField = new GridBagConstraints();
    gbc_angularVelTextField.insets = new Insets(0, 0, 0, 5);
    gbc_angularVelTextField.anchor = GridBagConstraints.NORTHWEST;
    gbc_angularVelTextField.gridx = 2;
    gbc_angularVelTextField.gridy = 1;
    panel_1.add(angularVelTextField, gbc_angularVelTextField);
    angularVelTextField.setColumns(12);
    
    JLabel lblrads = new JLabel("(rad/s)");
    lblrads.setHorizontalAlignment(SwingConstants.LEFT);
    GridBagConstraints gbc_lblrads = new GridBagConstraints();
    gbc_lblrads.insets = new Insets(0, 0, 0, 5);
    gbc_lblrads.gridx = 3;
    gbc_lblrads.gridy = 1;
    panel_1.add(lblrads, gbc_lblrads);

    btnBack.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        behavior.speedUp(-inclVelocity);
      }
    });
    
    this.pack();
    this.setResizable(false);
    this.updateVelocityFields();
  }

}
