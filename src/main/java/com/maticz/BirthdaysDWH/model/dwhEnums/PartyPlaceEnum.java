package com.maticz.BirthdaysDWH.model.dwhEnums;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public enum PartyPlaceEnum {



    SVETLO_MODRA("Svetlo modra",3,1),
    TEMNO_MODRA("Temno modra",4,1),
    ROZA("Roza",5,1),
    VIJOLICNA("Vijolična",6,1),
    RUMENA("Rumena",8,1),
    BELA("Bela",9,1),
    MINI_WOOP("Miniwoop",23,1),
    DPLACE("DPlace",24,1),
    SILVERSTONE_SOBA("Silverstone soba",397,2),
    MONACO_SOBA("Monaco soba",32,2),
    DAYTONA_SOBA("Daytona soba",33,2),
    MOST("Most",35,2),
    TERARI_1("Terari 1",103, 3),
    VIP_1("Terari 1",104,3),
    ER_1("ER 1",105,3),
    TERARI_2("Terari 2",106,3),
    VIP_2("VIP 2", 107,3),
    ER_2("ER 2",108,3),
    VR("VR",109,3),
    VIP_3("VIP 3",110,3),
    LT("LT",111,3),
    LT_2("LT 2",112,3),
    VIP_4("VIP 4",113,3),
    PARTY_PROSTOR_1("Party prostor 1",303,6),
    PARTY_PROSTOR_2("Party prostor 2", 304,6),
    PARTY_PROSTOR_3("Party prostor 3",305,6),
    PARTY_PROSTOR_4("Party prostor 4",306,6),
    PARTY_SOBA_1("Party soba 1",203,5),
    PARTY_SOBA_2("Party soba 2",204,5),
    PARTY_SOBA_3("Party soba 3",205,5);



    private final String partyPlaceName;
    private final Integer idPlace;
    private final Integer idLocation;


    PartyPlaceEnum(String partyPlaceName, Integer idPlace, Integer idLocation ) {
        this.partyPlaceName = partyPlaceName;
        this.idPlace = idPlace;
        this.idLocation = idLocation;
    }

    public static Integer partyPlaceToidPlace(String partyPlaceName){
        Integer idPlace = 0;

        for( PartyPlaceEnum partyPlace : PartyPlaceEnum.values()){
            if(Objects.equals(partyPlace.getPartyPlaceName(), partyPlaceName)){
                 idPlace = partyPlace.getIdPlace();

            }else {
                return  idPlace = 0;
            }
        }
        return idPlace;
    }

}
