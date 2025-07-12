package com.example.demo.dto;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private Long sellerId;
}
