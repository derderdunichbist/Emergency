package org.hsrt.mc.emergency.user;

/**
 * Created by KA on 14.05.2016.
 */
public class Medication {

    private int id;
    private String name;
    private String dosis;
    private String manufacturer;
    private int amountPerDay;

    public Medication(){

    }

    public Medication(String name, String dosis, String manufacturer, int amountPerDay) {
        this.name = name;
        this.dosis = dosis;
        this.manufacturer = manufacturer;
        this.amountPerDay = amountPerDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getAmountPerDay() {
        return amountPerDay;
    }

    public void setAmountPerDay(int amountPerDay) {
        this.amountPerDay = amountPerDay;
    }

    public void setId(int id) {
     this.id = id;
    }
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        boolean isEqual = false;
        if(o instanceof Medication) {
            Medication medication = (Medication)o;
            if(medication.getName().equals(this.getName()) && medication.getDosis().equals(this.getDosis()) && medication.getManufacturer().equals(this.getManufacturer())&& medication.getAmountPerDay() == this.getAmountPerDay()) {
                isEqual = true;
            }
        }
        return isEqual;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Manufacturer: " + manufacturer + " , Dosis: " + dosis + ", apd: " + amountPerDay;
    }
}
