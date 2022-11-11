package com.example.simpledms.service;

import com.example.simpledms.model.GalleryDb;
import com.example.simpledms.repository.GalleryDbRepository;
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
public class GalleryDbService {

    @Autowired
    GalleryDbRepository GalleryDbRepository; // JPA CRUD 함수가 있는 인터페이스

    //    모든 파일 조회
    public Page<GalleryDb> findAllFiles(Pageable pageable) {
        Page<GalleryDb> page = GalleryDbRepository.findAll(pageable);

        return page;

    }

    //   ID(기본키) 로 파일 조회 ( findById(기본키) : JPA 제공하는 기본 함수 )
    public Optional<GalleryDb> getGallery(int gid) {
        Optional<GalleryDb> optionalGalleryDb = GalleryDbRepository.findById(gid);

        return optionalGalleryDb;
    }

    //    fileTitle(이미지명) 으로 like 검색하는 함수
    public Page<GalleryDb> findAllByGalleryTitleContaining(String galleryTitle, Pageable pageable) {
        Page<GalleryDb> page = GalleryDbRepository
                .findAllByGalleryTitleContaining(galleryTitle, pageable);

        return page;
    }

    //    ID(기본키) 로 삭제 함수 : 1건만 삭제됨
    public boolean removeById(int gid) {

        if (GalleryDbRepository.existsById(gid) == true) {
            GalleryDbRepository.deleteById(gid); // 삭제 실행
            return true;
        }

        return false;
    }

    //    이미지 저장 함수(*)
    public GalleryDb store(String galleryTitle,
                        MultipartFile gallery) throws IOException
    {
//        path(폴더경로) 제거후 순순한 fileName 가져오기
//        .getOriginalFilename() : 경로/파일명
        String galleryFileName = StringUtils.cleanPath(gallery.getOriginalFilename());

//      1) FileDb 생성자에 경로 등 여러 정보를 저장
        GalleryDb galleryDb = new GalleryDb(galleryTitle,
                galleryFileName,
                gallery.getContentType(),
                gallery.getBytes());

//      2) 위의 FileDb 를 DB 저장 + return
        return GalleryDbRepository.save(galleryDb);
    }

}









