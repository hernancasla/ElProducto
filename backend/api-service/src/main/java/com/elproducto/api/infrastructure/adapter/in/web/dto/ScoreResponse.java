package com.elproducto.api.infrastructure.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreResponse {
    private Integer home;
    private Integer away;
    private HalftimeScore halftime;
    private FulltimeScore fulltime;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HalftimeScore {
        private Integer home;
        private Integer away;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FulltimeScore {
        private Integer home;
        private Integer away;
    }
}
