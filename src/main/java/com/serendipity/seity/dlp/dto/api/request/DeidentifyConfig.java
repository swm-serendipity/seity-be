package com.serendipity.seity.dlp.dto.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeidentifyConfig {

    private InfoTypeTransformations infoTypeTransformations;
}
