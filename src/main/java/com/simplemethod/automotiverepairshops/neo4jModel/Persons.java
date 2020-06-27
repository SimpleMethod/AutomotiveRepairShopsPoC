package com.simplemethod.automotiverepairshops.neo4jModel;

import lombok.*;

import javax.annotation.Resource;

@Resource
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@NoArgsConstructor
public class Persons {
    @Getter
    @Setter
    public String name;
    @Getter
    @Setter
    public Integer pracownik;
    @Getter
    @Setter
    Long  telefon;
}
