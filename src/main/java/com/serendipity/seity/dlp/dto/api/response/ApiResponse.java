package com.serendipity.seity.dlp.dto.api.response;

import com.serendipity.seity.dlp.dto.api.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

    private Item item;
    private Overview overview;
}
