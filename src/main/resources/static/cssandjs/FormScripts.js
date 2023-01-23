let visible = false


// eventlisteners
operatorupdate()
setdisplayall()
$('#operator').change(operatorupdate)
$('#filtersbtn').click(togglefilters)
$('#resetfiltersbtn').click(resetfilters)
$('#submitbtn').click(submit)
$('#submitsecretbtn').click(secret)
$('#displayall').click(displayall)
$('.display').click(setdisplayall)


function hidefilters() {
    $('#filtersdiv').hide()
    $('#resetfiltersbtn').hide()
    $('#filtersbtn').removeClass('active')
    visible = false
}

function showfilters() {
    $('#filtersdiv').show()
    $('#resetfiltersbtn').show()
    $('#filtersbtn').addClass('active')
    visible = true
}

function togglefilters() {
    if (visible) hidefilters()
    else showfilters()
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

function setdisplayall() {
    if ($(".display:checked").length === $(".display").length) {
        $('#displayall').prop('checked', true)
    } else {
        $('#displayall').prop('checked', false)
    }
}

function operatorupdate() {
    let selectedOp = $('#operator').val()

    forminvisible()

    if (selectedOp === 'ReadTo') makeVisible('read')
    else if (selectedOp !== null && selectedOp !== '') makeVisible(selectedOp.toLowerCase())
}

function forminvisible() {
    makeInvisible('new')
    makeInvisible('read')
    // makeInvisible('add')
    makeInvisible('change')
    // makeInvisible('open')
    // makeInvisible('show')
    makeInvisible('list')
    makeInvisible('wait')
    makeInvisible('pause')
    $('#secret').hide()
    $('#filtersdiv').hide()
    $('#resetfiltersbtn').hide()

    if ($('#operator').val() === 'filter') {
        $('.filter').each(function () {
            var value = $(this).val()
            if (value !== '' && value != 0 && value != []) {
                showfilters()
                return false
            }
        })
    } else {
        hidefilters()
    }
}

function makeVisible(clazz) {
    $('.' + clazz).show()
}

function makeInvisible(clazz) {
    $('.' + clazz).hide()
}