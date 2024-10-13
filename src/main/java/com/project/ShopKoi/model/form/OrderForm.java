package com.project.ShopKoi.model.form;

import com.project.ShopKoi.model.enums.TransportMethod;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderForm {
    private int quantity;
    private double weight;
    private AddressForm origin;
    private AddressForm destination;
    private TransportMethod method;
    private String note;
}
