package org.example.houseplants;

import org.example.houseplants.model.Plant;
import org.example.houseplants.model.PlantException;
import org.example.houseplants.repository.PlantFileRepository;
import org.example.houseplants.service.PlantService;
import org.example.houseplants.service.PlantServiceException;

import java.time.LocalDate;

public final class MainClass {
    private static final String FILE_INPUT = "plants.txt";
    private static final String FILE_OUTPUT = "plants-output.txt";

    public static void main(String[] args) throws PlantServiceException, PlantException {
        PlantFileRepository plantFileRepository = new PlantFileRepository(
                FILE_INPUT,
                FILE_OUTPUT
        );
        PlantService plantService = new PlantService(plantFileRepository);

        try {
            plantService.load();
        } catch (PlantServiceException e) {
            System.out.println("Couldn't load plants from " + FILE_INPUT);
        }

        plantService.printWateringInfo();

        plantService.addPlant(new Plant(
                "Růže",
                "Trochu zdechlá",
                LocalDate.now().minusDays(10),
                LocalDate.now().minusDays(7),
                2
        ));

        for (int i = 0; i < 10; i++) {
            plantService.addPlant(new Plant(
                    "Tulipán na prodej " + i,
                    "",
                    LocalDate.now(),
                    LocalDate.now(),
                    14
            ));
        }

        plantService.removePlant(3);

        plantService.sortPlantsByWatering();

        plantService.printWateringInfo();

        try {
            plantService.save();
        } catch (PlantServiceException e) {
            System.out.println("Couldn't save plants into " + FILE_OUTPUT);
        }
    }
}
