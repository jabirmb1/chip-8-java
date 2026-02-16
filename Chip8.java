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

public class chip8{
    // chip 8 specifications 
    private static final int MEMORY_SIZE = 4096; // chip 8 has 4KB of memory
    int programCounter = 512; // program counter starts at 0x200 (512 in decimal)
    private static final int NUM_REGISTERS = 16; // chip 8 has 16 registers (V0 to VF)


    // memory setup
    // use array szie of 4096 to represent the memory of the chip 8
    private byte[] memoryArray = new byte[MEMORY_SIZE]; // 4KB of memory
}
