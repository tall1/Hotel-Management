package com.hotels.utils;

import com.hotels.repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeatureCounter {
    private static long amountOfFeatures;

    @Autowired
    public FeatureCounter(FeatureRepository featureRepository) {
        amountOfFeatures = featureRepository.count();
    }

    public static long getAmountOfFeatures() {
        return amountOfFeatures;
    }
}
