package com.poly.gestioncoworkingspace.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subscription_id", referencedColumnName = "id", unique = true)
    private Subscription subscription;

    @ManyToMany
    @JoinTable(
            name = "member_meeting_room",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private List<MeetingRoom> reservedRooms;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
}
