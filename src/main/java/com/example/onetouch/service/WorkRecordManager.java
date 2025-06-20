package com.example.onetouch.service;

import com.example.onetouch.entity.WorkRecord;
import com.example.onetouch.repository.WorkRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WorkRecordManager {

    private WorkRecord currentRecord;

    @Autowired
    private WorkRecordRepository repository;

    // 作業開始処理
    public WorkRecord saveStart() {
        if (currentRecord != null) {
            throw new IllegalStateException("作業記録はすでに開始されています");
        }

        currentRecord = new WorkRecord();
        currentRecord.setStartTime(LocalDateTime.now());
        currentRecord.setDate(LocalDateTime.now().toLocalDate());
        currentRecord.setDayOfWeek(LocalDateTime.now().getDayOfWeek().toString());

        return currentRecord;
    }

    // 作業完了処理
    public void saveComplete(String taskName, String memo) {
        if (currentRecord == null) {
            throw new IllegalStateException("作業記録が開始されていません");
        }

        currentRecord.setEndTime(LocalDateTime.now());
        currentRecord.setTaskName(taskName);
        currentRecord.setMemo(memo);
        currentRecord.setTotalMinutes(currentRecord.calculateTotalMinutes());

        repository.save(currentRecord); // DBに保存
        currentRecord = null; // 状態リセット
    }
}
