import React from 'react';
import { Outlet } from 'react-router-dom';
import ClientHeader from '../components/clientComponents/Header';
import ClientFooter from '../components/clientComponents/Footer';
import '../styles/clientStyles/BackgroundClient.scss';
export default function ClientLayout() {
  return (
    <div className="">
      <div className="sticky-top shadow-sm">
        <ClientHeader />
      </div>
      <main className="background-color">
        <Outlet />
      </main>
      <ClientFooter />
    </div>
  );
}
