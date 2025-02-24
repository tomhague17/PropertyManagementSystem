package uk.ac.ncl.tom.testing;

import uk.ac.ncl.tom.tenants.*;

public class NameTest {
    public static void main(String[] args) {
        NameTest nameTest = new NameTest();
        System.out.println("Test create name");
        nameTest.createName();
        System.out.println("Test get name info");
        nameTest.getNameInfo();
        System.out.println("Test toString");
        nameTest.toStringTest();
        System.out.println("Test equals");
        nameTest.testEquals();
        System.out.println("Test hashCode");
        nameTest.testHashCode();
        System.out.println("Test valueOf");
        nameTest.testValueOf();
    }

    private void createName() {
        // test normal case
        Name name = new Name("Thomas", "Hague");
        Assertions.assertNotNull(name);

        //test exception case
        try {
            final Name name1 = new Name(null, null);
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }

        // test exception case
        try {
            final Name name1 = new Name("Thomas", null);
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }
        // test exception case
        try {
            final Name name1 = new Name("", "Hague");
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }
    }

    private void getNameInfo() {
        // test normal case
        String firstName = "Thomas";
        String lastName = "Hague";
        Name name = new Name("Thomas", "Hague");
        Assertions.assertEquals(firstName, name.getFirstName());
        Assertions.assertEquals(lastName, name.getLastName());
    }

    private void toStringTest() {
        Name name = new Name("Thomas", "Hague");
        // test normal case
        Assertions.assertEquals("Thomas Hague", name.toString());
        // test boundary case
        Assertions.assertNotEquals("Thoma Hagu", name.toString());
    }

    private void testEquals() {
        Name name = new Name("Thomas", "Hague");
        // test normal case
        Assertions.assertTrue(name.equals(name));
        // test error case
        Assertions.assertFalse(name.equals(null));
        // test error case
        Assertions.assertFalse(name.equals("Thomas"));
        // test normal case
        Assertions.assertTrue(name.equals(new Name("Thomas", "Hague")));
        // test error case
        Assertions.assertFalse(name.equals(new Name("Thomas", "Hawk")));
        // test error case
        Assertions.assertFalse(name.equals(new Name("Sophie", "Radford")));
        // test error case
        Assertions.assertFalse(name.equals(new Name("tHOMAS", "hAGUE")));
    }

    private void testHashCode() {
        Name name = new Name("Thomas", "Hague");
        Name name2 = new Name("Sophie", "Radford");
        Name nameCopy = new Name("Thomas", "Hague");
        // test normal case
        Assertions.assertEquals(name.hashCode(), nameCopy.hashCode());
        // test error case: two names are not equal
        Assertions.assertNotEquals(name.hashCode(), name2.hashCode());

    }

    //test valueOf?
    private void testValueOf() {
        Name name = Name.valueOf("Thomas Hague");
        // test error case
        Assertions.assertNotNull(name);
        // test normal case
        Assertions.assertTrue("Thomas".equals(name.getFirstName()));
        // test normal case
        Assertions.assertTrue("Hague".equals(name.getLastName()));
        // test exception case: name is null
        try {
            Name.valueOf(null);
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }
        // test exception case: name is blank
        try {
            Name.valueOf("");
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }
        // test exception case: name has 4 components
        try {
            Name.valueOf("Sophie Radford Thomas Hague");
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }
    }
}
