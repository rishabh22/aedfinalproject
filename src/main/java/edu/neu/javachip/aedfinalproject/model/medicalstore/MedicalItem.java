package edu.neu.javachip.aedfinalproject.model.medicalstore;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class MedicalItem {
    private String name;
    private MedicalItem.Type type;
    private long price;

    public enum Type {
        MEDICINE("Medicine"),
        EQUIPMENT("Equipment");

        private String value;

        private Type(String value){
            this.value=value;
        }
        public String getValue() {
            return value;
        }
        @Override
        public String toString(){
            return value;
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalItem that = (MedicalItem) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
