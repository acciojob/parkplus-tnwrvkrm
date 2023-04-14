package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation = reservationRepository2.findById(reservationId).get();
        int timeForReservation = reservation.getNumberOfHours();
        int spotRatePerHour = reservation.getSpot().getPricePerHour();
        int billedAmount = timeForReservation * spotRatePerHour;

        if(amountSent < billedAmount)
            throw new Exception("Insufficient Amount");

        //setting up the payment mode
        PaymentMode paymentMode = null;
        if(mode.toUpperCase().equals(PaymentMode.CASH.toString()))
            paymentMode = PaymentMode.CASH;
        else if(mode.toUpperCase().equals(PaymentMode.CARD.toString()))
            paymentMode = PaymentMode.CARD;
        else if (mode.toUpperCase().equals(PaymentMode.UPI.toString()))
            paymentMode = PaymentMode.UPI;

        if(paymentMode == null)
            throw new Exception("Payment mode not detected");

        Payment payment = new Payment();
        payment.setPaymentMode(paymentMode);
        payment.setPaymentCompleted(true);
        payment.setReservation(reservation);

        reservation.setPayment(payment);

        reservationRepository2.save(reservation);

        return payment;
    }
}
