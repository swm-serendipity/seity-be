package com.serendipity.seity.dlp.dto.api.request;

import com.serendipity.seity.dlp.dto.api.CharactersToIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CharacterMaskConfig {

    private char maskingCharacter;
    private boolean reverseOrder;
    private List<CharactersToIgnore> charactersToIgnore;
}
