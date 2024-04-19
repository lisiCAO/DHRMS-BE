package com.lisi.booknavigator.fileservice.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Storage.SignUrlOption;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicUrlService {

    private final Storage storage;

    public PublicUrlService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    public URL generateSignedPublicUrl(String gcsUrl) {
        // 解析 gcsUrl，例如 "gs://bucket-name/object-name"
        String[] parts = gcsUrl.replace("gs://", "").split("/", 2);
        String bucketName = parts[0];
        String objectName = parts[1];
        log.info("gsurl= {} ", gcsUrl);
        log.info("bucketName= {} ", bucketName);
        log.info("objectName= {} ", objectName);

        // 获取对应的blob信息
        BlobInfo blobInfo = storage.get(bucketName, objectName);

        // 生成签名的URL , 有效期为30分钟
        URL publicUrl = storage.signUrl(blobInfo, 30, TimeUnit.MINUTES, SignUrlOption.withV4Signature());
        log.info("publicUrl= {} ", publicUrl);
        return publicUrl;
    }
}
