import React, { useState, useEffect, useMemo } from "react";
import {
  getAllDiscountCombos,
  createDiscountCombo,
  updateDiscountCombo,
  deleteDiscountCombo,
  getAllCombos,
  getAllDiscounts,
} from "../../services/staffService/combodiscountService";

const DiscountComboManager = () => {
  const [discountCombos, setDiscountCombos] = useState([]);
  const [combos, setCombos] = useState([]);
  const [discounts, setDiscounts] = useState([]);
  const [formData, setFormData] = useState({
    comboId: "",
    discountId: "",
    discountPercentage: "",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [currentId, setCurrentId] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(5);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const [comboRes, discountRes, discountComboRes] = await Promise.all([getAllCombos(), getAllDiscounts(), getAllDiscountCombos()]);
      setCombos(comboRes);
      setDiscounts(discountRes);
      setDiscountCombos(discountComboRes);
    } catch (err) {
      setError("Không thể tải dữ liệu. Vui lòng thử lại sau.");
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const validateForm = () => {
    const { comboId, discountId, discountPercentage } = formData;

    if (!comboId || !discountId || !discountPercentage) {
      setError("Vui lòng điền đầy đủ thông tin");
      return false;
    }

    const percentage = parseFloat(discountPercentage);
    if (isNaN(percentage) || percentage < 1 || percentage > 100) {
      setError("Phần trăm giảm giá phải nằm trong khoảng từ 1 đến 100");
      return false;
    }

    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    setLoading(true);
    setError(null);
    setSuccess(null);

    try {
      const payload = {
        comboId: parseInt(formData.comboId),
        discountId: parseInt(formData.discountId),
        discountPercentage: parseFloat(formData.discountPercentage),
      };

      let message;
      if (isEditing && currentId) {
        await updateDiscountCombo(currentId, payload);
        message = "Cập nhật thành công!";
      } else {
        await createDiscountCombo(payload);
        message = "Tạo mới thành công!";
      }

      setSuccess(message);
      await loadData();
      resetForm();
    } catch (err) {
      setError(err.response?.data?.message || err.message || "Có lỗi xảy ra");
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (item) => {
    setFormData({
      comboId: item.comboId.toString(),
      discountId: item.discountId.toString(),
      discountPercentage: item.discountPercentage.toString(),
    });
    setIsEditing(true);
    setShowForm(true);
    setCurrentId(item.id);
    setError(null);
    setSuccess(null);
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const handleDelete = async (id) => {
    if (window.confirm("Bạn có chắc chắn muốn xóa?")) {
      setLoading(true);
      setError(null);
      try {
        await deleteDiscountCombo(id);
        setDiscountCombos((prev) => prev.filter((item) => item.id !== id));
        setSuccess("Xóa thành công!");
      } catch (err) {
        setError(err.response?.data?.message || "Lỗi khi xóa combo. Vui lòng kiểm tra lại.");
      } finally {
        setLoading(false);
      }
    }
  };

  const resetForm = () => {
    setFormData({ comboId: "", discountId: "", discountPercentage: "" });
    setIsEditing(false);
    setCurrentId(null);
    setShowForm(false);
    setError(null);
    setSuccess(null);
  };

  const comboNames = useMemo(() => {
    return combos.reduce((acc, combo) => {
      acc[combo.id] = combo.name;
      return acc;
    }, {});
  }, [combos]);

  const discountNames = useMemo(() => {
    return discounts.reduce((acc, discount) => {
      acc[discount.id] = discount.name;
      return acc;
    }, {});
  }, [discounts]);

  const getComboName = (id) => comboNames[id] || `Combo ${id}`;
  const getDiscountName = (id) => discountNames[id] || `Giảm giá ${id}`;

  const indexOfLastCombo = currentPage * itemsPerPage;
  const indexOfFirstCombo = indexOfLastCombo - itemsPerPage;
  const currentDiscountCombos = discountCombos.slice(indexOfFirstCombo, indexOfLastCombo);
  const totalPages = Math.ceil(discountCombos.length / itemsPerPage);

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Quản lý Combo giảm giá</h2>
        <button
          onClick={() => {
            setShowForm(!showForm);
            if (showForm) resetForm();
          }}
          className={`btn ${showForm ? "btn-secondary" : "btn-primary"}`}
        >
          {showForm ? "Đóng form" : "Thêm mới"}
        </button>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}

      {showForm && (
        <div className="card mb-4">
          <div className="card-body">
            <h4 className="card-title">{isEditing ? "Chỉnh sửa Combo giảm giá" : "Thêm Combo giảm giá mới"}</h4>
            <form onSubmit={handleSubmit}>
              <div className="row g-3">
                <div className="col-md-4">
                  <label className="form-label">Combo</label>
                  <select name="comboId" value={formData.comboId} onChange={handleChange} className="form-select" required>
                    <option value="">Chọn combo</option>
                    {combos.map((combo) => (
                      <option key={combo.id} value={combo.id}>
                        {combo.name}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="col-md-4">
                  <label className="form-label">Giảm giá</label>
                  <select name="discountId" value={formData.discountId} onChange={handleChange} className="form-select" required>
                    <option value="">Chọn giảm giá</option>
                    {discounts.map((discount) => (
                      <option key={discount.id} value={discount.id}>
                        {discount.name}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="col-md-4">
                  <label className="form-label">Phần trăm giảm giá</label>
                  <div className="input-group">
                    <input
                      type="number"
                      name="discountPercentage"
                      value={formData.discountPercentage}
                      onChange={handleChange}
                      className="form-control"
                      min="1"
                      max="100"
                      step="0.1"
                      required
                    />
                    <span className="input-group-text">%</span>
                  </div>
                </div>
              </div>

              <div className="d-flex gap-2 mt-3">
                <button type="submit" className="btn btn-primary">
                  {isEditing ? "Cập nhật" : "Thêm mới"}
                </button>
                <button type="button" onClick={resetForm} className="btn btn-outline-secondary">
                  Hủy
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      <div className="table-responsive">
        <table className="table table-striped table-hover">
          <thead className="table-light">
            <tr>
              <th>Combo</th>
              <th>Giảm giá</th>
              <th>Phần trăm (%)</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            {currentDiscountCombos.map((item) => (
              <tr key={item.id}>
                <td>{getComboName(item.comboId)}</td>
                <td>{getDiscountName(item.discountId)}</td>
                <td>{item.discountPercentage}%</td>
                <td>
                  <div className="d-flex gap-2">
                    <button onClick={() => handleEdit(item)} className="btn btn-sm btn-warning">
                      Sửa
                    </button>
                    <button onClick={() => handleDelete(item.id)} className="btn btn-sm btn-danger">
                      Xóa
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <nav aria-label="Page navigation">
        <ul className="pagination justify-content-center mt-4">
          {Array.from({ length: totalPages }, (_, index) => (
            <li key={index} className={`page-item ${currentPage === index + 1 ? "active" : ""}`}>
              <button className="page-link" onClick={() => setCurrentPage(index + 1)}>
                {index + 1}
              </button>
            </li>
          ))}
        </ul>
      </nav>

      {loading && (
        <div className="text-center my-4">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Đang tải...</span>
          </div>
        </div>
      )}
    </div>
  );
};

export default DiscountComboManager;
