//CSC Project JFrame
//By Pierce Maginnis
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class ProjectJFrame extends JFrame {
   public ProjectJFrame() {
      super("CSC Project Game");
      Container contents = getContentPane();
      contents.setLayout(new FlowLayout());
      ProjectJPanel panel = new ProjectJPanel();
      panel.startGameThread();
      contents.add(panel);
      setSize(820,645); //sets size to slightly more than game so it doesn't go out of bounds
      contents.setBackground(Color.BLACK);
      setLocationRelativeTo(null); //puts panel to middle of the screen
      setVisible(true); //makes panel visible
      panel.requestFocus(); //sets what panel starts with the focus. In this example this line is not necessary but may be in the future
   }
   public static void main(String[] args) {
      ProjectJFrame frame = new ProjectJFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closes when pressing x on computer window
   }
}