package edu.neu.javachip.aedfinalproject.model.medicalstore;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class MedicalEquipmentCatalogue {
    Set<MedicalItem> medicalItemsSet = new HashSet<>();

    public MedicalEquipmentCatalogue() {
        for (int i = 0; i < 1000; i++) {
            MedicalItem medicalItem = new MedicalItem();
            int random = 0 + (int) (Math.random() * ((1000 - 0) + 1));
            if (random % 2 == 0) {
                medicalItem.setType(MedicalItem.Type.MEDICINE);
            } else {
                medicalItem.setType(MedicalItem.Type.EQUIPMENT);
            }
            medicalItem.setName(medicalItem.getType().getValue() + " " + i);
            this.medicalItemsSet.add(medicalItem);
        }
    }

    public MedicalItem getItemByName(String name){
        return medicalItemsSet.stream().filter(medicalItem -> medicalItem.getName().equals(name)).findFirst().orElse(null);
    }
}
