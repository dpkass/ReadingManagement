$('.custom-file-input').change(filechange)

function filechange() {
    var fileName = $(this).val().split('\\').pop()
    $(this).next('.custom-file-label').html(fileName)
}