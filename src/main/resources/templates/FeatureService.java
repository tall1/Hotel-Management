package com.hotels.service;


import com.hotels.entities.roomreservationfeature.Feature;

import java.sql.SQLException;
import java.util.List;

public interface FeatureService {
     List<Feature> getAll() throws SQLException;
     Feature getFeatureById(int id) throws SQLException;
     void insertFeature(Feature feature) throws SQLException;
     void updateFeature(Feature feature) throws SQLException;
     void deleteFeature(int id) throws SQLException;
}
