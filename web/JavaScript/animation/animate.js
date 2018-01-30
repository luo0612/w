function animate(element, target) {
    clearInterval(element.timer);
    element.timer = setInterval(function () {
        var step = (target - element.offsetTop) / 10;
        step = step > 0 ? Math.ceil(step) : Math.floor(step);
        element.style.top = element.offsetTop + step + "px";
        log(1);//检测是否
        if (Math.abs(target - element.offsetTop) <= Math.abs(step)) {
            element.top = target + "px";
            clearInterval(element.timer);
        }
    }, 25);
}

function log(log) {
    console.log(log);
}

/**
 * 封装自己的scrollTop
 */
function onScrollTop() {
    if (window.pageYOffset != null) {
        //IE9+高版本浏览器
        //因为window.pageYOffset默认的是0 , 所以这里需要进行判断
        return {
            left: window.pageXOffset,
            top: window.pageYOffset
        }
    } else if (document.compatMode === "CSS1Compat") {
        //标准浏览器
        return {
            left: document.documentElement.scrollLeft,
            top: document.documentElement.scrollTop
        }
    }
    //未声明DTD
    return {
        left: document.body.scrollLeft,
        top: document.body.scrollTop
    }
}

/**
 * 呼气屏幕可视区域的宽高
 *
 */
function client() {
    if (window.innerHeight !== undefined) {
        return {
            "width": window.innerWidth,
            "height": window.innerHeight
        }
    } else if (document.compatMode = "CSS1Compat") {
        return {
            "width": document.documentElement.clientWidth,
            "height": document.documentElement.clientHeight
        }
    } else {
        return {
            "width": document.body.clientWidth,
            "height": document.body.clientHeight
        }
    }
}

function show(element) {
    element.style.display = "block";
}

function hide(element) {
    element.style.display = "none";
}