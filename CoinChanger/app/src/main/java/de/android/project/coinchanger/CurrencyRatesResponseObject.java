package de.android.project.coinchanger;

import java.util.HashMap;

/**
 * Created by Frank
 * 20.05.2015.
 */
public class CurrencyRatesResponseObject {

    long timestamp;
    String base;
    HashMap<String, Float> rates;


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public CurrencyRatesResponseObject(long timestamp, String base, HashMap<String, Float> rates) {
        this.timestamp = timestamp;
        this.base = base;
        this.rates = rates;
    }

    public HashMap<String, Float> getRates() {
        return rates;
    }

    public void setRates(HashMap<String, Float> rates) {
        this.rates = rates;
    }
}
