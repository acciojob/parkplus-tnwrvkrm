package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

        //if user or parking lot not found
        User user;
        try {
            user = userRepository3.findById(userId).get();
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        }
        catch (Exception e){
            throw new Exception("Cannot make reservation");
        }

        //getting the spots in parking lot
        List<Spot> spotList = parkingLotRepository3.findById(parkingLotId).get().getSpotList();


        int currPricePerHour = Integer.MAX_VALUE;

        //iterating for the lowest price available spot
        Spot emptySpot = null;
        for(Spot currSpot : spotList){
            if(!currSpot.getOccupied()){

                //fetching the size of spot
                int wheelCount = Integer.MAX_VALUE;
                if(currSpot.getSpotType() == SpotType.TWO_WHEELER)
                    wheelCount = 2;
                else if(currSpot.getSpotType() == SpotType.FOUR_WHEELER)
                    wheelCount = 4;

                if(numberOfWheels <= wheelCount){
                    if(currSpot.getPricePerHour() < currPricePerHour) {
                        currPricePerHour = currSpot.getPricePerHour();
                        emptySpot = currSpot;
                    }
                }
            }
        }

        //spot not available condition
        if(emptySpot == null){
            throw new Exception("Cannot make reservation");
        }

        //all right let's set the spot
        emptySpot.setOccupied(true);


        //found the appropriate spot, let's make a reservation
        Reservation reservation = new Reservation();
        reservation.setNumberOfHours(timeInHours);
        reservation.setSpot(emptySpot);
        reservation.setUser(user);

        emptySpot.getReservationList().add(reservation);
        user.getReservationList().add(reservation);

        userRepository3.save(user);
        spotRepository3.save(emptySpot);


        return reservation;
    }
}
