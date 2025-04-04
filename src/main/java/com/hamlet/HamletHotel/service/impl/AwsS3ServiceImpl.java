package com.hamlet.HamletHotel.service.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.hamlet.HamletHotel.exception.UnableToUploadImageException;
import com.hamlet.HamletHotel.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service {

    private final String bucketName = "hamlet-hotel-images";

    @Value("${aws.s3.access.key}")
    private String awsS3Accesskey;

    @Value("${aws.s3.secret.key}")
    private String awsS3Secretkey;

    @Override
    public String saveImageToS3(MultipartFile photo) {

        String s3LocationImage = null;
        try {
            String s3Filename = photo.getOriginalFilename();

            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3Accesskey, awsS3Secretkey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.EU_NORTH_1)
                    .build();

            InputStream inputStream = photo.getInputStream();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg");

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3Filename, inputStream,metadata);
            s3Client.putObject(putObjectRequest);
            return "https://" + bucketName + ".s3.amazon.com/"+s3Filename;

        } catch (Exception e) {
            e.printStackTrace();
            throw new UnableToUploadImageException("unable to upload image to s3 bucket" + e.getMessage());
        }
    }
}
