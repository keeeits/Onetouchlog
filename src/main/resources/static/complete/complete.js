import { completeWorkRecord } from "../common/api.js";

window.addEventListener("DOMContentLoaded", () => {
  // サイドバー読み込み
  fetch("/common/sidebar.html")
    .then(res => res.text())
    .then(html => {
      document.getElementById("sidebar-container").innerHTML = html;
    });

  const form = document.getElementById("complete-form");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const id = localStorage.getItem("recordId");
    const startTime = new Date(localStorage.getItem("startTime"));
    const endTime = new Date();
    const taskId = document.getElementById("taskId").value;
    const memo = document.getElementById("memo").value;
    const evaluation = document.getElementById("evaluation").value;

    if (!id || !startTime) {
      alert("開始記録が見つかりません。");
      return;
    }

    try {
      await completeWorkRecord({
        id,
        endTime: endTime.toISOString(),
        taskId,
        memo,
        evaluation
      });

      localStorage.clear(); // 記録終了 → 状態初期化
      alert("作業記録を保存しました！");
      window.location.href = "/index.html";
    } catch (err) {
      alert("保存に失敗しました：" + err.message);
    }
  });
});
