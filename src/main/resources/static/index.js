// サイドバー読み込み
window.addEventListener("DOMContentLoaded", () => {
  fetch("common/sidebar.html")
    .then(res => res.text())
    .then(html => {
      document.getElementById("sidebar-container").innerHTML = html;
    });

  // 開始ボタンの処理
  const startBtn = document.getElementById("start-btn");
  startBtn.addEventListener("click", async () => {
  try {
    const response = await fetch("http://localhost:8080/record/start", {
      method: "POST"
    });

    if (!response.ok) {
      const msg = await response.text();

      // 🔽 すでに作業中だったら遷移だけする（保存はされない）
      if (msg.includes("すでに作業中です")) {
        alert("すでに作業中の記録があります。続きから開始します。");
        window.location.href = "record/record.html";
        return;
      }

      // 他のエラーは通常通り止める
      throw new Error(msg);
    }

    const data = await response.json();
    localStorage.setItem("recordId", data.id);
    localStorage.setItem("startTime", data.startTime);
    window.location.href = "record/record.html";
  } catch (err) {
      alert("作業開始に失敗しました: " + err.message);
      console.error(err);
    }
  });
});
