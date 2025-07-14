import { Routes, Route } from "react-router-dom";
import StaffLayout from "../layouts/StaffLayout.tsx";
import Index from "../components/staffComponents/Index.tsx";
import StaffDashboard from "../components/staffComponents/StaffDashboard.tsx";

export default function StaffRouter() {
  return (
    <Routes>
      <Route path="/" element={<StaffLayout />}>
        <Route path="Branch/:branchId/Dashboard" element={<StaffDashboard />} />
        <Route path="/Branch/:branchId/Table/:tableId/Order/:orderId/Category/:categoryId" element={<Index />} />
      </Route>
    </Routes>
  );
}
