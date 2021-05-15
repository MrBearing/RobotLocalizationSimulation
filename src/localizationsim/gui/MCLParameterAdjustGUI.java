package localizationsim.gui;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JSlider;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import localizationsim.localization.MCLocalizationer;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class MCLParameterAdjustGUI extends JFrame {
  private static final int MAX_ = 10000;
  /**
   * 
   */

  private static final long serialVersionUID = 1L;
  private JTextField textFast;
  private JTextField textSlow;
  private MCLocalizationer mclocalizationer;

  public MCLParameterAdjustGUI(MCLocalizationer _mclocalizationer) {
    super("robot ID:"
        + _mclocalizationer.getTargetRobot().getNetworkNode().getID());
    this.mclocalizationer = _mclocalizationer;

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
    gridBagLayout.rowHeights = new int[] { 35, 32, 0, 0 };
    gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0,
        Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
    getContentPane().setLayout(gridBagLayout);

    JLabel lblAlphaFast = new JLabel("Alpha fast");
    GridBagConstraints gbc_lblAlphaFast = new GridBagConstraints();
    gbc_lblAlphaFast.anchor = GridBagConstraints.EAST;
    gbc_lblAlphaFast.insets = new Insets(0, 0, 5, 5);
    gbc_lblAlphaFast.gridx = 0;
    gbc_lblAlphaFast.gridy = 0;
    getContentPane().add(lblAlphaFast, gbc_lblAlphaFast);

    final JSlider sliderFast = new JSlider();
    sliderFast.setMaximum(MAX_);
    sliderFast.setValue((int) (mclocalizationer.getAlphaFast() * sliderFast
        .getMaximum()));
    sliderFast.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        double fastValue = ((double) sliderFast.getValue())
            / ((double) sliderFast.getMaximum());
        mclocalizationer.setAlphaFast(fastValue);
        textFast.setText(Double.toString(fastValue));
      }
    });

    GridBagConstraints gbc_sliderFast = new GridBagConstraints();
    gbc_sliderFast.fill = GridBagConstraints.HORIZONTAL;
    gbc_sliderFast.weightx = 2.0;
    gbc_sliderFast.insets = new Insets(0, 0, 5, 5);
    gbc_sliderFast.gridx = 1;
    gbc_sliderFast.gridy = 0;
    getContentPane().add(sliderFast, gbc_sliderFast);

    textFast = new JTextField();
    textFast.setEditable(false);
    GridBagConstraints gbc_textFast = new GridBagConstraints();
    gbc_textFast.fill = GridBagConstraints.VERTICAL;
    gbc_textFast.insets = new Insets(0, 0, 5, 0);
    gbc_textFast.gridx = 2;
    gbc_textFast.gridy = 0;
    getContentPane().add(textFast, gbc_textFast);
    textFast.setColumns(5);
    textFast.setText(Double.toString( (double)(sliderFast.getValue())
        / ( (double) sliderFast.getMaximum() )));

    JLabel lblAlphaSlow = new JLabel("Alpha slow");
    GridBagConstraints gbc_lblAlphaSlow = new GridBagConstraints();
    gbc_lblAlphaSlow.anchor = GridBagConstraints.EAST;
    gbc_lblAlphaSlow.insets = new Insets(0, 0, 5, 5);
    gbc_lblAlphaSlow.gridx = 0;
    gbc_lblAlphaSlow.gridy = 1;
    getContentPane().add(lblAlphaSlow, gbc_lblAlphaSlow);

    final JSlider sliderSlow = new JSlider();
    sliderSlow.setMaximum(MAX_);
    sliderSlow.setValue((int) (mclocalizationer.getAlphaSlow() * sliderSlow
        .getMaximum()));
    sliderSlow.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        double slowValue = ((double) sliderSlow.getValue())
            / ((double) sliderSlow.getMaximum());
        mclocalizationer.setAlphaSlow(slowValue);
        textSlow.setText(Double.toString(slowValue));
      }
    });

    GridBagConstraints gbc_sliderSlow = new GridBagConstraints();
    gbc_sliderSlow.fill = GridBagConstraints.HORIZONTAL;
    gbc_sliderSlow.weightx = 2.0;
    gbc_sliderSlow.insets = new Insets(0, 0, 5, 5);
    gbc_sliderSlow.gridx = 1;
    gbc_sliderSlow.gridy = 1;
    getContentPane().add(sliderSlow, gbc_sliderSlow);

    textSlow = new JTextField();
    textSlow.setEditable(false);
    GridBagConstraints gbc_textSlow = new GridBagConstraints();
    gbc_textSlow.insets = new Insets(0, 0, 5, 0);
    gbc_textSlow.fill = GridBagConstraints.VERTICAL;
    gbc_textSlow.gridx = 2;
    gbc_textSlow.gridy = 1;
    getContentPane().add(textSlow, gbc_textSlow);
    textSlow.setColumns(5);
    textSlow.setText(Double.toString(((double) sliderSlow.getValue())
        / ((double) sliderSlow.getMaximum())));

    JButton btnStoreParam = new JButton("Store Param");
    btnStoreParam.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        try {
          mclocalizationer.storeParameter();
        } catch (IOException e) {
          JOptionPane.showMessageDialog(null, e.getMessage(),
              "Fail to Save",JOptionPane.ERROR_MESSAGE);
          e.printStackTrace();
        }
        
        
      }
    });
    GridBagConstraints gbc_btnStoreParam = new GridBagConstraints();
    gbc_btnStoreParam.insets = new Insets(0, 0, 0, 5);
    gbc_btnStoreParam.gridx = 1;
    gbc_btnStoreParam.gridy = 2;
    getContentPane().add(btnStoreParam, gbc_btnStoreParam);

    setResizable(false);
    this.pack();

  }

}
