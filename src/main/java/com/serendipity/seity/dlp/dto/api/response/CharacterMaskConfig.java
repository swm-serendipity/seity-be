package com.serendipity.seity.dlp.dto.api.response;

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
    private List<CharactersToIgnore> charactersToIgnore;
}
