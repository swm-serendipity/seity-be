package com.serendipity.seity.dlp.dto.api.request;

import com.serendipity.seity.dlp.dto.api.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExternalApiRequest {

    private Item item;
    private DeidentifyConfig deidentifyConfig;
    private InspectConfig inspectConfig;
}
