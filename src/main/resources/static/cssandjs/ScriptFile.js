let idbooktxt, idbooksel, idadditionalread, idadditionaladd, idadditionalchangetype, idadditionalchangetextvalue,
    idadditionalchangewsvalue, idadditionalnewpage, idadditionalnewlink, idadditionalnewws, idadditionalnewlr,
    idadditionalwaitwu, iddisplayargs, idordersort, idordergroup, idordersortdirection, idordergroupdirection;

function getelements() {
    // books
    idbooktxt = document.getElementById("idbooktxtdiv")
    idbooksel = document.getElementById("idbookseldiv")

    // additional args
    idadditionalread = document.getElementById("idadditionalreaddiv")
    idadditionaladd = document.getElementById("idadditionaladddiv")
    idadditionalchangetype = document.getElementById("idadditionalchangetypediv")
    idadditionalchangetextvalue = document.getElementById("idadditionalchangetextvaluediv")
    idadditionalchangewsvalue = document.getElementById("idadditionalchangewsvaluediv")
    idadditionalnewpage = document.getElementById("idadditionalnewpagediv")
    idadditionalnewlink = document.getElementById("idadditionalnewlinkdiv")
    idadditionalnewws = document.getElementById("idadditionalnewwsdiv")
    idadditionalnewlr = document.getElementById("idadditionalnewlrdiv")
    idadditionalwaitwu = document.getElementById("idadditionalwaitwudiv")

    // display
    iddisplayargs = document.getElementById("iddisplayargsdiv")
    idordersort = document.getElementById("idordersortdiv")
    idordergroup = document.getElementById("idordergroupdiv")
    idordersortdirection = document.getElementById("idordersortdirectiondiv")
    idordergroupdirection = document.getElementById("idordergroupdirectiondiv")
}

function operatorupdate() {

    let selectedOp = document.getElementById("idoperator").value


    forminvisible()

    switch (selectedOp) {
        case "New":
            makeVisible(idbooktxt);
            makeVisible(idadditionalnewpage)
            makeVisible(idadditionalnewlink)
            makeVisible(idadditionalnewws)
            makeVisible(idadditionalnewlr)
            break

        case "Read":
        case "ReadTo":
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
            changeupdate();
            break

        case "Open":
            makeVisible(idbooksel);
            break

        case "Show":
            makeVisible(idbooksel);
            makeVisible(iddisplayargs);
            break

        case "List":
            makeVisible(idordersort);
            makeVisible(idordergroup);
            makeVisible(idordersortdirection);
            makeVisible(idordergroupdirection);
            makeVisible(iddisplayargs);
            break

        case "Wait":
            makeVisible(idbooksel);
            makeVisible(idadditionalwaitwu);
            break

        case "Pause":
            makeVisible(idbooksel);
            break

        case "ListAll":
        case "Recommend":
        case "":
            break
    }
}

function changeupdate() {
    let selectedCh = document.getElementById("idadditionalchangeattribute").value

    switch (selectedCh) {
        case "rating":
            makeVisible(idadditionalchangetextvalue)
            makeInvisible(idadditionalchangewsvalue)
            document.getElementById("idadditionalchangetextvalue").type = 'number'
            break

        case "writingStatus":
            makeInvisible(idadditionalchangetextvalue)
            makeVisible(idadditionalchangewsvalue)
            break

        default:
            makeVisible(idadditionalchangetextvalue)
            makeInvisible(idadditionalchangewsvalue)
            document.getElementById("idadditionalchangetextvalue").type = 'text'
    }
}

function forminvisible() {

    // books
    makeInvisible(idbooktxt)
    makeInvisible(idbooksel)

    // additional args
    makeInvisible(idadditionaladd)
    makeInvisible(idadditionalread)
    makeInvisible(idadditionalchangetype)
    makeInvisible(idadditionalchangetextvalue)
    makeInvisible(idadditionalchangewsvalue)
    makeInvisible(idadditionalnewpage)
    makeInvisible(idadditionalnewlink)
    makeInvisible(idadditionalnewws)
    makeInvisible(idadditionalnewlr)
    makeInvisible(idadditionalwaitwu)

    // display
    makeInvisible(iddisplayargs)
    makeInvisible(idordersort)
    makeInvisible(idordergroup)
    makeInvisible(idordersortdirection)
    makeInvisible(idordergroupdirection)
}

function makeVisible(element) {
    element.style.display = "block"
}

function makeInvisible(element) {
    element.style.display = "none"
}