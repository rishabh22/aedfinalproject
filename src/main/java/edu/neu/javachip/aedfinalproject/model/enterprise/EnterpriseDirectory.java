package edu.neu.javachip.aedfinalproject.model.enterprise;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnterpriseDirectory {

    private List<Enterprise> enterpriseList = new ArrayList<>();

    public List<Enterprise> getEnterpriseList() {
        return enterpriseList;
    }

    public void addEnterprise(Enterprise enterprise) {
        enterprise.setId(enterpriseList.stream().mapToInt(Enterprise::getId).max().orElse(0) + 1);
        enterpriseList.add(enterprise);
    }

    public void deleteEnterprise(Enterprise enterprise){
        enterpriseList.remove(enterprise);
    }

    public List<Enterprise> getEnterprisesByType(Enterprise.Type type){
        return enterpriseList.stream()
                .filter(enterprise -> enterprise.getEnterpriseType().equals(type))
                .collect(Collectors.toList());
    }

    public Enterprise getEnterpriseById(int id){
        return enterpriseList.stream().filter(enterprise -> enterprise.getId()==id).findFirst().orElse(null);
    }

}
