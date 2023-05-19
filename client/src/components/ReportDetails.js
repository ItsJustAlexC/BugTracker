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
        if (response.status == 200) {
          console.log(response);
          return response.json();
        } else {
          console.log(response);
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
    }).then((response) => {
      console.log(response);
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
      console.log(response);
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
            console.log(response);
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
            <div className="col m-3 p-3">
                <h3>Report Details</h3>
                <p>Select a report for more details.</p>
            </div>
        )
    }

  if (!report) {
    return (
        <div className="col text-center m-3 p-3">
            <h3>Report Details</h3>
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
    <div className="col text-center m-3 p-3">
      <h3>Report Details</h3>
      <div className="p-3 text-left bg-white border overflow-hidden shadow">
        <div className="d-flex">
          <h5 className="mr-auto w-75 overflow-hidden">{report.title}</h5>

          <p style={{fontSize:"1rem"}} className="m-1 text-center">Votes: {report.voteCount}</p>
          
          <div>
            {context ? (
              voted ? (
                <button
                  className="btn btn-danger btn-sm m-1"
                  type="button"
                  onClick={removeVote}
                >
                  Remove Vote
                </button>
              ) : (
                <button
                  className="btn btn-primary btn-sm m-1"
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
          
        </div>

        <div className="d-flex justify-content-between">
          {report.completionStatus ? (
            <p className="text-success">Resolved</p>
          ) : (
            <p className="text-danger">Unresolved</p>
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
          <span className="d-flex text-info text-uppercase">
            <span className="text-truncate">{report.authorUsername}</span>
          </span>{" "}
          | Posted: {new Date(report.postDate).toLocaleDateString("en-US")}
        </p>

        <h6>Issue Description</h6>
        <p className="w-100 overflow-hidden">{report.issueDescription}</p>

        <h6>Replication Instructions</h6>
        <p className="w-100 overflow-hidden">{report.replicationInstructions}</p>

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
          <p className="text-center">No current messages.</p>
        )}
        {context ? (
          <MessageForm
            report={report}
            SERVER_URL={SERVER_URL}
            getMessages={getMessages}
          />
        ) : (
          <></>
        )}
      </div>
    </div>
  );
}

export default ReportDetails;
