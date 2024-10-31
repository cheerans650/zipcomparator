package com.zipcomparator;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.Enumeration;

public class Main {
    public static List<String> compareZipFiles(String filePath1, String filePath2) throws IOException {
        List<String> differences = new ArrayList<>();
        ZipFile zipFile1 = new ZipFile(new File(filePath1));
        ZipFile zipFile2 = new ZipFile(new File(filePath2));

        List<String> fileList1 = getFileList(zipFile1);
        List<String> fileList2 = getFileList(zipFile2);

        // Find files that are in zipFile1 but not in zipFile2
        for (String file : fileList1) {
            if (!fileList2.contains(file)) {
                differences.add("File only in " + filePath1 + ": " + file);
            }
        }

        // Find files that are in zipFile2 but not in zipFile1
        for (String file : fileList2) {
            if (!fileList1.contains(file)) {
                differences.add("File only in " + filePath2 + ": " + file);
            }
        }

        // Compare common files by content
        for (String file : fileList1) {
            if (fileList2.contains(file)) {
                ZipEntry entry1 = zipFile1.getEntry(file);
                ZipEntry entry2 = zipFile2.getEntry(file);
                if (!FileUtils.contentEquals(zipFile1.getInputStream(entry1), zipFile2.getInputStream(entry2))) {
                    differences.add("Content difference in file: " + file);
                }
            }
        }

        zipFile1.close();
        zipFile2.close();

        return differences;
    }

    private static List<String> getFileList(ZipFile zipFile) {
        List<String> fileList = new ArrayList<>();
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            fileList.add(entry.getName());
        }
        return fileList;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java ZipComparator <zip-file1> <zip-file2>");
            return;
        }

        String filePath1 = args[0];
        String filePath2 = args[1];

        try {
            List<String> differences = compareZipFiles(filePath1, filePath2);
            if (differences.isEmpty()) {
                System.out.println("The zip files are identical.");
            } else {
                System.out.println("Differences found:");
                for (String difference : differences) {
                    System.out.println(difference);
                }
            }
        } catch (IOException e) {
            System.err.println("Error comparing zip files: " + e.getMessage());
        }
    }
}