package edu.neu.javachip.aedfinalproject.model.medicalstore;

import edu.neu.javachip.aedfinalproject.model.ecosystem.Ecosystem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class MedicalStoreInventory {
    private Set<MedicalStoreInventoryItem> medicalStoreInventoryItemsList = new HashSet<>();

    public MedicalStoreInventory() {
        for (int i = 0; i < 700; i++) {
            int randomIndex = 0 + (int) (Math.random() * ((999 - 0) + 1));
            int randomQuantity = 0 + (int) (Math.random() * ((100 - 0) + 1));

            List<MedicalItem> medicalEquipmentCatalogue = new ArrayList<>(Ecosystem.getInstance().getMedicalEquipmentCatalogue().getMedicalItemsSet());
            this.medicalStoreInventoryItemsList.add(new MedicalStoreInventoryItem(medicalEquipmentCatalogue.get(randomIndex), randomQuantity));
        }
    }


    public int getQuantityByItem(MedicalItem medicalItem) {
        return medicalStoreInventoryItemsList.stream()
                .filter(medicalStoreInventoryItem -> medicalStoreInventoryItem.getMedicalItem().equals(medicalItem))
                .map(MedicalStoreInventoryItem::getQuantity)
                .findFirst()
                .orElse(0);
    }

    public MedicalStoreInventoryItem getItemByName(String name) {
        return medicalStoreInventoryItemsList.stream()
                .filter(medicalStoreInventoryItem -> medicalStoreInventoryItem.getMedicalItem().getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public void addItem(MedicalStoreInventoryItem item) {
        medicalStoreInventoryItemsList.add(item);
    }
}
