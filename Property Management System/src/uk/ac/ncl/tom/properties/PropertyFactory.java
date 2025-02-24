package uk.ac.ncl.tom.properties;

import java.util.Date;

/**
 * Abstract class providing partial implementation of Property.
 *
 * @author Thomas Hague
 */

public abstract class PropertyFactory implements Property {
    private boolean isRented;
    private Date terminationDate;
    public static final String labelVilla = "Villa";
    public static final String labelApartment = "Apartment";

    /**
     * Creates a Property Factory object.
     */
    PropertyFactory() {
        this.isRented = false;
        this.terminationDate = null;
    }

    /**
     * Static Factory method that returns a property of the specified type.
     * If the label doesn't match either a Villa or Apartment, an exception is thrown.
     *
     * @param label, representing the relevant property type.
     * @return an account of the specified type, either a Villa or Apartment.
     */
    public static Property getInstance(String label) {
        if (!label.equalsIgnoreCase(labelVilla) && !label.equalsIgnoreCase(labelApartment)) {
            throw new IllegalArgumentException("Invalid property type: " + label + ", please try again.");
        }
        if (label.equalsIgnoreCase(labelVilla)) {
            return new Villa();
        } else {
            return new Apartment();
        }
    }

    /**
     * Returns the Property Code.
     * All properties must have a code.
     *
     * @return the PropertyCode object
     */
    public abstract PropertyCode getPropertyCode();

    /**
     * Returns a boolean indicating whether the property is rented.
     *
     * @return true if the property is rented and false otherwise.
     */
    @Override
    public boolean isRented() {
        return isRented;
    }

    /**
     * Returns an integer indicating the deposit amount of a property.
     *
     * @return an int
     */
    public abstract int getDeposit();

    /**
     * Returns a string indicating the Property type.
     *
     * @return a string
     */
    public abstract String getPropertyType();

    /**
     * Returns the termination date for the rental contract .
     *
     * @return a Date
     */
    @Override
    public Date getTerminationDate() {
        return terminationDate;
    }

    /**
     * Setter method to change the value of a properties rental state
     *
     * @param rented , properties' rental status
     */
    @Override
    public void setRented(boolean rented) {
        this.isRented = rented;
    }

    /**
     * Setter method to set a new termination date of a rental.
     *
     * @param terminationDate of the rental
     */
    @Override
    public void setTerminationDate(Date terminationDate) {
        this.terminationDate = terminationDate;
    }

}
