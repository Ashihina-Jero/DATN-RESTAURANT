import React from 'react'
import { Outlet } from 'react-router-dom'

export default function StaffLayout() {
  return (
    <div>
      <div><Outlet/></div>
    </div>
  )
}


