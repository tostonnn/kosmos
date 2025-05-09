package com.fifty.kosmos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Consultorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int number;
    private int floor;
    @JsonIgnore
    @OneToMany(mappedBy = "consultorio")
    private Set<Citas> citas;

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Set<Citas> getCitas() {
        return citas;
    }

    public void setCitas(Set<Citas> citas) {
        this.citas = citas;
    }
}
