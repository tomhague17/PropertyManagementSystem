package uk.ac.ncl.tom.tenants;

import java.util.Date;

/**
 * Class representing a Tenant Record
 *
 * @author Thomas Hague
 */

public final class TenantRecord {
    private final Name name;
    private final Date dateOfBirth;
    private final TenantID tenantID;
    private final boolean premiumClass;

    /**
     * Creates a tenant record using the specified parameters.
     * Exception is thrown if date of birth is null.
     *
     * @param name
     * @param dateOfBirth
     * @param isPremium
     */
    public TenantRecord(Name name, Date dateOfBirth, boolean isPremium) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth can't be null");
        }
        Name n = new Name(name.getFirstName(), name.getLastName());
        this.name = n;
        this.dateOfBirth = dateOfBirth;
        this.tenantID = TenantID.getInstance(n);
        this.premiumClass = isPremium;
    }

    /**
     * Returns the tenants name.
     *
     * @return Name.
     */
    public Name getName() {
        return new Name(name.getFirstName(), name.getLastName());
    }

    /**
     * Returns the tenant's date of birth.
     *
     * @return Date.
     */
    public Date getDateOfBirth() {
        return (Date) dateOfBirth.clone();
    }

    /**
     * Returns the tenant's ID.
     *
     * @return TenantID.
     */
    public TenantID getTenantID() {
        return tenantID;
    }

    /**
     * Returns the tenant's class status. Having premium class status means the tenant is eligible to rent a Villa,
     * otherwise they can't.
     *
     * @return true if the tenant is premium, false if not
     */
    public boolean isPremiumClass() {
        return premiumClass;
    }

    /**
     * Overrides the existing toString method, to specify how we like to view tenant record objects.
     *
     * @return a String representing a tenant ID, made up of initials, year of issue and serial number.
     */
    @Override
    public String toString() {
        return tenantID.toString();
    }

    /**
     * Overriding the existing equals method to determine if two tenant record objects are the same. They are the same
     * if first name, last name and date of birth are all identical, or they both point to the same object.
     *
     * @param o, the object to be compared.
     * @return true if the tenant records are equal, false if not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TenantRecord)) return false;
        final TenantRecord tR = (TenantRecord) o;
        return getName().equals(tR.getName()) && getDateOfBirth().equals(tR.getDateOfBirth());
    }

    /**
     * Overriding the existing hashCode method so two tenant record objects will have the same hash representation
     * if they are equal, using the same parameters as the tenant record equal method.
     *
     * @return an int
     */
    @Override
    public int hashCode() {
        int hc = 19;
        hc = 31 * hc + getName().hashCode();
        return hc * 31 + getDateOfBirth().hashCode();
    }


}
