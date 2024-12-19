package com.poly.gestioncoworkingspace.services;

import com.poly.gestioncoworkingspace.entities.Reservation;
import com.poly.gestioncoworkingspace.repositories.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation with ID " + id + " not found"));
    }

    public Reservation addReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void updateReservation(Long id, Reservation reservation) {
        reservationRepository.findById(id).map(existingRes -> {
            existingRes.setStartTime(reservation.getStartTime());
            existingRes.setEndTime(reservation.getEndTime());
            existingRes.setMember(reservation.getMember());
            existingRes.setMeetingRoom(reservation.getMeetingRoom());
            return reservationRepository.save(existingRes);
        }).orElseThrow(() -> new RuntimeException("Reservation with ID " + id + " not found"));
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
    public Page<Reservation> getReservationsByMC(String mc, Pageable pageable) {
        return reservationRepository.findByMeetingRoomNameContains(mc, pageable);
    }


}
