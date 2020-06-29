package com.simplemethod.automotiverepairshops.DataModel;

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


    @Override
    public String toString() {
        final String ifEmployee;
        if (pracownik == 1) {
            ifEmployee = "Tak";
        } else {
            ifEmployee = "Nie";
        }
        ;
        return "======================================= \r\n" +
                "Imię i nazwisko: " + name + "\r\n" +
                "Pracownik: " + ifEmployee + "\r\n" +
                "Telefon: " + telefon + "\r\n" +
                "======================================= \n";
    }
}
