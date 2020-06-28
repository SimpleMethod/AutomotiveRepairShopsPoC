package com.simplemethod.automotiverepairshops;

import com.arangodb.*;
import com.arangodb.entity.*;
import com.arangodb.ArangoDB;
import com.arangodb.entity.BaseDocument;
import com.arangodb.model.EdgeCreateOptions;
import com.simplemethod.automotiverepairshops.DataModel.Cars;
import com.simplemethod.automotiverepairshops.DataModel.CarsParts;
import com.simplemethod.automotiverepairshops.DataModel.Persons;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class Arango {
    ArangoDB arangoDB = new ArangoDB.Builder().user("root").password("technologiepsk").build();

    /**
     * Tworzenie nowej bazy danych.
     */
    public void createDatabase() {
        String dbName = "technolgie";
        try {
            arangoDB.createDatabase(dbName);
        } catch (ArangoDBException e) {
            System.err.println("Blad dodawania bazy danych:" + dbName + "; " + e.getMessage());
        }
    }

    /**
     * Tworzenie kolekcji do gromadzenia danych.
     */
    public void createCollection() {
        try {
            CollectionEntity samochodCollection = arangoDB.db("technolgie").createCollection("samochod");
            CollectionEntity osobaCollection = arangoDB.db("technolgie").createCollection("osoba");
            CollectionEntity czescCollection = arangoDB.db("technolgie").createCollection("czesc");

        } catch (ArangoDBException e) {

        }
    }

    /**
     * Dodanie nowych krawędzi.
     */
    public void createEdge() {
        final Collection<EdgeDefinition> edgeDefinitions = new ArrayList<>();
        edgeDefinitions.add(new EdgeDefinition().collection("NAPRAWIA").from("osoba").to("samochod"));
        edgeDefinitions.add(new EdgeDefinition().collection("WYMAGA").from("samochod").to("czesc"));
        edgeDefinitions.add(new EdgeDefinition().collection("WLASCICIEL").from("osoba").to("samochod"));
        arangoDB.db("technolgie").createGraph("technolgie", edgeDefinitions, null);
    }

    /**
     * Zwraca wszystkie cześci potrzebne do samochodu.
     *
     * @param numerRejestracyjny Numer rejestracyjny.
     * @return Liste obiektów części
     */
    public List<CarsParts> findAllPartsToCars(String numerRejestracyjny) {
        final List<CarsParts> carsArrayList = new ArrayList<>();
        try {
            String query = "FOR t IN WYMAGA RETURN t";
            ArangoCursor<BaseEdgeDocument> cursor = arangoDB.db("technolgie").query(query, null, null, BaseEdgeDocument.class);
            cursor.forEachRemaining(aDocument -> {
                if (aDocument.getFrom().toString().replace("samochod/", "").equals(numerRejestracyjny)) {
                    carsArrayList.add(findPartByName((aDocument.getTo().toString().replace("czesc/", ""))));
                }
            });
        } catch (ArangoDBException e) {
            System.err.println("Problem z wykonaniem zapytania " + e.getMessage());
        }
        return carsArrayList;
    }

    /**
     * Usuwanie samochodu z bazy danych.
     *
     * @param numerRejestracyjny Numer rejestracyjny samochodu.
     */
    public void removeCars(String numerRejestracyjny) {
        try {
            arangoDB.db("technolgie").collection("samochod").deleteDocument(numerRejestracyjny);
        } catch (ArangoDBException e) {
            System.err.println("Failed to delete document. " + e.getMessage());
        }
    }

    /**
     * Zmiana statusu naprawy samochodu.
     *
     * @param numerRejestracyjny Numer rejestracyjny pojazdu.
     */
    public void setStatusNaprawyByName(String numerRejestracyjny) {
        final BaseDocument myObject = new BaseDocument();
        myObject.addAttribute("statusNaprawy", 1);
        try {
            arangoDB.db("technolgie").collection("samochod").updateDocument(numerRejestracyjny, myObject);
        } catch (ArangoDBException e) {
            System.err.println("Failed to update document. " + e.getMessage());
        }
    }

    /**
     * Zwraca wszystkie samochody naprawiane przez pracownika.
     *
     * @param pracownik Dane personalne pracownika
     * @return Liste obiektów samocchodów
     */
    public List<Cars> findAllCarsByEmployees(String pracownik) {
        final List<Cars> carsArrayList = new ArrayList<>();
        try {
            String query = "FOR t IN NAPRAWIA RETURN t";
            ArangoCursor<BaseEdgeDocument> cursor = arangoDB.db("technolgie").query(query, null, null, BaseEdgeDocument.class);
            cursor.forEachRemaining(aDocument -> {
                if (aDocument.getFrom().replace("osoba/", "").equals(pracownik)) {
                    carsArrayList.add(findCarsByName(aDocument.getTo().replace("samochod/", "")));
                }
            });
        } catch (ArangoDBException e) {
            System.err.println("Problem z wykonaniem zapytania " + e.getMessage());
        }
        return carsArrayList;
    }

    /**
     * Zwraca listę samochodów według statusu naprawy.
     *
     * @param statusNaprawy status naprawy.
     * @return Lista obiektów z samocohdami.
     */
    public List<Cars> findCarsByStatusNaprawy(final Integer statusNaprawy) {
        final List<Cars> carsArrayList = new ArrayList<>();
        try {
            final String query = String.format("FOR t IN samochod FILTER t.statusNaprawy == %1$s RETURN t", statusNaprawy);
            ArangoCursor<BaseDocument> cursor = arangoDB.db("technolgie").query(query, null, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                carsArrayList.add(new Cars(
                        aDocument.getAttribute("nazwa").toString(),
                        aDocument.getAttribute("marka").toString(),
                        aDocument.getAttribute("model").toString(),
                        Integer.parseInt(aDocument.getAttribute("statusNaprawy").toString()),
                        aDocument.getAttribute("userka").toString()

                ));
            });
        } catch (ArangoDBException e) {
            System.err.println("Problem z wykonaniem zapytania " + e.getMessage());
        }
        return carsArrayList;
    }

    /**
     * Pobieranie wszystkich części.
     *
     * @return Lista obiektów jako częśći.
     */
    public CarsParts findPartByName(final String nazwaCzesci) {
        final List<CarsParts> carsArrayList = new ArrayList<>();
        try {
            final String query = String.format("FOR t IN czesc FILTER t.nazwa == '%1$s' RETURN t", nazwaCzesci);
            ArangoCursor<BaseDocument> cursor = arangoDB.db("technolgie").query(query, null, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                carsArrayList.add(new CarsParts(
                        aDocument.getAttribute("nazwa").toString(),
                        aDocument.getAttribute("kategoria").toString(),
                        Integer.parseInt(aDocument.getAttribute("cena").toString())
                ));
            });
        } catch (ArangoDBException e) {
            System.err.println("Problem z wykonaniem zapytania " + e.getMessage());
        }
        return carsArrayList.get(0);
    }

    /**
     * Zwraca obiekt samochodów według numeru rejestracyjnego.
     *
     * @param numerRejestracyjny numer rejestracyjny.
     * @return obiektów z samochodem.
     */
    public Cars findCarsByName(final String numerRejestracyjny) {
        final List<Cars> carsArrayList = new ArrayList<>();
        try {
            final String query = String.format("FOR t IN samochod FILTER t.nazwa == '%1$s' RETURN t", numerRejestracyjny);
            ArangoCursor<BaseDocument> cursor = arangoDB.db("technolgie").query(query, null, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                carsArrayList.add(new Cars(
                        aDocument.getAttribute("nazwa").toString(),
                        aDocument.getAttribute("marka").toString(),
                        aDocument.getAttribute("model").toString(),
                        Integer.parseInt(aDocument.getAttribute("statusNaprawy").toString()),
                        aDocument.getAttribute("userka").toString()

                ));
            });
        } catch (ArangoDBException e) {
            System.err.println("Problem z wykonaniem zapytania " + e.getMessage());
        }
        return carsArrayList.get(0);
    }

    /**
     * Zwraca listę wszystkich pracowników.
     *
     * @return Lista obiektów z pracownikami.
     */
    public List<Persons> findAllPersonsByEmployees() {
        final List<Persons> personsArrayList = new ArrayList<>();
        try {
            String query = "FOR t IN osoba FILTER t.pracownik == 1 RETURN t";
            ArangoCursor<BaseDocument> cursor = arangoDB.db("technolgie").query(query, null, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                personsArrayList.add(new Persons(
                        aDocument.getAttribute("nazwa").toString(),
                        Integer.parseInt(aDocument.getAttribute("pracownik").toString()),
                        Long.parseLong(aDocument.getAttribute("telefon").toString())
                ));
            });
        } catch (ArangoDBException e) {
            System.err.println("Problem z wykonaniem zapytania " + e.getMessage());
        }
        return personsArrayList;
    }

    /**
     * Zwraca listę wszystkich osób.
     *
     * @return Lista obiektów z osobami.
     */
    public List<Persons> findAllPersons() {
        final List<Persons> personsArrayList = new ArrayList<>();
        try {
            String query = "FOR t IN osoba RETURN t";
            ArangoCursor<BaseDocument> cursor = arangoDB.db("technolgie").query(query, null, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                personsArrayList.add(new Persons(
                        aDocument.getAttribute("nazwa").toString(),
                        Integer.parseInt(aDocument.getAttribute("pracownik").toString()),
                        Long.parseLong(aDocument.getAttribute("telefon").toString())
                ));
            });
        } catch (ArangoDBException e) {
            System.err.println("Problem z wykonaniem zapytania " + e.getMessage());
        }
        return personsArrayList;
    }

    /**
     * Pobieranie wszystkich części.
     *
     * @return Lista obiektów jako częśći.
     */
    public List<CarsParts> findAllParts() {
        final List<CarsParts> carsArrayList = new ArrayList<>();
        try {
            String query = "FOR t IN czesc RETURN t";
            ArangoCursor<BaseDocument> cursor = arangoDB.db("technolgie").query(query, null, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                carsArrayList.add(new CarsParts(
                        aDocument.getAttribute("nazwa").toString(),
                        aDocument.getAttribute("kategoria").toString(),
                        Integer.parseInt(aDocument.getAttribute("cena").toString())
                ));
            });
        } catch (ArangoDBException e) {
            System.err.println("Problem z wykonaniem zapytania " + e.getMessage());
        }
        return carsArrayList;
    }

    /**
     * Zwraca listę z wszystkimi samochodami.
     *
     * @return Lista obiektów z samocohdami.
     */
    public List<Cars> findAllCars() {
        final List<Cars> carsArrayList = new ArrayList<>();
        try {
            String query = "FOR t IN samochod RETURN t";
            ArangoCursor<BaseDocument> cursor = arangoDB.db("technolgie").query(query, null, null, BaseDocument.class);
            cursor.forEachRemaining(aDocument -> {
                carsArrayList.add(new Cars(
                        aDocument.getAttribute("nazwa").toString(),
                        aDocument.getAttribute("marka").toString(),
                        aDocument.getAttribute("model").toString(),
                        Integer.parseInt(aDocument.getAttribute("statusNaprawy").toString()),
                        aDocument.getAttribute("userka").toString()

                ));
            });
        } catch (ArangoDBException e) {
            System.err.println("Problem z wykonaniem zapytania " + e.getMessage());
        }
        return carsArrayList;
    }

    /**
     * Tworzenie krawędzi między pracownikiem, a pojazdem.
     *
     * @param numerRejestracyjny Numer rejestracyjny pojazdu.
     * @param pracownik          Imie i nazwisko pracownika.
     */
    public void setEmployeeAndCarRelationship(String numerRejestracyjny, String pracownik) {
        ArangoGraph graph = arangoDB.db("technolgie").graph("technolgie");
        ArangoEdgeCollection collection = graph.edgeCollection("NAPRAWIA");
        BaseEdgeDocument document = new BaseEdgeDocument(pracownik + numerRejestracyjny, "osoba/" + pracownik, "samochod/" + numerRejestracyjny);
        collection.insertEdge(document, new EdgeCreateOptions());
    }

    /**
     * Tworzenie krawędzi między wlascicielem, a pojazdem.
     *
     * @param numerRejestracyjny Numer rejestracyjny pojazdu.
     * @param wlasciciel         Imie i nazwisko pracownika.
     */
    public void setOwnerAndCarRelationship(String numerRejestracyjny, String wlasciciel) {
        ArangoGraph graph = arangoDB.db("technolgie").graph("technolgie");
        ArangoEdgeCollection collection = graph.edgeCollection("WLASCICIEL");
        BaseEdgeDocument document = new BaseEdgeDocument(wlasciciel + numerRejestracyjny, "osoba/" + wlasciciel, "samochod/" + numerRejestracyjny);
        collection.insertEdge(document, new EdgeCreateOptions());
    }

    /**
     * Tworzenie krawędzi między samochodem, a częścią do wymiany.
     *
     * @param numerRejestracyjny Numer rejestracyjny pojazdu.
     * @param czesc              Część do wymiany.
     */
    public void setCarAndCarPartsRelationship(String numerRejestracyjny, String czesc) {
        ArangoGraph graph = arangoDB.db("technolgie").graph("technolgie");
        ArangoEdgeCollection collection = graph.edgeCollection("WYMAGA");
        BaseEdgeDocument document = new BaseEdgeDocument(numerRejestracyjny + czesc, "samochod/" + numerRejestracyjny, "czesc/" + czesc);
        collection.insertEdge(document, new EdgeCreateOptions());
    }

    /**
     * Dodanie nowej części.
     *
     * @param nazwa     Nazwa części.
     * @param kategoria Kategoria części.
     * @param cena      Cena części.
     */
    public void saveCarsParts(String nazwa, String kategoria, Integer cena) {
        final BaseDocument myObject = new BaseDocument();
        myObject.setKey(nazwa.replaceAll("\\s+", ""));
        myObject.addAttribute("nazwa", nazwa);
        myObject.addAttribute("kategoria", kategoria);
        myObject.addAttribute("cena", cena);

        try {
            arangoDB.db("technolgie").collection("czesc").insertDocument(myObject);
        } catch (ArangoDBException e) {
            System.err.println("Problem z dodaniem danych: " + e.getMessage());
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
        final BaseDocument myObject = new BaseDocument();
        myObject.setKey(nazwa.replaceAll("\\s+", ""));
        myObject.addAttribute("nazwa", nazwa);
        myObject.addAttribute("telefon", telefon);
        myObject.addAttribute("pracownik", pracownik);
        try {
            arangoDB.db("technolgie").collection("osoba").insertDocument(myObject);
        } catch (ArangoDBException e) {
            System.err.println("Problem z dodaniem danych: " + e.getMessage());
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
        final BaseDocument myObject = new BaseDocument();
        myObject.setKey(numerRejestracyjny.replaceAll("\\s+", ""));
        myObject.addAttribute("nazwa", numerRejestracyjny);
        myObject.addAttribute("marka", marka);
        myObject.addAttribute("model", model);
        myObject.addAttribute("statusNaprawy", 0);
        myObject.addAttribute("userka", userka);
        try {
            arangoDB.db("technolgie").collection("samochod").insertDocument(myObject);
        } catch (ArangoDBException e) {
            System.err.println("Problem z dodaniem danych: " + e.getMessage());
        }
    }
}
