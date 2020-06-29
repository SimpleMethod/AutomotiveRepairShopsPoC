package com.simplemethod.automotiverepairshops;


import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "university", mixinStandardHelpOptions = true, version = "1.0 dev")
public class MyCommand implements Runnable {

    @Autowired
    MainViewNeo4j mainViewNeo4j;

    @Autowired
    MainViewArango arango;

    @CommandLine.Option(names = {"-t", "--type"}, description = "Type of database N=neo4j A=Arango")
    private String[] type = new String[]{"N"};

    @SneakyThrows
    public void run() {
        if (type[0].equals("N") || type[0].equals("n")) {
            mainViewNeo4j.menu();
        } else if (type[0].equals("A") || type[0].equals("a")) {
            arango.menu();
        } else {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Nie wybrano żadnego skłądu!|@"));
            System.exit(1);
        }
    }


}