import React, { useState, useEffect, useCallback } from "react";
import * as discountService from "../../services/staffService/discountService.js";

const formatDisplayDateTime = (dateString) => {
  if (!dateString) return "Ngày không hợp lệ";
  const date = new Date(dateString);
  if (isNaN(date.getTime())) return "Ngày không hợp lệ";

  const pad = (num) => String(num).padStart(2, "0");
  return `${pad(date.getDate())}/${pad(
    date.getMonth() + 1
  )}/${date.getFullYear()} ${pad(date.getHours())}:${pad(
    date.getMinutes()
  )}:${pad(date.getSeconds())}`;
};

const convertToDateTimeLocal = (dateString) => {
  if (!dateString) return "";
  const date = new Date(dateString);
  if (isNaN(date.getTime())) return "";

  const pad = (num) => String(num).padStart(2, "0");
  
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
};

const Discount = () => {
  const [discounts, setDiscounts] = useState([]);
  const [filteredDiscounts, setFilteredDiscounts] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [notification, setNotification] = useState({
    show: false,
    message: "",
    type: "",
  });
  const [formState, setFormState] = useState({
    id: null,
    name: "",
    startDate: "",
    endDate: "",
    status: true,
  });
  
  const [errors, setErrors] = useState({
    name: "",
    startDate: "",
    endDate: "",
  });

  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(5);

  const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);
  const [deleteId, setDeleteId] = useState(null);

  const showNotification = useCallback((message, type = "success") => {
    setNotification({ show: true, message, type });
    const timer = setTimeout(() => {
      setNotification((prev) => ({ ...prev, show: false }));
    }, 3000);
    return () => clearTimeout(timer);
  }, []);

  const closeNotification = useCallback(() => {
    setNotification((prev) => ({ ...prev, show: false }));
  }, []);

  const fetchDiscounts = useCallback(async () => {
    setIsLoading(true);
    try {
      const data = await discountService.getDiscounts();
      setDiscounts(data);
      setFilteredDiscounts(data);
    } catch (error) {
      showNotification("Lỗi khi tải dữ liệu khuyến mãi", "error");
      console.error("Lỗi khi lấy dữ liệu khuyến mãi:", error);
    } finally {
      setIsLoading(false);
    }
  }, [showNotification]);

  useEffect(() => {
    fetchDiscounts();
  }, [fetchDiscounts]);

  useEffect(() => {
    const filtered = discounts.filter(discount =>
      discount.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
    setFilteredDiscounts(filtered);
    setCurrentPage(1);
  }, [searchTerm, discounts]);

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = filteredDiscounts.slice(indexOfFirstItem, indexOfLastItem);
  const totalPages = Math.ceil(filteredDiscounts.length / itemsPerPage);

  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  const handleInputChange = useCallback((e) => {
    const { name, value, type, checked } = e.target;
    setFormState((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  }, []);

  const handleCancel = useCallback(() => {
    setShowForm(false);
    setFormState({
      id: null,
      name: "",
      startDate: "",
      endDate: "",
      status: true,
    });
    setErrors({ name: "", startDate: "", endDate: "" }); 
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    
    setErrors({ name: "", startDate: "", endDate: "" });

    let discountData;

    try {
      if (!formState.name) {
        setErrors((prev) => ({ ...prev, name: "Tên khuyến mãi không được để trống." }));
        return;
      }
      if (!formState.startDate) {
        setErrors((prev) => ({ ...prev, startDate: "Ngày bắt đầu không được để trống." }));
        return;
      }
      if (!formState.endDate) {
        setErrors((prev) => ({ ...prev, endDate: "Ngày kết thúc không được để trống." }));
        return;
      }

      const formatToCustomDateTime = (datetimeLocal) => {
        if (!datetimeLocal) return "";

        const date = new Date(datetimeLocal);
        const pad = (num) => num.toString().padStart(2, "0");

        const month = pad(date.getMonth() + 1);
        const day = pad(date.getDate());
        const year = date.getFullYear();
        const hours = pad(date.getHours());
        const minutes = pad(date.getMinutes());
        const seconds = pad(date.getSeconds());

        return `${month}/${day}/${year} ${hours}:${minutes}:${seconds}`;
      };

      discountData = {
        name: formState.name,
        startDate: formatToCustomDateTime(formState.startDate),
        endDate: formatToCustomDateTime(formState.endDate),
        status: formState.status,
      };

      if (formState.id) {
        await discountService.updateDiscount(formState.id, discountData);
        showNotification("Cập nhật khuyến mãi thành công");
      } else {
        await discountService.createDiscount(discountData);
        showNotification("Tạo khuyến mãi mới thành công");
      }

      await fetchDiscounts();
      handleCancel();
    } catch (error) {
      const errorMessage =
        error.response?.data?.message || error.message || "Đã xảy ra lỗi";
      showNotification(errorMessage, "error");
      console.error("Lỗi khi thao tác với khuyến mãi:", {
        error: error.response?.data,
        requestData: discountData,
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleEditDiscount = useCallback((discount) => {
    setFormState({
      id: discount.id,
      name: discount.name,
      startDate: convertToDateTimeLocal(discount.startDate),
      endDate: convertToDateTimeLocal(discount.endDate),
      status: discount.status,
    });
    setShowForm(true);
  }, []);

  const handleDeleteDiscount = useCallback(
    async (id) => {
      setIsLoading(true);
      try {
        await discountService.deleteDiscount(id);
        showNotification("Xóa khuyến mãi thành công", "success");
        await fetchDiscounts();
        setShowDeleteConfirmation(false);
      } catch (error) {
        const errorMessage =
          error.response?.data?.message ||
          error.message ||
          "Xóa khuyến mãi thất bại";
        showNotification(errorMessage, "error");
        console.error("Lỗi khi xóa khuyến mãi:", error);
      } finally {
        setIsLoading(false);
      }
    },
    [fetchDiscounts, showNotification]
  );

  const getDiscountStatus = useCallback((discount) => {
    if (!discount.status) return { text: "Đã kết thúc", class: "bg-secondary" };

    const today = new Date();
    const start = new Date(discount.startDate);
    const end = new Date(discount.endDate);

    if (today < start)
      return { text: "Sắp diễn ra", class: "bg-warning text-dark" };
    if (today > end) return { text: "Đã kết thúc", class: "bg-secondary" };
    return { text: "Đang hoạt động", class: "bg-success" };
  }, []);

  return (
    <div className="container mt-4">
      {notification.show && (
        <div
          className={`alert alert-${notification.type === "error" ? "danger" : "success"} alert-dismissible fade show`}
          role="alert"
        >
          {notification.message}
          <button
            type="button"
            className="btn-close"
            onClick={closeNotification}
            aria-label="Close"
          ></button>
        </div>
      )}

      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Quản lý Khuyến mãi</h2>
        <button
          className="btn btn-primary"
          onClick={() => setShowForm(true)}
          disabled={isLoading}
        >
          Thêm Khuyến mãi
        </button>
      </div>

      <div className="mb-3">
        <div className="input-group">
          <input
            type="text"
            className="form-control"
            placeholder="Tìm kiếm khuyến mãi..."
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
          <h4 className="mb-4">
            {formState.id ? "Chỉnh sửa Khuyến mãi" : "Thêm Khuyến mãi mới"}
          </h4>

          <div className="form-group mb-3">
            <label htmlFor="name">Tên khuyến mãi</label>
            <input
              type="text"
              className="form-control"
              id="name"
              name="name"
              value={formState.name}
              onChange={handleInputChange}
            />
            {errors.name && <div className="text-danger">{errors.name}</div>}
          </div>

          <div className="row mb-3">
            <div className="col-md-6">
              <label htmlFor="startDate">Ngày bắt đầu</label>
              <input
                type="datetime-local"
                className="form-control"
                id="startDate"
                name="startDate"
                value={formState.startDate}
                onChange={handleInputChange}
              />
              {errors.startDate && <div className="text-danger">{errors.startDate}</div>}
            </div>
            <div className="col-md-6">
              <label htmlFor="endDate">Ngày kết thúc</label>
              <input
                type="datetime-local"
                className="form-control"
                id="endDate"
                name="endDate"
                value={formState.endDate}
                onChange={handleInputChange}
                min={formState.startDate}
              />
              {errors.endDate && <div className="text-danger">{errors.endDate}</div>}
            </div>
          </div>



          <div className="d-flex justify-content-end">
            <button
              type="button"
              className="btn btn-outline-secondary me-2"
              onClick={handleCancel}
              disabled={isLoading}
            >
              Hủy bỏ
            </button>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={isLoading}
            >
              {isLoading
                ? "Đang xử lý..."
                : formState.id
                ? "Cập nhật"
                : "Thêm mới"}
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
                <p>Bạn có chắc chắn muốn xóa khuyến mãi này không?</p>
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
                  onClick={() => handleDeleteDiscount(deleteId)}
                >
                  Xóa
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {isLoading && discounts.length === 0 ? (
        <div className="text-center my-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
        </div>
      ) : (
        <>
          <div className="table-responsive">
            <table className="table table-striped table-hover">
              <thead>
                <tr>
                  <th>Tên khuyến mãi</th>
                  <th>Ngày bắt đầu</th>
                  <th>Ngày kết thúc</th>
                  <th>Trạng thái</th>
                  <th>Hành động</th>
                </tr>
              </thead>
              <tbody>
                {currentItems.map((discount) => {
                  const status = getDiscountStatus(discount);
                  return (
                    <tr key={discount.id}>
                      <td>{discount.name}</td>
                      <td>{formatDisplayDateTime(discount.startDate)}</td>
                      <td>{formatDisplayDateTime(discount.endDate)}</td>
                      <td>
                        <span className={`badge ${status.class}`}>
                          {status.text}
                        </span>
                      </td>
                      <td>
                        <button
                          className="btn btn-sm btn-outline-primary me-2"
                          onClick={() => handleEditDiscount(discount)}
                          disabled={isLoading}
                        >
                          Sửa
                        </button>
                        <button
                          className="btn btn-sm btn-outline-danger"
                          onClick={() => {
                            setDeleteId(discount.id);
                            setShowDeleteConfirmation(true);
                          }}
                          disabled={isLoading}
                        >
                          Xóa
                        </button>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
          {filteredDiscounts.length > itemsPerPage && (
            <nav className="mt-4">
              <ul className="pagination justify-content-center">
                <li className={`page-item ${currentPage === 1 ? "disabled" : ""}`}>
                  <button
                    className="page-link"
                    onClick={() => paginate(currentPage - 1)}
                    disabled={currentPage === 1}
                  >
                    &laquo; Trước
                  </button>
                </li>

                {Array.from({ length: totalPages }, (_, i) => i + 1).map(
                  (number) => (
                    <li
                      key={number}
                      className={`page-item ${
                        currentPage === number ? "active" : ""
                      }`}
                    >
                      <button
                        className="page-link"
                        onClick={() => paginate(number)}
                      >
                        {number}
                      </button>
                    </li>
                  )
                )}

                <li
                  className={`page-item ${
                    currentPage === totalPages ? "disabled" : ""
                  }`}
                >
                  <button
                    className="page-link"
                    onClick={() => paginate(currentPage + 1)}
                    disabled={currentPage === totalPages}
                  >
                    Sau &raquo;
                  </button>
                </li>
              </ul>
            </nav>
          )}
        </>
      )}
    </div>
  );
};

export default Discount;
