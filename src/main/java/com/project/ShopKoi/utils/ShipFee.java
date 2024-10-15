package com.project.ShopKoi.utils;

import com.project.ShopKoi.model.entity.Address;
import com.project.ShopKoi.model.entity.Orders;
import com.project.ShopKoi.model.enums.TransportMethod;

public class ShipFee {

    // Radius of the Earth in kilometers
    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double AIR_RATE_PER_KM = 5000; // Đơn giá vận chuyển bằng đường hàng không
    private static final double SEA_RATE_PER_KM = 3000;   // Đơn giá vận chuyển bằng đường biển
    private static final double LAND_RATE_PER_KM = 2000;  // Đơn giá vận chuyển bằng đường bộ
    private static final double ADDITIONAL_SERVICE_FEE = 50000; // Phí dịch vụ gia tăng (giả định)

    public static double calculate(Orders order) {
        // Tính khoảng cách giữa địa chỉ gốc và địa chỉ đích
        double distance = calculateDistance(order.getOrigin(), order.getDestination());

        // Tính phí vận chuyển theo phương thức
        double costPerKm = switch (order.getMethod()) {
            case AIR -> AIR_RATE_PER_KM;
            case SEA -> SEA_RATE_PER_KM;
            case LAND -> LAND_RATE_PER_KM;
        };

        // Tính tổng phí dựa trên số lượng, khối lượng và khoảng cách
        double baseCost = (distance * costPerKm) * order.getQuantity();

        // Tính tổng phí trước VAT
        double totalCostBeforeVAT = baseCost + ADDITIONAL_SERVICE_FEE;

        // Làm tròn tổng phí trước VAT
        totalCostBeforeVAT = Math.floor(totalCostBeforeVAT);

        // Tính phí VAT 8%
        double vat = totalCostBeforeVAT * 0.08;

        // Làm tròn VAT
        vat = Math.floor(vat);

        // Tổng phí bao gồm VAT
        double totalCost = totalCostBeforeVAT + vat;

        // Làm tròn tổng phí cuối cùng
        return Math.floor(totalCost); // Trả về tổng phí đã được làm tròn
    }

    private static double calculateDistance(Address origin, Address destination) {
        double lat1 = Math.toRadians(origin.getLatitude());
        double lng1 = Math.toRadians(origin.getLongitude());
        double lat2 = Math.toRadians(destination.getLatitude());
        double lng2 = Math.toRadians(destination.getLongitude());

        double latDiff = lat2 - lat1;
        double lngDiff = lng2 - lng1;
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.sin(lngDiff / 2)
                * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c; // Trả về khoảng cách thực tế (km)
    }
}
