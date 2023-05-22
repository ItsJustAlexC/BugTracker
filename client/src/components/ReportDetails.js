import React, { useContext, useEffect, useState } from "react";
import AuthContext from "../context/AuthContext";
import Message from "./Message";
import MessageForm from "./MessageForm";

function ReportDetails({ report, refresh, SERVER_URL }) {
  const VOTE_URL = SERVER_URL + "/api/vote";

  const [voted, setVoted] = useState(false);
  const [messages, setMessages] = useState([]);

  const context = useContext(AuthContext);

  let auth = false;
  if (context) {
    auth =
      context.userData.authorities.includes("ADMIN") ||
      context.userData.authorities.includes("DEV");
  }

  const checkVoters = function () {
    if (!context || !report) {
      return;
    }

    fetch(VOTE_URL + "/check/" + report.reportId, {
      method: "GET",
      headers: {
        Accept: "application/json",
        Authorization: `Bearer ${context.token}`,
      },
    })
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        }
      })
      .then(setVoted);
  };

  useEffect(checkVoters, [report]);

  const submitVote = function () {
    fetch(VOTE_URL + "/" + report.reportId, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${context.token}`,
      },
    }).then(() => {
      refresh();
    });
  };

  const removeVote = function () {
    fetch(VOTE_URL + "/" + report.reportId, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${context.token}`,
      },
    }).then((response) => {
      refresh();
    });
  };

    const updateStatus = function(status) {
        fetch(SERVER_URL + "/api/reports/update/" + report.reportId + "/" + status, {
            method: "PUT",
            headers: {
                "Authorization": `Bearer ${context.token}`
            }
        })
        .then((response) => {
            console.log(response);
            refresh();
        })
    }

    const getMessages = function () {
        if (!report) {
            return;
        }

        fetch(SERVER_URL + "/api/messages/" + report.reportId)
        .then((response) => {
            return response.json();
        })
        .then((json) => {
            const sorted = json.reverse();
            setMessages(sorted);
        })
    }

    useEffect(getMessages, [report]);

    if(!report) {
        return (
            <div className="">
                <p>Select a report for more details.</p>
            </div>
        )
    }

  if (!report) {
    return (
        <div className="col text-center m-3 p-3">
            <div className="p-3 text-left bg-white border overflow-hidden">
                <div className="d-flex">
                    <div className="mr-auto w-50 overflow-hidden">
                        <h5 className="text-truncate">{report.title}</h5>
                    </div>
                    
                    
                    <p className="m-1">Votes: {report.voteCount}</p>

                    {
                        context ? 
                        (
                            voted ?
                            <button className="btn btn-primary btn-sm m-1" type="button" onClick={removeVote}>Remove Vote</button>
                            : <button className="btn btn-primary btn-sm m-1" type="button" onClick={submitVote}>Vote</button>
                        )
                        
                        : <></>
                    }
                </div>
                
                <div className="d-flex justify-content-between">
                    {
                        report.completionStatus ?
                        <p className="text-success">Resolved</p>
                        : <p className="text-danger">Unresolved</p>
                    }

                    {
                        auth ?
                        (
                            report.completionStatus ?
                            <button className="btn btn-danger btn-sm m-1" type="button" onClick={() => updateStatus(false)}>Mark as Unresolved</button>
                            : <button className="btn btn-success btn-sm m-1" type="button" onClick={() => updateStatus(true)}>Mark as Resolved</button>
                        )
                        : <></>
                    }
                </div>
                

                <p>By: <span className="d-flex text-info text-uppercase text-truncate w-100">{report.authorUsername}</span> | Posted: {new Date(report.postDate).toLocaleDateString("en-US")}</p>

                <h6>Issue Description</h6>
                <p>{report.issueDescription}</p>

                <h6>Replication Instructions</h6>
                <p>{report.replicationInstructions}</p>

                <h4 className="text-center mt-5">Messages</h4>
            {
                messages.length > 0 ?
                messages.map((message) => {
                    return <Message 
                        key={message.messageId} 
                        message={message}
                        SERVER_URL={SERVER_URL}
                        getMessages={getMessages}
                    />
                })
                : <p className="text-center">No current messages.</p>
            }
            {
                context ?
                <MessageForm 
                    report={report} 
                    SERVER_URL={SERVER_URL}
                    getMessages={getMessages}
                />
                : <></>
            }
            </div>
        </div>
    );
  }

  return (
    <div className="container d-flex">
      <div className="col text-center">
        <div className="p-3 text-left bg-white rounded overflow-hidden shadow">
          <h5 className="overflow-hidden text-center report-title">{report.title}</h5>
          <div className="justify-content-between">
            {report.completionStatus ? (
              <p className="text-success text-center">Resolved</p>
            ) : (
              <p className="text-danger ">Unresolved</p>
            )}

            {auth ? (
              report.completionStatus ? (
                <button
                  className="btn btn-danger btn-sm m-1"
                  type="button"
                  onClick={() => updateStatus(false)}
                >
                  Mark as Unresolved
                </button>
              ) : (
                <button
                  id="makeComplete"
                  className="btn btn-primary btn-sm m-1"
                  type="button"
                  onClick={() => updateStatus(true)}
                >
                  Mark as Resolved
                </button>
              )
            ) : (
              <></>
            )}
          </div>

          <p>
            By:{" "}
            <span className="text-info text-uppercase">
              <span className="text-truncate">{report.authorUsername}</span>
            </span>{" "} <span className="fs-6">| Posted: {new Date(report.postDate).toLocaleDateString("en-US")}</span>
          </p>

          <div className="border p-1 rounded">
            <h6>Issue Description</h6>
            <p className="overflow-hidden">{report.issueDescription}</p>
          </div>
          
          <div className="mt-2 border p-1 rounded">
            <h6>Replication Instructions</h6>
            <p className="overflow-hidden">{report.replicationInstructions}</p>
          </div>
          <div className="text-end">
          Votes: {report.voteCount}
          {context ? (
                voted ? (
                  <button
                    className="btn btn-danger btn-sm m-1 me-1"
                    type="button"
                    onClick={removeVote}
                  >
                    Remove
                  </button>
                ) : (
                  <button
                    className="btn btn-primary btn-sm m-1 me-1"
                    type="button"
                    onClick={submitVote}
                  >
                    Vote
                  </button>
                )
              ) : (
                <></>
              )}
          </div>
            {context ? (
            <MessageForm
              report={report}
              SERVER_URL={SERVER_URL}
              getMessages={getMessages}
            />
          ) : (
            <></>
          )}
          <h4 className="text-center mt-5">Messages</h4>
          {messages.length > 0 ? (
            messages.map((message) => {
              return (
                <Message
                  key={message.messageId}
                  message={message}
                  SERVER_URL={SERVER_URL}
                  getMessages={getMessages}
                />
              );
            })
          ) : (
            <p className="text-center">No current messages</p>
          )}
        </div>
      </div>
    </div>
  );
}

export default ReportDetails;
