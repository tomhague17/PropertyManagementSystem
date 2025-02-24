package uk.ac.ncl.tom.properties;

import java.util.Date;

/**
 * Property - interface to a property.
 *
 * @author Thomas Hague & Rouaa Yassin Kassab
 * Copyright (C) 2025 Newcastle University, UK
 */
public interface Property {
    /**
     * Returns the Property Code.
     * All properties must have a code
     *
     * @return the PropertyCode object
     */
    PropertyCode getPropertyCode();


    /**
     * Returns the Property type.
     * a property can be either a villa or an apartment
     *
     * @return a string (villa or apartment)
     */
    String getPropertyType();


    /**
     * Returns a boolean indicating whether or not the property is rented.
     *
     * @return true if the property is rented and false otherwise.
     */
    boolean isRented();


    /**
     * Returns an integer indicating the deposit amount of a property.
     *
     * @return an int
     */
    int getDeposit();

    /**
     * Returns the termination date for the rental contract .
     *
     * @return a Date
     */
    Date getTerminationDate();

    /**
     * Setter method to change the value of a properties rental state
     *
     * @param rented
     */
    void setRented(boolean rented);

    /**
     * Setter method to set a new termination date of a rental.
     *
     * @param terminationDate
     */
    void setTerminationDate(Date terminationDate);

}


