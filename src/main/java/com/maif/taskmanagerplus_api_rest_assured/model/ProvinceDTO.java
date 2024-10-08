package com.maif.taskmanagerplus_api_rest_assured.model;

import lombok.Getter;
import lombok.Setter;

/**
 * ProvinceDTO class represents a Data Transfer Object for provinces.
 * This class contains properties for the province's ID, name, and abbreviation.
 * It provides methods to create a ProvinceDTO object with default or custom values.
 * 
 * Lombok is used to automatically generate the getters and setters for the fields.
 * 
 * Author: Maicon Fang
 * Date: 2024-06-19
 */
@Setter
@Getter
public class ProvinceDTO {
    private Integer id;           // Unique identifier for the province
    private String name;          // Name of the province
    private String abbreviation;  // Abbreviation of the province

    /**
     * Static method to create a ProvinceDTO object with optional custom values.
     * If the name or abbreviation is null, default values will be used.
     *
     * @param name          The name of the province (optional).
     * @param abbreviation  The abbreviation of the province (optional).
     * @return A ProvinceDTO object with the provided or default values.
     */
    public static ProvinceDTO createProvince(String name, String abbreviation) {
        ProvinceDTO provinceDTO = new ProvinceDTO();
        provinceDTO.setName(name != null ? name : "Default Province Name");
        provinceDTO.setAbbreviation(abbreviation != null ? abbreviation : "Default Province Abbreviation");
        return provinceDTO;
    }

    /**
     * Static method to create a ProvinceDTO object with a specific ID, and optional custom values.
     * If the name or abbreviation is null, default values will be used.
     *
     * @param id            The unique ID of the province.
     * @param name          The name of the province (optional).
     * @param abbreviation  The abbreviation of the province (optional).
     * @return A ProvinceDTO object with the provided ID and default or custom values.
     */
    public static ProvinceDTO createProvince(Integer id, String name, String abbreviation) {
        ProvinceDTO provinceDTO = new ProvinceDTO();
        provinceDTO.setId(id);
        provinceDTO.setName(name != null ? name : "Default Province Name");
        provinceDTO.setAbbreviation(abbreviation != null ? abbreviation : "Default Province Abbreviation");
        return provinceDTO;
    }

    // Getters and Setters are automatically generated by Lombok
}
