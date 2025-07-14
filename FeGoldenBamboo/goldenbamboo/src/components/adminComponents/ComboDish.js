import React, { useEffect, useState } from 'react';
import { getCombos, getDishes, getComboDishes, createComboDish, updateComboDish, deleteComboDish, getComboDishesFull } from '../../services/staffService/ComboDishService'; 

const ComboDishManagement = () => {
  const [combos, setCombos] = useState([]);
  const [dishes, setDishes] = useState([]);
  const [comboDishes, setComboDishes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [formData, setFormData] = useState({
    id: null,
    comboId: '',
    dish1Id: '',
    dish2Id: ''
  });
  const [showForm, setShowForm] = useState(false);
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    const loadData = async () => {
      try {
        setLoading(true);
        const [combosData, dishesData, comboDishesData] = await Promise.all([
          getCombos(),
          getDishes(),
          getComboDishesFull()
        ]);
        
        setCombos(combosData);
        setDishes(dishesData);
        setComboDishes(comboDishesData);
      } catch (err) {
        setError('Không thể tải dữ liệu. Vui lòng thử lại sau.');
      } finally {
        setLoading(false);
      }
    };
    loadData();
  }, []);
  

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    if (!formData.comboId || !formData.dish1Id || !formData.dish2Id) {
      setError('Vui lòng điền đầy đủ thông tin');
      return;
    }
  
    if (formData.dish1Id === formData.dish2Id) {
      setError('Hai món ăn không được trùng nhau');
      return;
    }
  
    try {
      setLoading(true);
      if (isEditing) {
        await updateComboDish(formData.id, {
          comboId: formData.comboId,
          dish1Id: formData.dish1Id,
          dish2Id: formData.dish2Id
        });
      } else {
        await createComboDish(formData.comboId, formData.dish1Id, formData.dish2Id);
      }
  
      const updatedData = await getComboDishes();
      setComboDishes(updatedData);
      resetForm();
    } catch (err) {
      setError(`Lỗi: ${err.message}`);
      console.error('Error submitting form:', err);
    } finally {
      setLoading(false);
    }
  };
  
  const handleEdit = (comboDish) => {
    console.log('Editing combo dish:', comboDish); 
    
    setFormData({
      id: comboDish.id,
      comboId: comboDish.comboId.toString(), 
      dish1Id: comboDish.dish1Id.toString(),
      dish2Id: comboDish.dish2Id.toString()
    });
    
    setIsEditing(true);
    setShowForm(true);
    setError(null);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa?')) {
      try {
        setLoading(true);
        await deleteComboDish(id);
        const updatedData = await getComboDishes();
        setComboDishes(updatedData);
      } catch (err) {
        setError(`Lỗi khi xóa: ${err.message}`);
      } finally {
        setLoading(false);
      }
    }
  };

  const resetForm = () => {
    setFormData({
      id: null,
      comboId: '',
      dish1Id: '',
      dish2Id: ''
    });
    setIsEditing(false);
    setShowForm(false);
    setError(null);
  };

  const getNameById = (id, list) => {
    const item = list.find(item => item.id === id);
    return item ? item.name : 'Không xác định';
  };

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Quản lý Combo và Món ăn</h2>
        <button 
          onClick={() => setShowForm(!showForm)} 
          className={`btn ${showForm ? 'btn-secondary' : 'btn-primary'}`}
        >
          {showForm ? 'Đóng form' : 'Thêm mới'}
        </button>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}

      {showForm && (
        <div className="card mb-4">
          <div className="card-body">
            <h4 className="card-title">{isEditing ? 'Chỉnh sửa Combo' : 'Thêm Combo mới'}</h4>
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">Combo</label>
                <select
                  name="comboId"
                  value={formData.comboId}
                  onChange={handleChange}
                  className="form-select"
                  required
                >
                  <option value="">Chọn combo</option>
                  {combos.map(combo => (
                    <option key={combo.id} value={combo.id.toString()}>
                      {combo.name}
                    </option>
                  ))}
                </select>
              </div>

              <div className="mb-3">
                <label className="form-label">Món ăn 1</label>
                <select
                  name="dish1Id"
                  value={formData.dish1Id}
                  onChange={handleChange}
                  className="form-select"
                  required
                >
                  <option value="">Chọn món ăn 1</option>
                  {dishes.map(dish => (
                    <option key={dish.id} value={dish.id.toString()}>
                      {dish.name}
                    </option>
                  ))}
                </select>
              </div>

              <div className="mb-3">
                <label className="form-label">Món ăn 2</label>
                <select
                  name="dish2Id"
                  value={formData.dish2Id}
                  onChange={handleChange}
                  className="form-select"
                  required
                >
                  <option value="">Chọn món ăn 2</option>
                  {dishes
                    .filter(dish => dish.id.toString() !== formData.dish1Id)
                    .map(dish => (
                      <option key={dish.id} value={dish.id.toString()}>
                        {dish.name}
                      </option>
                    ))}
                </select>
              </div>

              <div className="d-flex gap-2">
                <button type="submit" className="btn btn-primary">
                  {isEditing ? 'Cập nhật' : 'Thêm mới'}
                </button>
                <button 
                  type="button" 
                  onClick={resetForm} 
                  className="btn btn-outline-secondary"
                >
                  Hủy
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      <div className="table-responsive">
        <table className="table table-striped">
          <thead>
            <tr>
              <th>ID</th>
              <th>Tên Combo</th>
              <th>Món 1</th>
              <th>Món 2</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
  {comboDishes.map(item => (
    <tr key={item.id}>
      <td>{item.id}</td>
      <td>{item.comboName}</td>
      <td>{item.dish1Name}</td>
      <td>{item.dish2Name}</td>
      <td>
        <div className="d-flex gap-2">
          <button 
            onClick={() => handleEdit(item)}
            className="btn btn-sm btn-warning"
          >
            Sửa
          </button>
          <button 
            onClick={() => handleDelete(item.id)}
            className="btn btn-sm btn-danger"
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

      {loading && (
        <div className="text-center my-4">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
        </div>
      )}
    </div>
  );
};

export default ComboDishManagement;
