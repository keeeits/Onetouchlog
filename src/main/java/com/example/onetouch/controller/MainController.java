package com.example.onetouch.controller;

import com.example.onetouch.entity.WorkRecord;
import com.example.onetouch.repository.WorkRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/record")
public class MainController {

    @Autowired
    private WorkRecordRepository repository;

    private WorkRecord currentRecord;

    // 作業記録の開始
    @PostMapping("/start")
    public ResponseEntity<?> startRecord() {
        if (currentRecord != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)  // 409 Conflict
                    .body("すでに作業中です。");
        }

        currentRecord = new WorkRecord();
        currentRecord.setStartTime(LocalDateTime.now());
        currentRecord.setDate(LocalDate.now());
        currentRecord.setDayOfWeek(LocalDateTime.now().getDayOfWeek().toString());

        return ResponseEntity.ok(currentRecord);  // 200 OK with JSON
    }

    // 作業記録の完了
    @PostMapping("/complete")
    public ResponseEntity<?> completeRecord(@RequestBody Map<String, Object> payload) {
        if (currentRecord == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)  // 400 Bad Request
                    .body("作業が開始されていません。");
        }

        currentRecord.setEndTime(LocalDateTime.now());
        currentRecord.setTaskName((String) payload.get("taskName"));
        currentRecord.setMemo((String) payload.get("memo"));
        currentRecord.setTotalMinutes(currentRecord.calculateTotalMinutes());

        repository.save(currentRecord);
        currentRecord = null;

        return ResponseEntity.ok("作業記録を保存しました。");
    }
}
