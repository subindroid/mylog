package com.marumiru.mylog.dto;

import java.time.LocalDateTime;
import com.marumiru.mylog.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    private Long id;
    private String name;
    private LocalDateTime date;
    private String description;
    private String venueName; // 연관된 Venue의 장소명

    public static EventDto fromEntity(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .date(event.getEventDate())
                .description(event.getDescription())
                .venueName(event.getVenue() != null ? event.getVenue().getName() : null)
                .build();
    }
}