// version 1.0 of the chip8 emulator in java
// by: jabirmb1
/**************************************************
 * This is a simple implementation of the Chip-8 emulator in Java.
 * It includes basic functionality to load and execute Chip-8 programs.
 * The emulator uses a simple graphics system to display the output.
 **************************************************/

import java.io.FileInputStream; // For creating the window and handling graphics
import java.io.IOException; // For drawing the graphics
import java.io.InputStream; // For handling keyboard input
import java.util.Stack;
import javax.swing.JFrame;


public class Chip8{
    private static final int MEMORY_SIZE = 4096;
    static int programCounter = 512; //  0x200 (512 in decimal) - where next instruction is
    private static final int NUM_REGISTERS = 16;
    private static char  indeRegisterI; // where the sprite data is

    final int WIDTH =640;
    final int HEIGHT = 320;


    private static byte[] memoryArray = new byte[MEMORY_SIZE];
    private int[] registers = new int[NUM_REGISTERS]; // v0 to vf
    public static boolean[][] displayArray = new boolean[32][64];

    private Stack<Integer> stackForRegisters = new Stack<>();

    public int getRegister(int index) {
        return registers[index];
    }

    public int getIndexRegister() {
        return indeRegisterI;
    }
    

    public static void main(String[] args){
        Chip8 chip8 = new Chip8();

        chip8.createDisplay();
        
        

        while (true) {
            char opcodeFetched = chip8.fetchOpcode(); // fetch the next opcode from memory
            programCounter += 2; // move the program counter to the next instruction (each opcode is 2 bytes)

            // decode and execute the opcode
            // utilise switches
            // for now we will only implement a few opcodes to demonstrate the IBM logo program
            switch(opcodeFetched & 0xF000) {
                case 0x0000:
                    chip8.handle00E0(opcodeFetched); // handle opcodes that start with 0x0
                    break;
                case 0x1000:
                    // jump to new PC
                    chip8.handle0x1000(opcodeFetched);
                    break;
                case 0x6000:
                    // sets a register
                    chip8.handle0x6000(opcodeFetched);

                    break;
                case 0x7000:
                    // add value to register existing value
                    chip8.handle0x7000(opcodeFetched);

                    break;
                case 0xA000:
                    // sets register indeRegisterI
                    chip8.handle0xA000(opcodeFetched);
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
    // format:
    // X Y N NN NNN
    // X- lookup regisater Vx (0 to F)
    // Y- lookup register Vy (0 to F)
    // N- 4 bit number (0 to F)
    // NN- 8 bit number (0 to FF)
    // NNN- 12 bit number (0 to FFF) memory address
    public static char fetchOpcode(){
        char firstHalf = (char) (memoryArray[programCounter] & 0xFF); // fetch the first byte of the opcode
        char secondHalf = (char) (memoryArray[programCounter + 1] & 0xFF); // fetch the second byte of the opcode
        char opcode = (char) ((firstHalf << 8) | secondHalf); // combine the two bytes to form the full opcode
        return opcode; 
    }

    // clear the display
    public void handle00E0(char opcode) {
        for (int row = 0; row < 32; row++) {
            for (int col = 0; col < 64; col++) {
                displayArray[row][col] = false;
            }
        }
    }

    public void handle0x6000(char opcode) {
        int x2 = (opcode & 0x0F00) >> 8;
        int nn2 = opcode & 0x00FF;
        registers[x2] = nn2;
    }

    public void handle0x7000(char opcode) {
        int x3 = (opcode & 0x0F00) >> 8;
        int nn3 = opcode & 0x00FF;
        registers[x3] = (registers[x3] + nn3) & 0xFF; // handles overflow out of 8 bit
    }

    public void handle0xA000(char opcode){
        indeRegisterI = (char) (opcode & 0x0FFF);
    }

    public void handle0x1000(char opcode){
        programCounter = opcode & 0x0FFF;
    }

    public void handle0xD000(char opcode){
        int xValRegister = (opcode & 0x0F00) >> 8;
        int yValRegister = (opcode & 0x00F0) >> 4;
        int N = opcode & 0x000F;

        registers[15] = 0;

        int xCord = registers[xValRegister];
        int yCord = registers[yValRegister];

        for (int i = 0; i < N; i++){
            int cur8bit = memoryArray[indeRegisterI + i] & 0xFF;

            for (int j = 0; j< 8; j++){
                int currentBit = (cur8bit >> (7 - j)) & 0x01;
                if (currentBit == 1){
                    int pixelX = xCord + j;
                    int pixelY = yCord + i;

                    if (pixelX >= 64 || pixelY >= 32) {
                        continue;
                    }

                    if(displayArray[pixelY][pixelX]){
                        registers[15] = 1;
                    }

                    displayArray[pixelY][pixelX] = !displayArray[pixelY][pixelX];
                }
            }

        }

    }


    public void createDisplay() {
        JFrame frameGUI = new JFrame("CHIP 8 emulator");
        frameGUI.setSize(WIDTH,HEIGHT);
        displayArray[5][10] = true;
        displayArray[5][11] = true;

        Chip8Display chip8Display = new Chip8Display(displayArray);

        frameGUI.add(chip8Display);

        frameGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frameGUI.setVisible(true);


    }



}
