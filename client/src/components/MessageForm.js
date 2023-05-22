import React, { useContext, useState } from "react";
import AuthContext from "../context/AuthContext";

function MessageForm({ report, SERVER_URL, getMessages }) {
  const clearMessage = {
    message: "",
    postDate: "",
    authorUsername: "",
    reportId: "",
  };
  const [message, setMessage] = useState(clearMessage);
  const [err, setErr] = useState([]);

  const context = useContext(AuthContext);

  const handleChange = function (evt) {
    let newMessage = { ...message };
    newMessage[evt.target.id] = evt.target.value;
    setMessage(newMessage);
  };

  const handleSubmit = function (evt) {
    evt.preventDefault();

    let newMessage = { ...message };
    newMessage.postDate = new Date();
    newMessage.authorUsername = context.userData.sub;
    newMessage.reportId = report.reportId;

    fetch(SERVER_URL + "/api/messages", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${context.token}`,
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(newMessage),
    }).then((result) => {
      if (result.status === 201) {;
        setMessage(clearMessage);
        setErr([]);
        getMessages();
      } else {
        result.json().then((errors) => {
          setErr(errors);
        });
      }
    });
  };

  return (
    <form className="p-3" onSubmit={handleSubmit}>
      <h5>Post a Message</h5>
      <textarea
        id="message"
        onChange={handleChange}
        value={message.message}
        className="form-control shadow-sm"
      />
      <div className="w-100 d-flex justify-content-center mt-3">
        <button id="postMessage" type="submit" className="btn btn-primary">
          Post
        </button>
      </div>

      {err.length > 0 ? (
        <ul>
          {err.map((error) => {
            return <li key={error}>{error}</li>;
          })}
        </ul>
      ) : (
        <></>
      )}
    </form>
  );
}

export default MessageForm;
