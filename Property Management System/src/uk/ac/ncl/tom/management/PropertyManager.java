/**
 * A Singleton class representing a Property Manager to store and control Properties, Tenants and Rentals.
 * Includes methods for adding properties and tenants to the system, issuing and terminating rental contracts, calculating
 * the number of available properties for rent and properties with rentals expiring within 7 days.
 *
 * @authors Thomas Hague.
 */

package uk.ac.ncl.tom.management;

import uk.ac.ncl.tom.properties.*;
import uk.ac.ncl.tom.tenants.*;

import java.util.*;

import static java.util.Calendar.*;
import static uk.ac.ncl.tom.properties.PropertyFactory.*;

public class PropertyManager {
    /**
     * Fields for a Singleton instance of PropertyManager and structures for storing property records, tenant records
     * and tenants with their rented property.
     */
    private static final PropertyManager INSTANCE = new PropertyManager();

    private final Map<PropertyCode, Property> properties = new HashMap<>();
    private final Map<TenantID, TenantRecord> tenants = new HashMap<>();
    private final Map<TenantID, PropertyCode> tenantsProperties = new HashMap<>();

    /**
     * Creates an instance of PropertyManager.
     */
    private PropertyManager() {
    }

    /**
     * Returns the singleton Property Manager instance.
     *
     * @returns PropertyManager single instance
     */
    public static PropertyManager getInstance() {
        return INSTANCE;
    }

    /**
     * Returns a Map of existing properties on the system.
     *
     * @return Map of Properties.
     */
    public Map<PropertyCode, Property> getProperties() {
        return properties;
    }

    /**
     * Returns a Map of existing tenants on the system.
     *
     * @return Map of tenants.
     */
    public Map<TenantID, TenantRecord> getTenants() {
        return tenants;
    }

    /**
     * Returns a Map of existing rentals on the system, with TenantID as key and linked to the associated rental property code.
     *
     * @return Map of tenants with relevant rented property.
     */
    public Map<TenantID, PropertyCode> getTenantsProperties() {
        return tenantsProperties;
    }

    /**
     * Creates and adds a Property instance to the system, which is either a Villa or Apartment object, depending on
     * the propertyType passed as a parameter.
     * Exceptions are thrown if the property type parameter is null, or is neither "Villa" nor "Apartment".
     * Once properties have been created, they are added to the Properties Map, with Property code as the key and
     * Property as the value.
     *
     * @param propertyType (either Villa or Apartment)
     * @return Property, either a Villa or Apartment.
     */
    public Property addProperty(String propertyType) {
        if (propertyType == null) {
            throw new IllegalArgumentException("Property type can't be null, please try again. We offer Villas and Apartments " +
                    "for rental.");
        }
        if (!propertyType.equalsIgnoreCase(labelVilla) && !propertyType.equalsIgnoreCase(labelApartment)) {
            throw new IllegalArgumentException("Invalid property type, please try again. The only properties " +
                    "we offer for rental are Villas and Apartments");
        }
        Property p = PropertyFactory.getInstance(propertyType);
        properties.put(p.getPropertyCode(), p);
        return p;
    }

    /**
     * Returns the number of a given type of properties that are available for rent.
     * Exceptions are thrown if the property type parameter is null, or is neither "Villa" nor "Apartment".
     * Properties are counted as available for rent if they are the specified type and currently not rented.
     *
     * @param propertyType (either Villa or Apartment)
     * @return an int, the number of properties available for rent that are of a specified type.
     */
    public int noOfAvailableProperties(String propertyType) {
        if (propertyType == null) {
            throw new IllegalArgumentException("Property type can't be null, please try again. We offer Villas and " +
                    "Apartments for rental.");
        }
        if (!propertyType.equalsIgnoreCase(labelVilla) && !propertyType.equalsIgnoreCase(labelApartment)) {
            throw new IllegalArgumentException("Invalid property type, please try again. The only properties " +
                    "we offer for rental are Villas and Apartments");
        }
        // Search through properties Map and every time a property is found that is not currently rented, the counter increases by 1.
        int totalAvailableProperties = 0;
        for (Property p : properties.values()) {
            if (propertyType.equalsIgnoreCase(p.getPropertyType()) && !p.isRented()) {
                totalAvailableProperties++;
            }
        }
        return totalAvailableProperties;
    }

    /**
     * Creates and adds a Tenant to the system with specified first name, last name, date of birth and if they are premium class or not.
     * Exceptions are thrown if either or both of first name and last name are null, if date of birth is null, and if a
     * tenant with the new tenant ID, or with the same name and date of birth already exists.
     * On success, the method adds the new tenant record to the Tenant map, and returns it.
     *
     * @param firstName tenants first name
     * @param lastName  tenants last name
     * @param dob       tenants date of birth
     * @param premium   is the tenant previous or not
     * @return the newly created tenant record
     */
    public TenantRecord addTenantRecord(String firstName, String lastName, Date dob, Boolean premium) {
        if (firstName == null || lastName == null) {
            throw new IllegalArgumentException("First name and Last name can't be null, please have another go");
        }
        if (dob == null) {
            throw new IllegalArgumentException("Date of birth can't be null, please have another go.");
        }
        Name n = new Name(firstName, lastName);
        TenantRecord newTR = new TenantRecord(n, dob, premium);
        // Check that the new tenant Record does not have same name and dob as a current tenant record, and doesn't
        // exist in the tenants Map.
        for (TenantRecord tR : tenants.values()) {
            if (newTR.equals(tR) || tenants.containsKey(newTR.getTenantID())) {
                throw new IllegalArgumentException("Tenant with name and DoB: " + n + ", " + dob + "already exists on our records.");
            }
        }
        tenants.put(newTR.getTenantID(), newTR);
        return newTR;
    }

    /**
     * Issues a rental contract between the specified Tenant record, for a specified property of either a Villa or an
     * Apartment, for the specified duration.
     * The method randomly picks a relevant property from a list of current available properties of the specified property type.
     * Upon success, the specified tenant record and property will be added to the TenantsProperties map, the properties
     * rental status is changed to rented, with the termination date set using the duration and set from today's date.
     * Exceptions are thrown if the property type parameter is null, or is neither "Villa" nor "Apartment".
     * Relevant error messages are printed if the tenant is under 21 and tries to rent a Villa, under 18 and
     * tries to rent an Apartment, tenant is not premium class and tries to rent a Villa, or tries to rent a 2nd
     * property when already renting one, as tenants can only rent one property at a time.
     * Calls validTenant, calcTenantAge, getAvailableProperty, assignProperty, calcTerminationDate methods.
     *
     * @param tenantRecord that will be renting the property.
     * @param propertyType that they would like to rent.
     * @param duration     of the rental property, in days.
     * @return true upon success, otherwise false.
     */
    public boolean issueRentalContract(TenantRecord tenantRecord, String propertyType, int duration) {
        int tenantAge = calcTenantAge(tenantRecord.getDateOfBirth());
        if (propertyType == null) {
            throw new IllegalArgumentException("Property type can't be null, please try again. We offer Villas and " +
                    "Apartments for rental.");
        }
        if (!propertyType.equalsIgnoreCase(labelVilla) && !propertyType.equalsIgnoreCase(labelApartment)) {
            throw new IllegalArgumentException("Invalid property type, please try again. The only properties " +
                    "we offer for rental are Villas and Apartments");
        }
        List<Property> availableProperties = getAvailableProperties(properties, propertyType);
        // check the tenant is not already renting a property
        if (validTenant(tenantRecord)) {
            // check there are available properties for rent
            if (!availableProperties.isEmpty()) {
                if (propertyType.equalsIgnoreCase(labelVilla)) {
                    // check tenant is at least 21 to be able to rent a Villa
                    if (tenantAge >= 21) {
                        // check tenant is premium class so can rent a Villa
                        if (tenantRecord.isPremiumClass()) {
                            // assign tenant record to an arbitrary available Villa for rent and add both to
                            // the tenantsProperties Map.
                            Random random = new Random();
                            int index = random.nextInt(availableProperties.size());
                            Property chosenVilla = availableProperties.get(index);
                            assignProperty(chosenVilla, tenantRecord, duration);
                            return true;
                        } else {
                            System.out.println("Tenant needs to be premium class to rent a Villa. Please have a look at our " +
                                    "apartments for rental instead.");
                            return false;
                        }
                    } else {
                        System.out.println("Tenant needs to be 21 to rent a villa. They will be eligible in " +
                                (21 - tenantAge) + " years time.");
                        return false;
                    }
                } else {
                    // check tenant is at least 18 to be able to rent an Apartment.
                    if (tenantAge >= 18) {
                        // assign tenant record to an arbitrary available Apartment for rent and add both to
                        // the tenantsProperties Map.
                        Random random = new Random();
                        int index = random.nextInt(availableProperties.size());
                        Property chosenApartment = availableProperties.get(index);
                        assignProperty(chosenApartment, tenantRecord, duration);
                        return true;
                    } else {
                        System.out.println("Tenant needs to be 18 to rent an apartment. They will be eligible in " +
                                (18 - tenantAge) + " years time.");
                        return false;
                    }
                }
            } else {
                return false;
            }
        } else {
            System.out.println("Tenant: " + tenantRecord.getName() + " can only rent one property at " +
                    "a time.");
            return false;
        }
    }

    /**
     * Called in the issueRentalContract method, validates if the tenant is currently renting a property or not.
     *
     * @param tR, a specified tenant record
     * @return true if the tenant is not renting a property, false if they are.
     */
    private boolean validTenant(TenantRecord tR) {
        return !tenantsProperties.containsKey(tR.getTenantID());
    }

    /**
     * Called in the issueRentalContract method, calculates the current tenants age from their date of birth and today's date.
     * Exception is thrown if date of brith is null.
     *
     * @param dob, tenants date of birth.
     * @return the age of the tenant.
     */
    private int calcTenantAge(Date dob) {
        if (dob == null) {
            throw new IllegalArgumentException("Date of birth can't be null, please have another go.");
        }
        Calendar cal = Calendar.getInstance();
        Calendar dobCal = Calendar.getInstance();
        dobCal.setTime(dob);
        // initially set age as the current year - their date of birth year.
        int age = cal.get(YEAR) - dobCal.get(YEAR);
        // if tenant hasn't had their birthday this current year yet, reduce the age variable by 1.
        if (cal.get(MONTH) < dobCal.get(MONTH) || cal.get(MONTH) == dobCal.get(MONTH)
                && cal.get(DATE) < dobCal.get(DATE)) {
            age--;
        }
        return age;
    }

    /**
     * Called in the issueRentalContract method,returns a list of properties of a specified property type that are currently
     * available to be rented. A property is available for rent and added to the return list if it matches the specified
     * property type and is currently set as not rented.
     * If property type is a Villa, there are additional checks that a Villa has a clean pool for it be available for rent.
     * Appropriate error messages are printed if there are no available properties for rent, or in the case of villas, if
     * there are Villas that aren't rented, but they don;t have a clean pool so are not available.
     *
     * @param properties   Map of properties added to the system.
     * @param propertyType desired property type for rental
     * @return a list of the available properties for rental
     */
    private List<Property> getAvailableProperties(Map<PropertyCode, Property> properties, String
            propertyType) {
        List<Property> availableProperties = new ArrayList<>();
        boolean availableVillaWithCleanPoolFound = false;
        boolean availableVillaWithDirtyPoolFound = false;
        for (Property p : properties.values()) {
            if (propertyType.equalsIgnoreCase(p.getPropertyType()) && !p.isRented()) {
                // if we are looking for available Villas, also ensure the pools are clean before adding them to
                // availableProperties Map.
                if (p instanceof Villa) {
                    Villa v = (Villa) p;
                    if (v.getCleanPool()) {
                        availableProperties.add(v);
                        availableVillaWithCleanPoolFound = true;
                    } else {
                        availableVillaWithDirtyPoolFound = true;
                    }
                } else {
                    availableProperties.add(p);
                }
            }
        }
        // if we only find villas with dirty pools and no villas with clean pools, print appropriate message.
        if (availableVillaWithDirtyPoolFound && !availableVillaWithCleanPoolFound) {
            System.out.println("Unfortunately we currently have no available Villas for rental with clean pools. Please " +
                    "keep checking back as we clean the villa pools daily.");

        } // if there are no available properties, print the appropriate message.
        else if (availableProperties.isEmpty()) {
            System.out.println("All " + propertyType + "'s are currently already rented. Please reach out with your email " +
                    "address and we will add you to our waiting list.");
        }
        return Collections.unmodifiableList(availableProperties);
    }

    /**
     * Called in the issueRentalContract method, responsible for assigning a specified property to a tenant record
     * for a specified number of days.
     * Sets the property's rental status to true, the termination date using the calcTerminationDate method and if the
     * property is a villa, changes the status of the pool to need's cleaning.
     * The method also adds the specified tenant record and property to the tenantsProperties Map to store the record.
     *
     * @param p             , specified property to be rented.
     * @param tenantRecord, specified tenant record who is renting the property.
     * @param duration      of the rental in days.
     */
    private void assignProperty(Property p, TenantRecord tenantRecord, int duration) {
        p.setRented(true);
        if (p instanceof Villa) {
            Villa v = (Villa) p;
            v.setCleanPool(false);
        }
        p.setTerminationDate((calcTerminationDate(duration)));
        tenantsProperties.put(tenantRecord.getTenantID(), p.getPropertyCode());
        System.out.println("Tenant: " + tenantRecord.getName() + ", has rented " + p + " for " + duration + " days.");
    }

    /**
     * Called in the issueRentalContract method through assignProperty, calculates the termination date of the rental by
     * adding the specified duration in days, onto today's date.
     *
     * @param duration
     * @return
     */
    private Date calcTerminationDate(int duration) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, duration);
        final Date terminationDate = c.getTime();
        return terminationDate;
    }

    /**
     * Terminates the rental of a property associated with the specified tenant record by removing the tenant record and
     * property from the tenantsProperty Map.
     * Throws exceptions if the tenant record is null, doesn't have any current rented properties or if the rental
     * property associated with the tenant record is null.
     * Sets the property's rental status to true, the termination date to null and if the property is a villa, changes
     * the status of the pool to clean.
     *
     * @param tenantRecord
     */
    public void terminateRental(TenantRecord tenantRecord) {
        if (tenantRecord == null) {
            throw new IllegalArgumentException("Tenant Record is not valid. Please try again.");
        }
        // identify the relevant tenant ID from the specified tenant Record, and their corresponding rental property.
        TenantID relevantTID = tenantRecord.getTenantID();
        if (!tenantsProperties.containsKey(relevantTID)) {
            throw new IllegalArgumentException("Tenant " + tenantRecord.getTenantID() + " does not have any rental properties. " +
                    "Please ensure you are trying the correct tenant.");
        }
        Property releventP = properties.get(tenantsProperties.get(relevantTID));
        if (releventP == null) {
            throw new IllegalArgumentException(relevantTID + "'s Rental property is missing");
        }
        tenantsProperties.remove(relevantTID);
        releventP.setRented(false);
        releventP.setTerminationDate(null);
        if (releventP instanceof Villa) {
            Villa v = (Villa) releventP;
            v.setCleanPool(true);
        }
        System.out.println(tenantRecord.getName() + "'s rental of " + releventP + " has been terminated.");
    }

    /**
     * Returns a Collection of properties with rentals terminating soon. This is determined as any live rental in the
     * Properties Map with a termination date not before today's date (in case any properties that have finished rentals
     * have not been removed from the tenantsProperty Map yet) and not after 7 days time.
     *
     * @return an unmodifiable Collection of the properties terminating soon.
     */
    public Collection<Property> getPropertiesTerminatingSoon() {
        Set<Property> propertiesTerminatingSoon = new HashSet<>();
        for (PropertyCode pc : tenantsProperties.values()) {
            Property expiringP = properties.get(pc);
            Date expiringDate = expiringP.getTerminationDate();
            Date todaysDate = new Date();
            Calendar c = Calendar.getInstance();
            c.add(DAY_OF_YEAR, 7);
            Date sevenDaysLater = c.getTime();
            if (!expiringDate.before(todaysDate) && !expiringDate.after(sevenDaysLater)) {
                propertiesTerminatingSoon.add(expiringP);
            }
        }
        return Collections.unmodifiableSet(propertiesTerminatingSoon);
    }

}