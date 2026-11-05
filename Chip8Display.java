import javax.swing.*;
import java.awt.*;

public class Chip8Display extends JPanel{
    private final boolean[][] displayArray;

    //constructor
    public Chip8Display(boolean[][] displayArrayPassed){
        this.displayArray = displayArrayPassed;
        setBackground(Color.BLACK);
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.WHITE);

        for (int row = 0; row < 32; row++) {
            for (int col = 0; col < 64; col++) {
                if (displayArray[row][col] == true){
                    g.fillRect(col * 10, row * 10, 10, 10);
                }
            }
        }
    }
}