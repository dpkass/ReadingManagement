let visible = false


// eventlisteners
operatorupdate()
$('#operator').change(operatorupdate)
$('#additionalchangeattribute').change(changeupdate)
$('#filtersbtn').click(togglefilters)
$('#resetfiltersbtn').click(resetfilters)
$('#submitbtn').click(submit)
$('#submitsecretbtn').click(secret)
$('#displayall').click(displayall)
$('.display').click(notdisplayall)


function togglefilters() {
    if (visible) {
        $('#filtersdiv').hide()
        $('#resetfiltersbtn').hide()
        $('#filtersbtn').removeClass('active')
    } else {
        $('#filtersdiv').show()
        $('#resetfiltersbtn').show()
        $('#filtersbtn').addClass('active')
    }
    visible = !visible
}

function resetfilters() {
    $('input.filter[type=date],input.filter[type=text],select.filter').val('')
    $('input.filter[type=number]').val(0.0)
}

function submit() {
    $('#secret').prop('checked', false)
}

function secret() {
    $('#secret').prop('checked', true)
}

function displayall() {
    $('.display').prop('checked', this.checked)
}

function notdisplayall() {
    if (!$(this).checked) {
        $('#displayall').prop('checked', this.checked)
    }
}

function operatorupdate() {
    let selectedOp = $('#operator').val()

    forminvisible()

    if (selectedOp === 'ReadTo') makeVisible('read')
    else if (selectedOp !== null && selectedOp !== '') makeVisible(selectedOp.toLowerCase())

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
    $('#secret').hide()
    $('#filtersdiv').hide()
    $('#resetfiltersbtn').hide()

    $('.filter').each(function () {
        var value = $(this).val()
        if (value !== '' && value != 0 && value != []) {
            if (!visible) togglefilters()
        }
    })
}

function makeVisible(clazz) {
    $('.' + clazz).show()
}

function makeInvisible(clazz) {
    $('.' + clazz).hide()
}