package edu.neu.javachip.aedfinalproject.model.patient;

import edu.neu.javachip.aedfinalproject.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    private int Id;
    private String name;
    private LocalDate dateOfBirth;
    private long phoneNumber;
    private String email;
    private String address;
    //In kgs
    private int weight;

    //cm
    private int height;
    private Gender gender;
    private LocalDateTime createdOn = LocalDateTime.now();

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Id == patient.Id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }
}
