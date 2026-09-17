package com.marumiru.mylog.dto;

import com.marumiru.mylog.domain.Venue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueDto {

    private Long id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;

    // Entity -> DTO 변환 메서드
    public static VenueDto fromEntity(Venue venue) {
        return VenueDto.builder()
                .id(venue.getId())
                .name(venue.getName())
                .address(venue.getAddress())
                .latitude(venue.getLatitude())
                .longitude(venue.getLongitude())
                .build();
    }

    // DTO -> Entity 변환 메서드
    public Venue toEntity() {
        return Venue.builder()
                .name(this.name)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .build();
    }
}