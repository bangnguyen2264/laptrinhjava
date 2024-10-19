package com.project.ShopKoi.model.dto;

import com.project.ShopKoi.model.enums.TransportMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceTableDto {
    private TransportMethod method;
    private double price;
}
