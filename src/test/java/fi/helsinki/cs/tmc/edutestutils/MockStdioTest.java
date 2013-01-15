package fi.helsinki.cs.tmc.edutestutils;

import org.junit.After;
import org.junit.Before;
import java.util.Scanner;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

public class MockStdioTest {

    @Rule
    public MockStdio io = new MockStdio();

    @Before
    public void setUp() {
        assertTrue(io.isEnabled());
    }

    @After
    public void tearDown() {
        assertTrue(io.isEnabled());
    }

    @Test
    public void capturesStdout() {
        System.out.println("hello");
        assertEquals("hello\n", io.getSysOut());
        assertTrue(io.getSysErr().isEmpty());
    }

    @Test
    public void capturesStderr() {
        System.err.println("hello");
        assertEquals("hello\n", io.getSysErr());
        assertTrue(io.getSysOut().isEmpty());
    }

    @Test
    public void allowsSettingStdin() {
        io.setSysIn("hello");
        assertEquals("hello", new Scanner(System.in).nextLine());
    }

    @Test
    public void convertsWindowsLineEndingsToUnix() {
        System.out.println("hello\r\nworld");
        System.err.println("world\r\nhello");
        assertEquals("hello\nworld\n", io.getSysOut());
        assertEquals("world\nhello\n", io.getSysErr());
    }

    @Test
    public void testNonAsciiCharacters() {
        System.out.println("Hähä");
        System.err.println("Höhö");
        assertEquals("Hähä\n", io.getSysOut());
        assertEquals("Höhö\n", io.getSysErr());
    }

    @Test
    public void inOutNonAsciiCharacterTest() {
        Scanner lukija = new Scanner(System.in);
        String s2 = "tietojenkäsittely";
        String s1 = "käsi";
        io.setSysIn(s1 + "\n" + s2 + "\n");
        String sana1 = lukija.nextLine();
        assertTrue("Sana luettiin scannerilla: " + sana1, "käsi".equals(sana1)); //Hajoo mulla
        String sana2 = lukija.nextLine();
        assertTrue("Sana luettiin scannerilla: " + sana1, s2.equals(sana2));
        System.out.println(sana1 + " -- " + sana2);
        String out = io.getSysOut().replaceAll("'", "").replaceAll("-", "").replaceAll("  ", " ");
        String[] sanat = out.split(" ");
        assertTrue("Sana käsi tulostui: " + sanat[0], "käsi".equals(sanat[0]));
        assertTrue("Sana " + s2 + ": " + sanat[1], s2.equals(sanat[1]));

    }
}
