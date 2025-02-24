package uk.ac.ncl.tom.tenants;

import java.util.HashMap;
import java.util.*;
import java.util.Calendar;

/**
 * Class representing a tenants ID.
 *
 * @author Thomas Hague
 */

public final class TenantID {
    private final String initials;
    private final int yearOfIssue;
    private final String serialNumber;
    private final String strRep;
    private static final Map<String, TenantID> TENANTIDs = new HashMap<>();

    /**
     * Creates a tenant ID using the specified parameters.
     *
     * @param initials     of the tenant
     * @param yearOfIssue  year the ID was created.
     * @param serialNumber random two-digit number
     * @param strRep       String representation of the tenant ID
     */
    private TenantID(String initials, int yearOfIssue, String serialNumber, String strRep) {
        this.initials = initials;
        this.yearOfIssue = yearOfIssue;
        this.serialNumber = serialNumber;
        this.strRep = strRep;
    }

    /**
     * Static Factory method that returns a unique Tenant ID for the specified name.
     * If the generated tenantID already exists in the system, it will generate a new one until a unique tenant ID is
     * created that doesn't exist in the system. This new tenant ID is then added to the Tenant ID Map.
     * The method calculates the initials based on the specified name, sets the year of issue to the date of creation
     * and generates the alphanumeric code.
     * Exception is thrown if name is null
     *
     * @param name , made up of first name and last name.
     * @return the tenant ID
     */
    public static TenantID getInstance(Name name) {
        if (name == null) {
            throw new IllegalArgumentException("Name can't be null");
        }
        String initials = "" + name.getFirstName().charAt(0) + name.getLastName().charAt(0);
        int yearOfIssue = generateYearOfIssue();
        // set serial number and string representation of tenant ID.
        String serialNumber = "";
        String strRep = "";
        while (strRep.isEmpty() || TENANTIDs.containsKey(strRep)) {
            serialNumber = generateSerialNumber();
            strRep = initials + "." + yearOfIssue + "." + serialNumber;
        }
        // when a unique tenant ID has been generated, create tenant ID object.
        TenantID tID = new TenantID(initials, yearOfIssue, serialNumber, strRep);
        TENANTIDs.put(strRep, tID);
        return tID;
    }

    /**
     * Called in the tenant ID getInstance method, that calculates the appropriate year of Issue as the current year.
     *
     * @return an int representing the year the ID was issued
     */
    private static int generateYearOfIssue() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * Called in the tenant ID getInstance method, generates a random two-digit number between 0-99.
     *
     * @return a String representing the serial number.
     */
    private static String generateSerialNumber() {
        Random random = new Random();
        return String.format("%02d", random.nextInt(100));
    }

    /**
     * Returns the initials.
     *
     * @return a string representing the tenants initials.
     */
    public String getInitials() {
        return initials;
    }

    /**
     * Returns the year of issue.
     *
     * @return an int representing the year of issue.
     */
    public int getYearOfIssue() {
        return yearOfIssue;
    }

    /**
     * Returns the serial number.
     *
     * @return an int representing serial number.
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Returns a Map of tenant IDs
     *
     * @return Map
     */
    public Map<String, TenantID> getTenantIDsMap() {
        return Collections.unmodifiableMap(TENANTIDs);
    }

    /**
     * Overrides the existing toString method, to specify how we like to view tenant ID objects.
     *
     * @return a String representing a tenant ID, made up of initials, year of issue and serial number.
     */
    @Override
    public String toString() {
        return strRep;
    }
}
