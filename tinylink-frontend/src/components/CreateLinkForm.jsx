import React, { useState } from 'react'
import { Box, TextField, Button, Tooltip } from '@mui/material'
import { createLink } from '../services/api'

export default function CreateLinkForm({ onCreated }) {
  const [targetUrl, setTargetUrl] = useState("")
  const [code, setCode] = useState("")
  const [error, setError] = useState(null)
  const CODE_REGEX = /^[A-Za-z0-9]{6,8}$/

  async function handleSubmit(e){
    e.preventDefault()
    setError(null)
    if(!targetUrl) { setError("Enter target URL"); return; }
    if(code && !CODE_REGEX.test(code)){ setError("Code must be 6-8 alphanumeric"); return; }

    const res = await createLink({ targetUrl, code: code || undefined })
    if(!res.ok){
      const body = await res.json()
      setError(body?.error || "Create failed"); return;
    }
    setTargetUrl(""); setCode("");
    onCreated()
  }

  return (
    <Box component="form" onSubmit={handleSubmit} sx={{ display:'flex', gap:2, alignItems:'center', mb:3 }}>
      <Tooltip title="Paste the full URL (must start with http:// or https://)">
        <TextField label="Target URL" value={targetUrl} onChange={e=>setTargetUrl(e.target.value)} variant="outlined" fullWidth />
      </Tooltip>
      <Tooltip title="Optional: 6-8 alphanumeric characters. Leave blank to auto-generate">
        <TextField label="Custom code" value={code} onChange={e=>setCode(e.target.value)} variant="outlined" sx={{ width:220 }} />
      </Tooltip>
      <Button variant="contained" color="primary" type="submit">Create</Button>
      {error && <div style={{color:'red'}}>{error}</div>}
    </Box>
  )
}
