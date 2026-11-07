import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;


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

    // Test 7: Set index register I to address 0x250
    @Test
    void testSetIndexRegister() {
        Chip8 chip8 = new Chip8();

        chip8.handle0xA000((char) 0xA250);

        assertEquals(0x250, chip8.getIndexRegister());
    }

    // Test 9: Draw a sprite onto an empty screen
    @Test
    void testDrawSprite() {
        Chip8 chip8 = new Chip8();

        // Sprite: 11110000
        chip8.addToMemory(0x300, (byte) 0xF0);

        // I = 0x300
        chip8.handle0xA000((char) 0xA300);

        // V1 = 10 (X), V2 = 5 (Y)
        chip8.handle0x6000((char) 0x610A);
        chip8.handle0x6000((char) 0x6205);

        // Draw one sprite row
        chip8.handle0xD000((char) 0xD121);

        assertTrue(chip8.getPixel(5, 10));
        assertTrue(chip8.getPixel(5, 11));
        assertTrue(chip8.getPixel(5, 12));
        assertTrue(chip8.getPixel(5, 13));
        assertFalse(chip8.getPixel(5, 14));

        // No collision
        assertEquals(0, chip8.getRegister(15));
    }

    // Test 10: Drawing the same sprite twice clears it
    @Test
    void testSpriteCollision() {
        Chip8 chip8 = new Chip8();

        chip8.addToMemory(0x300, (byte) 0xF0);

        chip8.handle0xA000((char) 0xA300);
        chip8.handle0x6000((char) 0x610A);
        chip8.handle0x6000((char) 0x6205);

        // First draw
        chip8.handle0xD000((char) 0xD121);

        assertEquals(0, chip8.getRegister(15));

        // Second draw at the same location
        chip8.handle0xD000((char) 0xD121);

        // Pixels should be switched OFF
        assertFalse(chip8.getPixel(5, 10));
        assertFalse(chip8.getPixel(5, 11));
        assertFalse(chip8.getPixel(5, 12));
        assertFalse(chip8.getPixel(5, 13));

        // Collision occurred
        assertEquals(1, chip8.getRegister(15));
    }

    // Test 11: Draw sprite at right edge of display
    @Test
    void testSpriteClipping() {
        Chip8 chip8 = new Chip8();

        chip8.addToMemory(0x300, (byte) 0xFF);

        chip8.handle0xA000((char) 0xA300);

        // V1 = 62, V2 = 10
        chip8.handle0x6000((char) 0x613E);
        chip8.handle0x6000((char) 0x620A);

        chip8.handle0xD000((char) 0xD121);

        // Only columns 62 and 63 should be drawn
        assertTrue(chip8.getPixel(10, 62));
        assertTrue(chip8.getPixel(10, 63));

        assertEquals(0, chip8.getRegister(15));
    }

    // Test 12: Drawing a blank sprite changes nothing
    @Test
    void testBlankSprite() {
        Chip8 chip8 = new Chip8();

        chip8.addToMemory(0x300, (byte) 0x00);

        chip8.handle0xA000((char) 0xA300);

        chip8.handle0x6000((char) 0x610A);
        chip8.handle0x6000((char) 0x6205);

        chip8.handle0xD000((char) 0xD121);

        assertFalse(chip8.getPixel(5, 10));
        assertFalse(chip8.getPixel(5, 11));

        assertEquals(0, chip8.getRegister(15));
    }
    @Test
    void testIndependentEmulatorInstances() {
        Chip8 emulator1 = new Chip8();
        Chip8 emulator2 = new Chip8();

        emulator1.handle0x6000((char) 0x63FF);

        assertEquals(255, emulator1.getRegister(3));
        assertEquals(0, emulator2.getRegister(3));

        emulator1.handle0xA000((char) 0xA300);

        assertEquals(0x300, emulator1.getIndexRegister());
        assertEquals(0, emulator2.getIndexRegister());
    }
}