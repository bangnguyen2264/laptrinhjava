package com.project.ShopKoi.model.form;

import com.project.ShopKoi.model.entity.Orders;
import com.project.ShopKoi.model.enums.TransportMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OrdersForm implements Serializable {
    @NotNull
    @Min(1)
    private int quantity;
    @NotNull
    @Min(1)
    private double weight;
    @NotNull
    private AddressForm origin;
    @NotNull
    private AddressForm destination;
    @NotNull
    private TransportMethod method;
    @NotBlank
    private String note;

}
