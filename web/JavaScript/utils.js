/**
 * console.log工具类
 * @param log
 */
function log(log) {
    console.log(log);
}

/**
 * 获取指定类名的第一个元素
 * @param className
 * @returns {Element}
 */
function getFirstClassElement(className) {
    return document.getElementsByClassName(className)[0];
}

/**
 * 获取指定id的元素
 * @param id
 * @returns {HTMLElement | null}
 */
function getElementById(id) {
    return document.getElementById(id);
}

/**
 * 获取指定tag的第一个元素
 * @param tag
 * @returns {*}
 */
function getFirstTagElement(tag) {
    return document.getElementsByTagName(tag)[0];
}