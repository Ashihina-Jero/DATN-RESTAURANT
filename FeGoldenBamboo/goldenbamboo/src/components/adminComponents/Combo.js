import React, { useState, useEffect } from "react";
import { getCombos, createCombo, updateCombo, deleteCombo } from "../../services/staffService/comboService";  
import { getAllDiscountCombos } from '../../services/staffService/combodiscountService';

const ComboTable = () => {
  const [combos, setCombos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);
  const [deleteId, setDeleteId] = useState(null);
  const [formState, setFormState] = useState({
    id: null,
    name: "",
    price: "",
    description: "",
    status: "active",
    image: null,
    currentImage: null,
    imageName: "Chưa chọn file",
  });

  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(5);
  
  const [errors, setErrors] = useState({
    name: "",
    price: "",
    description: "",
  });

  useEffect(() => {
    fetchCombos();
  }, []);

  const fetchCombos = async () => {
    try {
      const [comboData, discountComboData] = await Promise.all([
        getCombos(),
        getAllDiscountCombos()
      ]);
  
      const combosWithDiscount = comboData.map((combo) => {
        const discount = discountComboData.find(dc => dc.comboId === combo.id);
        const discountPercentage = discount ? discount.discountPercentage : 0;
        const discountedPrice = combo.price * (1 - discountPercentage / 100);
  
        return {
          ...combo,
          discountPercentage,
          discountedPrice,
        };
      });
  
      setCombos(combosWithDiscount);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormState((prev) => ({ ...prev, [name]: value }));
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setFormState((prev) => ({
      ...prev,
      image: file,
      currentImage: file ? URL.createObjectURL(file) : prev.currentImage,
      imageName: file ? file.name : "Chưa chọn file",
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({ name: "", price: "", description: "" });

    let hasError = false;

    if (!formState.name) {
      setErrors((prev) => ({ ...prev, name: "Tên combo không được để trống." }));
      hasError = true;
    }

    if (!formState.price || formState.price <= 0) {
      setErrors((prev) => ({ ...prev, price: "Giá phải lớn hơn 0." }));
      hasError = true;
    }

    if (!formState.description) {
      setErrors((prev) => ({ ...prev, description: "Mô tả không được để trống." }));
      hasError = true;
    }

    if (hasError) return;

    try {
      const formData = new FormData();
      formData.append("name", formState.name);
      formData.append("price", parseFloat(formState.price));
      formData.append("description", formState.description);
      formData.append("status", formState.status);
      
      if (formState.image) {
        formData.append("image", formState.image);
      }

      if (formState.id) {
        const updatedCombo = await updateCombo(formState.id, formData);
        setCombos((prev) =>
          prev.map((c) => (c.id === updatedCombo.id ? updatedCombo : c))
        );
      } else {
        const newCombo = await createCombo(formData);
        setCombos((prev) => [...prev, newCombo]);
      }
      resetForm();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleEdit = (combo) => {
    setFormState({
      id: combo.id,
      name: combo.name,
      price: combo.price,
      description: combo.description,
      status: combo.status ? "active" : "inactive",
      image: null,
      currentImage: combo.image ? `${combo.image}` : null,
      imageName: combo.image ? combo.image : "Chưa có ảnh",
    });
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    setLoading(true);
    try {
      await deleteCombo(id);
      fetchCombos();
      setShowDeleteConfirmation(false);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const resetForm = () => {
    setFormState({
      id: null,
      name: "",
      price: "",
      description: "",
      status: "active",
      image: null,
      currentImage: null,
      imageName: "Chưa chọn file",
    });
    setShowForm(false);
    setError(null);
    setErrors({ name: "", price: "", description: "" });
  };

  const filteredCombos = combos.filter(combo =>
    combo.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const indexOfLastCombo = currentPage * itemsPerPage;
  const indexOfFirstCombo = indexOfLastCombo - itemsPerPage;
  const currentCombos = filteredCombos.slice(indexOfFirstCombo, indexOfLastCombo);
  const totalPages = Math.ceil(filteredCombos.length / itemsPerPage);

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  if (loading) {
    return (
      <div className="text-center">
        <div className="spinner-border text-primary"></div>
      </div>
    );
  }

  if (error) {
    return <div className="alert alert-danger">Error: {error}</div>;
  }

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Quản lý Combo</h2>
        <button
          className="btn btn-primary"
          onClick={() => setShowForm(true)}
        >
          Thêm Combo
        </button>
      </div>

      <div className="mb-3">
        <div className="input-group">
          <input
            type="text"
            className="form-control"
            placeholder="Tìm kiếm theo tên combo..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
      </div>

      {showForm && (
        <form
          onSubmit={handleSubmit}
          className="mb-4 border p-4 rounded shadow-sm bg-light"
        >
          <h4>{formState.id ? "Chỉnh sửa Combo" : "Thêm Combo mới"}</h4>

          <div className="mb-3">
            <label className="form-label">Tên combo</label>
            <input
              type="text"
              className={`form-control ${errors.name ? 'is-invalid' : ''}`}
              name="name"
              value={formState.name}
              onChange={handleInputChange}
            />
            {errors.name && <div className="invalid-feedback">{errors.name}</div>}
          </div>

          <div className="mb-3">
            <label className="form-label">Giá (VND)</label>
            <input
              type="number"
              className={`form-control ${errors.price ? 'is-invalid' : ''}`}
              name="price"
              value={formState.price}
              onChange={handleInputChange}
              min="0"
            />
            {errors.price && <div className="invalid-feedback">{errors.price}</div>}
          </div>

          <div className="mb-3">
            <label className="form-label">Mô tả</label>
            <textarea
              className={`form-control ${errors.description ? 'is-invalid' : ''}`}
              name="description"
              value={formState.description}
              onChange={handleInputChange}
              rows="3"
            />
            {errors.description && <div className="invalid-feedback">{errors.description}</div>}
          </div>

          <div className="mb-3">
            <label className="form-label">Trạng thái</label>
            <select
              className="form-control"
              name="status"
              value={formState.status}
              onChange={handleInputChange}
            >
              <option value="active">Hoạt động</option>
              <option value="inactive">Ngừng hoạt động</option>
            </select>
          </div>

          <div className="mb-3">
            <label className="form-label">Ảnh</label>
            <div className="input-group">
              <input
                type="file"
                className="form-control"
                id="imageInput"
                accept="image/*"
                onChange={handleFileChange}
                style={{ display: "none" }}
              />
              <input
                type="text"
                className="form-control"
                value={formState.imageName}
                readOnly
                placeholder="Chọn ảnh..."
              />
              <button
                className="btn btn-outline-secondary"
                type="button"
                onClick={() => document.getElementById("imageInput").click()}
              >
                Chọn file
              </button>
            </div>

            {formState.currentImage && (
              <div className="mt-3">
                <p className="mb-1">
                  Ảnh {formState.id ? "hiện tại" : "xem trước"}:
                </p>
                <img
                  src={formState.currentImage}
                  alt="Preview"
                  className="img-thumbnail"
                  style={{
                    maxWidth: "150px",
                    maxHeight: "150px",
                    objectFit: "cover",
                  }}
                />
                <p className="text-muted small mt-1">
                  {formState.id
                    ? "Chọn ảnh mới để thay đổi"
                    : "Ảnh sẽ được tải lên"}
                </p>
              </div>
            )}
          </div>

          <div className="d-flex justify-content-end">
            <button
              type="button"
              className="btn btn-secondary me-2"
              onClick={resetForm}
            >
              Hủy
            </button>
            <button type="submit" className="btn btn-success">
              {formState.id ? "Cập nhật" : "Thêm mới"}
            </button>
          </div>
        </form>
      )}

      {showDeleteConfirmation && (
        <div className="modal show d-block" style={{ backgroundColor: "rgba(0, 0, 0, 0.5)" }}>
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Xác nhận</h5>
                <button
                  type="button"
                  className="btn-close"
                  onClick={() => setShowDeleteConfirmation(false)}
                ></button>
              </div>
              <div className="modal-body">
                <p>Bạn có chắc chắn muốn xóa combo này không?</p>
              </div>
              <div className="modal-footer">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => setShowDeleteConfirmation(false)}
                >
                  Hủy bỏ
                </button>
                <button
                  type="button"
                  className="btn btn-danger"
                  onClick={() => handleDelete(deleteId)}
                >
                  Xóa
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      <div className="table-responsive">
        <table className="table table-hover align-middle">
          <thead className="table-dark">
            <tr>
              <th>Tên</th>
              <th>Giá</th>
              <th>Mô tả</th>
              <th>Trạng thái</th>
              <th>Ảnh</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            {currentCombos.map((combo) => (
              <tr key={combo.id}>
                <td>{combo.name}</td>
                <td>
                  {combo.discountPercentage > 0 ? (
                    <>
                      <span className="text-danger fw-bold">
                        {combo.discountedPrice.toLocaleString()} VND
                      </span>
                      <br />
                      <small className="text-muted text-decoration-line-through">
                        {combo.price.toLocaleString()} VND
                      </small>
                      <br />
                      <span className="badge bg-warning text-dark">
                        -{combo.discountPercentage}%
                      </span>
                    </>
                  ) : (
                    <>{combo.price.toLocaleString()} VND</>
                  )}
                </td>
                <td
                  style={{ maxWidth: "200px" }}
                  className="text-truncate"
                  title={combo.description}
                >
                  {combo.description}
                </td>
                <td>
                  <span
                    className={`badge ${
                      combo.status ? "bg-success" : "bg-secondary"
                    }`}
                  >
                    {combo.status ? "Hoạt động" : "Ngừng hoạt động"}
                  </span>
                </td>
                <td>
                  {combo.image ? (
                    <img
                      src={combo.image}
                      alt={combo.name}
                      className="img-thumbnail"
                      style={{ width: "70px", height: "70px", objectFit: "cover" }}
                    />
                  ) : (
                    <span className="text-secondary">[Không có ảnh]</span>
                  )}
                </td>
                <td>
                  <div className="d-flex">
                    <button
                      className="btn btn-warning btn-sm me-2"
                      onClick={() => handleEdit(combo)}
                      title="Chỉnh sửa"
                    >
                      Sửa
                    </button>
                    <button
                      className="btn btn-danger btn-sm"
                      onClick={() => {
                        setDeleteId(combo.id);
                        setShowDeleteConfirmation(true);
                      }}
                      title="Xóa"
                    >
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
        <ul className="pagination justify-content-center">
          {Array.from({ length: totalPages }, (_, index) => (
            <li key={index} className={`page-item ${currentPage === index + 1 ? 'active' : ''}`}>
              <button
                className="page-link"
                onClick={() => handlePageChange(index + 1)}
              >
                {index + 1}
              </button>
            </li>
          ))}
        </ul>
      </nav>
    </div>
  );
};

export default ComboTable;
