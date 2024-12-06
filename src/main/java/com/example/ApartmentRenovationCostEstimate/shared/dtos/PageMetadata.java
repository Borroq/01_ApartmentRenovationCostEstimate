package com.example.ApartmentRenovationCostEstimate.shared.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageMetadata {

    int pageNumber;
    int pageSize;
    int totalPages;
    long totalElements;
}
