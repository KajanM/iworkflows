function findDate(daysFrom, startingDate) {
    var today = new Date();
    if (startingDate === 0) {
        today = new Date();
    }
    else {
        today = new Date(startingDate);
    }

    var dd = today.getDate() + daysFrom;
    var mm = today.getMonth() + 1; //January is 0!
    var yyyy = today.getFullYear();
    if (dd < 10) {
        dd = '0' + dd
    }
    if (mm < 10) {
        mm = '0' + mm
    }

    return yyyy + '-' + mm + '-' + dd;
}


// start date min is currentDate
document.getElementById("startDate").setAttribute("min", findDate(0, 0));

function restrictEndDate() {
    var startingDate = document.getElementById("startDate").value;
    console.log(startingDate);
    document.getElementById("endDate").setAttribute("min", findDate(0, startingDate));
}