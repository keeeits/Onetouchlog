package com.example.onetouch.controller;

import com.example.onetouch.entity.WorkRecord;
import com.example.onetouch.repository.WorkRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/record")
public class MainController {

    @Autowired
    private WorkRecordRepository repository;

    // ✅ 作業開始：DBに即保存し、IDと開始時刻を返す
    // Javaコードで LocalDateTime をそのままISO文字列で返す
    @PostMapping("/start")
    public ResponseEntity<?> startRecord() {
        try {
            LocalDateTime now = LocalDateTime.now();
            WorkRecord record = new WorkRecord();

            record.setStartTime(now.toLocalTime()); // DBには時間だけ保存してもよい
            record.setDate(now.toLocalDate());
            record.setDayOfWeek(getJapaneseDayOfWeek(now));

            WorkRecord saved = repository.save(record);

            // ✅ ISO形式 "2025-06-30T10:53:00" を返す
            return ResponseEntity.ok(Map.of(
                "id", saved.getId(),
                "startTime", now.toString()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("作業開始時にエラーが発生しました。");
        }
    }


    // ✅ 作業完了：idに紐づくレコードを更新して保存
    @PostMapping("/complete")
    public ResponseEntity<?> completeRecord(@RequestBody Map<String, Object> payload) {
        try {
            System.out.println("💡 payload: " + payload); // デバッグ出力

            Object rawId = payload.get("id");
            Long id;
            if (rawId instanceof Number) {
                id = ((Number) rawId).longValue();
            } else if (rawId instanceof String) {
                id = Long.parseLong((String) rawId);
            } else {
                return ResponseEntity.badRequest().body("IDの形式が不正です。");
            }

            Optional<WorkRecord> optionalRecord = repository.findById(id);
            if (optionalRecord.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("指定されたIDの作業記録が見つかりません。");
            }

            WorkRecord record = optionalRecord.get();

            OffsetDateTime offsetDateTime = OffsetDateTime.parse((String) payload.get("endTime"));
            record.setEndTime(offsetDateTime.toLocalTime());  // LocalTime型で保存

            record.setTaskName((String) payload.get("taskName"));
            record.setMemo((String) payload.get("memo"));

            Object rawMinutes = payload.get("totalMinutes");
            long totalMinutes = 0;
            if (rawMinutes instanceof Number) {
                totalMinutes = Math.round(((Number) rawMinutes).doubleValue());
            } else if (rawMinutes instanceof String) {
                totalMinutes = Math.round(Double.parseDouble((String) rawMinutes));
            } else {
                return ResponseEntity.badRequest().body("totalMinutes の形式が不正です。");
            }

            record.setTotalMinutes(totalMinutes);

            repository.save(record);
            return ResponseEntity.ok("作業記録を保存しました。");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("エラー内容: " + e.getMessage());
        }
    }

    // ✅ 日本語の曜日を返す
    private String getJapaneseDayOfWeek(LocalDateTime dt) {
        String[] days = {"日", "月", "火", "水", "木", "金", "土"};
        return days[dt.getDayOfWeek().getValue() % 7];
    }

    // ✅ 分を "HH:mm:00" 形式に整形（必要ならフロント用にも使える）
    private static String formatMinutesToHHMMSS(long minutes) {
        long hours = minutes / 60;
        long mins = minutes % 60;
        return String.format("%02d:%02d:00", hours, mins);
    }
}
