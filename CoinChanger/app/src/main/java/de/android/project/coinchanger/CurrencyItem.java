package de.android.project.coinchanger;

/**
 * Created by Frank
 * 20.05.2015.
 */
public class CurrencyItem {

    String name;
    float value;
    String fullName;

    public CurrencyItem(String name, float value) {
        this.name = name;
        this.value = value;
    }

    public CurrencyItem(String name, float value, String fullName) {
        this.name = name;
        this.value = value;
        this.fullName = fullName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if (fullName != null) {
            return fullName;
        }
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
