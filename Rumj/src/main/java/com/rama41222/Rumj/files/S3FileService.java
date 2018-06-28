package com.rama41222.Rumj.files;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.rama41222.Rumj.app.BasicConfiguration;
import com.rama41222.Rumj.repository.FilesRepository;
import org.apache.juli.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class S3FileService {

    @Autowired
    private BasicConfiguration configuration;

    @Autowired
    private FilesRepository filesRepository;

    private AWSCredentials credentials;
    private AmazonS3 awsS3Client;

    Logger log = LoggerFactory.getLogger(this.getClass().getName());


    /**
     * This private method will return the uploaded file location as a map
     *
     * @param file multipart file
     * @return Response object file location and error as a map
     */

    public Map<String, String> uploadToS3(MultipartFile file) {
        Map<String, String> map = new HashMap<>();
        String location = "";
        try {
            location = this.streamToS3(file);
        } catch (IOException e) {
            map.put("url", null);
            map.put("error", "Error Occured");
            return map;
        }

        map.put("url", location);
        map.put("error", null);
        return map;
    }


    /**
     * This private method will return the uploaded file location
     *
     * @param mfile multipart file
     * @return Response object file location as a String or null
     */

    private String streamToS3(MultipartFile mfile) throws IOException {

        byte[] bytes = mfile.getBytes();

        try {
            String objectName = System.currentTimeMillis() + "".concat("-").concat(mfile.getOriginalFilename());
            InputStream stream = new ByteArrayInputStream(bytes);
            ObjectMetadata omd = new ObjectMetadata();
            omd.setContentLength(bytes.length);

            getClient().putObject(new PutObjectRequest(configuration.getBucketname(), objectName, stream, omd)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            );

            String s3DirectUrl = configuration.getUrl().concat(objectName);
            File file = new File();
            file.setName(objectName);
            file.setUrl(s3DirectUrl);
            filesRepository.save(file);
            return s3DirectUrl;

        } catch (AmazonServiceException ase) {
            log.error(ase.getMessage());
        } catch (AmazonClientException ace) {
            log.error(ace.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }


    /**
     * This method will return true or false according to the file deletion result from s3.
     *
     * @param name file descriptor name
     * @return Response object contains true or false
     */

    public boolean removeFromS3(String name){
        try {
            getClient().deleteObject(configuration.getBucketname(), name);
            return true;
        } catch (AmazonServiceException ase) {
            log.error(ase.getMessage());

        } catch (AmazonClientException ace) {
            log.error(ace.getMessage());

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }


    private AmazonS3 getClient(){
        if(awsS3Client == null) {
            awsS3Client = AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(getCredentials()))
                    .withRegion(configuration.getRegion())
                    .build();
        }
        return awsS3Client;
    }


    private AWSCredentials getCredentials() {
        if(credentials == null) {
            credentials = new BasicAWSCredentials(configuration.getKey(), configuration.getSecret());
        }
        return credentials;
    }
}
