package com.simplemethod.automotiverepairshops;


import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "university", mixinStandardHelpOptions = true, version = "1.0 dev")
public class MyCommand implements Runnable {

    @Autowired
    Neo4j neo4j;

    @Autowired
    Arango arango;

    @CommandLine.Option(names = {"-t", "--type"}, description = "Type of database N=neo4j A=Arango")
    private String[] type = new String[]{"A"};

    @SneakyThrows
    public void run() {

        if (type[0].equals("N") || type[0].equals("n")) {
            /*
            List<Cars> cars = neo4j.findAllCarsByEmployees("Jan Kowalki");
            for (Cars car : cars) {
                System.out.println("[T] " + car.toString());
            }

            List<Persons> persons = neo4j.findAllPersonsByEmployees();
            for (Persons person : persons) {
                System.out.println(person.toString());
            }
            */
            // neo4j.setStatusNaprawyByName("TK2144");
            //neo4j.saveCarsParts("Lampa Tylnia","Lampy",200);
            // neo4j.savePerson("Marcin Wydra",66148455,1);
            // neo4j.saveCars("TKN9997","Renault", "Megane", "Wtrysk paliwa");
            // neo4j.removeCars("TKN9997");
            neo4j.setCarsWithRelationships("TK2144", "Jan Kowalki", "Janusz Adamowski", new String[]{"Uszczelka pod glowice", "Silnik1.0TDI", "Akumulator 12V"});
        } else if (type[0].equals("A") || type[0].equals("a")) {
            // arango.savePerson("Andzej Propok",413742210,0);
            // arango.createCollection();
            //arango.createEdge();
            // arango.saveCars("TKN2444","BMW","E39","Uszczelka pod glowica");
            //arango.saveCarsParts("UszczelkaV12","Uczczelki",213);
            // arango.savePerson("Marcin Wydra",413742137,1);
            //arango.setEmployeeAndCarRelationship("TKN2444","MarcinWydra");
            //arango.setEmployeeAndCarRelationship("TKN2444","MarcinWydra");
            //arango.setOwnerAndCarRelationship("TKN2444","AndzejPropok");
            //arango.setCarAndCarPartsRelationship("TKN2444","UszczelkaV12");
            // List<Persons> persons = arango.findAllPersonsByEmployees();
            // persons.forEach(rx->System.out.println(rx.toString()));
             //List<Cars> cars = arango.findAllCarsByEmployees("MarcinWydra");
             //cars.forEach(rx -> System.out.println(rx.toString()));
            //List<CarsParts> carsParts = arango.findAllPartsToCars("TKN2444");
            //carsParts.forEach(rx->System.out.println(rx.toString()));
            arango.setStatusNaprawyByName("TKN2444");
        } else {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Nie wybrano żadnego skłądu!|@"));
            System.exit(1);
        }
    }


}