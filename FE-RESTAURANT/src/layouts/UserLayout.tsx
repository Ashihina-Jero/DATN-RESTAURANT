import { Outlet } from 'react-router-dom';
import Header from '../components/user/Header.tsx';
import Footer from '../components/user/Footer.tsx'; 
export default function UserLayout() {
  return (
    <div>
      <div><Header/></div>
      <div>
        <Outlet />
      </div>
      <div><Footer/></div>
    </div>
  )
}
