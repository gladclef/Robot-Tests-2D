/**
 * 
 */
package main;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jbox2d.testbed.framework.TestbedController;
import org.jbox2d.testbed.framework.TestbedErrorHandler;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedController.MouseBehavior;
import org.jbox2d.testbed.framework.TestbedController.UpdateBehavior;
import org.jbox2d.testbed.framework.j2d.DebugDrawJ2D;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;
import org.jbox2d.testbed.framework.j2d.TestbedMain;
import org.jbox2d.testbed.framework.j2d.TestbedSidePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gladc_000
 *
 */
public class Main {
  
  /** For logging errors specific to this class. */
  private static final Logger log = LoggerFactory.getLogger(TestbedMain.class);

  /**
   * Copied literally character-for-character from JBox2D testbed TestbedMain.java class.
   * 
   * @param args
   */
  public static void main(String[] args) {
    
    // interface looks pretty
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (Exception e) {
      log.warn("Could not set the look and feel to nimbus.  "
          + "Hopefully you're on a mac so the window isn't ugly as crap.");
    }
    
    // set up the model
    TestbedModel model = new TestbedModel();
    final TestbedController controller =
        new TestbedController(model, UpdateBehavior.UPDATE_CALLED, MouseBehavior.NORMAL,
            new TestbedErrorHandler() {
              @Override
              public void serializationError(Exception e, String message) {
                JOptionPane.showMessageDialog(null, message, "Serialization Error",
                    JOptionPane.ERROR_MESSAGE);
              }
            });
    TestPanelJ2D panel = new TestPanelJ2D(model, controller);
    model.setPanel(panel);
    model.setDebugDraw(new DebugDrawJ2D(panel, true));
    
    // add the tests
    MyTestList.populateModel(model);

    // add the GUI components
    JFrame testbed = new JFrame();
    testbed.setTitle("JBox2D Testbed");
    testbed.setLayout(new BorderLayout());
    TestbedSidePanel side = new TestbedSidePanel(model, controller);
    testbed.add((Component) panel, "Center");
    testbed.add(new JScrollPane(side), "East");
    testbed.pack();
    testbed.setVisible(true);
    testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // start the tests
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        controller.playTest(0);
        controller.start();
      }
    });
  }

}
