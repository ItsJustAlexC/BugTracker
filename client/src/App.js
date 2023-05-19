import { useState } from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";

import "./App.css";
import Contact from "./components/Contact";
import CreateAccount from "./components/CreateAccount";
import EditPermissions from "./components/EditPermissions";
import Home from "./components/Home";
import Login from "./components/Login";
import NavBar from "./components/NavBar";
import NotFound from "./components/NotFound";
import AuthContext from "./context/AuthContext";
import ViewBugs from "./components/ViewBugs";
import ReportForm from "./components/ReportForm";
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  const SERVER_URL = "http://ec2-18-208-151-222.compute-1.amazonaws.com:8080";

  let currentUserData = localStorage.getItem("userData");

  if (currentUserData) {
    currentUserData = JSON.parse(currentUserData);
  }

  const [user, setUser] = useState(currentUserData);

  let admin = false;
  
  if (user) {
    admin = user.userData.authorities.includes("ADMIN");
  }

  return (
    <AuthContext.Provider value={user}>
      <Router>
        <NavBar setUser={setUser} />
        <Routes>
          <Route index element={<Home />} />
          <Route path="bugs" element={<ViewBugs SERVER_URL={SERVER_URL} />} />
          <Route
            path="add"
            element={
              user ? (
                <ReportForm SERVER_URL={SERVER_URL} />
              ) : (
                <Navigate to="/" />
              )
            }
          />
          <Route path="contact" element={<Contact />} />
          <Route
            path="login"
            element={<Login setUser={setUser} SERVER_URL={SERVER_URL} />}
          />
          <Route
            path="create_account"
            element={<CreateAccount SERVER_URL={SERVER_URL} />}
          />
          <Route
            path="edit_permissions"
            element={
              admin ? (
                <EditPermissions SERVER_URL={SERVER_URL} />
              ) : (
                <Navigate to="/" />
              )
            }
          />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </Router>
    </AuthContext.Provider>
  );
}

export default App;
