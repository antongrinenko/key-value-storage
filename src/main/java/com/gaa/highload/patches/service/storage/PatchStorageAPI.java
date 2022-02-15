package com.gaa.highload.patches.service.storage;

import java.util.List;

public interface PatchStorageAPI {
    boolean addPatches(String emailId, List<String> patches);
    List<String> readLastPatches(String emailId, Long limit);
    boolean deletePatches(String emailId);
    boolean clearOldPatches();
}
