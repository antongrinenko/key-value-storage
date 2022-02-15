package com.gaa.highload.patches.service.storage;

import com.gaa.highload.patches.service.date.DateAPI;
import com.gaa.highload.patches.service.files.FilesAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@CommonsLog
@RequiredArgsConstructor
public class PatchStorageService implements PatchStorageAPI {
    private final DateAPI dateService;
    private final FilesAPI filesService;

    @Override
    public boolean addPatches(String emailId, List<String> patches) {
        try {
            filesService.addRowsToFile(dateService.getCurrentDateLabel(), emailId, patches);
            return true;
        } catch (IOException e) {
            log.error("Can not write patches for emailId: " + emailId, e);
            return false;
        }
    }

    @Override
    public List<String> readLastPatches(String emailId, Long limit) {
        List<String> result = new ArrayList<>();

        try {
            Long currentLimit = Objects.requireNonNullElse(limit, Long.MAX_VALUE);
            for (String folderName : filesService.getReverseSortedFolderNames()) {
                List<String> rows = filesService.readRowsFromFileTail(folderName, emailId, currentLimit);
                result.addAll(rows);
                currentLimit -= rows.size();
                if (currentLimit <= 0) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Can not read patches for emailId: " + emailId, e);
        }

        return result;
    }

    @Override
    public boolean deletePatches(String emailId) {
        try {
            for (String folderName : filesService.getReverseSortedFolderNames()) {
                filesService.deleteFile(folderName, emailId);
            }
            return true;
        } catch (Exception e) {
            log.error("Can not delete patches for emailId: " + emailId, e);
            return false;
        }
    }

    @Override
    public boolean clearOldPatches() {
        try {
            List<String> validFolderNames = dateService.getValidDateLabels();
            for (String folderName : filesService.getReverseSortedFolderNames()) {
                if (!validFolderNames.contains(folderName)) {
                    filesService.deleteFolder(folderName);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("Can not clear old patches", e);
            return false;
        }

    }
}
