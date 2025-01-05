package org.example.houseplants.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Comparator;

@Data
public final class Plant implements Comparable<Plant> {
    private static final int DEFAULT_FREQUENCY_OF_WATERING = 7;

    private String name;
    private String notes;
    private LocalDate planted;
    private LocalDate watering;
    private int frequencyOfWatering;

    public Plant(String name) throws PlantException {
        this(name, DEFAULT_FREQUENCY_OF_WATERING);
    }

    public Plant(String name, int frequencyOfWatering) throws PlantException {
        this(name, "", LocalDate.now(), LocalDate.now(), frequencyOfWatering);
    }

    public Plant(String name, String notes, LocalDate planted, LocalDate watering, int frequencyOfWatering) throws PlantException {
        if (watering.isBefore(planted)) {
            throw new PlantException("Cannot water plant which wasn't planted yet");
        }

        this.name = name;
        this.notes = notes;
        setPlanted(planted);
        this.watering = watering;
        setFrequencyOfWatering(frequencyOfWatering);
    }

    public void setPlanted(LocalDate planted) throws PlantException {
        if (planted.isAfter(LocalDate.now())) {
            throw new PlantException("Do you have crystal ball from Globus for 3.50 CZK?");
        }

        this.planted = planted;
    }

    public void setFrequencyOfWatering(int frequencyOfWatering) throws PlantException {
        if (frequencyOfWatering <= 0) {
            throw new PlantException("Frequency of watering has to be greater than 0");
        }

        this.frequencyOfWatering = frequencyOfWatering;
    }

    public String getWateringInfo() {
        return "\"" + name + "\", watered " + watering +
                ", next watering " + recommendedWatering();
    }

    public void doWateringNow() {
        watering = LocalDate.now();
    }

    private LocalDate recommendedWatering() {
        return watering.plusDays(frequencyOfWatering);
    }

    @Override
    public int compareTo(Plant o) {
        return Comparator.comparing(Plant::getName)
                .thenComparing(Plant::getPlanted)
                .compare(this, o);
    }
}
