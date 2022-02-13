package org.george.views;

import lombok.NonNull;
import org.george.controllers.CentralIntelligenceController;
import org.george.domains.RebelDomain;

import java.util.*;

public class CentralIntelligenceView {

    private final String[] options = {"1 - Name", "2 - Age", "3 - Race", "0 - Save and Exit"};

    private void printMenu(@NonNull String[] options) {
        for (String option : options) {
            System.out.println(option);
        }
        System.out.print("Choose your option: ");
    }

    public void show(List<RebelDomain> rebels) {
        System.out.println("------REBEL ALLIANCE 2.0------");
        System.out.println("-------------SORT-------------");

        List<RebelDomain> sortedList = rebels;

        int option = -1;

        do {

            printMenu(options);

            try {
                option = new Scanner(System.in).nextInt();

                switch (option) {
                    case 1 : sortedList = new CentralIntelligenceController().sortByName(rebels); break;
                    case 2 : sortedList = new CentralIntelligenceController().sortByAge(rebels); break;
                    case 3 : sortedList = new CentralIntelligenceController().sortByRace(rebels); break;
                    case 0 :
                        System.out.println("Registered Rebels: ");
                        for (RebelDomain rebel:
                             sortedList) {
                            System.out.println(rebel);
                        }
                        new CentralIntelligenceController().saveNExit(sortedList);
                        break;
                    default:
                        System.out.println("Invalid option. Try again!");
                        break;
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again!");
            }
        } while (option != 0);
    }
}
