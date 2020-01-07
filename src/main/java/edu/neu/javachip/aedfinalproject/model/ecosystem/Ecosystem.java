package edu.neu.javachip.aedfinalproject.model.ecosystem;

import edu.neu.javachip.aedfinalproject.model.medicalstore.MedicalEquipmentCatalogue;
import edu.neu.javachip.aedfinalproject.model.network.Network;
import edu.neu.javachip.aedfinalproject.model.organization.Organization;
import edu.neu.javachip.aedfinalproject.model.role.Role;
import edu.neu.javachip.aedfinalproject.model.role.SystemAdminRole;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ecosystem extends Organization {

    private static Ecosystem business;
    private List<Network> networkList = new ArrayList<>();
    @Getter @Setter
    private MedicalEquipmentCatalogue medicalEquipmentCatalogue = new MedicalEquipmentCatalogue();

    private Ecosystem() {
        super(null);
    }

    public static Ecosystem getInstance() {
        if (business == null) {
            business = new Ecosystem();
        }
        return business;

    }

    public void addNetwork(Network network){
        network.setId(networkList.stream().mapToInt(Network::getId).max().orElse(0) + 1);
        networkList.add(network);
    }

    public void deleteNetwork(Network network){
        networkList.remove(network);

    }



    @Override
    public List<Role> getSupportedRole() {
        return Collections.singletonList(new SystemAdminRole());
    }

    public List<Network> getNetworkList() {
        return networkList;
    }


    public Network getNetworkById(int id){
        return networkList.stream().filter(enterprise -> enterprise.getId()==id).findFirst().orElse(null);
    }


}
