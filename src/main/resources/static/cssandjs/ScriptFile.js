let idbooktxt, idbooksel, idadditionalread, idadditionaladd, idadditionalchangetype, idadditionalchangenew,
    idadditionalnewpage, idadditionalnewlink, idadditionalnewws, idadditionalnewlr, iddisplayargs, idordersort,
    idordergroup;

function getelements() {
    // books
    idbooktxt = document.getElementById("idbooktxtdiv")
    idbooksel = document.getElementById("idbookseldiv")

    // additional args
    idadditionalread = document.getElementById("idadditionalreaddiv")
    idadditionaladd = document.getElementById("idadditionaladddiv")
    idadditionalchangetype = document.getElementById("idadditionalchangetypediv")
    idadditionalchangenew = document.getElementById("idadditionalchangenewdiv")
    idadditionalnewpage = document.getElementById("idadditionalnewpagediv")
    idadditionalnewlink = document.getElementById("idadditionalnewlinkdiv")
    idadditionalnewws = document.getElementById("idadditionalnewwsdiv")
    idadditionalnewlr = document.getElementById("idadditionalnewlrdiv")

    // display
    iddisplayargs = document.getElementById("iddisplayargsdiv")
    idordersort = document.getElementById("idordersortdiv")
    idordergroup = document.getElementById("idordergroupdiv")
}

function formupdate(element) {

    let selectedOp = element.value

    forminvisible()

    function makeVisible(element) {
        element.style.display = "block"
    }

    switch (selectedOp) {
        case "New":
            makeVisible(idbooktxt);
            makeVisible(idadditionalnewpage)
            makeVisible(idadditionalnewlink)
            makeVisible(idadditionalnewws)
            makeVisible(idadditionalnewlr)
            break

        case "Read":
        case "Read To":
            makeVisible(idbooksel);
            makeVisible(idadditionalread);
            break

        case "Add":
            makeVisible(idbooksel);
            makeVisible(idadditionaladd);
            break

        case "Change":
            makeVisible(idbooksel);
            makeVisible(idadditionalchangetype);
            makeVisible(idadditionalchangenew);
            break

        case "Open":
            makeVisible(idbooksel);
            break

        case "Show":
            makeVisible(idbooksel);
        case "List":
            makeVisible(idordersort);
            makeVisible(idordergroup);
            makeVisible(iddisplayargs);
            break

        case "List All":
        case "Recommend":
            break
    }
}

function forminvisible() {

    function makeInvisible(element) {
        element.style.display = "none"
    }

    // books
    makeInvisible(idbooktxt)
    makeInvisible(idbooksel)

    // additional args
    makeInvisible(idadditionaladd)
    makeInvisible(idadditionalread)
    makeInvisible(idadditionalchangetype)
    makeInvisible(idadditionalchangenew)
    makeInvisible(idadditionalnewpage)
    makeInvisible(idadditionalnewlink)
    makeInvisible(idadditionalnewws)
    makeInvisible(idadditionalnewlr)

    // display
    makeInvisible(iddisplayargs)
    makeInvisible(idordersort)
    makeInvisible(idordergroup)
}