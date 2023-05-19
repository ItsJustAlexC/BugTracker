import React, { useContext, useEffect, useState } from "react";
import AuthContext from "../context/AuthContext";
import ReportDetails from "./ReportDetails";
import ReportListItem from "./ReportListItem";
import SearchBar from "./SearchBar";
import ViewFilter from "./ViewFilter";

function ViewBugs({SERVER_URL}) {
    const REPORT_URL = SERVER_URL + "/api/reports";
    const [reports, setReports] = useState([]);
    const [report, setReport] = useState();
    const [hidden, setHidden] = useState([]);
    const [view, setView] = useState("");
    const [sorting, setSorting] = useState("");
    const [loaded, setLoaded] = useState(true);
    const context = useContext(AuthContext);

    const getIncomplete = function() {
        setLoaded(false);

        fetch(REPORT_URL + "/incomplete")
        .then((response) => {return response.json()})
        .then((json) => {
            setReports(json);
            setView("INCOMPLETE");
            setLoaded(true);
        })
    }

    useEffect(getIncomplete, []);

    const getAll = function() {
        setLoaded(false);

        fetch(REPORT_URL, {
            headers: {
                "Authorization": `Bearer ${context.token}`
            }
        })
        .then((response) => {return response.json()})
        .then((json) => {
            setReports(json);
            setView("ALL");
            setLoaded(true);
        })
    }

    const getMyReports = function() {
        setLoaded(false);

        fetch(REPORT_URL + "/author", {
            headers: {
                "Authorization": `Bearer ${context.token}`
            }
        })
        .then((response) => {return response.json()})
        .then((json) => {
            setReports(json);
            setView("MY_REPORTS");
            setLoaded(true);
        })
    }

    const getVoted = function() {
        setLoaded(false);

        fetch(REPORT_URL + "/voted", {
            headers: {
                "Authorization": `Bearer ${context.token}`
            }
        })
        .then((response) => {return response.json()})
        .then((json) => {
            setReports(json);
            setView("VOTED");
            setLoaded(true);
        })
    }

    const sortByVote = function () {
        if(!loaded) {return;}
        const sorted = [...reports].sort((a, b) => b.voteCount - a.voteCount);
        setReports(sorted);
        setSorting("VOTE");
    }

    const sortByNewest = function () {
        if(!loaded) {return;}
        const sorted = [...reports].sort((a, b) => b.postDate > a.postDate ? 1 : -1);
        setReports(sorted);
        setSorting("NEWEST");
    }

    const sortByOldest = function () {
        if(!loaded) {return;}
        const sorted = [...reports].sort((a, b) => b.postDate > a.postDate ? -1 : 1);
        setReports(sorted);
        setSorting("OLDEST");
    }

    const sortByAuthor = function () {
        if(!loaded) {return;}
        const sorted = [...reports].sort((a, b) => b.authorUsername > a.authorUsername ? -1 : 1);
        setReports(sorted);
        setSorting("AUTHOR");
    }
    
    const search = function (searchTerm) {
        let newList = [];
        reports.map((r) => {
            if(
                !{...r}.title.toLowerCase().includes(searchTerm.toLowerCase())
                && !{...r}.issueDescription.toLowerCase().includes(searchTerm.toLowerCase())
                && !{...r}.replicationInstructions.toLowerCase().includes(searchTerm.toLowerCase())
            ) {
                newList.push(r);
            }
            return r;
        });

        setHidden(newList);
    }

    const updateReport = function () {
        let findReport = [];
        if(report) {
            findReport = reports.filter((r) => {return r.reportId === report.reportId});
        }
        
        let newReport = null;
        if(findReport.length === 1){
            newReport = findReport[0];
        }

        setReport(newReport);
    }

    useEffect(updateReport, [reports]);

    const refresh = function () {
        switch(view) {
            case "ALL":
                getAll();
                break;
            case "INCOMPLETE":
                getIncomplete();
                break;
            case "MY_REPORTS":
                getMyReports();
                break;
            case "VOTED":
                getVoted();
                break;
            default:
                getIncomplete();
                break;
        }
    }

    const sortView = function () {
        switch(sorting) {
            case "VOTE":
                sortByVote();
                break;
            case "NEWEST":
                sortByNewest();
                break;
            case "OLDEST":
                sortByOldest();
                break;
            case "AUTHOR":
                sortByAuthor();
                break;
            default:
                break;
        }
    }
    
    useEffect(sortView, [loaded]);

    return (
        <div className="container-fluid row d-flex justify-content-center">
            <div className="col-2 mr-5">
                {
                    context ?
                    <ViewFilter
                        getIncomplete = {getIncomplete}
                        getAll = {getAll}
                        getMyReports = {getMyReports}
                        getVoted = {getVoted}
                        sortByVote = {sortByVote}
                        sortByNewest = {sortByNewest}
                        sortByOldest = {sortByOldest}
                        sortByAuthor = {sortByAuthor}
                        view = {view}
                        sorting = {sorting}
                    />
                    : <></>
                }
            </div>
            
            <div className="col-5 mr-5">
                <div className="text-center">
                    <ReportDetails
                        report = {report}
                        refresh = {refresh}
                        SERVER_URL = {SERVER_URL}
                    />
                </div>
            </div>
            
            <div className="col text-center p-3 col-3">
                <h3>Reports List</h3>
                <SearchBar search={search}/>
                {reports.length === 0 ? <div>{ loaded ? "No Reports Found" : "Loading..."}
                </div> :
                reports.map((r) => {
                    return <div key = {r.reportId} className={hidden.includes(r) ? "d-none" : ""}>
                        <ReportListItem
                            report = {r}
                            setReport = {setReport}
                            />
                </div>
                })}
            </div>
        </div>
    );
}

export default ViewBugs;