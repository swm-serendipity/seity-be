package com.serendipity.seity.dlp.dto.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InspectConfig {

    private List<InfoType> infoTypes;
    private String minLikelihood;
    private List<SingleCustomInfoType> customInfoTypes = new ArrayList<>();
}
