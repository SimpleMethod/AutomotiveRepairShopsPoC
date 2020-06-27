package com.simplemethod.automotiverepairshops;


import com.simplemethod.automotiverepairshops.neo4jModel.Cars;
import com.simplemethod.automotiverepairshops.neo4jModel.Persons;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.List;

@Component
@CommandLine.Command(name = "university", mixinStandardHelpOptions = true, version = "1.0 dev")
public class MyCommand implements Runnable {

    @Autowired
    Neo4j neo4j;


    @CommandLine.Option(names = {"-t", "--type"}, description = "Type of database I=InfluxDB C=Cassandra")
    private String[] type = new String[]{"C"};

    @SneakyThrows
    public void run() {

        if (type[0].equals("C") || type[0].equals("c")) {
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
            neo4j.setCarsWithRelationships("TK2144", "Jan Kowalki", "Janusz Adamowski", new String[]{"Uszczelka pod glowice", "Silnik1.0TDI","Akumulator 12V"});
        } else if (type[0].equals("I") || type[0].equals("i")) {

        } else {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Nie wybrano żadnego skłądu!|@"));
            System.exit(1);
        }
    }


}