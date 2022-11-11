package com.example.simpledms.service;

import com.example.simpledms.model.Dept;
import com.example.simpledms.model.FileDb;
import com.example.simpledms.repository.DeptRepository;
import com.example.simpledms.repository.FileDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * packageName : com.example.jpaexam.service.exam01
 * fileName : DeptService
 * author : ds
 * date : 2022-10-20
 * description : 부서 업무 서비스 클래스
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2022-10-20         ds          최초 생성
 */
@Service
public class FileDbService {

    @Autowired
    FileDbRepository fileDbRepository; // JPA CRUD 함수가 있는 인터페이스

    //    모든 파일 조회
    public Page<FileDb> findAllFiles(Pageable pageable) {
        Page<FileDb> page = fileDbRepository.findAll(pageable);

        return page;
    }

    //   ID(기본키) 로 파일 조회 ( findById(기본키) : JPA 제공하는 기본 함수 )
    public Optional<FileDb> getFile(int fid) {
        Optional<FileDb> optionalFileDb = fileDbRepository.findById(fid);

        return optionalFileDb;
    }

    //    fileTitle(이미지명) 으로 like 검색하는 함수
    public Page<FileDb> findAllByFileTitleContaining(String fileTitle, Pageable pageable) {
        Page<FileDb> page = fileDbRepository
                .findAllByFileTitleContaining(fileTitle, pageable);

        return page;
    }

    //    ID(기본키) 로 삭제 함수 : 1건만 삭제됨
    public boolean removeById(int fid) {

        if (fileDbRepository.existsById(fid) == true) {
            fileDbRepository.deleteById(fid); // 삭제 실행
            return true;
        }

        return false;
    }

    //    이미지 저장 함수(*)
    public FileDb store(String fileTitle,
                        String fileContent,
                        MultipartFile file) throws IOException
    {
//        path(폴더경로) 제거후 순순한 fileName 가져오기
//        .getOriginalFilename() : 경로/파일명
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

//      1) FileDb 생성자에 경로 등 여러 정보를 저장
        FileDb fileDb = new FileDb(fileTitle,
                                   fileContent,
                                   fileName,
                                   file.getContentType(), // 이미지의 타입정보( .jpg, .png 등 )
                                   file.getBytes());      // 이미지 크기(size)

//      2) 위의 FileDb 를 DB 저장 + return
        return fileDbRepository.save(fileDb);
    }

}









