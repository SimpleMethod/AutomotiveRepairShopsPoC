package com.simplemethod.automotiverepairshops.neo4jModel;

import lombok.*;

import javax.annotation.Resource;

@Resource
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@NoArgsConstructor
public class Cars {
    @Getter
    @Setter
    public String name;
    @Getter
    @Setter
    public String marka;
    @Getter
    @Setter
    public String model;
    @Getter
    @Setter
    public Integer statusNaprawy;
    @Getter
    @Setter
    public String userka;


}
