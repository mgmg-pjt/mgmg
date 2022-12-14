package com.b306.mgmgproject.db.repository;

import com.b306.mgmgproject.db.entity.Diary;
import com.b306.mgmgproject.api.response.DiaryListMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Optional<Diary> findByDiaryNo(Long DiaryNo);

    Optional<Diary> findByUser_UserNoAndDiaryNo(Long userNo, Long diaryNo);

    @Transactional
    Optional<Diary> deleteByDiaryNo(Long diaryNo);

    long countByUser_UserNoAndDiaryDateBetween(Long userNo, Date startDate, Date endDate);

    long countByUser_UserNoAndDay(Long userNo, String day);

    List<DiaryListMapping> findByUser_UserNoAndDiaryDateBetween(Long userNo, Date startDate, Date endDate);

    long countByUser_UserNo(Long userNo);

    long countByUser_UserNoAndEmotion(Long userNo, String emotion);

    Optional<Diary> findByUser_UserNoAndDiaryDate(Long userNo, Date diaryDate);

}

