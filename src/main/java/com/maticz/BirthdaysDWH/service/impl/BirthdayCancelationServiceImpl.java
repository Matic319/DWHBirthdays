package com.maticz.BirthdaysDWH.service.impl;

import com.maticz.BirthdaysDWH.model.Birthdays;
import com.maticz.BirthdaysDWH.repository.BirthdaysRepository;
import com.maticz.BirthdaysDWH.service.BirthdayCancelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
public class BirthdayCancelationServiceImpl implements BirthdayCancelationService {

    @Autowired
    private BirthdaysRepository birthdaysRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. M. yyyy HH:mm:ss");
    DateTimeFormatter formatterDMYOnly = DateTimeFormatter.ofPattern("d. M. yyyy");

    DateTimeFormatter formatterHMSOnly = DateTimeFormatter.ofPattern("HH:mm:ss");

    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-M-d");

    @Autowired
    GoogleSheetsServiceImpl googleSheetsService;

    @Override
    public void mapAndCopyCancelattion(String sheetId, String sheetName, Integer idLocation) throws IOException {

        List<List<Object>> sheet = googleSheetsService.readSheetRangeFrom(sheetId, sheetName, "A3:P2000");

        for (List<Object> row : sheet) {


            Boolean dontSave = false;
            LocalDate dateCancellation = LocalDate.parse(row.get(0).toString(),formatterDMYOnly);

            String parentEmail = "0";

            try {
                parentEmail = row.get(1).toString();
            }catch (IndexOutOfBoundsException e) {
                dontSave = true;
            }

            String progType = null;
            LocalDateTime dateFrom = null;
            String partyPlace = null ;
            Integer idPartyPlace = null ;
            String partyType = null ;
            Integer idPartyType=null;
            String duration = null;
            Integer idCancellationType = null;
            Integer idProgType = null;

            try {
            String cancellationReasonName = row.get(2).toString();

             idCancellationType = switch (cancellationReasonName.toLowerCase()) {

               case "bolezen" -> 1;
               case "sprememba programa" -> 2;
               case "brez razloga" -> 3;
               case "drugo" -> 4;
               case "covid" -> 5;
               case "karantena" -> 6;
                   default -> 0;
            };

            LocalDate date;

            try {
                 date = LocalDate.parse(row.get(4).toString(), formatterDMYOnly);
            }catch (DateTimeParseException e) {

                date = LocalDate.of(1999,1,1);
            }
            LocalTime startTime;

            try {
                startTime = LocalTime.parse(row.get(5).toString());
            }catch (DateTimeParseException e) {
                startTime = LocalTime.of(0,0,0);
            }

             dateFrom = LocalDateTime.of(date,startTime);

             progType = row.get(6).toString();

             idProgType = switch (progType.toLowerCase().trim()) {

                case "jump" -> 1;
                case "super jump" -> 2;
                case "mini woop!" -> 3;
                case "tiktok jump" -> 4;
                case "lov na zaklad tematski" -> 5;
                case "lov na zaklad črke" -> 6;
                case "lov na zaklad simboli" -> 7;
                case "karting & jump kombo" -> 8;
                case "fun walls" -> 9;
                case "kombo po meri" -> 10;
                case "mini fiesta" -> 11;
                case "maxi fiesta" -> 12;
                case "glow golf" -> 13;
                case "karting & glow golf kombo" -> 14;
                case "karting & bowling kombo" -> 15;
                case "laser tag" -> 16;
                case "bowling" -> 17;
                case "bowling odrasli" -> 18;
                case "cosmic bowling" -> 19;
                case "er" -> 20;
                case "escape room" -> 20;
                case "vr zabava" -> 21;
                case "bowling & laser tag kombo" -> 22;
                case "bowling & vr kombo" -> 23;
                case "izzivi" -> 24;
                case "kombo vr & lt" -> 25;
                case "kombo glow golf + jump" -> 26;
                case "kombo fw + tp" -> 27;
                default -> 0;
            };

             partyType = row.get(7).toString();

             idPartyType = switch (partyType.toUpperCase().trim()) {

                case "LNZ ČAROBNI GOZD" -> 1;
                case "LNZ PODVODNI SVET" -> 2;
                case "LNZ PIRATI" -> 3;
                case "LNZ ČRKE RUMENA" -> 4;
                case "LNZ ČRKE MODRA" -> 5;
                case "LNZ ČRKE ROZA" -> 6;
                case "LNZ SIMBOLI RUMENA" -> 7;
                case "LNZ SIMBOLI MODRA" -> 8;
                case "LNZ SIMBOLI ROZA" -> 9;
                case "LT2 + VR" -> 10;
                case "VR 2 + LT" -> 11;
                case "WOOPATLON" -> 12;
                case "LNZ NINJA TRENING" -> 13;
                case "LNZ NINJE" -> 14;
                case "LNZ SKRITI AGENTI" -> 15;
                case "LNZ VESOLJCI" -> 16;
                default -> 0;
            };

             duration = row.get(8).toString();
             partyPlace = row.get(9).toString();

             idPartyPlace = switch (partyPlace.toUpperCase().trim()){


                case "SVETLO MODRA" -> 3;
                case "TEMNO MODRA" -> 4;
                case "ROZA" -> 5;
                case "VIJOLIČNA" -> 6;
                case "RUMENA" -> 8;
                case "BELA" -> 9;
                case "MINIWOOP" -> 23;
                case "DPLACE" -> 24;
                case "SILVERSTONE SOBA" -> 397;
                case "MONACO SOBA" -> 32;
                case "DAYTONA SOBA" -> 33;
                case "TERARI 1" -> 103;
                case "VIP 1" -> 104;
                case "ER 1" -> 105;
                case "TERARI 2" -> 106;
                case "VIP 2" -> 107;
                case "ER 2" -> 108;
                case "VR" -> 109;
                case "VIP 3" -> 110;
                case "LT" -> 111;
                case "LT 2" -> 112;
                case "VIP 4" -> 113;
                case "PARTY PROSTOR 1" -> 303;
                case "PARTY PROSTOR 2" -> 304;
                case "PARTY PROSTOR 3" -> 305;
                case "PARTY PROSTOR 4" -> 306;
                case "PARTY SOBA 1" -> 203;
                case "PARTY SOBA 2" -> 204;
                case "PARTY SOBA 3" -> 205;
                default -> 0;
            }; }catch (IndexOutOfBoundsException e) {
                dontSave = true;
            }

            String childName = null;
            String childSurname = null;
            try {
                 childName = row.get(10).toString();
                 childSurname = row.get(11).toString();

            }catch (IndexOutOfBoundsException e){
                dontSave = true;
            }

            Integer participantCount;

             try {
                participantCount = Integer.parseInt(row.get(12).toString());
            }catch (NumberFormatException  | IndexOutOfBoundsException e) {
                participantCount = 0;
            }
            String parentName = null;
            String parentSurname = null;
            try {
                  parentName = row.get(14).toString();
                  parentSurname = row.get(15).toString();
             }catch (IndexOutOfBoundsException e){
                 dontSave = true;
             }

            Integer idResType;

            try {
                idResType = switch (row.get(16).toString().toLowerCase().trim()) {
                    case "splet" -> -1;
                    case "recepcija" -> 1;
                    case "telefon" -> 2;
                    case "email" -> 3;
                    default -> 0;
                };
            } catch (IndexOutOfBoundsException e){
                idResType = 0;
            }

             Integer age ;

             try {
                 age = Integer.parseInt(row.get(16).toString());
             }catch (NumberFormatException | IndexOutOfBoundsException e) {
                 age = 0;
             }


            Birthdays bday = new Birthdays();

             bday.setIdBDayProgType(idProgType);
             bday.setBirthdayProgType(progType);
             bday.setActive(2);
             bday.setIdLocation(idLocation);
             bday.setDateFrom(dateFrom);
             bday.setDateCanceled(dateCancellation);
             bday.setIdPartyPlaceName(partyPlace);
             bday.setIdPlace(idPartyPlace);
             bday.setParticipantCount(participantCount);
             bday.setBirthdayPartyType(partyType);
             bday.setIdBDayPartyType(idPartyType);
             bday.setDuration(duration);
             bday.setChildBDayAge(age);
             bday.setParentEmail(parentEmail);
             bday.setParentFirstName(parentName);
             bday.setParentLastName(parentSurname);
             bday.setChildFirstName(childName);
             bday.setChildLastName(childSurname);
             bday.setIdContactResType(idResType);
             bday.setIdCancelationType(idCancellationType);

            String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

            if(parentEmail.matches(regex) && !dontSave) {

                if(birthdaysRepository.findByParentEmailAndDateFromAndChildFirstNameAndIdLocation(parentEmail,dateFrom,childName,idLocation).isEmpty()) {
                    birthdaysRepository.save(bday);
                }

            }


        }

    }


    Logger logger = LoggerFactory.getLogger(BirthdayCancelationService.class);
    @Override
    public void copyCancellationFromCopySheets(String sheetId, String sheetName, Integer idLocation) throws IOException {

        List<List<Object>> sheet = googleSheetsService.readSheetRangeFrom(sheetId, sheetName, "A3:AY2000");

        for (List<Object> row : sheet) {


            Boolean dontSave = false;

            LocalDate dateCancellation = null;
            try {
                dateCancellation = LocalDate.parse(row.get(0).toString(), formatter2);
            } catch (DateTimeParseException e ) {
                dontSave = true;
                
            }
            String parentEmail = "0";

            try {
                parentEmail = row.get(42).toString();

            }catch (IndexOutOfBoundsException e) {
                dontSave = true;
            }



            String progType = null;
            LocalDateTime dateFrom = null;
            String partyPlace = null ;
            Integer idPartyPlace = null ;
            String partyType = null ;
            Integer idPartyType=null;
            String duration = null;
            Integer idCancellationType = null;
            Integer idProgType = null;

            try {
                String cancellationReasonName = row.get(1).toString();

                idCancellationType = switch (cancellationReasonName.toLowerCase()) {

                    case "bolezen" -> 1;
                    case "sprememba programa" -> 2;
                    case "brez razloga" -> 3;
                    case "drugo" -> 4;
                    case "covid" -> 5;
                    case "karantena" -> 6;
                    default -> 0;
                };

                LocalDate date;

                try {
                    date = LocalDate.parse(row.get(8).toString(), formatterDMYOnly);
                }catch (DateTimeParseException e) {

                    date = LocalDate.of(1999,1,1);
                }
                LocalTime startTime;

                try {
                    startTime = LocalTime.parse(row.get(10).toString());
                }catch (DateTimeParseException e) {
                    startTime = LocalTime.of(0,0,0);
                }

                dateFrom = LocalDateTime.of(date,startTime);

                progType = row.get(13).toString();

                idProgType = switch (progType.toLowerCase().trim()) {

                    case "jump" -> 1;
                    case "super jump" -> 2;
                    case "mini woop!" -> 3;
                    case "tiktok jump" -> 4;
                    case "lov na zaklad tematski" -> 5;
                    case "lov na zaklad črke" -> 6;
                    case "lov na zaklad simboli" -> 7;
                    case "karting & jump kombo" -> 8;
                    case "fun walls" -> 9;
                    case "kombo po meri" -> 10;
                    case "mini fiesta" -> 11;
                    case "maxi fiesta" -> 12;
                    case "glow golf" -> 13;
                    case "karting & glow golf kombo" -> 14;
                    case "karting & bowling kombo" -> 15;
                    case "laser tag" -> 16;
                    case "bowling" -> 17;
                    case "bowling odrasli" -> 18;
                    case "cosmic bowling" -> 19;
                    case "er" -> 20;
                    case "escape room" -> 20;
                    case "vr zabava" -> 21;
                    case "bowling & laser tag kombo" -> 22;
                    case "bowling & vr kombo" -> 23;
                    case "izzivi" -> 24;
                    case "kombo vr & lt" -> 25;
                    case "kombo glow golf + jump" -> 26;
                    case "kombo fw + tp" -> 27;
                    default -> 0;
                };

                partyType = row.get(14).toString();

                idPartyType = switch (partyType.toUpperCase().trim()) {

                    case "LNZ ČAROBNI GOZD" -> 1;
                    case "LNZ PODVODNI SVET" -> 2;
                    case "LNZ PIRATI" -> 3;
                    case "LNZ ČRKE RUMENA" -> 4;
                    case "LNZ ČRKE MODRA" -> 5;
                    case "LNZ ČRKE ROZA" -> 6;
                    case "LNZ SIMBOLI RUMENA" -> 7;
                    case "LNZ SIMBOLI MODRA" -> 8;
                    case "LNZ SIMBOLI ROZA" -> 9;
                    case "LT2 + VR" -> 10;
                    case "VR 2 + LT" -> 11;
                    case "WOOPATLON" -> 12;
                    case "LNZ NINJA TRENING" -> 13;
                    case "LNZ NINJE" -> 14;
                    case "LNZ SKRITI AGENTI" -> 15;
                    case "LNZ VESOLJCI" -> 16;
                    default -> 0;
                };

                duration = row.get(15).toString();
                partyPlace = row.get(28).toString();

                idPartyPlace = switch (partyPlace.toUpperCase().trim()){


                    case "SVETLO MODRA" -> 3;
                    case "TEMNO MODRA" -> 4;
                    case "ROZA" -> 5;
                    case "VIJOLIČNA" -> 6;
                    case "RUMENA" -> 8;
                    case "BELA" -> 9;
                    case "MINIWOOP" -> 23;
                    case "DPLACE" -> 24;
                    case "SILVERSTONE SOBA" -> 397;
                    case "MONACO SOBA" -> 32;
                    case "DAYTONA SOBA" -> 33;
                    case "TERARI 1" -> 103;
                    case "VIP 1" -> 104;
                    case "ER 1" -> 105;
                    case "TERARI 2" -> 106;
                    case "VIP 2" -> 107;
                    case "ER 2" -> 108;
                    case "VR" -> 109;
                    case "VIP 3" -> 110;
                    case "LT" -> 111;
                    case "LT 2" -> 112;
                    case "VIP 4" -> 113;
                    case "PARTY PROSTOR 1" -> 303;
                    case "PARTY PROSTOR 2" -> 304;
                    case "PARTY PROSTOR 3" -> 305;
                    case "PARTY PROSTOR 4" -> 306;
                    case "PARTY SOBA 1" -> 203;
                    case "PARTY SOBA 2" -> 204;
                    case "PARTY SOBA 3" -> 205;
                    default -> 0;
                }; }catch (IndexOutOfBoundsException e) {
                dontSave = true;
            }

            String childName = null;
            String childSurname = null;
            try {
                childName = row.get(36).toString();
                childSurname = row.get(37).toString();

            }catch (IndexOutOfBoundsException e){
                dontSave = true;
            }

            Integer participantCount;

            try {
                participantCount = Integer.parseInt(row.get(38).toString());
            }catch (NumberFormatException  | IndexOutOfBoundsException e) {
                participantCount = 0;
            }
            String parentName = null;
            String parentSurname = null;
            try {
                parentName = row.get(44).toString();
                parentSurname = row.get(45).toString();
            }catch (IndexOutOfBoundsException e){
                dontSave = true;
            }

            Integer idResType;

            try {
                idResType = switch (row.get(47).toString().toLowerCase().trim()) {
                    case "splet" -> -1;
                    case "recepcija" -> 1;
                    case "telefon" -> 2;
                    case "email" -> 3;
                    default -> 0;
                };
            } catch (IndexOutOfBoundsException e){
                idResType = 0;
            }

            Integer age ;

            try {
                age = Integer.parseInt(row.get(39).toString());
            }catch (NumberFormatException | IndexOutOfBoundsException e) {
                age = 0;
            }


            Birthdays bday = new Birthdays();

            bday.setIdBDayProgType(idProgType);
            bday.setBirthdayProgType(progType);
            bday.setActive(2);
            bday.setIdLocation(idLocation);
            bday.setDateFrom(dateFrom);
            bday.setDateCanceled(dateCancellation);
            bday.setIdPartyPlaceName(partyPlace);
            bday.setIdPlace(idPartyPlace);
            bday.setParticipantCount(participantCount);
            bday.setBirthdayPartyType(partyType);
            bday.setIdBDayPartyType(idPartyType);
            bday.setDuration(duration);
            bday.setChildBDayAge(age);
            bday.setParentEmail(parentEmail);
            bday.setParentFirstName(parentName);
            bday.setParentLastName(parentSurname);
            bday.setChildFirstName(childName);
            bday.setChildLastName(childSurname);
            bday.setIdContactResType(idResType);
            bday.setIdCancelationType(idCancellationType);

            String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

            if(parentEmail.matches(regex) && !dontSave) {

                if(birthdaysRepository.findByParentEmailAndDateFromAndChildFirstNameAndIdLocation(parentEmail,dateFrom,childName,idLocation).isEmpty()) {
                    birthdaysRepository.save(bday);
                }

            }


        }



    }
}
