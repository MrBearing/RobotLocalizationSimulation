package localizationsim.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import localizationsim.SimSystem;
import localizationsim.draw.Drawable;
import localizationsim.env.SimWorld;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class SimGUI extends JFrame {

  /**
   * 
   */
  private static final long serialVersionUID = 1221572248654911420L;
  
  
  private final SimWorld simSpace;
  
  
  private DrawableDrawPanel drawablePanel;
  private ScheduledFuture<?> future;
  private ScheduledExecutorService scheduledService;
  private final JSlider slider;
  
  public void start() {
    Runnable runnalble = new Runnable() {
      @Override
      public void run() {
        //一定時間ごとにrepaintを行う。
        getDrawableDrawPanel().repaint();
      }
    };

    this.future = this.scheduledService.scheduleAtFixedRate(runnalble, 0,
        (long) Long.valueOf(SimSystem.getProperty("gui_period")) , TimeUnit.MILLISECONDS);

    SimSystem.startSimulation();
  }

  public void stop() {
    if (this.future != null) {
      this.future.cancel(true);
    }

    SimSystem.stopSimulation();
  }

  public SimGUI() {
    super();
    //TODO メニューバーの追加ロボットGUIの表示非表示の切り替えなど。
    
    this.simSpace =SimSystem.getSimWorld();

    scheduledService = Executors.newSingleThreadScheduledExecutor();

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());

    setDrawableDrawPanel(new DrawableDrawPanel());
    getDrawableDrawPanel().addDrawableObject(this.simSpace);
    
    Dimension dimension = new Dimension((int) this.simSpace.getWidth(),
        (int) this.simSpace.getHeight());
    
    this.getDrawableDrawPanel().setPreferredSize(dimension);
    this.getDrawableDrawPanel().setSize(dimension);
    
    
    getDrawableDrawPanel().setBorder(new LineBorder(Color.black));
    getDrawableDrawPanel().setBackground(Color.WHITE);
    
    
    mainPanel.add(getDrawableDrawPanel(), BorderLayout.CENTER);

    this.setContentPane(mainPanel);

    JPanel buttonPanel = new JPanel();
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    GridBagLayout gbl_buttonPanel = new GridBagLayout();
    gbl_buttonPanel.columnWidths = new int[] { 0, 0, 0 };
    gbl_buttonPanel.rowHeights = new int[] { 0, 0 };
    gbl_buttonPanel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
    gbl_buttonPanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
    buttonPanel.setLayout(gbl_buttonPanel);

    JButton btnStart = new JButton("Start");
    GridBagConstraints gbc_btnStart = new GridBagConstraints();
    gbc_btnStart.insets = new Insets(0, 0, 0, 5);
    gbc_btnStart.gridx = 0;
    gbc_btnStart.gridy = 0;
    buttonPanel.add(btnStart, gbc_btnStart);
    btnStart.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        start();
      }
    });

    JButton btnStop = new JButton("Stop");
    GridBagConstraints gbc_btnStop = new GridBagConstraints();
    gbc_btnStop.gridx = 1;
    gbc_btnStop.gridy = 0;
    buttonPanel.add(btnStop, gbc_btnStop);
    
    slider = new JSlider();
    slider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent arg0) {
        double scale = ((double) slider.getValue())
            / ((double) slider.getMaximum());
        getDrawableDrawPanel().setScale(scale);
        repaint();
      }
    });
    slider.setValue(100);
    slider.setMinimum(5);
    slider.setOrientation(SwingConstants.VERTICAL);
    mainPanel.add(slider, BorderLayout.WEST);
    btnStop.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        stop();

      }
    });

    this.pack();
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public void addDrawableObject(Drawable dobj){
    this.drawablePanel.addDrawableObject(dobj);
  }
  
  public DrawableDrawPanel getDrawableDrawPanel() {
    return drawablePanel;
  }

  private void setDrawableDrawPanel(DrawableDrawPanel panel) {
    this.drawablePanel = panel;
  }
}
