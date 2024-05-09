package com.penny.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class S3Service {

    private static final int EXPIRE_IN_MIN = 60;

    private final AmazonS3 amazonS3;

    public String generatePreSignedUrl(String bucketName, String filePath, HttpMethod http) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, EXPIRE_IN_MIN);
        return amazonS3.generatePresignedUrl(bucketName,filePath,cal.getTime(),http).toString();
    }

    public byte[] getObjects(String bucketName, String filePath) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, filePath);

        S3Object res = amazonS3.getObject(getObjectRequest);

        try {
            return res.getObjectContent().readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
