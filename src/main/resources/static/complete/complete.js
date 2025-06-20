import { completeWorkRecord } from "../common/api.js";
import { formatElapsedTime } from "../common/utils.js";

window.addEventListener("DOMContentLoaded", () => {
  // サイドバー読み込み
  fetch("/common/sidebar.html")
    .then(res => res.text())
    .then(html => {
      document.getElementById("sidebar-container").innerHTML = html;
    });

  // ✅ 開始・終了時刻の表示
  const startTimeStr = localStorage.getItem("startTime");
  const totalMinutes = localStorage.getItem("totalMinutes");
  const start = startTimeStr ? new Date(startTimeStr) : null;
  const end = new Date();  // 現在時刻が終了時刻

  if (start) {
    document.getElementById("start-time").textContent = start.toTimeString().slice(0, 5);
  }
  document.getElementById("end-time").textContent = end.toTimeString().slice(0, 5);

  // ✅ 作業時間の表示
  if (totalMinutes) {
    const formatted = formatElapsedTime(Number(totalMinutes) * 60 * 1000); // ← msに変換
    document.getElementById("duration").textContent = formatted;
  }

  // ✅ メモの表示（record.js で保存しておいた値を復元）
  const savedMemo = localStorage.getItem("memo");
  if (savedMemo) {
    document.getElementById("memo").value = savedMemo;
  }

  // フォーム処理
  const form = document.getElementById("complete-form");
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const id = localStorage.getItem("recordId");
    const taskName = document.getElementById("taskName").value;
    const memo = document.getElementById("memo").value;
    const evaluation = document.getElementById("evaluation").value;

    if (!id || !totalMinutes) {
      alert("開始記録が見つかりません。");
      return;
    }

    try {
      await completeWorkRecord({
        id,
        endTime: end.toISOString(),
        totalMinutes: Number(totalMinutes),
        taskName,
        memo,
        evaluation
      });

      localStorage.clear(); // 状態初期化
      alert("作業記録を保存しました！");
      window.location.href = "/index.html";
    } catch (err) {
      alert("保存に失敗しました：" + err.message);
    }
  });
});
