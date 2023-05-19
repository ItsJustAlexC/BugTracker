import React, { useContext } from "react";
import AuthContext from "../context/AuthContext";

function Message({message, SERVER_URL, getMessages}) {
    const context = useContext(AuthContext);

    let admin = false;
    if(context) {
        admin = context.userData.authorities.includes("ADMIN");
    }

    const deleteMessage = function () {
        fetch(SERVER_URL + "/api/messages/" + message.messageId, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${context.token}`
            }
        })
        .then((response) => {
            console.log(response);
            getMessages();
        })
    }

    return (
        <div className="border m-3 p-2 bg-white rounded">
            <p><span className="text-uppercase text-info">{message.authorUsername}</span> | {new Date(message.postDate).toLocaleDateString("en-US")}</p>
            <p>{message.message}</p>
            {
                admin ?
                <button type="button" className="btn btn-danger btn-sm" onClick={deleteMessage}>Delete</button>
                : <></>
            }
        </div>
    );
}

export default Message;