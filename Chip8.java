// vwersion 1.0 of the chip8 emulator in java
// by: jabirmb1
/**************************************************
 * This is a simple implementation of the Chip-8 emulator in Java.
 * It includes basic functionality to load and execute Chip-8 programs.
 * The emulator uses a simple graphics system to display the output.
 **************************************************/

import javax.swing.*; // For creating the window and handling graphics
import java.awt.*; // For drawing the graphics
import java.awt.event.KeyEvent; // For handling keyboard input
import java.io.InputStream;
import java.io.FileInputStream; // For reading the Chip-8 program from a file
import java.io.IOException;

public class Chip8{
    // chip 8 specifications 
    private static final int MEMORY_SIZE = 4096; // chip 8 has 4KB of memory
    static int programCounter = 512; // program counter starts at 0x200 (512 in decimal)
    private static final int NUM_REGISTERS = 16; // chip 8 has 16 registers (V0 to VF)
    private static char  indeRegisterI; // index register points at locations in memory


    // memory setup
    // use array szie of 4096 to represent the memory of the chip 8
    private static byte[] memoryArray = new byte[MEMORY_SIZE]; // 4KB of memory

    // stack setup for subroutine calls
    private char[] stack = new char[16]; // chip 8 has a stack of 16 levels

    // main- start the emulator
    public static void main(String[] args){
        Chip8 chip8 = new Chip8(); // create an intsnace 
        // load a chip 8 program into memory (for example, you can load a game or a test program)
        chip8.loadProgram(); // replace with actual path to your chip 8 program
        // start the emulation loop 
        while(true){
            chip8.emulateCycle();
        }
    }

    // load program into memory
    public static void loadProgram(){
        // code to read the chip 8 program from the specified file path and load it into memory starting at 0x200
        // you can use FileInputStream or any other method to read the file and store it in the memory array
        String filePath = "C:\\Users\\jabir\\dev\\IBMlogo.ch8"; 

        try ( InputStream inputFile = new FileInputStream(filePath);){
            int bytesRead;
            
            // read the file and load it into memory starting at 0x200
            while ((bytesRead = inputFile.read()) != -1) {
                Chip8.addToMemory(programCounter, (byte) bytesRead); // add the byte read from the file to memory at the current program counter
                programCounter++; // move to the next memory location
            }

        } catch (IOException e) {
            System.out.println("Error loading program: " + e.getMessage());
        }
    }

    public static void addToMemory(int address, byte value) {
        if (address >= 0 && address < MEMORY_SIZE) {
            memoryArray[address] = value; // add value to memory at the specified address
        } else {
            System.out.println("Memory address out of bounds: " + address);
        }
    }
}
