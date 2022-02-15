package com.gaa.highload.patches.service.files;

import java.io.IOException;
import java.util.List;

public interface FilesAPI {
    void addRowsToFile(String folderName, String fileName, List<String> rows) throws IOException;
    List<String> readRowsFromFileTail(String folderName, String fileName, long limit) throws IOException;
    void deleteFile(String folderName, String fileName);
    void deleteFolder(String folderName) throws IOException;
    List<String> getReverseSortedFolderNames();
}
