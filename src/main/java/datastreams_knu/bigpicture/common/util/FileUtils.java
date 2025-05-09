package datastreams_knu.bigpicture.common.util;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import datastreams_knu.bigpicture.board.entity.BoardFileEntity;

@Component
public class FileUtils {

    private final String uploadPath = "uploads";

    public List<BoardFileEntity> parseFileInfo(MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
        if (ObjectUtils.isEmpty(multipartHttpServletRequest) || !multipartHttpServletRequest.getFileNames().hasNext()) {
            return new ArrayList<>();
        }

        List<BoardFileEntity> fileList = new ArrayList<>();
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String today = LocalDateTime.now().format(formatter);
        String dailyPathSuffix = today;
        File dailyUploadDir = new File(uploadDir, dailyPathSuffix);
        if (!dailyUploadDir.exists()) {
            dailyUploadDir.mkdirs();
        }

        Iterator<String> iterator = multipartHttpServletRequest.getFileNames();

        while (iterator.hasNext()) {
            String fileNameKey = iterator.next();
            List<MultipartFile> multipartFiles = multipartHttpServletRequest.getFiles(fileNameKey);
            for (MultipartFile multipartFile : multipartFiles) {
                if (multipartFile != null && !multipartFile.isEmpty()) {
                    String originalFileName = multipartFile.getOriginalFilename();
                    String originalFileExtension = "";
                    if (originalFileName != null && originalFileName.contains(".")) {
                        originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                    }

                    String storedFileName = UUID.randomUUID().toString().replaceAll("-", "") + originalFileExtension;
                    String storedDbPath = Paths.get(dailyPathSuffix, storedFileName).toString().replace(File.separator, "/");

                    BoardFileEntity boardFile = new BoardFileEntity();
                    boardFile.setFileSize(multipartFile.getSize());
                    boardFile.setOriginalFileName(originalFileName);
                    boardFile.setStoredFilePath(storedDbPath);

                    fileList.add(boardFile);

                    File destinationFile = new File(dailyUploadDir, storedFileName);
                    multipartFile.transferTo(destinationFile);
                }
            }
        }
        return fileList;
    }
}