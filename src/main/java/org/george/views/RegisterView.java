package org.george.views;

import lombok.NonNull;
import org.george.controllers.RegisterController;
import org.george.domains.RebelDomain;
import org.george.enums.RaceEnum;
import org.george.exceptions.InvalidRebelInfoException;

import static java.lang.System.exit;

import java.io.IOException;
import java.util.*;

public class RegisterView {
    private final String[] options = {"1 - Register", "2 - Print", "0 - Exit"};

    private void printMenu(@NonNull String[] options) {
        for (String option : options) {
            System.out.println(option);
        }
        System.out.print("Choose your option: ");
    }

    public void show() {
        System.out.println("------REBEL ALLIANCE 2.0------");

        int option = -1;

        while (option != 0) {

            printMenu(options);

            try {
                option = new Scanner(System.in).nextInt();

                switch (option) {
                    case 1 : register(); break;
                    case 2 : print(); break;
                    case 0 : exit(0); break;
                    default:
                        System.out.println("Invalid option. Try again!");
                        break;
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again!");
            }
        }
    }

    private void print() {
    }

    @Deprecated
    private void abort() throws InterruptedException, IOException {
        System.err.println("Self Destruction - ACTIVATED!");
        for (int i = 2; i >= 0; i--) {
            System.err.println("..." + (i+1));
            Thread.sleep(1000);
        }
        new RegisterController().abort();
        System.err.println("ABORTED");
        exit(1);
    }

    private void register() {
        System.out.println("------REBEL ALLIANCE------");
        System.out.println("---------REGISTER---------");

        List<RebelDomain> candidatesList = new ArrayList<>();

        do {
            String name = askName();
            int age = askAge();
            RaceEnum raceEnum = askRace();

            candidatesList.add(new RebelDomain(name,age,raceEnum));

        } while (askNext());

        new CentralIntelligenceView().show(new RegisterController().register(candidatesList));
    }

    private boolean askNext() {
        System.out.print("Any more candidates(Y/N)? ");
        String option = "";
        do {
            try {
                option = new Scanner(System.in).nextLine();

                if (!option.equalsIgnoreCase("Y") && !option.equalsIgnoreCase("N")) {
                    System.out.println("Invalid option. Try again!");
                }
            } catch (NoSuchElementException e) {
                System.out.println("No line feed!");
            }
        } while (!option.equalsIgnoreCase("Y") && !option.equalsIgnoreCase("N"));

        if (option.equalsIgnoreCase("Y")) {
            return true;
        } else {
            return false;
        }
    }

    private String askName() {
        String name = "";

        do {
            try {
                System.out.print("Name: ");
                name = new Scanner(System.in).nextLine();

                if (name.isBlank() || name.isEmpty() || name == null) {
                    throw new InvalidRebelInfoException("Invalid name.");
                }

            } catch (NoSuchElementException e) {
                System.out.println("No line feed! Try Again");
            } catch (InvalidRebelInfoException irie) {
                System.out.println(irie.getMessage());
            }
        } while (name.isBlank() || name.isEmpty() || name == null);

        return name;
    }

    private int askAge() {
        int age = -1;

        do {
            try {
                System.out.print("Age: ");
                age = new Scanner(System.in).nextInt();

                if (age <= 0) {
                    throw new InvalidRebelInfoException("Invalid age!");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again!");
            } catch (InvalidRebelInfoException irie) {
                System.out.println(irie.getMessage());
            }
        } while (age <= 0);

        return age;
    }

    private RaceEnum askRace() {
        int raceEnum = -1;

        do {
            try {
                System.out.println("Race: ");
                for (RaceEnum race :
                        RaceEnum.values()) {
                    System.out.println(race.ordinal() + " - " + race.getDescription());
                }

                raceEnum = new Scanner(System.in).nextInt();

                if (raceEnum < 0 || raceEnum >= RaceEnum.values().length) {
                    throw new InvalidRebelInfoException("Invalid race!");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again!");
            } catch (InvalidRebelInfoException irie) {
                System.out.println(irie.getMessage());
            }

        } while (raceEnum < 0 || raceEnum >= RaceEnum.values().length);

        return RaceEnum.values()[raceEnum];
    }

}
