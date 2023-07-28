package com.serendipity.seity.dlp.dto.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InfoTypeTransformations {

    private List<Transformation> transformations;
}
