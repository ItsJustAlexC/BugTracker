import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import AuthContext from "../context/AuthContext";

export default function ReportForm({ SERVER_URL }) {
  const [title, setTitle] = useState("");
  const [issueDescription, setIssueDescription] = useState("");
  const [replicationInstructions, setReplicationInstructions] = useState("");
  const [errors, setErrors] = useState([]);

  const context = useContext(AuthContext);
  const name = context.userData.sub;

  const today = new Date();

  const navigate = useNavigate();

  const handleSubmit = (event) => {
    event.preventDefault();
    add();
  };

  const add = () => {
    const report = {
      title,
      issueDescription,
      replicationInstructions,
      completionStatus: false,
      postDate: today,
      authorUsername: name,
    };
    fetch(SERVER_URL + "/api/reports/add", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${context.token}`,
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(report),
    }).then((result) => {
      if (result.status === 201) {
        clearForm();
        setErrors([]);
        navigate("/bugs");
      } else {
        result.json().then((errors) => {
          setErrors(errors);
        });
      }
    });
  };

  const cancelAdd = () => {
    clearForm();
    navigate("/bugs");
  };

  const clearForm = () => {
    setTitle("");
    setIssueDescription("");
    setReplicationInstructions("");
  };

  return (
    <div className="container-fluid row d-flex justify-content-center">
      <div id="reportForm">
        <h1>Add A Bug Report</h1>
        <form
          onSubmit={(event) => {
            handleSubmit(event);
          }}
        >
          <div>
            <label id="title" className="form-label" htmlFor="titleInput">
              Title:{" "}
            </label>
            <input
              placeholder="Report Title"
              value={title}
              onChange={(event) => {
                setTitle(event.target.value);
              }}
              type="text"
              className="form-control w-25 mb-3 shadow-sm"
              id="title-input"
            />
          </div>

          <div>
            <label id="issueD"className="form-label" htmlFor="issueDescriptionInput">
              Issue Description:{" "}
            </label>
            <textarea
              id="issueDescription-input"
              placeholder="Please describe the issue you are experiencing. Be as descriptive as possible."
              value={issueDescription}
              onChange={(event) => {
                setIssueDescription(event.target.value);
              }}
              className="form-control w-100 mb-3 shadow-sm"
              rows="5"
            />
          </div>

          <div>
            <label
              id="replicationI"
              className="form-label"
              htmlFor="replicationInstructionsInput"
            >
              Replication Instructions:{" "}
            </label>
            <textarea
              placeholder="What steps should we follow in order to reproduce the issue?"
              value={replicationInstructions}
              onChange={(event) => {
                setReplicationInstructions(event.target.value);
              }}
              className="form-control w-100 mb-3 shadow-sm"
              id="replicationInstructions-input"
              rows="5"
            />
          </div>

          <div className="d-flex justify-content-center w-100">
            <div className="d-flex m-2 flex-column justify-content-center w-25">
              <button className="btn btn-danger" onClick={() => cancelAdd()}>
                Cancel Report
              </button>
            </div>
            <div className="d-flex m-2 flex-column justify-content-center w-25">
              <button
                id="submitReport"
                type="submit"
                className="btn btn-primary "
              >
                Submit Report
              </button>
            </div>
          </div>
        </form>
        <section id="errors">
          {errors.length > 0 ? (
            <ul>
              {errors.map((error) => {
                return <li key={error}>{error}</li>;
              })}
            </ul>
          ) : null}
        </section>
      </div>
    </div>
  );
}
