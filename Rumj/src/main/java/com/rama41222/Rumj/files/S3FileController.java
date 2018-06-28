package com.rama41222.Rumj.files;

import com.rama41222.Rumj.repository.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class S3FileController {

    @Autowired
    private S3FileService s3Service;

    @Autowired
    private FilesRepository filesRepository;


    /**
     * This resource will return a map of file url and error.
     *
     * @param mFile multipart file object
     * @return Response object uploaded file url and error
     */

    @RequestMapping(method = RequestMethod.POST, value = "/files")
    public Map<String, String > addFile(@RequestParam("file") MultipartFile mFile){
        Map<String, String > mp = s3Service.uploadToS3(mFile);
        return mp;
    }


    /**
     * This resource will return a list of File Objects.
     *
     * @return Response object contains a list of File Objects
     */

    @RequestMapping(method = RequestMethod.GET, value = "/files")
    public List<File> getAll(){
        return filesRepository.findAll();
    }


    /**
     * This resource will return a File Object.
     *
     * @param id file id
     * @return Response object contains File Object
     */

    @RequestMapping(method = RequestMethod.GET, value = "/files/{id}")
    public Optional<File> getFileById(@PathVariable("id") int id){
        return filesRepository.findById(id);
    }


    /**
     * This resource will remove a File Object from Database as well as S3.
     *
     * @param id file id
     * @return Response object contains true or false
     */

    @RequestMapping(method = RequestMethod.DELETE, value = "/files/{id}")
    public boolean removeFileById(@PathVariable("id") int id){
        Optional<File> file = filesRepository.findById(id);
        if(file.isPresent()) {
            String name = file.get().getName();
            boolean result = s3Service.removeFromS3(name);

            if(!result) {
                return false;
            }

            filesRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
