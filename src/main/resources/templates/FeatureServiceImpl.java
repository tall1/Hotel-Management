package com.hotels.service;
import com.hotels.entities.roomreservationfeature.Feature;
import com.hotels.repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

public class FeatureServiceImpl implements FeatureService {
    private final FeatureRepository featureRepository;

    @PostConstruct
    public void init453534(){
        // here put any after construction operations
        System.out.println("FeatureServiceImpl: @PostConstruct");
    }
    @PreDestroy
    public void  exitAll123(){
        System.out.println("FeatureServiceImpl: @PreDestroy");
    }

    @Autowired
    public FeatureServiceImpl(FeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
    }

    @Override
    public List<Feature> getAll() {
        return featureRepository.findAll();
    }

    @Override
    public Feature getFeatureById(int id)  {
        if(featureRepository.findById(id).isPresent()){
            return featureRepository.findById(id).get();
        }
        return null;
    }

    @Override
    public void insertFeature(Feature feature) {
        featureRepository.save(feature);
    }

    @Override
    public void updateFeature(Feature feature){
        featureRepository.save(feature);
    }

    @Override
    public void deleteFeature(int id) {
        featureRepository.deleteById(id);
    }
}
