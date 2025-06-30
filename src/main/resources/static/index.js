// サイドバー読み込み
window.addEventListener("DOMContentLoaded", () => {
  fetch("common/sidebar.html")
    .then(res => res.text())
    .then(html => {
      document.getElementById("sidebar-container").innerHTML = html;
    });

  const startBtn = document.getElementById("start-btn");
  startBtn.addEventListener("click", async () => {
    // ✅ 前回の残データをクリア
    localStorage.removeItem("startTime");
    localStorage.removeItem("totalMs");
    localStorage.removeItem("memo");

    try {
      const response = await fetch("http://localhost:8080/record/start", {
        method: "POST"
      });

      if (!response.ok) {
        const msg = await response.text();

        if (msg.includes("すでに作業中です")) {
          alert("すでに作業中の記録があります。続きから開始します。");
          window.location.href = "record/record.html";
          return;
        }

        throw new Error(msg);
      }

      const data = await response.json();
      console.log("サーバーから受け取ったレスポンス:", data);

      if (!data.startTime) {
        throw new Error("サーバーから開始時刻が受け取れませんでした。");
      }

      // ✅ ISO形式（"2025-06-30T11:23:45.000Z"）で保存
      localStorage.setItem("recordId", data.id);
      localStorage.setItem("startTime", data.startTime);  // ←そのまま保存

      window.location.href = "record/record.html";
    } catch (err) {
      alert("作業開始に失敗しました: " + err.message);
      console.error(err);
    }
  });
});
