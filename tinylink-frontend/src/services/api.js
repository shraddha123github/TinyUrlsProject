const API_BASE = import.meta.env.VITE_API_BASE || "http://localhost:8080";

export async function listLinks(){ 
  const res = await fetch(`${API_BASE}/api/links`);
  if(!res.ok) throw new Error("Failed");
  return res.json();
}

export async function createLink(payload){
  const res = await fetch(`${API_BASE}/api/links`, {
    method: "POST",
    headers: {"Content-Type":"application/json"},
    body: JSON.stringify(payload)
  });
  return res;
}

export async function getLink(code){
  const res = await fetch(`${API_BASE}/api/links/${code}`);
  if(!res.ok) throw new Error("not found");
  return res.json();
}

export async function deleteLink(code){
  return fetch(`${API_BASE}/api/links/${code}`, { method: "DELETE" });
}
