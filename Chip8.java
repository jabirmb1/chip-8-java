// vwersion 1.0 of the chip8 emulator in java
// by: jabirmb1
/**************************************************
 * This is a simple implementation of the Chip-8 emulator in Java.
 * It includes basic functionality to load and execute Chip-8 programs.
 * The emulator uses a simple graphics system to display the output.
 **************************************************/

import java.io.FileInputStream; // For creating the window and handling graphics
import java.io.IOException; // For drawing the graphics
import java.io.InputStream; // For handling keyboard input

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
        
        // execute program
        // fetch decode and execute cycle
        while (true) { 
            char opcodeFetched = chip8.festchOpcode(); // fetch the next opcode from memory
            programCounter += 2; // move the program counter to the next instruction (each opcode is 2 bytes)

            // decode and execute the opcode
            // utilise switches
            switch(opcodeFetched & 0xF000) {
                case 0x0000:
                    chip8.handle00E0(opcodeFetched); // handle opcodes that start with 0x0
                    break;
                case 0x1000:
                    // handle opcodes that start with 0x1
                    break;
                case 0x6000:
                    // handle opcodes that start with 0x2
                    break;
                case 0x7000:
                    // handle opcodes that start with 0x3
                    break;
                case 0xA000:
                    // handle opcodes that start with 0x4
                    break;
                case 0xD000:
                    // handle opcodes that start with 0x5
                    break;
                
                // add more cases for other opcode categories as needed
                default:
                    System.out.println("Unknown opcode: " + Integer.toHexString(opcodeFetched));
            }
        }
        
    }

    // load program into memory
    public static void loadProgram(){
        // code to read the chip 8 program from the specified file path and load it into memory starting at 0x200
        // you can use FileInputStream or any other method to read the file and store it in the memory array
        String filePath = "C:\\Users\\jabir\\dev\\IBMlogo.ch8"; 

        try ( InputStream inputFile = new FileInputStream(filePath);){
            int bytesRead;
            int pointerInToMemory = programCounter; // start loading at 0x200 (512 in decimal)
            
            // read the file and load it into memory starting at 0x200 
            while ((bytesRead = inputFile.read()) != -1) {
                Chip8.addToMemory(pointerInToMemory, (byte) bytesRead); // add the byte read from the file to memory at the current program counter size of bytes is 1 byte (8 bits)
                pointerInToMemory++; // move to the next memory location
            }

        } catch (IOException e) {
            System.out.println("Error loading program: " + e.getMessage());
        }
    }

    // add to memory protected method
    public static void addToMemory(int address, byte value) {
        if (address >= 0 && address < MEMORY_SIZE) {
            memoryArray[address] = value; // add value to memory at the specified address
        } else {
            System.out.println("Memory address out of bounds: " + address);
        }
    }

    // fetch opcode from memory
    public static char festchOpcode(){
        char firstHalf = (char) (memoryArray[programCounter] & 0xFF); // fetch the first byte of the opcode
        char secondHalf = (char) (memoryArray[programCounter + 1] & 0xFF); // fetch the second byte of the opcode
        char opcode = (char) ((firstHalf << 8) | secondHalf); // combine the two bytes to form the full opcode
        return opcode; 
    }

    // handle 00E0 opcode (clear the display)
    public void handle00E0(char opcode) {
        return;
    }

}
