package com.simplemethod.automotiverepairshops;

import com.simplemethod.automotiverepairshops.DataModel.Cars;
import com.simplemethod.automotiverepairshops.DataModel.CarsParts;
import com.simplemethod.automotiverepairshops.DataModel.Persons;
import org.neo4j.driver.*;
import org.neo4j.driver.exceptions.NoSuchRecordException;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.*;

import static org.neo4j.driver.Values.parameters;

@Component
public class Neo4j {
    private final Driver driver = GraphDatabase.driver("bolt://localhost:7687/neo4j", AuthTokens.basic("neo4j", "technologie"));


    public void close() throws Exception {
        driver.close();
    }


    /**
     * Zwraca listę samochodów według statusu naprawy.
     *
     * @param statusNaprawy status naprawy.
     * @return Lista obiektów z samocohdami.
     */
    public List<Cars> findCarsByStatusNaprawy(final Integer statusNaprawy) {
        try (Session session = driver.session()) {
            final List<Cars> carsArrayList = new ArrayList<>();
            return session.readTransaction(tx -> {
                final Result result = tx.run("MATCH (s:Samochod) WHERE s.statusnaprawy=$statusnaprawy RETURN s.name, s.marka, s.model, s.statusnaprawy, s.usterka", parameters("statusnaprawy", statusNaprawy));
                while (result.hasNext()) {
                    carsArrayList.add(new Cars(
                            result.peek().get(0).asString(),
                            result.peek().get(1).asString(),
                            result.peek().get(2).asString(),
                            result.peek().get(3).asInt(),
                            result.peek().get(4).asString()
                    ));
                    result.next();
                }
                return carsArrayList;
            });
        }
    }

    /**
     * Zwraca listę z wszystkimi samochodami.
     *
     * @return Lista obiektów z samocohdami.
     */
    public List<Cars> findAllCars() {
        try (Session session = driver.session()) {
            final List<Cars> names = new ArrayList<>();
            return session.readTransaction(tx -> {
                final Result result = tx.run("MATCH (s:Samochod) RETURN s.name, s.marka, s.model, s.statusnaprawy, s.usterka");
                while (result.hasNext()) {
                    names.add(new Cars(
                            result.peek().get(0).asString(),
                            result.peek().get(1).asString(),
                            result.peek().get(2).asString(),
                            result.peek().get(3).asInt(),
                            result.peek().get(4).asString()
                    ));
                    result.next();
                }
                return names;
            });
        }
    }

    /**
     * Zwraca listę wszystkich osób.
     *
     * @return Lista obiektów z osobami.
     */
    public List<Persons> findAllPersons() {
        try (Session session = driver.session()) {
            final List<Persons> personsArrayList = new ArrayList<>();
            return session.readTransaction(tx -> {
                final Result result = tx.run("MATCH (o:Osoba) RETURN o.name, o.pracownik, o.telefon");
                while (result.hasNext()) {
                    personsArrayList.add(new Persons(
                            result.peek().get(0).asString(),
                            result.peek().get(1).asInt(),
                            result.peek().get(2).asLong()));
                    result.next();
                }
                return personsArrayList;
            });
        }
    }

    /**
     * Pobieranie wszystkich części.
     *
     * @return Lista obiektów jako częśći.
     */
    public List<CarsParts> findAllParts() {
        try (Session session = driver.session()) {
            final List<CarsParts> carsPartsArrayList = new ArrayList<>();
            return session.readTransaction(tx -> {
                final Result result = tx.run("MATCH (c:Czesc) RETURN c.name, c.kategoria, c.cena");
                while (result.hasNext()) {
                    carsPartsArrayList.add(new CarsParts(
                            result.peek().get(0).asString(),
                            result.peek().get(1).asString(),
                            result.peek().get(2).asInt()));
                    result.next();
                }
                return carsPartsArrayList;
            });
        }
    }

    /**
     * Zwraca listę wszystkich pracowników.
     *
     * @return Lista obiektów z pracownikami.
     */
    public List<Persons> findAllPersonsByEmployees() {
        try (Session session = driver.session()) {
            final List<Persons> personsArrayList = new ArrayList<>();
            return session.readTransaction(tx -> {
                final Result result = tx.run("MATCH (o:Osoba) WHERE  o.pracownik=1  RETURN o.name, o.pracownik, o.telefon ORDER BY o.name");
                while (result.hasNext()) {
                    personsArrayList.add(new Persons(
                            result.peek().get(0).asString(),
                            result.peek().get(1).asInt(),
                            result.peek().get(2).asLong()));
                    result.next();
                }
                return personsArrayList;
            });
        }
    }

    /**
     * Zwraca wszystkie samochody naprawiane przez pracownika.
     *
     * @param pracownik Dane personalne pracownika
     * @return Liste obiektów samocchodów
     */
    public List<Cars> findAllCarsByEmployees(String pracownik) {
        try (Session session = driver.session()) {
            final List<Cars> names = new ArrayList<>();
            return session.readTransaction(tx -> {
                final Result result = tx.run("MATCH (p:Osoba { name: $pracownik })-[:NAPRAWIA]-(Samochod) RETURN Samochod.name, Samochod.marka, Samochod.model, Samochod.statusnaprawy, Samochod.usterka", parameters("pracownik", pracownik));
                while (result.hasNext()) {
                    names.add(new Cars(
                            result.peek().get(0).asString(),
                            result.peek().get(1).asString(),
                            result.peek().get(2).asString(),
                            result.peek().get(3).asInt(),
                            result.peek().get(4).asString())
                    );
                    result.next();
                }
                return names;
            });
        }
    }


    /**
     * Zwraca wszystkie cześci potrzebne do samochodu.
     *
     * @param numerRejestracyjny Numer rejestracyjny.
     * @return Liste obiektów części
     */
    public List<CarsParts> findAllPartsToCars(String numerRejestracyjny) {
        try (Session session = driver.session()) {
            final List<CarsParts> carsPartsArrayList = new ArrayList<>();
            return session.readTransaction(tx -> {
                final Result result = tx.run("MATCH (s:Samochod { name: $nr })-[:WYMAGA]-(Czesc) RETURN Czesc.name, Czesc.kategoria, Czesc.cena", parameters("nr", numerRejestracyjny));
                while (result.hasNext()) {
                    carsPartsArrayList.add(new CarsParts(
                            result.peek().get(0).asString(),
                            result.peek().get(1).asString(),
                            result.peek().get(2).asInt()));
                    result.next();
                }
                return carsPartsArrayList;
            });
        }
    }

    /**
     * Zmiana statusu naprawy samochodu.
     *
     * @param numerRejestracyjny Numer rejestracyjny pojazdu.
     */
    public void setStatusNaprawyByName(String numerRejestracyjny) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("MATCH (s:Samochod {name:$nr}) SET s.statusnaprawy=1", parameters("nr", numerRejestracyjny));
                return 1;
            });
        }
    }

    /**
     * Dodanie nowej części.
     *
     * @param nazwa     Nazwa części.
     * @param kategoria Kategoria części.
     * @param cena      Cena części.
     */
    public void saveCarsParts(String nazwa, String kategoria, Integer cena) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("nazwa", nazwa);
        parameters.put("kategoria", kategoria);
        parameters.put("cena", cena);
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("CREATE (:Czesc {cena: $cena, kategoria:$kategoria, name:$nazwa})", parameters);
                return 1;
            });
        }
    }

    /**
     * Dodanie nowej osoby.
     *
     * @param nazwa     Imie i nazwisko osoby.
     * @param telefon   Telefon osoby.
     * @param pracownik Czy osoba jest pracownikiem 0/1.
     */
    public void savePerson(String nazwa, Integer telefon, Integer pracownik) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("nazwa", nazwa);
        parameters.put("telefon", telefon);
        parameters.put("pracownik", pracownik);
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("CREATE (:Osoba {name:$nazwa, pracownik:$pracownik, telefon:$telefon})", parameters);
                return 1;
            });
        }
    }

    /**
     * Dodanie nowego samochodu.
     *
     * @param numerRejestracyjny Numer rejestracyjny samochodu.
     * @param marka              Marka samochodu.
     * @param model              Model samochodu.
     * @param userka             Usterka.
     */
    public void saveCars(String numerRejestracyjny, String marka, String model, String userka) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("nr", numerRejestracyjny);
        parameters.put("marka", marka);
        parameters.put("model", model);
        parameters.put("userka", userka);
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("CREATE (:Samochod {marka:$marka, model:$model, statusnaprawy:0, usterka: $userka, name:$nr})", parameters);
                return 1;
            });
        }
    }

    /**
     * Usuwanie samochodu z bazy danych.
     *
     * @param numerRejestracyjny Numer rejestracyjny samochodu.
     */
    public void removeCars(String numerRejestracyjny) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("MATCH  (s:Samochod {name:$nr}) DETACH DELETE s ", parameters("nr", numerRejestracyjny));
                return 1;
            });
        }
    }

    /**
     * Tworzenie relacji między danymi.
     *
     * @param numerRejestracyjny Numer rejestracyjny.
     * @param pracownik          Imie i nazwisko pracownika.
     * @param wlasciciel         Imie i nazwisko wlasciciela.
     * @param czesci             Tablica czesci do wymiany.
     */
    public void setCarsWithRelationships(String numerRejestracyjny, String pracownik, String wlasciciel, String[] czesci) {
        List<Cars> carsList = findCarsByName(numerRejestracyjny);
        List<Persons> employeeList = findEmployeeByName(pracownik);
        List<Persons> ownerList = findOwnerByName(wlasciciel);
        List<CarsParts> carsParts = new ArrayList<>();
        for (String s : czesci) {
            carsParts.add(findPartsByName(s));
        }
        if (carsList.isEmpty() || employeeList.isEmpty() || ownerList.isEmpty() || carsParts.isEmpty()) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Brak lub błędne dane|@"));
            return;
        }
        setCarAndOwnerRelationship(numerRejestracyjny,pracownik,wlasciciel);
        for (String s : czesci) {
            setEmployeeAndCarPartsRelationship(numerRejestracyjny,pracownik,s);
       }

    }

    /**
     * Ustawienie relacji pojazdu.
     * @param numerRejestracyjny Numer rejestracyjny pojazdu.
     * @param pracownik Imie i nazwisko pracownika.
     * @param wlasciciel Imie i nazwisko wlasciciela.
     */
    private void setCarAndOwnerRelationship(String numerRejestracyjny, String pracownik, String wlasciciel)
    {
        List<Cars> carsList = findCarsByName(numerRejestracyjny);
        List<Persons> employeeList = findEmployeeByName(pracownik);
        List<Persons> ownerList = findOwnerByName(wlasciciel);
        if(carsList.isEmpty() || employeeList.isEmpty() || ownerList.isEmpty())
        {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Brak lub błędne dane|@"));
            return;
        }
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("nr", numerRejestracyjny);
        parameters.put("pracownik", pracownik);
        parameters.put("wlasciciel", wlasciciel);
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("MATCH  (s:Samochod {name:$nr}) MATCH  (w:Osoba {name:$wlasciciel}) MATCH  (p:Osoba {name:$pracownik}) CREATE (p)-[:NAPRAWIA]->(s), (w)-[:WLASCICIEL]->(s)", parameters);
                return 1;
            });
        }
    }

    /**
     * Tworzenie relacji z cześciami do wymiany.
     * @param numerRejestracyjny Numer rejestracyjny samochodu.
     * @param pracownik Pracownik.
     * @param czesc Część do wymiany.
     */
    private void setEmployeeAndCarPartsRelationship(String numerRejestracyjny, String pracownik, String czesc)
    {
        List<Cars> carsList = findCarsByName(numerRejestracyjny);
        List<Persons> employeeList = findEmployeeByName(pracownik);
        CarsParts carsParts = findPartsByName(czesc);
        if(carsList.isEmpty() || employeeList.isEmpty() || Objects.requireNonNull(carsParts).name == null)
        {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Brak lub błędne dane|@"));
            return;
        }
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("nr", numerRejestracyjny);
        parameters.put("pracownik", pracownik);
        parameters.put("czesc", czesc);
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("MATCH  (s:Samochod {name:$nr}) MATCH  (p:Osoba {name:$pracownik}) MATCH  (c:Czesc {name:$czesc}) CREATE (s)-[:WYMAGA]->(c), (p)-[:WYMIENIA]->(c)", parameters);
                return 1;
            });
        }
    }

    /**
     * Zwraca listę samochód o podanym numerze rejestracyjnym.
     *
     * @param numerRejestracyjny Numer rejestracyjny.
     * @return Listę obiektów z samochodami.
     */
    private List<Cars> findCarsByName(String numerRejestracyjny) {
        try (Session session = driver.session()) {
            final List<Cars> names = new ArrayList<>();
            return session.readTransaction(tx -> {
                final Result result = tx.run("MATCH (s:Samochod) WHERE s.name=$name RETURN s.name, s.marka, s.model, s.statusnaprawy, s.usterka LIMIT 1", parameters("name", numerRejestracyjny));
                while (result.hasNext()) {
                    names.add(new Cars(
                            result.peek().get(0).asString(),
                            result.peek().get(1).asString(),
                            result.peek().get(2).asString(),
                            result.peek().get(3).asInt(),
                            result.peek().get(4).asString()
                    ));
                    result.next();
                }
                return names;
            });
        }
    }


    /**
     * Zwraca cześci według nazwy.
     *
     * @param partName Nazwa częsci.
     * @return Listę obiektów części.
     */
    private CarsParts findPartsByName(String partName) {
        try (Session session = driver.session()) {
            final List<CarsParts> carsPartsArrayList = new ArrayList<>();
            return session.readTransaction(tx -> {
                final Result result = tx.run("MATCH (c:Czesc) WHERE c.name=$name RETURN c.name, c.kategoria, c.cena LIMIT 1", parameters("name", partName));
                return new CarsParts(result.peek().get(0).asString(), result.peek().get(1).asString(), result.peek().get(2).asInt());
            });
        } catch (NoSuchRecordException e) {
            return null;
        }
    }


    /**
     * Zwraca wlasciciela o podanych danych.
     *
     * @param name Imie i nazwisko wlasciciela.
     * @return Liste z obiektem wlasciciela.
     */
    private List<Persons> findOwnerByName(String name) {
        try (Session session = driver.session()) {
            final List<Persons> personsArrayList = new ArrayList<>();
            return session.readTransaction(tx -> {
                final Result result = tx.run("MATCH (o:Osoba) WHERE  o.pracownik=0 AND o.name=$name RETURN o.name, o.pracownik, o.telefon ORDER BY o.name", parameters("name", name));
                while (result.hasNext()) {
                    personsArrayList.add(new Persons(
                            result.peek().get(0).asString(),
                            result.peek().get(1).asInt(),
                            result.peek().get(2).asLong()));
                    result.next();
                }
                return personsArrayList;
            });
        }
    }

    /**
     * Zwraca prcownika o podanych danych.
     *
     * @param name Imie i nazwisko pracownika.
     * @return Liste z jednym obiektem pracownika.
     */
    private List<Persons> findEmployeeByName(String name) {
        try (Session session = driver.session()) {
            final List<Persons> personsArrayList = new ArrayList<>();
            return session.readTransaction(tx -> {
                final Result result = tx.run("MATCH (o:Osoba) WHERE  o.pracownik=1 AND o.name=$name RETURN o.name, o.pracownik, o.telefon ORDER BY o.name LIMIT 1", parameters("name", name));
                while (result.hasNext()) {
                    personsArrayList.add(new Persons(
                            result.peek().get(0).asString(),
                            result.peek().get(1).asInt(),
                            result.peek().get(2).asLong()));
                    result.next();
                }
                return personsArrayList;
            });
        }
    }

    /**
     * Zwraca dane mechanika naprawiajacy samochód.
     *
     * @param numerRejestracyjny Numer rejestracyjny samochodu.
     * @return String z imieniem i nazwiskiem mechanika.
     */
    private String findEmployeeByCar(String numerRejestracyjny) {
        try (Session session = driver.session()) {

            return session.readTransaction(tx -> {
                final Result result = tx.run("MATCH (s:Samochod { name: nr })<-[:NAPRAWIA]-(Osoba) RETURN Osoba.name LIMIT 1", parameters("nr", numerRejestracyjny));
                return result.single().get(0).asString();
            });
        }
    }
}
