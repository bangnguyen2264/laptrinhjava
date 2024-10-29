package com.project.ShopKoi.utils;

import com.project.ShopKoi.model.entity.Address;
import com.project.ShopKoi.model.entity.Orders;
import com.project.ShopKoi.model.enums.TransportMethod;

public class ShipFee {

    // Radius of the Earth in kilometers
    public static final double EARTH_RADIUS_KM = 6371.0;
    public static final double AIR_RATE_PER_KM = 5000; // Đơn giá vận chuyển bằng đường hàng không
    public static final double SEA_RATE_PER_KM = 3000;   // Đơn giá vận chuyển bằng đường biển
    public static final double LAND_RATE_PER_KM = 2000;  // Đơn giá vận chuyển bằng đường bộ
    public static final double ADDITIONAL_SERVICE_FEE = 50000; // Phí dịch vụ gia tăng (giả định)

    public static double calculate(Orders order) {
        // Tính khoảng cách giữa địa chỉ gốc và địa chỉ đích
        double distance = calculateDistance(order.getOrigin(), order.getDestination());
        System.out.println("Distance: " + distance);

        // Tính phí vận chuyển theo phương thức
        double costPerKm = switch (order.getMethod()) {
            case AIR -> AIR_RATE_PER_KM;
            case SEA -> SEA_RATE_PER_KM;
            case LAND -> LAND_RATE_PER_KM;
        };
        System.out.println("Cost per km: " + costPerKm);

        // Tính số kg hàng hóa (giá theo costPerKm với mỗi 10kg hàng hóa)
        double weightFactor = order.getWeight() / 10.0; // Làm tròn lên số kg hàng hóa chia cho 10
        System.out.println("Weight Factor: " + weightFactor);

        // Tính tổng phí dựa trên số lượng, khối lượng và khoảng cách
        double baseCost = (distance * costPerKm * weightFactor) * order.getQuantity();
        System.out.println("Base Cost: " + baseCost);

        // Tính tổng phí trước VAT
        double totalCostBeforeVAT = baseCost + ADDITIONAL_SERVICE_FEE;
        System.out.println("Total Cost Before VAT: " + totalCostBeforeVAT);

        // Tính phí VAT 8%
        double vat = totalCostBeforeVAT * 0.08;
        System.out.println("VAT: " + vat);

        // Tổng phí bao gồm VAT
        double totalCost = totalCostBeforeVAT + vat;

        // Làm tròn tổng phí cuối cùng
        totalCost = Math.floor(totalCost);
        System.out.println("Total Cost: " + totalCost); // Trả về tổng phí đã được làm tròn

        return totalCost;
    }



    public static double calculateDistance(Address origin, Address destination) {
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
