$(document).ready(function() {
    // Animación para barras de progreso
    $('.progress-animate .progress-bar').each(function() {
        var width = $(this).data('width');
        $(this).css('width', width);
    });
    
    // Animación para contadores
    $('.counter').each(function() {
        var $this = $(this);
        var countTo = $this.data('count');
        
        $({countNum: 0}).animate({
            countNum: countTo
        }, {
            duration: 2000,
            easing: 'swing',
            step: function() {
                $this.text(Math.floor(this.countNum));
            },
            complete: function() {
                $this.text(this.countNum);
            }
        });
    });
    
    // Botón volver arriba
    $(window).scroll(function() {
        if ($(this).scrollTop() > 300) {
            $('#back-to-top').addClass('show');
        } else {
            $('#back-to-top').removeClass('show');
        }
    });
    
    $('#back-to-top').click(function(e) {
        e.preventDefault();
        $('html, body').animate({scrollTop: 0}, 500);
    });
});