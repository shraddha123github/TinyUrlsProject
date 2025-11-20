import React, { useEffect, useState } from 'react'
import CreateLinkForm from '../components/CreateLinkForm'
import LinksTable from '../components/LinksTable'
import { listLinks, deleteLink } from '../services/api'
import { Typography, Box, CircularProgress } from '@mui/material'

export default function Dashboard(){
  const [links, setLinks] = useState([])
  const [loading, setLoading] = useState(true)

  async function fetchLinks(){
    setLoading(true)
    try {
      const data = await listLinks()
      setLinks(data)
    } catch(e) {
      console.error(e)
    } finally { setLoading(false) }
  }

  useEffect(()=>{ fetchLinks() }, [])

  async function handleDelete(code){
    if(!confirm(`Delete ${code}?`)) return;
    await deleteLink(code)
    fetchLinks()
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom sx={{ mb:2 }}>TinyLink — Dashboard</Typography>
      <CreateLinkForm onCreated={fetchLinks} />
      {loading ? <CircularProgress /> : <LinksTable links={links} onDelete={handleDelete} />}
    </Box>
  )
}
