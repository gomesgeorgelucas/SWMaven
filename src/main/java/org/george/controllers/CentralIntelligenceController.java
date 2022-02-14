package org.george.controllers;

import lombok.Cleanup;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.george.domains.RebelDomain;
import org.george.enums.RaceEnum;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.google.gson.Gson;

public class CentralIntelligenceController {

    public static final Logger LOGGER_CENTRAL_INTELLIGENCE_CONTROLLER = LogManager.getLogger(CentralIntelligenceController.class);

    public static String REBELS_FILE_PATH;

    static {
        try {
            REBELS_FILE_PATH = Objects.requireNonNull(URLDecoder.decode(Objects.requireNonNull(CentralIntelligenceController.class.getClassLoader().
                    getResource("./rebels.txt")).getPath(), Charset.defaultCharset()));
        } catch (NullPointerException e) {
            LOGGER_CENTRAL_INTELLIGENCE_CONTROLLER.error("File not found...");
            //e.printStackTrace();
            try {
                FileUtils.touch(new File(URLDecoder.decode(Objects.requireNonNull(CentralIntelligenceController.class.getClassLoader().getResource("")).getPath() + "rebels.txt", Charset.defaultCharset())));
                REBELS_FILE_PATH = Objects.requireNonNull(URLDecoder.decode(Objects.requireNonNull(CentralIntelligenceController.class.getClassLoader().
                        getResource("./rebels.txt")).getPath(), Charset.defaultCharset()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String SUSPECTS_FILE_PATH;

    static {
        try {
            SUSPECTS_FILE_PATH = Objects.requireNonNull(URLDecoder.decode(Objects.requireNonNull(CentralIntelligenceController.class.getClassLoader().
                    getResource("./suspects.txt")).getPath(), Charset.defaultCharset()));
        } catch (NullPointerException e) {
            LOGGER_CENTRAL_INTELLIGENCE_CONTROLLER.error("File not found...");
            //e.printStackTrace();
            try {
                FileUtils.touch(new File(URLDecoder.decode(Objects.requireNonNull(CentralIntelligenceController.class.getClassLoader().getResource("")).getPath() + "suspects.txt", Charset.defaultCharset())));
                SUSPECTS_FILE_PATH = Objects.requireNonNull(URLDecoder.decode(Objects.requireNonNull(CentralIntelligenceController.class.getClassLoader().
                        getResource("./suspects.txt")).getPath(), Charset.defaultCharset()));

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

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

    public boolean verify(RebelDomain rebel) {
        if (
                Arrays.asList(knownSiths).contains(rebel.getName())
                || (rebel.getRace().equalsIgnoreCase(RaceEnum.HUMAN.getDescription()) && rebel.getAge() >= 65)
                || (rebel.getRace().equalsIgnoreCase(RaceEnum.HUMAN.getDescription()) && rebel.getAge() < 15)
        ) {
            return false;
        } else return !new Random().nextBoolean();

    }

    public boolean isDuplicate(File file, RebelDomain rebel) {
        try {
            File tempDir = FileUtils.getTempDirectory();
            FileUtils.copyFileToDirectory(file, tempDir);
            File newTempFile = FileUtils.getFile(tempDir, file.getName());
            String data = FileUtils.readFileToString(newTempFile, Charset.defaultCharset());

            if (data.contains(new Gson().toJson(rebel))) {
                LOGGER_CENTRAL_INTELLIGENCE_CONTROLLER.warn("Duplicate found: " + rebel.toString());
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<RebelDomain> save(List<RebelDomain> candidates, boolean accepted) {

        File file;

        if (accepted) {
            LOGGER_CENTRAL_INTELLIGENCE_CONTROLLER.warn("Trying to access file: " + REBELS_FILE_PATH);
            file = new File(REBELS_FILE_PATH);
        } else {
            LOGGER_CENTRAL_INTELLIGENCE_CONTROLLER.warn("Trying to access file: " + SUSPECTS_FILE_PATH);
            file = new File(SUSPECTS_FILE_PATH);
            candidates.addAll(new RegisterController().readFromFiles(false));
        }

        Queue<RebelDomain> rebelsQueue = new ArrayDeque<>(candidates);
        List<RebelDomain> unique = new ArrayList<>();

        for (RebelDomain uniqueRebel :
                rebelsQueue) {
            if (!isDuplicate(file, uniqueRebel)) {
                unique.add(uniqueRebel);
            }
        }

        //Erase file
        try {
            @Cleanup PrintWriter writer = new PrintWriter(file);
            LOGGER_CENTRAL_INTELLIGENCE_CONTROLLER.warn("Erasing file. Prepping for new entries...");
            writer.print("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Append data
        while (!rebelsQueue.isEmpty()) {
            if (!isDuplicate(file, rebelsQueue.peek())) {
                try {
                    //unique.add(rebelsQueue.peek());
                    String json = new Gson().toJson(Objects.requireNonNull(rebelsQueue.poll()));
                    FileUtils.writeStringToFile(file, json + "\r\n", StandardCharsets.UTF_8, true);
                } catch (IOException mje) {
                    mje.printStackTrace();
                } finally {
                    LOGGER_CENTRAL_INTELLIGENCE_CONTROLLER.warn("Tried to write new entry to file...");
                }
            } else {
                rebelsQueue.poll();
            }
        }

        return unique;
    }

    public List<RebelDomain> sortByName(List<RebelDomain> rebels) {
        //Collections.sort(rebels, new RebelDomain.RebelNameSorter());
        //return rebels;
        RebelDomain[] rebelsArray = new RebelDomain[rebels.size()];
        rebels.toArray(rebelsArray);
        mergeSort(rebelsArray, rebelsArray.length, "name");
        return Arrays.asList(rebelsArray);
    }

    public List<RebelDomain> sortByAge(List<RebelDomain> rebels) {
        //Collections.sort(rebels, new RebelDomain.RebelAgeSorter());
        //return rebels;
        RebelDomain[] rebelsArray = new RebelDomain[rebels.size()];
        rebels.toArray(rebelsArray);
        mergeSort(rebelsArray, rebelsArray.length, "age");
        return Arrays.asList(rebelsArray);
    }

    public List<RebelDomain> sortByRace(List<RebelDomain> rebels) {
        //Collections.sort(rebels, new RebelDomain.RebelRaceSorter());
        //return rebels;
        RebelDomain[] rebelsArray = new RebelDomain[rebels.size()];
        rebels.toArray(rebelsArray);
        mergeSort(rebelsArray, rebelsArray.length, "race");
        return Arrays.asList(rebelsArray);
    }

    private void mergeSort(RebelDomain[] rebels, int size, String field) {
        if (size < 2) {
            return;
        }

        int midPoint = size / 2;

        RebelDomain[] leftArray = new RebelDomain[midPoint];
        RebelDomain[] rightArray = new RebelDomain[size - midPoint];

        System.arraycopy(rebels, 0, leftArray, 0, midPoint);
        if (size - midPoint >= 0) System.arraycopy(rebels, midPoint, rightArray, 0, size - midPoint);

        mergeSort(leftArray, midPoint, field);
        mergeSort(rightArray, size - midPoint, field);

        merge(rebels, leftArray, rightArray, midPoint, size - midPoint, field);
    }

    private void merge(RebelDomain[] rebels, RebelDomain[] leftArray, RebelDomain[] rightArray, int left, int right, String field) {

        int i = 0, j = 0, k = 0;
        while (i < left && j < right) {
            if (field.equalsIgnoreCase("name")) {
                if (leftArray[i].getName().compareTo(rightArray[j].getName()) <= 0) {
                    rebels[k++] = leftArray[i++];
                }
                else {
                    rebels[k++] = rightArray[j++];
                }
            } else if (field.equalsIgnoreCase("age")) {
                if (leftArray[i].getAge() <= rightArray[j].getAge()) {
                    rebels[k++] = leftArray[i++];
                }
                else {
                    rebels[k++] = rightArray[j++];
                }
            } else if (field.equalsIgnoreCase("race")) {
                if (leftArray[i].getRace().compareTo(rightArray[j].getRace()) <= 0) {
                    rebels[k++] = leftArray[i++];
                }
                else {
                    rebels[k++] = rightArray[j++];
                }
            }
        }
        while (i < left) {
            rebels[k++] = leftArray[i++];
        }
        while (j < right) {
            rebels[k++] = rightArray[j++];
        }
    }
}
