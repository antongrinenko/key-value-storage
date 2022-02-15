package com.gaa.highload.patches.service.files;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.APPEND;

@CommonsLog
@Service
public class FilesAPIService implements FilesAPI {
    private static final String STORAGE_DIR = "./patch_storage";
    private static final Charset STORAGE_CHARSET = StandardCharsets.UTF_8;

    private Map<String, File> filesMap = new ConcurrentHashMap<>();


    @Override
    public void addRowsToFile(String folderName, String fileName, List<String> rows) throws IOException {
        String fullFileName = getFullFileName(folderName, fileName);

        File file;
        if (!filesMap.containsKey(fullFileName)) {
            file = new File(STORAGE_DIR + "/" + fullFileName + ".txt");
            FileUtils.touch(file);
            filesMap.put(fullFileName, file);
        } else {
            file = filesMap.get(fullFileName);
        }
        Files.write(file.toPath(), rows, STORAGE_CHARSET, APPEND);
    }

    @Override
    public List<String> readRowsFromFileTail(String folderName, String fileName, long limit) throws IOException {
        String fullFileName = getFullFileName(folderName, fileName);
        if (!filesMap.containsKey(fullFileName)) {
            return Collections.emptyList();
        }

        ReversedLinesFileReader reader = new ReversedLinesFileReader(filesMap.get(fullFileName), STORAGE_CHARSET);
        List<String> result = new ArrayList<>();
        int counter = 0;
        while(counter < limit) {
            String line = reader.readLine();
            if (Objects.isNull(line)) {
                break;
            }
            result.add(line);
            counter++;
        }
        return result;
    }

    @Override
    public void deleteFile(String folderName, String fileName) {
        String fullFileName = getFullFileName(folderName, fileName);
        File file = filesMap.remove(fullFileName);
        if (Objects.nonNull(file)) {
            file.delete();
        }
    }

    @Override
    public void deleteFolder(String folderName) throws IOException {
        FileUtils.deleteDirectory(new File(STORAGE_DIR + "/" + folderName));
        Iterator<String> iterator = filesMap.keySet().iterator();
        while (iterator.hasNext()) {
            String fullFileName = iterator.next();
            if (fullFileName.startsWith(folderName)) {
                iterator.remove();
            }
        }
    }

    @Override
    public List<String> getReverseSortedFolderNames() {
        return Stream.of(new File(STORAGE_DIR).listFiles())
                .filter(file -> file.isDirectory())
                .map(File::getName)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    private String getFullFileName(String folderName, String fileName) {
        return folderName + "/" + fileName;
    }
}
