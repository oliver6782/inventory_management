package com.project.inventory_management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FDADrugResponseDTO {
    private List<Result> results;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private String effective_time;
        private List<String> inactive_ingredient;
        private List<String> purpose;
        private List<String> warnings;
        private List<String> other_safety_information;
        private List<String> dosage_and_administration;
        private List<String> pregnancy_or_breast_feeding;
        private List<String> stop_use;
        private List<String> indications_and_usage;
        private List<String> active_ingredient;
    }
}
