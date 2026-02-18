package com.elproducto.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDTO {
    private Integer home;
    private Integer away;
    private HalftimeScoreDTO halftime;
    private FulltimeScoreDTO fulltime;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HalftimeScoreDTO {
        private Integer home;
        private Integer away;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FulltimeScoreDTO {
        private Integer home;
        private Integer away;
    }
}
