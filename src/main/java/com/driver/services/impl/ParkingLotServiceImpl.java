package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();

        parkingLot.setName(name);
        parkingLot.setAddress(address);
        parkingLot.setSpotList(new ArrayList<>());

        parkingLotRepository1.save(parkingLot);

        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {

        //getting the parking lot
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();

        //creating new empty spot
        Spot spot = new Spot();
        spot.setPricePerHour(pricePerHour);

        if (numberOfWheels <= 2){
            spot.setSpotType(SpotType.TWO_WHEELER);
        } else if (numberOfWheels <= 4) {
            spot.setSpotType(SpotType.FOUR_WHEELER);
        } else {
            spot.setSpotType(SpotType.OTHERS);
        }

        spot.setOccupied(false);
        spot.setParkingLot(parkingLot);
        spot.setReservationList(new ArrayList<>());

        parkingLot.getSpotList().add(spot);

        parkingLotRepository1.save(parkingLot);

        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
//        Spot spot = spotRepository1.findById(spotId).get();
//
//        spot.setParkingLot(parkingLotRepository1.findById(parkingLotId).get());
//        spot.setPricePerHour(pricePerHour);
//
//        spotRepository1.save(spot);

        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();

        for(Spot currSpot : parkingLot.getSpotList()){
            if(currSpot.getId() == spotId){
                currSpot.setPricePerHour(pricePerHour);
                spotRepository1.save(currSpot);

                return currSpot;
            }
        }

        return null;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
