import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { getLink } from '../services/api'
import { Typography, Card, CardContent } from '@mui/material'

export default function Stats(){
  const { code } = useParams()
  const [link, setLink] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(()=>{
    async function load(){
      try {
        const data = await getLink(code)
        setLink(data)
      } catch(e) {
        console.error(e)
      } finally { setLoading(false) }
    }
    load()
  }, [code])

  if(loading) return <div>Loading...</div>
  if(!link) return <div>Not found</div>

  return (
    <Card>
      <CardContent>
        <Typography variant="h5">Stats for {link.code}</Typography>
        <Typography sx={{ mt:2 }}><strong>Target URL:</strong> {link.targetUrl}</Typography>
        <Typography sx={{ mt:1 }}><strong>Total clicks:</strong> {link.totalClicks}</Typography>
        <Typography sx={{ mt:1 }}><strong>Last clicked:</strong> {link.lastClicked ? new Date(link.lastClicked).toLocaleString() : '-'}</Typography>
      </CardContent>
    </Card>
  )
}
