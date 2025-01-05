package org.example.houseplants.service;

import lombok.RequiredArgsConstructor;
import org.example.houseplants.model.Plant;
import org.example.houseplants.repository.PlantFileRepository;
import org.example.houseplants.repository.PlantRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
public final class PlantService {
    private final PlantRepository plantRepository;

    private List<Plant> plants = new ArrayList<>();

    public void addPlant(Plant plant) {
        plants.add(plant);
    }

    public void removePlant(int index) throws PlantServiceException {
        try {
            plants.remove(index);
        } catch (IndexOutOfBoundsException e) {
            throw new PlantServiceException("Plant " + index + " as it does not exist", e);
        }
    }

    public Collection<Plant> getPlants() {
        return Collections.unmodifiableList(plants);
    }

    public Plant getPlant(int index) {
        return plants.get(index);
    }

    public void printWateringInfo() {
        System.out.println("(¯`·._.··¸.-~*´¨¯¨`*·~-. My Beautiful Plants .-~*´¨¯¨`*·~-.¸··._.·´¯)");

        for (int i = 0; i < plants.size(); i++) {
            Plant plant = plants.get(i);
            System.out.println("" + i + ": " + plant.getWateringInfo());
        }

        System.out.println();
    }

    public void sortPlantsByName() {
        plants.sort(Comparator.comparing(Plant::getName));
    }

    public void sortPlantsByWatering() {
        plants.sort(Comparator.comparing(Plant::getWatering));
    }

    public List<Plant> getPlantsThatNeedsWatering() {
        List<Plant> result = new ArrayList<>();

        for (Plant plant : plants) {
            LocalDate nextDayOfWatering = plant.getWatering().plusDays(plant.getFrequencyOfWatering());

            if (nextDayOfWatering.isBefore(LocalDate.now()))
                result.add(plant);
        }

        return result;
    }

    public void load() throws PlantServiceException {
        try {
            plants = plantRepository.load();
        } catch (Exception e) {
            throw new PlantServiceException("Failed to load plants", e);
        }
    }

    public void save() throws PlantServiceException {
        try {
            plantRepository.save(plants);
        } catch (IOException e) {
            throw new PlantServiceException("Failed to save plants", e);
        }
    }

}
