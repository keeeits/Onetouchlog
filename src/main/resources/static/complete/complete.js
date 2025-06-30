import { completeWorkRecord } from "../common/api.js";
import { formatElapsedTime } from "../common/utils.js";

window.addEventListener("DOMContentLoaded", () => {
  fetch("/common/sidebar.html")
    .then(res => res.text())
    .then(html => {
      document.getElementById("sidebar-container").innerHTML = html;
    });

  const startTimeStr = localStorage.getItem("startTime"); // ISO形式
  const totalMs = localStorage.getItem("totalMs");

  const start = startTimeStr ? new Date(startTimeStr) : null;
  const end = new Date();

  if (start) {
    // ✅ 表示用に "HH:mm" に整形
    document.getElementById("start-time").textContent = start.toTimeString().slice(0, 5);
  }
  document.getElementById("end-time").textContent = end.toTimeString().slice(0, 5);

  if (totalMs) {
    const formatted = formatElapsedTime(Number(totalMs));
    document.getElementById("duration").textContent = formatted;
  }

  const savedMemo = localStorage.getItem("memo");
  if (savedMemo) {
    document.getElementById("memo").value = savedMemo;
  }

  const form = document.getElementById("complete-form");
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const id = localStorage.getItem("recordId");
    const taskName = document.getElementById("taskName").value;
    const memo = document.getElementById("memo").value;
    const evaluation = document.getElementById("evaluation").value;

    if (!id || !totalMs) {
      alert("開始記録が見つかりません。");
      return;
    }

    try {
      await completeWorkRecord({
        id: Number(id),
        endTime: end.toISOString(), // ✅ ISO形式で送信
        totalMinutes: Number(totalMs) / 60000,
        taskName,
        memo,
        evaluation
      });

      alert("作業記録を保存しました！");
    } catch (err) {
      alert("保存に失敗しました：" + err.message);
    } finally {
      localStorage.clear();
      window.location.href = "/index.html";
    }
  });
});
