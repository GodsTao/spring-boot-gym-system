$(function() {

    //轮播图代码
    var imgCount = 5;
    var index = 1;
    var intervalId;
    var buttonSpan = $('.pointer')[0].children;

    //设置定时器定时轮播
    function auto() {
        intervalId = setInterval(function () {
            nextPage(true);
        }, 3000);
    }
    auto();

    $(".slide").mouseover(function () {
        clearInterval(intervalId);
    });


    $(".slide").mouseout(function () {
        auto();
    });
    //点击下一页 上一页的功能
    $('.left').click(function () {
        nextPage(false);
    });
    $('.right').click(function () {
        nextPage(true);
    });

    clickButtons();

    function clickButtons() {
        var length = buttonSpan.length;
        for (var i = 0; i < length; i++) {
            buttonSpan[i].onclick = function () {
                $(buttonSpan[index - 1]).removeClass('selected');
                if ($(this).attr('index') == 1) {
                    index = 5;
                } else {
                    index = $(this).attr('index') - 1;
                }
                nextPage(true);
            };
        }
    }

    //轮播
    function nextPage(next) {
        var targetLeft = 0;
        //当前的圆点去掉on样式
        $(buttonSpan[index - 1]).removeClass('selected');
        if (next) {//往后走
            if (index == 5) {//到最后一张，直接跳到第一张
                targetLeft = 0;
                index = 1;
            } else {
                index++;
                targetLeft = -730 * (index - 1);
            }

        } else {//往前走
            if (index == 1) {//在第一张，直接跳到第五张
                index = 5;
                targetLeft = -730 * (imgCount - 1);
            } else {
                index--;
                targetLeft = -730 * (index - 1);
            }
        }
        //动画
        $('.img-list').animate({left: targetLeft + 'px'});
        $('.slide-text').animate({left:targetLeft+'px'});
        //更新后的圆点加上样式
        $(buttonSpan[index - 1]).addClass('selected');
    }
});