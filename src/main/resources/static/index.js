// サイドバー読み込み
window.addEventListener("DOMContentLoaded", () => {
  fetch("common/sidebar.html")
    .then(res => res.text())
    .then(html => {
      document.getElementById("sidebar-container").innerHTML = html;
    });

  // 開始ボタンの処理
  const startBtn = document.getElementById("start-btn");
  startBtn.addEventListener("click", () => {
    fetch("/record/start", {
      method: "POST"
    })
    .then(response => response.json())
    .then(data => {
      localStorage.setItem("recordId", data.id);
      localStorage.setItem("startTime", data.startTime);
      window.location.href = "record/record.html";
    })
    .catch(err => {
      alert("作業開始に失敗しました");
      console.error(err);
    });
  });
});
