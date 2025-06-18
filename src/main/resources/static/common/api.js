// 📤 作業記録の開始
export async function startWorkRecord() {
  const res = await fetch("/record/start", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    }
  });

  if (!res.ok) {
    throw new Error("作業記録の開始に失敗しました");
  }

  return await res.json(); // { id, startTime, date, dayOfWeek }
}

// 📥 作業記録の完了
export async function completeWorkRecord(data) {
  const res = await fetch("/record/complete", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  });

  if (!res.ok) {
    throw new Error("作業記録の完了に失敗しました");
  }

  return await res.json(); // { status: "ok" } など
}

// ⏳ 作業一覧の取得（例: 日付指定あり）
export async function fetchWorkRecordsByDate(date) {
  const res = await fetch(`/records?date=${date}`);

  if (!res.ok) {
    throw new Error("記録データの取得に失敗しました");
  }

  return await res.json(); // [{...}, {...}]
}
