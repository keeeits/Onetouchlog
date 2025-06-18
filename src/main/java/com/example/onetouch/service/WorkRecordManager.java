package com.example.onetouch.service;

import java.time.LocalDateTime;

import com.example.onetouch.entity.WorkRecord;

public class WorkRecordManager {
    private WorkRecord currentRecord;
    private final WorkRecordDatabase database;

    public WorkRecordManager(WorkRecordDatabase database) {
        this.database = database;
    }

    // 作業開始
    public void saveStart() {
        if (currentRecord != null) {
            throw new IllegalStateException("作業記録はすでに開始されています");
        }
        //WorkRecordクラス内にある、作業開始時に保存したい情報を獲得するメソッド呼び出す
        currentRecord = new WorkRecord();
        currentRecord.setStartTime(LocalDateTime.now());
    }

    // 作業終了
    public void saveComplete(String description) {
        if (currentRecord == null) {
            throw new IllegalStateException("作業記録が開始されていません");
        }
        currentRecord.setEndTime(LocalDateTime.now());
        //WorkRecordクラス内にある、作業終了時に保存したい情報を獲得するメソッド呼び出す
        currentRecord.setDescription(description);
        database.saveComplete(currentRecord);
        currentRecord = null; // 状態リセット
    }
}
