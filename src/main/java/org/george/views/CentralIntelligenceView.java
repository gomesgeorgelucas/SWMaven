package org.george.views;

import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.george.controllers.CentralIntelligenceController;
import org.george.controllers.RegisterController;
import org.george.domains.RebelDomain;

import java.util.*;

import static java.lang.System.exit;

public class CentralIntelligenceView {

    public static final Logger LOGGER_REGISTER_CONTROLLER = LogManager.getLogger(CentralIntelligenceView.class);

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


        List<RebelDomain> sortedList = new ArrayList<>();

        sortedList.addAll(new RegisterController().readFromFiles(true));
        sortedList.addAll(rebels);
        printList(sortedList, "Unsorted");

        int option = -1;

        do {
            System.out.println("-------------SORT-------------");
            printMenu(options);

            try {
                option = new Scanner(System.in).nextInt();

                switch (option) {
                    case 1 :
                        sortedList = new CentralIntelligenceController().sortByName(sortedList);
                        LOGGER_REGISTER_CONTROLLER.info("Sorted by Name");
                        printList(sortedList, "By Name");
                    break;
                    case 2 :
                        sortedList = new CentralIntelligenceController().sortByAge(sortedList);
                        LOGGER_REGISTER_CONTROLLER.info("Sorted by Age");
                        printList(sortedList, "By Age");
                        break;
                    case 3 :
                        sortedList = new CentralIntelligenceController().sortByRace(sortedList);
                        LOGGER_REGISTER_CONTROLLER.info("Sorted by Race");
                        printList(sortedList, "By Race");
                        break;
                    case 0 :
                        List<RebelDomain> unique = new CentralIntelligenceController().save(sortedList, true);

                        if (unique.isEmpty()) {
                            System.out.println("No new rebel was found!");
                        } else {
                            printList(unique, "New Registered Rebels");
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
