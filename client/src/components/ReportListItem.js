import React from "react";

function ReportListItem({report, setReport}) {
    return (
        <button className="reportListBtn h-25 mb-2 bg-white border text-truncate rounded shadow" type="button" onClick={() => setReport(report)}>
            {report.title} <br/> 
            <span className="text-uppercase text-info">{report.authorUsername}</span> | {new Date(report.postDate).toLocaleDateString("en-US")}
        </button>
        
    );
}

export default ReportListItem;