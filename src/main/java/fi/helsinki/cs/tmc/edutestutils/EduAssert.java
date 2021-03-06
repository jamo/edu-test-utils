package fi.helsinki.cs.tmc.edutestutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Assert;

/**
 * Import static this instead of {@code org.junit.Assert.*} to get more assert methods.
 */
public class EduAssert extends Assert {
    /**
     * A regex that matches numbers with or without a decimal point or comma and a plus/minus sign.
     * 
     * <p>
     * It does not, however, tolerate spaces in between digits.
     */
    public static final Pattern tolerantNumberPattern = Pattern.compile("[+-]?(?:[0-9]+)(?:[.,][0-9]+)?");
    
    /**
     * Asserts that a string matches a regexp <em>entirely</em>.
     * 
     * <p>
     * The entire string must match the regexp.
     * Partial matches are not accepted, so make your regexp tolerant enough
     * at the edges if applicable.
     * 
     * @param message The failure message.
     * @param regex The regex to match against (see {@link Pattern}).
     * @param actual The actual string to match.
     */
    public static void assertMatches(String message, String regex, String actual) {
        if (!Pattern.matches(regex, actual)) {
            fail(message);
        }
    }
    
    /**
     * Asserts that a string matches a regexp <em>entirely</em>.
     * 
     * <p>
     * The entire string must match the regexp.
     * Partial matches are not accepted, so make your regexp tolerant enough
     * at the edges if applicable.
     * 
     * <p>
     * The default failure message is <code>`"&lt;actual&gt;" does not have the required form.`</code>
     * 
     * @param regex The regex to match against (see {@link Pattern}).
     * @param actual The actual string to match.
     */
    public static void assertMatches(String regex, String actual) {
        assertMatches("\"" + actual + "\" does not have the required form.", regex, actual);
    }
    
    /**
     * Asserts that a string contains a number close to (diff &lt; 0.000001) the given double.
     * 
     * <p>
     * {@link #tolerantNumberPattern} defines what substrings are considered numbers.
     * 
     * @param number The expected number.
     * @param actual The string that should contain the number.
     */
    public static void assertContainsNumber(double number, String actual) {
        assertContainsNumber("Expected to find the number " + number + " in `" + actual + "`", number, actual);
    }
    
    /**
     * Asserts that a string contains a number close to (diff &lt; 0.000001) the given double.
     * 
     * <p>
     * {@link #tolerantNumberPattern} defines what substrings are considered numbers.
     * 
     * @param message The failure message.
     * @param number The expected number.
     * @param actual The string that should contain the number.
     */
    public static void assertContainsNumber(String message, double number, String actual) {
        if (!containsNumber(number, actual)) {
            fail(message);
        }
    }
    
    /**
     * Checks whether a string contains a number close to (diff &lt; 0.000001) the given double.
     * 
     * <p>
     * {@link #tolerantNumberPattern} defines what substrings are considered numbers.
     * 
     * @param number The expected number.
     * @param actual The string that should contain the number.
     * @return Whether the string contains a match for {@link #tolerantNumberPattern} that is close enough to {@code number}.
     */
    public static boolean containsNumber(double number, String actual) {
        Matcher matcher = tolerantNumberPattern.matcher(actual);
        while (matcher.find()) {
            String text = matcher.group();
            text = text.replace(',', '.');
            double value = Double.parseDouble(text);
            if (Math.abs(value - number) < 0.000001) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Asserts two strings equal ignoring consecutive whitespace.
     * 
     * <p>
     * Precisely, this method compares the strings after applying
     * {@link #collapseWhitespace(java.lang.String)} to both.
     */
    public static void assertEqualsIgnoreSpaces(String message, String expected, String actual) {
        expected = collapseWhitespace(expected);
        actual = collapseWhitespace(actual);
        assertEquals(message, expected, actual);
    }
    
    /**
     * Asserts two strings equal ignoring consecutive whitespace.
     * 
     * <p>
     * Precisely, this method compares the strings after applying
     * {@link #collapseWhitespace(java.lang.String)} to both.
     */
    public static void assertEqualsIgnoreSpaces(String expected, String actual) {
        expected = collapseWhitespace(expected);
        actual = collapseWhitespace(actual);
        assertEquals(expected, actual);
    }

    /**
     * Collapses consecutive spaces and tabs into one space and removes extra "\r" characters.
     * 
     * <p>
     * The string is first trimmed of spaces and newlines in its beginning and end.
     * Then all carriage return characters ("\r") are stripped, which converts
     * newlines to Unix format. Finally all spaces and tabs are compressed into one
     * and spaces around newlines are removed.
     */
    public static String collapseWhitespace(String s) {
        // Trim away whitespace and newlines that don't matter
        s = s.trim();
        // Get rid of Windows newlines.
        s = s.replace("\r", "");
        // Collapse spaces and tabs into one
        s = s.replaceAll("[ \\t]+", " ");
        // Collapse spaces around newlines
        s = s.replace(" \n", "\n");
        s = s.replace("\n ", "\n");
        
        return s;
    }
}
