$(function () {
    var duration = 3000;
    setTimeout(function () { $('#mainAlertMessage').hide(); }, duration);
});

$(function () {
    $('[data-toggle="popover"]').popover()
})

jQuery(document).ready(function($) {
    $(".clickable-row").click(function() {
        window.location = $(this).data("href");
    });
});
