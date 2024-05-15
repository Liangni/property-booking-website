package com.penny.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${aws.presigned-url.expire-in-min}")
    private Integer EXPIRE_IN_MIN;

    private final AmazonS3 amazonS3;

    /**
     * 生成預簽署 URL，用於指定的存儲桶和文件路徑。
     *
     * @param bucketName 存儲桶名稱
     * @param filePath 文件路徑
     * @param http HTTP 方法（例如 GET 或 PUT）
     * @return 預簽署 URL 字串
     */
    public String generatePreSignedUrl(String bucketName, String filePath, HttpMethod http) {
        // 取得目前時間
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        // 加上過期時間（以分鐘為單位）
        cal.add(Calendar.MINUTE, EXPIRE_IN_MIN);

        // 生成預簽署 URL 並轉換為字串返回
        return amazonS3.generatePresignedUrl(bucketName,filePath,cal.getTime(),http).toString();
    }

}
