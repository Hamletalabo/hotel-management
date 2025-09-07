package com.hamlet.HamletHotel.service;

import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Service {

    String saveImageToS3(MultipartFile photo);
    void deleteFile(String fileUrl);
}
