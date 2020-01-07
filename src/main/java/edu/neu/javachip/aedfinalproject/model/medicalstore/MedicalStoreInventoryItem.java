package edu.neu.javachip.aedfinalproject.model.medicalstore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalStoreInventoryItem {
    private MedicalItem medicalItem;
    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalStoreInventoryItem that = (MedicalStoreInventoryItem) o;
        return Objects.equals(medicalItem, that.medicalItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicalItem);
    }
}
