import React, { useEffect, useState } from "react";
import { findBranch } from "../../../services/adminService/Branch.js";
import { Link } from "react-router-dom";

export default function Branch() {
  const [branches, setBranches] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    findBranch()
      .then((data) => setBranches(data))
      .catch(() => setBranches([]))
      .finally(() => setLoading(false));
  }, []);

  const handleDelete = (id) => {
    if (window.confirm("Bạn có chắc muốn xóa chi nhánh này?")) {
      alert(`Xóa chi nhánh ID: ${id}`);
    }
  };

  return (
    <div className="container mt-4">
      <h3>Danh sách chi nhánh</h3>
      <Link className="btn btn-success mb-3" to={"/Admin/Branch/Create"}>
        Thêm chi nhánh
      </Link>
      {loading ? (
        <div>Đang tải...</div>
      ) : (
        <table className="table table-bordered mt-3">
          <thead>
            <tr>
              <th>ID</th>
              <th>Tên chi nhánh</th>
              <th>Địa chỉ</th>
              <th>Mô tả</th>
              <th>Trạng thái</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            {branches.map((branch) => (
              <tr key={branch.id}>
                <td>{branch.id}</td>
                <td>{branch.name}</td>
                <td>{branch.address}</td>
                <td>{branch.description}</td>
                <td>
                  <span className={`badge ${branch.status ? "bg-success" : "bg-secondary"}`}>
                    {branch.status ? "Hoạt động" : "Ngừng hoạt động"}
                  </span>
                </td>
                <td>
                  <Link className="btn btn-primary btn-sm me-2" to={`/Admin/Branch/Update/${branch.id}`}>
                    Sửa
                  </Link>
                  <button className="btn btn-danger btn-sm" onClick={() => handleDelete(branch.id)}>
                    Xóa
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
