package edu.neu.javachip.aedfinalproject.model.medicalstore;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class MedicalStoreRequestTableModel {
    private StringProperty medicalItem;
    private StringProperty medicalItemType;
    private IntegerProperty quantityRequested;
    private IntegerProperty quantityAvailable;
    private IntegerProperty shortfall;

    public MedicalStoreRequestTableModel(StringProperty medicalItem, StringProperty medicalItemType, IntegerProperty quantityRequested, IntegerProperty quantityAvailable, IntegerProperty shortfall) {
        this.medicalItem = medicalItem;
        this.medicalItemType = medicalItemType;
        this.quantityRequested = quantityRequested;
        this.quantityAvailable = quantityAvailable;
        this.shortfall = shortfall;
    }

    public String getMedicalItem() {
        return medicalItem.get();
    }

    public StringProperty medicalItemProperty() {
        return medicalItem;
    }

    public void setMedicalItem(String medicalItem) {
        this.medicalItem.set(medicalItem);
    }

    public String getMedicalItemType() {
        return medicalItemType.get();
    }

    public StringProperty medicalItemTypeProperty() {
        return medicalItemType;
    }

    public void setMedicalItemType(String medicalItemType) {
        this.medicalItemType.set(medicalItemType);
    }

    public int getQuantityRequested() {
        return quantityRequested.get();
    }

    public IntegerProperty quantityRequestedProperty() {
        return quantityRequested;
    }

    public void setQuantityRequested(int quantityRequested) {
        this.quantityRequested.set(quantityRequested);
    }

    public int getQuantityAvailable() {
        return quantityAvailable.get();
    }

    public IntegerProperty quantityAvailableProperty() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable.set(quantityAvailable);
    }

    public int getShortfall() {
        return shortfall.get();
    }

    public IntegerProperty shortfallProperty() {
        return shortfall;
    }

    public void setShortfall(int shortfall) {
        this.shortfall.set(shortfall);
    }
}
