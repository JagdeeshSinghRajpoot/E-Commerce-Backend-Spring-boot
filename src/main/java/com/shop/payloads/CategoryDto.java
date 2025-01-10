package com.shop.payloads;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {

    private Long id;

    private String name;

    private List<Long> subCategoriesIds;
}
