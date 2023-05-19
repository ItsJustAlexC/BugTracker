import React, { useState } from "react";

function SearchBar({ search }) {
  const [searchTerm, setSearchTerm] = useState("");

  const handleChange = function (evt) {
    let newTerm = evt.target.value;
    setSearchTerm(newTerm);
  };

  const handleSearch = function (evt) {
    evt.preventDefault();
    search(searchTerm);
  };

  return (
    <form
      style={{ height: "2.5rem" }}
      className="form d-flex m-2"
      onSubmit={handleSearch}
    >
      <input
        value={searchTerm}
        onChange={handleChange}
        className="form-control form-control-sm border-primary m-1"
      />
      <button id="search" type="submit" className="btn btn-primary btn-sm m-1">
        Search
      </button>
    </form>
  );
}

export default SearchBar;
