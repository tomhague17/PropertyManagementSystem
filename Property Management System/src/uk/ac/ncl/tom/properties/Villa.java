package uk.ac.ncl.tom.properties;

/**
 * Class representing an Apartment.
 *
 * @author Thomas Hague
 */

public class Villa extends PropertyFactory {
    private final PropertyCode propertyCode;
    private static final int deposit = 500;
    private boolean cleanPool;
    private static final String propertyType = "Villa";

    /**
     * Creates a Villa object and its Property code.
     */
    Villa() {
        super();
        this.propertyCode = PropertyCode.getInstance('V');
        this.cleanPool = true;
    }

    /**
     * Returns the Villa's Property Code.
     * All properties must have a code
     *
     * @return the PropertyCode object
     */
    @Override
    public PropertyCode getPropertyCode() {
        return propertyCode;
    }

    /**
     * Returns an integer indicating the deposit amount of a villa.
     *
     * @return an int
     */
    @Override
    public int getDeposit() {
        return deposit;
    }

    /**
     * Returns the apartment Property type.
     *
     * @return a string representing villa.
     */
    @Override
    public String getPropertyType() {
        return propertyType;
    }

    /**
     * Returns a boolean indicating whether the Villa's pool is clean.
     *
     * @return true if the pool is clean and false if it needs cleaning.
     */
    public boolean getCleanPool() {
        return cleanPool;
    }

    /**
     * Setter method to change the value of a properties' pools' cleanliness.
     *
     * @param cleanPool , new status of the pool's cleanliness.
     */
    public void setCleanPool(boolean cleanPool) {
        this.cleanPool = cleanPool;
    }

    /**
     * Overrides the existing toString method, to specify how we like to view Villa objects.
     *
     * @return a String representing an Villa, made up of the property type and property code.
     */
    @Override
    public String toString() {
        return getPropertyType() + " " + getPropertyCode();
    }
}
