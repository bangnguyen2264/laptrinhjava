    package com.project.ShopKoi.model.entity;

    import com.fasterxml.jackson.annotation.JsonBackReference;
    import com.project.ShopKoi.model.enums.AddressClass;
    import jakarta.persistence.*;

    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.io.Serializable;
    import java.util.List;

    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class AddressItem extends BaseEntity implements Serializable {
        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String name;

        @Enumerated(EnumType.STRING)
        private AddressClass addressClass;

        private double longitude;
        private double latitude;

        @ManyToMany(mappedBy = "addressItems")
        private List<Address> addresses; // Danh sách địa chỉ liên kết với AddressItem

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parentId", nullable = true)
        @JsonBackReference
        private AddressItem parent; // Tham chiếu tới AddressItem cha
    }

