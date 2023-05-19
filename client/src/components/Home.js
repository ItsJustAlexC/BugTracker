import React, { useContext } from "react";
import AuthContext from "../context/AuthContext";

function Home() {
  const context = useContext(AuthContext);
  let name = "";
  if (context) {
    name = context.userData.sub;
  }

  return (
    <div className="container d-flex flex-column justify-content-center align-items-center h-100 vw-100 mt-5 p-4 bg-light shadow-sm rounded">
      <h1>
        Welcome <span className="text-info text-uppercase">{name}</span> to
        bugket.com
      </h1>
      <p>This is a full stack application that attempts to mimic solving a real world business problem, reporting software bugs. <br/> 
      To get started, you can create an account or login to report any software bug, and check the status of your report later.</p>
    </div>
  );
}

export default Home;
