package com.serendipity.seity.dlp.dto.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transformation {

    private List<InfoType> infoTypes;
    private PrimitiveTransformation primitiveTransformation;
}
