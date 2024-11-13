document.addEventListener('DOMContentLoaded', init);

function init() {
    initEventListeners();
}

function initEventListeners() {
    window.addEventListener('scroll', () => {
        handleHeaderFixed();
    });
}

function handleHeaderFixed() {
    const header = document.querySelector('header');
    toggleClass(header, window.scrollY > 0, 'fixed');
}

function toggleClass(element, condition, className) {
    if (condition) {
        element.classList.add(className);
    } else {
        element.classList.remove(className);
    }
}