import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;


public class Chip8Test {

    // Test 1: Set register V3 to 250
    @Test
    void testSetRegister() {
        Chip8 chip8 = new Chip8();

        chip8.handle0x6000((char) 0x63FA);

        assertEquals(250, chip8.getRegister(3));
    }

    // Test 2: Overwrite an existing register value
    @Test
    void testOverwriteRegister() {
        Chip8 chip8 = new Chip8();

        chip8.handle0x6000((char) 0x63FA);
        chip8.handle0x6000((char) 0x6305);

        assertEquals(5, chip8.getRegister(3));
    }

    // Test 3: Add 10 to a register containing 5
    @Test
    void testAddToRegister() {
        Chip8 chip8 = new Chip8();

        chip8.handle0x6000((char) 0x6305);
        chip8.handle0x7000((char) 0x730A);

        assertEquals(15, chip8.getRegister(3));
    }

    // Test 4: Check 8-bit overflow (250 + 10 = 4)
    @Test
    void testRegisterOverflow() {
        Chip8 chip8 = new Chip8();

        chip8.handle0x6000((char) 0x63FA);
        chip8.handle0x7000((char) 0x730A);

        assertEquals(4, chip8.getRegister(3));
    }

    // Test 5: Check maximum register value
    @Test
    void testMaximumRegisterValue() {
        Chip8 chip8 = new Chip8();

        chip8.handle0x6000((char) 0x60FF);

        assertEquals(255, chip8.getRegister(0));
    }

    // Test 6: Check overflow from 255 to 0
    @Test
    void testOverflowToZero() {
        Chip8 chip8 = new Chip8();

        chip8.handle0x6000((char) 0x60FF);
        chip8.handle0x7000((char) 0x7001);

        assertEquals(0, chip8.getRegister(0));
    }
}