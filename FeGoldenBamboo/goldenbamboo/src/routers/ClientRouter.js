import { Routes, Route } from "react-router-dom";
import ClientLayout from "../layouts/ClientLayout.tsx";
import Home from "../components/clientComponents/Home.tsx";
import Search from "../components/clientComponents/Search.tsx";
import Cart from "../components/clientComponents/Cart.tsx";
import Account from "../components/clientComponents/Account.tsx";
export default function ClientRouter() {
  return (
    <Routes>
      <Route path="/" element={<ClientLayout />}>
        <Route path="Home" element={<Home />} />
        <Route path="Search" element={<Search />} />
        <Route path="Cart" element={<Cart />} />
        <Route path="Account" element={<Account />} />
        
      </Route>
    </Routes>
  );
}
