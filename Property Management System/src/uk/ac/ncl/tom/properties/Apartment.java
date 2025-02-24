package uk.ac.ncl.tom.properties;

/**
 * Class representing an Apartment.
 *
 * @author Thomas Hague
 */
class Apartment extends PropertyFactory {
    private final PropertyCode propertyCode;
    private static final int deposit = 200;
    private static final String propertyType = "Apartment";

    /**
     * Creates an Apartment object and its Property code.
     */
    Apartment() {
        super();
        this.propertyCode = PropertyCode.getInstance('A');
    }

    /**
     * Returns the Apartment's Property Code.
     * All properties must have a code
     *
     * @return the PropertyCode object
     */
    @Override
    public PropertyCode getPropertyCode() {
        return propertyCode;
    }

    /**
     * Returns an integer indicating the deposit amount of an apartment.
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
     * @return a string representing apartment.
     */
    @Override
    public String getPropertyType() {
        return propertyType;
    }

    /**
     * Overrides the existing toString method, to specify how we like to view Apartment objects.
     *
     * @return a String representing an Apartment, made up of the property type and property code.
     */
    @Override
    public String toString() {
        return getPropertyType() + " " + getPropertyCode();
    }
}
