package uk.ac.ncl.tom.testing;

import uk.ac.ncl.tom.properties.*;
import uk.ac.ncl.tom.management.PropertyManager;
import uk.ac.ncl.tom.tenants.TenantRecord;

import java.util.Calendar;
import java.util.Date;

public class PropertyManagerTest {
    String villaPropertyType = "Villa";
    String apartmentPropertyType = "Apartment";
    String mansionPropertyType = "Mansion";
    String nullPropertyType = null;

    public static void main(String[] args) {
        PropertyManagerTest propertyManagerTest = new PropertyManagerTest();
        System.out.println("Test add property");
        propertyManagerTest.testAddProperty();
        System.out.println("Test number of available properties method");
        propertyManagerTest.testNumberOfAvailableProperties();
        System.out.println("Test add tenant");
        propertyManagerTest.testAddTenant();
        System.out.println("Test issue rental contract");
        propertyManagerTest.testIssueRentalContract();
        System.out.println("Test terminate rental contract");
        propertyManagerTest.testTerminateRentalContract();
        System.out.println("Test get properties terminating soon");
        propertyManagerTest.testGetPropertiesTerminatingSoon();
    }

    private void testAddProperty() {
        // test normal case
        Property pV = PropertyManager.getInstance().addProperty(villaPropertyType);
        Assertions.assertNotNull(pV);
        Assertions.assertNotNull(PropertyManager.getInstance().getProperties());
        Property pA = PropertyManager.getInstance().addProperty(apartmentPropertyType);
        Assertions.assertNotNull(pA);
        Assertions.assertNotNull(PropertyManager.getInstance().getProperties());
        // test normal case: two properties created and added to properties map.
        Assertions.assertEquals(2, PropertyManager.getInstance().getProperties().size());
        Property pV2 = PropertyManager.getInstance().addProperty(villaPropertyType);
        // test normal case: properties added are unique
        Assertions.assertNotEquals(pV, pV2);
        // test exception case: invalid property type
        try {
            Property p1 = PropertyManager.getInstance().addProperty(mansionPropertyType);
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }
        // test exception case: null property type
        try {
            Property p2 = PropertyManager.getInstance().addProperty(nullPropertyType);
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }
    }

    private void testNumberOfAvailableProperties() {
        Property pA = PropertyManager.getInstance().addProperty(apartmentPropertyType);
        Property pA1 = PropertyManager.getInstance().addProperty(apartmentPropertyType);
        Property pA2 = PropertyManager.getInstance().addProperty(apartmentPropertyType);
        Property pA3 = PropertyManager.getInstance().addProperty(apartmentPropertyType);
        Property pA4 = PropertyManager.getInstance().addProperty(apartmentPropertyType);
        // test normal case
        PropertyManager.getInstance().noOfAvailableProperties("Villa");
        Assertions.assertEquals(2, PropertyManager.getInstance().noOfAvailableProperties(villaPropertyType));
        // test boundary case
        Assertions.assertNotEquals(3, PropertyManager.getInstance().noOfAvailableProperties(villaPropertyType));
        // test error case
        Assertions.assertNotEquals(-2, PropertyManager.getInstance().noOfAvailableProperties(villaPropertyType));
        // test normal case
        Assertions.assertEquals(6, PropertyManager.getInstance().noOfAvailableProperties(apartmentPropertyType));
        // test boundary case
        Assertions.assertNotEquals(5, PropertyManager.getInstance().noOfAvailableProperties(apartmentPropertyType));
        // test error case
        Assertions.assertNotEquals(0, PropertyManager.getInstance().noOfAvailableProperties(apartmentPropertyType));
        // test exception case: invalid property type
        try {
            Assertions.assertEquals(4, PropertyManager.getInstance().noOfAvailableProperties(mansionPropertyType));
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }
        // test exception case: null property type
        try {
            Assertions.assertEquals(4, PropertyManager.getInstance().noOfAvailableProperties(nullPropertyType));
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }
    }

    private void testAddTenant() {
        Date dob = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(1995, Calendar.JUNE, 8);
        dob = cal.getTime();
        // test normal case
        TenantRecord newTr = PropertyManager.getInstance().addTenantRecord("Thomas", "Hague", dob, true);
        cal.set(2000, Calendar.FEBRUARY, 26);
        Date dob2 = new Date();
        dob2 = cal.getTime();
        TenantRecord newTR2 = PropertyManager.getInstance().addTenantRecord("Sophie", "Radford", dob2, false);
        // test normal case: tenants Map is not null and now holds 2 tenants
        Assertions.assertNotNull(PropertyManager.getInstance().getTenants());
        Assertions.assertEquals(2, PropertyManager.getInstance().getTenants().size());
        // test exception case: full name is null
        try {
            TenantRecord newTR3 = PropertyManager.getInstance().addTenantRecord(null, null, dob2, true);
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }
        // test exception case: last name is null
        try {
            TenantRecord newTR3 = PropertyManager.getInstance().addTenantRecord("Thomas", null, dob2, true);
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }
        // test exception case: dob is null
        try {
            TenantRecord newTR3 = PropertyManager.getInstance().addTenantRecord("Thomas", "Hague", null, true);
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }
        // test exception case: trying to add tenant that already is on the system, with same name and dob
        try {
            TenantRecord newTR4 = PropertyManager.getInstance().addTenantRecord("Thomas", "Hague", dob, true);
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }
    }

    private void testIssueRentalContract() {
        // Cases also test validTennant, calcTenantAge, getAvailableProperty, assignProperty, calcTerminationDate methods,
        // that are called directly or indirectly in issueRentalContract.
        Date dob = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(1995, Calendar.JUNE, 8);
        dob = cal.getTime();
        TenantRecord tR = PropertyManager.getInstance().addTenantRecord("Thomas", "Hague", dob, true);
        // test normal case: issue contract and add record to tenants properties Map.
        PropertyManager.getInstance().issueRentalContract(tR, villaPropertyType, 20);
        Assertions.assertNotNull(PropertyManager.getInstance().getTenantsProperties());
        Assertions.assertEquals(1, PropertyManager.getInstance().getTenantsProperties().size());
        // test error case: tenant can only rent one property of all types at one time
        PropertyManager.getInstance().issueRentalContract(tR, apartmentPropertyType, 40);
        // test error case: tenant needs to premium class to rent a villa.
        cal.set(2000, Calendar.FEBRUARY, 26);
        Date dob2 = cal.getTime();
        TenantRecord tR2 = PropertyManager.getInstance().addTenantRecord("Sophie", "Radford", dob2, false);
        PropertyManager.getInstance().issueRentalContract(tR2, villaPropertyType, 20);
        // test normal case
        TenantRecord tR3 = PropertyManager.getInstance().addTenantRecord("Jessie", "Hague", dob2, true);
        PropertyManager.getInstance().issueRentalContract(tR3, villaPropertyType, 20);
        // test exception case: null property type
        try {
            PropertyManager.getInstance().issueRentalContract(tR3, null, 50);
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }
        // test exception case: invalid property type
        try {
            PropertyManager.getInstance().issueRentalContract(tR3, mansionPropertyType, 50);
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }
        // test error case: tenant needs to be 18 to rent an apartment
        cal.set(2010, Calendar.FEBRUARY, 26);
        Date dob3 = cal.getTime();
        TenantRecord tR4 = PropertyManager.getInstance().addTenantRecord("Gary", "Brown", dob3, true);
        PropertyManager.getInstance().issueRentalContract(tR4, apartmentPropertyType, 20);
        Property pV = PropertyManager.getInstance().addProperty(villaPropertyType);
        // test error case: tenant needs to be 21 to rent a Villa
        PropertyManager.getInstance().issueRentalContract(tR4, villaPropertyType, 20);
        PropertyManager.getInstance().issueRentalContract(tR2, villaPropertyType, 20);
        // test error case: only villa's not currently rented have dirty pools, so are unavailable for rent.
        if (pV instanceof Villa) {
            Villa v = (Villa) pV;
            v.setCleanPool(false);
        }
        PropertyManager.getInstance().issueRentalContract(tR3, villaPropertyType, 20);
        PropertyManager.getInstance().terminateRental(tR);
    }

    private void testTerminateRentalContract() {
        Date dob = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(1995, Calendar.JUNE, 8);
        dob = cal.getTime();
        TenantRecord tR = PropertyManager.getInstance().addTenantRecord("Thomas", "Hague", dob, true);
        // test exception case: tenant doesn't have a current rental contract to terminate
        try {
            PropertyManager.getInstance().terminateRental(tR);
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }
        // test exception case: null tenant trying to terminate rental contract
        try {
            PropertyManager.getInstance().terminateRental(null);
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
        }
        Property v2 = PropertyManager.getInstance().addProperty(villaPropertyType);
        // test normal case
        PropertyManager.getInstance().issueRentalContract(tR, villaPropertyType, 20);
        Assertions.assertEquals(2, PropertyManager.getInstance().getTenantsProperties().size());
        // test error case: tenant can't rent two properties at the same time
        PropertyManager.getInstance().issueRentalContract(tR, apartmentPropertyType, 20);
        Assertions.assertEquals(2, PropertyManager.getInstance().getTenantsProperties().size());
        // test normal case: terminating rental
        PropertyManager.getInstance().terminateRental(tR);
        Assertions.assertEquals(1, PropertyManager.getInstance().getTenantsProperties().size());
        // test normal case: now their previous rental has terminated, tenant can now rent a different property
        PropertyManager.getInstance().issueRentalContract(tR, apartmentPropertyType, 20);
        Assertions.assertEquals(2, PropertyManager.getInstance().getTenantsProperties().size());
        // test normal case: terminate the tenant's second rental
        PropertyManager.getInstance().terminateRental(tR);
        Assertions.assertEquals(1, PropertyManager.getInstance().getTenantsProperties().size());
    }

    private void testGetPropertiesTerminatingSoon() {
        Date dob = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(1998, Calendar.MAY, 8);
        dob = cal.getTime();
        TenantRecord tR5 = PropertyManager.getInstance().addTenantRecord("James", "May", dob, true);
        cal.set(1994, Calendar.FEBRUARY, 16);
        Date dob2 = cal.getTime();
        TenantRecord tR6 = PropertyManager.getInstance().addTenantRecord("Sharon", "Osbourne", dob2, false);
        // test normal case: no properties terminating soon
        PropertyManager.getInstance().issueRentalContract(tR5, villaPropertyType, 20);
        PropertyManager.getInstance().issueRentalContract(tR6, apartmentPropertyType, 20);
        Assertions.assertEquals(0, PropertyManager.getInstance().getPropertiesTerminatingSoon().size());
        PropertyManager.getInstance().terminateRental(tR5);
        // test normal case: 1 property terminating soon
        PropertyManager.getInstance().issueRentalContract(tR5, villaPropertyType, 2);
        Assertions.assertEquals(1, PropertyManager.getInstance().getPropertiesTerminatingSoon().size());
        PropertyManager.getInstance().terminateRental(tR6);
        // test boundary case: still 1 property terminating soon
        PropertyManager.getInstance().issueRentalContract(tR6, apartmentPropertyType, 8);
        Assertions.assertEquals(1, PropertyManager.getInstance().getPropertiesTerminatingSoon().size());
        PropertyManager.getInstance().terminateRental(tR6);
        // test normal case: now 2 properties terminating soon
        PropertyManager.getInstance().issueRentalContract(tR6, apartmentPropertyType, 7);
        Assertions.assertEquals(2, PropertyManager.getInstance().getPropertiesTerminatingSoon().size());
    }


}
