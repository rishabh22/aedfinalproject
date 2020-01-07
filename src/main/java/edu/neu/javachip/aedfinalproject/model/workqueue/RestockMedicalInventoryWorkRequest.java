package edu.neu.javachip.aedfinalproject.model.workqueue;

import edu.neu.javachip.aedfinalproject.model.medicalstore.MedicalStoreInventoryItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class RestockMedicalInventoryWorkRequest extends WorkRequest {
    private String prescription;
    private ProvideMedicalItemsWorkRequest provideMedicalItemsWorkRequest;
    private List<MedicalStoreInventoryItem> medicalStoreInventoryItems;

    public RestockMedicalInventoryWorkRequest() {
        this.setType(Type.RESTOCK_MEDICAL_INVENTORY);
    }
}
