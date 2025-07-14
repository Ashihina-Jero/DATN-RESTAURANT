import React from 'react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckCircle } from '@fortawesome/free-solid-svg-icons';

// Định nghĩa props
interface MenuDisplayProps {
    categories: any[];
    combos: any[];
    foods: any[];
    handleAddToOrder: (item: any, isCombo: boolean) => void;
    justAddedId: number | null;
    params: {
        branchId?: string;
        tableId?: string;
        orderId?: string;
        categoryId?: string;
    };
}

export default function MenuDisplay({ categories, combos, foods, handleAddToOrder, justAddedId, params }: MenuDisplayProps) {
    const { branchId, tableId, orderId, categoryId } = params;

    return (
        <div className='col-7 border'>
            <h1 className='text-center'>Staff</h1>
            <nav className="navbar navbar-expand-lg bg-body-tertiary">
                <div className="container-fluid">
                    <Link className="navbar-brand" to={`/Staff/Branch/${branchId}/Table/${tableId}/Order/${orderId}/Category/-1`}>Combo</Link>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarNav">
                        <ul className="navbar-nav">
                            {categories.map((cat) => (
                                <li className="nav-item" key={cat.id}>
                                    <Link className="nav-link" to={`/Staff/Branch/${branchId}/Table/${tableId}/Order/${orderId}/Category/${cat.id}`}>
                                        {cat.name}
                                    </Link>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>
            </nav>
            <div className='row border p-2'>
                {/* Hiển thị Combos */}
                {parseInt(categoryId ?? "-1") === -1 && combos.map((combo) => (
                    <div
                        className={`col-3 card p-3 position-relative ${justAddedId === combo.id ? 'border-success' : ''}`}
                        key={`combo-${combo.id}`}
                        onClick={() => handleAddToOrder(combo, true)}
                        style={{ cursor: 'pointer', transition: 'border 0.3s' }}
                    >
                        {justAddedId === combo.id && <FontAwesomeIcon icon={faCheckCircle} className="text-success position-absolute top-0 end-0 p-2" style={{ fontSize: '1.5rem' }} />}
                        {combo.image ? <img src={combo.image} alt={combo.name} style={{ width: '100%', height: 'auto' }} /> : <div style={{ width: '100%', height: '100px', backgroundColor: '#e0e0e0', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>No Image</div>}
                        <h5>{combo.name}</h5>
                        <p>{combo.description}</p>
                        <p>Giá: {combo.price.toLocaleString('vi-VN')} VNĐ</p>
                    </div>
                ))}
                {/* Hiển thị Món ăn theo category */}
                {parseInt(categoryId ?? "-1") !== -1 && foods.map((dish) => (
                    <div
                        className={`col-3 card p-3 position-relative ${justAddedId === dish.id ? 'border-success' : ''}`}
                        key={`dish-${dish.id}`}
                        onClick={() => handleAddToOrder(dish, false)}
                        style={{ cursor: 'pointer', transition: 'border 0.3s' }}
                    >
                        {justAddedId === dish.id && <FontAwesomeIcon icon={faCheckCircle} className="text-success position-absolute top-0 end-0 p-2" style={{ fontSize: '1.5rem' }} />}
                        {dish.image ? <img src={dish.image} alt={dish.name} style={{ width: '100%', height: 'auto' }} /> : <div style={{ width: '100%', height: '100px', backgroundColor: '#e0e0e0', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>No Image</div>}
                        <h5>{dish.name}</h5>
                        <p>{dish.description}</p>
                        <p>Giá: {dish.price.toLocaleString('vi-VN')} VNĐ</p>
                    </div>
                ))}
            </div>
        </div>
    );
}