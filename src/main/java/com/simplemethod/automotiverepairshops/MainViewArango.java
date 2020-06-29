package com.simplemethod.automotiverepairshops;

import com.simplemethod.automotiverepairshops.DataModel.Cars;
import com.simplemethod.automotiverepairshops.DataModel.CarsParts;
import com.simplemethod.automotiverepairshops.DataModel.Persons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Controller
public class MainViewArango {

    @Autowired
    Arango arango;

    public void menu() throws IOException {

        for (; ; ) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(green) Menu Arango:|@"));
            System.out.print("\n");
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(green) Wybierz pozycję z menu:|@"));
            System.out.print("\n");
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [1]|@ Wyświetlanie wszystkich samochodów "));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [2]|@ Wyświetlanie wszystkich osób"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [3]|@ Wyświetlanie wszystkich pracowników"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [4]|@ Wyświetlanie wszystkich części"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [5]|@ Wyświetlanie ukończonych/nieukończonych samochodów"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [6]|@ Wyświetlanie samochodów naprawianych przez pracownika"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [7]|@ Wyświetlanie części potrzebnych do samochodu"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [8]|@ Zmiana statusu naprawy samochodu"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [9]|@ Usunięcie samochodu"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [10]|@ Dodanie nowej osoby"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [11]|@ Dodanie nowej czesci"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [12]|@ Dodanie nowego samochodu"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red)  [13]|@ Zamknięcie programu"));

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String input = br.readLine();
            if (isNumeric(input)) {
                int parseInt = Integer.parseInt(input);
                switch (parseInt) {
                    case 1:
                        findAllCars();
                        break;
                    case 2:
                        findAllPersons();
                        break;
                    case 3:
                        findAllEmployee();
                        break;
                    case 4:
                        findAllCarParts();
                        break;
                    case 5:
                        findCarsWithStatusNaprawy();
                        break;
                    case 6:
                        findCardByEmployee();
                        break;
                    case 7:
                        findAllCarPartsByCar();
                        break;
                    case 8:
                        setStatusNaprawy();
                        break;
                    case 9:
                        deleteCars();
                        break;
                    case 10:
                        addNewPerson();
                        break;
                    case 11:
                        addNewParts();
                        break;
                    case 12:
                        addNewCars();
                        break;
                    case 13:
                        Runtime.getRuntime().exit(1);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void findAllCars() {
        List<Cars> carsParts = arango.findAllCars();
        carsParts.forEach(rx -> System.out.println(rx.toString()));
    }

    private void findAllPersons() {
        List<Persons> personsList = arango.findAllPersons();
        personsList.forEach(rx -> System.out.println(rx.toString()));
    }

    private void findAllEmployee() {
        List<Persons> personsList = arango.findAllPersonsByEmployees();
        personsList.forEach(rx -> System.out.println(rx.toString()));
    }

    private void findAllCarParts() {
        List<CarsParts> carsParts = arango.findAllParts();
        carsParts.forEach(rx -> System.out.println(rx.toString()));
    }

    private void findCarsWithStatusNaprawy() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(white) Menu wyświetlania samochodów|@"));
        System.out.print("\n");
        System.out.println("Status ukończenia samochodu 0/1:");
        String statusNaprawy = br.readLine();
        if (isNumeric(statusNaprawy)) {
            List<Cars> carsParts = arango.findCarsByStatusNaprawy(Integer.parseInt(statusNaprawy));
            carsParts.forEach(rx -> System.out.println(rx.toString()));
        }
    }

    private void findCardByEmployee() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(white) Menu wyświetlania samochodow naprawianych przez pracownika|@"));
        System.out.print("\n");
        System.out.println("Imie i nazwisko pracownika:");
        String name = br.readLine();
        if (name == null) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Bledne dane !|@"));
            return;
        }
        List<Cars> carsParts = arango.findAllCarsByEmployees(name.replaceAll("\\s+", ""));
        carsParts.forEach(rx -> System.out.println(rx.toString()));
    }

    private void findAllCarPartsByCar() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(white) Menu wyświetlania części potrzebnych do naprawy |@"));
        System.out.print("\n");
        System.out.println("Numer rejestracyjny pojazdu:");
        String name = br.readLine();
        if (name == null) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Bledne dane !|@"));
            return;
        }
        List<CarsParts> carsParts = arango.findAllPartsToCars(name.replaceAll("\\s+", ""));
        carsParts.forEach(rx -> System.out.println(rx.toString()));
    }

    private void setStatusNaprawy() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(white) Menu zmiany statusu pojazdu |@"));
        System.out.print("\n");
        System.out.println("Numer rejestracyjny pojazdu:");
        String name = br.readLine();
        if (name == null) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Bledne dane !|@"));
            return;
        }
        arango.setStatusNaprawyByName(name.replaceAll("\\s+", ""));
    }

    private void deleteCars() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(white) Menu zmiany statusu pojazdu |@"));
        System.out.print("\n");
        System.out.println("Numer rejestracyjny pojazdu:");
        String name = br.readLine();
        if (name == null) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Bledne dane !|@"));
            return;
        }
        arango.removeCars(name.replaceAll("\\s+", ""));
    }

    private void addNewPerson() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(white) Menu dodawania nowej osoby |@"));
        System.out.print("\n");
        System.out.println("Imie:");
        String firstName = br.readLine();
        System.out.println("Nazwisko:");
        String secondName = br.readLine();
        System.out.println("Telefon:");
        String phoneNumber = br.readLine();
        System.out.println("Pracownik 0/1:");
        String employee = br.readLine();
        try {
            if (firstName == null || secondName == null || phoneNumber.isEmpty() || employee.isEmpty()) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
                return;
            }
        } catch (NullPointerException e) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
            return;
        }
        if (isNumeric(phoneNumber) && isNumeric(employee)) {
            if (employee.equals("0") || employee.equals("1")) {
                arango.savePerson((firstName + " " + secondName), Integer.parseInt(phoneNumber), Integer.parseInt(employee));
            } else {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Status pracownika musi być liczbą!|@"));
            }

        } else {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Numer telefonu być liczbą!|@"));
        }
    }

    private void addNewParts() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(white) Menu dodawania nowej czesci |@"));
        System.out.print("\n");
        System.out.println("Nazwa czesci:");
        String partName = br.readLine();
        System.out.println("Kategoria:");
        String category = br.readLine();
        System.out.println("Cena:");
        String price = br.readLine();
        try {
            if (partName == null || category == null || price.isEmpty()) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
                return;
            }
        } catch (NullPointerException e) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
            return;
        }
        if (isNumeric(price)) {
            arango.saveCarsParts(partName, category, Integer.parseInt(price));

        } else {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Numer telefonu być liczbą!|@"));
        }
    }

    private void addNewCars() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(white) Menu dodawania nowejgo samochodu |@"));
        System.out.print("\n");
        System.out.println("Numer rejestracyjny:");
        String plateNumber = br.readLine();
        System.out.println("Marka:");
        String manufacturer = br.readLine();
        System.out.println("Model:");
        String models = br.readLine();
        System.out.println("Usterka:");
        String defect = br.readLine();
        System.out.println("Pracownik:");
        String employee = br.readLine();
        System.out.println("Wlasciciel:");
        String owners = br.readLine();

        try {
            if (plateNumber == null || defect == null || employee == null || manufacturer == null || models == null || owners == null) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
                return;
            }
            arango.saveCars(plateNumber, manufacturer, models, defect);

        } catch (NullPointerException e) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
            return;
        }

        for (; ; ) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(white) Menu dodawania nowej czesci |@"));
            System.out.println("Nazwa czesci:");
            String newpart = br.readLine();
            System.out.println("Kolejna czesci 0/1 ?");
            String next = br.readLine();

            arango.setCarAndCarPartsRelationship(plateNumber.replaceAll("\\s+", ""), newpart.replaceAll("\\s+", ""));
            if (next.equals("0")) {
                break;
            }
        }

        arango.setEmployeeAndCarRelationship(plateNumber.replaceAll("\\s+", ""), employee.replaceAll("\\s+", ""));
        arango.setOwnerAndCarRelationship(plateNumber.replaceAll("\\s+", ""), owners.replaceAll("\\s+", ""));


    }

    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    /**
     * Checking if string is a number.
     *
     * @param strNum String to check.
     * @return True of false.
     */
    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

}
