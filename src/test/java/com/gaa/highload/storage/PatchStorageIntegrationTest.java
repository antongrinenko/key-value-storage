package com.gaa.highload.storage;

import com.gaa.highload.patches.service.date.DateAPI;
import com.gaa.highload.patches.service.date.DateService;
import com.gaa.highload.patches.service.files.FilesAPI;
import com.gaa.highload.patches.service.files.FilesAPIService;
import com.gaa.highload.patches.service.storage.PatchStorageAPI;
import com.gaa.highload.patches.service.storage.PatchStorageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;

public class PatchStorageIntegrationTest {
//    @Test
//    public void integrationTest() {
//        DateAPI dateService = Mockito.mock(DateService.class);
//        FilesAPI filesService = new FilesAPIService();
//        PatchStorageAPI patchStorage = new PatchStorageService(dateService, filesService);
//
//        Mockito.when(dateService.getCurrentDateLabel()).thenReturn("2022-2");
//        patchStorage.addPatches("email1", Arrays.asList("p1_1", "p1_2"));
//        patchStorage.addPatches("email1", Arrays.asList("p1_3"));
//        patchStorage.addPatches("email2", Arrays.asList("p2_1"));
//
//        assertThat(patchStorage.readLastPatches("email1", 2L), is(Arrays.asList("p3", "p2")));
//    }
}
