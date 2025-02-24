package uk.ac.ncl.tom.properties;

import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;

/**
 * Class representing a property's code.
 *
 * @author Thomas Hague
 */

public final class PropertyCode {

    private final char prefix;
    private final String anuCode, strRep;
    private static final Map<String, PropertyCode> PROPERTYCODES = new HashMap<>();

    /**
     * Creates a property code with the specified parameters.
     *
     * @param prefix  either 'V' for Villa, or 'A' for objects
     * @param anuCode alphanumeric code, made up an arbitrary letter and two-digit number between 0-99.
     * @param strRep
     */
    private PropertyCode(char prefix, String anuCode, String strRep) {
        this.prefix = prefix;
        this.anuCode = anuCode;
        this.strRep = strRep;
    }

    /**
     * Static Factory method that returns a unique property code for the property type specified by the prefix.
     * If the generated property code already exists in the system, it will generate a new one until a unique property code  is
     * created that doesn't exist in the system. This new property code is then added to the property code Map.
     * The method creates the property code as specified by the prefix and generates the alphanumeric code.
     * Exception is thrown if the prefix is not 'V' or 'A'.
     *
     * @param prefix
     * @return
     */
    public static PropertyCode getInstance(char prefix) {
        if (prefix != 'V' && prefix != 'A') {
            throw new IllegalArgumentException("Invalid property prefix. " +
                    "Must be either 'V' for Villa, or 'A' for apartments.");
        }
        // set blank alphanumeric code and string representation of property code.
        String anuCode = "";
        String strRep = "";
        while (strRep.isEmpty() || PROPERTYCODES.containsKey(strRep)) {
            anuCode = generateANUCode();
            strRep = prefix + "-" + anuCode;
        }
        // when a unique property code has been generated, create property code object.
        PropertyCode pc = new PropertyCode(prefix, anuCode, strRep);
        PROPERTYCODES.put(strRep, pc);
        return pc;

    }

    /**
     * Called in the property code getInstance method, generates an arbitrary capital letter and two-digit number between 0-99.
     *
     * @return a String representing the alphanumeric code.
     */
    public static String generateANUCode() {
        Random random = new Random();
        char anucLetter = (char) ('A' + random.nextInt(26));
        String anucDigits = String.format("%02d", random.nextInt(100));
        return anucLetter + anucDigits;
    }

    /**
     * Returns the prefix.
     *
     * @return a char representing prefix.
     */
    public char getPrefix() {
        return prefix;
    }

    /**
     * Returns the alphanumeric code.
     *
     * @return a string representing the alphanumeric code.
     */
    public String getAnuCode() {
        return anuCode;
    }

    /**
     * Returns a Map of property codes.
     *
     * @return Map
     */
    public static Map<String, PropertyCode> getPropertyCodesMap() {
        return Collections.unmodifiableMap(PROPERTYCODES);
    }

    /**
     * Overrides the existing toString method, to specify how we like to view property codes objects.
     *
     * @return a String representing a property codes, made up of prefix and alphanumeric code.
     */
    @Override
    public String toString() {
        return strRep;
    }
}
