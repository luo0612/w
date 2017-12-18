/**
 * 获取document中指定id的元素
 * @param id
 * @returns {HTMLElement | null}
 */
function getElement(id) {
    return document.getElementById(id);
}

/**
 * 获取指定元素的第一个子节点元素
 * @param element
 */
function getFirstNode(element) {
    var node = element.firstElementChild || element.firstChild;
    return node;
}

/**
 * 获取指定元素的最后一个子节点
 * @param element
 */
function getLastNode(element) {
    var node = element.lastElementChild || element.lastChild;
    return node;
}

/**
 * 获取指定元素的下一个元素兄弟节点
 * @param element
 */
function getNextNode(element) {
    var node = element.nextElementSibling || element.nextSibling;
    return node;
}

/**
 * 获取指定元素的上一个元素兄弟节点
 * @param element
 * @returns {Element | Node}
 */
function getPrevNode(element) {
    var node = element.previousElementSibling || element.previousNode();
    return node;
}

/**
 * 获取指定元素的指定索引的兄弟元素节点
 * @param element
 * @param index
 * @returns {Element}
 */
function getElementOfIndex(element, index) {
    return element.parentNode.children[index];
}

/**
 * 获取指定元素的其他所有兄弟元素
 * @param element
 */
function getAllSiblings(element) {
//sibling:兄弟姊妹
    var newArr = [];
    var arr = element.parentNode.children;
    for (var i = 0; i < arr.length; i++) {
        if (arr[i] !== element) {
            newArr.push(arr[i]);
        }
    }
    return newArr;
}