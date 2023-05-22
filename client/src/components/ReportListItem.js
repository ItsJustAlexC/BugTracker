import React from "react";

function ReportListItem({report, setReport}) {
    return (
        <button className="reportListBtn mb-2 bg-white border text-truncate rounded shadow m-2" type="button" onClick={() => setReport(report)}>
            {report.title} <br/> 
            <span className="text-uppercase text-info">{report.authorUsername}</span> | {new Date(report.postDate).toLocaleDateString("en-US")}
        </button>
        
    );
}

export default ReportListItem;