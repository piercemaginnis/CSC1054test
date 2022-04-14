//CSC Project JPanel
//By Pierce Maginnis
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ProjectJPanel extends JPanel implements Runnable {
   ProjectLevel level = new ProjectLevel("level.txt");
   private int FPS = 100; //this variable holds the FPS, or how often the drawing refreshes per second
   int squareX = level.getNextData(0) * 25, squareY = level.getNextData(1) * 25, rows = level.getNextData(2) * 25, columns = level.getNextData(3) * 25; //the square X & Y are the left top corner of the cube. Updated continuously as game runs.
   public boolean up, down, left, right;
   
   
   private ArrayList<Integer> blueX = new ArrayList();
   private ArrayList<Integer> blueY = new ArrayList();
   private int winningBlockX,winningBlockY;
   private boolean isOnGround, jumping;
   private double jump, n;
   
   public ProjectJPanel() {
      addKeyListener(new KeyEventDemo()); //allows us to "listen" to the users key inputs
      setPreferredSize(new Dimension(800,600)); //sets the dimension of the game to 450x450
      setFocusable(true); //allows the panel to have "focus" of where the keyinputs go
   }
   
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      //Backgroud
      for (int i = 0; i < columns; i+=25) {
         for (int j = 0; j < rows; j+=25) {
            if (level.getLevelData(i/25,j/25) == 0)
               g.setColor(Color.GRAY);
            if (level.getLevelData(i/25,j/25) == 1) {
               g.setColor(Color.BLUE);
               blueX.add(i);
               blueY.add(j);
            }
            if (level.getLevelData(i/25,j/25) == 2) {
               g.setColor(Color.GREEN);
               winningBlockX = i;
               winningBlockY = j;
            }
            g.fillRect(i,j,25,25);
         }
      }
      g.setColor(Color.RED);
      g.fillRect(squareX,squareY,25,25);
   }
   
   public void collisionDetection() {
      for (int i = 0; i < blueY.size(); i++) { //Basically, this checks the cubes coordinates with the blue walls coordinates.
         //Winning Block
         if (squareX+25 > winningBlockX //if the coordinates are the same, the cube is not allowed to go through.
         && squareX < winningBlockX+25
         && squareY+25 > winningBlockY
         && squareY < winningBlockY+25) {
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame, "You Win!");
            System.exit(1);
         }
         //CEILING
         if (squareX+25 > blueX.get(i)
         && squareX < blueX.get(i)+25
         && squareY+25 > blueY.get(i)
         && squareY == blueY.get(i)+25) {
            up = false;
            jump = 0;
         }
         //RIGHT
         if (squareX+25 == blueX.get(i)
         && squareX < blueX.get(i)+25
         && squareY+25 > blueY.get(i)
         && squareY < blueY.get(i)+25) {
            right = false;
         }
         //LEFT
         if (squareX+25 > blueX.get(i)
         && squareX == blueX.get(i)+25
         && squareY+25 > blueY.get(i)
         && squareY < blueY.get(i)+25) {
            left = false;
         }
         //FLOOR
         if (squareX+25 > blueX.get(i)
         && squareX < blueX.get(i)+25
         && squareY+25 == blueY.get(i)
         && squareY < blueY.get(i)+25) {
            down = false;
            isOnGround = true; //once cube hits floor, it is on ground
            jumping = false; //once cube hits the floor, no longer jumping
            jump = 7; //set jump to 7 if cube is resting on ground and player presses W
            n = 1; //sets n to 1 if cube is resting on ground
         }
      }
   }
   public void update() {
      collisionDetection(); //prevents player from going through blue blocks      
      //JUMPING
      if (up == true && isOnGround == true) {
         isOnGround = false;
         jumping = true;
      }
      if (jumping == true) { //if jumping is true, every loop, subtract .1 from jumping (starts at 7).
         for (int i = 0; i < jump; i++) { //if jump var is 7, go up 7 times.
            squareY -= 1;
            collisionDetection();
         }
      }
      if (jumping == true && jump > 0)
         jump -= .1;
      //END JUMPING
      
      //GRAVITY
      if (isOnGround == false) {
         for (int i = 0; i < n; i++) {
            squareY += 1;//lower the square by 1 pixel n times
            collisionDetection(); //check for objects in the way
         }
      }
      if (isOnGround == false && n <= 7)
      n += 0.05; //this variable alters the gravity effect (lower num = less gravity)
      //END GRAVITY
      
      if (left == true) squareX--;
      if (right == true) squareX++;
      isOnGround = false; //isOnGround is always set to false. The game assumes the player is not on the ground, 
   }                      //until the player block is on top of a blue block, in which case isOnGround is set to true.
   
   public void run() {
      double drawInterval = 1000000000/FPS; //1b nanoseconds or 1 second / fps. to allow us to draw the screen *fps* times a second
      double nextDrawTime = System.nanoTime() + drawInterval;//sets the next draw time to the current time + the draw interval
      
      while (gameThread != null) {//gameThread will not be null as long as the program is running
         update();
         repaint();
         try {
            double remainingTime = nextDrawTime - System.nanoTime();
            remainingTime = remainingTime/1000000; //divided by 1 million to accept time in milliseconds
            if (remainingTime < 0) //if no time is left, the thread doesnt need to sleep
               remainingTime = 0;
            Thread.sleep((long) remainingTime);
            nextDrawTime += drawInterval; //e.g: if 60fps selected, 0.06 seconds later will be the next draw time
         } catch (InterruptedException e) {}
      }
   }
   
   Thread gameThread; //make a thread called gameThread
   public void startGameThread() { //method to start the thread
      gameThread = new Thread(this);
      gameThread.start(); //start the thread
   }
   public class KeyEventDemo implements KeyListener { //records W,A,S,D keyboard input
      public void keyTyped(KeyEvent e) {} //if W,A,S,D is pressed, set corresponding boolean to true.
      public void keyPressed(KeyEvent e) { //this is to prevent choppy movement from OS-Specific keyboard configurations.
         if(e.getKeyCode() == KeyEvent.VK_W) up = true;
         if(e.getKeyCode() == KeyEvent.VK_S) down = true;
         if(e.getKeyCode() == KeyEvent.VK_A) left = true;
         if(e.getKeyCode() == KeyEvent.VK_D) right = true;
      }
      public void keyReleased(KeyEvent e) { //if W,A,S,D is released, set corresponding boolean to false.
         if(e.getKeyCode() == KeyEvent.VK_W) up = false;
         if(e.getKeyCode() == KeyEvent.VK_S) down = false;
         if(e.getKeyCode() == KeyEvent.VK_A) left = false;
         if(e.getKeyCode() == KeyEvent.VK_D) right = false;
      }
   }
}
