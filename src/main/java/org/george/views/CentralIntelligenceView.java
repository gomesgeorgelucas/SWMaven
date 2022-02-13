package org.george.views;

import lombok.NonNull;
import org.george.controllers.CentralIntelligenceController;
import org.george.domains.RebelDomain;
import org.george.enums.RaceEnum;

import java.util.*;

import static java.lang.System.exit;

public class CentralIntelligenceView {

    private final String[] options = {"1 - Name", "2 - Age", "3 - Race", "0 - Save and Exit"};

    private void printMenu(@NonNull String[] options) {
        for (String option : options) {
            System.out.println(option);
        }
        System.out.println("Choose your option: ");
    }

    public void printList(List<RebelDomain> rebels, String order) {
        System.out.println(order + ": ");

        for (RebelDomain rebel: rebels) {
            System.out.println(rebel);
        }
    }

    public void show(List<RebelDomain> rebels) {
        System.out.println("------REBEL ALLIANCE 2.0------");



        List<RebelDomain> sortedList = rebels;
        printList(sortedList, "Unsorted");

        int option = -1;

        do {
            System.out.println("-------------SORT-------------");
            printMenu(options);

            try {
                option = new Scanner(System.in).nextInt();

                switch (option) {
                    case 1 :
                        sortedList = new CentralIntelligenceController().sortByName(rebels);
                        printList(sortedList, "By Name");
                    break;
                    case 2 :
                        sortedList = new CentralIntelligenceController().sortByAge(rebels);
                        printList(sortedList, "By Age");
                        break;
                    case 3 :
                        sortedList = new CentralIntelligenceController().sortByRace(rebels);
                        printList(sortedList, "By Race");
                        break;
                    case 0 :
                        List<RebelDomain> unique = new CentralIntelligenceController().save(sortedList, true);

                        System.out.println("New Registered Rebels: ");
                        if (unique.isEmpty()) {
                            System.out.println("NONE!");
                        } else {
                            for (RebelDomain rebel :
                                    unique) {
                                System.out.println(rebel);
                            }
                        }

                        exit(0);
                        break;
                    default:
                        System.out.println("Invalid option. Try again!");
                        break;
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again!");
            }
        } while (option != 0);

        System.out.println("Mindful of your thoughts, be,You, they betray.\n - Jedi");
    }
}
