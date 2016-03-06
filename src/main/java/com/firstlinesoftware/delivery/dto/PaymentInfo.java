package com.firstlinesoftware.delivery.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public class PaymentInfo {

    public enum PaymentType{
        imp,
        exp,
        transport,
        transit
    }

    private PaymentType type;

    private String receiver;

    private LocalDate date;

    public PaymentInfo() {
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
