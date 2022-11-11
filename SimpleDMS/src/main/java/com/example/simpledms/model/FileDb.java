package com.example.simpledms.model;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * packageName : com.example.jpaexam.model
 * fileName : Dept
 * author : ds
 * date : 2022-10-19
 * description : 부서 모델(== JPA : 엔티티(Entity)) 클래스
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2022-10-19         ds          최초 생성
 */
@Entity
@Table(name = "TB_FILE")
@SequenceGenerator(
        name= "SQ_FILE_GENERATOR"
        , sequenceName = "SQ_FILE"
        , initialValue = 1
        , allocationSize = 1
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Where(clause = "DELETE_YN = 'N'")
@SQLDelete(sql="UPDATE TB_FILE SET DELETE_YN = 'Y', DELETE_TIME = TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') WHERE FID = ?")
public class FileDb extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE
                   , generator = "SQ_FILE_GENERATOR"
    )
    private Integer fid;

    @Column
    private String fileTitle;

    @Column
    private String fileContent;

    @Column
    private String fileName;

    @Column
    private String fileType;

//    @Lob : DB 테이블의 Blob/Clob 자료형에 해당하는 컬럼일 경우 @ 사용함
//          자료형은 byte[] 사용.
    @Lob
    @Column
    private byte[] fileData; // 이미지

//    id 제외 생성자
    public FileDb(String fileTitle, String fileContent, String fileName, String fileType, byte[] fileData) {
        this.fileTitle = fileTitle;
        this.fileContent = fileContent;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileData = fileData;
    }
}










