package com.mindera.finalproject.be.s3;

import software.amazon.awssdk.services.s3.model.S3Object;

public class FileObject {

    private String objectKey;

    private Long size;

    public FileObject() {

    }

    public static FileObject from(S3Object s3Object) {
        FileObject fileObject = new FileObject();
        if (s3Object != null) {
            fileObject.setObjectKey(s3Object.key());
            fileObject.setSize(s3Object.size());
        }
        return fileObject;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public Long getSize() {
        return size;
    }

    public FileObject setSize(Long size) {
        this.size = size;
        return this;
    }
}
