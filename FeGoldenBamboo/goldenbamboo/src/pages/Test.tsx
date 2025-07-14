import React, { useEffect, useState } from 'react'
import axios from 'axios'

interface User {
  id: number;
  name: string;
  phone: string;
  status: boolean;
}

export default function Test() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axios.get<User[]>('http://localhost:8080/')
      .then(res => setUsers(res.data))
      .catch(() => setUsers([]))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="container mt-4">
      <h3>Danh sách người dùng</h3>
      {loading ? (
        <div>Đang tải...</div>
      ) : (
        <table className="table table-bordered mt-3">
          <thead>
            <tr>
              <th>ID</th>
              <th>Tên</th>
              <th>Số điện thoại</th>
              <th>Trạng thái</th>
            </tr>
          </thead>
          <tbody>
            {users.map(user => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.name}</td>
                <td>{user.phone}</td>
                <td>{user.status ? "Hoạt động" : "Không hoạt động"}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}
