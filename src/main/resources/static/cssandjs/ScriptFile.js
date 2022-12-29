$(function togglefilters() {
    let visible = false
    let filters = $('#filtersdiv')
    let filtersbtn = document.getElementById('filtersbtn')
    let resetfiltersbtn = $('#resetfiltersbtn')
    $('#filtersbtn').click(function () {
        console.log(1)
        if (visible) {
            filters.hide()
            resetfiltersbtn.hide()
            filtersbtn.classList.remove('active')
        } else {
            filters.show()
            resetfiltersbtn.show()
            filtersbtn.classList.add('active')
        }

        visible = !visible;
    })
})

$(function resetfilters() {
    $('#resetfiltersbtn').click(function () {
        $('input.filter[type=date],input.filter[type=text],select.filter').val('')
        $('input.filter[type=number]').val(0.0)
    })

})

$(function displayall() {
    $('#displayall').click(function () {
        $('.display').prop('checked', this.checked)
    })
})

$(function notdisplayall() {
    $('.display').click(function () {
        if (!this.checked) $('#displayall').prop('checked', this.checked)
    })
})

function operatorupdate() {
    let selectedOp = document.getElementById('operator').value

    forminvisible()

    console.log(selectedOp.toLowerCase())

    if (selectedOp === 'ReadTo') makeVisible('read')
    else if (selectedOp !== '') makeVisible(selectedOp.toLowerCase())

    if (selectedOp === 'Change') changeupdate()
}

function changeupdate() {
    let selectedCh = $('#additionalchangeattribute').val()

    if (selectedCh === 'writingStatus') {
        $('#additionalchangetextvaluediv').hide()
        $('#additionalchangewsvaluediv').show()
    } else {
        $('#additionalchangetextvaluediv').show()
        $('#additionalchangewsvaluediv').hide()
        if (selectedCh === 'rating') document.getElementById('additionalchangetextvalue').type = 'number'
        else document.getElementById('additionalchangetextvalue').type = 'text'
    }
}

function forminvisible() {
    makeInvisible('new')
    makeInvisible('read')
    makeInvisible('add')
    makeInvisible('change')
    makeInvisible('open')
    makeInvisible('show')
    makeInvisible('list')
    makeInvisible('wait')
    makeInvisible('pause')
    $('#filtersdiv').hide()
    $('#resetfiltersbtn').hide()
}

function makeVisible(clazz) {
    $('.' + clazz).show()
}

function makeInvisible(clazz) {
    $('.' + clazz).hide()
}