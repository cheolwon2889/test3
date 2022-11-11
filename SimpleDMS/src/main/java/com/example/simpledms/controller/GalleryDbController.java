package com.example.simpledms.controller;

import com.example.simpledms.dto.ResponseMessageDto;
import com.example.simpledms.dto.galleryDb.ResponseGalleryDto;
import com.example.simpledms.model.GalleryDb;
import com.example.simpledms.service.GalleryDbService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * packageName : com.example.jpaexam.controller.exam07
 * fileName : Dept07Controller
 * author : ds
 * date : 2022-10-21
 * description : 부서 컨트롤러 쿼리 메소드
 * 요약 :
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2022-10-21         ds          최초 생성
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class GalleryDbController {

    @Autowired
    GalleryDbService galleryDbService;

    //  Model to Dto 자동변환, Dto to Model 자동변환 외부 라이브러리
    ModelMapper modelMapper = new ModelMapper();

    //    이미지 업로드 컨트롤러 함수
    @PostMapping("/galleryDb/upload")
    public ResponseEntity<Object> fileDbUploadFile(@RequestParam("galleryTitle") String galleryTitle,
                                                   @RequestParam("galleryDb") MultipartFile galleryDb
    ) {

        String message = ""; // front-end 전송할 메세지

//            디버깅 출력
        log.debug("galleryTitle :" + galleryTitle);
        log.debug("galleryDb :" + galleryDb);

        try {
//            db 저장 함수 호출
            galleryDbService.store(galleryTitle,  galleryDb);

            message = "Upload the file successfully: " + galleryDb.getOriginalFilename();

            return new ResponseEntity<>(new ResponseMessageDto(message), HttpStatus.OK);

        } catch (Exception e) {
            log.debug(e.getMessage());
            message = "Could not upload the file : " + galleryDb.getOriginalFilename();
            return new ResponseEntity<>(new ResponseMessageDto(message),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    모든 이미지 정보 가져오기 함수
    @GetMapping("/galleryDb")
    public ResponseEntity<Object> getListFiles(@RequestParam(required = false) String galleryTitle,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "3") int size
    ) {

        try {
//            Pageable 객체 정의 ( page, size 값 설정 )
            Pageable pageable = PageRequest.of(page, size);

//          Upload 이미지 정보를 가져오는 함수
            Page<ResponseGalleryDto> galleryPage = galleryDbService
                    .findAllByGalleryTitleContaining(galleryTitle, pageable)
                    .map(dbGallery -> {
//                      자동적으로 반복문이 실행됨 : .map()
//                      1) 다운로드 URL 만들기
//          ServletUriComponentsBuilder : URL 만들어 주는 클래스
                        String galleryDownloadUri = ServletUriComponentsBuilder
//                                (변경)
//                               .fromCurrentRequest() -> .fromCurrentContextPath()
                                .fromCurrentContextPath() // 이미지 파일 경로
                                .path("/api/galleryDb/")
                                .path(dbGallery.getGid().toString()) // "/api/fileDb/1"
                                .toUriString(); // 마지막에 호출( URL 완성됨 )

//                      modelMapper 로 dbFile == fileDB -> ResponseFileDto 변환
//                        modelMapper.map(소스모델, 타겟DTO.class)
                        ResponseGalleryDto galleryDto = modelMapper.map(dbGallery, ResponseGalleryDto.class);

//                      DTO 에 2개 남은 속성 처리 : setter 이용 가공된 데이터 저장
                        galleryDto.setGallerySize(dbGallery.getGalleryData().length);
                        galleryDto.setGalleryUrl(galleryDownloadUri);

                        return galleryDto;
                    });

//            맵 자료구조에 넣어서 전송
            Map<String, Object> response = new HashMap<>();
            response.put("galleryDb", galleryPage.getContent());
            response.put("currentPage", galleryPage.getNumber());
            response.put("totalItems", galleryPage.getTotalElements());
            response.put("totalPages", galleryPage.getTotalPages());

            if (galleryPage.isEmpty() == false) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    파일 다운로드 함수 : url /api/fileDb/1
    @GetMapping("/galleryDb/{gid}")
    public ResponseEntity<byte[]> getFile(@PathVariable int gid) {

//        id 로 조회 함수
        GalleryDb galleryDb = galleryDbService.getGallery(gid).get();

//        첨부파일 다운로드 : url Content-Type 규칙
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + galleryDb.getGalleryFileName() + "\"")
                .body(galleryDb.getGalleryData());
    }

//    id 삭제 함수
    @DeleteMapping("/galleryDb/deletion/{gid}")
    public ResponseEntity<Object> deleteFileDb(@PathVariable int gid) {

        try {
            boolean bSuccess = galleryDbService.removeById(gid);

            if (bSuccess == true) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

        } catch (Exception e) {
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}










