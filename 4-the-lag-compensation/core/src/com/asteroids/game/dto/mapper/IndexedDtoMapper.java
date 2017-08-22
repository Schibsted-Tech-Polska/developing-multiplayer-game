package com.asteroids.game.dto.mapper;

import com.asteroids.game.dto.Dto;
import com.asteroids.game.dto.IndexedDto;

public class IndexedDtoMapper {
    public static <UnderlyingDto extends Dto> IndexedDto<UnderlyingDto>
    wrapWithIndex(UnderlyingDto dto, long index) {
        return new IndexedDto<>(dto, index);
    }
}
