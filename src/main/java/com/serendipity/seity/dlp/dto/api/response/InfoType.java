package com.serendipity.seity.dlp.dto.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InfoType {

    private String name;
    private SensitivityScore sensitivityScore;
}
