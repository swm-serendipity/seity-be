package com.serendipity.seity.dlp.dto.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransformationSummary {

    private InfoType infoType;
    private Transformation transformation;
    private List<Result> results;
    private String transformedBytes;
}
