package com.hamlet.HamletHotel.service.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.hamlet.HamletHotel.exception.UnableToDeleteException;
import com.hamlet.HamletHotel.exception.UnableToUploadImageException;
import com.hamlet.HamletHotel.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service {

    @Value("${bucket-name}")
    private String bucketName;

    @Value("${aws.s3.access.key}")
    private String awsS3AccessKey;

    @Value("${aws.s3.secret.key}")
    private String awsS3SecretKey;

    @Value("${aws.s3.region:eu-north-1}")
    private String awsS3Region;

    @Override
    public String saveImageToS3(MultipartFile photo) {
        try {
            String originalName = photo.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf("."));
            String uniqueName = UUID.randomUUID() + extension;

            // Create AWS credentials
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);

            // Build S3 client
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.fromName(awsS3Region))
                    .build();

            // Prepare metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(photo.getContentType());

            // Upload to S3
            try (InputStream inputStream = photo.getInputStream()) {
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueName, inputStream, metadata);
                s3Client.putObject(putObjectRequest);
            }

            // Return S3 file URL
            return "https://" + bucketName + ".s3." + awsS3Region + ".amazonaws.com/" + uniqueName;

        } catch (Exception e) {
            throw new UnableToUploadImageException("Unable to upload image to S3 bucket: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            String fileKey = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.fromName(awsS3Region))
                    .build();

            s3Client.deleteObject(bucketName, fileKey);

        } catch (Exception e) {
            throw new UnableToDeleteException("Unable to delete image from S3 bucket: " + e.getMessage());
        }
    }

}
