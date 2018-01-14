function onScrpll() {

    if (window.pageYOffset !== undefined) { //IE9+高版本浏览器
        return {
            left: window.pageXOffset,
            top: window.pageYOffset
        }
    } else if (document.compatMode === "CSS1Compat") {//标准浏览器
        return {
            left: document.documentElement.scrollLeft,
            top: document.documentElement.scrollTop
        }
    }
    return { //未声明DTD
        left: document.body.scrollLeft,
        top: document.body.scrollTop
    }
}

function animate(element, target) {
    clearInterval(element.timer);
    element.timer = setInterval(function () {
        var step = (target - element.offsetLeft) / 10;
        step = step > 0 ? Math.ceil(step) : Math.floor(step);
        element.style.left = element.offsetLeft + step + "px";
        console.log(1);
        if (Math.abs(target - element.offsetLeft) <= Math.abs(step)) {
            element.style.left = target + "px";
            clearInterval(element.timer);
        }
    }, 18);
}