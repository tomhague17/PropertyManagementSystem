package uk.ac.ncl.tom.testing;

import uk.ac.ncl.tom.properties.PropertyCode;

import java.util.HashSet;
import java.util.Set;

public class PropertyCodeTest {
    public static void main(String[] args) {
        PropertyCodeTest pcTest = new PropertyCodeTest();
        System.out.println("Test create Property code");
        pcTest.createPropertyCode();
        System.out.println("Test randomness and uniqueness of Property codes");
        pcTest.testRandomUniqueCodes();
        System.out.println("Test generating property code format");
        pcTest.testGenerateANUCode();
        System.out.println("Test get Property code info");
        pcTest.getPCInfo();
        System.out.println("Test get Property codes HashMap");
        pcTest.testGetPropertiesHashMap();
        System.out.println("Test toString");
        pcTest.testToString();
    }

    private void createPropertyCode() {
        // test normal case
        PropertyCode pc = PropertyCode.getInstance('V');
        String expectedStrRep = pc.toString();
        // test normal case: check property code isn't null
        Assertions.assertNotNull(pc);
        // test normal case: prefix of property code equals 'V'
        Assertions.assertEquals('V', pc.getPrefix());
        // test normal case: alphanumeric component of property code isn't null
        Assertions.assertNotNull(pc.getAnuCode());
        // test normal case: expected String representation of property code does contain the generated alphanumeric code.
        Assertions.assertTrue(expectedStrRep.contains(pc.getAnuCode()));
        // test normal case: expected String representation of property code is now a key in the properties map
        Assertions.assertTrue(PropertyCode.getPropertyCodesMap().containsKey(expectedStrRep));
        // test normal case: property code is the value of the properties map, with the expected string representation
        // as the corresponding key
        Assertions.assertEquals(pc, PropertyCode.getPropertyCodesMap().get(expectedStrRep));
        // test error case
        Assertions.assertNotEquals('A', pc.getPrefix());
        // exception case: Test invalid prefix
        try {
            PropertyCode pcNo = PropertyCode.getInstance('T');
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }
    }

    private void testRandomUniqueCodes() {
        PropertyCode pc1 = PropertyCode.getInstance('V');
        PropertyCode pc2 = PropertyCode.getInstance('V');
        // test normal case: both generated codes are different
        Assertions.assertNotEquals(pc1, pc2);
        Assertions.assertNotEquals(pc1.toString(), pc2.toString());

        // test complicated normal case: all 500 generated property codes are unique
        Set<String> generatedCodes = new HashSet<>();
        for (int i = 0; i < 500; i++) {
            PropertyCode pc = PropertyCode.getInstance('V');
            Assertions.assertFalse(generatedCodes.contains(pc.toString()));
            generatedCodes.add(pc.toString());
        }
        Assertions.assertEquals(500, generatedCodes.size());
    }

    private void testGenerateANUCode() {
        for (int i = 0; i < 500; i++) {
            // test normal case, of 500 generated alphanumeric codes, they all consist of one letter, followed by two digits.
            String anuCode = PropertyCode.generateANUCode();
            Assertions.assertNotNull(anuCode);
            Assertions.assertTrue(Character.isLetter(anuCode.charAt(0)));
            Assertions.assertTrue(Character.isDigit(anuCode.charAt(1)));
            Assertions.assertTrue(Character.isDigit(anuCode.charAt(2)));
            // test normal case: alphanumeric codes are 3 characters in length
            Assertions.assertEquals(3, anuCode.length());
            // boundary case: alphanumeric codes are not 4 characters in length
            Assertions.assertNotEquals(4, anuCode.length());
            // error case:
            Assertions.assertNotEquals(-3, anuCode.length());
            Assertions.assertNotEquals(0, anuCode.length());
        }
    }

    private void getPCInfo() {
        char prefix = 'A';
        // test normal case
        PropertyCode pc = PropertyCode.getInstance(prefix);
        Assertions.assertNotNull(pc.getPrefix());
        Assertions.assertEquals('A', pc.getPrefix());
        Assertions.assertNotNull(pc.getAnuCode());
    }

    private void testGetPropertiesHashMap() {
        // test normal case
        PropertyCode pc = PropertyCode.getInstance('V');
        Assertions.assertTrue(PropertyCode.getPropertyCodesMap().containsKey(pc.toString()));
        // test error case:
        Assertions.assertFalse(PropertyCode.getPropertyCodesMap().isEmpty());
    }

    private void testToString() {
        // test normal case
        PropertyCode pc = PropertyCode.getInstance('A');
        String expectedSTRRep = "A" + "-" + pc.getAnuCode();
        Assertions.assertTrue(pc.toString().equals(expectedSTRRep));
    }
}
