import React, { useState, useEffect } from "react";
import "../pages/Index.css";
import Footer from "../components/clientComponents/Footer.js";
import logo from "../assets/logos/LogoLight.png";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faHouse, faFaceSmileWink, faLocationDot } from "@fortawesome/free-solid-svg-icons";
import { findBranch } from "../services/adminService/Branch.js";
import { Link } from "react-router-dom";

export default function PublicBranch() {
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
      <div className="background-image">
        <ul className="p-4 d-flex container align-items-center ">
          <div className="me-2">
            <img src={logo} alt="Logo Van Khoa" className="logo-image" />
          </div>
          <div className="d-inline-flex bg-light rounded me-2">
            {branches.length > 0 ? (
              branches.map((branch, index) => (
                <li key={index} className="list-unstyled p-3 fw-bold">
                  <Link
                    to={`/Client/Branch/${branch.id}/DefaultMenu`}
                    className="link-offset-2 link-offset-3-hover link-underline link-underline-opacity-0 link-underline-opacity-75-hover"
                  >
                    {branch.name}
                  </Link>
                </li>
              ))
            ) : (
              <li className="link-offset-2 link-offset-3-hover link-underline link-underline-opacity-0 link-underline-opacity-75-hover">
                Không có chi nhánh
              </li>
            )}
          </div>
          <div className="btn btn-light p-3 fw-bold text-primary">
            <Link to="/Login" className="">
              <FontAwesomeIcon icon={faFaceSmileWink} className="me-2" />
              Tài khoản
            </Link>
          </div>
        </ul>
      </div>

      <Footer />
    </div>
  );
}
