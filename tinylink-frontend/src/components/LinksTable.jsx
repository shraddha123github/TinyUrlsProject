import React from 'react'
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton, Tooltip } from '@mui/material'
import OpenInNewIcon from '@mui/icons-material/OpenInNew'
import BarChartIcon from '@mui/icons-material/BarChart'
import DeleteIcon from '@mui/icons-material/Delete'

const API_BASE = import.meta.env.VITE_API_BASE || "http://localhost:8080";

export default function LinksTable({ links, onDelete, onOpen }) {
  // onOpen is optional: if provided it's called with (code) before opening backend url
  const handleOpen = (code) => {
    if (typeof onOpen === 'function') {
      try { onOpen(code); } catch (e) { console.warn('onOpen handler failed', e); }
    }
    // open backend redirect (this will hit your spring endpoint and increment)
    const url = `${API_BASE}/${encodeURIComponent(code)}`;
    window.open(url, "_blank", "noopener,noreferrer");
  };

  return (
    <TableContainer component={Paper} elevation={3}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Code</TableCell>
            <TableCell>Target URL</TableCell>
            <TableCell align="right">Clicks</TableCell>
            <TableCell>Last clicked</TableCell>
            <TableCell>Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {links.map(l => (
            <TableRow key={l.id} hover>
              <TableCell>{l.code}</TableCell>
              <TableCell style={{maxWidth:400, overflow:'hidden', textOverflow:'ellipsis', whiteSpace:'nowrap'}}>{l.targetUrl}</TableCell>
              <TableCell align="right">{l.totalClicks}</TableCell>
              <TableCell>{l.lastClicked ? new Date(l.lastClicked).toLocaleString() : '-'}</TableCell>
              <TableCell>
                <Tooltip title="Open redirect in new tab">
                  <IconButton onClick={() => handleOpen(l.code)} aria-label={`open-${l.code}`}>
                    <OpenInNewIcon />
                  </IconButton>
                </Tooltip>
                <Tooltip title="View stats">
                  <IconButton component="a" href={`/code/${l.code}`}><BarChartIcon /></IconButton>
                </Tooltip>
                <Tooltip title="Delete link">
                  <IconButton onClick={()=>onDelete(l.code)}><DeleteIcon color="error" /></IconButton>
                </Tooltip>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  )
}
