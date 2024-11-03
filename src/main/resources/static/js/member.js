const selectBoxes = document.querySelectorAll('.securityQuestion');

selectBoxes.forEach(selectBox => {
    selectBox.addEventListener('change', function() {
        if (selectBox.value) {
            selectBox.classList.add('selected');
        } else {
            selectBox.classList.remove('selected');
        }
    });

    window.addEventListener('load', function() {
        if (selectBox.value) {
            selectBox.classList.add('selected');
        }
    });
});