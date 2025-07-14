import React from 'react';
import HeaderAdmin from '../components/adminComponents/HeaderAdmin.tsx';
import SidebarAdmin from '../components/adminComponents/SidebarAdmin.tsx';
import { Outlet } from 'react-router-dom';
export default function indexLayout() {
  return (
    <div>
        <div className='container-fluid'>

          <div className='row'>
            <div className='col-sm-2 overflow-auto vh-100 border bg-secondary-subtle '>
                <SidebarAdmin />
            </div>
            <div className='col-sm-10 border bg-light'>
                    <HeaderAdmin/>
                <Outlet />
            </div>
            </div>
        </div>
    </div>
  )
}
