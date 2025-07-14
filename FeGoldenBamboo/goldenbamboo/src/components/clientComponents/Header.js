import React, { useState, useEffect } from "react";
import "../../styles/clientStyles/BackgroundClient.scss";
import logo from "../../assets/logos/LogoDark.png";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faHouse, faFaceSmileWink, faLocationDot, faCartShopping, faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";
import { findBranch } from "../../services/adminService/Branch.js";
import { Link } from "react-router-dom";
export default function ClientHeader() {
  const [branches, setBranches] = useState([]);

  useEffect(() => {
    const fetchBranches = async () => {
      try {
        const data = await findBranch();
        setBranches(data);
      } catch (error) {
        console.error("Lỗi khi lấy danh sách chi nhánh:", error);
      }
    };

    fetchBranches();
  }, []);

  return (
    <div>
      <nav className="p-2">
        <div className="container-fluid d-flex justify-content-between">
          {/* Logo */}
          <Link className="navbar-brand me-3" href="/Client/Home">
            <img src={logo} alt="Logo" className="logo" />
          </Link>

          <div className="d-flex align-items-center">
            <div className="me-2">
              <Link to="/Client/Home" className="btn btn-outline-success me-2 text-nowrap">
                <FontAwesomeIcon icon={faHouse} />
              </Link>
            </div>
            <div className="me-2">
              <Link to="/Client/Search" className="btn btn-outline-success me-2 text-nowrap">
                <FontAwesomeIcon icon={faMagnifyingGlass} />
              </Link>
            </div>
            <div className="me-2">
              <Link to="/Client/Cart" className="btn btn-outline-success me-2 text-nowrap">
                <FontAwesomeIcon icon={faCartShopping} />
              </Link>
            </div>
            <div className="me-2">
              <Link to="/Login" className="btn btn-outline-success me-2 text-nowrap">
                <FontAwesomeIcon icon={faFaceSmileWink} className="me-2" />
                Tài khoản
              </Link>
            </div>
            <div className="me-2">
              <Link to="/Client/Account" className="btn btn-outline-success me-2 text-nowrap">
                <FontAwesomeIcon icon={faFaceSmileWink} className="me-2" />
                Tài khoản
              </Link>
            </div>
            <div className="dropdown">
              <button className="btn btn-outline-success dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                <FontAwesomeIcon icon={faLocationDot} className="me-2" />
                Chọn chi nhánh
              </button>
              <ul className="dropdown-menu">
                {branches.length > 0 ? (
                  branches.map((branch, index) => (
                    <li key={index} className="dropdown-item">
                      <Link to={`/Client/Branch/${branch.id}/DefaultMenu`} className="dropdown-link">
                        {branch.name}
                      </Link>
                    </li>
                  ))
                ) : (
                  <li className="dropdown-item">Không có chi nhánh</li>
                )}
              </ul>
            </div>
          </div>
        </div>
      </nav>
    </div>
  );
}
