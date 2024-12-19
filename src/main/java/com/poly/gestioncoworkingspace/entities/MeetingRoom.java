package com.poly.gestioncoworkingspace.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Entity
@Getter
@Setter
public class MeetingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int capacity;
    private boolean isAvailable;
    @ManyToMany(mappedBy = "reservedRooms")
    private List<Member> members;

    @OneToMany(mappedBy = "meetingRoom", cascade = CascadeType.ALL)
    private List<Reservation> reservations;


}
