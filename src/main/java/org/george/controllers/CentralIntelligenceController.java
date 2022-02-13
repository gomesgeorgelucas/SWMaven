package org.george.controllers;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.george.domains.RebelDomain;
import org.george.enums.RaceEnum;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CentralIntelligenceController {

    private static final String REBELS_FILE_PATH = CentralIntelligenceController.class.getClassLoader().
            getResource("./rebels.txt").getPath();
    private static final String SUSPECTS_FILE_PATH = CentralIntelligenceController.class.getClassLoader().
            getResource("./suspects.txt").getPath();

    private final String[] knownSiths = {"Ajunta Pall", "Belia Darzu", "The Dark Underlord",
            "Darth Andeddu", "Darth Andru", "Darth Bandon",
            "Darth Glovoc", "Darth Malak", "Darth Nihilus",
            "Darth Revan", "Darth Ruin", "Darth Sion",
            "Darth Traya", "Dathka Graush", "Exar Kun",
            "Freedon Nadd", "Kaan", "Ludo Kressh",
            "Marka Ragnos", "Naga Sadow", "Simus",
            "Tulak Hord", "Ulic Qel-Droma",
            "Darth Bane", "Darth Zannah", "Darth Cognus",
            "Darth Millennial", "Darth Vectivus", "Darth Tenebrous",
            "Darth Plagueis", "Darth Sidious", "Darth Maul",
            "Darth Tyranus", "Darth Vader", "Lady Kynthera"};

    @SneakyThrows
    public boolean verify(RebelDomain rebel) {
        if (
                Arrays.asList(knownSiths).contains(rebel.getName())
                || (rebel.getRace() == RaceEnum.HUMAN && rebel.getAge() >= 65)
                || (rebel.getRace() == RaceEnum.HUMAN && rebel.getAge() < 15)
        ) {
            return false;
        } else {
            File suspects = new File(".\\suspects.txt");
            if (suspects.exists()) {
                @Cleanup Scanner fileScanner = new Scanner(suspects);
                while (fileScanner.hasNextLine()) {
                    byte[] decodeBytes = Base64.getDecoder().decode(fileScanner.nextLine());
                    String decodedString = new String(decodeBytes);
                    if (decodedString.equals(rebel.toString())) {
                        return false;
                    }
                }
            }
        }

        if (new Random().nextBoolean()) {
            return false;
        }

        System.out.println("Mindful of your thoughts, be,You, they betray.\n - Jedi");
        return true;

    }

    public List<RebelDomain> sortByName(List<RebelDomain> rebels) {
        System.out.println(rebels);
        Collections.sort(rebels, new RebelDomain.RebelNameSorter());
        System.out.println(rebels);
        return rebels;
    }

    public List<RebelDomain> sortByAge(List<RebelDomain> rebels) {
        System.out.println(rebels);
        Collections.sort(rebels, new RebelDomain.RebelAgeSorter());
        System.out.println(rebels);
        return rebels;
    }

    public List<RebelDomain> sortByRace(List<RebelDomain> rebels) {
        System.out.println(rebels);
        Collections.sort(rebels, new RebelDomain.RebelRaceSorter());
        System.out.println(rebels);
        return rebels;
    }

    public void saveNExit(List<RebelDomain> rebels) {
        File file = new File(REBELS_FILE_PATH);
        Queue<RebelDomain> rebelsQueue = new ArrayDeque<>(rebels);
        while (!rebelsQueue.isEmpty()) {
            try {
                FileUtils.writeStringToFile(file, rebelsQueue.poll().toString() + "\r\n",StandardCharsets.UTF_8, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
