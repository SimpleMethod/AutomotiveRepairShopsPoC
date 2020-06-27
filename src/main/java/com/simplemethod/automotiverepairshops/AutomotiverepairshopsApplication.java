package com.simplemethod.automotiverepairshops;

import org.fusesource.jansi.AnsiConsole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import picocli.CommandLine;


import org.fusesource.jansi.AnsiConsole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import picocli.CommandLine;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class AutomotiverepairshopsApplication  implements CommandLineRunner, ExitCodeGenerator {


    private CommandLine.IFactory factory;
    private MyCommand myCommand;
    private int exitCode;


    AutomotiverepairshopsApplication(CommandLine.IFactory factory, MyCommand myCommand) {
        this.factory = factory;
        this.myCommand = myCommand;
    }

    @Override
    public void run(String... args) {
        exitCode = new CommandLine(myCommand, factory).execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        SpringApplication.run(AutomotiverepairshopsApplication.class, args);

    }
}
