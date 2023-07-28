package com.serendipity.seity.dlp.dto.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Overview {

    private String transformedBytes;
    private List<TransformationSummary> transformationSummaries;
}
