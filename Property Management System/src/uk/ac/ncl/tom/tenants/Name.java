package uk.ac.ncl.tom.tenants;

/**
 * Class representing a tenants Name.
 *
 * @author Thomas Hague
 */

public final class Name {
    private final String firstName;
    private final String lastName;

    /**
     * Creates a Name object using specified first name and last name.
     * Exceptions are thrown if either or both of first name or last name are null or empty.
     *
     * @param firstName
     * @param lastName
     */
    public Name(String firstName, String lastName) {
        if (firstName == null) {
            throw new IllegalArgumentException("First name cannot be null");
        }
        if (lastName == null) {
            throw new IllegalArgumentException("Last name cannot be null");
        }
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Returns the first name.
     *
     * @return a string representing first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name.
     *
     * @return a string representing last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Overrides the existing toString method, to specify how we like to view Apartment objects.
     *
     * @return a String representing a Name, made up of the first name and last name.
     */
    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }

    /**
     * Overriding the existing equals method to determine if two name objects are the same. They are the same
     * if first and last name are both identical, or they point to the same object.
     *
     * @param o, the object to be compared.
     * @return true if the names are equal, false if not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Name)) return false;
        final Name n = (Name) o;
        return getFirstName().equals(n.getFirstName()) && getLastName().equals(n.getLastName());
    }

    /**
     * Overriding the existing hashCode method so two equal name objects will have the same hash representation,
     * using the same parameters as the Name equals method.
     *
     * @return an int
     */
    @Override
    public int hashCode() {
        int hc = 19;
        int mult = 31;
        hc = mult * hc + getFirstName().hashCode();
        return mult * hc + getLastName().hashCode();
    }

    /**
     * Method for creating a name object using a specified String representing a name.
     * Exceptions are thrown if specified name is null, empty or does not have two parts..
     *
     * @param name
     * @return
     */
    public static Name valueOf(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (name.length() == 0) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        // split name into two parts using " " as the divider.
        final String[] nameParts = name.split(" ");
        if (nameParts.length != 2) {
            throw new IllegalArgumentException("Name must contain a First and Last name");
        }

        return new Name(nameParts[0], nameParts[1]);
    }
}
