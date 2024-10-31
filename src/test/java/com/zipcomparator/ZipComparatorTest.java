package com.zipcomparator;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;
public class ZipComparatorTest {

    private File zipFile1;
    private File zipFile2;
    private File tempDir;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("zip-comparator-test").toFile();
    }
    @AfterEach
    void tearDown() {
        try {
            if (zipFile1 != null) {
                zipFile1.delete();
            }
            if (zipFile2 != null) {
                zipFile2.delete();
            }
        } catch (Exception e) {
            System.err.println("Error closing zip files: " + e.getMessage());
        }
        // Delete tempDir if it's a directory created for testing
        if (tempDir != null && tempDir.exists()) {
            tempDir.delete();
        }
    }

    @Test
    @DisplayName("Test identical ZIP files")
    void testIdenticalZipFiles() throws IOException {
        zipFile1 = createZipFile("file1.txt", "Hello, World!");
        zipFile2 = createZipFile("file1.txt", "Hello, World!");

        List<String> differences = ZipComparator.compareZipFiles(zipFile1.getAbsolutePath(), zipFile2.getAbsolutePath());
        assertTrue(differences.isEmpty(), "There should be no differences for identical files");
    }

    @Test
    @DisplayName("Test ZIP files with different contents")
    void testZipFilesWithDifferentContents() throws IOException {
        zipFile1 = createZipFile("file1.txt", "Hello, World!");
        zipFile2 = createZipFile("file1.txt", "Hello, Universe!");

        List<String> differences = ZipComparator.compareZipFiles(zipFile1.getAbsolutePath(), zipFile2.getAbsolutePath());
        assertEquals(1, differences.size(), "There should be one difference for content mismatch");
        assertTrue(differences.get(0).contains("Content difference in file: file1.txt"));
    }

    @Test
    @DisplayName("Test ZIP files with unique files")
    void testZipFilesWithUniqueFiles() throws IOException {
        // Arrange - create zip files with unique contents
        zipFile1 = createZipFile("file1.txt", "Hello, World!");
        zipFile2 = createZipFile("file2.txt", "Goodbye, World!");

        // Act - compare zip files
        List<String> differences = ZipComparator.compareZipFiles(zipFile1.getAbsolutePath(), zipFile2.getAbsolutePath());

        // Print the differences for debug purposes
        System.out.println("Differences found: " + differences);

        // Assert - check that the differences list contains the expected unique files
        assertTrue(differences.stream().anyMatch(diff -> diff.contains("file1.txt")), "Expected file1.txt to be in the differences");
        assertTrue(differences.stream().anyMatch(diff -> diff.contains("file2.txt")), "Expected file2.txt to be in the differences");
    }

    @Test
    @DisplayName("Test with empty ZIP files")
    void testEmptyZipFiles() throws IOException {
        zipFile1 = createEmptyZipFile();
        zipFile2 = createEmptyZipFile();

        List<String> differences = ZipComparator.compareZipFiles(zipFile1.getAbsolutePath(), zipFile2.getAbsolutePath());
        assertTrue(differences.isEmpty(), "Empty ZIP files should have no differences");
    }

    @Test
    @DisplayName("Test handling of missing or corrupt ZIP files")
    void testInvalidZipFile() {
        assertThrows(IOException.class, () -> {
            ZipComparator.compareZipFiles("nonexistent1.zip", "nonexistent2.zip");
        });
    }

    private File createZipFile(String filename, String content) throws IOException {
        File zipFile = new File(tempDir, "testZip_" + System.currentTimeMillis() + ".zip");
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            ZipEntry entry = new ZipEntry(filename);
            zos.putNextEntry(entry);
            zos.write(content.getBytes());
            zos.closeEntry();
        }
        return zipFile;
    }

    private File createEmptyZipFile() throws IOException {
        return createZipFile("", "");
    }
}
