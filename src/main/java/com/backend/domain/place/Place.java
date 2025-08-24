package com.backend.domain.place;

import com.backend.domain.BaseEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "place_picture_url",
            joinColumns = @JoinColumn(name = "place_id")
    )
    @Column(name = "url", length = 1000)
    private List<String> pictureUrls;

    private Double latitude;

    private Double longitude;

    private String address;

    private Boolean canBabyEquipmentRental;

    private String categoryId;

    private String closeForTheDay;

    private Boolean canCreditCard;

    private String discount;

    private String fee;

    private String occupancy;

    @Column(length = 5000)
    private String parking;

    private Boolean isPetAvailable;

    private Integer postCode;

    private String seasons;

    private String telNumber;

    @Column(length = 5000)
    private String timeAvailable;

    @Column(length = 5000)
    private String travelTime;

    private String type;
}
