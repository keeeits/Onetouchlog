import { formatElapsedTime } from "../common/utils.js";

let timerInterval;
let startTime, breakStartTime;
let totalBreakTime = 0;
let isOnBreak = false;

window.addEventListener("DOMContentLoaded", () => {
  // サイドバー読み込み
  fetch("/common/sidebar.html")
    .then(res => res.text())
    .then(html => {
      document.getElementById("sidebar-container").innerHTML = html;
    });

  // 開始時刻取得
  const storedStartTime = localStorage.getItem("startTime");
  if (!storedStartTime) {
    alert("記録が開始されていません。");
    window.location.href = "/index.html";
    return;
  }

  startTime = new Date(storedStartTime);
  document.getElementById("start-time").textContent = startTime.toLocaleTimeString();

  // 経過時間タイマー開始
  timerInterval = setInterval(updateElapsedTime, 1000);

  // ボタン処理
  document.getElementById("break-button").addEventListener("click", toggleBreak);
  document.getElementById("end-button").addEventListener("click", endSession);
});

function updateElapsedTime() {
  if (isOnBreak) return;
  const now = new Date();
  const elapsedMs = now - startTime - totalBreakTime;
  document.getElementById("elapsed-time").textContent = formatElapsedTime(elapsedMs);
}

function toggleBreak() {
  const button = document.getElementById("break-button");
  const now = new Date();

  if (!isOnBreak) {
    isOnBreak = true;
    breakStartTime = now;
    button.textContent = "再開";
  } else {
    isOnBreak = false;
    totalBreakTime += now - breakStartTime;
    breakStartTime = null;
    button.textContent = "休憩";
  }
}

function endSession() {
  const memo = document.getElementById("memo").value;
  localStorage.setItem("memo", memo);
  localStorage.setItem("breakTimeMs", totalBreakTime);
  window.location.href = "/complete/complete.html";
}
