package com.simplemethod.automotiverepairshops.DataModel;

import lombok.*;

import javax.annotation.Resource;

@Resource
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@NoArgsConstructor
public class CarsParts {
    @Getter
    @Setter
    public String name;
    @Getter
    @Setter
    public String kategoria;
    @Getter
    @Setter
    Integer cena;
}
