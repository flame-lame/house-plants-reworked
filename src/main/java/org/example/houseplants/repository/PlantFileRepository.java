package org.example.houseplants.repository;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.example.houseplants.model.Plant;
import org.example.houseplants.model.PlantException;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public final class PlantFileRepository implements PlantRepository {
    private static final String CSV_COLUMN_DELIMITER = "\t";

    private static final String[] HEADERS = new String[]{
            "name",
            "notes",
            "frequencyOfWatering",
            "watering",
            "planted"
    };

    private static final CSVFormat CSV_READ_FORMAT = CSVFormat.DEFAULT.builder()
            .setDelimiter(CSV_COLUMN_DELIMITER)
            .setHeader(HEADERS)
            .setSkipHeaderRecord(false) // Do not skip the first line while reading
            .build();

    private static final CSVFormat CSV_WRITE_FORMAT = CSVFormat.DEFAULT.builder()
            .setDelimiter(CSV_COLUMN_DELIMITER)
            .setSkipHeaderRecord(true) // Skip writing the headers
            .build();

    private final String fileIn;
    private final String fileOut;

    @Override
    public void save(Collection<Plant> plants) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileOut, false);
             PrintWriter pw = new PrintWriter(fos);
             CSVPrinter csvPrinter = new CSVPrinter(pw, CSV_WRITE_FORMAT)) {

            for (Plant plant : plants) {
                csvPrinter.printRecord(
                        plant.getName(),
                        plant.getNotes(),
                        plant.getFrequencyOfWatering(),
                        plant.getWatering().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        plant.getPlanted().format(DateTimeFormatter.ISO_LOCAL_DATE)
                );
            }
        }
    }

    @Override
    public List<Plant> load() throws IOException, PlantException {
        try (FileReader reader = new FileReader(fileIn)) {
            Iterable<CSVRecord> records = CSV_READ_FORMAT.parse(reader);

            ArrayList<Plant> result = new ArrayList<>();

            for (CSVRecord csvRecord : records) {
                Plant plant = parsePlant(csvRecord);
                result.add(plant);
            }

            return result;
        }
    }

    private Plant parsePlant(CSVRecord csvRecord) throws PlantException {
        String name = csvRecord.get("name");
        String description = csvRecord.get("notes");
        LocalDate planted = LocalDate.parse(csvRecord.get("planted"), DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate watering = LocalDate.parse(csvRecord.get("watering"), DateTimeFormatter.ISO_LOCAL_DATE);
        int frequencyOfWatering = Integer.parseInt(csvRecord.get("frequencyOfWatering"));

        return new Plant(
                name,
                description,
                planted,
                watering,
                frequencyOfWatering
        );
    }
}
