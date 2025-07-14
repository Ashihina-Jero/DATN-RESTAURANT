import React, { useState } from 'react'
import { createBranch } from '../../../services/adminService/Branch.js';

export default function BranchCreate() {
  const [name, setName] = useState('');
  const [address, setAddress] = useState('');
  const [description, setDescription] = useState('');
  const [status, setStatus] = useState(true);
  const [message, setMessage] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await createBranch({ name, address, description, status });
      setMessage('Tạo chi nhánh thành công!');
      setName('');
      setAddress('');
      setDescription('');
      setStatus(true);
    } catch {
      setMessage('Có lỗi xảy ra khi tạo chi nhánh.');
    }
  };

  return (
    <div className="container mt-4" style={{ maxWidth: 500 }}>
      <h3 className="mb-3">Tạo chi nhánh mới</h3>
      {message && <div className="alert alert-info">{message}</div>}
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">Tên chi nhánh</label>
          <input
            type="text"
            className="form-control"
            value={name}
            onChange={e => setName(e.target.value)}
            required
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Địa chỉ</label>
          <input
            type="text"
            className="form-control"
            value={address}
            onChange={e => setAddress(e.target.value)}
            required
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Mô tả</label>
          <textarea
            className="form-control"
            value={description}
            onChange={e => setDescription(e.target.value)}
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Trạng thái</label>
          <select
            className="form-select"
            value={status ? 'true' : 'false'}
            onChange={e => setStatus(e.target.value === 'true')}
          >
            <option value="true">Hoạt động</option>
            <option value="false">Ngừng hoạt động</option>
          </select>
        </div>
        <button type="submit" className="btn btn-success w-100">Tạo chi nhánh</button>
      </form>
    </div>
  )
}