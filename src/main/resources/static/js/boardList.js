document.addEventListener('DOMContentLoaded', function() {
    const searchBox = document.querySelector(".search_box");

    searchBox.querySelector("input").addEventListener("focus", () => {
        searchBox.classList.add("on");
    });

    searchBox.querySelector("input").addEventListener("blur", () => {
        searchBox.classList.remove("on");
    });
});
