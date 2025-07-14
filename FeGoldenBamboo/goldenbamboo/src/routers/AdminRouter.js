import React from "react";
import { Routes, Route } from "react-router-dom";
import AdminLayout from "../layouts/AdminLayout.tsx";

// Chi nhánh
import BranchList from "../components/adminComponents/Branch/BranchList";
import BranchCreate from "../components/adminComponents/Branch/BranchCreate.tsx";
import BranchDetail from "../components/adminComponents/Branch/BranchDetail.tsx";

// Bàn ăn
import TableList from "../components/adminComponents/Table/TableList.tsx";
import TableCreate from "../components/adminComponents/Table/TableCreate.tsx";
import TableUpdate from "../components/adminComponents/Table/TableUpdate.tsx";
import TableDelete from "../components/adminComponents/Table/TableDelete";

// Thể loại món ăn
import CategoryList from "../components/adminComponents/Category/CategoryList";
import CategoryCreate from "../components/adminComponents/Category/CategoryCreate";
import CategoryUpdate from "../components/adminComponents/Category/CategoryUpdate";
import CategoryDelete from "../components/adminComponents/Category/CategoryDelete";

// Món ăn
import FoodList from "../components/adminComponents/Food/FoodList";
import FoodCreate from "../components/adminComponents/Food/FoodCreate";
import FoodUpdate from "../components/adminComponents/Food/FoodUpdate";
import FoodDelete from "../components/adminComponents/Food/FoodDelete";

// Combo
import ComboList from "../components/adminComponents/Combo/ComboList";
import ComboCreate from "../components/adminComponents/Combo/ComboCreate";
import ComboUpdate from "../components/adminComponents/Combo/ComboUpdate";
import ComboDelete from "../components/adminComponents/Combo/ComboDelete";

// Combo chi tiết
import ComboDetailList from "../components/adminComponents/ComboDetail/ComboDetailList";
import ComboDetailCreate from "../components/adminComponents/ComboDetail/ComboDetailCreate";
import ComboDetailUpdate from "../components/adminComponents/ComboDetail/ComboDetailUpdate";
import ComboDetailDelete from "../components/adminComponents/ComboDetail/ComboDetailDelete";

// Menu
import MenuList from "../components/adminComponents/Menu/MenuList";
import MenuCreate from "../components/adminComponents/Menu/MenuCreate";
import MenuUpdate from "../components/adminComponents/Menu/MenuUpdate";
import MenuDelete from "../components/adminComponents/Menu/MenuDelete";

// Menu chi tiết
import MenuDetailList from "../components/adminComponents/MenuDetail/MenuDetailList";
import MenuDetailCreate from "../components/adminComponents/MenuDetail/MenuDetailCreate";
import MenuDetailUpdate from "../components/adminComponents/MenuDetail/MenuDetailUpdate";
import MenuDetailDelete from "../components/adminComponents/MenuDetail/MenuDetailDelete";

// Giảm giá
import DiscountList from "../components/adminComponents/Discount/DiscountList";
import DiscountCreate from "../components/adminComponents/Discount/DiscountCreate";
import DiscountUpdate from "../components/adminComponents/Discount/DiscountUpdate";
import DiscountDelete from "../components/adminComponents/Discount/DiscountDelete";

// Giảm giá chi tiết
import DiscountDetailList from "../components/adminComponents/DiscountDetail/DiscountDetailList";
import DiscountDetailCreate from "../components/adminComponents/DiscountDetail/DiscountDetailCreate";
import DiscountDetailUpdate from "../components/adminComponents/DiscountDetail/DiscountDetailUpdate";
import DiscountDetailDelete from "../components/adminComponents/DiscountDetail/DiscountDetailDelete";

// Thông tin tài khoản
import UserInfo from "../pages/UserInfo.tsx";

export default function AdminRouter() {
  return (
    <Routes>
      <Route path="/" element={<AdminLayout />}>
        <Route path="UserInfo" element={<UserInfo />} />

        {/* Chi nhánh */}
        <Route path="Branch/List" element={<BranchList />} />
        <Route path="Branch/Create" element={<BranchCreate />} />
        <Route path="Branch/Update/:branchId" element={<BranchCreate />} />
        <Route path="Branch/Detail/:branchId" element={<BranchDetail />} />

        {/* Bàn ăn */}
        <Route path="Table/List" element={<TableList />} />
        <Route path="Table/Create" element={<TableCreate />} />
        <Route path="Table/Update/:tableId" element={<TableUpdate />} />
        <Route path="Table/Delete/:tableId" element={<TableDelete />} />

        {/* Thể loại món ăn */}
        <Route path="Category/List" element={<CategoryList />} />
        <Route path="Category/Create" element={<CategoryCreate />} />
        <Route path="Category/Update/:categoryId" element={<CategoryUpdate />} />
        <Route path="Category/Delete/:categoryId" element={<CategoryDelete />} />

        {/* Món ăn */}
        <Route path="Food/List" element={<FoodList />} />
        <Route path="Food/Create" element={<FoodCreate />} />
        <Route path="Food/Update/:foodId" element={<FoodUpdate />} />
        <Route path="Food/Delete/:foodId" element={<FoodDelete />} />

        {/* Combo */}
        <Route path="Combo/List" element={<ComboList />} />
        <Route path="Combo/Create" element={<ComboCreate />} />
        <Route path="Combo/Update/:comboId" element={<ComboUpdate />} />
        <Route path="Combo/Delete/:comboId" element={<ComboDelete />} />

        {/* Combo chi tiết */}
        <Route path="ComboDetail/List" element={<ComboDetailList />} />
        <Route path="ComboDetail/Create" element={<ComboDetailCreate />} />
        <Route path="ComboDetail/Update/:comboDetailId" element={<ComboDetailUpdate />} />
        <Route path="ComboDetail/Delete/:comboDetailId" element={<ComboDetailDelete />} />

        {/* Menu */}
        <Route path="Menu/List" element={<MenuList />} />
        <Route path="Menu/Create" element={<MenuCreate />} />
        <Route path="Menu/Update/:menuId" element={<MenuUpdate />} />
        <Route path="Menu/Delete/:menuId" element={<MenuDelete />} />

        {/* Menu chi tiết */}
        <Route path="MenuDetail/List" element={<MenuDetailList />} />
        <Route path="MenuDetail/Create" element={<MenuDetailCreate />} />
        <Route path="MenuDetail/Update/:menuDetailId" element={<MenuDetailUpdate />} />
        <Route path="MenuDetail/Delete/:menuDetailId" element={<MenuDetailDelete />} />

        {/* Giảm giá */}
        <Route path="Discount/List" element={<DiscountList />} />
        <Route path="Discount/Create" element={<DiscountCreate />} />
        <Route path="Discount/Update/:discountId" element={<DiscountUpdate />} />
        <Route path="Discount/Delete/:discountId" element={<DiscountDelete />} />

        {/* Giảm giá chi tiết */}
        <Route path="DiscountDetail/List" element={<DiscountDetailList />} />
        <Route path="DiscountDetail/Create" element={<DiscountDetailCreate />} />
        <Route path="DiscountDetail/Update/:discountDetailId" element={<DiscountDetailUpdate />} />
        <Route path="DiscountDetail/Delete/:discountDetailId" element={<DiscountDetailDelete />} />
      </Route>
    </Routes>
  );
}
