import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { getTables } from '../../../services/adminService/Table.tsx'

export default function ListTable() {
  const { branchId } = useParams<{ branchId: string }>();
  const [tables, setTables] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!branchId) return;
    getTables(branchId)
      .then(data => setTables(data))
      .catch(() => setTables([]))
      .finally(() => setLoading(false));
  }, [branchId]);

  return (
    <div className="container mt-4">
      <h3>Danh sách bàn</h3>
      {loading ? (
        <div>Đang tải...</div>
      ) : (
        <table className="table table-bordered mt-3">
          <thead>
            <tr>
              <th>ID</th>
              <th>Số bàn</th>
              <th>Trạng thái</th>
              <th>Chi nhánh</th>
            </tr>
          </thead>
          <tbody>
            {tables.map((table: any) => (
              <tr key={table.id}>
                <td>{table.id}</td>
                <td>{table.number}</td>
                <td>
                  <span className={`badge ${table.status ? 'bg-success' : 'bg-secondary'}`}>
                    {table.status ? 'Đang hoạt động' : 'Ngừng hoạt động'}
                  </span>
                </td>
                <td>{table.branchId}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}
