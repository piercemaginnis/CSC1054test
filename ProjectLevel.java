//CSC Project File-Reader
//By Pierce Maginnis
import java.util.*;
import java.io.*;

public class ProjectLevel {  
      
   private int xStart,yStart,numberOfRows,numberOfColumns;
   private int[] gameData = new int[4]; //string to hold the "next" data
   private int[][] levelData = new int[32][24]; //array to hold the "level" data
   
   //getter method for level x and y coords
   public int getLevelData(int x, int y) {
      return this.levelData[x][y];
   }
   
   //getter method for level data
   public int getNextData(int a) {
      return this.gameData[a];
   }
   
   //passes in the filename as a string through level
   ProjectLevel(String fileName) { 
      Scanner scan = null; //declare scanner outside try/catch
      
      try {
         scan = new Scanner(new File(fileName)); //reads in the passed through file with scanner
      } catch (FileNotFoundException fnfe) {}
      
      gameData[0] = scan.nextInt(); //puts the data in a 1D int array
      gameData[1] = scan.nextInt();
      String trash = scan.nextLine();
      gameData[2] = scan.nextInt();
      gameData[3] = scan.nextInt();
      trash = scan.nextLine();
      
      //fills a 9x9 array with the numbers in the file
      for (int i = 0; i < gameData[2]; i++) {
         for (int j = 0; j < gameData[3]; j++) {
            levelData[j][i] = scan.nextInt();
         }
      }
   }
}