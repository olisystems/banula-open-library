package com.banula.openlib.ocpi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * With CancelReservation the Sender can request the Cancel of an existing
 * Reservation.
 * The CancelReservation needs to contain the reservation_id that was given by
 * the Sender to the ReserveNow.
 * As there might be cost involved for a Reservation, canceling a reservation
 * might still result
 * in a CDR being sent for the reservation.
 */
@Data
@ToString
@NoArgsConstructor
public class CancelReservation {

    /**
     * URL that the CommandResult POST should be sent to. This URL might contain an
     * unique ID
     * to be able to distinguish between CancelReservation requests.
     */
    @NotEmpty(message = "Response URL must not be empty")
    @JsonProperty("response_url")
    private String responseUrl;

    /**
     * Reservation id, unique for this reservation. If the Charge Point already has
     * a reservation that matches
     * this reservationId, the Charge Point will replace the reservation.
     */
    @NotEmpty(message = "Reservation ID must not be empty")
    @Size(max = 36, message = "Reservation ID length must be at most 36 characters")
    @JsonProperty("reservation_id")
    private String reservationId;

}
