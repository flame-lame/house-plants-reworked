package org.example.houseplants.repository;

import org.example.houseplants.model.Plant;
import org.example.houseplants.model.PlantException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface PlantRepository {
    void save(Collection<Plant> plants) throws IOException;
    List<Plant> load() throws IOException, PlantException;
}
