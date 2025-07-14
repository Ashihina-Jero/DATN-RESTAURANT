import { Outlet } from 'react-router-dom';
import Header from "../components/admin/Header"


export default function AdminLayout() {
    return (
        <div>
            <div><Header/></div>
            <div>
                <Outlet />
            </div>
        </div>

  )
}
