import React, { useContext, useState } from "react";
import AuthContext from "../context/AuthContext";

function UserPermission({ username, role, SERVER_URL }) {
  const context = useContext(AuthContext);
  const [newRole, setNewRole] = useState(role);
  const [message, setMessage] = useState("");

  const handleSubmit = function (evt) {
    evt.preventDefault();

    fetch(`${SERVER_URL}/api/update_user/${username}/${newRole}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${context.token}`,
      },
    }).then((response) => {
      if (response.status === 204) {
        console.log(response);
        setMessage("Updating...");
        window.location.reload();
      } else {
        console.log(response);
        setMessage("Update Failed");
      }
    });
  };

  const handleChange = function (evt) {
    setNewRole(evt.target.value);
  };

  return (
    <div>
      <form
        onSubmit={handleSubmit}
        style={{ width: "17rem" }}
        className="border rounded p-3 bg-white m-2 shadow overflow-hidden"
      >
        <h4 className="m-1 text-uppercase text-info text-truncate">
          {username}
        </h4>
        <div className="d-flex">
          <select
            name="role"
            onChange={handleChange}
            value={newRole}
            className="form-control m-1"
          >
            <option value="USER">User</option>
            <option value="DEV">Developer</option>
            <option value="ADMIN">Administrator</option>
          </select>
          <button id="update" className="btn btn-primary btn-sm m-1">
            Update
          </button>
        </div>
      </form>
      <p className="font-italic">{message}</p>
    </div>
  );
}

export default UserPermission;
