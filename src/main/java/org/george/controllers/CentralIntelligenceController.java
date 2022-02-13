package org.george.controllers;

import com.google.gson.stream.MalformedJsonException;
import lombok.Cleanup;
import org.apache.commons.io.FileUtils;
import org.george.domains.RebelDomain;
import org.george.enums.RaceEnum;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.google.gson.Gson;

public class CentralIntelligenceController {

    public static final String REBELS_FILE_PATH = Objects.requireNonNull(CentralIntelligenceController.class.getClassLoader().
            getResource("./rebels.txt")).getPath();
    public static final String SUSPECTS_FILE_PATH = Objects.requireNonNull(CentralIntelligenceController.class.getClassLoader().
            getResource("./suspects.txt")).getPath();

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
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<RebelDomain> save(List<RebelDomain> candidates, boolean accepted) {

        File file = null;

        if (accepted) {
            file = new File(REBELS_FILE_PATH);
        } else {
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
                } catch (MalformedJsonException mje) {
                    mje.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                rebelsQueue.poll();
            }
        }

        return unique;
    }

    /**
     * MergeSort Collections.sort()
     * @param rebels
     * @return
     */
    public List<RebelDomain> sortByName(List<RebelDomain> rebels) {
        //Collections.sort(rebels, new RebelDomain.RebelNameSorter());
        //return rebels;
        RebelDomain[] rebelsArray = new RebelDomain[rebels.size()];
        rebels.toArray(rebelsArray);
        mergeSort(rebelsArray, rebelsArray.length, "name");
        return Arrays.asList(rebelsArray);
    }

    /**
     * MergeSort Collections.sort()
     * @param rebels
     * @return
     */
    public List<RebelDomain> sortByAge(List<RebelDomain> rebels) {
        //Collections.sort(rebels, new RebelDomain.RebelAgeSorter());
        //return rebels;
        RebelDomain[] rebelsArray = new RebelDomain[rebels.size()];
        rebels.toArray(rebelsArray);
        mergeSort(rebelsArray, rebelsArray.length, "age");
        return Arrays.asList(rebelsArray);
    }

    /**
     * MergeSort Collections.sort()
     * @param rebels
     * @return
     */
    public List<RebelDomain> sortByRace(List<RebelDomain> rebels) {
        //Collections.sort(rebels, new RebelDomain.RebelRaceSorter());
        //return rebels;
        RebelDomain[] rebelsArray = new RebelDomain[rebels.size()];
        rebels.toArray(rebelsArray);
        mergeSort(rebelsArray, rebelsArray.length, "race");
        return Arrays.asList(rebelsArray);
    }

    /**
     * MergeSort Algorth (Recursive) - enables sort by field wihtout using comparator.
     * @param rebels
     * @param size
     * @param field - String literal for name, age or race.
     */
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

    /**
     * Actual merging of smaller left and right arrays.
     * @param rebels
     * @param leftArray
     * @param rightArray
     * @param left
     * @param right
     * @param field
     */
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
