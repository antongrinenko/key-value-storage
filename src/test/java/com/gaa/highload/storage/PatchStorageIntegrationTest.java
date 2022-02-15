package com.gaa.highload.storage;

import com.gaa.highload.patches.service.date.DateAPI;
import com.gaa.highload.patches.service.date.DateService;
import com.gaa.highload.patches.service.files.FilesAPI;
import com.gaa.highload.patches.service.files.FilesAPIService;
import com.gaa.highload.patches.service.storage.PatchStorageAPI;
import com.gaa.highload.patches.service.storage.PatchStorageService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static com.gaa.highload.patches.service.files.FilesAPIService.STORAGE_DIR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PatchStorageIntegrationTest {
    @Test
    public void integrationTest() throws IOException {
        DateAPI dateService = Mockito.mock(DateService.class);
        FilesAPI filesService = new FilesAPIService();
        PatchStorageAPI patchStorage = new PatchStorageService(dateService, filesService);
        FileUtils.deleteDirectory(new File(STORAGE_DIR));

        //Создаем текущую партицию "2022-2" и наполняем данными
        Mockito.when(dateService.getCurrentDateLabel()).thenReturn("2022-2");
        patchStorage.addPatches("email1", Arrays.asList("p1_1", "p1_2"));
        patchStorage.addPatches("email1", Arrays.asList("p1_3"));
        patchStorage.addPatches("email2", Arrays.asList("p2_1"));
        assertThat(patchStorage.readLastPatches("email1", 2L), is(Arrays.asList("p1_3", "p1_2")));

        //Меняем текущую партицию на "2022-3" и наполняем данными
        Mockito.when(dateService.getCurrentDateLabel()).thenReturn("2022-3");
        patchStorage.addPatches("email1", Arrays.asList("p1_4"));
        assertThat(patchStorage.readLastPatches("email1", null), is(Arrays.asList("p1_4", "p1_3", "p1_2", "p1_1")));
        assertThat(patchStorage.readLastPatches("email2", null), is(Arrays.asList("p2_1")));

        //Удаляем все патчи из всех партиций по email2
        patchStorage.deletePatches("email2");
        assertThat(patchStorage.readLastPatches("email2", null), is(Collections.emptyList()));

        //Очищаем базу от ненужных данных. Удаляем партицию "2022-2"
        Mockito.when(dateService.getValidDateLabels()).thenReturn(Arrays.asList("2022-3"));
        patchStorage.clearOldPatches();
        assertThat(patchStorage.readLastPatches("email1", null), is(Arrays.asList("p1_4")));
    }
}
