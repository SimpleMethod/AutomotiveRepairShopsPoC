package com.simplemethod.automotiverepairshops.DataModel;

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
    public String usterka;

    @Override
    public String toString() {
        return "======================================= \r\n" +
                "Numer rejestracyjny: " + name + "\r\n" +
                "Marka:" + marka + "\r\n" +
                "Model:" + model + "\r\n" +
                "Status Naprawy: " + statusNaprawy + "\r\n" +
                "Usterka: " + usterka +  "\r\n" +
                "======================================= \r\n";
    }
}
