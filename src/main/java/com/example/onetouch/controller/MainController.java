package com.example.onetouch.controller;

import com.example.onetouch.entity.WorkRecord;
import com.example.onetouch.service.WorkRecordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/record")
public class MainController {

    private final WorkRecordManager manager;

    @Autowired
    public MainController(WorkRecordManager manager) {
        this.manager = manager;
    }

    // 作業記録の開始
    @PostMapping("/start")
    public ResponseEntity<WorkRecord> startRecord() {
        WorkRecord record = manager.saveStart();
        return ResponseEntity.ok(record);
    }

    // 作業記録の完了
    @PostMapping("/complete")
    public ResponseEntity<Void> completeRecord(@RequestBody WorkRecord record) {
        manager.saveComplete(record);
        return ResponseEntity.ok().build();
    }
}
